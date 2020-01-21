package com.galwaykart.helpdesksupport.mycomplaint;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;

import java.util.List;


public class ComplAdapter extends RecyclerView.Adapter<ComplAdapter.ViewHolder> {

    private List<ComplModel> complModels;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date_tv1, order_tv1,support_type_tv1, ticket_no_tv1, status_tv1, remark_tv1;
        public LinearLayout main_row_lay;
        public View layout;


        public ViewHolder(View view) {
            super(view);
            layout = view;
            date_tv1 = (TextView) view.findViewById(R.id.date_tv1);
            order_tv1 = (TextView) view.findViewById(R.id.order_tv1);
            support_type_tv1 = (TextView) view.findViewById(R.id.support_type_tv1);
            ticket_no_tv1 = (TextView) view.findViewById(R.id.ticket_no_tv1);
            status_tv1 = (TextView) view.findViewById(R.id.status_tv1);
            remark_tv1 = (TextView) view.findViewById(R.id.remark_tv1);
            main_row_lay = (LinearLayout) view.findViewById(R.id.main_row_lay);
        }
    }

    public ComplAdapter(Context context, List<ComplModel> complModels) {
        this.context = context;
        this.complModels = complModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.date_tv1.setText(complModels.get(position).getCreated_at());
        holder.order_tv1.setText(complModels.get(position).getOrder_id());
        holder.support_type_tv1.setText(complModels.get(position).getComplaint_type());
        holder.ticket_no_tv1.setText(complModels.get(position).getComplaint_id());
        holder.status_tv1.setText(complModels.get(position).getStatus_label());
        holder.remark_tv1.setText(Html.fromHtml(complModels.get(position).getDescription()));

        holder.main_row_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent complaint_detal_intent = new Intent(context, ComplaintDetailActivity.class);
                Log.d("complaint_id",complModels.get(position).getComplaint_id());
                complaint_detal_intent.putExtra("complaint_id",complModels.get(position).getComplaint_id());
                complaint_detal_intent.putExtra("complaint_type",complModels.get(position).getComplaint_type());
                complaint_detal_intent.putExtra("is_show",complModels.get(position).getIsShow()); //0 for none,1 for products
                complaint_detal_intent.putExtra( "remarks",complModels.get(position).getDescription());
                context.startActivity(complaint_detal_intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return complModels.size();
    }




}
