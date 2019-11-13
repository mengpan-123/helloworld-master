package com.ceshi.helloworld.bean;

import java.io.Serializable;
import java.util.List;

public class RequestDeleteGoods implements Serializable {

    /**
     * storeId : S1501
     * userId : 526374
     * itemMap : [{"barcode":"692116850925"}]
     */

    private String storeId;
    private String userId;
    private List<ItemMapBean> itemMap;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ItemMapBean> getItemMap() {
        return itemMap;
    }

    public void setItemMap(List<ItemMapBean> itemMap) {
        this.itemMap = itemMap;
    }

    public static class ItemMapBean {
        /**
         * barcode : 692116850925
         */

        private String barcode;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }
}
