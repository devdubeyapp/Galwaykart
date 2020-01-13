package com.galwaykart.address_book;

import android.app.Activity;
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
 * Address Book adapter
 * Created by sumitsaini on 9/23/2017.
 */

public class AddressBookAdapter extends SimpleAdapter {

    ArrayList<HashMap<String,String>> itemList;
    final String TAG_region= "region";
    final String TAG_street = "street";
    final String TAG_company= "company";
    final String TAG_telephone= "telephone";
    final String TAG_postcode = "postcode";
    final String TAG_city = "city";
    final String TAG_firstname= "firstname";
    final String TAG_lastname = "lastname";
    final String TAG_customer_id = "customer_id";
    final String  TAG_edit = "Edit_Add";
    final String TAG_selected_address = "Address";
    int cur_position;
    SharedPreferences pref;
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
     *
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public AddressBookAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.itemList = (ArrayList<HashMap<String, String>>) data;
        this.ctx = context;

    }

    class Holder{

        TextView textStreet_name;
        TextView textTelephone_name;
        TextView textPincode_name;
        TextView textCity_name;
        TextView textCustomer_name;
        TextView textCompany_name;
        Button btEdit,btselect,bt_edit_add_icon;
       // Button btChangeAddress;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        cur_position= position;

        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_default_shipping_address_list, null);
            holder = new Holder();

            holder.textCity_name = (TextView) convertView.findViewById(R.id.textCity_name);
            holder.textStreet_name = (TextView) convertView.findViewById(R.id.textStreet_name);
            holder.textTelephone_name = (TextView) convertView.findViewById(R.id.textTelephone_name);
            holder.textPincode_name = (TextView) convertView.findViewById(R.id.textPincode_name);
            holder.textCustomer_name = (TextView) convertView.findViewById(R.id.textCustomer_name);
            holder.textCompany_name = (TextView) convertView.findViewById(R.id.textCompany_name);
            holder.btEdit = (Button)convertView.findViewById(R.id.btEdit);
            holder.btselect = (Button)convertView.findViewById(R.id.btselect);
            holder.bt_edit_add_icon = (Button)convertView.findViewById(R.id.bt_edit_add_icon);
           // holder.btChangeAddress=(Button)convertView.findViewById(R.id.btChangeAddress);
            convertView.setTag(holder);

        }

         company_name= itemList.get(position).get(TAG_company).toString();


        pref = CommonFun.getPreferences(ctx);
        String login_group_id=pref.getString("login_group_id","");

//        if(login_group_id.equals("4")) {
//
//            String st_edit_add = itemList.get(position).get(TAG_edit).toString();
//
//            if (st_edit_add.equalsIgnoreCase("true")) {
//
//                holder.bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pref = ctx.getSharedPreferences("glazekartapp", ctx.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("st_come_from_update","updateaddressCheckout");
//                        editor.commit();
//
//                        Intent intent = new Intent(ctx, AddNewAddress.class);
//                        ctx.startActivity(intent);
//
//                        CommonFun.finishscreen((Activity) ctx);
//
//                    }
//                });
//
//            } else {
//                holder.bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
////                        Intent intent = new Intent(ctx, UpdateAddressActivity.class);
////                        intent.putExtra("addressupdate", "checkout");
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        ctx.startActivity(intent);
////                        CommonFun.finishscreen((Activity) ctx);
//
//
//                        Intent intent = new Intent(ctx, AddNewAddress.class);
//                        intent.putExtra("st_come_from_update",itemList.get(position).get("st_come_from_update").toString());
//                        ctx.startActivity(intent);
//                        CommonFun.finishscreen((Activity) ctx);
//
//                    }
//                });
//            }
//        }
//        else
//        {

            holder.bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ctx,AddNewAddress.class);
                    intent.putExtra("add_new","no");
                    intent.putExtra("st_come_from_update",itemList.get(position).get("st_come_from_update").toString());
                    intent.putExtra("first_name",itemList.get(position).get(TAG_firstname).toString());
                    intent.putExtra("last_name",itemList.get(position).get(TAG_lastname).toString());
                    intent.putExtra("phone_no",itemList.get(position).get(TAG_telephone).toString());
                    intent.putExtra("zip",itemList.get(position).get(TAG_postcode).toString());
                    intent.putExtra("city",itemList.get(position).get(TAG_city).toString());
                    intent.putExtra("street_address",itemList.get(position).get(TAG_street).toString());
                    intent.putExtra("region",itemList.get(position).get(TAG_region).toString());
                    ctx.startActivity(intent);
                    CommonFun.finishscreen((Activity)ctx);

                }
            });

        //}

        if(company_name.equalsIgnoreCase("")) {
            holder.textCity_name.setText(itemList.get(position).get(TAG_city).toString());
            holder.textStreet_name.setText(itemList.get(position).get(TAG_street).toString());
            holder.textTelephone_name.setText(itemList.get(position).get(TAG_telephone).toString());
            holder.textPincode_name.setText(itemList.get(position).get(TAG_postcode).toString());
            holder.textCustomer_name.setText(itemList.get(position).get(TAG_firstname).toString());
            holder.textCompany_name.setVisibility(View.GONE);
        }
        else {


            holder.textCity_name.setText(itemList.get(position).get(TAG_city).toString());
            holder.textStreet_name.setText(itemList.get(position).get(TAG_street).toString());
            holder.textTelephone_name.setText(itemList.get(position).get(TAG_telephone).toString());
            holder.textPincode_name.setText(itemList.get(position).get(TAG_postcode).toString());
            holder.textCustomer_name.setText(itemList.get(position).get(TAG_firstname).toString());
//            holder.textCompany_name.setText(itemList.get(position).get(TAG_company).toString());
        }


        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ctx,AddNewAddress.class);
                intent.putExtra("st_come_from_update",itemList.get(position).get("st_come_from_update").toString());
                intent.putExtra("first_name",itemList.get(position).get(TAG_firstname).toString());
                intent.putExtra("last_name",itemList.get(position).get(TAG_lastname).toString());
                intent.putExtra("phone_no",itemList.get(position).get(TAG_telephone).toString());
                intent.putExtra("zip",itemList.get(position).get(TAG_postcode).toString());
                intent.putExtra("city",itemList.get(position).get(TAG_city).toString());
                intent.putExtra("street_address",itemList.get(position).get(TAG_street).toString());
                ctx.startActivity(intent);
                CommonFun.finishscreen((Activity)ctx);


            }
        });




        holder.btselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref;
                pref = CommonFun.getPreferences(ctx);

                String st_selected_address  = itemList.get(position).get(TAG_selected_address).toString();
                //Log.d("st_select",st_selected_address);
                SharedPreferences.Editor editor= pref.edit();
                editor.putString("st_selected_address",st_selected_address);
                editor.commit();

                Intent intent = new Intent(ctx,DeliveryTypeActivity.class);
                ctx.startActivity(intent);

                CommonFun.finishscreen((Activity)ctx);

            }
        });

            return convertView;


    }
}
