package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.galwaykart.BaseActivity;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.essentialClass.VolleySingleton;
import com.galwaykart.profile.UpdateAddressActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fetch default shipping address and show to the user
 * RKT user can add new shilpping address
 * all user can update shipping address
 * Created by ankesh on 9/18/2017.
 */

public class AddressBookClass extends BaseActivity{

    ListView list_address;
    String url_address="";

    ArrayList<HashMap<String,String>> itemList;
    String tokenData="",add_line1="",add_line2="";

    JSONArray addressArray = null;

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
    //String user_detail_url="";
    String  new_telephone="",
            new_postcode="",
            new_city="",
            new_firstname="",
            new_lastname="",
            new_company="",
            new_region_code="",
            new_region="",
            new_region_id="",
            new_add_line1="",
            new_country_id="";

    String [] arr_add_line,arr_telephone,arr_postcode,arr_region,arr_edit_boolean,arr_selected_address,arr_city,
            arr_firstname,arr_lastname,arr_state,arr_customer_id,arr_company;

    String st_come_from_update;
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(AddressBookClass.this,CartItemList.class);
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


        Intent intents=new Intent(this,CustomerAddressBook.class);
        startActivity(intents);
        CommonFun.finishscreen(this);

        preferences = CommonFun.getPreferences(AddressBookClass.this);

        pref = CommonFun.getPreferences(AddressBookClass.this);



        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String st_come_from_update1=bundle.getString("st_come_from_update");
            if(st_come_from_update1!=null && !st_come_from_update1.equals(""))
                st_come_from_update=st_come_from_update1;


        }

        list_address = findViewById(R.id.list_address);

        itemList=new ArrayList<HashMap<String, String>>();


        new_telephone = pref.getString("new_telephone", "");
        new_postcode = pref.getString("new_postcode", "");
        new_city = pref.getString("new_city", "");
        new_firstname = pref.getString("new_firstname", "");
        new_lastname = pref.getString("new_lastname", "");
        new_company = pref.getString("new_company", "");
        new_region_code = pref.getString("new_region_code", "");
        new_region = pref.getString("new_region", "");
        new_region_id = pref.getString("new_region_id", "");
        new_add_line1 = pref.getString("new_add_line1", "");
        new_country_id = pref.getString("new_country_id", "");



//        SharedPreferences.Editor editor=pref.edit();
//        editor.putString("st_dist_id","");
//        editor.putString("log_user_zone","");
//        editor.commit();




        initNavigationDrawer();

        tokenData = preferences.getString("tokenData", "");
//        tokenData = "ituo1gwphqdjidohau02y4dbu6880rof";
      //  //Log.d("tokenData123",tokenData);

        rel_no_address = findViewById(R.id.rel_no_address);
        btn_add_address = findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(btn_add_addressOnClickListener);



        url_address = Global_Settings.api_url + "rest/V1/customers/me/shippingAddress";
       // //Log.d("url_address", url_address);

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


//                SharedPreferences.Editor editor= preferences.edit();
//                editor.putString("addnew","shipping");
//                editor.putString("new_add_added","false");
//                editor.commit();



                Intent intent = new Intent(AddressBookClass.this,AddNewAddress.class);
                intent.putExtra("add_new","new");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(AddressBookClass.this);

            }
        });


        btn_change_address= findViewById(R.id.btn_change_address);
        btn_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requsetChangeAddress();
            }
        });


        //user_detail_url= Global_Settings.user_details_url+pref.getString("login_customer_id","");
        ////Log.d("user_detail_url",user_detail_url);
        //callAddressBookList();

        String login_group_id=pref.getString("login_group_id","");

//        if(login_group_id.equalsIgnoreCase("4")) {
//
            callAddressBookList();
//        }
//        else
//        {
//            Intent intent=new Intent(AddressBookClass.this,CustomerAddressBook.class);
//            startActivity(intent);
//            CommonFun.finishscreen(AddressBookClass.this);
//        }


    }

    /**
     * Call to change address
     *
     */
    private void requsetChangeAddress(){

         Intent intent=new Intent(this,UpdateAddressActivity.class);
         intent.putExtra("addressupdate","checkout");
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);
         CommonFun.finishscreen(this);

    }

    
    

    Button.OnClickListener btn_add_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


