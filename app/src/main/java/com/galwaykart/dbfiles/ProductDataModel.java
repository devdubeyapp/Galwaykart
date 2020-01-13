package com.galwaykart.dbfiles;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;


import com.galwaykart.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.realm.RealmObject;

//@Entity(tableName = "table_product_detail")
public class ProductDataModel extends RealmObject{


//    @PrimaryKey
//    @NonNull
//    //@ColumnInfo(name = "sku")
    public String sku;

    //@ColumnInfo(name = "pname")
    public String pname;

    //@ColumnInfo(name = "ip")
    public String ip;

    //@ColumnInfo(name = "price")
    public String price;

    //@ColumnInfo(name = "imageUrl")
    public String imageUrl;

    //@ColumnInfo(name = "p_category_id")
    public String p_category_id;

    //@ColumnInfo(name = "p_category_name")
    public String p_category_name;

    //@ColumnInfo(name = "login_user_id")
    public String login_user_id;

    public ProductDataModel(){}

    public ProductDataModel(
            String pname, String ip,
            String price, String sku,
            String image, String p_category_id,
            String p_category_name, String login_user_id) {
        this.pname = pname;
        this.ip = ip;
        this.price = price;
        this.sku = sku;
        this.imageUrl = image;
        this.p_category_id=p_category_id;
        this.p_category_name=p_category_name;
        this.login_user_id=login_user_id;

    }

    public String getP_category_id() {
        return p_category_id;
    }

    public void setP_category_id(String p_category_id) {
        this.p_category_id = p_category_id;
    }

    public String getP_category_name() {
        return p_category_name;
    }

    public void setP_category_name(String p_category_name) {
        this.p_category_name = p_category_name;
    }

    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }




    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView,String imageUrl){


        //Log.d("comp_url",imageUrl);
        if(imageUrl!=null && !imageUrl.equals("")) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(200, 300)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    //.memoryPolicy()
                    .into(imageView);
        }
        else {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(200, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);
        }
    }




}
