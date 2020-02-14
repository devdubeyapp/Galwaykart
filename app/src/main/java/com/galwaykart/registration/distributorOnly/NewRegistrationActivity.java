package com.galwaykart.registration.distributorOnly;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.registration.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sumitsaini on 10/10/2017.
 */

public class NewRegistrationActivity extends AppCompatActivity {


    Spinner spinner_select_franch;
    String [] spinner_data = {};
    ArrayAdapter<String> adapter;
    RelativeLayout rel_layout1,rel_layout2,rel_layout3,rel_layout4;
    Button button_sign_up,button_proceed,button_get_otp,button_Submit;
    EditText dist_id,first_name,last_name,email,password,confirm_password,otp,phone_no;
    String st_Get_Franch_Details_URL = "";
    String st_franchisee_name = "",st_franchisee_code="",st_franchisee_address="",st_franchisee_state="",st_franchisee_city="",st_alert_msg="",st_franchisee_pin="";
    TransparentProgressDialog pDialog;
    JSONArray franch_details = null;
    int  otp_random_no;
    String st_entered_otp="",st_coming_from="";
    String st_group_id="";
    TimerTask timerTask;
    SharedPreferences pref;
    String st_email = "",st_first_name="",st_last_name="",st_password="",st_comfirm_pwd="",st_dibid="",selected_franch_stateCode="";

    String st_Save_User_URL = "",st_input_data="";
    private Pattern pattern;
    private Matcher matcher;

    EditText franch_address,franch_state,franch_city,franch_pin;

    String [] arr_franchisee_address,arr_franchisee_state,arr_franchisee_city,arr_franchisee_pin,arr_franchisee_stateCode,arr_franchisee_fcode;

    String selected_franch_code="";
    //Button button_sign_up;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(NewRegistrationActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_for_distributor);
        //     initNavigationDrawer();
        pref = CommonFun.getPreferences(getApplicationContext());
        button_sign_up= findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    callRegistration();
            }
        });

        spinner_select_franch = findViewById(R.id.spinner_select_franch);
        franch_pin = findViewById(R.id.franch_pin);
        franch_address = findViewById(R.id.franch_address);
        franch_state = findViewById(R.id.franch_state);
        franch_city = findViewById(R.id.franch_city);

        st_Get_Franch_Details_URL = Global_Settings.galway_api_url+"returnAPi/Load_franchisee_details";

        st_dibid = pref.getString("st_dist_id","");
        ////Log.d("st_dibid",st_dibid);

        getFranchDetails();

    }

    String input_data="";

    private void callRegistration(){

        final SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());


//        editor.putString("user_email",st_email);
//        editor.putString("user_password",st_password);
//        editor.putString("login_fname",st_first_name);
//        editor.putString("login_lname",st_last_name);
//        editor.putString("login_phone",phone_no.getText().toString());
        //franch_address,franch_state,franch_city,franch_pin
//
//        String register_api_url=Global_Settings.api_custom_url+ "registeration.php?" +
//                                                    "email=" + +
//                                                    "&fname=" ++
//                                                    "&lname=" + +
//                                                    "&customertype=4" +
//                                                    "&dob=" +
//                                                    "&password=" + pref.getString("user_password","")+
//                                                    "&postcode=" + franch_pin.getText().toString()+
//                                                    "&city=" +franch_city.getText().toString().toString()+
//                                                    "&state=" + franch_state.getText().toString().trim()+
//                                                    "&telephone=" + pref.getString("login_phone","")+
//                                                    "&street="+franch_address.getText().toString().trim();
//

        String register_api_url=Global_Settings.api_url+"/rest/V1/customer/registration";

//       final HashMap<String, String> headers = new HashMap<>();
//
//        headers.put("email", pref.getString("user_email",""));
//        headers.put("fname", pref.getString("login_fname",""));
//        headers.put("lname", pref.getString("login_lname",""));
//        headers.put("customertype", pref.getString("user_password",""));
//        headers.put("dob", "");
//        headers.put("password", pref.getString("user_password",""));
//        headers.put("postcode", franch_pin.getText().toString());
//        headers.put("city", franch_city.getText().toString());
//        headers.put("state", franch_state.getText().toString().trim());
//        headers.put("telephone", pref.getString("login_phone",""));
//        headers.put("street", franch_address.getText().toString().trim());
//        headers.put("dibid",st_dibid);

        ////Log.d("fcode reg",st_franchisee_code);



        ////Log.d("inputsubmit",selected_franch_code);
