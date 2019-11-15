package com.ceshi.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeDataActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exchagedata);


        Button passpram = findViewById(R.id.parampass);


        passpram.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {


                /*String  phone = ((EditText) findViewById(R.id.inputphone)).getText().toString();
                String  sname = ((EditText) findViewById(R.id.inputname)).getText().toString();
                String  age = ((EditText) findViewById(R.id.inputage)).getText().toString();

                //第二个参数就是要传递的参数对象
                Intent intent = new Intent(ChangeDataActivity.this, MainActivity.class);
                //创建并实例化一个Bundle对象
                Bundle bundle = new Bundle();
                bundle.putCharSequence("phone", phone);
                bundle.putCharSequence("sname", sname);
                bundle.putCharSequence("age", age);

                intent.putExtras(bundle);     //将Bundle对象添加到Intent对象中
                startActivity(intent);*/
            }
        });
    }
}
