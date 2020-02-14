package com.galwaykart.address_book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Address Book adapter
 * Created by sumitsaini on 9/23/2017.
 */

public class CustomerAddressBookAdapter extends SimpleAdapter {

    ArrayList<HashMap<String,String>> itemList;
    final String TAG_region= "region";
    final String TAG_region_id= "region_id";
    final String TAG_region_code= "region_code";

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
    final String TAG_show_select="select";

    final String TAG_id= "id";
    int cur_position;
    String company_name = "";
    Holder holder = null;
    SharedPreferences pref;
    Context ctx;
    String return_data="";
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
    public CustomerAddressBookAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
        Button bt_delete_add_icon;
       // Button btChangeAddress;

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
    public View getView(final int position, View convertView, ViewGroup parent) {


        cur_position= position;

        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_default_shipping_address_list, null);
            holder = new Holder();

            holder.textCity_name = convertView.findViewById(R.id.textCity_name);
            holder.textStreet_name = convertView.findViewById(R.id.textStreet_name);
            holder.textTelephone_name = convertView.findViewById(R.id.textTelephone_name);
            holder.textPincode_name = convertView.findViewById(R.id.textPincode_name);
            holder.textCustomer_name = convertView.findViewById(R.id.textCustomer_name);
            holder.textCompany_name = convertView.findViewById(R.id.textCompany_name);
            holder.btEdit = convertView.findViewById(R.id.btEdit);
            holder.btselect = convertView.findViewById(R.id.btselect);
            holder.bt_edit_add_icon = convertView.findViewById(R.id.bt_edit_add_icon);
            holder.bt_delete_add_icon= convertView.findViewById(R.id.bt_delete_add_icon);
           // holder.btChangeAddress=(Button)convertView.findViewById(R.id.btChangeAddress);
            convertView.setTag(holder);

        }

         company_name= itemList.get(position).get(TAG_company);


        SharedPreferences pref = CommonFun.getPreferences(ctx);
        String login_group_id=pref.getString("login_group_id","");

      //  if(login_group_id.equals("4")) {

          //  String st_edit_add = itemList.get(position).get(TAG_edit).toString();

