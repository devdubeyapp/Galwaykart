package com.galwaykart.address_book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.Checkout.ConfirmationActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Show Shipping charges of the order to the user
 * Created by sumitsaini on 9/23/2017.
 */


public class DeliveryTypeActivity extends BaseActivityWithoutCart {
    SharedPreferences pref;
    ListView list_delivery_type;
    String tokenData = "",
            delivery_type_URL = "";
    TransparentProgressDialog pDialog;
    String customer_id = "", telephone = "", postcode = "", city_name = "", region = "", default_billing = "",
            same_as_billing = "", login_email = "",
            firstname = "", lastname = "", company_name = "", country_id = "", region_code = "", region_id = "", add_line1 = "";
    String delivery_data_in;
    RequestQueue queue = null;
    JSONArray array = null;

    String carrier_code = "",method_code="",carrier_title="",method_title="",amount="",base_amount="",available="",error_message="",
            price_excl_tax="",price_incl_tax="";

    final String TAG_carrier_code= "carrier_code";
    final String TAG_method_code = "method_code";
    final String TAG_carrier_title= "carrier_title";
    final String TAG_method_title= "method_title";
    final String TAG_amount = "amount";
    final String TAG_base_amount = "base_amount";
    final String TAG_available= "available";
    final String TAG_error_message = "error_message";
    final String TAG_price_excl_tax = "price_excl_tax";
    final String TAG_price_incl_tax = "price_incl_tax";
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String,String>> itemList;
    String[] arr_method_code,arr_carrier_code,arr_amount;

    int total_arr_length;
    String check_pin_url="";


    String payment_method_url = "";
    String shipping_info_string="";








    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(DeliveryTypeActivity.this, CustomerAddressBook.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_type);

        initNavigationDrawer();


    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMap = new HashMap<String, String>();
        itemList=new ArrayList<HashMap<String, String>>();

        queue = Volley.newRequestQueue(this);

        pref = CommonFun.getPreferences(getApplicationContext());
        //initNavigationDrawer();
        tokenData = pref.getString("tokenData", "");
//        tokenData = "d5nl77giwcqm6qmgwuqhpmdbg93dfdb2";


        /**
         * Fetch all user details
         * to pass on the api
         */



        String st_selected_address=pref.getString("st_selected_address","");
        if(st_selected_address.equalsIgnoreCase("Franchisee")) {
            customer_id = pref.getString("customer_id", "");
            telephone = pref.getString("telephone", "");
            postcode = pref.getString("postcode", "");
            city_name = pref.getString("city", "");
            firstname = pref.getString("firstname", "");
            lastname = pref.getString("lastname", "");
            company_name = pref.getString("company", "");

            region_code = pref.getString("region_code", "");
            region = pref.getString("region", "");
            region_id = pref.getString("region_id", "");
            add_line1 = pref.getString("add_line1", "");
            country_id = pref.getString("country_id", "");
            default_billing = pref.getString("default_billing", "");
            login_email = pref.getString("login_email", "");
        }
        else if(st_selected_address.equalsIgnoreCase("Home")) {
//            region = pref.getString("new_region", "");
//            region_id = pref.getString("new_region_id", "");
//            region_code = pref.getString("new_region_code", "");
//            country_id = pref.getString("new_country_id", "");
//            add_line1 = pref.getString("new_add_line1", "");
//            postcode = pref.getString("new_postcode", "");
//            city_name = pref.getString("new_city", "");
//            firstname = pref.getString("new_firstname", "");
//            lastname = pref.getString("new_lastname", "");
//            login_email = pref.getString("login_email", "");
//            telephone = pref.getString("new_telephone", "");
            customer_id = pref.getString("customer_id", "");
            telephone = pref.getString("telephone", "");
            postcode = pref.getString("postcode", "");
            city_name = pref.getString("city", "");
            firstname = pref.getString("firstname", "");
            lastname = pref.getString("lastname", "");
            company_name = pref.getString("company", "");

            region_code = pref.getString("region_code", "");
            region = pref.getString("region", "");
            region_id = pref.getString("region_id", "");
            add_line1 = pref.getString("add_line1", "");
            country_id = pref.getString("country_id", "");
            default_billing = pref.getString("default_billing", "");
            login_email = pref.getString("login_email", "");
        }


        //postcode="1234";

        if (country_id.equalsIgnoreCase(""))
            country_id = "IN";

        if(customer_id.equals(""))
            customer_id="0";

        if(region_id.equals(""))
            region_id="0";

        list_delivery_type = findViewById(R.id.list_delivery_type);

        delivery_type_URL = Global_Settings.api_url+"rest/V1/m-carts/mine/estimate-shipping-methods";

        if (default_billing.equalsIgnoreCase("true"))
            same_as_billing = "1";
        else
            same_as_billing = "0";

