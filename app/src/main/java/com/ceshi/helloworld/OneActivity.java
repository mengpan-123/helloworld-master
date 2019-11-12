package com.ceshi.helloworld;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.net.ToastUtil;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.学习基本布局和控件
 *
 * */
public class OneActivity extends AppCompatActivity {

    public static final String TAG = "OneActivity";

    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_SUCCESS = "SUCCESS";
    public static final String RETURN_FAILE = "SUCCESS";
    public static final String RETURN_MSG = "return_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //查找返回图片的id,设置点击事件

        findViewById(R.id.faceinit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //微信刷脸初始化
              /*  Map<String, String> m1 = new HashMap<>();
//                m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
//                m1.put("port", "8888");//若没有代理,则不需要此行
                WxPayFace.getInstance().initWxpayface(OneActivity.this, m1, new IWxPayfaceCallback() {
                    @Override
                    public void response(Map info) throws RemoteException {
                        if (!isSuccessInfo(info)) {
                            return;
                        }
                        ToastUtil.showToast(OneActivity.this, "温馨提示", "微信刷脸支付初始化完成");
                    }
                });*/
            }
        });







    }


    private boolean isSuccessInfo(Map info) {
        if (info == null) {

            ToastUtil.showToast(OneActivity.this, "温馨提示", "调用返回为空");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String code = (String)info.get(RETURN_CODE);
        String msg = (String)info.get(RETURN_MSG);
        Log.d(TAG, "response | getWxpayfaceRawdata " + code + " | " + msg);


        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
            ToastUtil.showToast(OneActivity.this, "温馨提示", "调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
            return false;
        }
        Log.d(TAG, "调用返回成功");
        return true;
    }
}
