package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.UpdateAddressActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Fetch default shipping address and show to the user
 * RKT user can add new shilpping address
 * all user can update shipping address
 * Created by ankesh on 9/18/2017.
 */

public class CustomerAddressBook extends BaseActivity {

    ListView list_address;
    String url_address="";

    ArrayList<HashMap<String,String>> itemList;
    String tokenData="",add_line1="",add_line2="";

    JSONArray addressArray = null;

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
    final String TAG_id= "id";
    final String TAG_show_select="select";

    SharedPreferences preferences;
    RelativeLayout rel_no_address;
    int length_of_street;
    HashMap<String, String> hashMap;
    Button btn_add_address;
    String st_add_line1="";
    String customer_id = "",telephone="",postcode="",city="",region="",default_billing="",default_shipping="",
            firstname="",lastname="",company="",street="",region_code="",region_id="",country_id = "",new_add_added="";
    TransparentProgressDialog pDialog;
    TextView tv_title_address;

    Button btn_add_new_address;
    SharedPreferences pref;
    JSONArray dist_details = null;
    Boolean isAddressLoad=false;

    Button btn_change_address;
    String city_name="",company_name="";
   // String user_detail_url="";

    String [] arr_add_line,arr_telephone,arr_postcode,arr_region,arr_edit_boolean,arr_selected_address,arr_city,
            arr_firstname,arr_lastname,arr_state,arr_customer_id,arr_company,arr_address_id,arr_region_id,arr_region_code,
    arr_default_ship,arr_default_bill;

            ;

