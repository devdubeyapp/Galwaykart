package com.galwaykart.Cart;

/**
 *
 * Created by itsoftware on 08/01/2018.
 */

public class DataModelRecentItem {

    String p_name;
    String p_sku;
    String p_img;

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



    public DataModelRecentItem(String p_name, String p_sku, String p_img) {
        this.p_name = p_name;
        this.p_sku = p_sku;
        this.p_img = p_img;
    }




}
