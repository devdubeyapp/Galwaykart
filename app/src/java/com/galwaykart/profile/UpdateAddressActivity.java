package com.galwaykart.profile;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.galwaykart.address_book.AddressBook;
import com.galwaykart.BaseActivityWithoutCart;
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
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.galwaykart.essentialClass.Global_Settings.api_url;

/**
 * Created by sumitsaini on 10/17/2017.
 */

public class UpdateAddressActivity extends BaseActivityWithoutCart {

    Spinner spinner_select_franch;
    String [] spinner_data = {};
    ArrayAdapter<String> adapter;
    RelativeLayout rel_layout1,rel_layout2,rel_layout3,rel_layout4;
    Button button_update_address,button_save_address,button_get_otp,button_Submit;
    EditText dist_id,first_name,last_name,email,password,confirm_password,otp,phone_no;
    String st_Get_Franch_Details_URL = "";
    String st_franchisee_name = "",st_franchisee_code="",st_franchisee_state_code="",
            st_franchisee_address="",st_franchisee_state="",st_franchisee_city="",st_alert_msg="",st_franchisee_pin="";
    TransparentProgressDialog pDialog;
    JSONArray franch_details = null;
    int  otp_random_no;
    String st_entered_otp="",st_coming_from="";
    String st_group_id="";
    TimerTask timerTask;
    SharedPreferences pref;
    String st_email = "",st_first_name="",st_last_name="",st_password="",st_comfirm_pwd="",st_dibid="";

    String st_Save_User_URL = "",st_input_data="";
    private Pattern pattern;
    private Matcher matcher;
    JSONObject object = null;


    String login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",region_code="",region="",region_id="",st_street="",
            login_email="",login_fname="",login_lname="",login_id="",login_group_id="",default_shipping="",created_at="",
            updated_at="",created_in="",store_id="",website_id="",disable_auto_group_change="",default_billing="",st_company="",
            selected_franch_pin="",selected_franch_city="",selected_franch_address="",selected_franch_state="";

    EditText franch_address,franch_state,franch_city,franch_pin;

    String st_update_address_URL = "",mRequestBody="",tokenData="",selected_franch_state_code="",selected_franch_code="",
            st_pin="",st_city="",st_state="",street="",getRegAddressURL = "";

    String [] arr_franchisee_address,arr_franchisee_state,arr_franchisee_city,arr_franchisee_pin,arr_franchisee_code,arr_franchisee_state_code;

