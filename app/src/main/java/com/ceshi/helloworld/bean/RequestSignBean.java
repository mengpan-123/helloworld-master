package com.ceshi.helloworld.bean;

import java.util.List;

public class RequestSignBean {


    /**
     * authCode :
     * bizType : 1
     * deviceId : 5266354
     * extra : {'prepayId':'cdb033e85e9049f692c2ee0d94826974958','mydata':'testinfo'}
     * faceCode : c1281f0f-d1ba-4c87-93da-9d314c463a2f
     * openid : 1111111
     * payMap : [{"payTypeId":1,"payVal":0.01}]
     * payWay : WXFacePay
     * pluMap : [{"barcode":"1","goodsId":"1","pluAmount":0.01,"pluDis":0,"pluPrice":0.01,"pluQty":1,"realPrice":0.01}]
     * sPayTypeExt : devicewxface
     * storeId : S1500
     * storeLat : 0
     * storeLng : 0
     * userId : 5266354
     */

    private String authCode;
    private int bizType;
    private String deviceId;
    private String extra;
    private String faceCode;
    private String openid;
    private String payWay;
    private String sPayTypeExt;
    private String storeId;
    private int storeLat;
    private int storeLng;
    private String userId;
    private List<PayMapBean> payMap;
    private List<PluMapBean> pluMap;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getSPayTypeExt() {
        return sPayTypeExt;
    }

    public void setSPayTypeExt(String sPayTypeExt) {
        this.sPayTypeExt = sPayTypeExt;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(int storeLat) {
        this.storeLat = storeLat;
    }

    public int getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(int storeLng) {
        this.storeLng = storeLng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<PayMapBean> getPayMap() {
        return payMap;
    }

    public void setPayMap(List<PayMapBean> payMap) {
        this.payMap = payMap;
    }

    public List<PluMapBean> getPluMap() {
        return pluMap;
    }

    public void setPluMap(List<PluMapBean> pluMap) {
        this.pluMap = pluMap;
    }

    public static class PayMapBean {
        /**
         * payTypeId : 1
         * payVal : 0.01
         */

        private int payTypeId;
        private double payVal;

        public int getPayTypeId() {
            return payTypeId;
        }

        public void setPayTypeId(int payTypeId) {
            this.payTypeId = payTypeId;
        }

        public double getPayVal() {
            return payVal;
        }

        public void setPayVal(double payVal) {
            this.payVal = payVal;
        }
    }

    public static class PluMapBean {
        /**
         * barcode : 1
         * goodsId : 1
         * pluAmount : 0.01
         * pluDis : 0
         * pluPrice : 0.01
         * pluQty : 1
         * realPrice : 0.01
         */

        private String barcode;
        private String goodsId;
        private double pluAmount;
        private int pluDis;
        private double pluPrice;
        private int pluQty;
        private double realPrice;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public double getPluAmount() {
            return pluAmount;
        }

        public void setPluAmount(double pluAmount) {
            this.pluAmount = pluAmount;
        }

        public int getPluDis() {
            return pluDis;
        }

        public void setPluDis(int pluDis) {
            this.pluDis = pluDis;
        }

        public double getPluPrice() {
            return pluPrice;
        }

        public void setPluPrice(double pluPrice) {
            this.pluPrice = pluPrice;
        }

        public int getPluQty() {
            return pluQty;
        }

        public void setPluQty(int pluQty) {
            this.pluQty = pluQty;
        }

        public double getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(double realPrice) {
            this.realPrice = realPrice;
        }
    }
}

