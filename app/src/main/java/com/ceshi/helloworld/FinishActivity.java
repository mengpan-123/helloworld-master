package com.ceshi.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.CommonData;

import java.util.Timer;
import java.util.TimerTask;


public class FinishActivity extends AppCompatActivity  {
    private TextView time;
    private  int i=0;
    private Timer timer=null;
    private TimerTask task=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finishpay);  //设置页面


        //进入之后设置界面的总金额
        TextView totalmoney=findViewById(R.id.totalmoney);

        totalmoney.setText("￥"+String.valueOf(CommonData.orderInfo.totalPrice));



        //倒计时30s立即返回到 首界面

        time=(TextView) findViewById(R.id.tv_time);
        i=Integer.parseInt(time.getText().toString());
        startTime();




        /**
         * Created by zhoupan on 2019/11/8.
         * 点此立即返回到首页
         * */
        TextView comback=findViewById(R.id.comback);


        comback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(FinishActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });

    }

    public void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (i > 0) {   //加入判断不能小于0
                    i--;
                    Message message = mHandler.obtainMessage();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                }else {

                }
            }
        };
        timer.schedule(task, 1000);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            time.setText(msg.arg1 + "");
            startTime();
        };
    };
}