//            SharedPreferences.Editor editor= preferences.edit();
//            editor.putString("addnew","shipping");
//            editor.commit();



            Intent intent = new Intent(AddressBookClass.this,AddNewAddress.class);
            intent.putExtra("st_come_from_update","addnew");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            CommonFun.finishscreen(AddressBookClass.this);

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
     * Get default address list of the user
     */
    private void  callAddressBookList(){


        tokenData = tokenData.replaceAll("\"", "");

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_address, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response987654", response.toString());
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();

                        try {


                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));


                            addressArray = jsonObj.getJSONArray("street");
                            length_of_street = addressArray.length();
                            arr_add_line = new String[2];
                            arr_firstname = new String[2];
                            arr_lastname = new String[2];
                            arr_postcode = new String[2];
                            arr_region = new String[2];
                            arr_state = new String[2];
                            arr_telephone = new String[2];
                            arr_customer_id = new String[2];
                            arr_company = new String[2];
                            arr_edit_boolean = new String[2];
                            arr_selected_address = new String[2];
                            arr_city=new String[2];

                            country_id = jsonObj.getString("country_id");
                            default_shipping = "1";
                            default_billing = "1";

                            if (jsonObj.has("company")) {
                                customer_id = jsonObj.getString(TAG_customer_id);
                                telephone = jsonObj.getString(TAG_telephone);
                                postcode = jsonObj.getString(TAG_postcode);
                                city = jsonObj.getString(TAG_city);
                                firstname = jsonObj.getString(TAG_firstname);
                                lastname = jsonObj.getString(TAG_lastname);
                                company = jsonObj.getString(TAG_company);

                                JSONObject object = jsonObj.getJSONObject("region");

                                region_code = object.getString("region_code");
                                region = object.getString("region");
                                region_id = object.getString("region_id");

                                arr_telephone[0] = telephone;
                                arr_postcode[0] = postcode;
                                arr_firstname[0] = firstname;
                                arr_lastname[0] = lastname;
                                arr_region[0] = city;
                                arr_state[0] = region;
                                arr_customer_id[0] = customer_id;
                                arr_company[0]=company;
                                arr_edit_boolean[0] = "false";
                                arr_selected_address[0]="Franchisee";
                                arr_city[0] = city;

                            } else {
                                customer_id = jsonObj.getString(TAG_customer_id);

                                telephone = jsonObj.getString(TAG_telephone);
                                postcode = jsonObj.getString(TAG_postcode);
                                city = jsonObj.getString(TAG_city);
                                firstname = jsonObj.getString(TAG_firstname);
                                lastname = jsonObj.getString(TAG_lastname);


                                JSONObject object = jsonObj.getJSONObject("region");

                                region_code = object.getString("region_code");
                                region = object.getString("region");
                                region_id = object.getString("region_id");


                            }

                            for (int i = 0; i < addressArray.length(); i++) {

                                add_line1 = add_line1 + addressArray.getString(i);
                                arr_add_line[0]=add_line1;
                              //  //Log.d("add_line1", add_line1);
                              //  //Log.d("st_add_line1", st_add_line1);

                            }

                            SharedPreferences pref;
                            pref = CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("customer_id", customer_id);
                            editor.putString("telephone", telephone);
                            editor.putString("postcode", postcode);
                            editor.putString("city", city);
                            editor.putString("firstname", firstname);
                            editor.putString("lastname", lastname);
                            editor.putString("company", company);
                            editor.putString("region_code", region_code);
                            editor.putString("country_id", country_id);
                            editor.putString("region", region);
                            editor.putString("region_id", region_id);
                            editor.putString("add_line1", add_line1);
                            editor.putString("default_shipping", default_shipping);
                            editor.putString("default_billing", default_billing);

                            editor.putString("bill_customer_id", customer_id);
                            editor.putString("bill_telephone", telephone);
                            editor.putString("bill_postcode", postcode);
                            editor.putString("bill_city", city);
                            editor.putString("bill_firstname", firstname);
                            editor.putString("bill_lastname", lastname);
                            editor.putString("bill_company", company);
                            editor.putString("bill_region_code", region_id);

                            editor.putString("bill_region", region);
                            editor.putString("bill_region_id", region_id);
                            editor.putString("bill_add_line1", add_line1);


                            editor.commit();

                            if(!new_telephone.equalsIgnoreCase("")) {

                                arr_telephone[1] = new_telephone;
                                arr_postcode[1] = new_postcode;
                                arr_firstname[1] = new_firstname;
                                arr_lastname[1] = new_lastname;
                                arr_region[1] = new_city;
                                arr_state[1] = new_region;
                                arr_customer_id[1] = new_country_id;
                                arr_add_line[1] = new_add_line1;
                                arr_edit_boolean[1] = "true";
                                arr_selected_address[1]="Home";
                                arr_city[1] = new_city;

                                for(int i=0;i<arr_add_line.length;i++) {

                                    hashMap = new HashMap<String, String>();
                                    hashMap.put(TAG_customer_id, arr_customer_id[i]);
                                    hashMap.put(TAG_street, arr_add_line[i]);
                                    hashMap.put(TAG_telephone, "T: " + arr_telephone[i]);
                                    hashMap.put(TAG_postcode, "Pin: " + arr_postcode[i]);
                                    hashMap.put(TAG_city, arr_city[i] + ", " + arr_state[i]);
                                    hashMap.put(TAG_firstname, arr_firstname[i] + " " + arr_lastname[i]);
                                    hashMap.put(TAG_region, arr_state[i]);
                                    hashMap.put(TAG_company, "");
                                    hashMap.put(TAG_edit, arr_edit_boolean[i]);
                                    hashMap.put(TAG_selected_address, arr_selected_address[i]);
                                    hashMap.put("st_come_from_update",st_come_from_update);

                                    itemList.add(hashMap);

                                }
                                btn_add_address.setVisibility(View.GONE);
                                btn_change_address.setVisibility(View.GONE);
                                btn_add_new_address.setVisibility(View.GONE);
                            }
                            else{

                                arr_telephone[0] = telephone;
                                arr_postcode[0] = postcode;
                                arr_firstname[0] = firstname;
                                arr_lastname[0] = lastname;
                                arr_region[0] = city;
                                arr_state[0] = region;
                                arr_customer_id[0] = customer_id;
                                arr_edit_boolean[0] = "false";
                                arr_selected_address[0]="Franchisee";
                                arr_city[0] = city;

                                for(int i=0;i<arr_add_line.length-1;i++) {

                                    hashMap = new HashMap<String, String>();
                                    hashMap.put(TAG_customer_id, arr_customer_id[i]);
                                    hashMap.put(TAG_street, arr_add_line[i]);
                                    hashMap.put(TAG_telephone, "T: " + arr_telephone[i]);
                                    hashMap.put(TAG_postcode, "Pin: " + arr_postcode[i]);
                                    hashMap.put(TAG_city, arr_city[i] + ", " + arr_state[i]);
                                    hashMap.put(TAG_firstname, arr_firstname[i] + " " + arr_lastname[i]);
                                    hashMap.put(TAG_region, arr_state[i]);
                                    hashMap.put(TAG_company, "");
                                    hashMap.put(TAG_edit, arr_edit_boolean[i]);
                                    hashMap.put(TAG_selected_address, arr_selected_address[i]);
                                    hashMap.put("st_come_from_update",st_come_from_update);
                                    itemList.add(hashMap);

                                }

                            }



                                updateAddressList();



