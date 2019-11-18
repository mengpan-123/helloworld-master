package com.ceshi.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.CommonData;

import java.io.Serializable;

public class PayFailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payfail);


      Intent intent = getIntent();                 //获取Intent对象 07
        Bundle bundle = intent.getExtras();

      String  reason =bundle.getString("reason");


        TextView edit= findViewById(R.id.ErrReason);

        edit.setText(reason);




        findViewById(R.id.newtopay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PayFailActivity.this, InputGoodsActivity.class);


                startActivity(intent);

            }
        });

    }
}
