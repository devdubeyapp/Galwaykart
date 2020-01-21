package com.galwaykart.helpdesksupport.mycomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;

import java.util.List;

public class ComplaintItemAdapter extends RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>{

    private List<ComplModel> complModelList;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_product_name, tv_qty_request;
        public LinearLayout main_row_lay;
        public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            tv_qty_request = (TextView) view.findViewById(R.id.tv_qty_request);
            main_row_lay = (LinearLayout) view.findViewById(R.id.main_row_lay);
        }
    }

    public ComplaintItemAdapter(Context context, List<ComplModel> complModelList) {
        this.context = context;
        this.complModelList = complModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_complaint_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_product_name.setText(complModelList.get(position).getProduct_name());
        holder.tv_qty_request.setText("Qty: " + complModelList.get(position).getRequest_qty());

    }

    @Override
    public int getItemCount() {
        return complModelList.size();
    }


}
