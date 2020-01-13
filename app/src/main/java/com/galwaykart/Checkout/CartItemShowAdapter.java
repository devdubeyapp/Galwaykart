package com.galwaykart.Checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**Cart Item adapter
 * Created by ankesh on 10/3/2017.
 */

public class CartItemShowAdapter extends BaseAdapter {

    Context mContext;
    String[] arr_cart_qty,arr_item_name,arr_item_price,arr_boolean,arr_updated_cart_qty,arr_sku;
    ViewHolder holder;
    String tokenData;
    String image_path = com.galwaykart.essentialClass.Global_Settings.api_url+"pub/media/catalog/product";

    public CartItemShowAdapter(Context ctx,String[] arr_sku,String[] arr_cart_qty,String[] arr_item_name,String[]arr_item_price,String[] arr_boolean){

        this.mContext=ctx;
        this.arr_sku=arr_sku;
        this.arr_cart_qty = arr_cart_qty;
        this.arr_item_name = arr_item_name;
        this.arr_item_price = arr_item_price;
        this.arr_boolean = arr_boolean;

    }

    @Override
    public int getCount() {
        return arr_item_price.length;
    }

    @Override
    public Object getItem(int position) {
        return arr_item_name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.activity_cart_item_list, parent, false);
            holder = new ViewHolder();
            holder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.textView_itemprice = (TextView) convertView.findViewById(R.id.textView_itemprice);
            holder.itemqty = (TextView) convertView.findViewById(R.id.itemqty);

            holder.iv_minus_cart_item = (ImageView)convertView.findViewById(R.id.iv_minus_cart_item);
            holder.add_item = (ImageView)convertView.findViewById(R.id.add_item);
            holder.btUpdate = (Button)convertView.findViewById(R.id.btUpdate);
            holder.imageView_Item=(ImageView)convertView.findViewById(R.id.imageView_Item);

            holder.btDelete=(Button)convertView.findViewById(R.id.btDelete);

            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.btDelete.setVisibility(View.GONE);

        holder.textView_name.setText(arr_item_name[position]);
        holder.textView_itemprice.setText(arr_item_price[position]);
        holder.itemqty.setText(arr_cart_qty[position]);


        holder.iv_minus_cart_item.setVisibility(View.GONE);
        holder.add_item.setVisibility(View.GONE);



        String update_cart = arr_boolean[position];
//            String updated_cart_qty = arr_updated_cart_qty[position];
//
//                 holder.itemqty.setText(updated_cart_qty);


        if(update_cart.equalsIgnoreCase("true"))
            holder.btUpdate.setVisibility(View.VISIBLE);

        String sku_prod=arr_sku[position];
        String imagepath="";
        DatabaseHandler dbh=new DatabaseHandler(mContext);
        if(dbh.getCartProductImageSKuCount()>0) {
            List<CartProductImage> contacts = dbh.getCartProductImage(sku_prod);

            for (CartProductImage cn : contacts) {
                imagepath = cn.get_image();
            }
        }

        if(!imagepath.equals("")) {
            Picasso.get()
                    .load(imagepath)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    // .resize(200, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.imageView_Item);
        }


        return convertView;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView textView_name;
        TextView textView_itemprice;
        TextView itemqty;

        ImageView iv_minus_cart_item;
        ImageView add_item;
        Button btUpdate;
        Button btDelete;
        ImageView imageView_Item;
    }


}
