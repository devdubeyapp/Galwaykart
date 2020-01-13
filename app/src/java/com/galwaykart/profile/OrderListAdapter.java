package com.galwaykart.profile;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by itsoftware on 19/03/2018.
 */

public class OrderListAdapter extends SimpleAdapter {


    final String TAG_orderid= "orderid";
    final String TAG_status = "status";
    final String TAG_created_at= "created_at";
    final String TAG_total_qty_ordered= "total_qty_ordered";
    final String TAG_subtotal = "subtotal";
    String TAG_Order_date= "";
    String TAG_year = "";

    Holder holder = null;
    Context ctx;

    ArrayList<HashMap<String, String>> arrayList;

    public OrderListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.ctx = context;
        this.arrayList = (ArrayList<HashMap<String, String>>) data;
    }

    class Holder{

        ImageView img_view_ordered_product;
        TextView product_name_id;
        TextView total_product_amt;
        TextView order_date_time;
        TextView total_product_qty;


    }


    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
