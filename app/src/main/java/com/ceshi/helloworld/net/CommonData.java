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


    //商户编号，是由接口返回的
    public static String    lCorpId="";

    //机器名称
    public static String    machine_name="";

    //订单流水号
    public static String    prepayId="";

    //微信支付商户号
    public static String    mch_id="";

    //微信支付子商户号
    public static String    sub_mch_id="";


    //程序版本号
    public static String    app_version="";



    public static  class   SpInfoList {

        public String barcode = "";

        public String goodsId = "";
        public String pluName = "";
        public String pluUnit = "";
        public String mainPrice = "";

        public String pluTypeId = "";

        public String realPrice="";

        //订单总价
        public String totalPrice="";
    }


    public static  class   HyInfo {

        //会员卡号
        public String cardnumber = "";
        //会员手机号
        public String hytelphone = "";
        //是否录入会员
        public String isInputHy = "";

    }
}
