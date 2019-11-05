package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PurchaseBag implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"CartService.upGetBaginfo.OK"}
     * response : {"server_timestamp":1572954602136,"storeId":"S101","bagMap":[{"bagId":1,"barcode":"6945541220828","goodsId":"6945541220828","pluName":"小购物袋","pluPrice":0.02,"imgurl":"https://ruepay.oss-cn-beijing.aliyuncs.com/skin/device/bag/1.png"},{"bagId":2,"barcode":"6945541220835","goodsId":"6945541220835","pluName":"大购物袋","pluPrice":0.01,"imgurl":"https://ruepay.oss-cn-beijing.aliyuncs.com/skin/device/bag/2.png"}]}
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
         * strInfo : CartService.upGetBaginfo.OK
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
         * server_timestamp : 1572954602136
         * storeId : S101
         * bagMap : [{"bagId":1,"barcode":"6945541220828","goodsId":"6945541220828","pluName":"小购物袋","pluPrice":0.02,"imgurl":"https://ruepay.oss-cn-beijing.aliyuncs.com/skin/device/bag/1.png"},{"bagId":2,"barcode":"6945541220835","goodsId":"6945541220835","pluName":"大购物袋","pluPrice":0.01,"imgurl":"https://ruepay.oss-cn-beijing.aliyuncs.com/skin/device/bag/2.png"}]
         */

        private long server_timestamp;
        private String storeId;
        private List<BagMapBean> bagMap;

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

        public List<BagMapBean> getBagMap() {
            return bagMap;
        }

        public void setBagMap(List<BagMapBean> bagMap) {
            this.bagMap = bagMap;
        }

        public static class BagMapBean {
            /**
             * bagId : 1
             * barcode : 6945541220828
             * goodsId : 6945541220828
             * pluName : 小购物袋
             * pluPrice : 0.02
             * imgurl : https://ruepay.oss-cn-beijing.aliyuncs.com/skin/device/bag/1.png
             */

            private int bagId;
            private String barcode;
            private String goodsId;
            private String pluName;
            private double pluPrice;
            private String imgurl;

            public int getBagId() {
                return bagId;
            }

            public void setBagId(int bagId) {
                this.bagId = bagId;
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

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }
        }
    }


}
