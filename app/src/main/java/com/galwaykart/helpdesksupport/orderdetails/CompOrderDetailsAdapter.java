package com.galwaykart.helpdesksupport.orderdetails;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompOrderDetailsAdapter extends RecyclerView.Adapter<CompOrderDetailsAdapter.ViewHolder> {

    private List<ComplaintOrderDetailModel> complaintOrderDetailModelList;
    private Context context;

    private int qty_ordered_int;
    private int j;

    private String str_qty_request="";

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CheckBox check_mark;
        private TextView tv_product_name, tv_qty_ordered, tv_qty_ordered1, total_product_amt ;
        private ImageView img_product;
        private Spinner sp_return_req;
        private LinearLayout main_row_lay;
        private View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            Snackbar snackbar;

            check_mark = (CheckBox) view.findViewById(R.id.check_mark);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            total_product_amt = (TextView) view.findViewById(R.id.total_product_amt);
            tv_qty_ordered = (TextView) view.findViewById(R.id.tv_qty_ordered);
            tv_qty_ordered1 = (TextView) view.findViewById(R.id.tv_qty_ordered1);
            sp_return_req = (Spinner) view.findViewById(R.id.sp_return_req);
            img_product = (ImageView) view.findViewById(R.id.img_product);
            main_row_lay = (LinearLayout) view.findViewById(R.id.main_row_lay);
        }
    }

    public CompOrderDetailsAdapter(Context context, List<ComplaintOrderDetailModel> complaintOrderDetailModelList) {
        this.context = context;
        this.complaintOrderDetailModelList = complaintOrderDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_row_complaint_orders, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       // holder.sp_return_req.setEnabled(false);

        ComplaintOrderDetailModel complaintModel=complaintOrderDetailModelList.get(position);

        holder.tv_product_name.setText(complaintOrderDetailModelList.get(position).getProduct_name());
        holder.tv_qty_ordered.setText("Total qty: " + complaintOrderDetailModelList.get(position).getQty_ordered());
        holder.tv_qty_ordered1.setText(complaintOrderDetailModelList.get(position).getQty_ordered());
        holder.total_product_amt.setText("Price: "+ complaintOrderDetailModelList.get(position).getProduct_price());

        holder.check_mark.setChecked(complaintModel.getCheck_for_return_req());
        holder.check_mark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(complaintModel.getCheck_for_return_req()==true)
                {
                    //holder.sp_return_req.setEnabled(true);
                    complaintModel.setCheck_for_return_req(false);
                }
                else
                {
                    //holder.sp_return_req.setEnabled(false);
                    complaintModel.setCheck_for_return_req(true);
                }


            }
        });

        holder.sp_return_req.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_qty_request = parent.getItemAtPosition(position).toString();
                complaintModel.setReturn_qty_req(str_qty_request);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(complaintOrderDetailModelList.get(position).getImage()!=null && !complaintOrderDetailModelList.get(position).getImage().equals("")) {
            Picasso.get()
                    .load(complaintOrderDetailModelList.get(position).getImage())
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(300, 300)
                   // .rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.img_product);
        }
        else
        {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.icons_users_gray)      // optional
                    // .resize(300, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.img_product);
        }







        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.layout_for_spinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.text1)).setText("Qty");
                    ((TextView) v.findViewById(R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qty_ordered_int = Integer.parseInt(complaintOrderDetailModelList.get(position).getQty_ordered());
        Log.e("qty_ordered_int",qty_ordered_int + "");

      for (j = 1; j <=qty_ordered_int; j++) {
            String qty= String.valueOf(j);
            adapter.add(qty);
        }

        adapter.add("Qty");
        holder.sp_return_req.setAdapter(adapter);
        holder.sp_return_req.setSelection(adapter.getCount());
    }



    @Override
    public int getItemCount() {
        return complaintOrderDetailModelList.size();

    }



}
