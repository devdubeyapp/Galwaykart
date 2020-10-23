package com.galwaykart.profile.address;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.AddNewAddress;
import com.galwaykart.address_book.AddressBookAdapter;
import com.galwaykart.BaseActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.galwaykart.essentialClass.Global_Settings.api_url;

/**
 * Created by sumitsaini on 10/26/2017.
 * Show list of all Addresses saved in user profile
 * user can edit their address
 * user can add new address
 * user can add their first address
 */

public class UpdateAddressBook extends BaseActivity {

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
    final String TAG_address_id = "id";
    final String TAG_customer_name = "";

    SharedPreferences preferences;
    RelativeLayout rel_no_address;
    int length_of_street;
    HashMap<String, String> hashMap;
    Button btn_add_address,bt_add_new_address;
    String st_add_line1="";
    String customer_id = "",telephone="",postcode="",city="",region="",
            firstname="",lastname="",company="",street="",region_code="",region_id="";
    TransparentProgressDialog pDialog;
    TextView tv_title_address;

    String login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",st_street="";


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

            Intent i = new Intent(UpdateAddressBook.this, HomePageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            CommonFun.finishscreen(UpdateAddressBook.this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_shipping_address);

        preferences = CommonFun.getPreferences(getApplicationContext());


        initNavigationDrawer();

        tokenData = preferences.getString("tokenData","");
//        tokenData = "ituo1gwphqdjidohau02y4dbu6880rof";

        rel_no_address = findViewById(R.id.rel_no_address);
        btn_add_address = findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(btn_add_addressOnClickListener);

       // bt_add_new_address = (Button)findViewById(R.id.bt_add_new_address);
       // bt_add_new_address.setOnClickListener(bt_add_new_addressOnClickListener);


        list_address= findViewById(R.id.list_address);

        if(Global_Settings.multi_store==true)
            url_address=api_url+"rest/V1/customers/me";
        else
            url_address=api_url+"index.php/rest/V1/customers/me";

        tv_title_address= findViewById(R.id.tv_title_address);
        tv_title_address.setText("Billing Address");
//        callAddressBookList();
        callLoginAPI();


    }

    Button.OnClickListener btn_add_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("addnew","billing");
            editor.putString("coming_from","profile");
            editor.commit();

