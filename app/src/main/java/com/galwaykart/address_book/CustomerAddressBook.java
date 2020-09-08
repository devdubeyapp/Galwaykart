package com.galwaykart.address_book;

import android.app.Activity;
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
import java.nio.charset.StandardCharsets;
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
    final String TAG_telephone_new= "newtelephone";
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
    RelativeLayout rel_no_address, rl_default_shipping;
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

    /**
     * Addded on Sep 8, 2020
     * Ankesh kumar
     */
    String[] arr_telephone2;


    String login_group_id="";
    HashSet<String> arrayList_AddressId;
    public Bundle bundle;
    String is_show_select_button="true";
    String st_come_from_update="";
    int total_address_count=0;

    String address_id="";
    String strFirstName="";
    String strLastName="";
    String strCustomerName="";
    String strStreetName ="";
    String strCityName= "";
    String strPincode="";
    String strTelephone="";
    String strTelephone2="";

    String is_default_shipping ="";



    TextView textStreet_name_default;
    TextView textTelephone_name_default;
    TextView textPincode_name_default;
    TextView textCity_name_default;
    TextView textCustomer_name_default;
    TextView textCompany_name_default;
    Button btselect,bt_edit_add_icon;

    TextView txt_others;
    RelativeLayout rl_other_address;



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

        textCity_name_default = findViewById(R.id.textCity_name_default);
        textStreet_name_default = findViewById(R.id.textStreet_name_default);
        textTelephone_name_default = findViewById(R.id.textTelephone_name_default);
        textPincode_name_default = findViewById(R.id.textPincode_name_default);
        textCustomer_name_default = findViewById(R.id.textCustomer_name_default);
        textCompany_name_default = findViewById(R.id.textCompany_name_default);

        btselect = findViewById(R.id.btselect);
        bt_edit_add_icon = findViewById(R.id.bt_edit_add_icon);

        preferences = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor23= preferences.edit();
        editor23.putString("default_address_id","");
        editor23.commit();


        txt_others = findViewById(R.id.txt_others);
        rl_other_address = findViewById(R.id.rl_other_address);

        txt_others.setVisibility(View.GONE);
        rl_other_address.setVisibility(View.GONE);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            String st_come_from_update1 = bundle.getString("st_come_from_update");
            if(st_come_from_update1!=null && !st_come_from_update1.equals(""))
                st_come_from_update=st_come_from_update1;

        }


        if(st_come_from_update.equalsIgnoreCase("updateaddress"))
        {
            is_show_select_button="false";
            btselect.setVisibility(View.GONE);

        }



        btselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref;
                pref = CommonFun.getPreferences(CustomerAddressBook.this);

                //String st_selected_address  = itemList.get(position).get(TAG_selected_address).toString();

                Log.e("customer_id", customer_id);
                Log.e("telephone", strTelephone);
                Log.e("postcode", strPincode);
                Log.e("city", strCityName);
                Log.e("region", region);
                Log.e("default_billing", default_billing);
                Log.e("default_shipping", default_shipping);
                Log.e("firstname", strFirstName);
                Log.e("lastname", strLastName);
                Log.e("company", company);
                Log.e("street", strStreetName);
                Log.e("region_code", region_code);
                Log.e("country_id", country_id);
                Log.e("new_add_added", new_add_added);

                SharedPreferences.Editor editor= pref.edit();
                editor.putString("st_selected_address","Franchisee");
                editor.putString("telephone", strTelephone);
                editor.putString("newtelephone", strTelephone2);

                editor.putString("postcode", strPincode);
                editor.putString("city", strCityName);

                editor.putString("region_code", region_code);
                editor.putString("region", region);
                editor.putString("region_id", region_id);

                editor.putString("add_line1", strStreetName);
                editor.putString("country_id","IN");
                editor.putString("default_billing","true");
                editor.putString("firstname", strFirstName); //firstname
                editor.putString("lastname", strLastName); //lastname
                editor.commit();

                Intent intent = new Intent(CustomerAddressBook.this, DeliveryTypeActivity.class);
                startActivity(intent);
                CommonFun.finishscreen(CustomerAddressBook.this);
            }
        });

        bt_edit_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("customer_id", customer_id);
                Log.e("telephone", strTelephone);
                Log.e("postcode", strPincode);
                Log.e("city", strCityName);
                Log.e("region", region);
                Log.e("default_billing", default_billing);
                Log.e("default_shipping", default_shipping);
                Log.e("firstname", strFirstName);
                Log.e("lastname", strLastName);
                Log.e("company", company);
                Log.e("street", strStreetName);
                Log.e("region_code", region_code);
                Log.e("country_id", country_id);
                Log.e("new_add_added", new_add_added);

                Intent intent = new Intent(CustomerAddressBook.this, AddNewAddress.class);
                intent.putExtra("add_new","no");
                intent.putExtra("is_default_ship_edit","yes");
                intent.putExtra("selecteddata","done");
                intent.putExtra("region", region);
                intent.putExtra("address_id", address_id);
                intent.putExtra("first_name", strFirstName);
                intent.putExtra("last_name", strLastName);
                intent.putExtra("phone_no", strTelephone);
                intent.putExtra("newtelephone",strTelephone2);

                intent.putExtra("zip", strPincode);
                intent.putExtra("city", strCityName);
                intent.putExtra("street_address", strStreetName);
                intent.putExtra("st_come_from_update", st_come_from_update);
                intent.putExtra("total_address_data", String.valueOf(total_address_count));
                intent.putExtra("default_ship", "1");
                intent.putExtra("default_bill", "1");
                intent.putExtra("is_default_shipping",is_default_shipping);



                intent.putExtra("","");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(CustomerAddressBook.this);
            }
        });


        //Log.d("customer","customer_addressbook");
        preferences = CommonFun.getPreferences(getApplicationContext());
        pref = CommonFun.getPreferences(getApplicationContext());
        list_address = findViewById(R.id.list_address);
        itemList=new ArrayList<HashMap<String, String>>();
        arrayList_AddressId=new HashSet<String>();

        SharedPreferences.Editor editor2=pref.edit();
        editor2.putString("st_dist_id","");
        editor2.putString("log_user_zone","");
        editor2.commit();

        SharedPreferences.Editor editor_p=preferences.edit();
        editor_p.putString("st_dist_id","");
        editor_p.putString("log_user_zone","");
        editor_p.commit();

        initNavigationDrawer();

        tokenData = preferences.getString("tokenData", "");
        rel_no_address = findViewById(R.id.rel_no_address);
        rl_default_shipping = findViewById(R.id.rl_default_shipping);
        rl_default_shipping.setVisibility(View.GONE);

        btn_add_address = findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(btn_add_addressOnClickListener);



        url_address = Global_Settings.api_url + "rest/V1/customers/me";
        tv_title_address = findViewById(R.id.tv_title_address);

        String add_type = preferences.getString("addnew", "");
        if (add_type != null && !add_type.equals("")) {
            if (add_type.equalsIgnoreCase("billing"))
                tv_title_address.setText("Billing Address");
            else
                tv_title_address.setText("Shipping Address");
        } else
            tv_title_address.setText("Shipping Address");


        //callAddressBookList();

        btn_add_new_address = findViewById(R.id.btn_add_new_address);

        btn_add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Log.d("total_data_2", String.valueOf(total_address_count));
                Intent intent = new Intent(CustomerAddressBook.this,AddNewAddress.class);
                intent.putExtra("add_new","new");
                intent.putExtra("st_come_from_update",st_come_from_update);
                intent.putExtra("total_address_data",String.valueOf(total_address_count));
                intent.putExtra("is_default_ship_edit","no");
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(CustomerAddressBook.this);

            }
        });


        btn_change_address= findViewById(R.id.btn_change_address);
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
                        Log.e("response_address", response.toString());
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
                            arr_telephone2= new String[addressArray.length()];
                            arr_customer_id = new String[addressArray.length()];
                            arr_company = new String[addressArray.length()];
                            arr_edit_boolean = new String[addressArray.length()];
                            arr_selected_address = new String[addressArray.length()];
                            arr_city = new String[addressArray.length()];
                            arr_address_id=new String[addressArray.length()];
                            arr_region_id=new String[addressArray.length()];
                            arr_region_code=new String[addressArray.length()];
                            arr_default_ship=new String[addressArray.length()]; //for default billing address
                            arr_default_bill=new String[addressArray.length()];




                            for(int i=0;i<addressArray.length();i++)
                            {
                                JSONObject jsonObj=addressArray.getJSONObject(i);
                                address_id = jsonObj.getString(TAG_id);
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
                                //arr_company[0] = company;
                                arr_edit_boolean[i] = "true";
                                arr_selected_address[i] = "";
                                arr_city[i] = city;

                                arr_telephone2[i]="";
                                if(jsonObj.has("custom_attributes")){

                                    JSONArray jsonArray_custom=jsonObj.getJSONArray("custom_attributes");

                                    for(int custom_at=0;custom_at<jsonArray_custom.length();custom_at++){
                                        JSONObject jsonObject1=jsonArray_custom.getJSONObject(custom_at);

                                        Log.d("phone_no_new","phone_no_new");
                                        if(jsonObject1.getString("attribute_code").equalsIgnoreCase("number_new"))
                                        arr_telephone2[i]=jsonObject1.getString("value");
                                        Log.d("phone_no_new", arr_telephone2[i]);
                                    }

                                }

                                if(jsonObj.has("default_shipping"))
                                {



                                    rl_default_shipping.setVisibility(View.VISIBLE);
                                    arr_default_ship[i]="1";

                                    is_default_shipping = jsonObj.getString("default_shipping");
                                    Log.e("is_default_shipping", is_default_shipping);

                                    if(is_default_shipping.equalsIgnoreCase("true"))
                                    {

                                        SharedPreferences.Editor editor= preferences.edit();
                                        editor.putString("default_address_id",address_id);
                                        editor.commit();

                                        strFirstName =jsonObj.getString(TAG_firstname);
                                        strLastName = jsonObj.getString(TAG_lastname);
                                        strCustomerName= strFirstName + " "+ strLastName;
                                        strStreetName = jsonArray_street.getString(0);
                                        strCityName= jsonObj.getString(TAG_city);
                                        strPincode= jsonObj.getString(TAG_postcode);
                                        strTelephone= jsonObj.getString(TAG_telephone);
                                        strTelephone2=arr_telephone2[i];
                                        textCompany_name_default.setVisibility(View.GONE);

                                        textCustomer_name_default.setText(strCustomerName);
                                        textStreet_name_default.setText(strStreetName);
                                        textCity_name_default.setText(strCityName);
                                        textPincode_name_default.setText("Pin :" + strPincode);
                                        textTelephone_name_default.setText("T: " + strTelephone);
                                        textCompany_name_default.setVisibility(View.GONE);
                                    }

                                }
                                else
                                    {
                                       arr_default_ship[i]="0";
                                        //rl_default_shipping.setVisibility(View.GONE);
                                       hashMap = new HashMap<String, String>();
                                       hashMap.put(TAG_customer_id, arr_customer_id[i]);
                                       hashMap.put(TAG_street, arr_add_line[i]);
                                       hashMap.put(TAG_telephone,  arr_telephone[i]);
                                       hashMap.put("newtelephone",arr_telephone2[i]);

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
                                       itemList.add(hashMap);
                                }

                            }


                            SharedPreferences.Editor editor= preferences.edit();
                            editor.putStringSet("all_address_id",arrayList_AddressId);
                            editor.commit();

                            updateAddressList();
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
                                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                CommonFun.alertError(CustomerAddressBook.this, body);
                                // JSONObject jsonObject=new JSONObject(body.toString());
                                // String message=jsonObject.getString("message");
                                //CommonFun.alertError(CustomerAddressBook.this,message);

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
                        R.id.textCustomer_name,

                }
        );

        if (lstadapter.getCount() > 0) {
            txt_others.setVisibility(View.VISIBLE);
            rl_other_address.setVisibility(View.VISIBLE);
            list_address.invalidate();
            list_address.setAdapter(lstadapter);
            list_address.setVisibility(View.VISIBLE);
            // btn_add_new_address.setVisibility(View.GONE);
        }
        else
        {
            txt_others.setVisibility(View.GONE);
            rl_other_address.setVisibility(View.GONE);
            list_address.setVisibility(View.GONE);
            btn_add_new_address.setVisibility(View.VISIBLE);
        }

    }


}
