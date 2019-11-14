package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class getWXFacepayAuthInfo implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1573701991302,"out_trade_no":"900150119111400014","authinfo":"uOs/AtaoyKinWb5RkLjunIuk78m9trvnt+C9HlnVIM/MsBv4z06ljmVbf8emF1YIfMSmBXtVUwCdmyBgqkz4dJdag+CCYviFMVX/R5ATC3vG7vtuN876hm150zgu5uGT2hxEP9z2UvJGQux0UNPCI0pMh7B+7SZP75hGzXGLzJxFmv+oc+tNWyh8zFcignt/L9368gdmKvbh3vB4iEHEdbEZRndKwt0fiXRvRq5oLyKEG03TlakULH2UxldlZ9HZxItzOiwWYTYCRG1Be/hJ+ORGzLcwfdCzpjasea244LXxOqHszvOTycbqsYuhdTRS/PEhwBqSav4l6rwu7pSDY+TX6pUhu6D8MvgvI/9IMdvYvYyC9XmOWUfUT3z+YQJrxBefPHjwsM/doK5aYCi+kC28GVm8QE2kO6N6y0Jql+vXk7h2PVLPMVcnUxquabmvBCwEJzIqM4LVK2ADVVcMHJGOTHEc23e4j7nHCXevdY4Xu4IWll3x/j6/5jyAfixGGDJQXm+AbMGxvq8HsOzozl6+UfABaFug5SrUGD0YkFmO1eHOfuiBrqGAFrnacDcWlodTaSWIRYiqrhr+l9bLCk167Kck8KynNtJUZBaXmMLK7qrN4YdTnIqoxCcs","appid":"wx5729940f616f4b4b","mch_id":"1474161602","subAppid":"wx7813c8f8c9903b08","subMchId":"1513495961"}
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
         * server_timestamp : 1573701991302
         * out_trade_no : 900150119111400014
         * authinfo : uOs/AtaoyKinWb5RkLjunIuk78m9trvnt+C9HlnVIM/MsBv4z06ljmVbf8emF1YIfMSmBXtVUwCdmyBgqkz4dJdag+CCYviFMVX/R5ATC3vG7vtuN876hm150zgu5uGT2hxEP9z2UvJGQux0UNPCI0pMh7B+7SZP75hGzXGLzJxFmv+oc+tNWyh8zFcignt/L9368gdmKvbh3vB4iEHEdbEZRndKwt0fiXRvRq5oLyKEG03TlakULH2UxldlZ9HZxItzOiwWYTYCRG1Be/hJ+ORGzLcwfdCzpjasea244LXxOqHszvOTycbqsYuhdTRS/PEhwBqSav4l6rwu7pSDY+TX6pUhu6D8MvgvI/9IMdvYvYyC9XmOWUfUT3z+YQJrxBefPHjwsM/doK5aYCi+kC28GVm8QE2kO6N6y0Jql+vXk7h2PVLPMVcnUxquabmvBCwEJzIqM4LVK2ADVVcMHJGOTHEc23e4j7nHCXevdY4Xu4IWll3x/j6/5jyAfixGGDJQXm+AbMGxvq8HsOzozl6+UfABaFug5SrUGD0YkFmO1eHOfuiBrqGAFrnacDcWlodTaSWIRYiqrhr+l9bLCk167Kck8KynNtJUZBaXmMLK7qrN4YdTnIqoxCcs
         * appid : wx5729940f616f4b4b
         * mch_id : 1474161602
         * subAppid : wx7813c8f8c9903b08
         * subMchId : 1513495961
         */

        private long server_timestamp;
        private String out_trade_no;
        private String authinfo;
        private String appid;
        private String mch_id;
        private String subAppid;
        private String subMchId;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getAuthinfo() {
            return authinfo;
        }

        public void setAuthinfo(String authinfo) {
            this.authinfo = authinfo;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getSubAppid() {
            return subAppid;
        }

        public void setSubAppid(String subAppid) {
            this.subAppid = subAppid;
        }

        public String getSubMchId() {
            return subMchId;
        }

        public void setSubMchId(String subMchId) {
            this.subMchId = subMchId;
        }
    }
}
