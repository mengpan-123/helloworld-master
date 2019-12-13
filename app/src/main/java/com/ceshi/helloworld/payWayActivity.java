package com.ceshi.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class payWayActivity  extends Activity {

    //支付方式相关的
    private Boolean isPay=true;
    private String payWay = "";

    private String sPayTypeExt = "";
    //支付识别码
    private String payAuthCode = "";

    private String mAuthInfo;   //刷脸支付 获取的个人授权信息，从接口获取
    private String openid;      //刷脸支付获取微信openid
    private String out_trade_no;   //获取的交易流水号，用于刷脸支付
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    private String responseAppid;
    private String responnse_subAppid;
    private String responsemachid;
    private String responnse_submachid;


    //当前添加的产品列表的集合
    private Map<String, List<SplnfoList>> MapList = new HashMap<String, List<SplnfoList>>();

    private Call<ResponseSignBean> ResponseSignBeanCall;
    private Call<getWXFacepayAuthInfo> getWXFacepayAuthInfoCall;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payway);


        //设置底部的显示信息
        TextView totaldisc=findViewById(R.id.totaldisc);
        totaldisc.setText(CommonData.orderInfo.totalDisc);

        TextView totalpay=findViewById(R.id.totalpay);
        totalpay.setText(CommonData.orderInfo.totalPrice);


        CommonData.player.reset();
        CommonData.player=MediaPlayer.create(this,R.raw.paytype);
        CommonData.player.start();
        CommonData.player.setLooping(false);


        ImageView zfb = findViewById(R.id.zfb);
        ImageView wx = findViewById(R.id.wx);
        ImageView wx_face = findViewById(R.id.wx_face);

        TextView returnback = findViewById(R.id.returnback);


        zfb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                payWay = "AliPaymentCodePay";
                sPayTypeExt="devicealimicropay";

                Intent intent = new Intent(payWayActivity.this, payexeamActivity.class);
                Bundle bundle = new Bundle();

                bundle.putCharSequence("payWay",payWay);

                intent.putExtras(bundle);
                startActivity(intent);

           }
       });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payWay = "WXPaymentCodePay";
                sPayTypeExt="devicewxmicropay";

                /*CommonData.player.reset();
                CommonData.player=MediaPlayer.create(payWayActivity.this,R.raw.wxpay);
                CommonData.player.start();
                CommonData.player.setLooping(false);*/
                Intent intent = new Intent(payWayActivity.this, payexeamActivity.class);
                Bundle bundle = new Bundle();

                bundle.putCharSequence("payWay",payWay);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        wx_face.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                sPayTypeExt="devicewxface";
                payWay = "WXFacePay";

                CommonData.usepayway="WXFacePay";
                wxFacepay();
            }
        });



        //返回上一页

        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payWayActivity.this, CarItemsActicity.class);

                startActivity(intent);
            }
        });
    }

    //刷脸支付初始化
    public  void wxFacepay(){
        Map<String, String> m1 = new HashMap<>();
        //m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
        //m1.put("port", "8888");//若没有代理,则不需要此行*/
        try {
            //1.0 刷脸支付初始化成功
            WxPayFace.getInstance().initWxpayface(payWayActivity.this, m1, new IWxPayfaceCallback() {
                @Override
                public void response(Map info) throws RemoteException {
                    if (!isSuccessInfo(info)) {
                        return;
                    }

                    CommonData.player.reset();
                    CommonData.player= MediaPlayer.create(payWayActivity.this,R.raw.facepay);
                    CommonData.player.start();
                    CommonData.player.setLooping(false);

                    // 2.0微信刷脸  获取rawdata和AuthInfo 数据
                    try {
                        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (!isSuccessInfo(info)) {
                                    ToastUtil.showToast(payWayActivity.this, "接口获取失败", "当前调用微信SDK获取rawdata异常");
                                    return;
                                }

                                String rawdata = info.get("rawdata").toString();
                                try {
                                    //selfgetAuthInfo(rawdata);

                                    InterfaceGetAuthInfo(rawdata);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtil.showToast(payWayActivity.this, "温馨提示", "当前调用接口获取设备认证信息异常");
                                }
                            }
                        });
                    }
                    catch(Exception ex){
                        ToastUtil.showToast(payWayActivity.this, "接口获取失败", "当前系统未安装微信刷脸程序");
                    }

                }
            });
        }
        catch(Exception ex){
            ToastUtil.showToast(payWayActivity.this, "温馨提示", "当前系统未安装微信刷脸程序");
        }
    }



    private boolean isSuccessInfo(Map info) {
        if (info == null) {

            ToastUtil.showToast(payWayActivity.this, "温馨提示", "调用返回为空");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String code = (String) info.get(RETURN_CODE);
        String msg = (String) info.get(RETURN_MSG);


        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
            ToastUtil.showToast(payWayActivity.this, "温馨提示", "调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
            return false;
        }

        return true;
    }


    private void InterfaceGetAuthInfo(String rowdata) {

        getWXFacepayAuthInfoCall = RetrofitHelper.getInstance().getWXFacepayAuthInfo(CommonData.khid, CommonData.userId, rowdata);
        getWXFacepayAuthInfoCall.enqueue(new Callback<getWXFacepayAuthInfo>() {
            @Override
            public void onResponse(Call<getWXFacepayAuthInfo> call, Response<getWXFacepayAuthInfo> response) {

                if (response != null) {
                    getWXFacepayAuthInfo body = response.body();

                    if(null==body) {
                        ToastUtil.showToast(payWayActivity.this, "接口信息获取异常", "接口请求获取设备认证信息获取失败");
                        return;
                    }
                    if (body.getReturnX().getNCode() == 0) {

                        getWXFacepayAuthInfo.ResponseBean response1 = body.getResponse();
                        mAuthInfo = response1.getAuthinfo();

                        if (mAuthInfo == null) {

                            ToastUtil.showToast(payWayActivity.this, "支付通知", "设备认证信息获取失败");
                            return;
                        }
                        out_trade_no=response1.getOut_trade_no();
                        responseAppid=response1.getAppid();
                        responnse_subAppid=response1.getSubAppid();
                        responsemachid=response1.getMch_id();
                        responnse_submachid=response1.getSubMchId();

                        double a = Double.valueOf(CommonData.orderInfo.totalPrice)*10*10;
                        int total_fee= new Double(a).intValue();

                        //3.0 然后调用首先获取  facecode。用于人脸支付
                        Map<String, String> m1 = new HashMap<String, String>();
                        m1.put("appid", responseAppid); // 公众号，必填
                        m1.put("mch_id", responsemachid); // 商户号，必填
                        m1.put("sub_appid", responnse_subAppid); // 商户号，必填
                        m1.put("sub_mch_id", responnse_submachid); // 商户号，必填
                        m1.put("store_id", CommonData.khid); // 门店编号，必填
                        m1.put("out_trade_no", out_trade_no); // 商户订单号， 必填
                        m1.put("total_fee", String.valueOf(total_fee)); // 订单金额（数字），单位：分，必填
                        m1.put("face_authtype", "FACEPAY"); // FACEPAY：人脸凭证，常用于人脸支付    FACEPAY_DELAY：延迟支付   必填
                        m1.put("authinfo", mAuthInfo);
                        m1.put("ask_face_permit", "0"); // 展开人脸识别授权项，详情见上方接口参数，必填
                        m1.put("ask_ret_page", "0"); // 是否展示微信支付成功页，可选值："0"，不展示；"1"，展示，非必填

                        WxPayFace.getInstance().getWxpayfaceCode(m1, new IWxPayfaceCallback() {
                            @Override
                            public void response(final Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述

                                if (!code.equals("SUCCESS")) {

                                    //没有成功需要关掉 人脸支付
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("authinfo", mAuthInfo); // 调用凭证，必填
                                    WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                                        @Override
                                        public void response(Map info) throws RemoteException {
                                            if (info == null) {
                                                new RuntimeException("调用返回为空").printStackTrace();
                                                return;
                                            }
                                            String code = (String) info.get("return_code"); // 错误码
                                            String msg = (String) info.get("return_msg"); // 错误码描述
                                            if (code == null || !code.equals("SUCCESS")) {
                                                new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                                return;
                                            }
                                        }
                                    });
                                }

                                if(code.equals("SUCCESS")) {
                                    String faceCode = info.get("face_code").toString(); // 人脸凭证，用于刷脸支付
                                    openid = info.get("openid").toString(); // 人脸凭证，用于刷脸支付
                                    //在这里处理业务逻辑
                                    payAuthCode = faceCode;//将刷脸支付返回码进行订单调用

                                    wxFaceMoneypay();
                                }
                               /* else if(code.equals("USER_CANCEL")){

                                    WxPayFace.getInstance().releaseWxpayface(InputGoodsActivity.this);
                                    //ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "用户取消了支付，请重试");
                                    return;

                                }*/
                                WxPayFace.getInstance().releaseWxpayface(payWayActivity.this);
                                //ToastUtil.showToast(InputGoodsActivity.this, "支付通知", "用户取消了支付，请重试");
                                return;

                            }
                        });

                    } else {

                        ToastUtil.showToast(payWayActivity.this, "支付通知", body.getReturnX().getStrText());

                    }
                }

            }

            @Override
            public void onFailure(Call<getWXFacepayAuthInfo> call, Throwable t) {

            }
        });


    }


    public void wxFaceMoneypay() {

        //1.0 初始化所有的产品信息,
        List<RequestSignBean.PluMapBean> pluMap = new ArrayList<RequestSignBean.PluMapBean>();
        MapList = CommonData.orderInfo.spList;
        try {

            for (Map.Entry<String, List<SplnfoList>> entry : MapList.entrySet()) {

                RequestSignBean.PluMapBean payMapcls = new RequestSignBean.PluMapBean();
                payMapcls.setBarcode(entry.getValue().get(0).getBarcode());
                payMapcls.setGoodsId(entry.getValue().get(0).getGoodsId());
                payMapcls.setPluQty(entry.getValue().get(0).getPackNum());
                payMapcls.setRealPrice(Double.valueOf(entry.getValue().get(0).getRealPrice()));  //单项实付金额
                payMapcls.setPluPrice(Double.valueOf(entry.getValue().get(0).getMainPrice()));  //单品单价
                payMapcls.setPluDis(Double.valueOf(entry.getValue().get(0).getTotaldisc()));  //单品优惠
                payMapcls.setPluAmount(Double.valueOf(entry.getValue().get(0).getRealPrice()));   //单项小计
                pluMap.add(payMapcls);
            }
        } catch (Exception ex) {
            ToastUtil.showToast(payWayActivity.this, "温馨提示", ex.toString());

        }

        //2.0 选取支付方式 ,初始化支付信息
        int PayTypeId = 1;

        List<RequestSignBean.PayMapBean> payMap = new ArrayList<RequestSignBean.PayMapBean>();
        RequestSignBean.PayMapBean pmp = new RequestSignBean.PayMapBean();
        pmp.setPayTypeId(PayTypeId);
        pmp.setPayVal(Double.valueOf(CommonData.orderInfo.totalPrice));
        payMap.add(pmp);

        //调用确认支付接口
        ResponseSignBeanCall = RetrofitHelper.getInstance().getSign(payWay, payAuthCode, sPayTypeExt,openid,out_trade_no,pluMap, payMap);

        ResponseSignBeanCall.enqueue(new Callback<ResponseSignBean>() {
            @Override
            public void onResponse(Call<ResponseSignBean> call, Response<ResponseSignBean> response) {
                if (response!=null){
                    ResponseSignBean body = response.body();
                    if (payWay.equals("WXFacePay")){

                        /*HashMap<String, String> map = new HashMap<String, String>();
                        map.put("appid", responseAppid); // 公众号，必填
                        map.put("mch_id", responsemachid); // 商户号，必填
                        map.put("store_id", CommonData.khid); // 门店编号，必填
                        map.put("authinfo", mAuthInfo); // 调用凭证，必填
                        map.put("payresult", "SUCCESS"); // 支付结果，SUCCESS:支付成功   ERROR:支付失败   必填
                        WxPayFace.getInstance().updateWxpayfacePayResult(map, new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述
                                if (code == null || !code.equals("SUCCESS")) {
                                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                    return ;
                                }
                            }
                        });*/

                        //没有成功需要关掉 人脸支付
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("authinfo", mAuthInfo); // 调用凭证，必填
                        WxPayFace.getInstance().stopWxpayface(map, new IWxPayfaceCallback() {
                            @Override
                            public void response(Map info) throws RemoteException {
                                if (info == null) {
                                    new RuntimeException("调用返回为空").printStackTrace();
                                    return;
                                }
                                String code = (String) info.get("return_code"); // 错误码
                                String msg = (String) info.get("return_msg"); // 错误码描述
                                if (code == null || !code.equals("SUCCESS")) {
                                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                    return;
                                }
                            }
                        });

                    }
                    if (body.getReturnX().getNCode()==0){

                        ResponseSignBean.ResponseBean response1 = body.getResponse();

                        try {
                            CommonData.ordernumber = response1.getTransId();
                            CommonData.outTransId = response1.getOutTransId();
                            CommonData.get_jf=response1.getNCurrentPoint();
                        }
                        catch(Exception ex){

                        }

                        //说明当前支付时成功的，跳转到 支付等待界面
                        Intent intent = new Intent(payWayActivity.this, WaitingFinishActivity.class);

                        startActivity(intent);
                    }
                    else{
                        //Toast.makeText(InputGoodsActivity.this,body.getReturnX().getStrText(),Toast.LENGTH_SHORT).show();
                        String  Result=body.getReturnX().getStrText();

                        //返回标识 66 是等待用户输入密码的过程。也会跳转到支付等待界面
                        if ((Result.equals("支付等待")&&body.getReturnX().getNCode()==1)||
                                body.getReturnX().getNCode()==66) {

                            try {
                                ResponseSignBean.ResponseBean response1 = body.getResponse();
                                CommonData.ordernumber = response1.getTransId();
                                CommonData.outTransId = response1.getOutTransId();
                                CommonData.get_jf=response1.getNCurrentPoint();
                            }
                            catch(Exception ex){

                            }

                            Intent intent = new Intent(payWayActivity.this, WaitingFinishActivity.class);
                            startActivity(intent);
                            return;
                        }

                        ToastUtil.showToast(payWayActivity.this, "支付通知", Result);

                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignBean> call, Throwable t) {

            }
        });

    }


}
