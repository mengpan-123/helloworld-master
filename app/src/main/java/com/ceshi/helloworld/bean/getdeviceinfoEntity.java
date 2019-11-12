package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class getdeviceinfoEntity implements Serializable {



    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1573442114939,"corpId":"C1501","storeId":"S1501","storeName":"佳惠超市黔江大十字店","sCorpName":"佳惠超市","lStoreId":78,"lCorpId":16,"userId":"526374","userCount":1,"sStorePicPath":"http://file1.rongxwy.com//img/logo.png","token":"c367cf9d89767aa698afdd2e26f18296d17fb9ef07b62a27e06f07d8eca5d793","tokenGataWay":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJnZW5lcmF0aW9uVGltZSAiOiJNb24gTm92IDExIDExOjE1OjE1IENTVCAyMDE5IiwiZXhwIjoxNTc0MDQ2OTE1LCJ1c2VySWQiOiI1MjYzNzQifQ.eJzixn2WZSqmQzDm20_lp32-E2lX6D0vLNfV2vEVgU8"}
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
         * server_timestamp : 1573442114939
         * corpId : C1501
         * storeId : S1501
         * storeName : 佳惠超市黔江大十字店
         * sCorpName : 佳惠超市
         * lStoreId : 78
         * lCorpId : 16
         * userId : 526374
         * userCount : 1
         * sStorePicPath : http://file1.rongxwy.com//img/logo.png
         * token : c367cf9d89767aa698afdd2e26f18296d17fb9ef07b62a27e06f07d8eca5d793
         * tokenGataWay : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJnZW5lcmF0aW9uVGltZSAiOiJNb24gTm92IDExIDExOjE1OjE1IENTVCAyMDE5IiwiZXhwIjoxNTc0MDQ2OTE1LCJ1c2VySWQiOiI1MjYzNzQifQ.eJzixn2WZSqmQzDm20_lp32-E2lX6D0vLNfV2vEVgU8
         */

        private long server_timestamp;
        private String corpId;
        private String storeId;
        private String storeName;
        private String sCorpName;
        private String lStoreId;
        private String lCorpId;
        private String userId;
        private String userCount;
        private String sStorePicPath;
        private String token;
        private String tokenGataWay;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public String getCorpId() {
            return corpId;
        }

        public void setCorpId(String corpId) {
            this.corpId = corpId;
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

        public String getSCorpName() {
            return sCorpName;
        }

        public void setSCorpName(String sCorpName) {
            this.sCorpName = sCorpName;
        }

        public String getLStoreId() {
            return lStoreId;
        }

        public void setLStoreId(String lStoreId) {
            this.lStoreId = lStoreId;
        }

        public String getLCorpId() {
            return lCorpId;
        }

        public void setLCorpId(String lCorpId) {
            this.lCorpId = lCorpId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserCount() {
            return userCount;
        }

        public void setUserCount(String userCount) {
            this.userCount = userCount;
        }

        public String getSStorePicPath() {
            return sStorePicPath;
        }

        public void setSStorePicPath(String sStorePicPath) {
            this.sStorePicPath = sStorePicPath;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTokenGataWay() {
            return tokenGataWay;
        }

        public void setTokenGataWay(String tokenGataWay) {
            this.tokenGataWay = tokenGataWay;
        }
    }

}
