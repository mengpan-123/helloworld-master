package com.ceshi.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ceshi.helloworld.bean.OrderDetailEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.RetrofitHelper;
import com.ceshi.helloworld.net.ToastUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPrintActivity   extends  Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newprint);

        //返回上一页
        TextView returnback = findViewById(R.id.returnback);
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPrintActivity.this, NewIndexActivity.class);
                startActivity(intent);
            }
        });

    }


    //扫码枪的回车事件
    String barcode = "";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // ToastUtil.showToast(InputGoodsActivity.this, "商品录入通知", barcode);
            if (!TextUtils.isEmpty(barcode)) {
                if (barcode.contains("\n")) {
                    barcode = barcode.substring(0, barcode.length() - 1);
                }
                //开始打印

                GetPrintStr(barcode);

                barcode = "";
            }

        }
        return true;

    }


    public  void  GetPrintStr(String transId){

        //拿到 订单流水号接口，去请求 查询订单详情接口
        Call<OrderDetailEntity> OrderDetailEntityCall = RetrofitHelper.getInstance().upgetorderbyid(CommonData.userId, CommonData.khid, transId);
        OrderDetailEntityCall.enqueue(new Callback<OrderDetailEntity>() {
            @Override
            public void onResponse(Call<OrderDetailEntity> call, Response<OrderDetailEntity> response) {

                if (response!=null) {
                    OrderDetailEntity body = response.body();
                    if (null != body) {
                        if (body.getReturnX().getNCode()==0) {
                            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String day = form.format(date);

                            String storename = body.getResponse().getStoreName();


                            String Str = "                   欢迎光临                   " + "\n";
                            Str += "门店名称:" + storename + "\n";
                            Str += "流水号：" + body.getResponse().getOutTransId() + "     " + "\n";
                            //Str+="商户订单号："+CommonData.outTransId+"     "+"\n";
                            Str += "日期   " + day + "     " + "\n";
                            Str += "===============================================" + "\n";
                            Str += "条码     名称     数量        单价     金额" + "\n";

                            List<OrderDetailEntity.ResponseBean.PluMapBean> plumap = body.getResponse().getPluMap();

                            for (int sk = 0; sk < plumap.size(); sk++) {

                                String barcode = plumap.get(sk).getBarcode();
                                String sname = plumap.get(sk).getPluName();
                                double qty = 0;
                                double weight = plumap.get(sk).getNWeight();
                                double dj = 0;
                                dj = plumap.get(sk).getPluPrice();
                                if (weight == 0.00) {
                                    //说明重量是 0.  那就取显示数量
                                    qty = plumap.get(sk).getPluQty();

                                } else {
                                    //净重含量存在值 则显示重量
                                    qty = weight;
                                }

                                double zj = plumap.get(sk).getRealAmount();
                                Str += barcode + "\n";
                                Str += sname + "     " + qty + "     " + dj + "    " + zj + "  " + "\n";
                            }

                            //付款方式
                            String paytype = body.getResponse().getPayMap().getPayTypeName();
                            double paynet = body.getResponse().getPayMap().getPayVal();
                            Str += "===============================================" + "\n";
                            Str += "付款方式             金额          总折扣" + "\n";

                            Str += paytype + "             " + paynet + "          " + body.getResponse().getDisAmount() + "\n";

                            Str += "总数量         应收        找零" + "\n";
                            Str += "" + paynet + "             " + body.getResponse().getDisAmount() + "          0.00     " + "\n";

                            Str += "=====================" + body.getResponse().getTrnTime() + "===================" + "\n";
                            Str += "            谢谢惠顾，请妥善保管小票         " + "\n";
                            Str += "               开正式发票，当月有效              " + "\n";

                            usePrint(Str);
                        }
                        else
                        {
                            ToastUtil.showToast(NewPrintActivity.this, "查询失败",body.getReturnX().getStrText());
                        }
                    }
                    else{
                        ToastUtil.showToast(NewPrintActivity.this, "接口访问失败","请检查接口异常");

                    }


                }

            }

            @Override
            public void onFailure(Call<OrderDetailEntity> call, Throwable t) {
                ToastUtil.showToast(NewPrintActivity.this, "接口请求失败","请检查网络异常或接口请求异常");
            }
        });

    }


    public  void  usePrint(String  printstr){
        PrinterAPI mPrinter = PrinterAPI.getInstance();  //打印机部分
        InterfaceAPI io = null;
        io = new SerialAPI(new File(
                "/dev/ttyS1"),
                38400,
                1);
        try {
            if (PrinterAPI.SUCCESS == mPrinter.connect(io)) {

                try {
                    if (PrinterAPI.SUCCESS == mPrinter.printString(printstr, "GBK", true)) {
//                                Thread.sleep(time);
                    } else {
                        ToastUtil.showToast(NewPrintActivity.this, "打印失败","票打印失败,请重试");
//                                handler.sendEmptyMessage(1);
                    }

                    mPrinter.printFeed();
                    //清楚打印缓存区 避免对后面数据的影响 有些设置会恢复默认值
                    mPrinter.init();
                    mPrinter.cutPaper(66, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                    //printstate.setText("小票打印失败,请重试");
                }
            }
        }
        catch(Exception ex){
            ToastUtil.showToast(NewPrintActivity.this, "打印异常通知", "请检查当前打印机连接");
        }
    }

}
