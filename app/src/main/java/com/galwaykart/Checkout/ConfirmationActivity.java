package com.galwaykart.Checkout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.DeliveryTypeActivity;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartItem;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Show all cart item with price and address
 * for confirmation of the user
 * Created by ankesh on 10/3/2017.
 */

public class ConfirmationActivity extends BaseActivityWithoutCart {

    SharedPreferences pref;
    TransparentProgressDialog pDialog;
    String tokenData="";
    TextView tv_shipping_address;


    String url_cart_item_list = "";
    ListAdapter lstadapter;
    int total_cart_qty;
    int total_cart_count;
    boolean isFirstTime = false;

    final String TAG_total_item_count = "items_count";
    final String TAG_total_items_qty = "items_qty";
    final String TAG_item_id = "item_id";
    final String TAG_sku = "sku";
    final String TAG_qty = "qty";
    final String TAG_name = "name";
    final String TAG_price = "price";
    final String TAG_quote_id = "quote_id";
    String cart_imagepath="";


    String[] arr_sku, arr_qty,arr_quote_id,arr_name,arr_price,arr_initial_qty,arr_final_qty,arr_boolean,arr_qty_api,arr_item_id,
            arr_updated_cart_qty;

    ListView list_order_list;
    Button btConfirmOrder;

    TextView tv_txt_view;
    String cart_amount="";
    String inc_tax="";
    String offer_dis="";
    String amt_with_tax="";
    Boolean delete_successfully=false;
    String stNewDonationAmt="";
    int gawalaDonationAmt;
    String subtotal_inc_tax="";
    String disc_amount="",stDonationTitle="";
    int stDonationValue=0;
    String base_grand_total="",st_total_cart_ip="";
    Dialog dialog;
    EditText ed_edit_donation;


    //TextView tv_grand_total;


    ArrayList<HashMap<String, String>> itemList;
    String payment_method_url = "";
    String[] arr_pay_code;
    String TAG_pay_code = "code";
    String TAG_pay_title = "title";
    String jsonObject_grand_total = "";
    String jsonObject_total_discount="";

    /*
    *  variables for Offer
     */

    int arr_catalog_length;
    String [] arr_non_offer_sku,arr_offered_sku,arr_sku_off,arr_qty_off,
            arr_thrshold_amt,arr_max_price,
            arr_offer_cart_item,
            arr_offer_item_qty,
            arr_offer_item_price,
            arr_boolean_edit;

    String st_sku_off1="",
            st_thresshold_price_off1="",
            st_qty_off1="",
            st_thresshold_price_off2="",
            st_max_price="",
            add_to_cart_URL="",
            item_quote_id="",
            ship_region="",
            ship_region_id="",
            ship_region_code="",
            ship_country_id="",
            ship_street="",
            ship_postcode="",
            ship_city="",
            ship_fname="",
            ship_lname="",
            ship_email="",
            ship_telephone="";

    Double total_offer_amount=0.0;
    Double total_amt=0.0;
    DatabaseHandler dbh = new DatabaseHandler(ConfirmationActivity.this);

    Boolean is_data_load=false;
    CheckBox cb_donation_msg;
    TextView tv_donation_text;



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(ConfirmationActivity.this, DeliveryTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.galwaykart.R.layout.activity_confirmation_order);

        initNavigationDrawer();


        //tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);

        pref = CommonFun.getPreferences(getApplicationContext());
       // pref = CommonFun.getPreferences(getApplicationContext());
        tokenData=pref.getString("tokenData","");
        cb_donation_msg = findViewById(com.galwaykart.R.id.cb_donation_msg);
        //st_total_cart_ip = pref.getString("cart_ip","");

        ////Log.d("st_total_cart_ip",st_total_cart_ip);

        String st_selected_address=pref.getString("st_selected_address","");
        tv_donation_text=findViewById(R.id.tv_donation_text);

        /**
         * Fetch all address details from the
         * temp storage
         */

        if(st_selected_address.equalsIgnoreCase("Franchisee")) {
             ship_region = pref.getString("region", "");
             ship_region_id = pref.getString("region_id", "");
             ship_region_code = pref.getString("region_code", "");
             ship_country_id = pref.getString("country_id", "");
             ship_street = pref.getString("add_line1", "");
             ship_postcode = pref.getString("postcode", "");
             ship_city = pref.getString("city", "");
             ship_fname = pref.getString("firstname", "");
             ship_lname = pref.getString("lastname", "");
             ship_email = pref.getString("login_email", "");
             ship_telephone = pref.getString("telephone", "");
        }
        else if(st_selected_address.equalsIgnoreCase("Home")) {
//            ship_region = pref.getString("new_region", "");
//            ship_region_id = pref.getString("new_region_id", "");
//            ship_region_code = pref.getString("new_region_code", "");
//            ship_country_id = pref.getString("new_country_id", "");
//            ship_street = pref.getString("new_add_line1", "");
//            ship_postcode = pref.getString("new_postcode", "");
//            ship_city = pref.getString("new_city", "");
//            ship_fname = pref.getString("new_firstname", "");
//            ship_lname = pref.getString("new_lastname", "");
//            ship_email = pref.getString("login_email", "");
//            ship_telephone = pref.getString("new_telephone", "");

            ship_region = pref.getString("region", "");
            ship_region_id = pref.getString("region_id", "");
            ship_region_code = pref.getString("region_code", "");
            ship_country_id = pref.getString("country_id", "");
            ship_street = pref.getString("add_line1", "");
            ship_postcode = pref.getString("postcode", "");
            ship_city = pref.getString("city", "");
            ship_fname = pref.getString("firstname", "");
            ship_lname = pref.getString("lastname", "");
            ship_email = pref.getString("login_email", "");
            ship_telephone = pref.getString("telephone", "");
        }

