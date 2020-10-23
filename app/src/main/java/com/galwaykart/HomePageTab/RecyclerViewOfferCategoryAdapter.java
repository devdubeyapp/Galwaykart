package com.galwaykart.HomePageTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.CAdapter.DataModelHomeProduct;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.productList.ProductListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.galwaykart.Guest.GuestProductListActivity;

/**
 * Created by ankesh on 9/20/2017.
 */

public class RecyclerViewOfferCategoryAdapter extends RecyclerView.Adapter<RecyclerViewOfferCategoryAdapter.ViewHolder> {


    private List<DataModelHomeCategory> mValues;
    Context mContext;
    //protected ItemListener mListener;
    View view;


    public RecyclerViewOfferCategoryAdapter(Context context, List<DataModelHomeCategory> values) {

        mValues = values;
        mContext = context;
//        mListener=itemListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_category_layout, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataModelHomeCategory dataModel = mValues.get(position);

        final String current_id= dataModel.getId();


        Picasso.get()
                .load(dataModel.getImage())
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)      // optional
//                .resize(220, 300)
                //.rotate(90)                             // optional
                //.networkPolicy(NetworkPolicy.)
                .into(holder.imageView_product);

        holder.imageView_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, current_sku.toString(), Toast.LENGTH_SHORT).show();

                SharedPreferences pref;
                pref= CommonFun.getPreferences(mContext);

                String value_email=pref.getString("login_email","");
//                if(value_email!=null && !value_email.equals(""))
//                {
                    if(!current_id.equals("") && !current_id.equalsIgnoreCase("0")) {

                        pref = mContext.getSharedPreferences("GalwayKart", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("selected_id", current_id);
                        Log.e("selected_id",current_id);
                        editor.putString("selected_name", "");
                        editor.commit();

                        Intent intent = new Intent(mContext, ProductListActivity.class);
                        intent.putExtra("onback", "home");
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }



            }
        });




    }

    private void finishScreen(){

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_price;
        ImageView imageView_product;

        DataModelHomeProduct item;

        public ViewHolder(View v) {

            super(v);

//            v.setOnClickListener(this);
            textView_name = v.findViewById(R.id.textView_name);
            textView_price = v.findViewById(R.id.textView_price);
            imageView_product = v.findViewById(R.id.imageView_product);


        }

    }

}
