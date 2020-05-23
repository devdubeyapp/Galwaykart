package com.galwaykart.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.address_book.AddNewAddress;
import com.galwaykart.address_book.CustomerAddressBook;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.newsnotice.NoticeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditShippingAddress extends AppCompatActivity {



    //Code written on 20 May 2020 for edit shipping address


    private EditText first_name,last_name,phone_no,street_address,city,zip,country;


    public Bundle bundle;


    private String is_edit_address ="";
    private String address_type = "";
    private Button bt_shipping_address_edit_icon;
    private String st_ship_cust_fname = "";
    private String st_ship_cust_lname =  "";
    private String st_ship_cust_full_name =  "";
    private String st_ship_cust_telephone =  "";
    private String ship_email_id="";
    private String region= "";
    private String st_ship_postcode= "";
    private String st_ship_city= "";
    private String st_ship_street="";
    private String customer_ship_address_id="";
    private String ship_address_entity_id = "";
    private String ship_parent_id = "" ;
    private String region_code = "";
    private String region_id = "";
    private String ship_country_id ="";


    //Get and Set State
    private Spinner spinner_state_profile;
    private String st_get_State_URL="";
    private TransparentProgressDialog pDialog;

    ArrayList<HashMap<String, String>> arrayList;
    String [] arr_state_name, arr_state_code, arr_pin_state_id, arr_region_id;
    final String TAG_region_id= "region_id";
    final String TAG_country_id = "country_id";
    final String TAG_code= "code";
    final String TAG_default_name= "default_name";
    private String st_new_shp_state="";
    private String st_pin_state="";
    private String st_pin_state_id="";
    int state_pos;
    private String st_input_data ="";


    String tokenData="";
    String return_data="";
    SharedPreferences pref;
    String increment_id="";
    String order_grand_total="";


    private Button button_edit_address;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shipping_address);

        bundle = getIntent().getExtras();

        if (bundle != null){

            ship_address_entity_id = bundle.getString("ship_address_entity_id");

            st_ship_cust_fname = bundle.getString("st_ship_cust_fname");
            st_ship_cust_lname = bundle.getString("st_ship_cust_lname");
            st_ship_cust_telephone = bundle.getString("st_ship_cust_telephone");
            ship_email_id = bundle.getString("ship_email_id");
            st_ship_postcode = bundle.getString("st_ship_postcode");
            st_ship_city = bundle.getString("st_ship_city");
            st_new_shp_state = bundle.getString("st_ship_region");
            st_ship_street = bundle.getString("st_ship_street");

            Log.e("st_new_shp_state_on", st_new_shp_state);

            //below are extra parameter and it is not require for for edit shipping address
            customer_ship_address_id = bundle.getString("customer_ship_address_id");
            address_type = bundle.getString("address_type");
            ship_parent_id = bundle.getString("ship_parent_id");
            region_code = bundle.getString("ship_region_code");
            region_id = bundle.getString("ship_region_id");
            ship_country_id = bundle.getString("ship_country_id");

            increment_id=bundle.getString("increment_id");
            order_grand_total=bundle.getString("order_grand_total");
            Log.e("region_id_on_create", region_id);

        }

        Init();


    }


    void Init() {
        st_get_State_URL = Global_Settings.api_url+"rest/V1/m-state/statelist";

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        phone_no = findViewById(R.id.phone_no);
        street_address = findViewById(R.id.street_address);
        city = findViewById(R.id.city);
        zip = findViewById(R.id.zip);

        button_edit_address = findViewById(R.id.button_save_address);

        arrayList = new ArrayList<HashMap<String,String>>();
        spinner_state_profile = findViewById(R.id.spinner_state_profile);
        spinner_state_profile.setEnabled(false);

        spinner_state_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                region_code=arr_state_code[i];
                region_id=arr_region_id[i];
                region=arr_state_name[i];


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddressAndContinue();
            }
        });

        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    getStateCity(s);
                } else {

                }

            }
        });


        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });



        showExistingData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }



    private void goBack(){
        Intent intent = new Intent(EditShippingAddress.this, OrderDetails.class);
        intent.putExtra("increment_id",increment_id);
        intent.putExtra("order_grand_total",order_grand_total);
        startActivity(intent);
        CommonFun.finishscreen(EditShippingAddress.this);
    }


    private void showExistingData() {

        first_name.setText(st_ship_cust_fname);
        last_name.setText(st_ship_cust_lname);
        phone_no.setText(st_ship_cust_telephone);
        //zip.setHint(address_postcode);
        zip.setText(st_ship_postcode);
        city.setText(st_ship_city);
        street_address.setText(st_ship_street);
        getState();

    }

    private void getStateCity(Editable editable) {

        String st_get_state_city_URL = Global_Settings.galway_wcf_api_url+"Distributor.svc/GetPincodeDetails/"+editable+"/0";
        //Log.d("st_get_state_city_URL",st_get_state_city_URL);

        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);

            pDialog = new TransparentProgressDialog(EditShippingAddress.this);
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.GET, st_get_state_city_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            try {

                                if(response!=null)
                                {
                                    //Log.d("VOLLEYresponse", response.toString());
                                    JSONArray array = new JSONArray(response);

                                    arr_pin_state_id = new String[array.length()];
                                    for(int i = 0; i< array.length(); i++){

                                        JSONObject object = array.getJSONObject(i);
                                        st_ship_city = object.getString("City");
                                        ship_country_id = object.getString("Capital");
                                        //region = object.getString("State");
                                        st_pin_state = object.getString("State");
                                        st_pin_state_id = object.getString("statecode");

                                        arr_pin_state_id[i] = st_pin_state_id;
                                    }

                                    setStateCityDefault();
                                }


                            } catch (Exception e) {
                                //e.printStackTrace();

                                //CommonFun.alertError(AddNewAddress.this,e.toString());
                                Intent intent=new Intent(EditShippingAddress.this, ExceptionError.class);
                                startActivity(intent);

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(pDialog.isShowing())
                        pDialog.dismiss();
//                        Log.d("VOLLEY", error.toString());
//                        CommonFun.alertError(AddNewAddress.this,error.toString());

                    CommonFun.showVolleyException(error,EditShippingAddress.this);

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

                    return st_input_data == null ? null : st_input_data.getBytes(StandardCharsets.UTF_8);
                    //return new byte[0];
                }


            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.d("error...","Error");

        }
    }

    private void setStateCityDefault() {
        if(!st_pin_state_id.equalsIgnoreCase("null")) {
            for (int i = 0; i < arr_state_code.length; i++) {
                if (st_pin_state_id.equalsIgnoreCase(arr_region_id[i])) {

                    state_pos = i;
                }
            }
            Log.e("state_pos", "" + state_pos);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditShippingAddress.this, android.R.layout.simple_spinner_dropdown_item, arr_state_name);
            spinner_state_profile.setAdapter(adapter);

            spinner_state_profile.setSelection(state_pos);
            spinner_state_profile.setEnabled(false);
//            spinner_state_profile.setEnabled(true);


        }
        else{
            //zip.setText("");
            spinner_state_profile.setEnabled(true);
            //Toast.makeText(this,"Service is not available in this pincode",Toast.LENGTH_LONG).show();
        }

    }

    private void getState() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest=null;



        try {
            jsObjRequest = new StringRequest(Request.Method.GET, st_get_State_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
                            //Log.d("st_get_State_URL",response.toString());

                            if (response != null) {
                                try {

                                    JSONArray jsonArray=new JSONArray(response);

                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    JSONArray arr_get_state_obj = jsonObject.getJSONArray("statelist");

                                    int order_list_length = arr_get_state_obj.length();

                                    arr_state_name = new String[order_list_length];
                                    arr_state_code = new String[order_list_length];
                                    arr_region_id = new String[order_list_length];

                                    for(int i=0; i<order_list_length; i++) {

                                        JSONObject  order_list_obj = arr_get_state_obj.getJSONObject(i);

                                        String st_region_id = order_list_obj.getString(TAG_region_id);
                                        String  country_id = order_list_obj.getString(TAG_country_id);
                                        String code = order_list_obj.getString(TAG_code);
                                        String default_name = order_list_obj.getString(TAG_default_name);

                                        int region_code = Integer.parseInt(st_region_id);
                                        arr_state_name[i] = default_name;
                                        arr_state_code[i] = code;
                                        if(region_code < 10)
                                            arr_region_id[i] = "0"+st_region_id;
                                        else
                                            arr_region_id[i] = st_region_id;


                                    }




                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditShippingAddress.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
                                    spinner_state_profile.setAdapter(adapter);

                                    //Log.d("itemdata",new_state);


                                    if (st_new_shp_state != null && !st_new_shp_state.equals("")){
                                        //  Log.d("item_position1312312",""+item_position);
                                        spinner_state_profile.setSelection(adapter.getPosition(st_new_shp_state));

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // Intent intent=new Intent(AddNewAddress.this, ExceptionError.class);
                                    //  startActivity(intent);
                                }
                            }


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,EditShippingAddress.this);
                    //CommonFun.alertError(AddNewAddress.this,error.toString());

                    // error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);




    }



    private void saveAddressAndContinue(){

        String st_tel = phone_no.getText().toString();
        String st_postcode = zip.getText().toString();
        String st_city = city.getText().toString();
        String st_fname = first_name.getText().toString();
        String st_lname = last_name.getText().toString();
        String st_address = street_address.getText().toString();
        String st_state = spinner_state_profile.getSelectedItem().toString();
        int item_position = spinner_state_profile.getSelectedItemPosition();

        int pin_code_length = st_postcode.length();
        int mobile_no_length = st_tel.length();


        Boolean isValid = false;
        Boolean valid_pin = false;
        if ((!st_tel.equals("")) && (!st_postcode.equals("")) && (!st_city.equals("")) && (!st_fname.equals("")) &&
                (!st_lname.equals("")) && (!st_address.equals("")) ) {
            if ((pin_code_length == 6 && mobile_no_length == 10)) {
                valid_pin = true;
                isValid = true;
            }
        }

        pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");


        //tokenData = "1y0kfustfzns74r3rtmvl4l86m8v6zsi";

        if(isValid == true) {
            if (valid_pin == true) {

                String email = pref.getString("user_email", "");

                String save_address_url = Global_Settings.api_url + "rest/V1/m-orders/address/update";
                return_data="";

                if (st_ship_cust_telephone != null && !st_ship_cust_telephone.equals("")){

                }else{
                    st_ship_cust_telephone = phone_no.getText().toString().trim();
                }


                String input_data = "{\"shipping_address\":{\"address_type\":\"shipping\"," +
                        "\"city\":\""+city.getText().toString().trim()+"\"," +
                        "\"country_id\":\"IN\"," +
                        "\"customer_address_id\":"+customer_ship_address_id+"," +
                        "\"email\":\""+ship_email_id+"\"," +
                        "\"entity_id\":"+ship_address_entity_id+"," +
                        "\"firstname\":\""+first_name.getText().toString().trim()+"\"," +
                        "\"lastname\":\""+last_name.getText().toString().trim()+"\"," +
                        "\"parent_id\":"+ship_parent_id+"," +
                        "\"postcode\":\""+zip.getText().toString().trim()+"\"," +
                        "\"region\":\""+region+"\"," +
                        "\"region_code\":\""+region_code+"\"," +
                        "\"region_id\":\""+region_id+"\"," +
                        "\"street\":[\""+street_address.getText().toString().trim().replaceAll("\n", " ")+"\"]," +
                        "\"telephone\":\""+phone_no.getText().toString().trim()+"\"}}";


                Log.e("input_data",input_data);

                pDialog = new TransparentProgressDialog(EditShippingAddress.this);
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.show();

                try {

                    RequestQueue requestQueue = Volley.newRequestQueue(this);

                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, save_address_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                        Log.e("response",response);
                                    Intent intent = new Intent(EditShippingAddress.this, OrderDetails.class);
                                        intent.putExtra("increment_id",increment_id);
                                        intent.putExtra("order_grand_total",order_grand_total);
                                        startActivity(intent);
                                        CommonFun.finishscreen(EditShippingAddress.this);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            //CommonFun.alertError(EditShippingAddress.this,error.toString());
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                            CommonFun.showVolleyException(error, EditShippingAddress.this);
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
                            return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
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
            else {
                CommonFun.alertError(EditShippingAddress.this, "Must enter proper mobile no or pincode");
            }
        }
        else {
            CommonFun.alertError(EditShippingAddress.this, "Must enter all required fields");
        }



    }


}

