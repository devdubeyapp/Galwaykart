package com.galwaykart.address_book;

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
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**Show Default Billing address of the user
 * Created by ankesh on 9/18/2017.
 */

public class BillingAddress extends BaseActivity {

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
    SharedPreferences preferences;
    RelativeLayout rel_no_address;
    int length_of_street;
    HashMap<String, String> hashMap;
    Button btn_add_address;
    String st_add_line1="";
    String customer_id = "",telephone="",postcode="",city="",region="",
            firstname="",lastname="",company="",street="",region_code="",region_id="";
    TransparentProgressDialog pDialog;
    TextView tv_title_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_shipping_address);

        preferences = CommonFun.getPreferences(getApplicationContext());
        hashMap = new HashMap<String, String>();

        initNavigationDrawer();

        tokenData = preferences.getString("tokenData","");
//        tokenData = "ituo1gwphqdjidohau02y4dbu6880rof";

        rel_no_address = (RelativeLayout)findViewById(R.id.rel_no_address);
        btn_add_address = (Button)findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(btn_add_addressOnClickListener);

        list_address=(ListView)findViewById(R.id.list_address);

        url_address= Global_Settings.api_url+"rest/V1/customers/me/billingAddress";

        tv_title_address=(TextView)findViewById(R.id.tv_title_address);
        tv_title_address.setText("Billing Address");


        /**
         * Get default billing address of the user
         * and show
         */
            callAddressBookList();


    }

    Button.OnClickListener btn_add_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("addnew","billing");
            editor.commit();

            Intent intent = new Intent(BillingAddress.this,AddNewAddress.class);
            startActivity(intent);
            CommonFun.finishscreen(BillingAddress.this);



        }};

    /**
     * Get default billing address of the user
     * and show
     */
    private void  callAddressBookList(){

        itemList=new ArrayList<HashMap<String, String>>();
//        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(BillingAddress.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_address, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response", response.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {

/**
 * Get all billing address from the api
 * and fill it in array
 */
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


                                //Log.d("add_line1", add_line1);
                                //Log.d("st_add_line1", st_add_line1);
                            }


/**
 * Store  all billing address from the api
 * in the temp storage
 */
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


/**
 * fill address data in the list
 *
 */

                            ListAdapter lstadapter = new AddressBookAdapter(BillingAddress.this, itemList, R.layout.activity_default_shipping_address_list,
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
                            Intent intent=new Intent(BillingAddress.this, ExceptionError.class);
                            startActivity(intent);
                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Log.d("ERROR", "error => " + error.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        CommonFun.showVolleyException(error, BillingAddress.this);
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
        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);

    }

}
