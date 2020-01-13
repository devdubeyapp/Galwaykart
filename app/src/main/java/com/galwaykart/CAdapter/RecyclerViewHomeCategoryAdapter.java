package com.galwaykart.CAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.galwaykart.Guest.GuestProductListActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.productList.ProductListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ankesh on 9/20/2017.
 */

public class RecyclerViewHomeCategoryAdapter extends RecyclerView.Adapter<RecyclerViewHomeCategoryAdapter.ViewHolder> {


    private List<DataModelHomeCategory> mValues;
    Context mContext;
    protected ItemListener mListener;
    View view;


    public RecyclerViewHomeCategoryAdapter(Context context, List<DataModelHomeCategory> values) {

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

        final String current_id=dataModel.getId().toString();

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
//                SharedPreferences.Editor editor=pref.edit();
//                editor.putString("showitemsku",current_sku.toString());
//                editor.commit();

//                Intent intent=new Intent(mContext, MainActivity.class);
//                mContext.startActivity(intent);

String value_email=pref.getString("login_email","");
if(value_email!=null && !value_email.equals(""))
{
if(!current_id.equals("") && current_id!=null) {


    pref = mContext.getSharedPreferences("GalwayKart", mContext.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("selected_id", current_id);
    editor.putString("selected_name", "");
    editor.commit();

    Intent intent = new Intent(mContext, ProductListActivity.class);
    intent.putExtra("onback", "home");
    mContext.startActivity(intent);
    ((Activity) mContext).finish();
}

}
else
{
    pref = mContext.getSharedPreferences("GalwayKart", mContext.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("selected_id", current_id);
    editor.putString("selected_name", "");
    editor.commit();

    Intent intent = new Intent(mContext, ProductListActivity.class);
    intent.putExtra("onback", "home");
    mContext.startActivity(intent);
    ((Activity) mContext).finish();
}
   }
        });


//        holder.setData(mValues.get(position));



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
            textView_name = (TextView) v.findViewById(R.id.textView_name);
            textView_price = (TextView) v.findViewById(R.id.textView_price);
            imageView_product = (ImageView) v.findViewById(R.id.imageView_product);


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
