package com.galwaykart.RoomDb;

import androidx.annotation.NonNull;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "table_top_product")
public class Tbl_Top_Product {

//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    @ColumnInfo(name = "p_id")
//    private int p_id;


//    @NonNull
//    @ColumnInfo(name = "p_name")
    private String p_name;

    //@ColumnInfo(name = "p_image")
    private String p_image;

//    @PrimaryKey
//    @NonNull
//    @ColumnInfo(name = "p_sku")
    private String p_sku;

    //@ColumnInfo(name = "p_ip")
    private String p_ip;

//    public int getP_id() {
//        return p_id;
//    }

    public String getP_name() {
        return p_name;
    }

    public String getP_image() {
        return p_image;
    }

    public String getP_sku() {
        return p_sku;
    }

    public String getP_ip() {
        return p_ip;
    }



    public Tbl_Top_Product(String p_name, String p_image, String p_sku, String p_ip) {
        this.p_name = p_name;
        this.p_image = p_image;
        this.p_sku = p_sku;
        this.p_ip = p_ip;
    }



}
