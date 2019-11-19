package com.ceshi.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PayWaitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paywait);
    }

    /**
     * 返回首页
     */
    public void return_home(View view) {
        Intent intent = new Intent(PayWaitActivity.this, IndexActivity.class);
        startActivity(intent);
    }

}