    String login_group_id="";
    HashSet<String> arrayList_AddressId;
    public Bundle bundle;
    String is_show_select_button="true";
    String st_come_from_update="";
    int total_address_count=0;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(CustomerAddressBook.this, CartItemList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onResume() {
        super.onResume();



        //if(isAddressLoad==false)

        // getUserDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_shipping_address);



        bundle = getIntent().getExtras();

        if (bundle != null) {
            String st_come_from_update1 = bundle.getString("st_come_from_update");
            if(st_come_from_update1!=null && !st_come_from_update1.equals(""))
                st_come_from_update=st_come_from_update1;

        }


        if(st_come_from_update.equalsIgnoreCase("updateaddress"))
            is_show_select_button="false";

        Log.d("customer","customer_addressbook");
        preferences = CommonFun.getPreferences(getApplicationContext());

        pref = CommonFun.getPreferences(getApplicationContext());


        list_address = (ListView) findViewById(R.id.list_address);

        itemList=new ArrayList<HashMap<String, String>>();

        arrayList_AddressId=new HashSet<String>();

        SharedPreferences.Editor editor=pref.edit();
        editor.putString("st_dist_id","");
        editor.putString("log_user_zone","");
        editor.commit();

        SharedPreferences.Editor editor_p=preferences.edit();
        editor_p.putString("st_dist_id","");
        editor_p.putString("log_user_zone","");
        editor_p.commit();

        initNavigationDrawer();

        tokenData = preferences.getString("tokenData", "");
//        tokenData = "ituo1gwphqdjidohau02y4dbu6880rof";
        //  Log.d("tokenData123",tokenData);

        rel_no_address = (RelativeLayout) findViewById(R.id.rel_no_address);
        btn_add_address = (Button) findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(btn_add_addressOnClickListener);



        url_address = Global_Settings.api_url + "rest/V1/customers/me";
        // Log.d("url_address", url_address);

        tv_title_address = (TextView) findViewById(R.id.tv_title_address);

        String add_type = preferences.getString("addnew", "");
        if (add_type != null && !add_type.equals("")) {
            if (add_type.equalsIgnoreCase("billing"))
                tv_title_address.setText("Billing Address");
            else
                tv_title_address.setText("Shipping Address");
        } else
            tv_title_address.setText("Shipping Address");


        //callAddressBookList();

        btn_add_new_address = (Button) findViewById(R.id.btn_add_new_address);

        btn_add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("total_data_2", String.valueOf(total_address_count));
                Intent intent = new Intent(CustomerAddressBook.this,AddNewAddress.class);
                intent.putExtra("add_new","new");
                intent.putExtra("st_come_from_update",st_come_from_update);
                intent.putExtra("total_address_data",String.valueOf(total_address_count));
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(CustomerAddressBook.this);

            }
        });


        btn_change_address=(Button)findViewById(R.id.btn_change_address);
        btn_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requsetChangeAddress();
            }
        });


       // user_detail_url= Global_Settings.user_details_url+pref.getString("login_customer_id","");
        //Log.d("user_detail_url",user_detail_url);

        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
        login_group_id=pref.getString("login_group_id","");
        //if(login_group_id.equals("4"))


            callAddressBookList();



    }

    /**
     * Call to change address
     *
     */
    private void requsetChangeAddress(){

        Intent intent=new Intent(this, UpdateAddressActivity.class);
        intent.putExtra("addressupdate","checkout");
        intent.putExtra("st_come_from_update","");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }




    Button.OnClickListener btn_add_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("addnew","shipping");
            editor.commit();



            Intent intent = new Intent(CustomerAddressBook.this, AddNewAddress.class);
            intent.putExtra("st_come_from_update",st_come_from_update);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            CommonFun.finishscreen(CustomerAddressBook.this);

        }};


    @Override
    protected void onStop() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
        super.onStop();

    }


    /**
     *
     *
     * Get default address list of the user
     */
    private void  callAddressBookList(){


        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(CustomerAddressBook.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_address, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response987654", response.toString());
                        Log.d("response987654", response.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {

                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(String.valueOf(response));

                            addressArray = jsonObject.getJSONArray("addresses");
                            total_address_count=addressArray.length();


                            arrayList_AddressId.clear();
                                arr_add_line = new String[addressArray.length()];
                                arr_firstname = new String[addressArray.length()];
                                arr_lastname = new String[addressArray.length()];
                                arr_postcode = new String[addressArray.length()];
                                arr_region = new String[addressArray.length()];
                                arr_state = new String[addressArray.length()];
                                arr_telephone = new String[addressArray.length()];
                                arr_customer_id = new String[addressArray.length()];
                                arr_company = new String[addressArray.length()];
                                arr_edit_boolean = new String[addressArray.length()];
                                arr_selected_address = new String[addressArray.length()];
                                arr_city = new String[addressArray.length()];
                                arr_address_id=new String[addressArray.length()];
                                arr_region_id=new String[addressArray.length()];
                                arr_region_code=new String[addressArray.length()];
                                arr_default_ship=new String[addressArray.length()];
                                arr_default_bill=new String[addressArray.length()];


                                //default_shipping = "1";
                                //default_billing = "1";

                                for(int i=0;i<addressArray.length();i++) {

                                    JSONObject jsonObj=addressArray.getJSONObject(i);
                                    String address_id = jsonObj.getString(TAG_id);
                                    arr_address_id[i]=address_id;

                                    arrayList_AddressId.add(address_id);

                                    country_id = jsonObj.getString("country_id");

                                    customer_id = jsonObj.getString(TAG_customer_id);
                                    telephone = jsonObj.getString(TAG_telephone);
                                    postcode = jsonObj.getString(TAG_postcode);
                                    city = jsonObj.getString(TAG_city);
                                    firstname = jsonObj.getString(TAG_firstname);
                                    lastname = jsonObj.getString(TAG_lastname);
                                    //company = jsonObj.getString(TAG_company);

                                    if(jsonObj.has("default_shipping"))
                                        arr_default_ship[i]="1";
                                    else
                                        arr_default_ship[i]="0";

                                    if(jsonObj.has("default_billing"))
                                        arr_default_bill[i]="1";
                                    else
                                        arr_default_bill[i]="0";

                                    JSONObject object = jsonObj.getJSONObject("region");

                                    region_code = object.getString("region_code");
                                    region = object.getString("region");
                                    region_id = object.getString("region_id");


                                    JSONArray jsonArray_street=jsonObj.getJSONArray("street");
                                    arr_add_line[i]=jsonArray_street.getString(0);
                                    arr_telephone[i] = telephone;
                                    arr_postcode[i] = postcode;
                                    arr_firstname[i] = firstname;
                                    arr_lastname[i] = lastname;
                                    arr_region[i] = city;
                                    arr_region_code[i]=region_code;
                                    arr_region_id[i]=region_id;

                                    arr_state[i] = region;
                                    arr_customer_id[i] = customer_id;
                                    // arr_company[0] = company;
                                    arr_edit_boolean[i] = "true";
                                    arr_selected_address[i] = "";
                                    arr_city[i] = city;



                                }

                            SharedPreferences.Editor editor= preferences.edit();
                            editor.putStringSet("all_address_id",arrayList_AddressId);
                            editor.commit();


//                                for (int i = 0; i < addressArray.length(); i++) {
//
//                                    add_line1 = add_line1 + addressArray.getString(i);
//                                    arr_add_line[0] = add_line1;
//                                    //  Log.d("add_line1", add_line1);
//                                    //  Log.d("st_add_line1", st_add_line1);
//
//                                }

//
//                                if(login_group_id.equalsIgnoreCase("4")) {
//                                    SharedPreferences pref;
//                                    pref = getSharedPreferences("glazekartapp", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.putString("customer_id", customer_id);
//                                    editor.putString("telephone", telephone);
//                                    editor.putString("postcode", postcode);
//                                    editor.putString("city", city);
//                                    editor.putString("firstname", firstname);
//                                    editor.putString("lastname", lastname);
//                                    editor.putString("company", company);
//                                    editor.putString("region_code", region_code);
//                                    editor.putString("country_id", country_id);
//                                    editor.putString("region", region);
//                                    editor.putString("region_id", region_id);
//                                    editor.putString("add_line1", add_line1);
//                                    editor.putString("default_shipping", default_shipping);
//                                    editor.putString("default_billing", default_billing);
//
//                                    editor.putString("bill_customer_id", customer_id);
//                                    editor.putString("bill_telephone", telephone);
//                                    editor.putString("bill_postcode", postcode);
//                                    editor.putString("bill_city", city);
//                                    editor.putString("bill_firstname", firstname);
//                                    editor.putString("bill_lastname", lastname);
//                                    editor.putString("bill_company", company);
//                                    editor.putString("bill_region_code", region_id);
//
//                                    editor.putString("bill_region", region);
//                                    editor.putString("bill_region_id", region_id);
//                                    editor.putString("bill_add_line1", add_line1);
//                                    editor.putString("address_id",address_id);
//
//                                    editor.commit();
////                                }


//

                                    for (int i = 0; i < addressArray.length(); i++) {

                                        hashMap = new HashMap<String, String>();
                                        hashMap.put(TAG_customer_id, arr_customer_id[i]);
                                        hashMap.put(TAG_street, arr_add_line[i]);
                                        hashMap.put(TAG_telephone,  arr_telephone[i]);
                                        hashMap.put(TAG_postcode, arr_postcode[i]);
                                        hashMap.put(TAG_city, arr_city[i]);
                                        hashMap.put(TAG_firstname, arr_firstname[i]);
                                        hashMap.put(TAG_lastname,arr_lastname[i]);
                                        hashMap.put(TAG_region, arr_state[i]);
                                        hashMap.put(TAG_region_id, arr_region_id[i]);
                                        hashMap.put(TAG_region_code, arr_region_code[i]);
                                        hashMap.put(TAG_company, "");
                                        hashMap.put(TAG_edit, arr_edit_boolean[i]);
                                        hashMap.put(TAG_selected_address, arr_selected_address[i]);
                                        hashMap.put(TAG_id,arr_address_id[i]);
                                        hashMap.put(TAG_show_select,is_show_select_button);
                                        hashMap.put("st_come_from_update",st_come_from_update);
                                        hashMap.put("total_data", String.valueOf(total_address_count));
                                        hashMap.put("default_ship",arr_default_ship[i]);
                                        hashMap.put("default_bill",arr_default_bill[i]);

                                        Log.d("total_data", String.valueOf(total_address_count));

                                        itemList.add(hashMap);

                                    }

                                updateAddressList();

                          //  }

/**
 * Get user details from Galwaykart
 * zone and user id
 */
//                            if(login_group_id.equals("4"))
//                                getUserDetails();

//                            }
//                            else
//                            {
//                                CommonFun.alertError(CustomerAddressBook.this,"no address");
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            Intent intent=new Intent(CustomerAddressBook.this, ExceptionError.class);
                            startActivity(intent);

                            rel_no_address.setVisibility(View.VISIBLE);
                            list_address.setVisibility(View.GONE);
                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // Log.d("ERROR", "error => " + error.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();




                        try {
                           // CommonFun.alertError(CustomerAddressBook.this,"No address");


                            //  JSONObject JO = new JSONObject(error.toString());
                            //  CommonFun.alertError(LoginActivity.this, error.toString());

                            String body;
                            //get status code here
                           // String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            if(error.networkResponse.data!=null) {
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                    CommonFun.alertError(CustomerAddressBook.this,body.toString());
                                    // JSONObject jsonObject=new JSONObject(body.toString());
                                    // String message=jsonObject.getString("message");
                                    //CommonFun.alertError(CustomerAddressBook.this,message);

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e){

                            e.printStackTrace();
                        }
                        //CommonFun.showVolleyException(error,CustomerAddressBook.this);
                        //rel_no_address.setVisibility(View.VISIBLE);
                        //list_address.setVisibility(View.GONE);
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
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);

    }

    private void updateAddressList() {


        ListAdapter lstadapter = new CustomerAddressBookAdapter(CustomerAddressBook.this, itemList, R.layout.activity_default_shipping_address_list,
                new String[]{TAG_street, TAG_telephone, TAG_postcode,
                             TAG_city, TAG_firstname, TAG_company, TAG_edit,
                             TAG_lastname},
                new int[]{R.id.textStreet_name,
                        R.id.textTelephone_name,
                        R.id.textPincode_name,
                        R.id.textCity_name,
                        R.id.textCustomer_name,
                        R.id.textCompany_name,
                        R.id.bt_edit_add_icon,
                        R.id.textCustomer_name

                }
        );

        if (lstadapter.getCount() > 0) {
            list_address.invalidate();
            list_address.setAdapter(lstadapter);

            list_address.setVisibility(View.VISIBLE);
           // btn_add_new_address.setVisibility(View.GONE);
        }
        else
        {
            list_address.setVisibility(View.GONE);
            btn_add_new_address.setVisibility(View.VISIBLE);
        }

    }

//
//    private void getUserDetails(){
//
//
//        pDialog = new TransparentProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        RequestQueue requestQueue_1= Volley.newRequestQueue(CustomerAddressBook.this);
//
//
//        JsonObjectRequest jsonObjectRequest_1 = new JsonObjectRequest(Request.Method.GET, user_detail_url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        // Log.d("response_galwaykart",response.toString());
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
//                            //  Log.d("distid",jsonObject_distid);
//                            //  Log.d("distzone",jsonObject_fcode);
//                            //getPaymentMethod(shipping_info_string);
//
//                            /**
//                             * Call distributor details and zone
//                             */
////                            getDistributorDetails(jsonObject_distid);
//
//
//                            //CommonFun.alertError(CustomerAddressBook.this,jsonObject_fcode+" "+jsonObject_distid);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Intent intent=new Intent(CustomerAddressBook.this, ExceptionError.class);
//                            startActivity(intent);
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
//                CommonFun.alertError(CustomerAddressBook.this,error.toString());
//            }
//        });
//
//
//        jsonObjectRequest_1.setShouldCache(false);
////        if(requestQueue.getCache().get(url)!=null) {
////            requestQueue.getCache().clear();
//        requestQueue_1.getCache().remove(user_detail_url);
////            Log.d("response_galwaykart","cache");
////        }
//        requestQueue_1.add(jsonObjectRequest_1);
//    }

//    private void getUserDetails(String url){
//        st_get_kart_zone=url;
//        new userdataFromKart().execute();
//    }
//
//    private class userdataFromKart extends AsyncTask<String,String,String>{
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            pDialog = new TransparentProgressDialog(CustomerAddressBook.this);
////            pDialog.setCancelable(false);
////            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
////            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            ServiceHandler sh = new ServiceHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(st_get_kart_zone, ServiceHandler.GET);
//
//            Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//
//                Log.d("response_galwaykart",jsonStr.toString());
//                try {
//                    JSONObject jsonObject=new JSONObject(String.valueOf(jsonStr));
//
//                    JSONObject jsonObject1=jsonObject.getJSONObject("details");
//
//                    String jsonObject_fcode=jsonObject1.getString("fcode");
//                    String jsonObject_distid=jsonObject1.getString("distributor_id");
//
//
//                    SharedPreferences.Editor editor=pref.edit();
//                    editor.putString("log_user_id",jsonObject_distid);
//                    editor.putString("log_user_zone",jsonObject_fcode);
//                    editor.commit();
//
//
//
//                    Log.d("distid",jsonObject_distid);
//                    Log.d("distzone",jsonObject_fcode);
//                    //getPaymentMethod(shipping_info_string);
//                    //getDistributorDetails(jsonObject_distid);
//
//
//                    //CommonFun.alertError(CustomerAddressBook.this,jsonObject_fcode+" "+jsonObject_distid);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//
//                }
//
//            }
//            return null;
//        }
//
//
//    }


    /**
     * Get distributor id and zone from Glaze Sales api
     * to check rkt zone of the user
     * @param st_dist_id
     */
//    private void getDistributorDetails(String st_dist_id) {
//
//
//        pref = getSharedPreferences("glazekartapp",MODE_PRIVATE);
//
//        // String st_dist_id=pref.getString("st_dist_id","");
//
//        String st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_verify_guest?ID="+st_dist_id;
//        Log.d("st_Get_Dist_details_URL",st_Get_Dist_details_URL);
//
//        pDialog = new TransparentProgressDialog(CustomerAddressBook.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Dist_details_URL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        if(response!=null){
//                            try {
//
//                                dist_details = new JSONArray(String.valueOf(response));
//                                JSONObject dist_details_object =dist_details.getJSONObject(0);
//
//                                String current_zone = dist_details_object.getString("current_zone");
//
//
//                                pref = getSharedPreferences("glazekartapp",MODE_PRIVATE);
//
//                                Log.d("current_zone",current_zone);
//
//
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("st_dist_id",current_zone);
//                                editor.commit();
//
//                                /**
//                                 * check  zone of the user
//                                 * rkt or not
//                                 */
////                                  ValidateToAddAddress();
//
//
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Log.d("error",e.toString());
//
//
//
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("st_dist_id","");
//                                editor.commit();
//
//                                /**
//                                 * check  zone of the user
//                                 * rkt or not
//                                 */
////                                ValidateToAddAddress();
//
//
//
//                            }
//                        }
//                    }
//
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
////                CommonFun.alertError(RegistrationActivity.this,error.toString());
////                error.printStackTrace();
//
//                CommonFun.showVolleyException(error,CustomerAddressBook.this);
//
//            }
//        }){
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//
//        };
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        jsObjRequest.setShouldCache(false);
//        queue.getCache().clear();
//        queue.add(jsObjRequest);
//
//    }

    /**
     * check  zone of the user
     * rkt or not
     */
//    private void ValidateToAddAddress(){
//
//        String sales_user_zone=pref.getString("st_dist_id","").toLowerCase();
//        String magento_user_zone=pref.getString("log_user_zone","").toLowerCase();
//        /**
//         * if user is of rkt then show
//         * Add new address button
//         */
//
//        if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                magento_user_zone.equalsIgnoreCase("rkt")) {
//
//
//            //CommonFun.alertError(CustomerAddressBook.this,"Address same");
//            btn_add_new_address.setVisibility(View.VISIBLE);
//
//        }
//        else
//            {
//                /**
//                 * if user is of not rkt then show
//                 * hide new address button
//                 */
//            btn_add_new_address.setVisibility(View.GONE);
//        }
//        isAddressLoad=true;
//    }

}
