package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClearCarEntity  implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"CartService.deleteCart.OK"}
     * response : {"server_timestamp":1572949839609}
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
         * strInfo : CartService.deleteCart.OK
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
         * server_timestamp : 1572949839609
         */

        private long server_timestamp;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }
    }
}
