package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

public class UpdateVersionEntity {

   /* *//**
     * return : {"nCode":1,"strText":"获取新版本信息失败!","strInfo":"systemService.updateVersion.Error"}
     * response : {"server_timestamp":1573011103272}
     *//*

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
        *//**
         * nCode : 1
         * strText : 获取新版本信息失败!
         * strInfo : systemService.updateVersion.Error
         *//*

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
        *//**
         * server_timestamp : 1573011103272
         *//*

        private long server_timestamp;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }
    }*/


    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1575279740041,"appVersion":"3002","appName":"自助收银","updatePath":"https://acquisition-goodsimg.oss-cn-beijing.aliyuncs.com/2019-07-30/1000001/1.apk","appUpdateForce":1,"appUpdateDesc":"1","lastUpdateTime":"2019-12-02 17:03:31"}
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
         * server_timestamp : 1575279740041
         * appVersion : 3002
         * appName : 自助收银
         * updatePath : https://acquisition-goodsimg.oss-cn-beijing.aliyuncs.com/2019-07-30/1000001/1.apk
         * appUpdateForce : 1
         * appUpdateDesc : 1
         * lastUpdateTime : 2019-12-02 17:03:31
         */

        private long server_timestamp;
        private String appVersion;
        private String appName;
        private String updatePath;
        private int appUpdateForce;
        private String appUpdateDesc;
        private String lastUpdateTime;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getUpdatePath() {
            return updatePath;
        }

        public void setUpdatePath(String updatePath) {
            this.updatePath = updatePath;
        }

        public int getAppUpdateForce() {
            return appUpdateForce;
        }

        public void setAppUpdateForce(int appUpdateForce) {
            this.appUpdateForce = appUpdateForce;
        }

        public String getAppUpdateDesc() {
            return appUpdateDesc;
        }

        public void setAppUpdateDesc(String appUpdateDesc) {
            this.appUpdateDesc = appUpdateDesc;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }

}
