package com.ceshi.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);  //设置页面


       //接受界面的参数

        //Intent intent = getIntent();                 //获取Intent对象
        //Bundle bundle = intent.getExtras();

        //bundle.getString("sname");  //就可以根据键获取参数


        Button tvOne = findViewById(R.id.button);
        //设置点击事件
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {
                //3.跳转页面（显示Internet）
                Intent intent = new Intent(MainActivity.this, ConTrolActivity.class);
                startActivity(intent);


                //Intent hideintent = new Intent();

                //hideintent.setAction(Intent.ACTION_VIEW); //为Intent设置动作，ACTION_VIEW表示将数据显示给用户
               // hideintent.setData(Uri.parse("http://www.mingribook.com"));  //设置数据
               // startActivity(intent);                                  //将Intent传递给Activity

                //主角（Activity）与配角（Intent）

                //finish();
                //关闭当前Activity如果当前的Activity不是主活动，那么执行ﬁnish()后，将返回到调用它的那个Activity；否则，将返回到主屏幕中
                //比如说 登陆完成之后 不需要再次回来的那种，就直接关闭 finish

                //配角：Intent 在这里起着一个媒体中介的作用，专门提供组件间互相调用的相关信息，实现调用者与被调用者之间的解耦
            }
        });
        TextView tv_title = findViewById(R.id.tv_title);
        //设置点击事件
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {
                //3.跳转页面
                Intent intent = new Intent(MainActivity.this, ConTrolActivity.class);
                startActivity(intent);
            }
        });

    }
}
