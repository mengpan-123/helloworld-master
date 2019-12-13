package com.ceshi.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.PaysuccessofdeviceEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingFinishActivity extends AppCompatActivity {

    //支付成功后 等待查询支付界面

    private Call<PaysuccessofdeviceEntity> PaysuccessCall;
    private String  payresult="";
    private  int  i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paywait);  //设置页面


        TextView pay_status=findViewById(R.id.pay_status);



        try {
            new Thread() {
                @Override
                public void run() {
                    //应该是界面先显示，然后再来执行这个轮询。轮询支付界面，最多10次进行等待
                    while (i < 50) {
                        //Thread.sleep(1000);

                        SystemClock.sleep(3000);

                        if (getPayresult().equals("OK")) {
                            Intent intent = new Intent(WaitingFinishActivity.this, FinishActivity.class);
                            startActivity(intent);
                            break;
                        }

                        i++;
                    }

                    if (payresult != "OK") {
                        //说明 支付失败了
                        Intent intent = new Intent(WaitingFinishActivity.this, PayFailActivity.class);
                        startActivity(intent);
                    }
                }
            }.start();

        }
        catch(Exception ex){
            Intent intent = new Intent(WaitingFinishActivity.this, PayFailActivity.class);
            startActivity(intent);
        }




    }


    /**
     * Created by zhoupan on 2019/11/8.
     * 轮询支付结果信息
     * */
    public String getPayresult() {

        PaysuccessCall= RetrofitHelper.getInstance().paysuccessofdevice(CommonData.khid,CommonData.orderInfo.prepayId);
        PaysuccessCall.enqueue(new Callback<PaysuccessofdeviceEntity>() {
            @Override
            public void onResponse(Call<PaysuccessofdeviceEntity> call, Response<PaysuccessofdeviceEntity> response) {

                if (response!=null){

                    PaysuccessofdeviceEntity body = response.body();
                    if (body.getReturnX().getNCode()==0){
                        //跳转到支付等待界面
                        //Intent intent = new Intent(WaitingFinishActivity.this, FinishActivity.class);
                        //startActivity(intent);
                        //payresult="OK";
                        payresult=  "OK";
                    }
                    else
                    {

                        payresult="";
                    }

                }
            }

            @Override
            public void onFailure(Call<PaysuccessofdeviceEntity> call, Throwable t) {

            }

        });


        return payresult;
    }
}


