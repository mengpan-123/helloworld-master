package com.ceshi.helloworld;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.RequestSignBean;
import com.ceshi.helloworld.bean.ResponseSignBean;
import com.ceshi.helloworld.bean.getWXFacepayAuthInfo;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.SplnfoList;
import com.ceshi.helloworld.net.ToastUtil;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payexeamActivity extends AppCompatActivity {


    private Boolean isPay=true;
    private String payWay = "";

    private String sPayTypeExt = "";
    //支付识别码
    private String payAuthCode = "";

    private Call<ResponseSignBean> ResponseSignBeanCall;
    private Call<getWXFacepayAuthInfo> getWXFacepayAuthInfoCall;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payexam);  //设置页面

        //接收到传过来的支付方式类型
        Intent intent = getIntent();                 //获取Intent对象 07
        Bundle bundle = intent.getExtras();

        try {
            CommonData.player.reset();
            CommonData.player=MediaPlayer.create(this,R.raw.pay);
            CommonData.player.start();
            CommonData.player.setLooping(false);


            //设置底部的显示信息
            TextView totaldisc=findViewById(R.id.totalcount);
            totaldisc.setText("数量合计 ："+CommonData.orderInfo.totalCount);

            TextView totalpay=findViewById(R.id.totalpay);
            totalpay.setText("金额合计 ："+CommonData.orderInfo.totalPrice);


            TextView storename=findViewById(R.id.storename);
            storename.setText(CommonData.machine_name);

            payWay = bundle.getString("payWay");
        }
        catch (Exception ex) {
            //因为有可能是 轮询支付结果的时候失败了走过来，是没有参数的
            payWay = "很抱歉，用户支付失败，请重试！";
        }

        CommonData.usepayway=payWay;
        Resources res = getResources();

        if (payWay.equals("WXPaymentCodePay")){
            Drawable d=res.getDrawable(R.mipmap.wxforexe);
            findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);

        }
        else
        {
            Drawable d=res.getDrawable(R.mipmap.aliapyexam);
            findViewById(R.id.wxuseinfo).setBackgroundDrawable(d);
        }

        //返回事件
        findViewById(R.id.returnchoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payexeamActivity.this, payWayActivity.class);

                startActivity(intent);
            }
        });

        findViewById(R.id.returnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payexeamActivity.this, CarItemsActicity.class);

                startActivity(intent);
            }
        });

    }

    //增加扫码枪的事件，检测是否录入了支付码
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            payAuthCode += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!TextUtils.isEmpty(payAuthCode)) {
                if (payAuthCode.contains("\n")) {
                    payAuthCode = payAuthCode.substring(0, payAuthCode.length() - 1);
                }
                //开始支付了的部分
                Moneypay();

                payAuthCode = "";
            }

        }
        return true;

    }



    /**
     * Moneypay
     * Created by zhoupan on 2019/11/8.
     * 选择支付方式之后进行支付
     */

    public void Moneypay() {


        //默认值为 true，表示  可以支付了 ，只有为 true 的时候才可以进行支付
        if (!isPay){
            return;
        }

        isPay=false;   //说明 已经开始支付了，避免一次 扫码扫到连个触发两次

        //1.0 初始化所有的产品信息,
        List<RequestSignBean.PluMapBean> pluMap = new ArrayList<RequestSignBean.PluMapBean>();

        try {

            for (Map.Entry<String, List<SplnfoList>> entry : CommonData.orderInfo.spList.entrySet()) {

                RequestSignBean.PluMapBean payMapcls = new RequestSignBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getRealPrice()));  //单项实付金额
                payMapcls.setPluPrice(Double.valueOf(entry.getValue().get(0).getMainPrice()));  //单品单价
                payMapcls.setPluDis(entry.getValue().get(0).getTotaldisc());  //单品优惠
                payMapcls.setPluAmount(Double.valueOf(entry.getValue().get(0).getRealPrice()));   //单项小计
                pluMap.add(payMapcls);
            }
        } catch (Exception ex) {

        }

        //2.0 选取支付方式 ,初始化支付信息
        int PayTypeId = 1;

        List<RequestSignBean.PayMapBean> payMap = new ArrayList<RequestSignBean.PayMapBean>();
        RequestSignBean.PayMapBean pmp = new RequestSignBean.PayMapBean();
        pmp.setPayTypeId(PayTypeId);
        pmp.setPayVal(Double.valueOf(CommonData.orderInfo.totalPrice));
        payMap.add(pmp);

        //调用确认支付接口
        ResponseSignBeanCall = RetrofitHelper.getInstance().getSign(payWay, payAuthCode, sPayTypeExt,"","",pluMap, payMap);
        ResponseSignBeanCall.enqueue(new Callback<ResponseSignBean>() {
            @Override
            public void onResponse(Call<ResponseSignBean> call, Response<ResponseSignBean> response) {
                isPay=true;  //只有当这一笔支付完成 之后  才可以重新进行支付

                if (response != null) {

                    ResponseSignBean body = response.body();

                    if (body.getReturnX().getNCode() == 0) {

                        ResponseSignBean.ResponseBean response1 = body.getResponse();
                        //说明当前支付时成功的，跳转到 支付等待界面
                        Intent intent = new Intent(payexeamActivity.this, WaitingFinishActivity.class);

                        startActivity(intent);

                    } else {
                        //Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrText(),Toast.LENGTH_SHORT).show();
                        String Result = body.getReturnX().getStrText();

                        //66是等待用户输入密码的过程。也会跳转到支付等待界面
                        if ((Result.equals("支付等待") && body.getReturnX().getNCode() == 1) ||
                                body.getReturnX().getNCode() == 66) {

                            Intent intent = new Intent(payexeamActivity.this, WaitingFinishActivity.class);
                            startActivity(intent);
                            return;
                        }
                        ToastUtil.showToast(payexeamActivity.this, "支付通知", Result);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignBean> call, Throwable t) {

                isPay=true;  //只有当这一笔支付完成 之后  才可以重新进行支付
            }
        });

    }



}
