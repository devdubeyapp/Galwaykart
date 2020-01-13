package com.galwaykart.profile.address;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.galwaykart.BaseActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.wishList.WishListDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.galwaykart.essentialClass.Global_Settings.api_url;

/**
 * Created by sumitsaini on 10/17/2017.
 * this is only for distributor
 * Distributor can update their saved Address
 */

public class UpdateAddressActivity extends BaseActivity {

    Spinner spinner_select_franch;
    String [] spinner_data = {};
    ArrayAdapter<String> adapter;
    Button button_update_address;
    String st_Get_Franch_Details_URL = "";
    String st_franchisee_name = "",st_franchisee_code="",st_franchisee_address="",st_franchisee_state="",st_franchisee_city="",st_alert_msg="",st_franchisee_pin="";
    TransparentProgressDialog pDialog;
    JSONArray franch_details = null;

    SharedPreferences pref;
    JSONObject object = null;


    String login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",region_code="",region="",region_id="",st_street="",
            login_email="",login_fname="",login_lname="",login_id="",login_group_id="",default_shipping="",created_at="",
            updated_at="",created_in="",store_id="",website_id="",disable_auto_group_change="",default_billing="",st_company="",
            selected_franch_pin="",selected_franch_city="";

    EditText franch_address,franch_state,franch_city,franch_pin;

    String st_update_address_URL = "",mRequestBody="",tokenData="";

    String [] arr_franchisee_address,arr_franchisee_state,arr_franchisee_city,arr_franchisee_pin,arr_franchisee_code;
    String st_f_code;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent intent = new Intent(UpdateAddressActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initNavigationDrawer();

        pref = CommonFun.getPreferences(getApplicationContext());


        // get Already saved address details

        login_email = pref.getString("login_email","");
        login_fname = pref.getString("login_fname","");
        login_lname = pref.getString("login_lname","");
        login_id = pref.getString("login_id","");
        login_group_id = pref.getString("login_group_id","");
        default_billing = pref.getString("default_billing","");
        default_shipping = pref.getString("default_shipping","");
        created_at = pref.getString("created_at","");
        updated_at = pref.getString("updated_at","");
        created_in = pref.getString("created_in","");
        store_id = pref.getString("store_id","");
        website_id = pref.getString("website_id","");
        disable_auto_group_change = pref.getString("disable_auto_group_change","");
        login_telephone = pref.getString("login_telephone","");
        login_customer_id = pref.getString("login_customer_id","");
        address_id= pref.getString("address_id","");
        address_region_id = pref.getString("address_region_id","");
        address_country_id = pref.getString("address_country_id","");
        address_telephone = pref.getString("address_telephone","");
        address_postcode = pref.getString("address_postcode","");
        address_city = pref.getString("address_city","");
        address_firstname= pref.getString("address_firstname","");
        address_lastname = pref.getString("address_lastname","");
        address_default_shipping= pref.getString("address_default_shipping","");
        region_code =  pref.getString("region_code","");
        region =  pref.getString("region","");
        region_id =  pref.getString("region_id","");
        st_street = pref.getString("st_street","");
        tokenData = pref.getString("tokenData","");


//        String st_region_id= region_id.substring(0,1);
//
//        if(st_region_id.equalsIgnoreCase("0")){
//            region_id = region_id.replace("0","");
//        }
//
//        String st_address_region_id= address_region_id.substring(0,1);
//
//        if(st_address_region_id.equalsIgnoreCase("0")){
//            address_region_id = address_region_id.replace("0","");
//        }
//
//        ////Log.d("region_id",region_id);

//        st_company = "Glaze";
//        address_region_id = "517";
//        region_id = "517";




    }

