package com.galwaykart.dbfiles.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.galwaykart.Guest.GuestMainActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.databinding.MRecyclerViewItemHometopBinding;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.dbfiles.handler.ProductCardClickListener;
import com.galwaykart.essentialClass.CommonFun;

import java.util.List;

public class MvvmRecyclerViewProductAdapter
        extends RecyclerView.Adapter<MvvmRecyclerViewProductAdapter.ViewHolder>
        implements ProductCardClickListener

{

    private List<ProductDataModel> dataModelList;
    private Context mContext;

    public MvvmRecyclerViewProductAdapter(Context context,List<ProductDataModel> dataModelList) {
        this.dataModelList = dataModelList;
        mContext=context;
    }

    @Override
    public MvvmRecyclerViewProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        MRecyclerViewItemHometopBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.m_recycler_view_item_hometop, parent, false);

        ViewHolder viewHolder=new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductDataModel dataModel = dataModelList.get(position);
        holder.itemRowBinding.setProductDetails(dataModel);
        holder.itemRowBinding.setClickHandler(this);

        //holder.itemRowBinding


    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }


    public void clickListener(ProductDataModel productDataModel) {

        //Log.d("sku",productDataModel.getSku());
        SharedPreferences pref;
        pref= CommonFun.getPreferences(mContext);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("showitemsku",productDataModel.getSku());
        editor.commit();

        Boolean is_logged_in_user=false;
        String value_email = pref.getString("login_email", "");
        if(value_email!=null && !value_email.equals(""))
            is_logged_in_user=true;



        if(is_logged_in_user==true) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public MRecyclerViewItemHometopBinding itemRowBinding;

        public ViewHolder(MRecyclerViewItemHometopBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

    }
//
//    public void btnClick(ProductDataModel productDataModel){
//        Toast.makeText(context, user.empId, Toast.LENGTH_LONG).show();
//
//    }

}

