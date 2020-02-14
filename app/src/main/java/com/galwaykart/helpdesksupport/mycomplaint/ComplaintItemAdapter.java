package com.galwaykart.helpdesksupport.mycomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplaintItemAdapter extends RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>{

    private List<ComplModel> complModelList;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_product_name, tv_qty_request,tv_product_sku;
        public LinearLayout main_row_lay;
        public View layout;
        private ImageView imageView_product;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            tv_product_sku= view.findViewById(R.id.tv_product_sku);
            tv_product_name = view.findViewById(R.id.tv_product_name);
            tv_qty_request = view.findViewById(R.id.tv_qty_request);
          //  main_row_lay = (LinearLayout) view.findViewById(R.id.main_row_lay);
            imageView_product= view.findViewById(R.id.imageView_product);
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
        holder.tv_qty_request.setText("Qty : " + complModelList.get(position).getRequest_qty());
        holder.tv_product_sku.setText("SKU : " + complModelList.get(position).getItem_sku());

        if(!complModelList.get(position).getProduct_url().equals("")){

            Picasso.get()
                    .load(complModelList.get(position).getRequest_qty())
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    // .resize(200, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.imageView_product);

        }


    }

    @Override
    public int getItemCount() {
        return complModelList.size();
    }


}
