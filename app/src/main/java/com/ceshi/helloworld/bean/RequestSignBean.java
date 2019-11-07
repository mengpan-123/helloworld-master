package com.ceshi.helloworld.bean;

import java.util.List;

public class RequestSignBean {


    /**
     * storeId : S101
     * sSourceType : device
     * userId : 526374
     * storeLng : 0
     * storeLat : 0
     * payWay : WXPaymentCodePay
     * authCode : 18位支付码
     * bizType : 1
     * vipNo :
     * extra : {'prepayId':'12345678912345678912345678912346','mydata':'hahah'}
     * deviceId : 1111
     * pluMap : [{"goodsId":"56550","barcode":"6906907113246","pluQty":"1","pluPrice":"48","realPrice":70,"pluAmount":70,"pluDis":70}]
     * payMap : [{"paySn":"","payTypeId":1,"payVal":0.01}]
     */

    private String storeId;
    private String sSourceType;
    private String userId;
    private int storeLng;
    private int storeLat;
    private String payWay;
    private String authCode;
    private int bizType;
    private String vipNo;
    private String extra;
    private String deviceId;
    private List<PluMapBean> pluMap;
    private List<PayMapBean> payMap;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSSourceType() {
        return sSourceType;
    }

    public void setSSourceType(String sSourceType) {
        this.sSourceType = sSourceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(int storeLng) {
        this.storeLng = storeLng;
    }

    public int getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(int storeLat) {
        this.storeLat = storeLat;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

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

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<PluMapBean> getPluMap() {
        return pluMap;
    }

    public void setPluMap(List<PluMapBean> pluMap) {
        this.pluMap = pluMap;
    }

    public List<PayMapBean> getPayMap() {
        return payMap;
    }

    public void setPayMap(List<PayMapBean> payMap) {
        this.payMap = payMap;
    }

    public static class PluMapBean {
        /**
         * goodsId : 56550
         * barcode : 6906907113246
         * pluQty : 1
         * pluPrice : 48
         * realPrice : 70
         * pluAmount : 70
         * pluDis : 70
         */

        private String goodsId;
        private String barcode;
        private String pluQty;
        private String pluPrice;
        private int realPrice;
        private int pluAmount;
        private int pluDis;

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

        public String getPluQty() {
            return pluQty;
        }

        public void setPluQty(String pluQty) {
            this.pluQty = pluQty;
        }

        public String getPluPrice() {
            return pluPrice;
        }

        public void setPluPrice(String pluPrice) {
            this.pluPrice = pluPrice;
        }

        public int getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(int realPrice) {
            this.realPrice = realPrice;
        }

        public int getPluAmount() {
            return pluAmount;
        }

        public void setPluAmount(int pluAmount) {
            this.pluAmount = pluAmount;
        }

        public int getPluDis() {
            return pluDis;
        }

        public void setPluDis(int pluDis) {
            this.pluDis = pluDis;
        }
    }

    public static class PayMapBean {
        /**
         * paySn :
         * payTypeId : 1
         * payVal : 0.01
         */

        private String paySn;
        private int payTypeId;
        private double payVal;

        public String getPaySn() {
            return paySn;
        }

        public void setPaySn(String paySn) {
            this.paySn = paySn;
        }

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
}

