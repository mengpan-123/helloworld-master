package com.ceshi.helloworld.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetHyInfoEntity implements Serializable {

    /**
     * return : {"nCode":0,"strText":"OK","strInfo":"OK"}
     * response : {"server_timestamp":1573443983295,"cdoData":{"sMemberId":"6000482469","nPoint":"0","strName":"陈步桥","sBindMobile":"15201341046","sIDCard":"410882198502160516","dBirthDay":"1985-02-16","nSex":"0"}}
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
         * server_timestamp : 1573443983295
         * cdoData : {"sMemberId":"6000482469","nPoint":"0","strName":"陈步桥","sBindMobile":"15201341046","sIDCard":"410882198502160516","dBirthDay":"1985-02-16","nSex":"0"}
         */

        private long server_timestamp;
        private CdoDataBean cdoData;

        public long getServer_timestamp() {
            return server_timestamp;
        }

        public void setServer_timestamp(long server_timestamp) {
            this.server_timestamp = server_timestamp;
        }

        public CdoDataBean getCdoData() {
            return cdoData;
        }

        public void setCdoData(CdoDataBean cdoData) {
            this.cdoData = cdoData;
        }

        public static class CdoDataBean {
            /**
             * sMemberId : 6000482469
             * nPoint : 0
             * strName : 陈步桥
             * sBindMobile : 15201341046
             * sIDCard : 410882198502160516
             * dBirthDay : 1985-02-16
             * nSex : 0
             */

            private String sMemberId;
            private String nPoint;
            private String strName;
            private String sBindMobile;
            private String sIDCard;
            private String dBirthDay;
            private String nSex;

            public String getSMemberId() {
                return sMemberId;
            }

            public void setSMemberId(String sMemberId) {
                this.sMemberId = sMemberId;
            }

            public String getNPoint() {
                return nPoint;
            }

            public void setNPoint(String nPoint) {
                this.nPoint = nPoint;
            }

            public String getStrName() {
                return strName;
            }

            public void setStrName(String strName) {
                this.strName = strName;
            }

            public String getSBindMobile() {
                return sBindMobile;
            }

            public void setSBindMobile(String sBindMobile) {
                this.sBindMobile = sBindMobile;
            }

            public String getSIDCard() {
                return sIDCard;
            }

            public void setSIDCard(String sIDCard) {
                this.sIDCard = sIDCard;
            }

            public String getDBirthDay() {
                return dBirthDay;
            }

            public void setDBirthDay(String dBirthDay) {
                this.dBirthDay = dBirthDay;
            }

            public String getNSex() {
                return nSex;
            }

            public void setNSex(String nSex) {
                this.nSex = nSex;
            }
        }
    }
}
