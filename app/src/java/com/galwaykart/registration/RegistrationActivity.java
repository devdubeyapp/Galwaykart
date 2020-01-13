package com.galwaykart.registration;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.registration.distributorOnly.NewRegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sumitsaini on 9/20/2017.
 * Modified by Ankesh Kumar
 */

public class RegistrationActivity extends AppCompatActivity {




    Spinner spinner_customer_type;
    //String [] spinner_data = {"Customer","Distributor","I Know Distributor","Employee"};
    String [] spinner_data = {"Distributor"};
    ArrayAdapter<String> adapter;
    RelativeLayout rel_layout1,rel_layout2,rel_layout3,rel_layout4;
    Button button_sign_up,button_proceed,button_get_otp,button_Submit;
    EditText dist_id,first_name,last_name,email,password,confirm_password,otp,phone_no;
    String st_Get_Dist_details_URL = "",st_get_otp_URL="",st_Get_Employee_Details_URL="",st_verify_dist_id="";
    String st_dist_id = "",st_Fname="",st_Lname="",st_emailId="",st_phone_no="",st_alert_msg="",st_emp_code="";
    TransparentProgressDialog pDialog;
    JSONArray dist_details = null;
    int  otp_random_no;
    String st_entered_otp="",st_coming_from="";
    String st_group_id="";
    TimerTask timerTask;
    SharedPreferences pref;
    String st_email = "",st_first_name="",st_last_name="",st_password="",st_comfirm_pwd="";
RelativeLayout rlayout_msg;
    String st_Save_User_URL = "",st_input_data="";
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN ="((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_.]).{8,20})";

    ImageView iv_close_instruction;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


      st_group_id="4";
   //     initNavigationDrawer();
      pref = CommonFun.getPreferences(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner_customer_type = (Spinner)findViewById(R.id.spinner_customer_type);
        rel_layout1 = (RelativeLayout)findViewById(R.id.rel_layout1);
        rel_layout2 = (RelativeLayout)findViewById(R.id.rel_layout2);
        rel_layout3 = (RelativeLayout)findViewById(R.id.rel_layout3);
        rel_layout4 = (RelativeLayout)findViewById(R.id.rel_layout4);
        rlayout_msg = (RelativeLayout)findViewById(R.id.rlayout_msg);
        /*
         Last time changes : only for Distributor...
        */

        rel_layout3.setVisibility(View.VISIBLE);
        rel_layout1.setVisibility(View.GONE);
        rel_layout2.setVisibility(View.GONE);
        rlayout_msg.setVisibility(View.GONE);

        button_sign_up = (Button)findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(button_sign_upOnClickListener);

        button_proceed = (Button)findViewById(R.id.button_proceed);
        button_proceed.setOnClickListener(button_proceedOnClickListener);

        button_get_otp = (Button)findViewById(R.id.button_get_otp);
        button_get_otp.setOnClickListener(button_get_otpOnClickListener);

        button_Submit = (Button)findViewById(R.id.button_Submit);
        button_Submit.setOnClickListener(button_SubmitOnClickListener);

        dist_id = (EditText)findViewById(R.id.dist_id);
        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        otp =  (EditText)findViewById(R.id.otp);
        phone_no = (EditText)findViewById(R.id.phone_no);

        /**
         * close instruction
         */

        iv_close_instruction=(ImageView)findViewById(R.id.iv_close_instruction);
        iv_close_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlayout_msg.setVisibility(View.GONE);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {


            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

                rlayout_msg.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

     password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
         @Override
         public void onFocusChange(View view, boolean b) {
             if(b==false)
                 rlayout_msg.setVisibility(View.GONE);
         }
     });



        adapter = new ArrayAdapter<String>(RegistrationActivity.this,android.R.layout.simple_spinner_dropdown_item,spinner_data);
        spinner_customer_type.setAdapter(adapter);

        /**
         * Customer selection spinner
         */

        spinner_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
//                        rel_layout2.setVisibility(View.VISIBLE);
//                        st_group_id = "1";
                        phone_no.setEnabled(true);

                        rel_layout1.setVisibility(View.GONE);
                        rel_layout2.setVisibility(View.GONE);
                        rel_layout3.setVisibility(View.VISIBLE);
                        st_group_id = "1";