    @Override
    protected void onResume() {
        super.onResume();

        pref = CommonFun.getPreferences(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spinner_select_franch = (Spinner) findViewById(R.id.spinner_select_franch);
        franch_pin = (EditText)findViewById(R.id.franch_pin);
        franch_address = (EditText)findViewById(R.id.franch_address);
        franch_state = (EditText)findViewById(R.id.franch_state);
        franch_city = (EditText)findViewById(R.id.franch_city);

        button_update_address = (Button)findViewById(R.id.button_update_address);
        button_update_address.setOnClickListener(button_update_addressOnClickListener);


        st_Get_Franch_Details_URL = Global_Settings.galway_api_url+"returnAPi/Load_franchisee_details";

        spinner_select_franch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected_franch_address = arr_franchisee_address[position];
                selected_franch_pin = arr_franchisee_pin[position];
                selected_franch_city= arr_franchisee_city[position];
                String selected_franch_state = arr_franchisee_state[position];

                st_f_code=arr_franchisee_code[position];

                if(position > 0) {

                    franch_address.setText(selected_franch_address);
                    franch_state.setText(selected_franch_state);
                    franch_city.setText(selected_franch_city);
                    franch_pin.setText(selected_franch_pin);

//                    updateAddress();

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getFranchDetails();



    }

    Button.OnClickListener button_update_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            updateAddress();
        }};



