package com.ceshi.helloworld.net;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

//生成 xml内容
public class GenerateXMLData {


   public static  Map<String, Object> treeMap = new TreeMap<String, Object>(new Comparator<String>() {
        public int compare(String o1, String o2) {
            // 升序排序
            return o1.compareTo(o2);
        }
    });



    public static String ToXml()
    {
        String xml = "<xml>";

        for(TreeMap.Entry<String,Object> entry : treeMap.entrySet()) {

           /* if (entry.getValue()  instanceof  Integer) {
                xml += "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
            }
            else
            {
                //就是字符串
            }*/

            xml += "<" + entry.getKey() + "><![CDATA["+entry.getValue()+"]]></" + entry.getKey() + ">";

        }

        xml += "</xml>";
        return xml;
    }


    public static String SecToXml()
    {
        String xml = "<xml>";

        for(TreeMap.Entry<String,Object> entry : treeMap.entrySet()) {

           if (entry.getKey().equals("openid")) {

               xml += "<" + entry.getKey() + "><![CDATA[" + entry.getValue() + "]]></" + entry.getKey() + ">";
           }
           else
           {
               xml += "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
           }

        }

        xml += "</xml>";
        return xml;
    }



    //每增加一个元素，就写进去
    public static  void  AddNewvalue(String  key,Object   objvalue){

        if (!treeMap.containsKey(key)) {
            //如果不包含改键，则写一个进去
            treeMap.put(key, objvalue);
        }


    }


    //计算签名
    public static String GetSign(){


        String signStr="";
        for(TreeMap.Entry<String,Object> entry : treeMap.entrySet()) {

            if (!entry.getValue().equals("")){
                //只有参数不为空才参与签名
                signStr+=entry.getKey()+"="+entry.getValue()+"&";
            }
        }

        //拿到 key

        String  Signtemp=signStr+"key="+CommonData.appKey;
        //生成签名
        String  finalSign=getMD5(Signtemp);


        return finalSign;
    }



    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值

            return new BigInteger(1, md.digest()).toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 生成随机串，随机串包含字母或数字
     * @return 随机串
     */
    public static String GenerateNonceStr()
    {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random=new Random();

        StringBuffer sb=new StringBuffer();

        for(int i=0;i<16;i++){

            int number=random.nextInt(62);

            sb.append(str.charAt(number));

        }
        return sb.toString();
    }

    /**
     * 生成随机串，随机串包含字母或数字
     * @return 随机串
     */
    public static long GeneratetimeStamp()
    {
        long  timeNew =  System.currentTimeMillis()/ 1000; // 10位数的时间戳
        return  timeNew;

    }

}
