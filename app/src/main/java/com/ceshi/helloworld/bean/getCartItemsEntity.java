package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class getCartItemsEntity implements Serializable {


    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1573183097112,"storeId":"S101","totAmount":"735.65","disAmount":"0.00","shouldAmount":"4.07","totalQty":193,"itemsList":[{"items":[{"nItemNo":12,"sBarcode":"6945541220835","sGoodsId":"129725","sGoodsName":"超市发塑料袋大号","sDisType":"n","nGoodsType":1,"nQty":22,"nPluPrice":0.4,"nRealPrice":0.01,"pluAmount":"8.80","pluDis":8.58,"pluRealAmount":"0.22","nWeight":0,"bSelected":true,"dLastUpdateTime":"2019-11-08 11:09:09","sGoodsHeadPic":"","dDiscount":0,"nDisType":0,"sDisNo":""}],"disRule":"正常商品","disGroupCount":0,"disGroupPrice":"0","nDisType":0,"dLastUpdateTime":"2019-11-08 11:09:09"}]}
     */

    @SerializedName("return")
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
         * strInfo : OK
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
         * server_timestamp : 1573183097112
         * storeId : S101
         * totAmount : 735.65
         * disAmount : 0.00
         * shouldAmount : 4.07
         * totalQty : 193
         * itemsList : [{"items":[{"nItemNo":12,"sBarcode":"6945541220835","sGoodsId":"129725","sGoodsName":"超市发塑料袋大号","sDisType":"n","nGoodsType":1,"nQty":22,"nPluPrice":0.4,"nRealPrice":0.01,"pluAmount":"8.80","pluDis":8.58,"pluRealAmount":"0.22","nWeight":0,"bSelected":true,"dLastUpdateTime":"2019-11-08 11:09:09","sGoodsHeadPic":"","dDiscount":0,"nDisType":0,"sDisNo":""}],"disRule":"正常商品","disGroupCount":0,"disGroupPrice":"0","nDisType":0,"dLastUpdateTime":"2019-11-08 11:09:09"}]
         */

        private long server_timestamp;
        private String storeId;
        private String totAmount;
        private String disAmount;
        private String shouldAmount;
        private int totalQty;
        private List<ItemsListBean> itemsList;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getTotAmount() {
            return totAmount;
        }

        public void setTotAmount(String totAmount) {
            this.totAmount = totAmount;
        }

        public String getDisAmount() {
            return disAmount;
        }

        public void setDisAmount(String disAmount) {
            this.disAmount = disAmount;
        }

        public String getShouldAmount() {
            return shouldAmount;
        }

        public void setShouldAmount(String shouldAmount) {
            this.shouldAmount = shouldAmount;
        }

        public int getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(int totalQty) {
            this.totalQty = totalQty;
        }

        public List<ItemsListBean> getItemsList() {
            return itemsList;
        }

        public void setItemsList(List<ItemsListBean> itemsList) {
            this.itemsList = itemsList;
        }

        public static class ItemsListBean {
            /**
             * items : [{"nItemNo":12,"sBarcode":"6945541220835","sGoodsId":"129725","sGoodsName":"超市发塑料袋大号","sDisType":"n","nGoodsType":1,"nQty":22,"nPluPrice":0.4,"nRealPrice":0.01,"pluAmount":"8.80","pluDis":8.58,"pluRealAmount":"0.22","nWeight":0,"bSelected":true,"dLastUpdateTime":"2019-11-08 11:09:09","sGoodsHeadPic":"","dDiscount":0,"nDisType":0,"sDisNo":""}]
             * disRule : 正常商品
             * disGroupCount : 0
             * disGroupPrice : 0
             * nDisType : 0
             * dLastUpdateTime : 2019-11-08 11:09:09
             */

            private String disRule;
            private int disGroupCount;
            private String disGroupPrice;
            private int nDisType;
            private String dLastUpdateTime;
            private List<ItemsBean> items;

            public String getDisRule() {
                return disRule;
            }

            public void setDisRule(String disRule) {
                this.disRule = disRule;
            }

            public int getDisGroupCount() {
                return disGroupCount;
            }

            public void setDisGroupCount(int disGroupCount) {
                this.disGroupCount = disGroupCount;
            }

            public String getDisGroupPrice() {
                return disGroupPrice;
            }

            public void setDisGroupPrice(String disGroupPrice) {
                this.disGroupPrice = disGroupPrice;
            }

            public int getNDisType() {
                return nDisType;
            }

            public void setNDisType(int nDisType) {
                this.nDisType = nDisType;
            }

            public String getDLastUpdateTime() {
                return dLastUpdateTime;
            }

            public void setDLastUpdateTime(String dLastUpdateTime) {
                this.dLastUpdateTime = dLastUpdateTime;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                /**
                 * nItemNo : 12
                 * sBarcode : 6945541220835
                 * sGoodsId : 129725
                 * sGoodsName : 超市发塑料袋大号
                 * sDisType : n
                 * nGoodsType : 1
                 * nQty : 22
                 * nPluPrice : 0.4
                 * nRealPrice : 0.01
                 * pluAmount : 8.80
                 * pluDis : 8.58
                 * pluRealAmount : 0.22
                 * nWeight : 0
                 * bSelected : true
                 * dLastUpdateTime : 2019-11-08 11:09:09
                 * sGoodsHeadPic :
                 * dDiscount : 0
                 * nDisType : 0
                 * sDisNo :
                 */

                private int nItemNo;
                private String sBarcode;
                private String sGoodsId;
                private String sGoodsName;
                private String sDisType;
                private int nGoodsType;
                private int nQty;
                private double nPluPrice;
                private double nRealPrice;
                private String pluAmount;
                private double pluDis;
                private String pluRealAmount;
                private int nWeight;
                private boolean bSelected;
                private String dLastUpdateTime;
                private String sGoodsHeadPic;
                private int dDiscount;
                private int nDisType;
                private String sDisNo;

                public int getNItemNo() {
                    return nItemNo;
                }

                public void setNItemNo(int nItemNo) {
                    this.nItemNo = nItemNo;
                }

                public String getSBarcode() {
                    return sBarcode;
                }

                public void setSBarcode(String sBarcode) {
                    this.sBarcode = sBarcode;
                }

                public String getSGoodsId() {
                    return sGoodsId;
                }

                public void setSGoodsId(String sGoodsId) {
                    this.sGoodsId = sGoodsId;
                }

                public String getSGoodsName() {
                    return sGoodsName;
                }

                public void setSGoodsName(String sGoodsName) {
                    this.sGoodsName = sGoodsName;
                }

                public String getSDisType() {
                    return sDisType;
                }

                public void setSDisType(String sDisType) {
                    this.sDisType = sDisType;
                }

                public int getNGoodsType() {
                    return nGoodsType;
                }

                public void setNGoodsType(int nGoodsType) {
                    this.nGoodsType = nGoodsType;
                }

                public int getNQty() {
                    return nQty;
                }

                public void setNQty(int nQty) {
                    this.nQty = nQty;
                }

                public double getNPluPrice() {
                    return nPluPrice;
                }

                public void setNPluPrice(double nPluPrice) {
                    this.nPluPrice = nPluPrice;
                }

                public double getNRealPrice() {
                    return nRealPrice;
                }

                public void setNRealPrice(double nRealPrice) {
                    this.nRealPrice = nRealPrice;
                }

                public String getPluAmount() {
                    return pluAmount;
                }

                public void setPluAmount(String pluAmount) {
                    this.pluAmount = pluAmount;
                }

                public double getPluDis() {
                    return pluDis;
                }

                public void setPluDis(double pluDis) {
                    this.pluDis = pluDis;
                }

                public String getPluRealAmount() {
                    return pluRealAmount;
                }

                public void setPluRealAmount(String pluRealAmount) {
                    this.pluRealAmount = pluRealAmount;
                }

                public int getNWeight() {
                    return nWeight;
                }

                public void setNWeight(int nWeight) {
                    this.nWeight = nWeight;
                }

                public boolean isBSelected() {
                    return bSelected;
                }

                public void setBSelected(boolean bSelected) {
                    this.bSelected = bSelected;
                }

                public String getDLastUpdateTime() {
                    return dLastUpdateTime;
                }

                public void setDLastUpdateTime(String dLastUpdateTime) {
                    this.dLastUpdateTime = dLastUpdateTime;
                }

                public String getSGoodsHeadPic() {
                    return sGoodsHeadPic;
                }

                public void setSGoodsHeadPic(String sGoodsHeadPic) {
                    this.sGoodsHeadPic = sGoodsHeadPic;
                }

                public int getDDiscount() {
                    return dDiscount;
                }

                public void setDDiscount(int dDiscount) {
                    this.dDiscount = dDiscount;
                }

                public int getNDisType() {
                    return nDisType;
                }

                public void setNDisType(int nDisType) {
                    this.nDisType = nDisType;
                }

                public String getSDisNo() {
                    return sDisNo;
                }

                public void setSDisNo(String sDisNo) {
                    this.sDisNo = sDisNo;
                }
            }
        }
    }
}
