package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by ankesh on 10/10/2017.
 */

public class CategoryList {

   String TAG_cat_name="catname";
    String TAG_cat_sub_name="subcatname";
    String TAG_cat_sub_id="subcatid";

    public String getTAG_cat_name() {
        return TAG_cat_name;
    }

    public void setTAG_cat_name(String TAG_cat_name) {
        this.TAG_cat_name = TAG_cat_name;
    }

    public String getTAG_cat_sub_name() {
        return TAG_cat_sub_name;
    }

    public void setTAG_cat_sub_name(String TAG_cat_sub_name) {
        this.TAG_cat_sub_name = TAG_cat_sub_name;
    }

    public String getTAG_cat_sub_id() {
        return TAG_cat_sub_id;
    }

    public void setTAG_cat_sub_id(String TAG_cat_sub_id) {
        this.TAG_cat_sub_id = TAG_cat_sub_id;
    }


    public CategoryList(){

    }
    public CategoryList(String TAG_cat_name, String TAG_cat_sub_name, String TAG_cat_sub_id) {
        this.TAG_cat_name = TAG_cat_name;
        this.TAG_cat_sub_name = TAG_cat_sub_name;
        this.TAG_cat_sub_id = TAG_cat_sub_id;
    }



}