    Boolean is_req_from_checkout=false;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(is_req_from_checkout==false) {
            Intent intent = new Intent(UpdateAddressActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }
        else
        {
           //super.onBackPressed();
            Intent intent = new Intent(UpdateAddressActivity.this, AddressBook.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initNavigationDrawer();

        pref = CommonFun.getPreferences(getApplicationContext());


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
        //////Log.d("tokenData123",tokenData);
        st_pin=pref.getString("pin","");
        st_city=pref.getString("city","");
        st_state=pref.getString("state","");
        street=pref.getString("street","");


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                String req_from_data= extras.getString("addressupdate");
                if(req_from_data.equals("checkout"))
                    is_req_from_checkout=true;

            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        pref = CommonFun.getPreferences(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spinner_select_franch = (Spinner) findViewById(R.id.spinner_select_franch);
        spinner_select_franch.setVisibility(View.GONE);
        franch_pin = (EditText)findViewById(R.id.franch_pin);
        franch_address = (EditText)findViewById(R.id.franch_address);
        franch_state = (EditText)findViewById(R.id.franch_state);
        franch_city = (EditText)findViewById(R.id.franch_city);

        button_update_address = (Button)findViewById(R.id.button_update_address);
        button_update_address.setOnClickListener(button_update_addressOnClickListener);

        button_update_address.setVisibility(View.VISIBLE);

        button_save_address = (Button)findViewById(R.id.button_save_address);
        button_save_address.setOnClickListener(button_save_addressOnClickListener);

        button_save_address.setVisibility(View.GONE);


        st_Get_Franch_Details_URL = Global_Settings.galway_api_url+"returnAPi/Load_franchisee_details";

        spinner_select_franch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 selected_franch_address = arr_franchisee_address[position];
                selected_franch_pin = arr_franchisee_pin[position];
                selected_franch_city= arr_franchisee_city[position];
                selected_franch_state = arr_franchisee_state[position];
                selected_franch_state_code = arr_franchisee_state_code[position];
                selected_franch_code = arr_franchisee_code[position];


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


        getRegAddressURL = api_url+"rest/V1/customers/me";


        //getFranchDetails();

        if(is_req_from_checkout==true){
//            button_update_address.setVisibility(View.GONE);
//            button_save_address.setVisibility(View.VISIBLE);
            callUpdateAddress();
        }
        else
        {
            getRegisteredAddress();
        }

    }


    private void getRegisteredAddress(){


        tokenData=tokenData.replaceAll("\"","");
        //////Log.d("tokenData",tokenData);

        pDialog = new TransparentProgressDialog(UpdateAddressActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,getRegAddressURL,null,
                new Response.Listener<JSONObject>() {

                    @Override   public void onResponse(JSONObject response) {
                        //////Log.d("response",response.toString());
//                        CommonFun.alertError(UpdateAddressActivity.this,response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        try {

                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));
                            String login_email=jsonObj.getString("email");
                            String login_fname=jsonObj.getString("firstname");
                            String login_lname=jsonObj.getString("lastname");
                            String login_id=jsonObj.getString("id");
                            String login_group_id=jsonObj.getString("group_id");


                            String default_shipping = jsonObj.getString("default_shipping");
                            String default_billing = jsonObj.getString("default_billing");
                            String created_at = jsonObj.getString("created_at");
                            String updated_at = jsonObj.getString("updated_at");
                            String created_in = jsonObj.getString("created_in");


                            String store_id = jsonObj.getString("store_id");
                            String website_id = jsonObj.getString("website_id");
                            String disable_auto_group_change = jsonObj.getString("disable_auto_group_change");



                            JSONArray custom_data= jsonObj.getJSONArray("addresses");

                            if(custom_data.length()>0) {

                                for(int i =0;i<custom_data.length();i++) {

                                    JSONObject c = custom_data.getJSONObject(i);

                                    login_telephone  = c.getString("telephone");
                                    login_customer_id = c.getString("customer_id");

                                    address_id = c.getString("id");
                                    address_region_id = c.getString("region_id");

                                    address_country_id = c.getString("country_id");
                                    address_telephone = c.getString("telephone");
                                    address_postcode = c.getString("postcode");
                                    address_city = c.getString("city");
                                    address_firstname = c.getString("firstname");
                                    address_lastname = c.getString("lastname");
                                    address_default_shipping = c.getString("default_shipping");

                                    JSONObject c_region = c.getJSONObject("region");

                                    region_code = c_region.getString("region_code");
                                    region = c_region.getString("region");
                                    region_id = c_region.getString("region_id");

                                    JSONArray arr_Street = c.getJSONArray("street");
                                    int arr_street_length = arr_Street.length();
                                    for(int j=0; j < arr_street_length; j++){

                                        st_street = arr_Street.getString(0);
                                    }

                                }
                            }

                            franch_city.setText(address_city);
                            franch_pin.setText(address_postcode);
                            franch_address.setText(st_street);
                            franch_state.setText(region);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },


                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

//                        CommonFun.alertError(UpdateAddressActivity.this,error.toString());

                        CommonFun.showVolleyException(error,UpdateAddressActivity.this);

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




    Button.OnClickListener button_update_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            callUpdateAddress();

            //updateAddress();
        }};

    private void callUpdateAddress() {
        spinner_select_franch.setVisibility(View.VISIBLE);
        button_save_address.setVisibility(View.VISIBLE);
        button_update_address.setVisibility(View.GONE);

//            franch_state.setText("");
//            franch_address.setText("");
//            franch_pin.setText("");
//            franch_city.setText("");

        getFranchDetails();
    }

    Button.OnClickListener button_save_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            spinner_select_franch.setVisibility(View.VISIBLE);
            String st_spinner_value = spinner_select_franch.getSelectedItem().toString();
            String st_address = franch_pin.getText().toString();
            String st_state = franch_pin.getText().toString();
            String st_city = franch_pin.getText().toString();
            String st_pincode =franch_pin.getText().toString();
            //////Log.d("st_spinner_value",st_spinner_value);
            if(!st_spinner_value.equalsIgnoreCase("Select Franchisee")){
                updateAddress();
            }
            else{
                CommonFun.alertError(UpdateAddressActivity.this,"Please select franchisee first!!!");
            }


        }};