    private void updateAddress() {

        st_update_address_URL = api_url+"rest/V1/customers/me";




                String st_region_id= region_id.substring(0,1);

        if(st_region_id.equalsIgnoreCase("0")){
            region_id = region_id.replace("0","");
        }

        String st_address_region_id= address_region_id.substring(0,1);

        if(st_address_region_id.equalsIgnoreCase("0")){
            address_region_id = address_region_id.replace("0","");
        }

        ////Log.d("region_id",region_id);

        int i_reg_id=Integer.parseInt(region_id);
        int add_reg_id=Integer.parseInt(address_region_id);

        //region_id.replaceFirst("^0+(?!$)","");
        //address_region_id.replaceFirst("^0+(?!$)","");
        mRequestBody ="{\"customer\":{\"id\":"+login_id+",\"group_id\":"+login_group_id+","+
                "\"default_billing\":\""+default_billing+"\",\"Default_shipping\":\""+default_shipping+"\"," +
                "\"created_at\":\""+created_at+"\",\"updated_at\":\""+updated_at+"\"," +
                "\"created_in\":\""+created_in+"\",\"email\":\""+login_email+"\"," +
                "\"firstname\":\""+login_fname+"\",\"lastname\":\""+login_lname+"\",\"store_id\":"+store_id+"," +
                "\"website_id\":"+website_id+",\"addresses\":[{\"id\":"+address_id+",\"customer_id\":"+login_customer_id+"," +
                "\"region\":{\"region_code\":\""+region_code+"\",\"region\":\""+region+"\"," +


                "\"region_id\":\""+st_f_code+"\"}," +
                "\"region_id\":\""+st_f_code+"\""+

                ",\"country_id\":\""+address_country_id+"\"," +
                "\"street\":[\""+st_street+"\"],\"company\":\""+st_company+"\",\"telephone\":\""+address_telephone+"\"," +
                "\"postcode\":\""+selected_franch_pin+"\",\"city\":\""+selected_franch_city+"\",\"firstname\":\""+login_fname+"\"," +
                "\"lastname\":\""+login_lname+"\",\"default_shipping\":true,\"default_billing\":true}]," +
                "\"disable_auto_group_change\":0}}";

        ////Log.d("region_id",region_id);
        ////Log.d("mRequestBody", mRequestBody);

        pDialog = new TransparentProgressDialog(UpdateAddressActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        try {

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, st_update_address_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();


//                            CommonFun.alertError(UpdateAddressActivity.this, response.toString());
                            ////Log.d("VOLLEY", response);
                            try {

                                object = new JSONObject(String.valueOf(response));

                                String id = object.getString("id");
                                ////Log.d("id",id);
                                JSONArray arr_address = object.getJSONArray("addresses");
                                int arr_address_length = arr_address.length();

                                for(int i = 0; i < arr_address_length; i++) {
                                    JSONObject address_obj = arr_address.getJSONObject(0);

                                    String add_id = address_obj.getString("id");
                                    ////Log.d("add_id",add_id);

                                    if (!add_id.equalsIgnoreCase("")) {
                                        Vibrator vibrator = (Vibrator) UpdateAddressActivity.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);

                                        final Dialog dialog = new Dialog(UpdateAddressActivity.this);
                                        dialog.setContentView(R.layout.custom_alert_dialog_design);
                                        TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
                                        tv_dialog.setText("Your address book has been updated successfully...");
                                        ImageView image_view_dialog = (ImageView) dialog.findViewById(R.id.image_view_dialog);
                                        dialog.show();

                                        new CountDownTimer(4000, 4000) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                // TODO Auto-generated method stub

                                            }

                                            @Override
                                            public void onFinish() {
                                                // TODO Auto-generated method stub

                                                dialog.dismiss();

                                            }
                                        }.start();
                                    }


                                }

                            } catch (Exception e) {
                                //e.printStackTrace();
//                                CommonFun.alertError(UpdateAddressActivity.this, e.toString());


                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();

//                    ////Log.d("VOLLEY", error.toString());
//                    CommonFun.alertError(LoginActivity.this, error.toString());
                    CommonFun.showVolleyException(error,UpdateAddressActivity.this);

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
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
//                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            requestQueue.add(stringRequest);


        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...", "Error");
        }


    }

    private void getFranchDetails() {

        pDialog = new TransparentProgressDialog(UpdateAddressActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Franch_Details_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){
                            try {

                                franch_details = new JSONArray(String.valueOf(response));
                                int franch_details_arr_length = franch_details.length();
                                spinner_data = new String[franch_details_arr_length+1];
                                arr_franchisee_address = new String[franch_details_arr_length+1];
                                arr_franchisee_city = new String[franch_details_arr_length+1];
                                arr_franchisee_state = new String[franch_details_arr_length+1];
                                arr_franchisee_pin = new String[franch_details_arr_length+1];
                                arr_franchisee_code = new String[franch_details_arr_length+1];

                                for(int i=0; i < franch_details_arr_length; i++){

                                    JSONObject object = franch_details.getJSONObject(i);
                                    st_franchisee_name = object.getString("franchisee_name");
                                    st_franchisee_code = object.getString("franchisee_code");
                                    st_franchisee_address = object.getString("franchisee_address");
                                    st_franchisee_state = object.getString("franchisee_state");
                                    st_franchisee_city = object.getString("franchisee_city");
                                    st_franchisee_pin = object.getString("franchisee_pin");

                                     spinner_data[0] = "Select Franchisee";
                                     spinner_data[i+1] = st_franchisee_name + " (" + st_franchisee_code + ")";

                                        arr_franchisee_address[i+1] = st_franchisee_address;
                                        arr_franchisee_pin[i+1] = st_franchisee_pin;
                                        arr_franchisee_state[i+1] = st_franchisee_state;
                                        arr_franchisee_city[i+1] = st_franchisee_city;
                                    arr_franchisee_code[i+1] = object.getString("franchisee_statecode");


                                }



                                adapter = new ArrayAdapter<String>(UpdateAddressActivity.this,android.R.layout.simple_spinner_dropdown_item,spinner_data);
                                spinner_select_franch.setAdapter(adapter);




                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());

                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(String.valueOf(response));
                                    if(jsonObj.has("Message"))
                                    {
                                        st_alert_msg = jsonObj.getString("Message");
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                ////Log.d("error",st_alert_msg);

                                CommonFun.alertError(UpdateAddressActivity.this,st_alert_msg);
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

                CommonFun.showVolleyException(error,UpdateAddressActivity.this);

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

        queue.add(jsObjRequest);



    }
}
