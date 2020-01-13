package com.galwaykart.CAdapter;

/**
 * Created by ankesh on 9/20/2017.
 */

public class DataModelHomeProduct {

    String pname;
    String ip;
    String price;
    String sku;
    String image;

    public DataModelHomeProduct(String pname, String price, String sku, String image,String ip) {
        this.pname = pname;
        this.price = price;
        this.sku = sku;
        this.image = image;
        this.ip=ip;

    }



    public String getIp() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