                        st_coming_from = "Distributor";
                        break;
                    case 1:
                        rel_layout1.setVisibility(View.GONE);
                        rel_layout2.setVisibility(View.GONE);
                        rel_layout3.setVisibility(View.VISIBLE);
                        st_group_id = "4";

                        st_coming_from = "Distributor";
                        break;
                    case 2:
                        rel_layout1.setVisibility(View.GONE);
                        rel_layout2.setVisibility(View.GONE);
                        rel_layout3.setVisibility(View.VISIBLE);
                        st_coming_from = "I Know Distributor";
                        phone_no.setEnabled(true);
                        st_group_id = "3";
                        break;

                    case 3:
                        rel_layout1.setVisibility(View.GONE);
                        rel_layout2.setVisibility(View.GONE);
                        rel_layout3.setVisibility(View.VISIBLE);
                        st_group_id = "4";

                        st_coming_from = "Employee";
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    Button.OnClickListener button_SubmitOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            st_entered_otp = otp.getText().toString();

            if(st_entered_otp.equalsIgnoreCase(String.valueOf(otp_random_no))){

                rel_layout2.setVisibility(View.VISIBLE);
                first_name.setText(st_Fname);
                last_name.setText(st_Lname);
                email.setText(st_emailId);
                phone_no.setText(st_phone_no);
                phone_no.setEnabled(false);
                rel_layout4.setVisibility(View.GONE);


                if(st_group_id.equalsIgnoreCase("4")) {

                    st_Lname=st_Lname.trim();
                    first_name.setEnabled(false);
                    if(st_Lname.equals(""))
                        last_name.setEnabled(true);
                    else
                        last_name.setEnabled(false);

                    email.requestFocus();



                }
            }
            else
            {
                CommonFun.alertError(RegistrationActivity.this,"OTP not matched...Try Again !!!");
            }


        }};

    Button.OnClickListener button_get_otpOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            /**
             * send otp from api
             */
            sendOtpViaAPI();

        }};

    Button.OnClickListener button_proceedOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

//            if(st_coming_from.equalsIgnoreCase("Distributor")) {

                st_dist_id = dist_id.getText().toString();
                if(!st_dist_id.equalsIgnoreCase("")) {
                    st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_Guest?ID=" + st_dist_id;


                    /**
                     * Get distributor details
                      */
                    getDistributorDetails();

                }
                else
                 CommonFun.alertError(RegistrationActivity.this,"Enter distributor ID first...");
