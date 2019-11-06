package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Addgoods  implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1573004404786,"cartItemCount":3,"cartGoodsCount":37,"itemNo":3,"nQty":35,"goodsList":[{"storeId":"S101","barcode":"6907992501468","goodsId":"090673","nGoodsId":90673,"pluName":"伊利纯鲜牛奶(百利包)200ml","pluTypeId":1,"nGoodsType":1,"pluUnit":"袋","pluStatus":0,"mainPrice":1.9,"pluPrice":1.9,"vipPrice":0,"realPrice":0.01,"disType":"p","extPrice":0.01,"goodsNo":"6907992501468","sGoodsNo":"6907992501468","groupId":"20","deptNo":"202020301","nX":"1","packNum":1,"extDisNo":"","vipDisNo":"","nStock":0,"nStatus":0,"barcode1":"6907992501468","pluPrice1":1.9,"realPrice1":0.01,"vipPrice1":0,"str0":"","str1":"","str2":"","str3":"","str4":"","str5":"","str6":"","str7":"","str8":"","str9":"","weight":0,"nWeight":0,"nBizType":1,"pluDis":0}]}
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
         * server_timestamp : 1573004404786
         * cartItemCount : 3
         * cartGoodsCount : 37
         * itemNo : 3
         * nQty : 35
         * goodsList : [{"storeId":"S101","barcode":"6907992501468","goodsId":"090673","nGoodsId":90673,"pluName":"伊利纯鲜牛奶(百利包)200ml","pluTypeId":1,"nGoodsType":1,"pluUnit":"袋","pluStatus":0,"mainPrice":1.9,"pluPrice":1.9,"vipPrice":0,"realPrice":0.01,"disType":"p","extPrice":0.01,"goodsNo":"6907992501468","sGoodsNo":"6907992501468","groupId":"20","deptNo":"202020301","nX":"1","packNum":1,"extDisNo":"","vipDisNo":"","nStock":0,"nStatus":0,"barcode1":"6907992501468","pluPrice1":1.9,"realPrice1":0.01,"vipPrice1":0,"str0":"","str1":"","str2":"","str3":"","str4":"","str5":"","str6":"","str7":"","str8":"","str9":"","weight":0,"nWeight":0,"nBizType":1,"pluDis":0}]
         */

        private long server_timestamp;
        private int cartItemCount;
        private int cartGoodsCount;
        private int itemNo;
        private int nQty;
        private List<GoodsListBean> goodsList;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public int getCartItemCount() {
            return cartItemCount;
        }

        public void setCartItemCount(int cartItemCount) {
            this.cartItemCount = cartItemCount;
        }

        public int getCartGoodsCount() {
            return cartGoodsCount;
        }

        public void setCartGoodsCount(int cartGoodsCount) {
            this.cartGoodsCount = cartGoodsCount;
        }

        public int getItemNo() {
            return itemNo;
        }

        public void setItemNo(int itemNo) {
            this.itemNo = itemNo;
        }

        public int getNQty() {
            return nQty;
        }

        public void setNQty(int nQty) {
            this.nQty = nQty;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class GoodsListBean {
            /**
             * storeId : S101
             * barcode : 6907992501468
             * goodsId : 090673
             * nGoodsId : 90673
             * pluName : 伊利纯鲜牛奶(百利包)200ml
             * pluTypeId : 1
             * nGoodsType : 1
             * pluUnit : 袋
             * pluStatus : 0
             * mainPrice : 1.9
             * pluPrice : 1.9
             * vipPrice : 0.0
             * realPrice : 0.01
             * disType : p
             * extPrice : 0.01
             * goodsNo : 6907992501468
             * sGoodsNo : 6907992501468
             * groupId : 20
             * deptNo : 202020301
             * nX : 1
             * packNum : 1
             * extDisNo :
             * vipDisNo :
             * nStock : 0.0
             * nStatus : 0
             * barcode1 : 6907992501468
             * pluPrice1 : 1.9
             * realPrice1 : 0.01
             * vipPrice1 : 0.0
             * str0 :
             * str1 :
             * str2 :
             * str3 :
             * str4 :
             * str5 :
             * str6 :
             * str7 :
             * str8 :
             * str9 :
             * weight : 0.0
             * nWeight : 0.0
             * nBizType : 1
             * pluDis : 0.0
             */

            private String storeId;
            private String barcode;
            private String goodsId;
            private int nGoodsId;
            private String pluName;
            private int pluTypeId;
            private int nGoodsType;
            private String pluUnit;
            private int pluStatus;
            private double mainPrice;
            private double pluPrice;
            private double vipPrice;
            private double realPrice;
            private String disType;
            private double extPrice;
            private String goodsNo;
            private String sGoodsNo;
            private String groupId;
            private String deptNo;
            private String nX;
            private int packNum;
            private String extDisNo;
            private String vipDisNo;
            private double nStock;
            private int nStatus;
            private String barcode1;
            private double pluPrice1;
            private double realPrice1;
            private double vipPrice1;
            private String str0;
            private String str1;
            private String str2;
            private String str3;
            private String str4;
            private String str5;
            private String str6;
            private String str7;
            private String str8;
            private String str9;
            private double weight;
            private double nWeight;
            private int nBizType;
            private double pluDis;

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
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

            public int getNGoodsId() {
                return nGoodsId;
            }

            public void setNGoodsId(int nGoodsId) {
                this.nGoodsId = nGoodsId;
            }

            public String getPluName() {
                return pluName;
            }

            public void setPluName(String pluName) {
                this.pluName = pluName;
            }

            public int getPluTypeId() {
                return pluTypeId;
            }

            public void setPluTypeId(int pluTypeId) {
                this.pluTypeId = pluTypeId;
            }

            public int getNGoodsType() {
                return nGoodsType;
            }

            public void setNGoodsType(int nGoodsType) {
                this.nGoodsType = nGoodsType;
            }

            public String getPluUnit() {
                return pluUnit;
            }

            public void setPluUnit(String pluUnit) {
                this.pluUnit = pluUnit;
            }

            public int getPluStatus() {
                return pluStatus;
            }

            public void setPluStatus(int pluStatus) {
                this.pluStatus = pluStatus;
            }

            public double getMainPrice() {
                return mainPrice;
            }

            public void setMainPrice(double mainPrice) {
                this.mainPrice = mainPrice;
            }

            public double getPluPrice() {
                return pluPrice;
            }

            public void setPluPrice(double pluPrice) {
                this.pluPrice = pluPrice;
            }

            public double getVipPrice() {
                return vipPrice;
            }

            public void setVipPrice(double vipPrice) {
                this.vipPrice = vipPrice;
            }

            public double getRealPrice() {
                return realPrice;
            }

            public void setRealPrice(double realPrice) {
                this.realPrice = realPrice;
            }

            public String getDisType() {
                return disType;
            }

            public void setDisType(String disType) {
                this.disType = disType;
            }

            public double getExtPrice() {
                return extPrice;
            }

            public void setExtPrice(double extPrice) {
                this.extPrice = extPrice;
            }

            public String getGoodsNo() {
                return goodsNo;
            }

            public void setGoodsNo(String goodsNo) {
                this.goodsNo = goodsNo;
            }

            public String getSGoodsNo() {
                return sGoodsNo;
            }

            public void setSGoodsNo(String sGoodsNo) {
                this.sGoodsNo = sGoodsNo;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getDeptNo() {
                return deptNo;
            }

            public void setDeptNo(String deptNo) {
                this.deptNo = deptNo;
            }

            public String getNX() {
                return nX;
            }

            public void setNX(String nX) {
                this.nX = nX;
            }

            public int getPackNum() {
                return packNum;
            }

            public void setPackNum(int packNum) {
                this.packNum = packNum;
            }

            public String getExtDisNo() {
                return extDisNo;
            }

            public void setExtDisNo(String extDisNo) {
                this.extDisNo = extDisNo;
            }

            public String getVipDisNo() {
                return vipDisNo;
            }

            public void setVipDisNo(String vipDisNo) {
                this.vipDisNo = vipDisNo;
            }

            public double getNStock() {
                return nStock;
            }

            public void setNStock(double nStock) {
                this.nStock = nStock;
            }

            public int getNStatus() {
                return nStatus;
            }

            public void setNStatus(int nStatus) {
                this.nStatus = nStatus;
            }

            public String getBarcode1() {
                return barcode1;
            }

            public void setBarcode1(String barcode1) {
                this.barcode1 = barcode1;
            }

            public double getPluPrice1() {
                return pluPrice1;
            }

            public void setPluPrice1(double pluPrice1) {
                this.pluPrice1 = pluPrice1;
            }

            public double getRealPrice1() {
                return realPrice1;
            }

            public void setRealPrice1(double realPrice1) {
                this.realPrice1 = realPrice1;
            }

            public double getVipPrice1() {
                return vipPrice1;
            }

            public void setVipPrice1(double vipPrice1) {
                this.vipPrice1 = vipPrice1;
            }

            public String getStr0() {
                return str0;
            }

            public void setStr0(String str0) {
                this.str0 = str0;
            }

            public String getStr1() {
                return str1;
            }

            public void setStr1(String str1) {
                this.str1 = str1;
            }

            public String getStr2() {
                return str2;
            }

            public void setStr2(String str2) {
                this.str2 = str2;
            }

            public String getStr3() {
                return str3;
            }

            public void setStr3(String str3) {
                this.str3 = str3;
            }

            public String getStr4() {
                return str4;
            }

            public void setStr4(String str4) {
                this.str4 = str4;
            }

            public String getStr5() {
                return str5;
            }

            public void setStr5(String str5) {
                this.str5 = str5;
            }

            public String getStr6() {
                return str6;
            }

            public void setStr6(String str6) {
                this.str6 = str6;
            }

            public String getStr7() {
                return str7;
            }

            public void setStr7(String str7) {
                this.str7 = str7;
            }

            public String getStr8() {
                return str8;
            }

            public void setStr8(String str8) {
                this.str8 = str8;
            }

            public String getStr9() {
                return str9;
            }

            public void setStr9(String str9) {
                this.str9 = str9;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public double getNWeight() {
                return nWeight;
            }

            public void setNWeight(double nWeight) {
                this.nWeight = nWeight;
            }

            public int getNBizType() {
                return nBizType;
            }

            public void setNBizType(int nBizType) {
                this.nBizType = nBizType;
            }

            public double getPluDis() {
                return pluDis;
            }

            public void setPluDis(double pluDis) {
                this.pluDis = pluDis;
            }
        }
    }
}
