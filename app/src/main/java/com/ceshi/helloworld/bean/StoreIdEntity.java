package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kekex on 2018/10/9.
 * 我先给你装个插件，我用我的studio生成的
 */

public class StoreIdEntity implements Serializable {


    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"mch_id":"1474161602","sub_mch_id":"1520550281"}
     */

    /**
     * 这个类自动生成的时候  因为后台返回的return是java里面的关键字 所以或自动转成returnX，上面的SerializedName就
     * 是表示序列化之前的key
     * */
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
         * mch_id : 1474161602
         * sub_mch_id : 1520550281
         */

        private String mch_id;
        private String sub_mch_id;

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getSub_mch_id() {
            return sub_mch_id;
        }

        public void setSub_mch_id(String sub_mch_id) {
            this.sub_mch_id = sub_mch_id;
        }
    }
}