            String shipment_address = ship_fname + " " + ship_lname + "\n" +
                    ship_street + "\n" +
                    ship_region + " ," +
                    ship_city + ", \n" +
                    "PostCode: " + ship_postcode + "\n" +
                    ship_email + " ," +
                    "Tel: " + ship_telephone;




        tv_shipping_address=(TextView)findViewById(com.galwaykart.R.id.tv_shipping_address);
        tv_shipping_address.setText(shipment_address);

        list_order_list=(ListView)findViewById(com.galwaykart.R.id.list_order_list);

        btConfirmOrder=(Button)findViewById(com.galwaykart.R.id.btConfirmOrder);
        btConfirmOrder.setVisibility(View.GONE);
        btConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPayment();
            }
        });

        String cus_id=pref.getString("login_customer_id","");
      //  url_cart_item_list = Global_Settings.api_url+"rest/V1/carts/mine/?customerId="+cus_id;
        url_cart_item_list = Global_Settings.api_url
                + "rest/V1/m-carts/mine";

    }

    /**
     * redirect payment gateway
     */
    private void gotoPayment(){

        Intent intent=new Intent(ConfirmationActivity.this, Payment_Method_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(ConfirmationActivity.this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        dialog = new Dialog(ConfirmationActivity.this);

        if(is_data_load==false) {

            tv_txt_view = (TextView) findViewById(com.galwaykart.R.id.tv_txt_view);
            pref = CommonFun.getPreferences(getApplicationContext());
            cart_amount = pref.getString("cartamount", "");
            inc_tax = pref.getString("arr_amount", "");
            offer_dis = pref.getString("offer_disc", "");
            ////Log.d("tax",inc_tax);
            ////Log.d("offer_dis",offer_dis);

            //String.valueOf(Double.parseDouble(total_grand_amount)+ Math.round(total_amt*100.0)/100.0);

            amt_with_tax = String.valueOf(Double.parseDouble(cart_amount) + Double.parseDouble(inc_tax));

//        tv_txt_view.setText("Cart Subtotal ("+total_cart_qty+" item): Rs "+cart_amount+
//                            "\nInc Tax: "+inc_tax+"\nTotal Amount:"+amt_with_tax);

/**
 * Get all cart item list
 */
            btConfirmOrder.setVisibility(View.GONE);

            cb_donation_msg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("isChecked", String.valueOf(isChecked));

                    if(isFirstTime == true) {
                         openDonationBox();

                    }

                }
            });

            callConfirmationActivity();
        }

    }

    private void openDonationBox() {

        dialog.setContentView(com.galwaykart.R.layout.dialog_donation);
        dialog.setCancelable(false);
        TextView tv_alert_title=dialog.findViewById(R.id.tv_alert_title);
        tv_alert_title.setText(stDonationTitle);
        ed_edit_donation = dialog.findViewById(com.galwaykart.R.id.ed_edit_donation);
        ed_edit_donation.setText(String.valueOf(stDonationValue));
        Button btn_add_donation = dialog.findViewById(com.galwaykart.R.id.btn_add_donation);
        Button btn_remove_donation = dialog.findViewById(com.galwaykart.R.id.btn_remove_donation);

        ImageView btn_close= dialog.findViewById(com.galwaykart.R.id.btn_close);
        btn_close.setVisibility(View.GONE);
        btn_add_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stNewDonationAmt = ed_edit_donation.getText().toString().trim();
                if (!stNewDonationAmt.equalsIgnoreCase("")) {
                    gawalaDonationAmt = Integer.parseInt(stNewDonationAmt);
                    Log.d("stNewDonationAmt", stNewDonationAmt);
                    if (gawalaDonationAmt >= 0) {
                        callDonationApi(stNewDonationAmt);
                    } else {
                        CommonFun.alertError(ConfirmationActivity.this, "Please enter greater than 0 amount");
                    }
                }
            }
        });

        btn_remove_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stNewDonationAmt = "0";
                callDonationApi(stNewDonationAmt);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void callDonationApi(String gawalaDonationAmt) {

        String api_url = Global_Settings.api_url+"rest/V1/donation/update/"+gawalaDonationAmt;

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                api_url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    Log.d("responseDonation", response);

                    Intent intent = new Intent(ConfirmationActivity.this, ConfirmationActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(ConfirmationActivity.this);

                    dialog.dismiss();
                }catch (Exception e){
                    CommonFun.alertError(ConfirmationActivity.this,"Donation amount has not added.Please try again!");
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
//                            Log.d("errorDonation",error.toString());
                        CommonFun.alertError(ConfirmationActivity.this,"Donation amount has not added.Please try again!");
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                //   params.put("Content-Type","application/json");

                return params;
            }
        };
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);



    }

    private void callConfirmationActivity() {

        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(ConfirmationActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        //Log.d("call_url",url_cart_item_list);
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    Log.d("call_resConfirmation", response.toString());
//                        CommonFun.alertError(ConfirmationActivity.this, response.toString());

                        lstadapter=null;
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));
                            total_cart_qty = Integer.parseInt(jsonObj.getString(TAG_total_items_qty));


                            JSONArray custom_data = jsonObj.getJSONArray("cart_items");

                            arr_sku = new String[custom_data.length()];
                            arr_qty = new String[custom_data.length()];
                            arr_quote_id= new String[custom_data.length()];
                            arr_name = new String[custom_data.length()];
                            arr_price = new String[custom_data.length()];
                            arr_initial_qty = new String[custom_data.length()];
                            arr_final_qty = new String[custom_data.length()];
                            arr_boolean = new String[custom_data.length()];
                            arr_qty_api= new String [custom_data.length()];
                            arr_item_id= new String[custom_data.length()];
                            arr_updated_cart_qty = new String[custom_data.length()];
                            arr_offer_cart_item = new String[custom_data.length()];
                            arr_offer_item_qty= new String[custom_data.length()];
                            arr_offer_item_price = new String[custom_data.length()];


                            for (int i = 0; i < custom_data.length(); i++) {
                                JSONObject c = custom_data.getJSONObject(i);

                                String item_id = c.getString(TAG_item_id);
                                String item_sku = c.getString(TAG_sku);
                                String item_qty = c.getString(TAG_qty);
                                String item_name = c.getString(TAG_name);
                                String item_price = c.getString(TAG_price);
                                item_quote_id= c.getString(TAG_quote_id);
                                String added_string="";

                                //if(c.has("product_type")) {

                                //    String p_size="";
                                 //   String p_color="";
                                    //String product_type = c.getString("product_type");
//                                    if(product_type.equalsIgnoreCase("configurable")){
//
//
//
//                                        JSONArray jsonArray_options=c.getJSONArray("options");
//                                        for(int k=0;k<jsonArray_options.length();k++){
//                                            JSONObject jsonObject=jsonArray_options.getJSONObject(k);
//                                            if(jsonObject.has("Size"))
//                                                p_size = jsonObject.getString("Size");
//
//                                            else if(jsonObject.has("Color"))
//                                                p_color=jsonObject.getString("Color");
//
//                                        }
//
//                                        added_string="("+p_size+" -"+p_color+")";
//
//                                    }
                              //  }

                                //item_name=item_name+ " "+added_string;

                                arr_sku[i] = item_sku;
                                arr_qty[i] = item_qty;
                                arr_quote_id[i] = item_quote_id;
                                arr_name[i] = item_name;
                                arr_price[i] = item_price;
                                arr_initial_qty[i] = item_qty;
                                arr_boolean[i]="false";
                                arr_item_id [i] = item_id;


                                DatabaseHandler dbh=new DatabaseHandler(ConfirmationActivity.this);
                                if(dbh.getCartProductImageSKuCount()>0) {
                                    List<CartProductImage> contacts = dbh.getCartProductImage(arr_sku[i]);

                                    for (CartProductImage cn : contacts) {
                                        cart_imagepath = cn.get_image().toString();
                                    }

                                }
                            }

                            JSONArray jsonArray_Subtotal = jsonObj.getJSONArray("total_segments");
                            for(int k=0;k<jsonArray_Subtotal.length();k++){
                                JSONObject jsonObject=jsonArray_Subtotal.getJSONObject(k);

                                if(jsonObject.getString("code").equalsIgnoreCase("donation"))
                                {

                                    int value =  jsonObject.getInt("value");
                                    stDonationTitle = jsonObject.getString("title");
                                    stDonationValue = value;
                                    Log.e("base_totalDonation", String.valueOf(stDonationValue));

                                }

                            }


                            if(!stDonationTitle.equals(""))
                            {
                                cb_donation_msg.setVisibility(View.VISIBLE);
                                tv_donation_text.setText("* " + stDonationTitle + " will not be refundable");
                            }
                            else
                            {
                                cb_donation_msg.setVisibility(View.GONE);
                                tv_donation_text.setText("");
                            }

                            showCartItem();
                        } catch (JSONException e) {

                            e.printStackTrace();
                            //Log.d("expi",e.toString());
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                            startActivity(intent);

                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if (error instanceof ParseError || error instanceof ServerError) {
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count =0;
                            //initNavigationDrawer();
                            list_order_list.setVisibility(View.GONE);

                        }
                        else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error, ConfirmationActivity.this);
                        }


