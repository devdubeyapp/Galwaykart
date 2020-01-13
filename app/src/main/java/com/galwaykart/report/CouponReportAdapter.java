package com.galwaykart.report;

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


public class CouponReportAdapter extends RecyclerView.Adapter<CouponReportAdapter.ViewHolder> {

    private List<CouponReportModel> couponReportModels;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView sr_no_tv, coupon_type_tv,amount_tv, created_date_tv, expired_date_tv,coupon_code_tv,used_reference_tv;
        public LinearLayout main_row_lay;
        public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            sr_no_tv = (TextView) view.findViewById(R.id.sr_no_tv);
            coupon_type_tv = (TextView) view.findViewById(R.id.coupon_type_tv);
            amount_tv = (TextView) view.findViewById(R.id.amount_tv);
            created_date_tv = (TextView) view.findViewById(R.id.created_date_tv);
            expired_date_tv = (TextView) view.findViewById(R.id.expired_date_tv);
            coupon_code_tv = (TextView) view.findViewById(R.id.coupon_code_tv);
            used_reference_tv = (TextView) view.findViewById(R.id.used_reference_tv);
            main_row_lay = (LinearLayout) view.findViewById(R.id.main_row_lay);

        }
    }

    public CouponReportAdapter(Context context, List<CouponReportModel> couponReportModels) {
        this.context = context;
        this.couponReportModels = couponReportModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_coupon_report, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.sr_no_tv.setText("S.No."+couponReportModels.get(i).getSr_no());
        viewHolder.coupon_type_tv.setText(couponReportModels.get(i).getCoupon_type());
        viewHolder.amount_tv.setText("\u20B9 "+couponReportModels.get(i).getAmount());
        viewHolder.created_date_tv.setText(couponReportModels.get(i).getCreated_date());
        viewHolder.expired_date_tv.setText(couponReportModels.get(i).getExpired_date());
        viewHolder.coupon_code_tv.setText(couponReportModels.get(i).getCoupon_code());
        viewHolder.used_reference_tv.setText(couponReportModels.get(i).getUsed_reference());
    }

    @Override
    public int getItemCount() {
        return couponReportModels.size();
    }




}
