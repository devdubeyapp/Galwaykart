package com.galwaykart.Cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by itsoftware on 04/01/2018.
 */

public class RecentItemAdapter extends RecyclerView.Adapter<RecentItemAdapter.ViewHolder> {


    private List<DataModelRecentItem> mValues;
    Context mContext;
    protected ItemListener mListener;
    View view;


    public RecentItemAdapter(Context context, List<DataModelRecentItem> values) {

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
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_view_item_layout_list, parent, false);

        return new ViewHolder(view);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name;
        TextView textView_itemprice;
       // TextView itemqty;
        //TextView textView_price;

       // ImageView iv_minus_cart_item;
       // ImageView add_item;
        //Button btUpdate;
       // Button btDelete;
        ImageView imageView_Item;
        //Spinner spinner_qty;
        //EditText ed_qty;

        public ViewHolder(View convertView) {

            super(convertView);



            textView_name = convertView.findViewById(R.id.textView_name);
            imageView_Item= convertView.findViewById(R.id.imageView_Item);


            textView_itemprice = convertView.findViewById(R.id.textView_itemprice);
            textView_itemprice.setVisibility(View.GONE);

        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataModelRecentItem dataModel = mValues.get(position);

        final String prod_name= dataModel.getP_name();
        final String img_name= dataModel.getP_img();
        final String prod_sku= dataModel.getP_sku();
        final String prod_price= dataModel.getP_price();


        holder.textView_itemprice.setVisibility(View.VISIBLE);
        holder.textView_itemprice.setText(prod_price);

        holder.textView_name.setText(prod_name);
        holder.textView_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openItem(dataModel.getP_sku());


            }
        });

        holder.imageView_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openItem(dataModel.getP_sku());

            }
        });


        if(img_name!=null && !img_name.equals("")) {
            Picasso.get()
                    .load(img_name)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(200, 300)

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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
        //CommonFun.finishscreen(mContext);
    }

    public interface ItemListener {
        void onItemClick(DataModelRecentItem item);
    }

}
