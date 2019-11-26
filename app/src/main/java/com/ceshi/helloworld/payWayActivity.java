package com.ceshi.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ceshi.helloworld.net.CommonData;

public class payWayActivity  extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payway);


        //设置底部的显示信息
        TextView totaldisc=findViewById(R.id.totaldisc);
        totaldisc.setText(CommonData.orderInfo.totalDisc);

        TextView totalpay=findViewById(R.id.totalpay);
        totalpay.setText(CommonData.orderInfo.totalPrice);




    }

}
