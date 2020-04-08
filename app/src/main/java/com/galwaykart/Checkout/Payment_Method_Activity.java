package com.galwaykart.Checkout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.galwaykart.Payment.PayUMainActivity;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.OrderDetails;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Payment.MainActivity;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method;

/**
 * Show payment method to the user
 * Created by ankesh on 9/25/2017.
 *
 *
 * ewallet condition
 * ewallet payment method will be shown to user
 * when the total amount <= ewallet amount
 *
 * added on 03-04-2018
 * Ankesh Kumar
 */

public class Payment_Method_Activity  extends BaseActivityWithoutCart {

    SharedPreferences pref;
    TransparentProgressDialog pDialog;
    String payment_method_url = "";
    String tokenData;
    ListView list_payment_method;
    ArrayList<HashMap<String, String>> itemList;
    String TAG_pay_code = "code";
    String TAG_pay_title = "title";
    String[] arr_pay_code;
    String order_id_method_url = "";
    String order_id_method_data = "";
    String jsonObject_grand_total = "";
    String jsonObject_total_discount="";
    String cart_imagepath="";
    int total_cart_qty;
    String log_user_zone="";
    String log_user_id="";
   // String get_Ip_URL = Global_Settings.api_custom_url+"ip.php?sku=";
   // String user_detail_url;
    final String TAG_total_item_count = "items_count";
    final String TAG_total_items_qty = "items_qty";
    final String TAG_item_id = "item_id";
    final String TAG_sku = "sku";
    final String TAG_qty = "qty";
    final String TAG_name = "name";
    final String TAG_price = "price";
    final String TAG_quote_id = "quote_id";

    int total_cart_count;
    String[] product_ip;
    String[] product_sku;
    String[] product_qty;
    String[] product_dp;
    String[] product_mrp;
    String[] product_discount;
    String[] single_pro_sorting;
    String url_cart_item_list = "";
    String user_zone;
    String product_ip_url="";
    JSONArray dist_details = null;
    JsonObjectRequest jsonObjRequest = null;
    String shipping_info_string="",ship_email="";
    String order_id_gen;
    String[] arr_sku, arr_qty,arr_quote_id,arr_name,arr_price,arr_initial_qty,arr_final_qty,arr_boolean,arr_qty_api,arr_item_id,
            arr_updated_cart_qty;
    String st_producted_sku="",st_selected_address="";
    String ip="",mrp="",final_sku="";
    String [] arr_ip,arr_mrp,arr_final_sku;
    Boolean is_ewallet=false;   
    float ewallet_amount=0;
     Boolean is_ewallet_selected=false;
    int sales_checkout_retry=0;

    String del_address="",st_city="",st_state="",st_pincode="",ship_fname="",ship_lname="",st_cust_name="",login_tel="";
    String after_order_hash_url="";
    String login_group_id="";

