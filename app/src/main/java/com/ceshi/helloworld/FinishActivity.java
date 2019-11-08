package com.ceshi.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.CommonData;


public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finishpay);  //设置页面


        //进入之后设置界面的总金额
        TextView totalmoney=findViewById(R.id.totalmoney);

        totalmoney.setText("￥"+String.valueOf(CommonData.orderInfo.totalPrice));



        //倒计时30s立即返回到 首界面






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


}
