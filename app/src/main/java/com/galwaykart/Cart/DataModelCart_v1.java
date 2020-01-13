package com.galwaykart.Cart;

/**
 * Created by itsoftware on 08/01/2018.
 */

public class DataModelCart_v1 {

    String arr_cart_qty;
    String arr_item_name;
    String arr_item_price;
    String arr_boolean;
    String arr_updated_cart_qty;
    String arr_sku,product_ip;
    String arr_boolean_edit;

    public String getArr_error_message() {
        return arr_error_message;
    }

    public void setArr_error_message(String arr_error_message) {
        this.arr_error_message = arr_error_message;
    }

    String arr_error_message;

    public void setArr_boolean_edit(String arr_boolean_edit) {
        this.arr_boolean_edit = arr_boolean_edit;
    }

    public String getProduct_ip() {
        return product_ip;
    }

    public void setProduct_ip(String product_ip) {
        this.product_ip = product_ip;
    }

    public String getArr_boolean_edit() {
        return arr_boolean_edit;
    }


    public String getArr_cart_qty() {
        return arr_cart_qty;
    }

    public void setArr_cart_qty(String arr_cart_qty) {
        this.arr_cart_qty = arr_cart_qty;
    }

    public String getArr_item_name() {
        return arr_item_name;
    }

    public void setArr_item_name(String arr_item_name) {
        this.arr_item_name = arr_item_name;
    }

    public String getArr_item_price() {
        return arr_item_price;
    }

    public void setArr_item_price(String arr_item_price) {
        this.arr_item_price = arr_item_price;
    }

    public String getArr_boolean() {
        return arr_boolean;
    }

    public void setArr_boolean(String arr_boolean) {
        this.arr_boolean = arr_boolean;
    }

    public String getArr_updated_cart_qty() {
        return arr_updated_cart_qty;
    }

    public void setArr_updated_cart_qty(String arr_updated_cart_qty) {
        this.arr_updated_cart_qty = arr_updated_cart_qty;
    }

    public String getArr_sku() {
        return arr_sku;
    }

    public void setArr_sku(String arr_sku) {
        this.arr_sku = arr_sku;
    }


    public DataModelCart_v1(String arr_cart_qty, String arr_item_name, String arr_item_price, String arr_boolean,String arr_sku,String product_ip,
                            String arr_boolean_edit,
                            String arr_error_message) {
        this.arr_cart_qty = arr_cart_qty;
        this.arr_item_name = arr_item_name;
        this.arr_item_price = arr_item_price;
        this.arr_boolean = arr_boolean;
        this.arr_sku = arr_sku;
        this.product_ip = product_ip;
        this.arr_boolean_edit = arr_boolean_edit;
        this.arr_error_message=arr_error_message;
    }





}
