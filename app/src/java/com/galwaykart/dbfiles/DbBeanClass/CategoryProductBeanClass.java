package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by sumitsaini on 9/13/2017.
 */

public class CategoryProductBeanClass {

    String cat_id,cat_name,cat_image_path;

    public CategoryProductBeanClass(){

    }

    public CategoryProductBeanClass(String cat_id,String cat_name,String cat_image_path){

        this.cat_id = cat_id;
        this.cat_name= cat_name;
        this.cat_image_path = cat_image_path;

    }

    // getting name
    public String getCat_id(){
        return this.cat_id;
    }

    // setting name
    public void setCat_id(String cat_id){
        this.cat_id= cat_id;
    }

    // getting name
    public String getCat_name(){
        return this.cat_name;
    }

    // setting name
    public void setCat_name(String cat_name){
        this.cat_name= cat_name;
    }

    // getting name
    public String getCat_image_path(){
        return this.cat_image_path;
    }

    // setting name
    public void setCat_image_path(String cat_image_path){
        this.cat_image_path= cat_image_path;
    }

}
