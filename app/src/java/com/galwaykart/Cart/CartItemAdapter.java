package com.galwaykart.Cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.MainActivity;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter of cart item
 *
 */

public class CartItemAdapter extends BaseAdapter {

    Context mContext;
    String[] arr_cart_qty,arr_item_name,arr_item_price,arr_boolean,arr_updated_cart_qty,arr_sku;

    String tokenData;
    String image_path = com.galwaykart.essentialClass.Global_Settings.api_url+"pub/media/catalog/product";

    public CartItemAdapter(Context ctx,String[] arr_sku,String[] arr_cart_qty,String[] arr_item_name,String[]arr_item_price,String[] arr_boolean){

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder = new ViewHolder();
        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.single_cart_item, parent, false);
        }
            holder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.textView_itemprice = (TextView) convertView.findViewById(R.id.textView_itemprice);
            holder.itemqty = (TextView) convertView.findViewById(R.id.itemqty);

            holder.iv_minus_cart_item = (ImageView)convertView.findViewById(R.id.iv_minus_cart_item);
            holder.add_item = (ImageView)convertView.findViewById(R.id.add_item);
            holder.btUpdate = (Button)convertView.findViewById(R.id.btUpdate);
            holder.btDelete=(Button)convertView.findViewById(R.id.btDelete);

            holder.imageView_Item=(ImageView)convertView.findViewById(R.id.imageView_Item);

            holder.textView_name.setText(arr_item_name[position]);

            holder.textView_price=(TextView)convertView.findViewById(R.id.textView_price);


            if(arr_item_price[position].equalsIgnoreCase("0")) {
                holder.textView_itemprice.setVisibility(View.GONE);
                holder.textView_price.setVisibility(View.GONE);
            }
                holder.textView_itemprice.setText(arr_item_price[position]);
            holder.itemqty.setText(arr_cart_qty[position]);
            //holder.itemqty.setText("1997");
        /**
         * Spinner quantity added
         */
        holder.ed_qty=(EditText)convertView.findViewById(R.id.ed_qty);

        holder.spinner_qty=(Spinner)convertView.findViewById(R.id.spinner_qty);
        String[] items3 = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "10", "10+"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items3);
        holder.spinner_qty.setAdapter(adapter3);

        if(Integer.parseInt(arr_cart_qty[position])<=10) {
            holder.spinner_qty.setSelection(adapter3.getPosition(arr_cart_qty[position]));
            holder.spinner_qty.setVisibility(View.VISIBLE);
            holder.ed_qty.setVisibility(View.GONE);
        }
        else
        {
            holder.spinner_qty.setVisibility(View.GONE);
            holder.ed_qty.setVisibility(View.VISIBLE);
            holder.ed_qty.setText(arr_cart_qty[position]);
        }

        if(Integer.parseInt(arr_cart_qty[position])==0){
            holder.spinner_qty.setVisibility(View.INVISIBLE);
            //holder.ed_qty.setVisibility(View.GONE);
            holder.iv_minus_cart_item.setVisibility(View.INVISIBLE);
            holder.add_item.setVisibility(View.INVISIBLE);
            //holder.ed_qty.setText("Out Of Stock");
            holder.itemqty.setText("Out Of Stock");
            holder.itemqty.setTextColor(Color.RED);
            holder.itemqty.getLayoutParams().width=400;
            holder.itemqty.setBackground(ContextCompat.getDrawable(mContext,R.drawable.blank));

        }
        else
        {
            holder.spinner_qty.setVisibility(View.VISIBLE);
            //holder.ed_qty.setVisibility(View.GONE);
            holder.iv_minus_cart_item.setVisibility(View.VISIBLE);
            holder.add_item.setVisibility(View.VISIBLE);
            holder.itemqty.getLayoutParams().width=96;
            //holder.itemqty.setText("1997");
            holder.itemqty.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.itemqty.setBackground(ContextCompat.getDrawable(mContext,R.drawable.cart));
        }

        holder.btUpdate.setVisibility(View.GONE);

//        holder.spinner_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                String selqty=holder.spinner_qty.getSelectedItem().toString();
//                // Toast.makeText(MainActivity.this,selqty,Toast.LENGTH_LONG).show();
//
//
//                if(selqty.equalsIgnoreCase("10+")){
//
//                    holder.spinner_qty.setVisibility(View.GONE);
//                    holder.ed_qty.setText("10");
//                    holder.ed_qty.setVisibility(View.VISIBLE);
//                    holder.btUpdate.setVisibility(View.VISIBLE);
//
//                }}
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });







        String update_cart = arr_boolean[position];
//            String updated_cart_qty = arr_updated_cart_qty[position];
//
//                 holder.itemqty.setText(updated_cart_qty);


            if(update_cart.equalsIgnoreCase("true")) {
                holder.btUpdate.setVisibility(View.VISIBLE);
            }
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
    public static class ViewHolder {
        TextView textView_name;
        TextView textView_itemprice;
        TextView itemqty;
        TextView textView_price;

        ImageView iv_minus_cart_item;
        ImageView add_item;
        Button btUpdate;
        Button btDelete;
        ImageView imageView_Item;
         Spinner spinner_qty;
         EditText ed_qty;
    }


}
