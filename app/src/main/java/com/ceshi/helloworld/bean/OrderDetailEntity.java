package com.ceshi.helloworld.bean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailEntity  implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OrderService.getOrderDetail.OK"}
     * response : {"server_timestamp":1576549984929,"userId":"1","transId":"900060619120800002","storeId":"S606","StorePic":"https://oss.cyzone.cn/2019/0916/51e63a87bcb74ea33573b8d03adf2429.png","storePhone":"18612995996","storeName":"兴达超市晨光店","totAmount":22.05,"disAmount":5.9,"realAmount":16.15,"payVal":16.15,"vipNo":"15","totQty":2,"trnTime":"2019-12-08 09:13:14","invoiceFlg":0,"returnStatus":0,"checkStatus":1,"bizType":1,"nPickupStatus":"0","corpId":"C601","nCurrentPoint":0,"corpTransId":"1084","outTransId":"751001084","pluMap":[{"itemId":1,"barcode":"287954100615011921/2879541","goodsId":"018799","pluName":"小叶桔1.192kg","pluPrice":21.9,"pluQty":1,"pluAmount":6.15,"RealAmount":6.15,"pluDis":0,"disType":"n","maxReturnCount":1,"nWeight":1.192},{"itemId":2,"barcode":"6933179230104/6933179230104","goodsId":"047314","pluName":"纳美小苏打源生护龈牙膏120g","pluPrice":15.9,"pluQty":1,"pluAmount":15.9,"RealAmount":10,"pluDis":5.9,"disType":"p","maxReturnCount":1,"nWeight":0}],"payMap":{"payTypeId":5,"payTypeName":"微信小程序","payVal":16.15},"giftMap":[],"coinMap":{"coinCount":0,"coinVal":0},"disMap":[],"returnMap":[],"totalDis":"0.00","activityMap":[],"activityUsedMap":[],"bShowRefund":true,"nPoint":"-1"}
     */

    @com.google.gson.annotations.SerializedName("return")
    private ReturnBean returnX;
    private ResponseBean response;

    public ReturnBean getReturnX() {
        return returnX;
    }

    public void setReturnX(ReturnBean returnX) {
        this.returnX = returnX;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ReturnBean {
        /**
         * nCode : 0
         * strText : OK
         * strInfo : OrderService.getOrderDetail.OK
         */

        private int nCode;
        private String strText;
        private String strInfo;

        public int getNCode() {
            return nCode;
        }

        public void setNCode(int nCode) {
            this.nCode = nCode;
        }

        public String getStrText() {
            return strText;
        }

        public void setStrText(String strText) {
            this.strText = strText;
        }

        public String getStrInfo() {
            return strInfo;
        }

        public void setStrInfo(String strInfo) {
            this.strInfo = strInfo;
        }
    }

    public static class ResponseBean {
        /**
         * server_timestamp : 1576549984929
         * userId : 1
         * transId : 900060619120800002
         * storeId : S606
         * StorePic : https://oss.cyzone.cn/2019/0916/51e63a87bcb74ea33573b8d03adf2429.png
         * storePhone : 18612995996
         * storeName : 兴达超市晨光店
         * totAmount : 22.05
         * disAmount : 5.9
         * realAmount : 16.15
         * payVal : 16.15
         * vipNo : 15
         * totQty : 2
         * trnTime : 2019-12-08 09:13:14
         * invoiceFlg : 0
         * returnStatus : 0
         * checkStatus : 1
         * bizType : 1
         * nPickupStatus : 0
         * corpId : C601
         * nCurrentPoint : 0
         * corpTransId : 1084
         * outTransId : 751001084
         * pluMap : [{"itemId":1,"barcode":"287954100615011921/2879541","goodsId":"018799","pluName":"小叶桔1.192kg","pluPrice":21.9,"pluQty":1,"pluAmount":6.15,"RealAmount":6.15,"pluDis":0,"disType":"n","maxReturnCount":1,"nWeight":1.192},{"itemId":2,"barcode":"6933179230104/6933179230104","goodsId":"047314","pluName":"纳美小苏打源生护龈牙膏120g","pluPrice":15.9,"pluQty":1,"pluAmount":15.9,"RealAmount":10,"pluDis":5.9,"disType":"p","maxReturnCount":1,"nWeight":0}]
         * payMap : {"payTypeId":5,"payTypeName":"微信小程序","payVal":16.15}
         * giftMap : []
         * coinMap : {"coinCount":0,"coinVal":0}
         * disMap : []
         * returnMap : []
         * totalDis : 0.00
         * activityMap : []
         * activityUsedMap : []
         * bShowRefund : true
         * nPoint : -1
         */

        private long server_timestamp;
        private String userId;
        private String transId;
        private String storeId;
        private String StorePic;
        private String storePhone;
        private String storeName;
        private double totAmount;
        private double disAmount;
        private double realAmount;
        private double payVal;
        private String vipNo;
        private int totQty;
        private String trnTime;
        private int invoiceFlg;
        private int returnStatus;
        private int checkStatus;
        private int bizType;
        private String nPickupStatus;
        private String corpId;
        private int nCurrentPoint;
        private String corpTransId;
        private String outTransId;
        private PayMapBean payMap;
        private CoinMapBean coinMap;
        private String totalDis;
        private boolean bShowRefund;
        private String nPoint;
        private List<PluMapBean> pluMap;
        private List<?> giftMap;
        private List<?> disMap;
        private List<?> returnMap;
        private List<?> activityMap;
        private List<?> activityUsedMap;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStorePic() {
            return StorePic;
        }

        public void setStorePic(String StorePic) {
            this.StorePic = StorePic;
        }

        public String getStorePhone() {
            return storePhone;
        }

        public void setStorePhone(String storePhone) {
            this.storePhone = storePhone;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public double getTotAmount() {
            return totAmount;
        }

        public void setTotAmount(double totAmount) {
            this.totAmount = totAmount;
        }

        public double getDisAmount() {
            return disAmount;
        }

        public void setDisAmount(double disAmount) {
            this.disAmount = disAmount;
        }

        public double getRealAmount() {
            return realAmount;
        }

        public void setRealAmount(double realAmount) {
            this.realAmount = realAmount;
        }

        public double getPayVal() {
            return payVal;
        }

        public void setPayVal(double payVal) {
            this.payVal = payVal;
        }

        public String getVipNo() {
            return vipNo;
        }

        public void setVipNo(String vipNo) {
            this.vipNo = vipNo;
        }

        public int getTotQty() {
            return totQty;
        }

        public void setTotQty(int totQty) {
            this.totQty = totQty;
        }

        public String getTrnTime() {
            return trnTime;
        }

        public void setTrnTime(String trnTime) {
            this.trnTime = trnTime;
        }

        public int getInvoiceFlg() {
            return invoiceFlg;
        }

        public void setInvoiceFlg(int invoiceFlg) {
            this.invoiceFlg = invoiceFlg;
        }

        public int getReturnStatus() {
            return returnStatus;
        }

        public void setReturnStatus(int returnStatus) {
            this.returnStatus = returnStatus;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getBizType() {
            return bizType;
        }

        public void setBizType(int bizType) {
            this.bizType = bizType;
        }

        public String getNPickupStatus() {
            return nPickupStatus;
        }

        public void setNPickupStatus(String nPickupStatus) {
            this.nPickupStatus = nPickupStatus;
        }

        public String getCorpId() {
            return corpId;
        }

        public void setCorpId(String corpId) {
            this.corpId = corpId;
        }

        public int getNCurrentPoint() {
            return nCurrentPoint;
        }

        public void setNCurrentPoint(int nCurrentPoint) {
            this.nCurrentPoint = nCurrentPoint;
        }

        public String getCorpTransId() {
            return corpTransId;
        }

        public void setCorpTransId(String corpTransId) {
            this.corpTransId = corpTransId;
        }

        public String getOutTransId() {
            return outTransId;
        }

        public void setOutTransId(String outTransId) {
            this.outTransId = outTransId;
        }

        public PayMapBean getPayMap() {
            return payMap;
        }

        public void setPayMap(PayMapBean payMap) {
            this.payMap = payMap;
        }

        public CoinMapBean getCoinMap() {
            return coinMap;
        }

        public void setCoinMap(CoinMapBean coinMap) {
            this.coinMap = coinMap;
        }

        public String getTotalDis() {
            return totalDis;
        }

        public void setTotalDis(String totalDis) {
            this.totalDis = totalDis;
        }

        public boolean isBShowRefund() {
            return bShowRefund;
        }

        public void setBShowRefund(boolean bShowRefund) {
            this.bShowRefund = bShowRefund;
        }

        public String getNPoint() {
            return nPoint;
        }

        public void setNPoint(String nPoint) {
            this.nPoint = nPoint;
        }

        public List<PluMapBean> getPluMap() {
            return pluMap;
        }

        public void setPluMap(List<PluMapBean> pluMap) {
            this.pluMap = pluMap;
        }

        public List<?> getGiftMap() {
            return giftMap;
        }

        public void setGiftMap(List<?> giftMap) {
            this.giftMap = giftMap;
        }

        public List<?> getDisMap() {
            return disMap;
        }

        public void setDisMap(List<?> disMap) {
            this.disMap = disMap;
        }

        public List<?> getReturnMap() {
            return returnMap;
        }

        public void setReturnMap(List<?> returnMap) {
            this.returnMap = returnMap;
        }

        public List<?> getActivityMap() {
            return activityMap;
        }

        public void setActivityMap(List<?> activityMap) {
            this.activityMap = activityMap;
        }

        public List<?> getActivityUsedMap() {
            return activityUsedMap;
        }

        public void setActivityUsedMap(List<?> activityUsedMap) {
            this.activityUsedMap = activityUsedMap;
        }

        public static class PayMapBean {
            /**
             * payTypeId : 5
             * payTypeName : 微信小程序
             * payVal : 16.15
             */

            private int payTypeId;
            private String payTypeName;
            private double payVal;

            public int getPayTypeId() {
                return payTypeId;
            }

            public void setPayTypeId(int payTypeId) {
                this.payTypeId = payTypeId;
            }

            public String getPayTypeName() {
                return payTypeName;
            }

            public void setPayTypeName(String payTypeName) {
                this.payTypeName = payTypeName;
            }

            public double getPayVal() {
                return payVal;
            }

            public void setPayVal(double payVal) {
                this.payVal = payVal;
            }
        }

        public static class CoinMapBean {
            /**
             * coinCount : 0
             * coinVal : 0.0
             */

            private int coinCount;
            private double coinVal;

            public int getCoinCount() {
                return coinCount;
            }

            public void setCoinCount(int coinCount) {
                this.coinCount = coinCount;
            }

            public double getCoinVal() {
                return coinVal;
            }

            public void setCoinVal(double coinVal) {
                this.coinVal = coinVal;
            }
        }

        public static class PluMapBean {
            /**
             * itemId : 1
             * barcode : 287954100615011921/2879541
             * goodsId : 018799
             * pluName : 小叶桔1.192kg
             * pluPrice : 21.9
             * pluQty : 1
             * pluAmount : 6.15
             * RealAmount : 6.15
             * pluDis : 0.0
             * disType : n
             * maxReturnCount : 1
             * nWeight : 1.192
             */

            private int itemId;
            private String barcode;
            private String goodsId;
            private String pluName;
            private double pluPrice;
            private int pluQty;
            private double pluAmount;
            private double RealAmount;
            private double pluDis;
            private String disType;
            private int maxReturnCount;
            private double nWeight;

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

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

            public String getPluName() {
                return pluName;
            }

            public void setPluName(String pluName) {
                this.pluName = pluName;
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

            public double getPluAmount() {
                return pluAmount;
            }

            public void setPluAmount(double pluAmount) {
                this.pluAmount = pluAmount;
            }

            public double getRealAmount() {
                return RealAmount;
            }

            public void setRealAmount(double RealAmount) {
                this.RealAmount = RealAmount;
            }

            public double getPluDis() {
                return pluDis;
            }

            public void setPluDis(double pluDis) {
                this.pluDis = pluDis;
            }

            public String getDisType() {
                return disType;
            }

            public void setDisType(String disType) {
                this.disType = disType;
            }

            public int getMaxReturnCount() {
                return maxReturnCount;
            }

            public void setMaxReturnCount(int maxReturnCount) {
                this.maxReturnCount = maxReturnCount;
            }

            public double getNWeight() {
                return nWeight;
            }

            public void setNWeight(double nWeight) {
                this.nWeight = nWeight;
            }
        }
    }

}
