package com.galwaykart.HomePageTab;

import io.realm.RealmObject;

public class DataModelHomeAPI extends RealmObject {

    String p_catid;
    String p_name;
    String p_sku;
    String p_price;
    String p_image;
    String p_banner_category;
    String cat_title;
    String p_loyalty_value;
    String p_category_segregation;

    public String getP_loyalty_value() {
        return p_loyalty_value;
    }

    public void setP_loyalty_value(String p_loyalty_value) {
        this.p_loyalty_value = p_loyalty_value;
    }

    public String getP_category_segregation() {
        return p_category_segregation;
    }

    public void setP_category_segregation(String p_category_segregation) {
        this.p_category_segregation = p_category_segregation;
    }





    public String getP_catid() {
        return p_catid;
    }

    public void setP_catid(String p_catid) {
        this.p_catid = p_catid;
    }

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

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getP_image() {
        return p_image;
    }

    public void setP_image(String p_image) {
        this.p_image = p_image;
    }

    public String getP_banner_category() {
        return p_banner_category;
    }

    public void setP_banner_category(String p_banner_category) {
        this.p_banner_category = p_banner_category;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }





}
