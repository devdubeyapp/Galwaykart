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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.wishList.WishListDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.galwaykart.essentialClass.Global_Settings.api_url;

/**
 * Created by sumitsaini on 10/27/2017.
 * User can edit their already Saved addresses
 */

public class EditAddress extends BaseActivity{

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

    String st_add_id="",st_fName="",st_lName="",st_tel_no="",st_pin="",
            st_city="",st_new_tel="",st_new_postcode="",st_new_city="",st_new_fname="",st_new_lname="",
            st_new_address="",login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",region_code="",region="",region_id="",st_street="",
            login_email="",login_fname="",login_lname="",login_id="",login_group_id="",default_shipping="",created_at="",
            updated_at="",created_in="",store_id="",website_id="",disable_auto_group_change="",default_billing="",st_company="",
            selected_franch_pin="",st_default_shipping="";

    String st_update_address_URL = "",mRequestBody="",tokenData="";
    JSONObject object = null;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(EditAddress.this,UpdateAddressBook.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        pref = CommonFun.getPreferences(getApplicationContext());

        arrayList = new ArrayList<HashMap<String,String>>();
        spinner_state_profile = (Spinner)findViewById(R.id.spinner_state_profile);
//        chk_same_bill=(CheckBox)findViewById(R.id.chk_same);

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

        /*
        Data From Already added address API
         */

        st_add_id = pref.getString("selected_address_id","");
        st_fName = pref.getString("selected_address_fName","");
        st_lName = pref.getString("selected_address_lName","");
        st_tel_no = pref.getString("selected_address_tel_no","");
        st_pin = pref.getString("selected_address_pin","");
        st_city = pref.getString("selected_address_city","");
        st_street = pref.getString("selected_address_street","");
        tokenData = pref.getString("tokenData","");


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

        first_name.setText(st_fName);
        last_name.setText(st_lName);
        phone_no.setText(st_tel_no);
        street_address.setText(st_street);
        city.setText(st_city);
        zip.setText(st_pin);


        // get all states details
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
                    CommonFun.alertError(EditAddress.this,"Please fill mandatory fields...");



        }};


    private void updateAddress() {

        st_update_address_URL = api_url+"rest/V1/customers/me";
        address_country_id = "IN";

        mRequestBody ="{\"customer\":{\"id\":"+login_id+",\"group_id\":"+login_group_id+","+
                "\"default_billing\":\""+default_billing+"\",\"Default_shipping\":\""+default_shipping+"\"," +
                "\"created_at\":\""+created_at+"\",\"updated_at\":\""+updated_at+"\"," +
                "\"created_in\":\""+created_in+"\",\"email\":\""+login_email+"\"," +
                "\"firstname\":\""+login_fname+"\",\"lastname\":\""+login_lname+"\",\"store_id\":"+store_id+"," +
                "\"website_id\":"+website_id+",\"addresses\":[{\"id\":" + st_add_id +",\"customer_id\":"+login_id+"," +
                "\"region\":{\"region_code\":\""+region_code+"\",\"region\":\""+region+"\",\"region_id\":"+region_id+"}," +
                "\"region_id\":"+region_id+",\"country_id\":\""+address_country_id+"\"," +
                "\"street\":[\""+st_new_address+"\"],\"company\":\""+st_company+"\",\"telephone\":\""+st_new_tel+"\"," +
                "\"postcode\":\""+st_new_postcode+"\",\"city\":\""+st_new_city+"\",\"firstname\":\""+st_new_fname+"\"," +
                "\"lastname\":\""+st_new_lname+"\",\"default_shipping\":"+st_default_shipping+",\"default_billing\":true}]," +
                "\"disable_auto_group_change\":0}}";

        ////Log.d("mRequestBody", mRequestBody);

        pDialog = new TransparentProgressDialog(EditAddress.this);
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


//                            CommonFun.alertError(AddNewAddress.this, response.toString());
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
                                        Vibrator vibrator = (Vibrator) EditAddress.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);

                                        final Dialog dialog = new Dialog(EditAddress.this);
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

                                                Intent intent = new Intent(EditAddress.this,UpdateAddressBook.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                                startActivity(intent);
                                                CommonFun.finishscreen(EditAddress.this);

                                            }
                                        }.start();
                                    }


                                }

                            } catch (Exception e) {
                                //e.printStackTrace();
                                CommonFun.alertError(EditAddress.this, e.toString());


                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();

//                    ////Log.d("VOLLEY", error.toString());
//                    CommonFun.alertError(AddNewAddress.this, error.toString());
                    CommonFun.showVolleyException(error,EditAddress.this);

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


    @Override
    protected void onResume() {
        super.onResume();
        getState();
    }

    private void getState() {

        pDialog = new TransparentProgressDialog(EditAddress.this);
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


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditAddress.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
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

                    CommonFun.showVolleyException(error,EditAddress.this);
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
