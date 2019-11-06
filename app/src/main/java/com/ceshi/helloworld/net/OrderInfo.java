package com.ceshi.helloworld.net;

import java.util.List;

public class OrderInfo {

     public   String   prepayId="";


     //订单总价
    public   String   totalPrice="";


    //订单总数量
    public   String   totalCount="";


    //订单总折扣
    public   String   totalDisc="";



    //上面几个字段，用于 对 整个订单金额的汇总，便于在下面 汇总显示



    //产品的集合
    public List<SplnfoList>   spList= null;

}
