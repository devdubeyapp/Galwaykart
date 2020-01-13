package com.galwaykart.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.galwaykart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumitsaini on 9/27/2017.
 */

public class OrderDetailsAdapter extends SimpleAdapter {

    final String TAG_image= "image";
    final String TAG_product_id = "product_id";
    final String TAG_name= "name";
    final String TAG_total_qty_ordered= "qty_ordered";
    final String TAG_original_price = "price";
    final String TAG_created_at= "created_at";
    final String TAG_boolean="Return_check";
    final String TAG_edit_rtn = "Edit_Qty";

    Holder holder = null;
    Context ctx;

    ArrayList<HashMap<String, String>> arrayList;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public OrderDetailsAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.ctx = context;
        this.arrayList = (ArrayList<HashMap<String, String>>) data;

    }


    class Holder{

        ImageView img_view_ordered_product;
        TextView product_name_id;
        TextView total_product_amt;
        TextView order_date_time;
        TextView total_product_qty,return_qty_req;
        Button bt_edit_qty_icon;
        CheckBox check_for_return_req;


    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


//    @Override
//    public int getCount() {
//        return arrayList.size();
//    }

//    @Override
//    public Object getItem(int position) {
//        return arrayList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int cur_position= position;

        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_details_item, null);
            holder = new Holder();

            holder.img_view_ordered_product = (ImageView)convertView.findViewById(R.id.img_view_ordered_product);

            holder.product_name_id = (TextView) convertView.findViewById(R.id.product_name_id);
            holder.total_product_amt = (TextView) convertView.findViewById(R.id.total_product_amt);
            holder.order_date_time = (TextView) convertView.findViewById(R.id.order_date_time);
            holder.total_product_qty = (TextView) convertView.findViewById(R.id.total_product_qty);
            holder.check_for_return_req = (CheckBox)convertView.findViewById(R.id.check_for_return_req);
            holder.return_qty_req = (TextView)convertView.findViewById(R.id.return_qty_req);
            holder.bt_edit_qty_icon = (Button)convertView.findViewById(R.id.bt_edit_qty_icon);

            convertView.setTag(holder);

         }
        else {

            holder = (Holder) convertView.getTag();
        }


        holder.product_name_id.setText(arrayList.get(position).get(TAG_name).toString());
        holder.total_product_amt.setText(arrayList.get(position).get(TAG_original_price).toString());
        holder.order_date_time.setText(arrayList.get(position).get(TAG_created_at).toString());
        holder.total_product_qty.setText("Total Quantity:  " +arrayList.get(position).get(TAG_total_qty_ordered).toString());

        String st_boolean = arrayList.get(position).get(TAG_boolean).toString();
        String st_edit_rtn = arrayList.get(position).get(TAG_edit_rtn).toString();
        String st_rtn_qty = arrayList.get(position).get(TAG_edit_rtn).toString();


        ////////Log.d("st_boolean",st_boolean);


        if(st_boolean.equalsIgnoreCase("true")) {
            holder.check_for_return_req.setVisibility(View.VISIBLE);
        }
        else {
            holder.check_for_return_req.setVisibility(View.GONE);
        }

        if(st_edit_rtn.equalsIgnoreCase("true")) {
            holder.bt_edit_qty_icon.setVisibility(View.VISIBLE);
        }
        else {
            holder.bt_edit_qty_icon.setVisibility(View.GONE);
        }

        if(st_rtn_qty.equalsIgnoreCase("true")) {
            holder.return_qty_req.setVisibility(View.VISIBLE);
        }
        else {
            holder.return_qty_req.setVisibility(View.GONE);
        }

        holder.check_for_return_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Picasso.get()
                .load(arrayList.get(position).get(TAG_image).toString())
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)      // optional
                .resize(150, 150)
                //.rotate(90)                             // optional
                //.networkPolicy(NetworkPolicy.)
                .into(holder.img_view_ordered_product);




        return convertView;
    }
}
