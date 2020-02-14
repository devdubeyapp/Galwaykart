package com.galwaykart.helpdesksupport.orderdetails;

public class ComplaintOrderDetailModel {

    String order_id = "";
    String product_id = "";
    String sku = "";
    String product_type = "";
    String product_name = "";
    String product_price= "";
    String qty_ordered= "";
    String order_date_time= "";
    Boolean check_for_return_req;
    String return_qty_req= "";
    String image= "";

    public String getQuote_item_id() {
        return quote_item_id;
    }

    public void setQuote_item_id(String quote_item_id) {
        this.quote_item_id = quote_item_id;
    }

    String quote_item_id="";


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getQty_ordered() {
        return qty_ordered;
    }

    public void setQty_ordered(String qty_ordered) {
        this.qty_ordered = qty_ordered;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public Boolean getCheck_for_return_req() {
        return check_for_return_req;
    }

    public void setCheck_for_return_req(Boolean check_for_return_req) {
        this.check_for_return_req = check_for_return_req;
    }

    public String getReturn_qty_req() {
        return return_qty_req;
    }

    public void setReturn_qty_req(String return_qty_req) {
        this.return_qty_req = return_qty_req;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