/**
 * Get user details from Galwaykart
 * zone and user id
 */
                                   //getUserDetails();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            Intent intent=new Intent(AddressBookClass.this, ExceptionError.class);
                            startActivity(intent);

                            rel_no_address.setVisibility(View.VISIBLE);
                            list_address.setVisibility(View.GONE);
                        }
                        catch (IllegalArgumentException ex){
                            Intent intent=new Intent(AddressBookClass.this, ExceptionError.class);
                            startActivity(intent);

                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                       // //Log.d("ERROR", "error => " + error.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        CommonFun.showVolleyException(error, AddressBookClass.this);
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
        //requestQueue.getCache().clear();
        //requestQueue.add(jsObjRequest);
        VolleySingleton.getInstance(AddressBookClass.this).addToRequestQueue(jsObjRequest);

    }

    private void updateAddressList() {


        ListAdapter lstadapter = new AddressBookAdapter(AddressBookClass.this, itemList, R.layout.activity_default_shipping_address_list,
                new String[]{TAG_street, TAG_telephone, TAG_postcode, TAG_city, TAG_firstname, TAG_company,TAG_edit},
                new int[]{R.id.textStreet_name,
                        R.id.textTelephone_name,
                        R.id.textPincode_name,
                        R.id.textCity_name,
                        R.id.textCustomer_name,
                        R.id.textCompany_name,
                        R.id.bt_edit_add_icon

                }
        );

        if (lstadapter.getCount() > 0) {
            list_address.invalidate();
            list_address.setAdapter(lstadapter);



        }
    }

}
