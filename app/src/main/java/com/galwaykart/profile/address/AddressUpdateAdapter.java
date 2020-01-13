package com.galwaykart.profile.address;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumitsaini on 10/27/2017.
 * Adapter to set all address details at list
 */

public class AddressUpdateAdapter extends SimpleAdapter {

    ArrayList<HashMap<String, String>> itemList;
    final String TAG_region = "region";
    final String TAG_street = "street";
    final String TAG_company = "company";
    final String TAG_telephone = "telephone";
    final String TAG_postcode = "postcode";
    final String TAG_city = "city";
    final String TAG_firstname = "firstname";
    final String TAG_lastname = "lastname";
    final String TAG_customer_name = "";

    final String TAG_customer_id = "customer_id";
    final String TAG_address_id = "id";
    int cur_position;
    String company_name = "";
    Holder holder = null;
    Context ctx;

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
    public AddressUpdateAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.itemList = (ArrayList<HashMap<String, String>>) data;
        this.ctx = context;

    }

    class Holder {

        TextView text_Street_name;
        TextView text_Telephone_name;
        TextView text_Pincode_name;
        TextView text_City_name;
        TextView text_Customer_name;
        TextView text_Company_name;
        Button bt_Edit_address, bt_add_new_address;

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
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        cur_position = position;

        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.update_address_list, null);
            holder = new Holder();

            holder.text_City_name = (TextView) convertView.findViewById(R.id.text_City_name);
            holder.text_Street_name = (TextView) convertView.findViewById(R.id.text_Street_name);
            holder.text_Telephone_name = (TextView) convertView.findViewById(R.id.text_Telephone_name);
            holder.text_Pincode_name = (TextView) convertView.findViewById(R.id.text_Pincode_name);
            holder.text_Customer_name = (TextView) convertView.findViewById(R.id.text_Customer_name);
            holder.text_Company_name = (TextView) convertView.findViewById(R.id.text_Company_name);
            holder.bt_Edit_address = (Button) convertView.findViewById(R.id.bt_Edit_address);


            convertView.setTag(holder);

        }

        company_name = itemList.get(position).get(TAG_company).toString();

        if (company_name.equalsIgnoreCase("")) {
            holder.text_City_name.setText(itemList.get(position).get(TAG_city).toString());
            holder.text_Street_name.setText(itemList.get(position).get(TAG_street).toString());
            holder.text_Telephone_name.setText(itemList.get(position).get(TAG_telephone).toString());
            holder.text_Pincode_name.setText(itemList.get(position).get(TAG_postcode).toString());
            holder.text_Customer_name.setText(itemList.get(position).get(TAG_customer_name).toString());
            holder.text_Company_name.setVisibility(View.GONE);
        } else {


            holder.text_City_name.setText(itemList.get(position).get(TAG_city).toString());
            holder.text_Street_name.setText(itemList.get(position).get(TAG_street).toString());
            holder.text_Telephone_name.setText("T: "+ itemList.get(position).get(TAG_telephone).toString());
            holder.text_Pincode_name.setText("Pin: " + itemList.get(position).get(TAG_postcode).toString());
            holder.text_Customer_name.setText(itemList.get(position).get(TAG_customer_name).toString());
            holder.text_Company_name.setText(itemList.get(position).get(TAG_company).toString());
        }


        holder.bt_Edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selected_address_id = itemList.get(position).get(TAG_address_id).toString();
                String selected_address_fName = itemList.get(position).get(TAG_firstname).toString();
                String selected_address_lName = itemList.get(position).get(TAG_lastname).toString();
                String selected_address_tel_no = itemList.get(position).get(TAG_telephone).toString();
                String selected_address_city = itemList.get(position).get(TAG_city).toString();
                String selected_address_street = itemList.get(position).get(TAG_street).toString();
                String selected_address_pin = itemList.get(position).get(TAG_postcode).toString();

                SharedPreferences pref;
                pref = CommonFun.getPreferences(ctx);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selected_address_id", selected_address_id);
                editor.putString("selected_address_fName", selected_address_fName);
                editor.putString("selected_address_lName", selected_address_lName);
                editor.putString("selected_address_tel_no", selected_address_tel_no);
                editor.putString("selected_address_city", selected_address_city);
                editor.putString("selected_address_street", selected_address_street);
                editor.putString("selected_address_pin", selected_address_pin);
                editor.commit();


                Intent intent = new Intent(ctx, EditAddress.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                ctx.startActivity(intent);


            }
        });


        return convertView;


    }
}