            Intent intent = new Intent(UpdateAddressBook.this,AddNewAddress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(UpdateAddressBook.this);



        }};


    Button.OnClickListener bt_add_new_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            preferences = CommonFun.getPreferences(getApplicationContext());
            SharedPreferences.Editor editor= preferences.edit();


            editor.commit();


            Intent intent = new Intent(UpdateAddressBook.this,AddMultipleAddress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(UpdateAddressBook.this);



        }};




    private void callLoginAPI(){

        tokenData=tokenData.replaceAll("\"","");
        itemList=new ArrayList<HashMap<String, String>>();

        itemList.clear();

//        login_progress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url_address,null,
                new Response.Listener<JSONObject>() {

                    @Override   public void onResponse(JSONObject response) {
                        //////Log.d("response",response.toString());
//                        CommonFun.alertError(UpdateAddressBook.this,response.toString());
//                        login_progress.setVisibility(View.INVISIBLE);

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            JSONArray custom_data= jsonObj.getJSONArray("addresses");
                            int custom_data_length = custom_data.length();

                            if(custom_data_length>0) {

                                for(int i =0; i < custom_data_length; i++) {

                                    JSONObject c = custom_data.getJSONObject(i);


                                    login_telephone = c.getString("telephone");
                                    login_customer_id = c.getString("customer_id");
                                    address_id = c.getString("id");
                                    address_region_id = c.getString("region_id");

                                    address_country_id = c.getString("country_id");
                                    address_telephone = c.getString("telephone");
                                    address_postcode = c.getString("postcode");
                                    address_city = c.getString("city");
                                    address_firstname = c.getString("firstname");
                                    address_lastname = c.getString("lastname");
//                                    address_default_shipping = c.getString("default_shipping");

                                    JSONObject c_region = c.getJSONObject("region");

                                    region_code = c_region.getString("region_code");
                                    region = c_region.getString("region");
                                    region_id = c_region.getString("region_id");

                                    JSONArray arr_Street = c.getJSONArray("street");
                                    int arr_street_length = arr_Street.length();
                                    for (int j = 0; j < arr_street_length; j++) {

                                        st_street = arr_Street.getString(0);
                                    }


                                    company = "Glaze";

                                    hashMap = new HashMap<String, String>();

                                    hashMap.put(TAG_customer_id, login_customer_id);
                                    hashMap.put(TAG_street, st_street);
                                    hashMap.put(TAG_telephone, address_telephone);
                                    hashMap.put(TAG_postcode, address_postcode);
                                    hashMap.put(TAG_city, address_city);
                                    hashMap.put(TAG_customer_name, address_firstname + " " + address_lastname);
                                    hashMap.put(TAG_company, company);
                                    hashMap.put(TAG_region, region);
                                    hashMap.put(TAG_address_id, address_id);
                                    hashMap.put(TAG_firstname, address_firstname);
                                    hashMap.put(TAG_lastname, address_lastname);

                                    itemList.add(hashMap);



                                }

                                ListAdapter lstadapter = new AddressUpdateAdapter(UpdateAddressBook.this, itemList, R.layout.update_address_list,
                                        new String[]{TAG_street, TAG_telephone, TAG_postcode, TAG_city, TAG_firstname,TAG_company},
                                        new int[]{R.id.text_Street_name,
                                                R.id.text_Telephone_name,
                                                R.id.text_Pincode_name,
                                                R.id.text_City_name,
                                                R.id.text_Customer_name,
                                                R.id.text_Company_name

                                        }
                                );

                                if (lstadapter.getCount() > 0) {
                                    list_address.setAdapter(lstadapter);
                                    bt_add_new_address.setVisibility(View.VISIBLE);
                                }


                            }
                            else{
                                rel_no_address.setVisibility(View.VISIBLE);
                                list_address.setVisibility(View.GONE);
                                bt_add_new_address.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },


                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
//                        login_progress.setVisibility(View.INVISIBLE);
                        CommonFun.showVolleyException(error,UpdateAddressBook.this);

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+tokenData);
                params.put("Content-Type","application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);

    }


    private void  callAddressBookList(){

        itemList=new ArrayList<HashMap<String, String>>();
//        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(UpdateAddressBook.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_address, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {


                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            addressArray = jsonObj.getJSONArray("street");
                            length_of_street = addressArray.length();

                            if(jsonObj.has("company")) {
                                customer_id= jsonObj.getString(TAG_customer_id);

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
                            }

                            else{
                                customer_id = jsonObj.getString(TAG_customer_id);

                                telephone = jsonObj.getString(TAG_telephone);
                                postcode = jsonObj.getString(TAG_postcode);
                                city = jsonObj.getString(TAG_city);
                                firstname = jsonObj.getString(TAG_firstname);
                                lastname = jsonObj.getString(TAG_lastname);
//                                String company = jsonObj.getString(TAG_company);

                                JSONObject object = jsonObj.getJSONObject("region");

                                region_code = object.getString("region_code");
                                region = object.getString("region");
                                region_id = object.getString("region_id");
                            }




                            for (int i = 0; i < addressArray.length(); i++) {

                                add_line1 = add_line1 + addressArray.getString(i);


                                ////Log.d("add_line1", add_line1);
                                ////Log.d("st_add_line1", st_add_line1);
                            }



                            SharedPreferences pref;
                            pref= CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("bill_customer_id",customer_id);
                            editor.putString("bill_telephone",telephone);
                            editor.putString("bill_postcode",postcode);
                            editor.putString("bill_city",city);
                            editor.putString("bill_firstname",firstname);
                            editor.putString("bill_lastname",lastname);
                            editor.putString("bill_company",company);
                            editor.putString("bill_region_code",region_code);

                            editor.putString("bill_region",region);
                            editor.putString("bill_region_id",region_id);
                            editor.putString("bill_add_line1",add_line1);

                            editor.commit();



                            hashMap.put(TAG_customer_id, customer_id);
                            hashMap.put(TAG_street, add_line1);
                            hashMap.put(TAG_telephone,"T: "+ telephone);
                            hashMap.put(TAG_postcode,"Pin: " + postcode);
                            hashMap.put(TAG_city, city+", "+region);
                            hashMap.put(TAG_firstname, firstname + " " + lastname);
                            hashMap.put(TAG_company, company);
                            hashMap.put(TAG_region, region);

                            itemList.add(hashMap);




                            ListAdapter lstadapter = new AddressBookAdapter(UpdateAddressBook.this, itemList, R.layout.update_address_list,
                                    new String[]{TAG_street, TAG_telephone, TAG_postcode, TAG_city, TAG_firstname,TAG_company},
                                    new int[]{R.id.textStreet_name,
                                            R.id.textTelephone_name,
                                            R.id.textPincode_name,
                                            R.id.textCity_name,
                                            R.id.textCustomer_name,
                                            R.id.textCompany_name

                                    }
                            );

                            if (lstadapter.getCount() > 0) {
                                list_address.setAdapter(lstadapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ////Log.d("ERROR", "error => " + error.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        rel_no_address.setVisibility(View.VISIBLE);
                        list_address.setVisibility(View.GONE);
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
        requestQueue.add(jsObjRequest);

    }




}
