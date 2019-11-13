package com.ceshi.helloworld;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ceshi.helloworld.bean.ReturnXMLParser;
import com.ceshi.helloworld.bean.XMLParseEntity;
import com.ceshi.helloworld.net.CommonData;
import com.ceshi.helloworld.net.GenerateXMLData;
import com.ceshi.helloworld.net.RetrofitHelper;
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
import okhttp3.FormBody;
import okhttp3.MediaType;
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

    private retrofit2.Call<String> XMLParseEntityCall;

    private String mAuthInfo;   //获取的个人用户信息
    private String openid;

    private  String out_tradno="1999999919191991919999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        //selfgetAuthInfo("1111111111111111111111111111111111111111111111");


        //查找返回图片的id,设置点击事件

        findViewById(R.id.faceinit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //微信刷脸初始化

                Map<String, String> m1 = new HashMap<>();
                //m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
                //m1.put("port", "8888");//若没有代理,则不需要此行*/
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

                            String rawdata = info.get("rawdata").toString();

                            try {
                                selfgetAuthInfo(rawdata);

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

        findViewById(R.id.getuserinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> m1 = new HashMap<String, String>();
                m1.put("appid", CommonData.appId); // 公众号，必填
                m1.put("mch_id", CommonData.mch_id); // 商户号，必填
                m1.put("store_id", CommonData.khid); // 门店编号，必填
                m1.put("face_authtype", "FACEID-ONCE"); // 人脸识别模式， FACEID-ONCE`: 人脸识别(单次模式) FACEID-LOOP`: 人脸识别(循环模式), 必填
                m1.put("authinfo", mAuthInfo); // 调用凭证，详见上方的接口参数
                //m1.put("out_trade_no", "" + (System.currentTimeMillis() / 100000));
                //m1.put("total_fee", "100");

                WxPayFace.getInstance().getWxpayfaceUserInfo(m1, new IWxPayfaceCallback() {
                    @Override
                    public void response(Map info) throws RemoteException {
                        if (info == null) {
                            new RuntimeException("调用返回为空").printStackTrace();
                            return;
                        }
                        String code = (String) info.get("return_code"); // 错误码
                        String msg = (String) info.get("return_msg"); // 错误码描述
                        if (code.equals("SUCCESS")) {

                            openid = info.get("openid").toString(); // openid

                            String sub_openid = "";
                            if (info.get("sub_openid") != null)
                                sub_openid = info.get("sub_openid").toString(); // 子商户号下的openid(服务商模式)
                            String nickName = info.get("nickname").toString(); // 微信昵称
                            if (code == null || openid == null || nickName == null || !code.equals("SUCCESS")) {
                                new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                                return;
                            }
                        }

                    }
                });
            }
        });


        findViewById(R.id.facecard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> m1 = new HashMap<String, String>();
                m1.put("appid", CommonData.appId); // 公众号，必填
                m1.put("mch_id", CommonData.appKey); // 商户号，必填

                m1.put("store_id", CommonData.khid); // 门店编号，必填
//        m1.put("telephone", "用户手机号"); // 用户手机号，用于传递会员手机号到界面输入栏，非必填
//        m1.put("openid", "用户openid"); // 用户openid，用于快捷支付模式，非必填
                m1.put("out_trade_no", out_tradno); // 商户订单号， 必填
                m1.put("total_fee", "100"); // 订单金额（数字），单位：分，必填
                m1.put("face_authtype", "FACEPAY"); // FACEPAY：人脸凭证，常用于人脸支付    FACEPAY_DELAY：延迟支付   必填
                m1.put("ask_face_permit", "0"); // 展开人脸识别授权项，详情见上方接口参数，必填
//        m1.put("ask_ret_page", "0"); // 是否展示微信支付成功页，可选值："0"，不展示；"1"，展示，非必填

                WxPayFace.getInstance().getWxpayfaceCode(m1, new IWxPayfaceCallback() {
                    @Override
                    public void response(final Map info) throws RemoteException {
                        if (info == null) {
                            new RuntimeException("调用返回为空").printStackTrace();
                            return;
                        }
                        String code = (String) info.get("return_code"); // 错误码
                        String msg = (String) info.get("return_msg"); // 错误码描述
                        String faceCode = info.get("face_code").toString(); // 人脸凭证，用于刷脸支付
                        openid = info.get("openid").toString(); // openid
                        String sub_openid = ""; // 子商户号下的openid(服务商模式)
                        int telephone_used = 0; // 获取的`face_code`，是否使用了请求参数中的`telephone`
                        int underage_state = 0; // 用户年龄信息（需联系微信支付开通权限）
                        if (info.get("sub_openid") != null) sub_openid = info.get("sub_openid").toString();
                        if (info.get("telephone_used") != null) telephone_used = Integer.parseInt(info.get("telephone_used").toString());
                        if (info.get("underage_state") != null) underage_state = Integer.parseInt(info.get("underage_state").toString());
                        if (code == null || faceCode == null || openid == null || !code.equals("SUCCESS")) {
                            new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                            return ;
                        }
       	        /*
       	        在这里处理您自己的业务逻辑
       	        解释：您在上述中已经获得了支付凭证或者用户的信息，您可以使用这些信息通过调用支付接口来完成支付的业务逻辑
       	        需要注意的是：
       	            1、上述注释中的内容并非是一定会返回的，它们是否返回取决于相应的条件
       	            2、当您确保要解开上述注释的时候，请您做好空指针的判断，不建议直接调用
       	         */

                        handleFaceNotice(faceCode);

                    }
                });
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
        String noncer= GenerateXMLData.GenerateNonceStr();

      /*  GenerateXMLData.AddNewvalue("nonce_str",noncer);
        GenerateXMLData.AddNewvalue("store_id", CommonData.khid);
        GenerateXMLData.AddNewvalue("store_name",CommonData.machine_name);
        GenerateXMLData.AddNewvalue("device_id",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("rawdata",rawdata);
        GenerateXMLData.AddNewvalue("appid",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("mch_id",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("sub_mch_id","MD5");
        GenerateXMLData.AddNewvalue("now",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("version",1);
        GenerateXMLData.AddNewvalue("sign_type","MD5");*/


        GenerateXMLData.treeMap.clear();
        GenerateXMLData.AddNewvalue("nonce_str",noncer);
        GenerateXMLData.AddNewvalue("store_id", CommonData.khid);
        GenerateXMLData.AddNewvalue("store_name",CommonData.machine_name);
        GenerateXMLData.AddNewvalue("device_id",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("rawdata",rawdata);
        GenerateXMLData.AddNewvalue("appid",CommonData.appId);
        GenerateXMLData.AddNewvalue("mch_id",CommonData.mch_id);
        GenerateXMLData.AddNewvalue("sub_mch_id",CommonData.sub_mch_id);
        GenerateXMLData.AddNewvalue("now",GenerateXMLData.GeneratetimeStamp());
        GenerateXMLData.AddNewvalue("version",1);
        GenerateXMLData.AddNewvalue("sign_type","MD5");
        //然后计算签名
        String  sign=GenerateXMLData.GetSign();

        GenerateXMLData.AddNewvalue("sign",sign);

        String  XML=GenerateXMLData.ToXml();

        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"),XML);

            Request request = new Request.Builder()
                    .url("https://payapp.weixin.qq.com/face/get_wxpayface_authinfo")
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
                                if (mAuthInfo.length()>200){
                                    ToastUtil.showToast(OneActivity.this, "温馨提示", "接口凭证authinfo获取成功");

                                }

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



    //进行人脸结果确认识别部分
    private   void  handleFaceNotice(String facecode){

        GenerateXMLData.treeMap.clear();
        GenerateXMLData.AddNewvalue("appid", CommonData.appId);
        GenerateXMLData.AddNewvalue("sub_appid",CommonData.sub_appId);
        GenerateXMLData.AddNewvalue("mch_id",CommonData.mch_id);
        GenerateXMLData.AddNewvalue("sub_mch_id",CommonData.sub_mch_id);
        GenerateXMLData.AddNewvalue("device_info",CommonData.machine_number);
        GenerateXMLData.AddNewvalue("nonce_str",GenerateXMLData.GenerateNonceStr());

        GenerateXMLData.AddNewvalue("body","基础测试信息");
        GenerateXMLData.AddNewvalue("detail","");//可不传
        GenerateXMLData.AddNewvalue("out_trade_no",out_tradno);
        GenerateXMLData.AddNewvalue("total_fee","100");
        GenerateXMLData.AddNewvalue("spbill_create_ip","192.168.1.1");  //机器IP
        GenerateXMLData.AddNewvalue("total_fee","100");
        GenerateXMLData.AddNewvalue("openid",openid);
        GenerateXMLData.AddNewvalue("face_code",facecode);

        String  sign=GenerateXMLData.GetSign();

        GenerateXMLData.AddNewvalue("sign",sign);

        String  XML=GenerateXMLData.SecToXml();


        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"),XML);

            Request request = new Request.Builder()
                    .url("https://api.mch.weixin.qq.com/pay/facepay")
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
                                if (mAuthInfo.length()<100){
                                    ToastUtil.showToast(OneActivity.this, "温馨提示", mAuthInfo);
                                    return;
                                }

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
}
