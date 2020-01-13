package com.galwaykart.productList;

/**
 * Model View of Product list
 * Created by sumitsaini on 9/12/2017.
 */

public class DataModel {

    String st_image="",st_tv_product_name,st_tv_product_price,sku,ip;

    public DataModel(String img,String pro_name,String pro_price,String sku,String ip)
    {
        this.st_image = img;
        this.st_tv_product_name = pro_name;
        this.st_tv_product_price = pro_price;
        this.sku = sku;
        this.ip=ip;
    }

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getst_image() {
        return st_image;
    }

    public void setst_image(String st_image) {
        this.st_image = st_image;
    }

    public String getst_tv_product_name() {
        return st_tv_product_name;
    }

    public void setst_tv_product_name(String st_tv_product_name) {
        this.st_tv_product_name= st_tv_product_name;
    }

    public String getst_tv_product_price() {
        return st_tv_product_price;
    }

    public void setst_tv_product_price(String  st_tv_product_price) {
        this.st_tv_product_price = st_tv_product_price;
    }


}
