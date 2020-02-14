package com.galwaykart.CAdapter;

/**
 * Created by ankesh on 9/20/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.R;
import com.galwaykart.Guest.GuestMainActivity;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sumitsaini on 9/12/2017.
 */

public class RecyclerViewHomeTopProductAdapter extends RecyclerView.Adapter<RecyclerViewHomeTopProductAdapter.ViewHolder> {


    private List<DataModelHomeProduct> mValues;
    Context mContext;
    protected ItemListener mListener;
    View view;

    public RecyclerViewHomeTopProductAdapter(Context context, List<DataModelHomeProduct> values) {

        mValues = values;
        mContext = context;
//        mListener=itemListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_hometop, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataModelHomeProduct dataModel = mValues.get(position);
        holder.textView_name.setText(dataModel.pname);
        holder.textView_price.setText("₹  "+dataModel.price);
        holder.textView_price.setVisibility(View.GONE);

        SharedPreferences pref = CommonFun.getPreferences(mContext);
        String login_group_id=pref.getString("login_group_id","");
        if(login_group_id.equals("4")) {
            if (!TextUtils.isEmpty(dataModel.ip))
                holder.tv_ipss.setText(dataModel.ip);
            else
                holder.tv_ipss.setVisibility(View.GONE);
        }
        else
        {
            holder.tv_ipss.setVisibility(View.GONE);
        }

        final String current_sku= dataModel.getSku();

        Picasso.get()
                .load(dataModel.getImage())
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)      // optional
                .resize(200, 300)
                //.rotate(90)                             // optional
                //.networkPolicy(NetworkPolicy.)
                .into(holder.imageView_product);

        holder.imageView_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(mContext, current_sku.toString(), Toast.LENGTH_SHORT).show();

                SharedPreferences pref;
                pref= CommonFun.getPreferences(mContext);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("showitemsku", current_sku);
                editor.commit();

                String email = pref.getString("user_email","");
                if(email!=null && !email.equals("")) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }
                else
                {
                    Intent intent = new Intent(mContext, GuestMainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }




            }
        });

//        holder.textView_more_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //Toast.makeText(mContext, current_sku.toString(), Toast.LENGTH_SHORT).show();
//
//                SharedPreferences pref;
//                pref= CommonFun.getPreferences(mContext);
//                SharedPreferences.Editor editor=pref.edit();
//                editor.putString("showitemsku",current_sku.toString());
//                editor.commit();
//
//                Intent intent=new Intent(mContext, MainActivity.class);
//                mContext.startActivity(intent);
//                ((Activity)mContext).finish();
//
//
//
//
//
//            }
//        });



//        holder.setData(mValues.get(position));



    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_price,textView_more_link,tv_ipss;
        ImageView imageView_product;

        DataModelHomeProduct item;

        public ViewHolder(View v) {

            super(v);

//            v.setOnClickListener(this);
            textView_name = v.findViewById(R.id.textView_name);
            textView_price = v.findViewById(R.id.textView_price);
            imageView_product = v.findViewById(R.id.imageView_product);
            textView_more_link = v.findViewById(R.id.textView_more_link);
            tv_ipss= v.findViewById(R.id.tv_ipss);

        }



//        public void setData(DataModel item) {
//            this.item = item;
//
//            textView_name.setText(item.st_tv_product_name);
//            textView_price.setText(item.st_tv_product_price);
//
//
//            Picasso.get()
//                    .load(item.st_image)
//                    .placeholder(R.drawable.imageloading)   // optional
//                    .error(R.drawable.noimage)      // optional
//                    .resize(200, 300)
//
//                    .into(imageView_product);
//
//
//        }


//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//                mListener.onItemClick(item);
//            }
//        }
    }

    public interface ItemListener {
        void onItemClick(DataModelHomeProduct item);
    }
}
