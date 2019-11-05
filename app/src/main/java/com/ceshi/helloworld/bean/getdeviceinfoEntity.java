package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class getdeviceinfoEntity implements Serializable {


    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1572952563194,"corpId":"C101","storeId":"S101","storeName":"如e测试店(220)","sCorpName":"融讯伟业","userId":"526374","userCount":1,"sStorePicPath":"http://file1.rongxwy.com//img/logo.png","token":"c367cf9d89767aa698afdd2e26f1829645aca7c8f6e1ad4d0afd5f2ffbf773f3","tokenGataWay":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJnZW5lcmF0aW9uVGltZSAiOiJUdWUgTm92IDA1IDE5OjE2OjAzIENTVCAyMDE5IiwiZXhwIjoxNTczNTU3MzYzLCJ1c2VySWQiOiI1MjYzNzQifQ.p3pVtGk0KEaN4cjFrCzpnKEMzQ1KCToSBQB8Pk3064M"}
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
            return  strText;
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
         * server_timestamp : 1572952563194
         * corpId : C101
         * storeId : S101
         * storeName : 如e测试店(220)
         * sCorpName : 融讯伟业
         * userId : 526374
         * userCount : 1
         * sStorePicPath : http://file1.rongxwy.com//img/logo.png
         * token : c367cf9d89767aa698afdd2e26f1829645aca7c8f6e1ad4d0afd5f2ffbf773f3
         * tokenGataWay : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJnZW5lcmF0aW9uVGltZSAiOiJUdWUgTm92IDA1IDE5OjE2OjAzIENTVCAyMDE5IiwiZXhwIjoxNTczNTU3MzYzLCJ1c2VySWQiOiI1MjYzNzQifQ.p3pVtGk0KEaN4cjFrCzpnKEMzQ1KCToSBQB8Pk3064M
         */

        private long server_timestamp;
        private String corpId;
        private String storeId;
        private String storeName;
        private String sCorpName;
        private String userId;
        private int userCount;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
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
