package com.ceshi.helloworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 1.学习基本布局和控件
 *
 * */
public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        //查找返回图片的id,设置点击事件

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭页面
                finish();
            }
        });







    }
}