//                        Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                        startActivity(intent);


                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }


    /**
     * Show cart item in the listview
     */

    private void showCartItem() {
        DatabaseHandler dbh=new DatabaseHandler(ConfirmationActivity.this);
        lstadapter = new CartItemShowAdapter(ConfirmationActivity.this,arr_sku,arr_qty,arr_name,arr_price,arr_boolean);

        if (lstadapter.getCount() > 0) {


            list_order_list.setAdapter(lstadapter);
            list_order_list.setVisibility(View.VISIBLE);

            //tv_txt_view.setText("Cart Subtotal ("+total_cart_qty+" item):Rs "+cart_amount);
            //tv_txt_view.setText("Cart Subtotal ("+total_cart_qty+" item): Rs "+cart_amount+"\nInc Tax: Rs "+inc_tax+"\nTotal Amount: Rs "+amt_with_tax);


//                                for(int i=0;i<total_cart_count;i++){
//
//
//
//
//                                }


            for(int i=0;i<total_cart_count;i++){

                if (Integer.parseInt(arr_qty[i])>0)
                    dbh.addCartProduct(new CartItem(arr_sku[i], arr_name[i], "", arr_qty[i], arr_price[i]));

            }

            /**
             * Fetch cart subtotal amount from the api
             */
            callCartItemAmount();
        }
        else
        {
           list_order_list.setVisibility(View.GONE);
        }
    }


    /**
     * Check stock availability of the product in the given zone
     */

    private void checkStockAvailability(){

        String product_code_xml="";
        for(int i=0;i<total_cart_count;i++){

            if(product_code_xml.equals("")){

                product_code_xml=
                        "\"product_code\": \""+arr_sku[i]+"\", " +
                        "\"qty\": \""+arr_qty[i]+"\" }" ;
            }
            else
            {
                product_code_xml=product_code_xml+",{" +
                        "\"product_code\": \""+arr_sku[i]+"\", " +
                        "\"qty\": \""+arr_qty[i]+"\" }" ;
            }

        }
        String user_zone= pref.getString("user_zone","");

        final String check_stock_xml="{\"franchisee_code\":\""+user_zone+"\"," +
                "\"itype\":\"247\"," +
                "\"stocktype\":\"-1\"," +
                "\"spmode\":\"0\", " +
                "\"xml_list\":[{ " +

                product_code_xml+

                "]}";

        //{"franchisee_code":"BST","itype":"247","stocktype":"-1","spmode":"0", "xml_list":[{ "product_code": "GNF04100", "qty": "5" },{ "product_code": "GRP09200", "qty": "5" }]}
        final String check_stock_url= Global_Settings.st_sales_api+"LoadProductSearchlistbyfranchiseecodeandproductlistApi";


        ////Log.d("check_stock_xml",check_stock_xml);


//        pDialog = new TransparentProgressDialog(ConfirmationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, check_stock_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if(pDialog.isShowing())
//                                pDialog.dismiss();
                            //////Log.d("VOLLEY", response);

                            //CommonFun.alertError(ConfirmationActivity.this,response);

                            try {

                            //    response=response.replaceAll("\\[","");
                            //    response=response.replaceAll("]","");

                                JSONArray jsonArray=new JSONArray(response);

                                for(int k=0;k<jsonArray.length();k++) {

                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String current_product_sku = jsonObject.getString("product_code");
                                    String current_product_qty= jsonObject.getString("Stock");

                                    for(int j=0;j<jsonArray.length();j++){

                                        if(arr_sku[j].equalsIgnoreCase(current_product_sku)){
                                            if(Integer.parseInt(arr_qty[j])>Integer.parseInt(current_product_qty)){

                                               deleteCartItem(arr_item_id[j].toString());


                                            }


                                        }


                                    }


                                }

                                showCartItem();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                                startActivity(intent);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error, ConfirmationActivity.this);
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
                    try {
                        return check_stock_xml == null ? null : check_stock_xml.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", check_stock_url, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }

    }


    StringRequest stringRequest;
    String delete_to_cart_URL;

    /**
     * Delete item from cart
     */
    private void deleteCartItem(String cart_item_id) {

        delete_to_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine/items/"+cart_item_id;

//        pDialog = new TransparentProgressDialog(ConfirmationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            stringRequest = new StringRequest(Request.Method.DELETE, delete_to_cart_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i("LOG_VOLLEY", response);
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();

                    if (response != null) {

                        //CommonFun.alertError(ConfirmationActivity.this,response.toString());

                        try {

                            delete_successfully= Boolean.parseBoolean(response);



                        } catch (Exception e) {
                            //e.printStackTrace();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content),"Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();

                        }
                    }

                    if(delete_successfully == true)
//                                callConfirmationActivity();
//                                refreshItemCount(ConfirmationActivity.this);

                      //  arr_price[current_index]="0";
                    {
                        Intent intent=new Intent(ConfirmationActivity.this, ConfirmationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        CommonFun.finishscreen(ConfirmationActivity.this);
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();


                    //onResume();
                    Intent intent=new Intent(ConfirmationActivity.this, ConfirmationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    CommonFun.finishscreen(ConfirmationActivity.this);
//                    Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                    startActivity(intent);

                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    headers.put("Content-Type","application/json");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }


//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }

            };
        } catch (Exception e) {
            //e.printStackTrace();
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
            startActivity(intent);
        }


//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));

        stringRequest.setShouldCache(false);
        queue.add(stringRequest);




    }

    /**
     * fetch cart subtotal amount from api
     * and show to the user  "<br/>"+
     */
    private void callCartItemAmount() {

        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(ConfirmationActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, Global_Settings.cart_amount_api,

                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("log_response", response.toString());
//                        CommonFun.alertError(ConfirmationActivity.this, response.toString());

                        lstadapter=null;

                        if(pDialog.isShowing())
                            pDialog.dismiss();



                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            ////Log.d("Response",String.valueOf(response));

                            subtotal_inc_tax=jsonObj.getString("subtotal_incl_tax");
                            disc_amount=jsonObj.getString("discount_amount");
                            base_grand_total=jsonObj.getString("base_grand_total");
                            String base_total=jsonObj.getString("base_subtotal");

                            String base_shipping_amount=jsonObj.getString("base_shipping_amount");
                            pref = CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("base_shipping_amount",inc_tax);
                            editor.commit();

                            String st_base_grand_total="";
                            try {
                                st_base_grand_total=String.valueOf(Float.parseFloat(base_total)+Float.parseFloat(inc_tax));
                                st_base_grand_total=String.valueOf(Float.parseFloat(st_base_grand_total)+Float.parseFloat(disc_amount));

                            }
                            catch (NumberFormatException ex){

                            }

                            JSONArray jsonArray = jsonObj.getJSONArray("total_segments");
                            for(int j = 0; j<jsonArray.length(); j++)
                            {
                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                String code =  jsonObjFinal.getString("code");
                                if(code.equalsIgnoreCase("subtotal"))
                                {
                                    String title =  jsonObjFinal.getString("title");
                                    String value =  jsonObjFinal.getString("value");
                                    base_total = value;
                                    Log.e("subtotal", base_total);

                                }
                                  if(code.equalsIgnoreCase("shipping"))
                                {
                                    String title =  jsonObjFinal.getString("title");
                                    String value =  jsonObjFinal.getString("value");
                                    inc_tax = value;
                                    Log.e("shipping", inc_tax);
                                }

                                  if(code.equalsIgnoreCase("discount"))
                                {
                                    String title =  jsonObjFinal.getString("title");
                                    String value =  jsonObjFinal.getString("value");
                                    disc_amount = value;
                                    Log.e("discount", disc_amount);
                                }
                                  if(code.equalsIgnoreCase("grand_total"))
                                {
                                    String title =  jsonObjFinal.getString("title");
                                    String value =  jsonObjFinal.getString("value");
                                    st_base_grand_total = value;
                                    Log.e("grand_total", st_base_grand_total);
                                }

                            }



//                            String st_base_grand_total=String.valueOf(Integer.parseInt(base_total)+Integer.parseInt(inc_tax));
//
//                            ////Log.d("st_base_grand_total",st_base_grand_total);
//
//                            st_base_grand_total=String.valueOf(Integer.parseInt(st_base_grand_total)+Integer.parseInt(disc_amount));
                            ////Log.d("st_base_grand_total1",st_base_grand_total);

                            SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                            String login_group_id=pref.getString("login_group_id","");
                            if(login_group_id.equals("4")) {

                                if(jsonObj.has("ip"))
                                    st_total_cart_ip=jsonObj.getString("ip");
                                else
                                    st_total_cart_ip="";
                                String sst_disc= (disc_amount.equals("0"))?"":("Discount/(Voucher Disc.):" + disc_amount + "<br/>");

                                 String ip_text=st_total_cart_ip.equals("")?"":"Total IP: " + st_total_cart_ip;
                                 String st_text = "<b>"+ip_text+"<br/>" +
                                            "-------------------------------</b><br/>" +
                                            "Cart Subtotal (" + total_cart_qty + " item) Inc Tax: ₹ " + base_total +
                                            "<br/>Shipping Charge: " + inc_tax +"<br/>"+

                                             stDonationTitle+": ₹ " +String.valueOf(stDonationValue)+"<br/>"+
                                            sst_disc+
                                            "Total Amount: ₹ " + st_base_grand_total;


                                tv_txt_view.setText(Html.fromHtml(st_text));
                                cb_donation_msg.setText(stDonationTitle);
                                if(stDonationValue > 0) {
                                    cb_donation_msg.setChecked(true);
                                }
                                isFirstTime = true;

                               // getOfferDetails();
                            }
                            else {
                                if(pDialog.isShowing())
                                    pDialog.dismiss();
                                String sst_disc=(disc_amount.equals("0"))?"":("Discount/(Voucher Disc.):" + disc_amount + "<br/>");
                                String st_text="Cart Subtotal (" + total_cart_qty + " item) Inc Tax: ₹ " + base_total +
                                        "<br/>Shipping Charge: " + inc_tax +"<br/>"+
                                        stDonationTitle+": ₹ " +String.valueOf(stDonationValue)+"<br/>"+
                                        sst_disc+"<br/>"+
                                        "Total Amount:" + st_base_grand_total;

                                tv_txt_view.setText(Html.fromHtml(st_text));
                                cb_donation_msg.setText(stDonationTitle);
                                if(stDonationValue > 0) {
                                    cb_donation_msg.setChecked(true);
                                }
                                isFirstTime = true;
                              //  btConfirmOrder.setVisibility(View.VISIBLE);
                            }

                            /**
                             * Remove Offer Calucation from app
                             * now it will auto from backend
                             */
                            //getOfferDetails();

                            btConfirmOrder.setVisibility(View.VISIBLE);
                            is_data_load=true;
                            /**
                             * check offer details from api
                             */

                        } catch (JSONException e) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //e.printStackTrace();
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                            startActivity(intent);

                        }

                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if (error instanceof ParseError || error instanceof ServerError) {
                            int total_cart_count =0;

                        }
                        else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error, ConfirmationActivity.this);
                        }


