package com.ceshi.helloworld.net;

public class  SplnfoList {


    //产品编码，用此键 来判断 产品是否存在
    private String goodsId = "";

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPluName() {
        return pluName;
    }

    public void setPluName(String pluName) {
        this.pluName = pluName;
    }

    public String getPluUnit() {
        return pluUnit;
    }

    public void setPluUnit(String pluUnit) {
        this.pluUnit = pluUnit;
    }

    public int getPluTypeId() {
        return pluTypeId;
    }

    public void setPluTypeId(int pluTypeId) {
        this.pluTypeId = pluTypeId;
    }

    public Double getMainPrice() {
        return mainPrice;
    }

    public void setMainPrice(Double mainPrice) {
        this.mainPrice = mainPrice;
    }

    public int getPackNum() {
        return packNum;
    }

    public void setPackNum(int packNum) {
        this.packNum = packNum;
    }

    public double getpluPrice() {
        return pluPrice;
    }

    public void setpluPrice(double pluPrice) {
        this.pluPrice = pluPrice;
    }


    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }





    public double getTotaldisc() {
        return totaldisc;
    }

    public void setTotaldisc(double totaldisc) {
        this.totaldisc = totaldisc;
    }

    //商品条形码
    private String barcode = "";

    //产品名称
    private String pluName = "";

    //产品单位
    private String pluUnit = "";

    //产品类型
    private int pluTypeId;


    private double mainPrice ;

    //产品数量
    private int packNum;

    //实际售价
    private String realPrice;

    //实际售价
    private double pluPrice;

    //订单折扣
    private double totaldisc;
}
