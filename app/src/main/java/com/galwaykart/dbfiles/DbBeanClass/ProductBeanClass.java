package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by sumitsaini on 9/13/2017.
 */

public class ProductBeanClass {

    String item_id;
    String sku;
    String name;
    String image;
    String price;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String qty;

    public ProductBeanClass()
    {

    }

  public ProductBeanClass(String item_id,String sku,String name,String image,String price,String qty){

      this.item_id = item_id;
      this.name = name;
      this.sku = sku;
      this.image=image;
      this.price = price;
      this.qty = qty;

  }

    // getting name
    public String getItem_id(){
        return this.item_id;
    }

    // setting name
    public void setItem_id(String item_id){
        this.item_id= item_id;
    }

    // getting name
    public String getSku(){
        return this.sku;
    }

    // setting name
    public void setSku(String sku){
        this.sku = sku;
    }

    // getting name
    public String getName(){
        return this.name;
    }

    // setting name
    public void setName(String name){
        this.name= name;
    }

    // getting name
    public String getPrice(){
        return this.price;
    }

    // setting name
    public void setPrice(String price){
        this.price= price;
    }

    // getting name
    public String getQty(){
        return this.qty;
    }

    // setting name
    public void setQty(String qty){
        this.qty= qty;
    }



}
