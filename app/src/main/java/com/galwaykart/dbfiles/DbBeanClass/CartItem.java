package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by ankesh on 10/9/2017.
 */

public class CartItem {

    String TAG_cart_sku="sku";
    String TAG_cart_name="pname";
    String TAG_cart_image="image";
    String TAG_cart_qty="qty";
    String TAG_cart_price="price";


//    public String getTAG_cart_dp_price() {
//        return TAG_cart_dp_price;
//    }
//
//    public void setTAG_cart_dp_price(String TAG_cart_dp_price) {
//        this.TAG_cart_dp_price = TAG_cart_dp_price;
//    }
//
//    public String getTAG_cart_ip() {
//        return TAG_cart_ip;
//    }
//
//    public void setTAG_cart_ip(String TAG_cart_ip) {
//        this.TAG_cart_ip = TAG_cart_ip;
//    }
//


    public String getTAG_cart_sku() {
        return TAG_cart_sku;
    }

    public void setTAG_cart_sku(String TAG_cart_sku) {
        this.TAG_cart_sku = TAG_cart_sku;
    }

    public String getTAG_cart_name() {
        return TAG_cart_name;
    }

    public void setTAG_cart_name(String TAG_cart_name) {
        this.TAG_cart_name = TAG_cart_name;
    }

    public String getTAG_cart_image() {
        return TAG_cart_image;
    }

    public void setTAG_cart_image(String TAG_cart_image) {
        this.TAG_cart_image = TAG_cart_image;
    }

    public String getTAG_cart_qty() {
        return TAG_cart_qty;
    }

    public void setTAG_cart_qty(String TAG_cart_qty) {
        this.TAG_cart_qty = TAG_cart_qty;
    }

    public String getTAG_cart_price() {
        return TAG_cart_price;
    }

    public void setTAG_cart_price(String TAG_cart_price) {
        this.TAG_cart_price = TAG_cart_price;
    }



    public CartItem(){

    }

    public CartItem(String TAG_cart_sku, String TAG_cart_name,
                    String TAG_cart_image, String TAG_cart_qty,
                    String TAG_cart_price) {
        this.TAG_cart_sku = TAG_cart_sku;
        this.TAG_cart_name = TAG_cart_name;
        this.TAG_cart_image = TAG_cart_image;
        this.TAG_cart_qty = TAG_cart_qty;
        this.TAG_cart_price = TAG_cart_price;
//        this.TAG_cart_dp_price=TAG_cart_dp_price;
//        this.TAG_cart_ip=TAG_cart_ip;
    }





}
