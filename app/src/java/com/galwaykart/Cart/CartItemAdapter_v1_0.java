package com.galwaykart.Cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Cart Item Adapter
 * Created by itsoftware on 08/01/2018.
 */

public class CartItemAdapter_v1_0 extends RecyclerView.Adapter<CartItemAdapter_v1_0.ViewHolder> {


    private List<DataModelCart_v1> mValues;
    Context mContext;
    protected ItemListener mListener;
    View view;


    public CartItemAdapter_v1_0(Context context, List<DataModelCart_v1> values) {

        mContext=context;
        mValues=values;

    }

    public CartItemAdapter_v1_0(Context context, List<DataModelCart_v1> values,Boolean is_visible) {

        mContext=context;
        mValues=values;

    }




    @Override
    public int getItemCount() {

        ////Log.d("All size", String.valueOf(mValues.size()));
        return mValues.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);

        return new ViewHolder(view);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View convertView) {

            super(convertView);



            textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            imageView_Item=(ImageView)convertView.findViewById(R.id.imageView_Item);


            textView_itemprice = (TextView) convertView.findViewById(R.id.textView_itemprice);

            itemqty = (TextView) convertView.findViewById(R.id.itemqty);

            iv_minus_cart_item = (ImageView)convertView.findViewById(R.id.iv_minus_cart_item);

            add_item = (ImageView)convertView.findViewById(R.id.add_item);

            btUpdate = (Button)convertView.findViewById(R.id.btUpdate);

            btDelete=(Button)convertView.findViewById(R.id.btDelete);


            ed_qty=(EditText)convertView.findViewById(R.id.ed_qty);

            spinner_qty=(Spinner)convertView.findViewById(R.id.spinner_qty);
            //textView_name.setText(arr_item_name[position]);

            textView_price=(TextView)convertView.findViewById(R.id.textView_price);
            textView_price.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataModelCart_v1 dataModel = mValues.get(position);

        final String prod_name=dataModel.getArr_item_name().toString();

        final String prod_price=dataModel.getArr_item_price().toString();

        if(prod_price.equalsIgnoreCase("0")) {
            holder.textView_itemprice.setVisibility(View.GONE);
            holder.textView_price.setVisibility(View.GONE);
        }

        holder.textView_name.setText(prod_name);
        holder.textView_itemprice.setText(dataModel.getArr_item_price());
        holder.itemqty.setText(dataModel.getArr_cart_qty());

/**
 * fill quantity details
 */
        String[] items3 = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "10", "10+"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items3);
        holder.spinner_qty.setAdapter(adapter3);

        if(Integer.parseInt(dataModel.getArr_cart_qty())<=10) {
            holder.spinner_qty.setSelection(adapter3.getPosition(dataModel.getArr_cart_qty()));
            holder.spinner_qty.setVisibility(View.VISIBLE);
            holder.ed_qty.setVisibility(View.GONE);
        }
        else
        {
            holder.spinner_qty.setVisibility(View.GONE);
            holder.ed_qty.setVisibility(View.VISIBLE);
            holder.ed_qty.setText(dataModel.getArr_cart_qty());
        }

        if(Integer.parseInt(dataModel.getArr_cart_qty())==0){
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


        String update_cart = dataModel.getArr_boolean();

        if(update_cart.equalsIgnoreCase("true")) {
            holder.btUpdate.setVisibility(View.VISIBLE);
        }
        String sku_prod=dataModel.getArr_sku();
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

    }

    private void finishScreen(){

    }


    private void openItem(String selItemSku){
        //String selItemSku = data;
        SharedPreferences pref;
        pref =  CommonFun.getPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("showitemsku", selItemSku);
        editor.commit();

        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        //CommonFun.finishscreen(mContext);
    }


    public interface ItemListener {
        void onItemClick(DataModelRecentItem item);
    }


    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if(convertView==null){
//
//            convertView = layoutInflater.inflate(R.layout.product_customer_review, null);
//            holder = new Holder();
//
//            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_customer);
//            holder.ratingBar.setNumStars(5);
//            holder.text_detail = (TextView) convertView.findViewById(R.id.text_desc);
//            holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
//            holder.text_nickname = (TextView) convertView.findViewById(R.id.text_nickname);
//
//            convertView.setTag(holder);
//        }
//
//        holder.text_detail.setText(arrayList.get(position).get(TAG_detail).toString());
//        holder.text_nickname.setText(arrayList.get(position).get(TAG_nickname).toString());
//        holder.text_title.setText(arrayList.get(position).get(TAG_title).toString());
//        holder.ratingBar.setRating(Float.parseFloat(arrayList.get(position).get(TAG_rating).toString()));
//
//
//       return  convertView;
//
//    }
}
