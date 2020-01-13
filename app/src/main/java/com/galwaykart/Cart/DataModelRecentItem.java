package com.galwaykart.Cart;

import com.google.auto.value.AutoValue;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 * Created by itsoftware on 08/01/2018.
 */

public class DataModelRecentItem extends RealmObject {



    int p_id;

    String p_name;

    @PrimaryKey
    String p_sku;

    String p_img;

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    String p_price;

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_sku() {
        return p_sku;
    }

    public void setP_sku(String p_sku) {
        this.p_sku = p_sku;
    }

    public String getP_img() {
        return p_img;
    }

    public void setP_img(String p_img) {
        this.p_img = p_img;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public DataModelRecentItem(){

    }

    public DataModelRecentItem(String p_name, String p_sku, String p_img,String p_price,int p_id) {
        this.p_name = p_name;
        this.p_sku = p_sku;
        this.p_img = p_img;
        this.p_price=p_price;
        this.p_id=p_id;
    }




}
