package com.galwaykart.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.helpdesksupport.CMSMainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<OrderListModel> orderListModels;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView order_id, text_view_date,total_qty, total_amt, tv_year, tv_create_complaint, tv_view_order_details;
        public RelativeLayout rel_lay1;
        public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            order_id = view.findViewById(R.id.order_id);
            total_qty = view.findViewById(R.id.total_qty);
            total_amt = view.findViewById(R.id.total_amt);
            text_view_date = view.findViewById(R.id.text_view_date);
            tv_year = view.findViewById(R.id.tv_year);
            tv_create_complaint = view.findViewById(R.id.tv_create_complaint);
            tv_view_order_details = view.findViewById(R.id.tv_view_order_details);

            rel_lay1 = view.findViewById(R.id.rel_lay1);
        }
    }

    public OrderListAdapter(Context context, List<OrderListModel> orderListModels) {
        this.context = context;
        this.orderListModels = orderListModels;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_order_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt= null;
        try {
            dt = format1.parse(orderListModels.get(position).getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String st_monthNumber  = (String) DateFormat.format("MMM",   dt); // month
        String st_year         = (String) DateFormat.format("yyyy", dt); // year
        String st_sday          = (String) DateFormat.format("dd",   dt); // date


        holder.order_id.setText("Order ID: "+ orderListModels.get(position).getIncrement_id() + "("+orderListModels.get(position).getStatus()+")");
        holder.total_qty.setText("Total Quantity: "+orderListModels.get(position).getTotal_qty_ordered());
        holder.total_amt.setText("Amount: ₹ "+ orderListModels.get(position).getGrand_total());
        holder.text_view_date.setText(st_sday);
        holder.tv_year.setText(st_monthNumber +"-"+ st_year);

        Log.e("st_sday",st_sday);
        Log.e("st_year", st_year);
        String is_need_help=orderListModels.get(position).getStatus_label().toLowerCase();
        if(is_need_help.contains("delivered") || is_need_help.contains("shipped"))
            holder.tv_create_complaint.setVisibility(View.VISIBLE);
        else
            holder.tv_create_complaint.setVisibility(View.GONE);

        holder.tv_create_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cms_detal_intent = new Intent(context, CMSMainActivity.class);
                cms_detal_intent.putExtra("entity_id",orderListModels.get(position).getEntity_id());
                cms_detal_intent.putExtra("order_id",orderListModels.get(position).getOrderid());
                cms_detal_intent.putExtra("order_date",orderListModels.get(position).getUpdated_at());
                Log.e("entity_id_or_list_adp", orderListModels.get(position).getEntity_id());
                Log.e("order_id_or_list_adp", orderListModels.get(position).getOrderid());
                context.startActivity(cms_detal_intent);

            }
        });


        holder.tv_view_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selected_order_id = orderListModels.get(position).getOrderid();
                String selected_order_status = orderListModels.get(position).getStatus();
                String selected_order_total =orderListModels.get(position).getSubtotal();


                SharedPreferences pref=CommonFun.getPreferences(context);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("st_status",selected_order_status);
                editor.putString("Order_ID",selected_order_id);
                // editor.putString("selected_order_rtn",selected_order_rtn);
                editor.putString("entity_id",orderListModels.get(position).getEntity_id());
                editor.putString("selected_order_total",selected_order_total);
                editor.putString("st_increment_id",orderListModels.get(position).getIncrement_id());
                editor.putString("order_grand_total",orderListModels.get(position).getGrand_total());
                editor.commit();


                Intent other_detal_intent = new Intent(context, OrderDetails.class);
                other_detal_intent.putExtra("entity_id",orderListModels.get(position).getEntity_id());
                other_detal_intent.putExtra("order_id",orderListModels.get(position).getOrderid());
                other_detal_intent.putExtra("increment_id",orderListModels.get(position).getIncrement_id());
                other_detal_intent.putExtra("order_grand_total",orderListModels.get(position).getGrand_total());

                Log.e("entity_id_or_list_ap", orderListModels.get(position).getEntity_id());
                Log.e("order_id_or_list_ap", orderListModels.get(position).getOrderid());


                context.startActivity(other_detal_intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return orderListModels.size();
    }




}
