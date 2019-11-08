package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaysuccessofdeviceEntity implements Serializable {


    /**
     * return : {"nCode":2,"strText":"正在等待用户付款","strInfo":""}
     * response : {"server_timestamp":1572849985038,"transId":"","userId":"","bSubmitOrder":false}
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
         * strText : 正在等待用户付款
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
         * server_timestamp : 1572849985038
         * transId :
         * userId :
         * bSubmitOrder : false
         */

        private long server_timestamp;
        private String transId;
        private String userId;
        private boolean bSubmitOrder;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isBSubmitOrder() {
            return bSubmitOrder;
        }

        public void setBSubmitOrder(boolean bSubmitOrder) {
            this.bSubmitOrder = bSubmitOrder;
        }
    }

}
