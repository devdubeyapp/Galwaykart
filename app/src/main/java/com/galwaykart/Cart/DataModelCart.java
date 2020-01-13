package com.galwaykart.Cart;

import io.realm.RealmModel;

/**
 * Created by sumitsaini on 9/14/2017.
 */

public class DataModelCart {

    String sku,qty,quote_id;


    public DataModelCart(String sku,String qty,String quote_id){

        this.sku = sku;
        this.qty = qty;
        this.quote_id = quote_id;

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }
}