        same_as_billing="1";

//
//        if (login_email.equalsIgnoreCase(""))
//            login_email = "enggsumi.05@gmail.com";


/**
 * create json string of the user address to pass it in the string
 */
        delivery_data_in =
                "{\"address\":" +
                        "{\"region\": \"" + region + "\"," +
                        "\"region_id\": \"" + region_id + "\"," +
                        "\"region_code\": \"" + region_code + "\"," +
                        "\"country_id\" : \"" + country_id + "\"," +
                        "\"street\" : [ \"" + add_line1 + "\" ]," +
                        "\"postcode\": \"" + postcode + "\"," +
                        "\"city\": \"" + city_name + "\"," +
                        "\"firstname\": \"" + firstname + "\"," +
                        "\"lastname\": \"" + lastname + "\"," +
                        "\"customer_id\": \"" + customer_id + "\"," +
                        "\"email\":\"" + login_email + "\"," +
                        "\"telephone\": \"" + telephone + "\"," +
                        "\"same_as_billing\" : \"" + same_as_billing + "\"}}";


        //Log.d("delivery_data_in", delivery_data_in);

        check_pin_url= Global_Settings.api_custom_url+"pincode_check.php?postcode="+postcode;
        //Log.d("check_pin_url",check_pin_url);
        callDeliveryTypeList();
      //  checkPinCode();
        //callDeliveryTypeList();
    }


    /**
     * Depreceted
     * Jan 15,2019
     * Ankesh kumar
     */
    private void checkPinCode() {

        pDialog = new TransparentProgressDialog(DeliveryTypeActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, check_pin_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //Log.d("responseDelivery", response);
                            //Log.d("response", response);
                            try {
                                array = new JSONArray(response);

                                JSONObject json_pincode=array.getJSONObject(0);
                                String check_servicable=json_pincode.getString("status");
                                if(!check_servicable.equals(""))
                                {
                                    if(check_servicable.equals("0"))
                                        callDeliveryTypeList();
                                    else
                                    {

                                        showMsg("आपके द्वारा चुने गए पिन कोड (एड्रेस) पर कोरियर सर्विस उपलब्ध नहीं है | इसलिए दूसरे पिन कोड या फ्रेंचाइजी का चयन करे.\nService is not available on this pincode.\nPlease change your address");


                                    }

                                }
                                else
                                {
                                    Toast.makeText(DeliveryTypeActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(DeliveryTypeActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    Toast.makeText(DeliveryTypeActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    onBackPressed();
                    //CommonFun.showVolleyException(error,DeliveryTypeActivity.this);
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



            };

//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    1000 * 60,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            //));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }
    }

    /**
     * Fetch shipping charges and amount from the api
     */
    private void callDeliveryTypeList() {

        //Log.d("tokendata",tokenData);

        pDialog = new TransparentProgressDialog(DeliveryTypeActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, delivery_type_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();
                    //Log.d("response", response);
                    //Log.d("response", response);
                    try {
                        array = new JSONArray(response);
                        int arr_length = array.length();

                        arr_method_code=new String[arr_length];
                         arr_carrier_code=new String[arr_length];
                        arr_amount=new String[arr_length];


                        total_arr_length=arr_length;

                        ////Log.d("totalklength", String.valueOf(arr_length));



                            for (int i = 0; i < arr_length; i++) {
                                JSONObject object = array.getJSONObject(i);

                                carrier_code = object.getString(TAG_carrier_code);
                                method_code = object.getString(TAG_method_code);
                                carrier_title = object.getString(TAG_carrier_title);
                                //method_title = object.getString(TAG_method_title);
                                method_title = "";
                                amount = object.getString(TAG_amount);
                                base_amount = object.getString(TAG_base_amount);
                                available = object.getString(TAG_available);
                                error_message = object.getString(TAG_error_message);
                                price_excl_tax = object.getString(TAG_price_excl_tax);
                                price_incl_tax = object.getString(TAG_price_incl_tax);

                                arr_method_code[i] = method_code;
                                arr_carrier_code[i] = carrier_code;
                                arr_amount[i] = base_amount;
                            }


//                        hashMap.put(TAG_carrier_code, carrier_code);
//                        hashMap.put(TAG_method_code, method_code);
                            hashMap.put(TAG_carrier_title, "Carrier: " + carrier_title);
                            hashMap.put(TAG_method_title, "" + method_title);
                            hashMap.put(TAG_amount, "Amount: " + amount);
                            hashMap.put(TAG_base_amount, "Base Amount: " + base_amount);
//                        hashMap.put(TAG_available, available);
//                        hashMap.put(TAG_error_message, error_message);
                            hashMap.put(TAG_price_excl_tax, "Ex. Tax: " + price_excl_tax);
                            hashMap.put(TAG_price_incl_tax, "Inc. Tax: " + price_incl_tax);


                            itemList.add(hashMap);


                            ListAdapter lstadapter = new SimpleAdapter(DeliveryTypeActivity.this, itemList, R.layout.activity_delivery_type_list,
                                    new String[]{TAG_carrier_code, TAG_method_code, TAG_carrier_title, TAG_method_title, TAG_amount, TAG_base_amount,
                                            TAG_available, TAG_error_message, TAG_price_excl_tax, TAG_price_incl_tax},

                                    new int[]{R.id.text_carrier_code,
                                            R.id.text_method_code,
                                            R.id.text_carrier_title,
                                            R.id.text_method_title,
                                            R.id.text_amount,
                                            R.id.text_base_amount,
                                            R.id.text_available, R.id.text_error_message,
                                            R.id.text_price_excl_tax, R.id.text_price_incl_tax

                                    }
                            );


                            ////Log.d("List Adapter Count", String.valueOf(lstadapter.getCount()));

                            if(total_arr_length>0) {
                                if (lstadapter.getCount() > 0) {

                                    /**
                                     * Fill listview with the api charges details
                                     */
                                    list_delivery_type.setAdapter(lstadapter);
                                }
                            }
                            else {

                                showMsg("Something Wrong!!! Try Again");
                            }



                    } catch (JSONException e) {
                        //e.printStackTrace();
                        //CommonFun.alertError(DeliveryTypeActivity.this,e.toString());
                        //Intent intent=new Intent(DeliveryTypeActivity.this, ExceptionError.class);
                        //startActivity(intent);

                        try {
                            JSONArray jsonObject= new JSONArray(response);

                            JSONObject json_status= jsonObject.getJSONObject(0);
                            String st_msg=json_status.getString("error_message");

                            //CommonFun.alertError(DeliveryTypeActivity.this,st_msg);
                            showMsg(st_msg);

                        } catch (JSONException e1) {
                            e1.printStackTrace();

                           showMsg("Something went wrong!!! try again");
                        }



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
                    CommonFun.showVolleyException(error, DeliveryTypeActivity.this);
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
                    return delivery_data_in == null ? null : delivery_data_in.getBytes(StandardCharsets.UTF_8);
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

    private void showMsg(String st_msg) {
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(DeliveryTypeActivity.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(st_msg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    onBackPressed();
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }
    }


    /**
     *  when user select the data
     *  save it on temp storage
     *  and redirect to confirmation page
     * @param v
     */
    public void selectItem(View v) {

       // Log.e("shipping_info_select", shipping_info_string);


        View parentRow = (View) v.getParent();
        final int position = list_delivery_type.getPositionForView(parentRow);

        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("arr_carrier_code",arr_carrier_code[position]);
        editor.putString("arr_method_code",arr_method_code[position]);
        editor.putString("arr_amount",arr_amount[position]);
        editor.commit();


        getPaymentMethod(shipping_info_string,true,"",false);


    }


private void getPaymentMethod(String shipping_info_string, final Boolean callFromCreate, final String selItemCode, final Boolean ispayu) {

        payment_method_url = Global_Settings.api_url + "rest/V1/carts/mine/shipping-information";

    shipping_info_string = "{" +
            "\"addressInformation\":{" +
            "\"shipping_address\":{" +
            "\"region\": \"" + region + "\"," +
            "\"region_id\": \"" + region_id + "\", " +
            "\"region_code\": \"" + region_code + "\", " +
            "\"country_id\": \"" + country_id + "\", " +
            "\"street\": [ \"" + add_line1 + "\" ], " +
            "\"postcode\": \"" + postcode + "\", " +
            "\"city\": \"" + city_name + "\", " +
            "\"firstname\": \"" + firstname + "\", " +
            "\"lastname\": \"" + lastname + "\", " +
            "\"email\": \"" + login_email + "\", " +
            "\"telephone\": \"" + telephone + "\" }," +
            "\"billing_address\":{ " +
            "\"region\": \"" + region + "\"," +
            "\"region_id\": \"" + region_id + "\", " +
            "\"region_code\": \"" + region_code + "\", " +
            "\"country_id\": \"" + country_id + "\", " +
            "\"street\": [ \"" + add_line1 + "\" ], " +
            "\"postcode\": \"" + postcode + "\", " +
            "\"city\": \"" + city_name + "\", " +
            "\"firstname\": \"" + firstname + "\", " +
            "\"lastname\": \"" + lastname + "\", " +
            "\"email\": \"" + login_email + "\", " +
            "\"telephone\": \"" + telephone + "\" }," +
            "\"shipping_carrier_code\": \"" + carrier_code + "\", " +
            "\"shipping_method code\": \"" + method_code + "\" " +
            "}" +
            "}";


    //Log.d("shipstring_delivery",shipping_info_string);






        itemList = new ArrayList<HashMap<String, String>>();

        //Log.d("payment_meth_ship_URL",payment_method_url);

        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.POST, payment_method_url, new JSONObject(shipping_info_string),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            //Log.d("response_pay_method", response.toString());

                            //CommonFun.alertError(Payment_Method_Activity.this,response.toString());
                            if (response != null) {

                                Intent intent=new Intent(DeliveryTypeActivity.this, ConfirmationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                CommonFun.finishscreen(DeliveryTypeActivity.this);

                            }


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    CommonFun.showVolleyException(error, DeliveryTypeActivity.this);

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

            if(pDialog.isShowing())
                pDialog.dismiss();
            e.printStackTrace();
        }


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);
    }





}
