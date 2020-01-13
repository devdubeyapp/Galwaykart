package com.galwaykart.SingleProductView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.CAdapter.DataModelHomeProduct;
import com.galwaykart.CAdapter.RecyclerViewHomeCategoryAdapter;
import com.galwaykart.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    public ArrayList<ConfigurableProductDataModel> array_value_list;
    Context mContext;
    View view;

    public CustomAdapter(Context context,ArrayList<ConfigurableProductDataModel> array_value_list) {

        this.array_value_list = array_value_list;
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.arr_list_item, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //Log.d("len_value",i+"");

        String stLabelValue = array_value_list.get(i).stAttId;

//        if(stLabelValue.equalsIgnoreCase("93"))
//            myViewHolder.tv_product_value.setBackgroundResource(R.color.colorYellow);
//        else if(stLabelValue.equalsIgnoreCase("141"))
//            myViewHolder.tv_product_value.setBackgroundResource(R.color.colorblue);
        myViewHolder.tv_product_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.d("label_value",array_value_list.get(i).stValueLabel);

//            if(stLabelValue.equalsIgnoreCase("93"))
                myViewHolder.tv_product_value.setBackgroundResource(R.color.colorYellow);
//            else if(stLabelValue.equalsIgnoreCase("141"))
//                    myViewHolder.tv_product_value.setBackgroundResource(R.color.colorblue);

            }
        });

        myViewHolder.tv_product_value.setText(array_value_list.get(i).stValueLabel);

    }

    @Override
    public int getItemCount() {
        return array_value_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_product_value;

        public MyViewHolder(View view) {
            super(view);

            tv_product_value = view.findViewById(R.id.tv_product_value);

        }
    }



}