    private void updateAddress() {



        st_update_address_URL = api_url+"rest/V1/customers/me";
        st_company=selected_franch_code;
        //////Log.d("st_update_address_URL",st_update_address_URL);

        mRequestBody ="{\"customer\":{\"id\":"+login_id+",\"group_id\":"+login_group_id+","+
                "\"default_billing\":\""+default_billing+"\",\"Default_shipping\":\""+default_shipping+"\"," +
                "\"created_at\":\""+created_at+"\",\"updated_at\":\""+updated_at+"\"," +
                "\"created_in\":\""+created_in+"\",\"email\":\""+login_email+"\"," +
                "\"firstname\":\""+login_fname+"\",\"lastname\":\""+login_lname+"\",\"store_id\":"+store_id+"," +
                "\"website_id\":"+website_id+",\"addresses\":[{\"id\":"+address_id+",\"customer_id\":"+login_customer_id+"," +
                "\"region\":{\"region_code\":\""+""+"\",\"region\":\""+selected_franch_state+"\",\"region_id\":\""+selected_franch_state_code+"\"}," +
                "\"region_id\":\""+selected_franch_state_code+"\",\"country_id\":\""+address_country_id+"\"," +
                "\"street\":[\""+selected_franch_address+"\"],\"company\":\""+st_company+"\",\"telephone\":\""+address_telephone+"\"," +
                "\"postcode\":\""+selected_franch_pin+"\",\"city\":\""+selected_franch_city+"\",\"firstname\":\""+login_fname+"\"," +
                "\"lastname\":\""+login_lname+"\",\"default_shipping\":true,\"default_billing\":true}]," +
                "\"disable_auto_group_change\":0}}";
        //////Log.d("mRequestBody", mRequestBody);

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
                            //////Log.d("VOLLEY", response);
                            try {

                                object = new JSONObject(String.valueOf(response));
//                                CommonFun.alertError(UpdateAddressActivity.this,String.valueOf(response));

                                String id = object.getString("id");
                                //////Log.d("id",id);
                                JSONArray arr_address = object.getJSONArray("addresses");
                                int arr_address_length = arr_address.length();

                                for(int i = 0; i < arr_address_length; i++) {
                                    JSONObject address_obj = arr_address.getJSONObject(0);

                                    String add_id = address_obj.getString("id");
                                    //////Log.d("add_id",add_id);

                                    if (!add_id.equalsIgnoreCase("")) {
                                        Vibrator vibrator = (Vibrator) UpdateAddressActivity.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);

                                        final Dialog dialog = new Dialog(UpdateAddressActivity.this);
                                        dialog.setContentView(R.layout.custom_alert_dialog_design);
                                        TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
                                        tv_dialog.setText("Your address has been updated successfully...");
                                        ImageView image_view_dialog = (ImageView) dialog.findViewById(R.id.image_view_dialog);
                                        dialog.setCancelable(false);
                                        dialog.show();

                                        pref = CommonFun.getPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor=pref.edit();
                                        editor.putString("st_dist_id","");
                                        editor.putString("log_user_zone","");
                                        editor.commit();



                                        new CountDownTimer(4000, 4000) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                // TODO Auto-generated method stub

                                            }

                                            @Override
                                            public void onFinish() {
                                                // TODO Auto-generated method stub

                                                if(dialog.isShowing())
                                                    dialog.dismiss();

                                                if(is_req_from_checkout==false)
                                                    onResume();
                                                else
                                                {
                                                    Intent intent=new Intent(UpdateAddressActivity.this, AddressBook.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    CommonFun.finishscreen(UpdateAddressActivity.this);
                                                }
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

//                    //////Log.d("VOLLEY", error.toString());
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
            //////Log.d("error...", "Error");
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

                                int franch_details_arr_length = franch_details.length()+1;
                                spinner_data = new String[franch_details_arr_length];

                                arr_franchisee_address = new String[franch_details_arr_length];
                                arr_franchisee_city = new String[franch_details_arr_length];
                                arr_franchisee_state = new String[franch_details_arr_length];
                                arr_franchisee_pin = new String[franch_details_arr_length];
                                arr_franchisee_code = new String[franch_details_arr_length];
                                arr_franchisee_state_code = new String[franch_details_arr_length];

                                for(int i=0; i < franch_details_arr_length-1; i++){

                                    JSONObject object = franch_details.getJSONObject(i);

                                    st_franchisee_name = object.getString("franchisee_name");
                                    st_franchisee_code = object.getString("franchisee_code");
                                    st_franchisee_address = object.getString("franchisee_address");
                                    st_franchisee_state = object.getString("franchisee_state");
                                    st_franchisee_city = object.getString("franchisee_city");
                                    st_franchisee_pin = object.getString("franchisee_pin");
                                    st_franchisee_state_code = object.getString("franchisee_statecode");


                                    spinner_data[0] = "Select Franchisee";
                                    arr_franchisee_address[0] = "";
                                    arr_franchisee_pin[0] = "";
                                    arr_franchisee_state[0] = "";
                                    arr_franchisee_city[0] = "";
                                    arr_franchisee_state_code[0] = "";
                                    arr_franchisee_code[0] = "";



                                    if(!st_franchisee_code.equalsIgnoreCase(""))
                                        spinner_data[i+1] = st_franchisee_name + " (" + st_franchisee_code + ")";
                                    else {
                                        st_franchisee_name = st_franchisee_name.toUpperCase(Locale.ENGLISH);
                                        spinner_data[i+1] = st_franchisee_name;
                                    }
                                    arr_franchisee_address[i+1] = st_franchisee_address;
                                    arr_franchisee_pin[i+1] = st_franchisee_pin;
                                    arr_franchisee_state[i+1] = st_franchisee_state;
                                    arr_franchisee_city[i+1] = st_franchisee_city;
                                    arr_franchisee_state_code[i+1] = st_franchisee_state_code;
                                    arr_franchisee_code[i+1] = st_franchisee_code;


                                }



                                adapter = new ArrayAdapter<String>(UpdateAddressActivity.this,android.R.layout.simple_spinner_dropdown_item,spinner_data);
                                spinner_select_franch.setAdapter(adapter);

                                franch_state.setText("");
                                franch_address.setText("");
                                franch_pin.setText("");
                                franch_city.setText("");





                            } catch (JSONException e) {
                                e.printStackTrace();
                                //////Log.d("error",e.toString());

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

                                //////Log.d("error",st_alert_msg);

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
