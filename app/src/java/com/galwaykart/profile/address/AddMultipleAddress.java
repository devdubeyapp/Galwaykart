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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.BillingAddress;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.galwaykart.essentialClass.Global_Settings.api_url;

/**
 * Created by sumitsaini on 10/27/2017.
 * ADD New Billing/Shipping Address
 * user can save Multiple Address in their profile
 */

public class AddMultipleAddress extends BaseActivity{

    SharedPreferences pref;
    EditText first_name,last_name,company,phone_no,street_address,city,state,zip,country;
    Button button_save_address;
    TextView tv_title_address;

    ArrayList<HashMap<String, String>> arrayList;
    Spinner spinner_state_profile;
    String [] arr_state_name,arr_state_code,arr_region_id;

    final String TAG_region_id= "region_id";
    final String TAG_country_id = "country_id";
    final String TAG_code= "code";
    final String TAG_default_name= "default_name";

    CheckBox chk_same_bill;


    String st_get_State_URL="";
    TransparentProgressDialog pDialog;

    String  st_new_tel="",st_new_postcode="",st_new_city="",st_new_fname="",st_new_lname="",
            st_new_address="",login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",region_code="",region="",region_id="",st_coming_from="",
            login_email="",login_fname="",login_lname="",login_id="",login_group_id="",default_shipping="",created_at="",
            updated_at="",created_in="",store_id="",website_id="",disable_auto_group_change="",default_billing="",st_company="",
            st_add_new_address="",st_default_shipping="";

    String st_update_address_URL = "",mRequestBody="",tokenData="";
    JSONObject object = null;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(st_coming_from.equalsIgnoreCase("billing")) {
            Intent i = new Intent(AddMultipleAddress.this, BillingAddress.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            CommonFun.finishscreen(AddMultipleAddress.this);
        }
        else if(st_coming_from.equalsIgnoreCase("profile")) {
            Intent i = new Intent(AddMultipleAddress.this, UpdateAddressBook.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            CommonFun.finishscreen(AddMultipleAddress.this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        pref = CommonFun.getPreferences(getApplicationContext());

        arrayList = new ArrayList<HashMap<String,String>>();
        spinner_state_profile = (Spinner)findViewById(R.id.spinner_state_profile);
        chk_same_bill=(CheckBox)findViewById(R.id.chk_same);

        tv_title_address = (TextView)findViewById(R.id.tv_title_address);
        tv_title_address.setText("Billing Address");

        /*
        Data From Login API
         */
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
        tokenData = pref.getString("tokenData","");

        st_coming_from = pref.getString("coming_from","");


        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        company = (EditText)findViewById(R.id.company);
        phone_no = (EditText)findViewById(R.id.phone_no);
        street_address = (EditText)findViewById(R.id.street_address);
        city = (EditText)findViewById(R.id.city);

        zip = (EditText)findViewById(R.id.zip);
        country = (EditText)findViewById(R.id.country);
        button_save_address = (Button)findViewById(R.id.button_save_address);
        button_save_address.setOnClickListener(button_save_addressOnClickListener);


        st_get_State_URL = Global_Settings.api_url+"glaze/statelist.php";

        spinner_state_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                region_code = arr_state_code[(int) l];
                region_id = arr_region_id[(int)l];
                region = arr_state_name[(int) l];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    Button.OnClickListener button_save_addressOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            st_new_tel = phone_no.getText().toString();
            st_new_postcode = zip.getText().toString();
            st_new_city = city.getText().toString();
            st_new_fname = first_name.getText().toString();
            st_new_lname = last_name.getText().toString();
            st_new_address = street_address.getText().toString();
            st_company = company.getText().toString();

            if(chk_same_bill.isChecked() == true)
                st_default_shipping = "true";
            else
                st_default_shipping = "false";

            Boolean isValid = false;
            if ((!st_new_tel.equals("")) && (!st_new_postcode.equals("")) && (!st_new_city.equals("")) && (!st_new_fname.equals("")) &&
                    (!st_new_lname.equals("")) && (!st_new_address.equals("")))
                isValid = true;


            if(isValid == true){
                updateAddress();
            }
            else
                CommonFun.alertError(AddMultipleAddress.this,"Please fill mandatory fields...");



        }};


    /**
     * Update address
     */
    private void updateAddress() {

        st_add_new_address = api_url+"glaze/newaddress.php?id="+login_id +
                "&&fname="+st_new_fname +
                "&&lname="+st_new_lname +
                "&&postcode="+st_new_postcode+
                "&&city="+st_new_city +
                "&&state="+region_id +
                "&&telephone="+st_new_tel +
                "&&street="+st_new_address;

        ////Log.d("st_add_new_address",st_add_new_address);


        pDialog = new TransparentProgressDialog(AddMultipleAddress.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest=null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_add_new_address,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    CommonFun.alertError(AddMultipleAddress.this,response.toString());

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            String st_new_add_reg = jsonObject.getString("registration");

                            Vibrator vibrator = (Vibrator) AddMultipleAddress.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);

                            final Dialog dialog = new Dialog(AddMultipleAddress.this);
                            dialog.setContentView(R.layout.custom_alert_dialog_design);
                            TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
                            ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
                            tv_dialog.setText(st_new_add_reg);
                            dialog.show();

                            new CountDownTimer(2000,2000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub

                                    if(st_coming_from.equalsIgnoreCase("billing")) {
                                        Intent i = new Intent(AddMultipleAddress.this, BillingAddress.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(i);
                                        CommonFun.finishscreen(AddMultipleAddress.this);
                                    }
                                    else if(st_coming_from.equalsIgnoreCase("profile")) {
                                        Intent i = new Intent(AddMultipleAddress.this, UpdateAddressBook.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(i);
                                        CommonFun.finishscreen(AddMultipleAddress.this);
                                    }
                                }
                            }.start();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,AddMultipleAddress.this);
                    //CommonFun.alertError(AddNewAddress.this,error.toString());

                    // error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsObjRequest);



    }



    @Override
    protected void onResume() {
        super.onResume();
        getState();
    }

    /**
     * get all state list from api
     */
    private void getState() {

        pDialog = new TransparentProgressDialog(AddMultipleAddress.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest=null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_get_State_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray arr_get_state_obj = jsonObject.getJSONArray("statlist");
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

                                arr_state_name[i] = default_name;
                                arr_state_code[i] = code;
                                arr_region_id[i] = st_region_id;


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMultipleAddress.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
                            spinner_state_profile.setAdapter(adapter);




                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,AddMultipleAddress.this);
                    //CommonFun.alertError(AddNewAddress.this,error.toString());

                    // error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsObjRequest);




    }

}
