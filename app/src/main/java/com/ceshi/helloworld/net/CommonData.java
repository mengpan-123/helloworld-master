package com.ceshi.helloworld.net;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Build;

import java.util.List;


//需要将该类型 配置到 清单文件中
public  class CommonData  extends Application {

    //public static String    CommonUrl="http://dvp.rongxwy.com/";

    public static String    CommonUrl="http://54.223.249.111:90/";  //新的地址


    //自定义一个全局变量，作为表名称，多个地方使用
    public static String    tablename="BaseInfo2";

    //设备号deviceId
    public static String    machine_number= Build.SERIAL;

    //接口返回的门店编号
    public static String    khid="";

    //实际输入的门店编号
    public static String    inputkhid="";


    //订单流水号，命名规则为 销售日期和 一个三位的序号,这里只使用序号
    public static  int    number=1;

    public static  String     ordernumber="";


    public static  String     outTransId="";  //小票打印的商户订单号

    //门店注册登陆之后 返回的 userid，用于 支付时使用
    public static String    userId="";

    //商户编号，是由接口返回的
    public static String    lCorpId="";

    public static String    corpId=""; //和上面的用于会员信息查询


    //机器名称
    public static String    machine_name="";

    //微信支付微信端appid
    public static String    appId="wxcb3df0aa8b0dd6f6";

    //子商户的appid
    public static String    sub_appId="wx2d784531bbb9a017";

    //微信支付商户密钥
    public static String    appKey="aiinbiaiinbiaiinbiaiinbiaiinbiai";

    public static boolean   is_click=true;

    //微信支付商户号
    public static String    mch_id="";

    public static String    usepayway="";

    public static MediaPlayer player=new MediaPlayer();

    //微信支付子商户号
    public static String    sub_mch_id="";

    //程序版本号
    public static String    app_version="";


    //本次交易获得的积分值
    public static String    get_jf="0.0";


    public static HyMessage  hyMessage=null;

    public static CreateAddAdapter list_adaptor=null;


    public static OrderInfo orderInfo=null;
}