//        headers.put("email", pref.getString("user_email",""));
//        headers.put("fname", pref.getString("login_fname",""));
//        headers.put("lname", pref.getString("login_lname",""));
//        headers.put("customertype","4");
//        headers.put("dob", "");
//        headers.put("password", pref.getString("user_password",""));
//        headers.put("postcode", franch_pin.getText().toString());
//        headers.put("city", franch_city.getText().toString());
//        headers.put("state", selected_franch_stateCode);
//        headers.put("telephone", pref.getString("login_phone",""));
//        headers.put("street", franch_address.getText().toString().trim());
//        headers.put("dibid",st_dibid.toUpperCase());
//        headers.put("fcode",selected_franch_code);

        input_data="{" +
                "\"dibid\":\""+ st_dibid.toUpperCase()  +"\"," +
                "\"telephone\":\""+ pref.getString("login_phone","") +"\"," +
                "\"email\":\""+pref.getString("user_email","") +"\"," +
                "\"customertype\":\""+"4" +"\"," +
                "\"fname\":\""+ pref.getString("login_fname","") +"\"," +
                "\"lname\":\""+ pref.getString("login_lname","")+"\"," +
                "\"password\":\""+pref.getString("user_password","") +"\""+
                "}";

        pDialog = new TransparentProgressDialog(NewRegistrationActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST, register_api_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    ////Log.d("submitresponse", String.valueOf(response));

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){


                                try {
                                           JSONObject jsonObj = new JSONObject(response);
                                            String status_msg=jsonObj.getString("registration");
                                            if(status_msg.contains("successfully"))
                                            {

                                                SharedPreferences.Editor editor=pref.edit();
                                                //editor.putString("user_email", "Confirmed");
                                                //editor.putString("onetime", "Confirmed");

                                                editor.commit();


                                                Vibrator vibrator = (Vibrator) NewRegistrationActivity.this.getSystemService(RegistrationActivity.VIBRATOR_SERVICE);
                                                vibrator.vibrate(100);

                                                final Dialog dialog = new Dialog(NewRegistrationActivity.this);
                                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                                TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                                ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                                tv_dialog.setText("Registered Successfully..Happy Shopping..");
                                                dialog.show();


                                                timerTask=new TimerTask() {
                                                    @Override
                                                    public void run() {


                                                        Intent intent=new Intent(NewRegistrationActivity.this, LoginActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        CommonFun.finishscreen(NewRegistrationActivity.this);



                                                    }};


                                                Timer timer=new Timer();
                                                timer.schedule(timerTask,4500);




                                            }
                                            else
                                            {
                                                CommonFun.alertError(NewRegistrationActivity.this,status_msg);
                                            }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

//                                ////Log.d("error",st_alert_msg);
//
//                                CommonFun.alertError(NewRegistrationActivity.this,st_alert_msg);

                        }
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

//                CommonFun.alertError(RegistrationActivity.this,error.toString());
//                error.printStackTrace();

                CommonFun.showVolleyException(error,NewRegistrationActivity.this);

            }
        }){

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
                //return new byte[0];
            }


        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsObjRequest);



    }





    @Override
    protected void onResume() {
        super.onResume();



        spinner_select_franch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected_franch_address = arr_franchisee_address[position];
                String selected_franch_pin = arr_franchisee_pin[position];
                String selected_franch_city= arr_franchisee_city[position];
                String selected_franch_state = arr_franchisee_state[position];
                selected_franch_stateCode = arr_franchisee_stateCode[position];

                selected_franch_code=arr_franchisee_fcode[position];

                franch_address.setText(selected_franch_address);
                franch_state.setText(selected_franch_state);
                franch_city.setText(selected_franch_city);
                franch_pin.setText(selected_franch_pin);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void getFranchDetails() {

        pDialog = new TransparentProgressDialog(NewRegistrationActivity.this);
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

                                franch_details = new JSONArray(response);
                                int franch_details_arr_length = franch_details.length();
                                spinner_data = new String[franch_details_arr_length];
                                arr_franchisee_address = new String[franch_details_arr_length];
                                arr_franchisee_city = new String[franch_details_arr_length];
                                arr_franchisee_state = new String[franch_details_arr_length];
                                arr_franchisee_pin = new String[franch_details_arr_length];
                                arr_franchisee_stateCode = new String[franch_details_arr_length];
                                arr_franchisee_fcode=new String[franch_details_arr_length];


                                for(int i=0; i < franch_details_arr_length; i++){

                                    JSONObject object = franch_details.getJSONObject(i);

                                    st_franchisee_name = object.getString("franchisee_name");
                                    st_franchisee_code = object.getString("franchisee_code");
                                    st_franchisee_address = object.getString("franchisee_address");
                                    st_franchisee_state = object.getString("franchisee_state");
                                    st_franchisee_city = object.getString("franchisee_city");
                                    st_franchisee_pin = object.getString("franchisee_pin");
                                    String st_franchisee_stateCode = object.getString("franchisee_statecode");

                                    if(!st_franchisee_code.equalsIgnoreCase(""))
                                        spinner_data[i] = st_franchisee_name + " (" + st_franchisee_code + ")";
                                    else {
                                        st_franchisee_name = st_franchisee_name.toUpperCase(Locale.ENGLISH);
                                        spinner_data[i] = st_franchisee_name;
                                    }

                                    arr_franchisee_address[i]=st_franchisee_address;
                                    arr_franchisee_pin[i]=st_franchisee_pin;
                                    arr_franchisee_state[i]=st_franchisee_state;
                                    arr_franchisee_city[i]=st_franchisee_city;
                                    arr_franchisee_stateCode[i]=st_franchisee_stateCode;
                                    arr_franchisee_fcode[i]=st_franchisee_code;

                                }



                                adapter = new ArrayAdapter<String>(NewRegistrationActivity.this,android.R.layout.simple_spinner_dropdown_item,spinner_data);
                                spinner_select_franch.setAdapter(adapter);




                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());

                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(response);
                                    if(jsonObj.has("Message"))
                                    {
                                        st_alert_msg = jsonObj.getString("Message");
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                ////Log.d("error",st_alert_msg);

                                CommonFun.alertError(NewRegistrationActivity.this,st_alert_msg);
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

                CommonFun.showVolleyException(error,NewRegistrationActivity.this);

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
