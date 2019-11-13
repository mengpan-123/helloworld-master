package com.ceshi.helloworld;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.ReturnXMLParser;
import com.ceshi.helloworld.net.ToastUtil;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
//import com.tencent.wxpayface.IWxPayfaceCallback;
//import com.tencent.wxpayface.WxPayFace;
//import com.tencent.wxpayface.WxfacePayCommonCode;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private String mAuthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //查找返回图片的id,设置点击事件

        findViewById(R.id.faceinit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //微信刷脸初始化

                Map<String, String> m1 = new HashMap<>();
                m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
                m1.put("port", "8888");//若没有代理,则不需要此行*/
                try {
                    WxPayFace.getInstance().initWxpayface(OneActivity.this, m1, new IWxPayfaceCallback() {
                        @Override
                        public void response(Map info) throws RemoteException {
                            if (!isSuccessInfo(info)) {
                                return;
                            }
                            ToastUtil.showToast(OneActivity.this, "温馨提示", "微信刷脸支付初始化完成");
                        }
                    });
                }
                catch(Exception ex){
                    ToastUtil.showToast(OneActivity.this, "温馨提示", ex.toString());
                }
            }
        });

        findViewById(R.id.facegetdata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //微信刷脸  获取数据

                try {
                    WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
                        @Override
                        public void response(Map info) throws RemoteException {
                            if (!isSuccessInfo(info)) {
                                return;
                            }
                            Log.d(TAG, "response | getWxpayfaceRawdata" );
                            String rawdata = info.get("rawdata").toString();

                            try {
                                getAuthInfo(rawdata);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch(Exception ex){
                    ToastUtil.showToast(OneActivity.this, "温馨提示", ex.toString());
                }
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


//        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
//            ToastUtil.showToast(OneActivity.this, "温馨提示", "调用返回非成功信息, 请查看日志");
//            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
//            return false;
//        }
        Log.d(TAG, "调用返回成功");
        return true;
    }


    private void getAuthInfo(String rawdata) throws IOException {
        //AuthInfo info =  new AuthInfo();
        Log.d(TAG, "enter | getAuthInfo ");
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();

            RequestBody body = RequestBody.create(null, rawdata);

            Request request = new Request.Builder()
                    .url("https://wxpay.wxutil.com/wxfacepay/api/getWxpayFaceAuthInfo.php")
                    .post(body)
                    .build();

            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG, "onFailure | getAuthInfo " + e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                 mAuthInfo = ReturnXMLParser.parseGetAuthInfoXML(response.body().byteStream());



                                ToastUtil.showToast(OneActivity.this, "温馨提示", "Get authinfo SUCCESS");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "onResponse | getAuthInfo " + mAuthInfo);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    private   void  selfgetAuthInfo(String rawdata){

        //利用 roadata初始化请求参数



    }
}