//            }
//            else if(st_coming_from.equalsIgnoreCase("I Know Distributor")) {
//
//                st_dist_id = dist_id.getText().toString();
//                if(!st_dist_id.equalsIgnoreCase("")) {
//                    st_Get_Dist_details_URL = "http://it.galway.in/returnapi/Load_Guest?ID=" + st_dist_id;
//                    getDistributorDetails();
//                }
//                else
//                    CommonFun.alertError(RegistrationActivity.this,"Enter distributor ID first...");
//            }
//
//            else if(st_coming_from.equalsIgnoreCase("Employee")) {
//
//                st_emp_code = dist_id.getText().toString();
//                if(!st_emp_code.equalsIgnoreCase("")) {
//
//                    st_Get_Employee_Details_URL = "http://it.galway.in/returnapi/Employee_Details?Emp_Code="+st_emp_code;
//                    getEmployeeDetails();
//                }
//                else
//                    CommonFun.alertError(RegistrationActivity.this,"Enter employee code first...");
//            }



        }



    };

    /**
     * Register new  user
     */

    Button.OnClickListener button_sign_upOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            st_email= email.getText().toString();
            st_first_name= first_name.getText().toString();
            st_last_name= last_name.getText().toString();
            st_password= password.getText().toString();
            st_comfirm_pwd = confirm_password.getText().toString();

            boolean email_id_format = android.util.Patterns.EMAIL_ADDRESS.matcher(st_email).matches();


            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(st_password);
            boolean password_format = matcher.matches();
            ////Log.d("password_format",""+password_format);

            if(email_id_format == true) {
                if (password_format == true) {
                    if (!st_email.equalsIgnoreCase("") && !st_first_name.equalsIgnoreCase("") &&
                            !st_last_name.equalsIgnoreCase("") && !st_password.equalsIgnoreCase("")) {

                        if (st_comfirm_pwd.equals(st_password)) {

                            // Registration for Distributor

                            if(st_group_id.equalsIgnoreCase("4")) {

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_email",st_email);
                                editor.putString("user_password",st_password);
                                editor.putString("login_fname",st_first_name);
                                editor.putString("login_lname",st_last_name);
                                editor.putString("login_phone",phone_no.getText().toString());
                                editor.commit();

                                Intent intent = new Intent(RegistrationActivity.this, NewRegistrationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CommonFun.finishscreen(RegistrationActivity.this);
                            }
                            else
                            {
                                registerUser();
                            }

                        } else
                            CommonFun.alertError(RegistrationActivity.this, "Password not matched..Try Again..");
                    } else
                        CommonFun.alertError(RegistrationActivity.this, "Fill mandatory fields first...");
                }
                else
                    CommonFun.alertError(RegistrationActivity.this, "अपने पासवर्ड मे कोई \n" +
                            "एक विशेष अक्षर जैसे (@#$%_.) \n" +
                            "एक अक्षर नंबर जैसे 0-9, और \n" +
                            "कम से कम 8 अक्षर, और \n"+
                            "एक बड़ा अक्षर जैसे A-Z ज़रूर प्रयोग करे ");
                    //CommonFun.alertError(RegistrationActivity.this, "Password must contain atleast 8 character,one special character,one Upper case and a number ");
            }
            else
                CommonFun.alertError(RegistrationActivity.this, "Incorrect Email ID...");

        }
    };

    /**
     * Register new user
     */

    private void registerUser() {



        st_Save_User_URL = Global_Settings.api_url+"rest/V1/customers/";

        st_input_data ="{\"customer\":{\"email\":\""+st_email+"\"," +
                "\"firstname\":\""+st_first_name+"\"," +
                "\"lastname\":\""+st_last_name+"\"," +
                "\"group_id\":\""+st_group_id+"\"}," +
                "\"password\":\""+st_password+"\"}";

        ////Log.d("st_input_data",st_input_data);


        pDialog = new TransparentProgressDialog(RegistrationActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.POST, st_Save_User_URL,new JSONObject(st_input_data),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();


//                            CommonFun.alertError(RegistrationActivity.this,response.toString());
                            if(response!=null){
                                try {
                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                    String st_id = jsonObj.getString("id");
                                    String st_group_id = jsonObj.getString("group_id");
                                    String st_created_at = jsonObj.getString("created_at");
                                    String st_updated_at = jsonObj.getString("updated_at");
                                    String st_email = jsonObj.getString("email");
                                    String st_firstname = jsonObj.getString("firstname");
                                    String st_lastname = jsonObj.getString("lastname");



                                    if(!st_id.equalsIgnoreCase("")){

                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("user_email",st_email);
                                        editor.putString("user_password",st_password);
                                        editor.putString("login_fname",st_firstname);
                                        editor.putString("login_lname",st_lastname);
                                        editor.commit();

//                                        CommonFun.alertError(RegistrationActivity.this,"Registered successfully...Happy Shopping...");

                                        Vibrator vibrator = (Vibrator) RegistrationActivity.this.getSystemService(RegistrationActivity.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);

                                        final Dialog dialog = new Dialog(RegistrationActivity.this);
                                        dialog.setContentView(R.layout.custom_alert_dialog_design);
                                        TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
                                        ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
                                        tv_dialog.setText("Registered Successfully..Happy Shopping..");
                                        dialog.show();


                                        timerTask=new TimerTask() {
                                            @Override
                                            public void run() {

                                                Intent i = new Intent(RegistrationActivity.this, SplashActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                CommonFun.finishscreen(RegistrationActivity.this);
                                            }};


                                        Timer timer=new Timer();
                                        timer.schedule(timerTask,6000);

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(pDialog.isShowing())
                        pDialog.dismiss();

//                    NetworkResponse response = error.networkResponse;
//                    if (error instanceof ServerError && response != null) {
//                        try {
//                            String res = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                            // Now you can use any deserializer to make sense of data
//                            JSONObject obj = new JSONObject(res);
//                        } catch (UnsupportedEncodingException e1) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (JSONException e2) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//                    }

                //    error.printStackTrace();

                   CommonFun.alertError(RegistrationActivity.this,"User already registered with Galwaykart");

                 //   CommonFun.showVolleyException(error,RegistrationActivity.this);
                }

            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }


            };
        } catch (JSONException e) {
            e.printStackTrace();
        }



        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);
    }


    /**
     * Get distributor details from sales
     */
    private void getDistributorDetails() {


        st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_verify_guest?ID="+st_dist_id;
        ////Log.d("st_Get_Dist_details_URL",st_Get_Dist_details_URL);

        pDialog = new TransparentProgressDialog(RegistrationActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Dist_details_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){
                            try {

                                dist_details = new JSONArray(String.valueOf(response));
                                JSONObject dist_details_object =dist_details.getJSONObject(0);

                                    st_Fname = dist_details_object.getString("FName");
                                    st_Lname = dist_details_object.getString("LName");
                                    st_emailId = dist_details_object.getString("EmailID");
                                    st_phone_no = dist_details_object.getString("PhoneNo");

                                ////Log.d("st_Fname",st_Fname);
                                ////Log.d("st_alert_msg",st_alert_msg);

                                st_dist_id = dist_id.getText().toString();

                                pref = CommonFun.getPreferences(getApplicationContext());

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("st_dist_id",st_dist_id);
                                editor.commit();




//                                if(st_coming_from.equalsIgnoreCase("Distributor")) {


                                verifyDistID();

                                  // sendOtp();

//                                }
//                                else if(st_coming_from.equalsIgnoreCase("I Know Distributor")) {
//
//                                    rel_layout2.setVisibility(View.VISIBLE);
//                                    rel_layout3.setVisibility(View.GONE);
//
//                                }

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

                                CommonFun.alertError(RegistrationActivity.this,st_alert_msg);
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

                CommonFun.showVolleyException(error,RegistrationActivity.this);

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
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

    /**
     * Verify user already register on galwaykart
     */
//
//    private void verifyDistID() {
//
//        st_verify_dist_id = Global_Settings.api_url+"glaze/dib_verify.php?id="+st_dist_id;
//        ////Log.d("st_verify_dist_id","");
//
//
//        pDialog = new TransparentProgressDialog(RegistrationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_verify_dist_id,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        JSONObject jsonObj1 = null;
//                        if(response!=null){
//                            try {
//
//                                jsonObj1 = new JSONObject(String.valueOf(response));
//
//                                String dist_verification = jsonObj1.getString("distributor_verify");
//
////                                rel_layout4.setVisibility(View.VISIBLE);
////                                    rel_layout3.setVisibility(View.GONE);
////                                sendOtp();
//                                if(dist_verification.equalsIgnoreCase("1")) {
//                                    rel_layout4.setVisibility(View.VISIBLE);
//                                    rel_layout3.setVisibility(View.GONE);
//                                    sendOtpViaAPI();
//                                }
//                                else
//                                    CommonFun.alertError(RegistrationActivity.this,"Your ID is already registered...");
//
//
//
//
//
//                                //}
////                                else if(st_coming_from.equalsIgnoreCase("I Know Distributor")) {
////
////                                    rel_layout2.setVisibility(View.VISIBLE);
////                                    rel_layout3.setVisibility(View.GONE);
////
////                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                ////Log.d("error",e.toString());
//
//                                ////Log.d("error",st_alert_msg);
//
//                                CommonFun.alertError(RegistrationActivity.this,st_alert_msg);
//                            }
//                        }
//                    }
//
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
////                CommonFun.alertError(RegistrationActivity.this,error.toString());
////                error.printStackTrace();
//
//                CommonFun.showVolleyException(error,RegistrationActivity.this);
//
//            }
//        }){
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//
//        };
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        jsObjRequest.setShouldCache(false);
//        queue.add(jsObjRequest);
//
//
//
//    }
//


//    private void sendOtp() {
//
//        Random rand = new Random();
//
//        otp_random_no = rand.nextInt(9999) + 1000;
//
//        ////Log.d("otp_random_no",""+otp_random_no);
//        //st_phone_no = "";
//
//
//        String st_text_msg = "Your verification code is "+otp_random_no+". Please enter it and continue your shopping from Galway."+"\n"+"Best regards-Galway";
//
//        st_get_otp_URL= "http://bhashsms.com/api/sendmsg.php?user=Galway&pass=P@nas0n1C&sender=GALWAY&phone="
//                +st_phone_no+"&text=Your verification code is "+otp_random_no+".Please enter it and continue your shopping from Galway.Best regards-Galway&priority=ndnd&stype=normal";
//
////        st_get_otp_URL= "http://bhashsms.com/api/sendmsg.php?user=Galway&pass=P@nas0n1C&sender=GALWAY&phone="
////                +""+"&text=Your verification code is "+otp_random_no+".Please enter it and continue your shopping from Galway.Best regards-Galway&priority=ndnd&stype=normal";
//
//        st_get_otp_URL = st_get_otp_URL.replaceAll(" ","%20");
//
//        ////Log.d("st_get_otp_URL",st_get_otp_URL);
//        //CommonFun.alertError(RegistrationActivity.this,st_get_otp_URL);
//
//        pDialog = new TransparentProgressDialog(RegistrationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_get_otp_URL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        if(response!=null){
//                            try {
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                ////Log.d("error", e.toString());
//
//                            }
//                        }
//                    }
//
//
//
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
//                CommonFun.showVolleyException(error,RegistrationActivity.this);
//            }
//        }){
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//
//        };
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        queue.add(jsObjRequest);
//
//    }


//    private void getEmployeeDetails() {
//
//         st_Get_Employee_Details_URL = "http://it.galway.in/returnapi/Employee_Details?Emp_Code="+st_emp_code;
//         ////Log.d("Employee_Details_URL",st_Get_Employee_Details_URL);
//
//        pDialog = new TransparentProgressDialog(RegistrationActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Employee_Details_URL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        if(response!=null){
//                            try {
//
//                                dist_details = new JSONArray(String.valueOf(response));
//                                JSONObject dist_details_object =dist_details.getJSONObject(0);
//
//                                st_Fname = dist_details_object.getString("employee_name");
//                                st_emailId = dist_details_object.getString("employee_email");
//                                st_phone_no = dist_details_object.getString("contact_no");
//
//                                ////Log.d("st_Fname",st_Fname);
//                                ////Log.d("st_alert_msg",st_alert_msg);
//
//                                if(!st_Fname.equalsIgnoreCase("")) {
//                                    rel_layout4.setVisibility(View.VISIBLE);
//                                    rel_layout3.setVisibility(View.GONE);
//                                    sendOtpViaAPI();
//                                }
//                                else
//                                    CommonFun.alertError(RegistrationActivity.this,"Employee code doesn't exist..Try again..");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                ////Log.d("error",e.toString());
//
//
//                            }
//                        }
//                    }
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
//                CommonFun.showVolleyException(error,RegistrationActivity.this);
//            }
//        }){
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//
//        };
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        queue.add(jsObjRequest);
//
//
//    }


/**
 * Send otp via api
 */
private void sendOtpViaAPI() {

    Random rand = new Random();
    rel_layout3.setVisibility(View.GONE);

    otp_random_no = rand.nextInt(9999) + 1000;

    ////Log.d("otp_random_no",""+otp_random_no);
    //st_phone_no = "";


    String st_text_msg = "Your verification code is "+otp_random_no+". Please enter it and continue your shopping from Galway."+"\n"+"Best regards-Galway";

//    st_get_otp_URL= "http://bhashsms.com/api/sendmsg.php?user=Galway&pass=P@nas0n1C&sender=GALWAY&phone="
//            +st_phone_no+"&text=Your verification code is "+otp_random_no+".Please enter it and continue your shopping from Galway.Best regards-Galway&priority=ndnd&stype=normal";


   st_get_otp_URL= Global_Settings.otp_url+"?mobile="+st_phone_no+"&otp="+otp_random_no;
    //st_get_otp_URL= Global_Settings.otp_url+"?mobile=9958170517&otp="+otp_random_no;


    //////Log.d("otpurl",st_get_otp_URL);

    //st_get_otp_URL = st_get_otp_URL.replaceAll(" ","%20");

    //////Log.d("st_get_otp_URL",st_get_otp_URL);
    //CommonFun.alertError(RegistrationActivity.this,st_get_otp_URL);

    pDialog = new TransparentProgressDialog(RegistrationActivity.this);
    pDialog.setCancelable(false);
    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

    RequestQueue queue = Volley.newRequestQueue(this);
    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_get_otp_URL,
            new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();
                    //Log.d("responseOTP",response);


                    if(response!=null){
                        try {


                        } catch (Exception e) {
                            e.printStackTrace();
                            ////Log.d("error", e.toString());

                        }
                    }
                }



            }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            CommonFun.showVolleyException(error,RegistrationActivity.this);
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

    jsObjRequest.setShouldCache(false);
    queue.add(jsObjRequest);

}



}

