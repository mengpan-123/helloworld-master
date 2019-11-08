package com.ceshi.helloworld.net;

import android.app.Application;

import java.util.List;


//需要将该类型 配置到 清单文件中
public  class CommonData  extends Application {

    public static String    CommonUrl="http://dvp.rongxwy.com/";


    //自定义一个全局变量，作为表名称，多个地方使用
    public static String    tablename="BaseInfo";

    //设备号deviceId
    public static String    machine_number="";

    //门店编号
    public static String    khid="";


    //门店注册登陆之后 返回的 userid，用于 支付时使用
    public static String    userId="526374";

    //商户编号，是由接口返回的
    public static String    lCorpId="";

    //机器名称
    public static String    machine_name="";

    //微信支付商户号
    public static String    mch_id="";

    //微信支付子商户号
    public static String    sub_mch_id="";


    //程序版本号
    public static String    app_version="";


    public static HyMessage  hyMessage=null;


    public static OrderInfo orderInfo=null;
}
