package com.galwaykart.profile.wishList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.R;
import com.squareup.picasso.Picasso;

/**
 * Created by sumitsaini on 10/4/2017.
 */

public class WishListItemAdapter extends BaseAdapter {

    Context mContext;
    String[] arr_image,arr_wishlist_name,arr_wishlist_price;
    ViewHolder holder;
    String wishlist_imagepath = "";


   public WishListItemAdapter(Context ctx, String[] arr_image, String[] arr_wishlist_name,String[]arr_wishlist_price){

       this.mContext = ctx;
       this.arr_image = arr_image;
       this.arr_wishlist_name = arr_wishlist_name;
       this.arr_wishlist_price = arr_wishlist_price;

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
        holder.textView_wishlist_name = (TextView) convertView.findViewById(R.id.textView_wishlist_name);
        holder.textView_wishlist_itemprice = (TextView) convertView.findViewById(R.id.textView_wishlist_itemprice);
        holder.bt_wishlist_Delete = (Button)convertView.findViewById(R.id.bt_wishlist_Delete);
        holder.imageView_Wishlist_Item=(ImageView)convertView.findViewById(R.id.imageView_Wishlist_Item);

        wishlist_imagepath = arr_image[position].toString();

        holder.textView_wishlist_name.setText(arr_wishlist_name[position]);
        holder.textView_wishlist_itemprice.setText(arr_wishlist_price[position]);


        if(!wishlist_imagepath.equals("")) {
            Picasso.get()
                    .load(wishlist_imagepath)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .into(holder.imageView_Wishlist_Item);
        }
        return convertView;

    }


    // View lookup cache
    private static class ViewHolder {
        TextView textView_wishlist_name;
        TextView textView_wishlist_itemprice;
        Button bt_wishlist_Delete;
        ImageView imageView_Wishlist_Item;
    }
}
