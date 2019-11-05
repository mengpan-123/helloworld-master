package com.ceshi.helloworld;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.MyDatabaseHelper;

import java.text.SimpleDateFormat;

public class LoginActivity extends AppCompatActivity {


    private MyDatabaseHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login1);  //设置页面


        //在这里需要检测，是否已经初始化过，即检查dblite是否存在，存在的话，直接跳转到首界面


        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        Button Login = findViewById(R.id.Login);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //查询数据
                SQLiteDatabase querydb = dbHelper.getWritableDatabase();
                Cursor cursor = querydb.query("Book", null, null, null, null, null, null);

                // 查询Book表中所有的数据
                if (cursor.moveToFirst()) {

                    do {
                        // 遍历Cursor对象，取出数据并打印

                        String khid = cursor.getString(cursor.getColumnIndex("khid"));

                        String machine_number = cursor.getString(cursor.getColumnIndex("machine_number"));

                        CommonData.khid=khid;
                        CommonData.machine_number=machine_number;


                    }

                    while (cursor.moveToNext());

                    cursor.close();

                }




                Toast.makeText(LoginActivity.this, "正在点击登录", Toast.LENGTH_SHORT).show();
                dbHelper.getWritableDatabase();

                //创建成功之后进行写数据

               TextView inputmachine = findViewById(R.id.inputmachine);
                TextView  inputkhid= findViewById(R.id.inputkhid);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("khid", inputkhid.getText().toString());
                values.put("machine_number", inputmachine.getText().toString());
                values.put("date_lr", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").toString());
                values.put("name", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").toString());

                db.insert("Book", null, values);
                values.clear();
                //写数据成功之后 ，跳转到主页




                setContentView(R.layout.activity_getphone);  //设置页面*//*

            }
        });


    }

   /* public void  LoginClick(View  view){

        Toast.makeText(LoginActivity.this, "正在点击登录", Toast.LENGTH_SHORT).show();
    }*/
}


