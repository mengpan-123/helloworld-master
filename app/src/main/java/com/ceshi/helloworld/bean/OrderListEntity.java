package com.ceshi.helloworld.bean;

import java.io.Serializable;
import java.util.List;

public class OrderListEntity implements Serializable {


    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OrderService.getOrderDetailByTransId.OK"}
     * response : {"server_timestamp":1576461855288,"orderList":[{"transId":"900060619121400106","trnTime":"2019-12-14 17:20:54","storeId":"S606","storeName":"兴达超市晨光店","StorePic":"https://oss.cyzone.cn/2019/0916/51e63a87bcb74ea33573b8d03adf2429.png","checkStatus":1,"totQty":8,"realPayVal":132.06,"corpTransId":1346,"outTransId":"751001346","cdoSaledtl":[{"itemId":1,"barcode":"6923644297251/6923644297251","goodsId":"067941","pluName":"蒙牛特仑苏纯牛奶梦幻盖装250ml*10","pluPrice":68,"pluQty":1,"pluAmount":68,"RealAmount":68,"pluDis":0,"nWeight":0},{"itemId":2,"barcode":"6932583203155/6932583203155","goodsId":"005021","pluName":"苏伯紫菜蛋花汤8g","pluPrice":3.9,"pluQty":2,"pluAmount":7.8,"RealAmount":7.8,"pluDis":0,"nWeight":0}]}]}
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
         * strInfo : OrderService.getOrderDetailByTransId.OK
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
         * server_timestamp : 1576461855288
         * orderList : [{"transId":"900060619121400106","trnTime":"2019-12-14 17:20:54","storeId":"S606","storeName":"兴达超市晨光店","StorePic":"https://oss.cyzone.cn/2019/0916/51e63a87bcb74ea33573b8d03adf2429.png","checkStatus":1,"totQty":8,"realPayVal":132.06,"corpTransId":1346,"outTransId":"751001346","cdoSaledtl":[{"itemId":1,"barcode":"6923644297251/6923644297251","goodsId":"067941","pluName":"蒙牛特仑苏纯牛奶梦幻盖装250ml*10","pluPrice":68,"pluQty":1,"pluAmount":68,"RealAmount":68,"pluDis":0,"nWeight":0},{"itemId":2,"barcode":"6932583203155/6932583203155","goodsId":"005021","pluName":"苏伯紫菜蛋花汤8g","pluPrice":3.9,"pluQty":2,"pluAmount":7.8,"RealAmount":7.8,"pluDis":0,"nWeight":0}]}]
         */

        private long server_timestamp;
        private List<OrderListBean> orderList;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean {
            /**
             * transId : 900060619121400106
             * trnTime : 2019-12-14 17:20:54
             * storeId : S606
             * storeName : 兴达超市晨光店
             * StorePic : https://oss.cyzone.cn/2019/0916/51e63a87bcb74ea33573b8d03adf2429.png
             * checkStatus : 1
             * totQty : 8
             * realPayVal : 132.06
             * corpTransId : 1346
             * outTransId : 751001346
             * cdoSaledtl : [{"itemId":1,"barcode":"6923644297251/6923644297251","goodsId":"067941","pluName":"蒙牛特仑苏纯牛奶梦幻盖装250ml*10","pluPrice":68,"pluQty":1,"pluAmount":68,"RealAmount":68,"pluDis":0,"nWeight":0},{"itemId":2,"barcode":"6932583203155/6932583203155","goodsId":"005021","pluName":"苏伯紫菜蛋花汤8g","pluPrice":3.9,"pluQty":2,"pluAmount":7.8,"RealAmount":7.8,"pluDis":0,"nWeight":0}]
             */

            private String transId;
            private String trnTime;
            private String storeId;
            private String storeName;
            private String StorePic;
            private int checkStatus;
            private int totQty;
            private double realPayVal;
            private int corpTransId;
            private String outTransId;
            private List<CdoSaledtlBean> cdoSaledtl;

            public String getTransId() {
                return transId;
            }

            public void setTransId(String transId) {
                this.transId = transId;
            }

            public String getTrnTime() {
                return trnTime;
            }

            public void setTrnTime(String trnTime) {
                this.trnTime = trnTime;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getStorePic() {
                return StorePic;
            }

            public void setStorePic(String StorePic) {
                this.StorePic = StorePic;
            }

            public int getCheckStatus() {
                return checkStatus;
            }

            public void setCheckStatus(int checkStatus) {
                this.checkStatus = checkStatus;
            }

            public int getTotQty() {
                return totQty;
            }

            public void setTotQty(int totQty) {
                this.totQty = totQty;
            }

            public double getRealPayVal() {
                return realPayVal;
            }

            public void setRealPayVal(double realPayVal) {
                this.realPayVal = realPayVal;
            }

            public int getCorpTransId() {
                return corpTransId;
            }

            public void setCorpTransId(int corpTransId) {
                this.corpTransId = corpTransId;
            }

            public String getOutTransId() {
                return outTransId;
            }

            public void setOutTransId(String outTransId) {
                this.outTransId = outTransId;
            }

            public List<CdoSaledtlBean> getCdoSaledtl() {
                return cdoSaledtl;
            }

            public void setCdoSaledtl(List<CdoSaledtlBean> cdoSaledtl) {
                this.cdoSaledtl = cdoSaledtl;
            }

            public static class CdoSaledtlBean {
                /**
                 * itemId : 1
                 * barcode : 6923644297251/6923644297251
                 * goodsId : 067941
                 * pluName : 蒙牛特仑苏纯牛奶梦幻盖装250ml*10
                 * pluPrice : 68.0
                 * pluQty : 1
                 * pluAmount : 68.0
                 * RealAmount : 68.0
                 * pluDis : 0.0
                 * nWeight : 0.0
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

                public double getNWeight() {
                    return nWeight;
                }

                public void setNWeight(double nWeight) {
                    this.nWeight = nWeight;
                }
            }
        }
    }

}