    Boolean is_payment_method_load=false;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated meth     od stub
        super.onBackPressed();
       goBack();

    }

    private void goBack() {
        Intent intent = new Intent(this, CartItemList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        initNavigationDrawer();
        after_order_hash_url=Global_Settings.api_custom_url+"galwaykart/success_checkout_cod.php?orderhash=";

    }


    @Override
    protected void onResume() {
        super.onResume();

        list_payment_method = findViewById(R.id.list_payment_method);
        url_cart_item_list = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/carts/mine";

        pref = CommonFun.getPreferences(getApplicationContext());
        st_selected_address = pref.getString("st_selected_address", "").toLowerCase();     //Changed by Sumit


        login_group_id=pref.getString("login_group_id","");

        tokenData = pref.getString("tokenData", "");
        //Log.d("tokenData11111",tokenData);

        user_zone=pref.getString("log_user_zone","");
        String cust_id = pref.getString("login_customer_id","");
        ////Log.d("cust_id_login",cust_id);
        //user_detail_url= Global_Settings.user_details_url+cust_id;

        /**
         * Fetch all details from the temp storage
         */
//
        String ship_region = "";
        String ship_region_id ="";
        String ship_region_code = "";
        String ship_country_id = "";
        String ship_street = "";
        String ship_postcode = "";
        String ship_city = "";
        String ship_fname = "";
        String ship_lname = "";
         ship_email = "";
        String ship_telephone = "";


        if (ship_country_id.equalsIgnoreCase(""))
            ship_country_id = "IN";

        if (ship_region_id.equals(""))
            ship_region_id = "0";

//        String bill_region = pref.getString("bill_region", "");
//        String bill_region_id = pref.getString("bill_region_id", "");
//        String bill_region_code = pref.getString("bill_region_code", "");
//        String bill_country_id = "IN";
//        String bill_street = pref.getString("bill_add_line1", "");
//        String bill_postcode = pref.getString("bill_postcode", "");
//        String bill_city = pref.getString("bill_city", "");
//        String bill_fname = pref.getString("bill_firstname", "");
//        String bill_lname = pref.getString("bill_lastname", "");
//        String bill_email = pref.getString("login_email", "");
//        String bill_telephone = pref.getString("bill_telephone", "");
//
        String carrier_code = pref.getString("arr_carrier_code", "");
        String method_code = pref.getString("arr_method_code", "");
//
//
//        if (bill_country_id.equalsIgnoreCase(""))
//            bill_country_id = "IN";
//
//        if (bill_region_id.equals(""))
//            bill_region_id = "0";

        String bill_region ="";
        String bill_region_id = pref.getString("bill_region_id", "");
        String bill_region_code = pref.getString("bill_region_code", "");
        String bill_country_id = "IN";
        String bill_street = "";
        String bill_postcode = "";
        String bill_city = "";
        String bill_fname ="";
        String bill_lname = "";
        String bill_email = "";
        String bill_telephone = "";

       // st_selected_address="Home";

        String st_selected_address=pref.getString("st_selected_address","");

        if(st_selected_address.equalsIgnoreCase("Franchisee")) {

//            ship_region = pref.getString("region", "");
//            ship_region_id = pref.getString("region_id", "");
//            ship_region_code = pref.getString("region_code", "");
//            ship_country_id = pref.getString("country_id", "");
//            ship_street = pref.getString("add_line1", "");
//            ship_postcode = pref.getString("postcode", "");
//            ship_city = pref.getString("city", "");
//            ship_fname = pref.getString("firstname", "");
//            ship_lname = pref.getString("lastname", "");
//            ship_email = pref.getString("login_email", "");
//            ship_telephone = pref.getString("telephone", "");
//
//

            bill_region = pref.getString("region", "");
            bill_street = pref.getString("add_line1", "");
            bill_postcode = pref.getString("postcode", "");
            bill_city = pref.getString("city", "");
            bill_fname = pref.getString("firstname", "");
            bill_lname = pref.getString("lastname", "");
            bill_email = pref.getString("login_email", "");
            st_cust_name = ship_fname + " " + ship_lname;
            bill_telephone = pref.getString("telephone", "");
            bill_region_id = pref.getString("region_id", "");
            bill_region_code = pref.getString("region_code", "");
            ship_fname=bill_fname;
            ship_lname=bill_lname;
            ship_email=bill_email;

           ship_region = bill_region;
             ship_region_id =bill_region_id;
             ship_region_code = bill_region_code;
             ship_country_id = bill_country_id;
             ship_street = bill_street;
             ship_postcode = bill_postcode;
             ship_city = bill_city;
             ship_fname = bill_fname;
             ship_lname = bill_lname;
            ship_email = bill_email;
             ship_telephone = bill_telephone;



        }
        else if(st_selected_address.equalsIgnoreCase("Home")) {
//            bill_region = pref.getString("new_region", "");
//            bill_street = pref.getString("new_add_line1", "");
//            bill_postcode = pref.getString("new_postcode", "");
//            bill_city = pref.getString("new_city", "");
//            bill_fname = pref.getString("new_firstname", "");
//            bill_lname = pref.getString("new_lastname", "");
//            bill_email = pref.getString("login_email", "");
//            st_cust_name = ship_fname + " " + ship_lname;
//            bill_telephone = pref.getString("new_telephone", "");
//            bill_region_id = pref.getString("new_region_id", "");
//            bill_region_code = pref.getString("new_region_code", "");
//            //login_zone="";
//            ship_fname=bill_fname;
//            ship_lname=bill_lname;
//            ship_email=bill_email;
//
//            ship_region = bill_region;
//            ship_region_id =bill_region_id;
//            ship_region_code = bill_region_code;
//            ship_country_id = bill_country_id;
//            ship_street = bill_street;
//            ship_postcode = bill_postcode;
//            ship_city = bill_city;
//            ship_fname = bill_fname;
//            ship_lname = bill_lname;
//            ship_email = bill_email;
//            ship_telephone = bill_telephone;

            bill_region = pref.getString("region", "");
            bill_street = pref.getString("add_line1", "");
            bill_postcode = pref.getString("postcode", "");
            bill_city = pref.getString("city", "");
            bill_fname = pref.getString("firstname", "");
            bill_lname = pref.getString("lastname", "");
            bill_email = pref.getString("login_email", "");
            st_cust_name = ship_fname + " " + ship_lname;
            bill_telephone = pref.getString("telephone", "");
            bill_region_id = pref.getString("region_id", "");
            bill_region_code = pref.getString("region_code", "");
            ship_fname=bill_fname;
            ship_lname=bill_lname;
            ship_email=bill_email;

            ship_region = bill_region;
            ship_region_id =bill_region_id;
            ship_region_code = bill_region_code;
            ship_country_id = bill_country_id;
            ship_street = bill_street;
            ship_postcode = bill_postcode;
            ship_city = bill_city;
            ship_fname = bill_fname;
            ship_lname = bill_lname;
            ship_email = bill_email;
            ship_telephone = bill_telephone;

        }


        /**
         * create json string to pass it into api
         */

            shipping_info_string = "{" +
                    "\"addressInformation\":{" +
                    "\"shipping_address\":{" +
                    "\"region\": \" " + ship_region + "\"," +
                    "\"region_id\": " + ship_region_id + ", " +
                    "\"region_code\": \"" + ship_region_code + "\", " +
                    "\"country_id\": \"" + ship_country_id + "\", " +
                    "\"street\": [ \"" + ship_street + "\" ], " +
                    "\"postcode\": \"" + ship_postcode + "\", " +
                    "\"city\": \"" + ship_city + "\", " +
                    "\"firstname\": \"" + ship_fname + "\", " +
                    "\"lastname\": \"" + ship_lname + "\", " +
                    "\"email\": \"" + ship_email + "\", " +
                    "\"telephone\": \"" + ship_telephone + "\" }," +
                    "\"billing_address\":{ " +
                    "\"region\": \" " + bill_region + "\"," +
                    "\"region_id\": " + bill_region_id + ", " +
                    "\"region_code\": \"" + bill_region_code + "\", " +
                    "\"country_id\": \"" + bill_country_id + "\", " +
                    "\"street\": [ \"" + bill_street + "\" ], " +
                    "\"postcode\": \"" + bill_postcode + "\", " +
                    "\"city\": \"" + bill_city + "\", " +
                    "\"firstname\": \"" + bill_fname + "\", " +
                    "\"lastname\": \"" + bill_lname + "\", " +
                    "\"email\": \"" + bill_email + "\", " +
                    "\"telephone\": \"" + bill_telephone + "\" }," +
                    "\"shipping_carrier_code\": \"" + carrier_code + "\", " +
                    "\"shipping_method_code\": \"" + method_code + "\" " +
                    "}" +
                    "}";


        //Log.d("shipstring",shipping_info_string);

        payment_method_url = Global_Settings.api_url + "rest/V1/carts/mine/shipping-information";

        //getPaymentMethod(shipping_info_string);

        /**
         * fetch all cart products from api
         */
        //callConfirmedProducts();
        getPaymentMethod(shipping_info_string, true, "", false);
    }

    /**
     * fetch all cart products from api
     */
    private void callConfirmedProducts() {


        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(Payment_Method_Activity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("responseConfmProdct", response.toString());
//                        CommonFun.alertError(Payment_Method_Activity.this, response.toString());


                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));
                            total_cart_qty = Integer.parseInt(jsonObj.getString(TAG_total_items_qty));


                            JSONArray custom_data = jsonObj.getJSONArray("items");

                            arr_sku = new String[total_cart_count];
                            arr_qty = new String[total_cart_count];
                            arr_quote_id= new String[total_cart_count];
                            arr_name = new String[total_cart_count];
                            arr_price = new String[total_cart_count];
                            arr_initial_qty = new String[total_cart_count];
                            arr_final_qty = new String[total_cart_count];
                            arr_boolean = new String[total_cart_count];
                            arr_qty_api= new String [total_cart_count];
                            arr_item_id= new String[total_cart_count];
                            arr_updated_cart_qty = new String[total_cart_count];
//                            arr_ip = new String[total_cart_count];
//                            arr_mrp = new String[total_cart_count];
//                            arr_final_sku = new String[total_cart_count];


                            for (int i = 0; i < total_cart_count; i++) {
                                JSONObject c = custom_data.getJSONObject(i);

                                String item_id = c.getString(TAG_item_id);
                                String item_sku = c.getString(TAG_sku);
                                String item_qty = c.getString(TAG_qty);
                                String item_name = c.getString(TAG_name);
                                String item_price = c.getString(TAG_price);
                                //item_quote_id= c.getString(TAG_quote_id);

                                arr_sku[i] = item_sku;
                                arr_qty[i] = item_qty;
                               // arr_quote_id[i] = item_quote_id;
                                arr_name[i] = item_name;
                                arr_price[i] = item_price;
                                arr_initial_qty[i] = item_qty;
                                arr_boolean[i]="false";
                                arr_item_id [i] = item_id;


                               // //Log.d("item_skudghs",item_sku);
                               // //Log.d("item_pricefghds",item_price);

                                DatabaseHandler dbh=new DatabaseHandler(Payment_Method_Activity.this);
                                if(dbh.getCartProductImageSKuCount()>0) {
                                    List<CartProductImage> contacts = dbh.getCartProductImage(arr_sku[i]);

                                    for (CartProductImage cn : contacts) {
                                        cart_imagepath = cn.get_image();
                                    }
                                }


                            }

                            /**
                             * Get IP of all the cart items
                             */
                            //getIpFromAPI();
                            //checkStockAvailability();

//                            SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
//                            String login_group_id=pref.getString("login_group_id","");
                           // if(login_group_id.equals("4")) {
                              //  getUserDetails(user_detail_url, shipping_info_string, payment_method_url);

//                                String crm_status="",crm_msg="",crm_irid="";
//
//                                JSONObject jsonObjects=jsonObj.getJSONObject("customer");
//                                JSONArray jsonArray=jsonObjects.getJSONArray("custom_attributes");
//
//                                for(int i=0;i<jsonArray.length();i++)
//                                {
//                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//                                    String attr_name=jsonObject.getString("attribute_code");
//                                    if(attr_name.equalsIgnoreCase("crm_status"))
//                                    {
//                                        crm_status=jsonObject.getString("value");
//
//                                    }
//                                    else if(attr_name.equalsIgnoreCase("crm_msg"))
//                                    {
//                                        crm_msg=jsonObject.getString("value");
//                                    }
//                                    else if(attr_name.equalsIgnoreCase("irid"))
//                                    {
//                                        crm_msg=jsonObject.getString("value");
//                                    }

                               // }
                          //  if(crm_status.equals("1")||crm_status.equals("")) {
                            if(is_payment_method_load==false)
                                getPaymentMethod(shipping_info_string, true, "", false);
//                            }
//                            else
//                            {
//                                CommonFun.alertError(Payment_Method_Activity.this,crm_msg);
//                            }
//                            }
//                            else {
//                                getPaymentMethod(shipping_info_string, true, "", false);
//                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            //Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
                            //startActivity(intent);


                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                            CommonFun.showVolleyException(error,Payment_Method_Activity.this);



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
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

    /**
     * Get IP of all the cart items
     */

//    private void getIpFromAPI() {
//
//        for(int i=0; i<total_cart_count; i++){
//            if(st_producted_sku.equalsIgnoreCase(""))
//                st_producted_sku = arr_sku[i];
//            else
//                st_producted_sku = st_producted_sku+","+arr_sku[i];
//
//        }
//        ////Log.d("st_producted_sku",st_producted_sku);
//
//        get_Ip_URL = get_Ip_URL+st_producted_sku;
//        ////Log.d("get_Ip_URL",get_Ip_URL);
//
//        RequestQueue requestQueue = Volley.newRequestQueue(Payment_Method_Activity.this);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
//                get_Ip_URL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        ////Log.d("offerRes", response.toString());
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        try {
//                            JSONObject object = new JSONObject(String.valueOf(response));
//
//                            JSONArray arr_obj = object.getJSONArray("extradeatils");
//                            int arr_len = arr_obj.length();
//                            arr_ip = new String[arr_len];
//                            arr_mrp = new String[arr_len];
//                            arr_final_sku = new String[arr_len];
//
//                            for(int i =0; i< arr_len; i++){
//
//                                JSONObject c = arr_obj.getJSONObject(i);
//                                ip = c.getString("ip");
//                                mrp= c.getString("mrp");
//                                final_sku = c.getString("sku");
//
//                                arr_ip[i] = ip;
//                                arr_mrp[i] = mrp;
//                                arr_final_sku[i] = final_sku;
//                            }
//
//                            /**
//                             * Get user zone and id from magento
//                             *
//                             */
//                            getUserDetails(user_detail_url,shipping_info_string,payment_method_url);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            if(pDialog.isShowing())
//                                pDialog.dismiss();
//
////                            CommonFun.alertError(Payment_Method_Activity.this,e.toString());
//                            Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                            startActivity(intent);
//
//
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//                        CommonFun.showVolleyException(error, Payment_Method_Activity.this);
////                        CommonFun.alertError(Payment_Method_Activity.this,error.toString());
//                    }
//                })
//
//        {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//
//                return params;
//            }
//
//        };
//        request.setShouldCache(false);
//        requestQueue.add(request);
//
//
//    }

    /**
     * get all available payment method from api
     * @param shipping_info_string
     * @param callFromCreate
     * @param selItemCode
     * @param ispayu
     */

    private void getPaymentMethod(String shipping_info_string, final Boolean callFromCreate, final String selItemCode, final Boolean ispayu) {

        itemList = new ArrayList<HashMap<String, String>>();

        //Log.d("payment_meth989999",payment_method_url);

        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Method.POST, payment_method_url, new JSONObject(shipping_info_string),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();


                            //Log.d("response9858585695", response.toString());


                            //CommonFun.alertError(Payment_Method_Activity.this,response.toString());
                            if (response != null) {
                                try {


                                    is_payment_method_load=true;

                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));
                                    JSONArray json_payment_method = jsonObj.getJSONArray("payment_methods");

                                    arr_pay_code = new String[json_payment_method.length()];

                                    /**
                                     * fetch total and discount amount from
                                     */
                                    JSONObject json_grand_total = jsonObj.getJSONObject("totals");
                                    jsonObject_grand_total = json_grand_total.getString("base_grand_total");
                                    jsonObject_total_discount=json_grand_total.getString("discount_amount");
                                    ////Log.d("grand total", jsonObject_grand_total);

                                    if(ewallet_amount>0.0f && ewallet_amount>=Float.parseFloat(jsonObject_grand_total))
                                        is_ewallet=true;

                                    if(is_ewallet==true)
                                        arr_pay_code=new String [json_payment_method.length()];
                                    else
                                        arr_pay_code=new String [json_payment_method.length()];
                                    /**
                                     * Get all payment method
                                     */
                                    int current_index=0;
                                    if(callFromCreate==true) {
                                        for (int i = 0; i < json_payment_method.length(); i++) {

                                            JSONObject jsonObject = json_payment_method.getJSONObject(i);
                                            String payment_code = jsonObject.getString(TAG_pay_code);
                                            String payment_title = jsonObject.getString(TAG_pay_title);


                                            if(st_selected_address.equalsIgnoreCase("Franchisee")) {
                                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                                hashMap.put(TAG_pay_code, payment_code);

                                                if(!jsonObject_grand_total.equals("0")) {

                                                    /**
                                                     * if payment mode is ewallet
                                                     * show ewallet amount to the user
                                                     *
                                                     * Ankesh kumar
                                                     */
                                                    if (payment_code.contains("ewallet"))
                                                        hashMap.put(TAG_pay_title, payment_title + " (Avl. Bal.  ₹" + ewallet_amount + ")");
                                                    else
                                                        hashMap.put(TAG_pay_title, payment_title);

                                                    /**
                                                     * Implement rules for payment methods
                                                     * when the Payu,COD will shown or not
                                                     * ewallet condition added on April 4, 2018
                                                     *
                                                     * Ankesh kumar
                                                     */

                                                    if (is_ewallet == true && payment_code.toLowerCase().contains("ewallet")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;
                                                    } else if (payment_code.toLowerCase().contains("payu")) {


//                                                String magento_user_zone = pref.getString("log_user_zone", "").toLowerCase();


//                                                if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                                                        magento_user_zone.equalsIgnoreCase("rkt")) {

                                                        //  arr_pay_code = new String[1];
                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;


//                                                }

                                                    } else if (payment_code.toLowerCase().contains("cashondelivery")) {
                                                        /**
                                                         * check for other payment method
                                                         * if user is from rkt ,then hide ,else show this payment method to the user
                                                         *
                                                         * Ankesh kumar
                                                         */


//                                                String sales_user_zone = pref.getString("st_dist_id", "").toLowerCase();
//                                                String magento_user_zone = pref.getString("log_user_zone", "").toLowerCase();


//                                                if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                                                        magento_user_zone.equalsIgnoreCase("rkt")) {
//                                                }
//                                                else {
                                                        //arr_pay_code = new String[1];

                                                        if(login_group_id.equalsIgnoreCase("4")) {
                                                            arr_pay_code[current_index] = payment_code;
                                                            itemList.add(hashMap);

                                                            current_index++;
                                                        }
//                                                }
                                                    } else if (payment_code.toLowerCase().contains("paytm")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;

                                                    } else if (payment_code.toLowerCase().contains("free")) {


                                                     //   if(login_group_id.equalsIgnoreCase("4")) {
                                                            arr_pay_code[current_index] = payment_code;
                                                            itemList.add(hashMap);

                                                            current_index++;
                                                       // }

                                                    }
                                                }
                                                else {
                                                    if (payment_code.toLowerCase().contains("free")) {

                                                      //  if(login_group_id.equalsIgnoreCase("4")) {
                                                            hashMap.put(TAG_pay_title, payment_title);

                                                            arr_pay_code[current_index] = payment_code;
                                                            itemList.add(hashMap);

                                                            current_index++;
                                                       // }

                                                    }
                                                }
                                            }
                                            else if(st_selected_address.equalsIgnoreCase("Home")){

                                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                                hashMap.put(TAG_pay_code, payment_code);
                                                if(!jsonObject_grand_total.equals("0")) {



                                                    if (payment_code.contains("ewallet"))
                                                        hashMap.put(TAG_pay_title, payment_title + " (Avl. Bal.  ₹" + ewallet_amount + ")");
                                                    else
                                                        hashMap.put(TAG_pay_title, payment_title);


                                                    if (is_ewallet == true && payment_code.toLowerCase().contains("ewallet")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;
                                                    } else if (payment_code.toLowerCase().contains("payu")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;

                                                    } else if (payment_code.toLowerCase().contains("paytm")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;

                                                    } else if (payment_code.toLowerCase().contains("free")) {

                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;

                                                    }
                                                }
                                                else {
                                                    if (payment_code.toLowerCase().contains("free")) {

                                                        hashMap.put(TAG_pay_title, payment_title);
                                                        arr_pay_code[current_index] = payment_code;
                                                        itemList.add(hashMap);

                                                        current_index++;

                                                    }
                                                }

                                            }
                                            //paymentMethodRules(payment_code, hashMap);

                                        }
                                    }



                                    if(callFromCreate==true) {
                                        ListAdapter lstadapter = new SimpleAdapter(Payment_Method_Activity.this, itemList,
                                                R.layout.activity_payment_method_list,
                                                new String[]{TAG_pay_title},
                                                new int[]{R.id.textpayment_name
                                                }
                                        );

                                        if (lstadapter.getCount() > 0) {
                                            list_payment_method.setAdapter(lstadapter);
                                        }
                                    }
                                    else
                                    {


                                        /**
                                         * fetch ip of all the cart items
                                         */
                                        ///callCartItemsIP(selItemCode,ispayu);
                                        //Log.d("PaymentMethod","Done");
                                        getOrderId(selItemCode,ispayu);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
                                    startActivity(intent);

                                }
                            }


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    //error.printStackTrace();
                    CommonFun.showVolleyException(error, Payment_Method_Activity.this);

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


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);

        queue.add(jsObjRequest);
    }


    /**
     * Implement rules for payment methods
     * when the Payu,COD will shown or not
     * ewallet condition added on April 4, 2018
     *
     * Ankesh kumar
     */
    private void paymentMethodRules(String payment_code, HashMap<String, String> hashMap) {


    }


    /**
     * Open Item
     *
     * @param v
     */
    public void openPayment(View v) {

        View parentRow = (View) v.getParent();
        final int position = list_payment_method.getPositionForView(parentRow);
        int selindexof = position;

        String sales_user_zone = pref.getString("st_dist_id", "").toLowerCase();
        String magento_user_zone = pref.getString("log_user_zone", "").toLowerCase();

        String selItemCode="";
//        if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                magento_user_zone.equalsIgnoreCase("rkt")) {

//            selItemCode = arr_pay_code[selindexof].toLowerCase();
//        }
//        else
//        {
            selItemCode = arr_pay_code[selindexof].toLowerCase();
//        }

       // //Log.d("selItemCode",selItemCode);


//        SharedPreferences pref;
//        pref= CommonFun.getPreferences(getApplicationContext());
//        SharedPreferences.Editor editor=pref.edit();
//        editor.putString("showitemsku",selItemSku);
//        editor.commit();
//
//        Intent intent=new Intent(Payment_Method_Activity.this, MainActivity.class);
//        startActivity(intent);
//        CommonFun.finishscreen(CartItemList.this);
//            CommonFun.alertError(Payment_Method_Activity.this,selItemCode);


/**
 * if payment method is payu
 */
            if (selItemCode.contains("payu")) {

                is_ewallet_selected=false;
//              CommonFun.alertError(Payment_Method_Activity.this,"payu");
                //CommonFun.alertError();

                final AlertDialog.Builder b;
                try
                {
                    b = new AlertDialog.Builder(this);
                    b.setTitle("निर्देश");
                    b.setCancelable(false);
                    b.setMessage("आई.आर को गैलवेकार्ट पर प्रीपेड आॅर्डर का आई.पी. तुरंत इनवाॅयस कटने पर मिलता है। इस प्रक्रिया में 2 से 24 घंटे का समय लगता है (अवकाश छोड़कर)।");
                    final String finalSelItemCode = selItemCode;
                    b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            String cust_id = pref.getString("login_customer_id","");
                            //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                            getPaymentMethod(shipping_info_string,false, finalSelItemCode,true);
                        }
                    });
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            b.create().dismiss();
                        }
                    });
                    SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                    String login_group_id=pref.getString("login_group_id","");
                    if(login_group_id.equals("4")) {
                        b.create().show();
                    }
                    else
                    {
                        String cust_id = pref.getString("login_customer_id","");
                        //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                        getPaymentMethod(shipping_info_string,false, finalSelItemCode,true);

                    }
                }
                catch(Exception ex)
                {
                }
                //getPaymentMethod(shipping_info_string,false,selItemCode,true);

                //  getOrderId(selItemCode,true,selItemCode,true);

            //getUserDetails(user_detail_url,"2",true);


        } else  if (selItemCode.contains("ewallet")){

            //getPaymentMethod(shipping_info_string,false,selItemCode,false);
  //              CommonFun.alertError(Payment_Method_Activity.this,"ewallet");
                is_ewallet_selected=true;
                final AlertDialog.Builder b;
                try
                {
                    b = new AlertDialog.Builder(this);
                    b.setTitle("निर्देश");
                    b.setCancelable(false);
                    b.setMessage("कृपया ध्यान रखें कि पी.ओ. जनरेट करने के बाद अगर आप फ्रैंचाइज़ी में जाकर उसकी बिलिंग नहीं कराते हैं तो भविष्य में आपकी आई.डी. ब्लॉक भी की जा सकती है।");
                    final String finalSelItemCode = selItemCode;




                    b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            String cust_id = pref.getString("login_customer_id","");
                            //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                            getPaymentMethod(shipping_info_string,false, finalSelItemCode,false);
                        }
                    });
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            b.create().dismiss();
                        }
                    });
                    b.create().show();
                }
                catch(Exception ex)
                {
                }

            //getOrderId(selItemCode,false);
          //  callWMSCheckout("270");
           // callSalesCheckout("20");
        }
            else  if (selItemCode.contains("cashon")){

//                CommonFun.alertError(Payment_Method_Activity.this,"cod");

                //getPaymentMethod(shipping_info_string,false,selItemCode,false);

                is_ewallet_selected=false;
                final AlertDialog.Builder b;
                try
                {
                    b = new AlertDialog.Builder(this);
                    b.setTitle("निर्देश");
                    b.setCancelable(false);
                    b.setMessage("फ्रैंचाइजी पर नकद भुगतान करने के बाद आई.पी आई.आर के अकाउंट में क्रेडिट हो जाएगा ।");
                    final String finalSelItemCode = selItemCode;
                   // //Log.d("finalSelItemCode1",finalSelItemCode);

                    b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            String cust_id = pref.getString("login_customer_id","");
                            //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                            getPaymentMethod(shipping_info_string,false, finalSelItemCode,false);
                        }
                    });
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            b.create().dismiss();
                        }
                    });
                    b.create().show();
                }
                catch(Exception ex)
                {
                }

                //getOrderId(selItemCode,false);
                //  callWMSCheckout("270");
                // callSalesCheckout("20");
            }
        else if (selItemCode.contains("paytm")) {

            is_ewallet_selected=false;
//              CommonFun.alertError(Payment_Method_Activity.this,"payu");
            //CommonFun.alertError();

            final AlertDialog.Builder b;
            try
            {
                final String finalSelItemCode = selItemCode;
//                Intent intent=new Intent(Payment_Method_Activity.this, com.galwaykart.Payment.paytm_integrate.MainActivity.class);
//                startActivity(intent);
                String cust_id = pref.getString("login_customer_id","");
                //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                getPaymentMethod(shipping_info_string,false, finalSelItemCode,true);

            }
            catch(Exception ex)
            {
            }


        }
        else if (selItemCode.contains("free")){

//                CommonFun.alertError(Payment_Method_Activity.this,"cod");

            //getPaymentMethod(shipping_info_string,false,selItemCode,false);

            is_ewallet_selected=false;
                final String finalSelItemCode = selItemCode;
                String cust_id = pref.getString("login_customer_id","");
                //Log.d("payment_selection",cust_id+"-"+finalSelItemCode);
                getPaymentMethod(shipping_info_string,false, finalSelItemCode,false);

        }
    }

    /**
     * Get order hash from the magento
     * @param is_payu (if yes, then redirect to payment gateway
     *                else procceed to call sales checkout api)
     */
    private void getOrderId(final String selItemCode, final Boolean is_payu) {

        payment_method_url = Global_Settings.api_url + "rest/V1/carts/mine/shipping-information";

        String bill_region ="";
        String bill_region_id = "";
        String bill_region_code = "";
        String bill_country_id = "IN";
        String bill_street = "";
        String bill_postcode = "";
        String bill_city = "";
        String bill_fname ="";
        String bill_lname = "";
        String bill_email = "";
        String bill_telephone = "";
        //String login_zone = ((is_payu)?"":(pref.getString("log_user_zone", "")));

        String login_zone =pref.getString("log_user_zone", "");
        String st_selected_address=pref.getString("st_selected_address","");

        if(st_selected_address.equalsIgnoreCase("Franchisee")) {

            bill_region = pref.getString("region", "");
            bill_street = pref.getString("add_line1", "");
            bill_postcode = pref.getString("postcode", "");
            bill_city = pref.getString("city", "");
            bill_fname = pref.getString("firstname", "");
            bill_lname = pref.getString("lastname", "");
            bill_email = pref.getString("login_email", "");
            bill_region_id = pref.getString("region_id", "");
            bill_region_code = pref.getString("region_code", "");
            st_cust_name = ship_fname + " " + ship_lname;
            bill_telephone = pref.getString("telephone", "");
            ship_fname=bill_fname;
            ship_lname=bill_lname;
            ship_email=bill_email;

        }

        else if(st_selected_address.equalsIgnoreCase("Home")) {
            bill_region = pref.getString("region", "");
            bill_street = pref.getString("add_line1", "");
            bill_postcode = pref.getString("postcode", "");
            bill_city = pref.getString("city", "");
            bill_fname = pref.getString("firstname", "");
            bill_lname = pref.getString("lastname", "");
            bill_email = pref.getString("login_email", "");
            bill_region_id = pref.getString("region_id", "");
            bill_region_code = pref.getString("region_code", "");
            st_cust_name = ship_fname + " " + ship_lname;
            bill_telephone = pref.getString("telephone", "");
            ship_fname=bill_fname;
            ship_lname=bill_lname;
            ship_email=bill_email;
        }






        String carrier_code = pref.getString("arr_carrier_code", "");
        String method_code = pref.getString("arr_method_code", "");
//        String ship_fname = pref.getString("firstname", "");
//        String ship_lname = pref.getString("lastname", "");
//        String ship_email = pref.getString("login_email", "");



        if (bill_country_id.equalsIgnoreCase(""))
            bill_country_id = "IN";

        if (bill_region_id.equals(""))
            bill_region_id = "0";


        order_id_method_data = "{" +
                "\"paymentMethod\":{" +
                "\"method\": \"" + selItemCode + "\"" +
                "}," +
                "\"billing_address\":" +
                "{" +
                "\"email\": \"" + ship_email + "\"," +
                "\"region\": \"" + bill_region + "\"," +
                "\"region_id\": \"" + bill_region_id + "\", " +
                "\"region_code\": \"" + bill_region_code + "\", " +
                "\"country_id\": \"" + bill_country_id + "\", " +
                "\"street\": [\"" + bill_street + "\"], " +
                "\"postcode\": \"" + bill_postcode + "\", " +
                "\"city\": \"" + bill_city + "\", " +
                "\"telephone\": \"" + bill_telephone + "\", " +
                "\"company\": \"" + "" + "\", " +
                "\"firstname\": \"" + ship_fname + "\"," +
                "\"lastname\": \"" + ship_lname + "\" " +
                "" +
                "}" +
                "}";


        //Log.d("insert_value",order_id_method_data);

        order_id_method_url = Global_Settings.api_url + "rest/V1/carts/mine/payment-information";

        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        //,new JSONObject(order_id_method_data)

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = null;
        try {
            jsObjRequest = new StringRequest(Method.POST, order_id_method_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();


                            String cust_id = pref.getString("login_customer_id","");

                            //Log.d("OrderHashresponse", cust_id+"-"+response.toString());

                            // CommonFun.alertError(Payment_Method_Activity.this,response.toString());
                            if (response != null) {

                                String st_orderhash = response.replaceAll("\"", "");
                                    st_orderhash=st_orderhash.replaceAll(" ","");

                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("orderhash", st_orderhash);

                                if(is_payu==false)
                                   editor.putString("paymentdue", "done");
                                else
                                   editor.putString("paymentdue","due");

                                editor.commit();



//
//     /** @param is_payu (if yes, then redirect to payment gateway
//     *                else procceed to call sales checkout api)
//      */

                        if(selItemCode.contains("payu")) {

//                            if (is_payu == false) {
//
//                                //  goToOrderDetails(st_orderhash);
//                                after_order_hash_url = after_order_hash_url + st_orderhash.trim();
//                                //Log.d("OrderHashresponse", "callagain");
//                                callToProcessOrderHash(st_orderhash.trim());
//
//                            } else {
                                ////Log.d("callsalefromhashfalse",st_orderhash);
                                pref = CommonFun.getPreferences(getApplicationContext());
                                SharedPreferences.Editor editor_pay = pref.edit();
                                editor_pay.putString("payment", st_orderhash);
                                editor_pay.putString("paydiscount", jsonObject_total_discount);
                                editor_pay.putString("paygrandtotal", jsonObject_grand_total);
                                editor_pay.commit();
                                Intent intent = new Intent(Payment_Method_Activity.this, PayUMainActivity.class);
                                intent.putExtra("grand_total", jsonObject_grand_total);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                /**
                                 * redirect to payment method activity
                                 */
                                startActivity(intent);
                                CommonFun.finishscreen(Payment_Method_Activity.this);




                            //getUserDetails(user_detail_url,st_orderhash,is_payu);
                        }
                        else if(selItemCode.contains("paytm")) {

//                            if (is_payu == false) {
//
//                                //  goToOrderDetails(st_orderhash);
//                                after_order_hash_url = after_order_hash_url + st_orderhash.trim();
//                                //Log.d("OrderHashresponse", "callagain");
//                                callToProcessOrderHash(st_orderhash.trim());
//
//                            } else {
//                                //Log.d("paytm",st_orderhash);
                                pref = CommonFun.getPreferences(getApplicationContext());
                                SharedPreferences.Editor editor_pay = pref.edit();
                                editor_pay.putString("payment", st_orderhash);
                                editor_pay.putString("paydiscount", jsonObject_total_discount);
                                editor_pay.putString("paygrandtotal", jsonObject_grand_total);
                                editor_pay.commit();

                                Intent intent = new Intent(Payment_Method_Activity.this, com.galwaykart.Payment.paytm_integrate.MainActivity.class);
                                intent.putExtra("grand_total", jsonObject_grand_total);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                /**
                                 * redirect to payment method activity
                                 */
                                startActivity(intent);
                                CommonFun.finishscreen(Payment_Method_Activity.this);
//                            }
                            //getUserDetails(user_detail_url,st_orderhash,is_payu);
                        }
                        else{
                            after_order_hash_url = after_order_hash_url + st_orderhash.trim();
                            //Log.d("OrderHashresponse", "callagain");
                            callToProcessOrderHash(st_orderhash.trim());
                        }

                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    // retry button
                    // error.printStackTrace();
//                    CommonFun.showVolleyException(error, Payment_Method_Activity.this);
                    NetworkResponse response = error.networkResponse;
                    String errorMsg = "";
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        //Log.d("log_error", errorString);

                        try {
                            JSONObject object = new JSONObject(errorString);
                            String st_msg = object.getString("message");
//                                String st_code = object.getString("code");
                            alertError(st_msg);
//                                //Log.d("st_code",st_code);
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            CommonFun.showVolleyException(error, Payment_Method_Activity.this);
                        }


                    } else
                        CommonFun.showVolleyException(error, Payment_Method_Activity.this);
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
                    return order_id_method_data == null ? null : order_id_method_data.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);


        queue.add(jsObjRequest);


    }

    private void callToProcessOrderHash(final String orderhash)
    {

        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        final RequestQueue requestQueue= Volley.newRequestQueue(Payment_Method_Activity.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, after_order_hash_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        //Log.d("orderhash",response.toString());


//                        try {
//                            //JSONObject jsonObject=new JSONObject(String.valueOf(response));
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                            startActivity(intent);
//
//
//
//                        }

                        goToOrderDetails(orderhash);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(pDialog.isShowing())
                    pDialog.dismiss();

                //CommonFun.showVolleyException(error,Payment_Method_Activity.this);
                goToOrderDetails(orderhash);

                  // cancelOrderEcom();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,
                1,
                1
        ));
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);

    }


    private void goToOrderDetails(String st_orderhash) {

        pref = CommonFun.getPreferences(getApplicationContext());
        st_orderhash=st_orderhash.replaceAll(" ","");
        SharedPreferences.Editor editor_new = pref.edit();
        editor_new.putString("orderhash", st_orderhash);
        editor_new.putString("orderpono","");
        editor_new.putString("paymentdue", "done");
        editor_new.commit();
        Intent intent = new Intent(Payment_Method_Activity.this, OrderDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(Payment_Method_Activity.this);
    }

    String salesOrderInsert = "";
    /**
     * Call Sales Checkout API
     * @param orderid
     */
//    private void callSalesCheckout(String orderid){
//
//        final String sales_checkout_url=Global_Settings.sales_checkout_url;
//        //Log.d("salescall",sales_checkout_url);
//        String itemArray="";
//        DatabaseHandler dbh = new DatabaseHandler(Payment_Method_Activity.this);
//
//        //int total_cart_count = dbh.getCartItemCount();
//
//        Double total_ip=0.0;
//        //"PO_Insert_xml_list":[{ "productcode": "GSG01125","ip": "0.770", "qty": "2", "mrp": "212", "dp": "208" }],
//
//        String PO_Insert_xml_list="[";
//
//        try {
//            if (dbh.getCartItemCount() > 0) {
//                List<CartItem> contacts = dbh.getallCartItem();
//
//
//                for (int i = 0; i < arr_final_sku.length; i++) {
//                    String p_sku = arr_final_sku[i];
//                    String p_ip = arr_ip[i];
//                    String p_mrp = arr_mrp[i];
//                    String p_dp = arr_price[i];
//                    String p_qty = arr_qty[i];
//
//
//                    //Log.d("p_ip", p_ip);
//
//                    if (p_ip.equalsIgnoreCase("null") || p_ip.equalsIgnoreCase("")) {
//
//                        p_ip = "0.0";
//                    }
//
//                    String p_discount = String.valueOf(Double.parseDouble(arr_mrp[i]) - Double.parseDouble(arr_price[i]));
//
///**
// * Create item array of all the cart items
// * for pass json data in sales api
// */
//                    if (itemArray.equals("")) {
//
//                        itemArray = "{ \"productcode\": \"" + p_sku + "\"" +
//                                ",\"ip\": \"" + p_ip + "\"," +
//                                "\"qty\": \"" + p_qty + "\"," +
//                                "\"disc\": \"" + p_discount + "\"," +
//                                "\"dp\": \"" + p_dp + "\"," +
//                                "\"price\": \"" + p_mrp + "\" }";
//
//
//                    } else {
//
//                        itemArray = itemArray + "," +
//                                "{ \"productcode\": \"" + p_sku + "\"" +
//                                ",\"ip\": \"" + p_ip + "\"," +
//                                "\"qty\": \"" + p_qty + "\"," +
//                                "\"disc\": \"" + p_discount + "\"," +
//                                "\"dp\": \"" + p_dp + "\"," +
//                                "\"price\": \"" + p_mrp + "\" }";
//
//                    }
//
//
//                    Double payment_ip = Double.parseDouble(p_ip);
//
//                    total_ip = total_ip + payment_ip;
//                    //Log.d("total_ip", total_ip + "");
//
//
//                }
//            }
//        }catch (NumberFormatException ex){
//            cancelOrderEcom();
//        }catch (Exception ex){
//            cancelOrderEcom();
//        }
//
//        ////Log.d("region99999",pref.getString("region",""));
//
//        if(!itemArray.equals("") && itemArray!=null) {
//            try {
//
////                 login_tel = pref.getString("login_telephone", "");
//                String st_total_ip = String.valueOf(Math.round(total_ip * 100.0) / 100.0); //get total ip
////                del_address = pref.getString("add_line1", "");
////                 st_city = pref.getString("city", "");
////                 st_state = pref.getString("region", "");
////                 st_pincode = pref.getString("postcode", "");
//
//
//                /**
//                 * Get user zone and id
//                 */
//                String login_zone = pref.getString("log_user_zone", "");
//                String login_id = pref.getString("log_user_id", "");
//
////                 ship_fname = pref.getString("firstname", "");
////                 ship_lname = pref.getString("lastname", "");
////                 st_cust_name = ship_fname + " " + ship_lname;
//
//
//                PO_Insert_xml_list = PO_Insert_xml_list + itemArray + "],";
//
//                String base_shipping_amount = pref.getString("base_shipping_amount", "");
//                String ac_base_grand_total = String.valueOf((Float.parseFloat(jsonObject_grand_total) - Float.parseFloat(base_shipping_amount)));
//
//                orderid = orderid.replaceAll(" ", "");
//
//                ////Log.d("orderid", orderid);
//                order_id_gen = orderid;
//
//                String sales_user_zone = pref.getString("st_dist_id", "").toLowerCase();
//                String magento_user_zone = pref.getString("log_user_zone", "").toLowerCase();
//
//                String st_selected_address=pref.getString("st_selected_address","");
//                if(st_selected_address.equalsIgnoreCase("Franchisee")) {
//                    st_state = pref.getString("region", "");
//                    del_address = pref.getString("add_line1", "");
//                    st_pincode = pref.getString("postcode", "");
//                    st_city = pref.getString("city", "");
//                    ship_fname = pref.getString("firstname", "");
//                    ship_lname = pref.getString("lastname", "");
//                    ship_email = pref.getString("login_email", "");
//                    st_cust_name = ship_fname + " " + ship_lname;
//                    login_tel = pref.getString("telephone", "");
//
//                }
//                else if(st_selected_address.equalsIgnoreCase("Home")) {
//                    st_state = pref.getString("new_region", "");
//                    del_address = pref.getString("new_add_line1", "");
//                    st_pincode = pref.getString("new_postcode", "");
//                    st_city = pref.getString("new_city", "");
//                    ship_fname = pref.getString("new_firstname", "");
//                    ship_lname = pref.getString("new_lastname", "");
//                    ship_email = pref.getString("login_email", "");
//                    st_cust_name = ship_fname + " " + ship_lname;
//                    login_tel = pref.getString("new_telephone", "");
//                    login_zone="";
//                }
///**}
// * if user zone is rkt
// * then franchisee_code=snp
// *
// */
//                salesOrderInsert = "{" +
//                        "\"ecom_po_no\":\"" + orderid.trim() + "\"," +
//                        "\"Id\":\"" + login_id + "\"," +
//                        "\"Name\":\"" + st_cust_name + "\"," +
//                        "\"address\":\"" + del_address + "\"," +
//                        "\"city\":\"" + st_city + "\"," +                            //city,state,pincode added by puneet sir
//                        "\"state\":\"" + st_state + "\"," +
//                        "\"email\":\"" + ship_email + "\"," +
//                        "\"pincode\":\"" + st_pincode + "\"," +
//                        "\"mobielno\":\"" + login_tel + "\"," +
//                        "\"franchisee_code\":\"" + login_zone + "\"," +
//                        "\"PO_Insert_xml_list\":" + PO_Insert_xml_list +
//                        "\"total_discount\":\"" + jsonObject_total_discount + "\"," +
//                        "\"total_bill_amt\":\"" + ac_base_grand_total + "\"," +
//                        "\"total_ip\":\"" + st_total_ip + "\"," +
//                        "\"total_qty\":\"" + dbh.getCartItemCount() + "\"," +
//                        "\"itype\":\"247\"," +
//                        "\"shippingcharge\":\"" + base_shipping_amount + "\"," +
//                        "\"payment_mode\":\"" + (is_ewallet_selected == true ? 3 : 0) + "\"," +
//                        "\"req_frm\":\"7\"" +
//                        "}";
//
//
////        salesOrderInsert = "{" +
////                "\"ecom_po_no\":\"" + orderid.trim() + "\"," +
////                "\"Id\":\"" + login_id + "\"," +
////                "\"Name\":\"" + st_cust_name + "\"," +
////                "\"address\":\"" + del_address + "\"," +
////                "\"mobielno\":\"" + login_tel + "\"," +
////                "\"franchisee_code\":\"" + login_zone + "\"," +
////                "\"PO_Insert_xml_list\":" + PO_Insert_xml_list +
////                "\"total_discount\":\"" + jsonObject_total_discount + "\"," +
////                "\"total_bill_amt\":\"" + ac_base_grand_total + "\"," +
////                "\"total_ip\":\"" + st_total_ip + "\"," +
////                "\"total_qty\":\"" + dbh.getCartItemCount() + "\"," +
////                "\"itype\":\"247\"," +
////                "\"shippingcharge\":\"" + base_shipping_amount + "\"," +
////                "\"payment_mode\":\""+ (is_ewallet_selected==true?3:0) +"\"," +
////                "\"req_frm\":\"7\"" +
////                "}";
//
////        "\"franchisee_code\":\"" + ((sales_user_zone.equalsIgnoreCase("rkt")
////                &&  magento_user_zone.equalsIgnoreCase("rkt"))?"snp":login_zone  ) + "\"," +
//
//
////        if (sales_user_zone.equalsIgnoreCase("rkt") &&
////                magento_user_zone.equalsIgnoreCase("rkt")) {
////
////            salesOrderInsert = "{" +
////                    "\"ecom_po_no\":\"" + orderid.trim() + "\"," +
////                    "\"Id\":\"" + login_id + "\"," +
////                    "\"Name\":\"" + st_cust_name + "\"," +
////                    "\"address\":\"" + del_address + "\"," +
////                    "\"mobielno\":\"" + login_tel + "\"," +
////                    "\"franchisee_code\":\"" + "snp" + "\"," +
////                    "\"PO_Insert_xml_list\":" + PO_Insert_xml_list +
////                    "\"total_discount\":\"" + jsonObject_total_discount + "\"," +
////                    "\"total_bill_amt\":\"" + ac_base_grand_total + "\"," +
////                    "\"total_ip\":\"" + st_total_ip + "\"," +
////                    "\"total_qty\":\"" + dbh.getCartItemCount() + "\"," +
////                    "\"itype\":\"247\"," +
////                    "\"shippingcharge\":\"" + base_shipping_amount + "\"," +
////                    "\"payment_mode\":\""+ (is_ewallet_selected==true?3:0) +"\"," +
////                    "\"req_frm\":\"7\"" +
////                    "}";
////        }
////        else
////        {
////            salesOrderInsert = "{" +
////                    "\"ecom_po_no\":\"" + orderid.trim() + "\"," +
////                    "\"Id\":\"" + login_id + "\"," +
////                    "\"Name\":\"" + st_cust_name + "\"," +
////                    "\"address\":\"" + del_address + "\"," +
////                    "\"mobielno\":\"" + login_tel + "\"," +
////                    "\"franchisee_code\":\"" + login_zone + "\"," +
////                    "\"PO_Insert_xml_list\":" + PO_Insert_xml_list +
////                    "\"total_discount\":\"" + jsonObject_total_discount + "\"," +
////                    "\"total_bill_amt\":\"" + ac_base_grand_total + "\"," +
////                    "\"total_ip\":\"" + st_total_ip + "\"," +
////                    "\"total_qty\":\"" + dbh.getCartItemCount() + "\"," +
////                    "\"itype\":\"247\"," +
////                    "\"shippingcharge\":\"" + base_shipping_amount + "\"," +
////                    "\"payment_mode\":\""+ (is_ewallet_selected==true?3:0) +"\"," +
////                    "\"req_frm\":\"7\"" +
////                    "}";
////        }
//
//                String cust_id = pref.getString("login_customer_id", "");
//                //Log.d("SalesInsertvalues", cust_id + "-" + salesOrderInsert);
//
//            } catch (NumberFormatException ex) {
//                cancelOrderEcom();
//            } catch (Exception ex) {
//                cancelOrderEcom();
//            }
//
//
//
//        pDialog = new TransparentProgressDialog(Payment_Method_Activity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        try {
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//            final String finalSalesOrderInsert = salesOrderInsert;
//            final String finalOrderid = orderid;
//            StringRequest stringRequest = new StringRequest(Method.POST, sales_checkout_url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if(pDialog.isShowing())
//                                pDialog.dismiss();
//                            ////Log.d("SalesVOLLEY", response);
//                            try {
//
//                                //CommonFun.alertError(Payment_Method_Activity.this,response);
//
//                                /**
//                                 * Get message and pono from sales api
//                                 * and redirect to orderdetails page
//                                 *
//                                 */
//                                JSONObject jsonObject=new JSONObject(response);
//                                String st_msg=jsonObject.getString("Message");
//                                String st_pono=jsonObject.getString("PO_no");
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("orderhash", order_id_gen);
//                                editor.putString("orderpono",st_pono);
//                                editor.putString("paymentdue", "done");
//                                editor.commit();
//
//
//
//if(!st_pono.equals("0")) {
//    String cust_id = pref.getString("login_customer_id","");
//    //Log.d("sales_success_pono",cust_id+"-"+st_pono);
//    Intent intent = new Intent(Payment_Method_Activity.this, OrderDetails.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//    startActivity(intent);
//    CommonFun.finishscreen(Payment_Method_Activity.this);
//}
//else
//{
////    final AlertDialog.Builder b;
////    try
////    {
////        b = new AlertDialog.Builder(Payment_Method_Activity.this);
////        b.setTitle("Alert");
////        b.setCancelable(false);
////        b.setMessage("SomeThing Wrong!!! Try Again");
////        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
////        {
////            @Override
////            public void onClick(DialogInterface dialog, int whichButton)
////            {
////                callSalesCheckout(orderid);
////            }
////        });
////        b.setNegativeButton("Go To Home", new DialogInterface.OnClickListener()
////        {
////            @Override
////            public void onClick(DialogInterface dialog, int whichButton)
////            {
////                Intent intent=new Intent(Payment_Method_Activity.this, HomePageActivity.class);
////                startActivity(intent);
////                CommonFun.finishscreen(Payment_Method_Activity.this);
////            }
////        });
////        b.create().show();
////    }
////    catch(Exception ex)
////    {
////    }
//
///**
// * if any error occurs
// * cancel order from magento
// */
//    String cust_id = pref.getString("login_customer_id","");
//    //Log.d("sale_faile_orderCancel",cust_id+"-"+response);
//    cancelOrderEcom();
//
//}
//
//
//                            } catch (Exception e) {
//                                //e.printStackTrace();
//                                //CommonFun.alertError(Payment_Method_Activity.this,e.toString());
//                              //  Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                               // startActivity(intent);
//                                    //cancelOrderEcom();
//                              //  cancelOrderEcom();
//
//                                if(sales_checkout_retry<3)
//                                {
//                                    sales_checkout_retry++;
//                                    callSalesCheckout(finalOrderid);
//                                }
//                                else
//                                {
//                                    String cust_id = pref.getString("login_customer_id","");
//                                    //Log.d("orderCancel",cust_id+"-retry");
//                                    cancelOrderEcom();
//                                }
//
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                   // //Log.d("VOLLEY", error.toString());
//                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
//                    if(pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    //CommonFun.alertError(Payment_Method_Activity.this,error.toString());
//                //   CommonFun.showVolleyException(error,Payment_Method_Activity.this);
//                    //cancelOrderEcom();
//                    if(sales_checkout_retry<3)
//                    {
//                        sales_checkout_retry++;
//                        callSalesCheckout(finalOrderid);
//                    }
//                    else
//                    {
//                        String cust_id = pref.getString("login_customer_id","");
//                        //Log.d("orderCancel",cust_id+"-retry");
//                        cancelOrderEcom();
//                    }
//                }
//            }) {
//
//
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                protected String getParamsEncoding() {
//                    return "utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return finalSalesOrderInsert == null ? null : finalSalesOrderInsert.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalSalesOrderInsert, "utf-8");
//                        return null;
//                    }
//                }
//
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
//
//            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
//                    0,
//                    0);
//
//            stringRequest.setRetryPolicy(retryPolicy);
//            stringRequest.setShouldCache(false);
//            requestQueue.add(stringRequest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            cancelOrderEcom();
//           // //Log.d("error...","Error");
//        }
//        }
//        else
//        {
//
//            cancelOrderEcom();
//        }
//
//    }


    /**
     * get User details (ID and Zone).
     * @param url
     */
//    private void getUserDetails(String url, final String shipping_info_string, final String payment_method_url){
//
//
//        pDialog = new TransparentProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        final RequestQueue requestQueue= Volley.newRequestQueue(Payment_Method_Activity.this);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        //Log.d("getUserDetails",response.toString());
//
//
//                        try {
//                            JSONObject jsonObject=new JSONObject(String.valueOf(response));
//
//                            JSONObject jsonObject1=jsonObject.getJSONObject("details");
//
//                            String jsonObject_fcode=jsonObject1.getString("fcode");
//                            String jsonObject_distid=jsonObject1.getString("distributor_id");
//
//
//                            SharedPreferences.Editor editor=pref.edit();
//                            editor.putString("log_user_id",jsonObject_distid);
//                            editor.putString("log_user_zone",jsonObject_fcode);
//                            editor.commit();
//
//
//
//                            //Log.d("distid",jsonObject_distid);
//                            //Log.d("distzone",jsonObject_fcode);
//                            //getPaymentMethod(shipping_info_string);
//
//                            /**
//                             *  Fetch user zone from the sales
//                             */
//                            getDistributorDetails(jsonObject_distid,shipping_info_string);
////                            getPaymentMethod(shipping_info_string,true,"",false);
//
//                            //CommonFun.alertError(Payment_Method_Activity.this,jsonObject_fcode+" "+jsonObject_distid);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                            startActivity(intent);
//
//
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
//                CommonFun.showVolleyException(error,Payment_Method_Activity.this);
//                //CommonFun.alertError(Payment_Method_Activity.this,error.toString());
//            }
//        });
//
//        jsonObjectRequest.setShouldCache(false);
//        requestQueue.add(jsonObjectRequest);
//    }


//    /**
//     * Fetch ip of all the cart items
//     * @param order_hash
//     * @param is_payu
//     */
//    private void callCartItemsIP(final String order_hash, final Boolean is_payu){
//
//    //Log.d("ipcall",order_hash);
//
//    String all_cart_item_sku = "";
//    DatabaseHandler dbh = new DatabaseHandler(Payment_Method_Activity.this);
//
//    if (dbh.getCartItemCount() > 0) {
//        List<CartItem> contacts = dbh.getallCartItem();
//
//        product_sku=new String[dbh.getCartItemCount()];
//        product_qty=new String[dbh.getCartItemCount()];
//        product_dp=new String[dbh.getCartItemCount()];
//
//        total_cart_count=dbh.getCartItemCount();
//        int i_loop=0;
//
//        for (CartItem cn : contacts) {
//
//            if (all_cart_item_sku.equals("")) {
//                i_loop=0;
//                all_cart_item_sku = cn.getTAG_cart_sku();
//
//            }
//            else {
//                all_cart_item_sku = all_cart_item_sku + "," + cn.getTAG_cart_sku();
//
//            }
//            product_sku[i_loop]=cn.getTAG_cart_sku();
//            product_qty[i_loop]=cn.getTAG_cart_qty();
//            product_dp[i_loop]=cn.getTAG_cart_price();
//
//            i_loop++;
//        }
//    }
//
      //  String url=Global_Settings.api_custom_url+"ip.php?sku="+all_cart_item_sku;
//
//    //Log.d("ip url",url);
//
//        RequestQueue requestQueue=Volley.newRequestQueue(Payment_Method_Activity.this);
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Method.GET, url,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            JSONObject jsonObject=new JSONObject(String.valueOf(response));
//                            JSONArray jsonArray=jsonObject.getJSONArray("extradeatils");
//
//                            if(jsonArray.length()>0){
//
//                                product_ip=new String[jsonArray.length()];
//                                product_mrp=new String[jsonArray.length()];
//
//                                for(int i=0;i<jsonArray.length();i++){
//
//                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                                    product_ip[i]=jsonObject1.getString("ip");
//                                    product_mrp[i]=jsonObject1.getString("mrp");
//
//                                }
//                            }
//
//
//                            pref = CommonFun.getPreferences(getApplicationContext());
//                            SharedPreferences.Editor editor=pref.edit();
//                            editor.putString("ipdetails",response.toString());
//                            editor.commit();
//
//                            /**
//                             * Get order hash from the magento
//                             *
//                             */
//                            getOrderId(order_hash,is_payu);
//
//                            //callSalesCheckout(order_hash);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                            startActivity(intent);
//
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                CommonFun.showVolleyException(error,Payment_Method_Activity.this);
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//    jsonObjectRequest.setShouldCache(false);
//    requestQueue.add(jsonObjectRequest);
//
//}

    /**
     * fetch user zone from sales
//     * @param st_dist_id
//     * @param shipping_info_string
     */
    private void getDistributorDetails(String st_dist_id,final String shipping_info_string) {


        pref = CommonFun.getPreferences(getApplicationContext());

       // String st_dist_id=pref.getString("st_dist_id","");

        String st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_verify_guest?ID="+st_dist_id;
        ////Log.d("st_Get_Dist_details_URL",st_Get_Dist_details_URL);

        pDialog = new TransparentProgressDialog(Payment_Method_Activity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Dist_details_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        //Log.d("st_Get_Dist_",response.toString());

                        if(response!=null){
                            try {

                                dist_details = new JSONArray(response);
                                JSONObject dist_details_object =dist_details.getJSONObject(0);

                                String current_zone = dist_details_object.getString("current_zone");

                                String st_eCredit = dist_details_object.getString("e_Creditamt");
                                //Log.d("st_eCredit",st_eCredit);



                                if(!st_eCredit.equals(""))
                                    ewallet_amount= Float.parseFloat(st_eCredit);



                                pref = CommonFun.getPreferences(getApplicationContext());

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("st_dist_id",current_zone);
                                editor.commit();


                                /**
                                 * Get all available payment method from api
                                 */
                                getPaymentMethod(shipping_info_string,true,"",false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d("error",e.toString());


                                final AlertDialog.Builder b;
                                try
                                {
                                    b = new AlertDialog.Builder(Payment_Method_Activity.this);
                                    b.setTitle("Alert");
                                    b.setCancelable(false);
                                    b.setMessage("Something Wrong!!! Try Again\nor Id is not active or id is not valid");
                                    b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton)
                                        {
                                            b.create().dismiss();

                                            Intent intent=new Intent(Payment_Method_Activity.this,Payment_Method_Activity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);
                                            CommonFun.finishscreen(Payment_Method_Activity.this);


                                        }
                                    });

                                    b.create().show();
                                }
                                catch(Exception ex)
                                {
                                    Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
                                    startActivity(intent);

                                }



                            }
                        }
                    }




                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

//                CommonFun.alertError(RegistrationActivity.this,error.toString());
//                error.printStackTrace();
                CommonFun.showVolleyException(error,Payment_Method_Activity.this);

            }
        }){
            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

    /**
     * if any error occurs
     * cancel order from magento by pass order hash
     */
//    private void cancelOrderEcom(){
//
//        pref = CommonFun.getPreferences(getApplicationContext());
//        String st_Cancel_Order_URL=Global_Settings.api_url+"glaze/order_cancel.php?id="+pref.getString("orderhash","").trim();
//        //cancelOrder(st_Cancel_Order_URL);
//    }
//
//
//    private void cancelOrder(String st_Cancel_Order_URL) {
//
//        pDialog = new TransparentProgressDialog(Payment_Method_Activity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        try {
//            jsonObjRequest = new JsonObjectRequest(Request.Method.GET, st_Cancel_Order_URL,null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    if (response != null) {
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                            ////Log.d("jsonObject",jsonObject+"");
//
//                            String st_Order_Cancel_Status = jsonObject.getString("orderdetails");
//                            //Log.d("st_Order_Cancel_Status",st_Order_Cancel_Status);
//
//                            //CommonFun.alertError(OrderDetails.this,st_Order_Cancel_Status.toString());
//                            final AlertDialog.Builder b;
//                            try
//                            {
////                                b = new AlertDialog.Builder(OrderDetails.this);
////                                b.setTitle("Alert");
////                                b.setCancelable(false);
////                                b.setMessage(st_Order_Cancel_Status.toString());
////                                b.setPositiveButton("OK", new DialogInterface.OnClickListener()
////                                {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int whichButton)
////                                    {
////                                        b.create().dismiss();
////                                        CommonFun.finishscreen(OrderDetails.this);
////                                    }
////                                });
////                                b.create().show();
//
//
//                                Vibrator vibrator = (Vibrator) Payment_Method_Activity.this.getSystemService(OrderDetails.VIBRATOR_SERVICE);
//                                vibrator.vibrate(100);
//
//                                final Dialog dialog = new Dialog(Payment_Method_Activity.this);
//                                dialog.setContentView(R.layout.custom_alert_dialog_design);
//                                TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
//                                ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
//                                tv_dialog.setText("Oops!!! Something Wrong\nCannot place your order\nPlease Try Again");
//                                dialog.show();
//
//                                DatabaseHandler dbh=new DatabaseHandler(Payment_Method_Activity.this);
//                                dbh.deleteAllData();
//
//                                TimerTask timerTask=new TimerTask() {
//                                    @Override
//                                    public void run() {
//
//
//                                        Intent intent=new Intent(Payment_Method_Activity.this, OrderListActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                        startActivity(intent);
//                                        CommonFun.finishscreen(Payment_Method_Activity.this);
//
//
//                                    }};
//
//
//                                Timer timer=new Timer();
//                                timer.schedule(timerTask,4500);
//
//                            }
//                            catch(Exception ex)
//                            {
//                            }
//
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Intent intent=new Intent(Payment_Method_Activity.this, ExceptionError.class);
//                            startActivity(intent);
//                        }
//
//                    }
//
//
//                }
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    CommonFun.showVolleyException(error,Payment_Method_Activity.this);
//
////                    CommonFun.alertError(OrderDetails.this,error.toString());
////
////                    error.printStackTrace();
//                }
//
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//        jsonObjRequest.setShouldCache(false);
//        queue.add(jsonObjRequest);
//
//    }

    public void alertError(String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    goBack();
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

    }
