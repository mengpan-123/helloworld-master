package com.ceshi.helloworld.net;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfo implements Serializable {

     public   String   prepayId="";


     //订单总价
    public   String   totalPrice="0";


    //订单总数量
    public   int   totalCount=0;


    //订单总折扣
    public   String   totalDisc="0";



    //上面几个字段，用于 对 整个订单金额的汇总，便于在下面 汇总显示



    //产品的集合
    public Map<String,List<SplnfoList>> spList=  new  HashMap<String, List<SplnfoList>>();

}