//                        Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                        startActivity(intent);


                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }


    /**
     * check offer details from api
     */
    private void getOfferDetails() {
//        pDialog = new TransparentProgressDialog(ConfirmationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();

        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
        String login_group_id=pref.getString("login_group_id","");
        if(login_group_id.equalsIgnoreCase(""))
        {
           Intent intent=new Intent(ConfirmationActivity.this, CartItemList.class);
           startActivity(intent);
           CommonFun.finishscreen(ConfirmationActivity.this);

        }

        String st_offer_api_URL = Global_Settings.api_custom_url+"offerData.php?customer_type="+login_group_id;

        //Log.d("offer_url",st_offer_api_URL);


        RequestQueue requestQueue = Volley.newRequestQueue(ConfirmationActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                st_offer_api_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String cust_id = pref.getString("login_customer_id","");
                        //Log.d("offer_details", cust_id+"-"+response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();

/**
 * Fetch all offer details from the api
 */
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            JSONArray arr_catalog_sku = object.getJSONArray("catalog_sku");

                            arr_catalog_length = arr_catalog_sku.length();
                            arr_non_offer_sku = new String[arr_catalog_length];

                            for(int x=0; x<arr_catalog_length; x++){

                                String st_sku_catalog = arr_catalog_sku.getString(x);

                                arr_non_offer_sku[x] =st_sku_catalog;
                                ////Log.d("arr_non_offer_sku",arr_non_offer_sku[x]);
                            }

                            int offer_status = object.getInt("status");
                            ////Log.d("offer_status",offer_status+"");


                            JSONArray offer_obj = object.getJSONArray("offer");
                            int len = offer_obj.length();
                            ////Log.d("len",""+len);

                            arr_offered_sku = new String[len];
                            arr_sku_off = new String[len];
                            arr_qty_off = new String[len];
                            arr_thrshold_amt = new String[len];
                            arr_max_price = new String[len];
                            for (int a=0; a< len; a++) {

                                JSONObject off_obj = offer_obj.getJSONObject(a);
                                st_sku_off1 = off_obj.getString("Sku");
                                st_thresshold_price_off1 = off_obj.getString("price");
                                st_qty_off1 = off_obj.getString("qty");
                                st_max_price = off_obj.getString("maxPrice");

//                                offered_cart_item.add(st_sku_off1);

                                arr_sku_off[a] = st_sku_off1;
                                arr_qty_off[a] = st_qty_off1;
                                arr_thrshold_amt[a] = st_thresshold_price_off1;
                                arr_max_price[a] = st_max_price;



                            }

//                            for (int x = 0; x < arr_sku_off.length; x++) {
//                                for (int p = 0; p < arr_sku.length; p++) {
//
//                                    if (arr_sku_off[x].equalsIgnoreCase(arr_sku[p])) {
//                                        cart_item_id = arr_item_id[p];
//                                        deleteCartItem(p, false, "");
//
//                                    }
//
//
//                                }
//                            }

                            /**
                             *Status 1 = offer on,status 0 = offer off
                              */

                            if (offer_status == 1) {

                                /**
                                 * calculate offer
                                 */
                                ////Log.d("offer","offer");
                               // offerCalculate();

                            }
                            else if(offer_status==0){
                                if(pDialog.isShowing())
                                    pDialog.dismiss();

                                btConfirmOrder.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                            startActivity(intent);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        CommonFun.showVolleyException(error, ConfirmationActivity.this);


                    }
                })

        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        requestQueue.add(request);


    }

    /**
     * Calculate offer from the fetched details from api
     */
    private void offerCalculate() {

        total_offer_amount = 0.0;


        for (int i = 0; i < total_cart_count; i++) {


            for (int j = 0; j < arr_catalog_length; j++) {

                if (!arr_sku[i].equalsIgnoreCase(arr_non_offer_sku[j])) {

                    arr_offer_cart_item[i] = arr_sku[i];
                    arr_offer_item_qty[i] = arr_qty[i];
                    arr_offer_item_price[i] = arr_price[i];


                } else {

                    arr_offer_cart_item[i] = "0";
                    arr_offer_item_qty[i] = "0";
                    arr_offer_item_price[i] = "0";


                    j = arr_catalog_length;


                }

            }

        }


        for (int s = 0; s < arr_offer_cart_item.length; s++) {

            if (!arr_offer_item_price[s].equals("0")
                    && !arr_offer_item_qty[s].equals("-1") && !arr_offer_item_qty[s].equals("0")) {

                Double offered_bill = Double.parseDouble(arr_offer_item_price[s]);
                int offered_qty = Integer.parseInt(arr_offer_item_qty[s]);
                total_amt = offered_bill * offered_qty;
                ////Log.d("Total_amt", "" + total_amt);

                total_offer_amount = total_offer_amount + total_amt;
//                    ////Log.d("Total_offer", total_offer_amount);
//                        total_cart_qty = total_cart_qty + Integer.parseInt(arr_qty[i]);
            }

            ////Log.d("Total_offer", "" + total_offer_amount);
        }


            /**
            * Hide add,sub,and delete button
            */

//        for (int x = 0; x < arr_sku_off.length; x++) {
//            for (int p = 0; p < arr_sku.length; p++) {
//
//                if (arr_sku_off[x].equalsIgnoreCase(arr_sku[p])) {
//                    arr_boolean_edit[p] = "false";
//
//                }
//
//
//            }
//        }

        if(arr_thrshold_amt.length>0) {
            for (int c = 0; c < arr_thrshold_amt.length; c++) {
                int total_found = 0;
                st_thresshold_price_off1 = arr_thrshold_amt[c];
                st_thresshold_price_off2 = arr_max_price[c];
                st_sku_off1 = arr_sku_off[c];
                st_qty_off1 = arr_qty_off[c];

                if (total_offer_amount >= Double.parseDouble(st_thresshold_price_off1) &&
                        total_offer_amount <= Double.parseDouble(st_thresshold_price_off2)) {


                    for (int m = 0; m < arr_sku.length; m++) {

                        if (arr_sku[m].equalsIgnoreCase(st_sku_off1)) {

                            total_found++;

                        }

                    }

//                for(int j = arr_sku.length; j > 0; j--){
//
//                    if(j != foundIndex){
//                        cart_item_id = arr_item_id[j];
//                        deleteCartItem(j, false, "");
//
//
//
//                    }
//                }


                    if (total_found == 0) {

//                    cart_item_id = arr_item_id[delete_index];

//                    deleteCartItem(delete_index, false, "");
                        ////Log.d("addtocart", "addtocart");
                        addItemToCart(item_quote_id, st_sku_off1, st_qty_off1);


                    }
                }
                else
                {
                    btConfirmOrder.setVisibility(View.VISIBLE);
                }


            }
        }
        else
        {
            btConfirmOrder.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Add item to the cart
     * when found in to the offer
     * @param cart_id
     * @param off_product_sku
     * @param off_cartqty
     */
           private void addItemToCart (String cart_id, final String off_product_sku, String off_cartqty){


            add_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/";


            final String stxt = "{\"cartItem\":{\"sku\": \"" + off_product_sku + "\",\"qty\": " + off_cartqty + ",\"quote_id\": \"" + cart_id + "\"}}";
            ////Log.d("stxt value", stxt);


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = null;
            try {
                jsObjRequest = new JsonObjectRequest(Request.Method.POST, add_to_cart_URL, new JSONObject(stxt),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                if(pDialog.isShowing())
                                    pDialog.dismiss();
                                if (response != null) {
                                    try {
                                        JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                        String st_qty = jsonObj.getString("qty");
                                        ////Log.d("st_qty", st_qty);

                                        /**
                                         * Update cart quantity
                                         */
                                        //refreshItemCount(ConfirmationActivity.this);
                                        //refreshItemCount();


//                                    bindDatainCartList(true);
//                                    onResume();
//                                    callConfirmationActivity();
                                       // onResume();

                                       // btConfirmOrder.setVisibility(View.VISIBLE);
                                       onResume();
                                        //loadCartProductImage(off_product_sku);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                                        startActivity(intent);
                                        btConfirmOrder.setVisibility(View.GONE);
                                    }

                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        btConfirmOrder.setVisibility(View.GONE);
                        if (error instanceof ServerError) {

                            CommonFun.alertError(ConfirmationActivity.this, "Please try to add maximum of 25 qty...");
                        } else
                            CommonFun.showVolleyException(error, ConfirmationActivity.this);
                    }


                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();

                        headers.put("Authorization", "Bearer " + tokenData);
                        //headers.put("Content-Type","application/json");
                        return headers;
                    }

                };
            } catch (JSONException e) {
                e.printStackTrace();
            }


            RetryPolicy retryPolicy = new DefaultRetryPolicy(1000 * 60,
                    0, 0);
            jsObjRequest.setRetryPolicy(retryPolicy);

            jsObjRequest.setShouldCache(false);
            queue.add(jsObjRequest);

        }

    /**
     * fetch cart product images
     * and load
     * @param items_sku
     */

    private void loadCartProductImage(String items_sku) {


        ////Log.d("cart_images","cartimage");

        dbh = new DatabaseHandler(ConfirmationActivity.this);

//        String items_sku = "";
//        for (int i = 0; i < arr_sku.length; i++) {
//            if (items_sku.equals(""))
//                items_sku = arr_sku[i];
//            else
//                items_sku = items_sku + "," + arr_sku[i];
//        }

        String getimage_url = Global_Settings.api_custom_url + "cart_product_img.php?sku=" + items_sku;
        ////Log.d("cart",getimage_url);
        //dbh.deleteCartProductImage();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, getimage_url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //   CommonFun.alertError(MainActivity.this,response.toString());
                        if (response != null) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            ////Log.d("cart_response",response.toString());
                            try {

                                JSONObject jsonObj = new JSONObject(String.valueOf(response));


                                JSONArray custom_data = jsonObj.getJSONArray("images");

                                for (int k = 0; k < custom_data.length(); k++) {

                                    JSONObject c_product_img = custom_data.getJSONObject(k);
                                    String st_product_image = c_product_img.getString("images");
                                    String st_product_sku = c_product_img.getString("sku");


                                    dbh.addCartProductImage(new CartProductImage(st_product_sku, st_product_image));
//
                                }

                              onResume();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progress_bar.setProgress(View.GONE);
                                Intent intent=new Intent(ConfirmationActivity.this, ExceptionError.class);
                                startActivity(intent);
                            }

                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // progress_bar.setProgress(View.GONE);
                CommonFun.showVolleyException(error, ConfirmationActivity.this);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + tokenData);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);


    }


}