//            if (st_edit_add.equalsIgnoreCase("true")) {
//
//                holder.bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent(ctx, AddNewAddress.class);
//                        ctx.startActivity(intent);
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
//                        Intent intent = new Intent(ctx, UpdateAddressActivity.class);
//                        intent.putExtra("addressupdate", "checkout");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        ctx.startActivity(intent);
//                        CommonFun.finishscreen((Activity) ctx);
//
//                    }
//                });


        holder.bt_delete_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String errmsg = "Are you sure want to delete";
                final AlertDialog.Builder b;
                try
                {
                    b = new AlertDialog.Builder(ctx);
                    b.setTitle("Alert");
                    b.setCancelable(false);
                    b.setMessage(errmsg);
                    b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            b.create().dismiss();



                            SharedPreferences pref = CommonFun.getPreferences(ctx);
                            String email = pref.getString("user_email", "");
                            String tokenData = pref.getString("tokenData", "");


                            String  st_attribute_value_mob =  itemList.get(position).get(TAG_telephone).trim();

                            Set<String> all_address_id;
                            all_address_id=new HashSet<String>();
                            all_address_id=pref.getStringSet("all_address_id",null);

                            ArrayList<String> address_list = new ArrayList<String>(all_address_id);

                            String st_add_id="";
                            String address_id= itemList.get(position).get(TAG_id);
                            //Log.d("return_data",address_id);

                            if(address_list.size()>0){

                                for(int i=0;i<address_list.size();i++) {
                                    if (st_add_id.equals("")) {
                                        if(! address_list.get(i).equals(address_id))
                                            st_add_id = "{\"id\":" + address_list.get(i) + "}";
                                    }
                                    else {
                                        if(! address_list.get(i).equals(address_id))
                                            st_add_id = st_add_id + "," + "{\"id\":" + address_list.get(i) + "}";
                                    }
                                }
                            }


                            return_data = "{\"customer\":" +
                                    "{\"email\":\"" + email + "\"," +
                                    "\"lastname\":\"" + itemList.get(position).get(TAG_lastname) + "\",\"group_id\":"+login_group_id+","+
                                    "\"custom_attributes\":[{\"value\":\""+st_attribute_value_mob+"\",\"attribute_code\":\"mobile_number\"}]," +
                                    "\"addresses\":" +
                                    "[" +
                                    st_add_id +"]," +
                                    "\"website_id\":\"1\"," +
                                    "\"store_id\":\"1\"," +
                                    "\"firstname\":\"" + itemList.get(position).get(TAG_firstname) + "\"}}";

                            callDeleteAPI(return_data,tokenData,ctx,position);

                        }
                    });
                    b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            b.create().dismiss();

                        }
                    });
                    b.create().show();
                }
                catch(Exception ex)
                {
                }




                }
            });


            holder.bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences pref = CommonFun.getPreferences(ctx);
                    //SharedPreferences.Editor editor = pref.edit();
                    //editor.putStringSet("st_come_from_update","updateaddressCheckout");
                   // editor.commit();

                    Intent intent = new Intent(ctx, AddNewAddress.class);
                    intent.putExtra("add_new","no");
                    intent.putExtra("selecteddata","done");
                    intent.putExtra("region", itemList.get(position).get(TAG_region));
                    intent.putExtra("address_id", itemList.get(position).get(TAG_id));
                    intent.putExtra("first_name", itemList.get(position).get(TAG_firstname));
                    intent.putExtra("last_name", itemList.get(position).get(TAG_lastname));
                    intent.putExtra("phone_no", itemList.get(position).get(TAG_telephone));
                    intent.putExtra("zip", itemList.get(position).get(TAG_postcode));
                   // intent.putExtra("state_",itemList.get(position).get(TAG_region).toString());
                    intent.putExtra("city", itemList.get(position).get(TAG_city));
                    intent.putExtra("street_address", itemList.get(position).get(TAG_street));
                    intent.putExtra("st_come_from_update", itemList.get(position).get(TAG_show_select).equalsIgnoreCase("false")?"updateaddress":"");
                    intent.putExtra("total_address_data", itemList.get(position).get("total_data"));
                    intent.putExtra("default_ship", itemList.get(position).get("default_ship"));
                    intent.putExtra("default_bill", itemList.get(position).get("default_bill"));


                    intent.putExtra("","");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                    CommonFun.finishscreen((Activity) ctx);


                }
            });


        if(company_name.equalsIgnoreCase("")) {
            holder.textCity_name.setText(itemList.get(position).get(TAG_city));
            holder.textStreet_name.setText(itemList.get(position).get(TAG_street));
            holder.textTelephone_name.setText("T: " + itemList.get(position).get(TAG_telephone));
            holder.textPincode_name.setText("Pin :"+ itemList.get(position).get(TAG_postcode));
            holder.textCustomer_name.setText(itemList.get(position).get(TAG_firstname) +" "+ itemList.get(position).get(TAG_lastname));
            holder.textCompany_name.setVisibility(View.GONE);
        }
        else {


            holder.textCity_name.setText(itemList.get(position).get(TAG_city));
            holder.textStreet_name.setText(itemList.get(position).get(TAG_street));
            holder.textTelephone_name.setText("T: " + itemList.get(position).get(TAG_telephone));
            holder.textPincode_name.setText("Pin :"+ itemList.get(position).get(TAG_postcode));
            holder.textCustomer_name.setText(itemList.get(position).get(TAG_firstname) +" "+ itemList.get(position).get(TAG_lastname));
//            holder.textCompany_name.setText(itemList.get(position).get(TAG_company).toString());
        }


        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String st_coming_from = "Adapter";
                SharedPreferences pref;
                pref= CommonFun.getPreferences(ctx);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("st_coming_from",st_coming_from);
                editor.commit();


                CommonFun.OpenNewAddress(ctx,"",1);


            }
        });


        String is_show_select_button=itemList.get(position).get(TAG_show_select);
        if(is_show_select_button.equalsIgnoreCase("false")) {
            holder.btselect.setVisibility(View.GONE);
        }

        //Log.d("TAG_show_select",is_show_select_button);
        holder.btselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref;
                pref = CommonFun.getPreferences(ctx);

                //String st_selected_address  = itemList.get(position).get(TAG_selected_address).toString();




                SharedPreferences.Editor editor= pref.edit();
                editor.putString("st_selected_address","Franchisee");
                editor.putString("telephone", itemList.get(position).get(TAG_telephone));
                editor.putString("postcode", itemList.get(position).get(TAG_postcode));
                editor.putString("city", itemList.get(position).get(TAG_city));

                editor.putString("region_code", itemList.get(position).get(TAG_region_code));
                editor.putString("region", itemList.get(position).get(TAG_region));
                editor.putString("region_id", itemList.get(position).get(TAG_region_id));

                editor.putString("add_line1", itemList.get(position).get(TAG_street));
                editor.putString("country_id","IN");
                editor.putString("default_billing","true");
                editor.putString("firstname", itemList.get(position).get(TAG_firstname));
                editor.putString("lastname", itemList.get(position).get(TAG_lastname));
                editor.commit();

                Intent intent = new Intent(ctx, DeliveryTypeActivity.class);
                ctx.startActivity(intent);
                CommonFun.finishscreen((Activity)ctx);


            }
        });

            return convertView;


    }


    private void callDeleteAPI(String return_data,String tokenData,Context context, int cur_position){

        //Log.d("return_data",return_data);

        TransparentProgressDialog pDialog;
        pDialog = new TransparentProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            String save_address_url = Global_Settings.api_url + "rest/V1/customers/me";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, save_address_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            //Log.d("responsePut", response);

                                Intent intent = new Intent(context, CustomerAddressBook.class);
                                 intent.putExtra("st_come_from_update", itemList.get(cur_position).get(TAG_show_select).equalsIgnoreCase("false")?"updateaddress":"");
                                context.startActivity(intent);
                                CommonFun.finishscreen((Activity) context);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error, context);
                }
            }) {


                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return return_data == null ? null : return_data.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();


                    ////Log.d("delievery data in",delivery_data_in.toString());
                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }
    }

}
