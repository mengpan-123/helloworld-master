package com.ceshi.helloworld.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpSendRequest {


    /// <summary>
    /// 请求地址
    /// </summary>
    private String httpRequestUrl = "";

    /// <summary>
    /// 内容类型,默认为application/x-www-form-urlencoded
    /// </summary>
    private String httpCONTENTTYPE = "";


    /// <summary>
    /// 请求方式的参数
    /// </summary>
    private String httpRequestMethod = "";

    /// <summary>
    /// 请求参数体
    /// </summary>
    private String httpRequestParams = "";

    /// <summary>
    /// 请求参数体
    /// </summary>
    private String httpRequestCharset = "";


    public HttpSendRequest(String httpRequestUrl, String httpCONTENTTYPE, String httpRequestMethod, String httpRequestParams)
    {
        this.httpRequestUrl = httpRequestUrl;
        this.httpCONTENTTYPE = httpCONTENTTYPE;
        this.httpRequestMethod = httpRequestMethod;
        this.httpRequestParams = httpRequestParams;

    }

    public HttpSendRequest(String httpRequestUrl, String httpRequestMethod, String httpRequestParams)
    {
        this.httpRequestUrl = httpRequestUrl;
        this.httpCONTENTTYPE = "application/x-www-form-urlencoded";
        this.httpRequestMethod = httpRequestMethod;
        this.httpRequestParams = httpRequestParams;

    }

    public  String   SendRequest() {

        URL url ;
        String result="";
        try {
            url = new URL(this.httpRequestUrl);
            //创建URL对象
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod(this.httpRequestMethod);                                //指定使用POST请求方式
            urlConn.setDoInput(true);                                       //向连接中写入数据
            urlConn.setDoOutput(true);                                      //从连接中读取数据
            urlConn.setUseCaches(false);                                    //禁止缓存
            urlConn.setInstanceFollowRedirects(true);                   //自动执行HTTP重定向
            urlConn.setRequestProperty("Content-Type", this.httpCONTENTTYPE);  //设置内容类型
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());   //获取输出流

            //byte[]  buffers=new byte[1024];

            //ByteArrayInputStream out=new  ByteArrayInputStream();

            String param = this.httpRequestParams;

            out.writeBytes(param);
            out.flush();
            out.close();

            //byte[] bypes = this.httpRequestParams.getBytes("UTF-8");
            //urlConn.getOutputStream().write(bypes);

            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {    //判断是否响应成功
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());  //获取读取的内容
                BufferedReader buffer = new BufferedReader(in);           //获取输入流对象
                String inputLine = null;

                while ((inputLine = buffer.readLine()) != null) {
                    result += inputLine;
                }
                in.close();                                             //关闭字符输入流
            }
            urlConn.disconnect();                                       //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  result;
    }



    public  String  NewSendRequest() {

        //创建OkHttpClient 对象
        String res = "";
        OkHttpClient okHttpClient = new OkHttpClient(); //Form表单格式的参数传递
        FormBody formBody = new FormBody.Builder().add("inputId", "111").add("deviceId","111").build();


        Request request = new Request.Builder().url(this.httpRequestUrl).post(formBody).build();
        try { //创建请求响应
            Response response = okHttpClient.newCall(request).execute();
            res = response.body().string();    //获取响应资源
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