//    private void getPaymentMethod(String shipping_info_string, final Boolean callFromCreate, final String selItemCode, final Boolean ispayu) {
//
//        itemList = new ArrayList<HashMap<String, String>>();
//
//
//        pDialog = new TransparentProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsObjRequest = null;
//        try {
//            jsObjRequest = new JsonObjectRequest(Request.Method.POST, payment_method_url, new JSONObject(shipping_info_string),
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            if (pDialog.isShowing())
//                                pDialog.dismiss();
//
//
//                            ////Log.d("response", response.toString());
//                            //CommonFun.alertError(Payment_Method_Activity.this,response.toString());
//                            if (response != null) {
//                                try {
//
//
//                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));
//                                    JSONArray json_payment_method = jsonObj.getJSONArray("payment_methods");
//
//                                    arr_pay_code = new String[json_payment_method.length()];
//
//                                    if(callFromCreate==true) {
//                                        for (int i = 0; i < json_payment_method.length(); i++) {
//
//                                            JSONObject jsonObject = json_payment_method.getJSONObject(i);
//                                            String payment_code = jsonObject.getString(TAG_pay_code);
//                                            String payment_title = jsonObject.getString(TAG_pay_title);
//
//                                            HashMap<String, String> hashMap = new HashMap<String, String>();
//                                            hashMap.put(TAG_pay_code, payment_code);
//                                            hashMap.put(TAG_pay_title, payment_title);
//
//                                            if (payment_code.toLowerCase().contains("payu")) {
//
//                                                String sales_user_zone = pref.getString("st_dist_id", "").toLowerCase();
//                                                String magento_user_zone = pref.getString("log_user_zone", "").toLowerCase();
//
//                                                if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                                                        magento_user_zone.equalsIgnoreCase("rkt")) {
//
//
//                                                    arr_pay_code[i] = payment_code;
//                                                    itemList.add(hashMap);
//
//
//                                                }
//
//
//                                            } else {
//                                                // arr_pay_code = new String[1];
//                                                arr_pay_code[i] = payment_code;
//                                                itemList.add(hashMap);
//                                            }
//                                        }
//                                    }
//
//                                    JSONObject json_grand_total = jsonObj.getJSONObject("totals");
//                                    jsonObject_grand_total = json_grand_total.getString("base_grand_total");
//                                    jsonObject_total_discount=json_grand_total.getString("discount_amount");
//                                    ////Log.d("grand total", jsonObject_grand_total);
//
//
//
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//
//                        }
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    //error.printStackTrace();
//                    CommonFun.showVolleyException(error, ConfirmationActivity.this);
//
//                }
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<>();
//
//                    headers.put("Authorization", "Bearer " + tokenData);
//                    //headers.put("Content-Type","application/json");
//                    return headers;
//                }
//
//            };
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        queue.add(jsObjRequest);
//    }



