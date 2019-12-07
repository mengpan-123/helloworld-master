package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseSignBean implements Serializable {


    /**
     * return : {"nCode":2,"strText":"购物车为空","strInfo":""}
     * response : {"server_timestamp":1573037090581}
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
         * nCode : 2
         * strText : 购物车为空
         * strInfo :
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
         * server_timestamp : 1573037090581
         */

        private long server_timestamp;
        private String transId;
        private String outTransId;

        private String nCurrentPoint;  //当前积分值


        public long getServer_timestamp() {
            return server_timestamp;
        }
        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }


        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getOutTransId() {
            return outTransId;
        }

        public void setOutTransId(String outTransId) {
            this.outTransId = outTransId;
        }

        public String getNCurrentPoint() {
            return nCurrentPoint;
        }

        public void setNCurrentPoint(String nCurrentPoint) {
            this.nCurrentPoint = nCurrentPoint;
        }


    }
}
