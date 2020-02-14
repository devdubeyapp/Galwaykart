package com.galwaykart.profile.wishList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.squareup.picasso.Picasso;

/**
 * Created by sumitsaini on 10/4/2017.
 */

public class WishListItemAdapter extends BaseAdapter {

    Context mContext;
    String[] arr_image,arr_wishlist_name,arr_wishlist_price,arr_wishlist_sku;
    ViewHolder holder;
    String wishlist_imagepath = "";


   public WishListItemAdapter(Context ctx,
                              String[] arr_image, String[] arr_wishlist_name,
                              String[] arr_wishlist_price,
                              String[] arr_wishlist_sku){

       this.mContext = ctx;
       this.arr_image = arr_image;
       this.arr_wishlist_name = arr_wishlist_name;
       this.arr_wishlist_price = arr_wishlist_price;
       this.arr_wishlist_sku=arr_wishlist_sku;

   }
   @Override
    public int getCount() {
        return arr_wishlist_name.length;
    }

    @Override
    public Object getItem(int position) {
        return arr_wishlist_name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = new ViewHolder();
        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.activity_wish_list_item_list, parent, false);
        }
        holder.textView_wishlist_name = convertView.findViewById(R.id.textView_wishlist_name);
        holder.textView_wishlist_itemprice = convertView.findViewById(R.id.textView_wishlist_itemprice);
        holder.bt_wishlist_Delete = convertView.findViewById(R.id.bt_wishlist_Delete);
        holder.imageView_Wishlist_Item= convertView.findViewById(R.id.imageView_Wishlist_Item);

        wishlist_imagepath = arr_image[position];

        holder.textView_wishlist_name.setText(arr_wishlist_name[position]);
        holder.textView_wishlist_itemprice.setText(arr_wishlist_price[position]);

        holder.textView_wishlist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(arr_wishlist_sku[position]);
            }
        });
        holder.imageView_Wishlist_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProductDetail(arr_wishlist_sku[position]);


            }
        });

        if(!wishlist_imagepath.equals("")) {
            Picasso.get()
                    .load(wishlist_imagepath)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .into(holder.imageView_Wishlist_Item);
        }
        return convertView;

    }

    private void openProductDetail(String value) {
        SharedPreferences pref;
        pref= CommonFun.getPreferences(mContext);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString("showitemsku", value);
        editor.commit();

        Intent intent=new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }


    // View lookup cache
    private static class ViewHolder {
        TextView textView_wishlist_name;
        TextView textView_wishlist_itemprice;
        Button bt_wishlist_Delete;
        ImageView imageView_Wishlist_Item;
    }
}
