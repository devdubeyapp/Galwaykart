package com.galwaykart.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChangeEmailActivity extends BaseActivityWithoutCart {


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    EditText edit_email,edit_password,edit_otp;
    Button email_sign_in_button;
    Button button_otp_check;

    ProgressBar login_progress;
    RelativeLayout rel_layout_otp,email_login_form;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_change);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        initNavigationDrawer();
        edit_email= findViewById(R.id.email);
        edit_password= findViewById(R.id.password);
        edit_otp= findViewById(R.id.edit_otp);


        login_progress= findViewById(R.id.login_progress);
        login_progress.setVisibility(View.GONE);

        email_sign_in_button= findViewById(R.id.email_sign_in_button);
        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enter_otp=edit_password.getText().toString();
                String enter_email=edit_email.getText().toString();
                if(!TextUtils.isEmpty(enter_otp) && !TextUtils.isEmpty(enter_email))
                {
                    SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                    String pass = pref.getString("user_password", "");

                    if(pass.equals(enter_otp)) {
                        String tokenData = pref.getString("tokenData", "");
                        String email_otp_url=Global_Settings.api_url+"rest/V1/m-send/otp-on-email";

                        setControl_1(false);

                        callAPIForOTP(email_otp_url,enter_email,tokenData);


                    }
                    else
                    {
                        CommonFun.alertError(ChangeEmailActivity.this,"Password does not match");
                    }
                }
                else
                {
                    CommonFun.alertError(ChangeEmailActivity.this,"Must enter new email and current password");
                }



            }
        });

        email_login_form= findViewById(R.id.email_login_form);
        email_login_form.setVisibility(View.VISIBLE);
        rel_layout_otp= findViewById(R.id.rel_layout_otp);
        rel_layout_otp.setVisibility(View.GONE);

        button_otp_check= findViewById(R.id.button_otp_check);
        button_otp_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=Global_Settings.api_url+"rest/V1/m-update-email/otp";
                String enter_otp=edit_otp.getText().toString();
                String enter_email=edit_email.getText().toString();
                SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                String tokenData = pref.getString("tokenData", "");


                    if(!TextUtils.isEmpty(enter_otp)) {
                        setControl_2(false);
                    call_otp_check(url, enter_otp,enter_email,tokenData);
                }
                else
                {
                    CommonFun.alertError(ChangeEmailActivity.this,"Must enter valid otp");
                }
            }
        });

    }

    private  void callAPIForOTP(String url,String new_email,String tokenData){


        String input_data="{\"editEmail\":\""+new_email+"\"}";

        login_progress.setVisibility(View.VISIBLE);
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                       login_progress.setVisibility(View.GONE);
                            //Log.d("OTPresponse", response);
                            //Log.d("OTPresponse", response);


                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                String status="",msg="";

                                status=jsonObject.getString("error");
                                msg=jsonObject.getString("msg");

                                if(status.equals("0"))
                                {
                                    final AlertDialog.Builder b;
                                    try
                                    {
                                        b = new AlertDialog.Builder(ChangeEmailActivity.this);
                                        b.setTitle("Alert");
                                        b.setCancelable(false);
                                        b.setMessage(msg);
                                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton)
                                            {
                                                email_login_form.setVisibility(View.GONE);
                                                rel_layout_otp.setVisibility(View.VISIBLE);
                                                b.create().dismiss();
                                            }
                                        });
                                        b.create().show();
                                    }
                                    catch(Exception ex)
                                    {
                                    }


                                }

                                else
                                {
                                    final AlertDialog.Builder b;
                                    try
                                    {
                                        b = new AlertDialog.Builder(ChangeEmailActivity.this);
                                        b.setTitle("Alert");
                                        b.setCancelable(false);
                                        b.setMessage(msg);
                                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton)
                                            {


//                                                email_login_form.setVisibility(View.GONE);
//                                                rel_layout_otp.setVisibility(View.VISIBLE);
//                                                b.create().dismiss();
                                                setControl_1(true);
                                                b.create().dismiss();
                                            }
                                        });
                                        b.create().show();
                                    }
                                    catch(Exception ex)
                                    {
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    login_progress.setVisibility(View.GONE);
                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,ChangeEmailActivity.this);
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


                    //////Log.d("delievery data in",delivery_data_in.toString());
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
            //////Log.d("error...","Error");
        }

    }


    private void call_otp_check(String url,String enter_otp,String new_email,String tokenData)
    {

        String input_data="{\"otp\":\""+enter_otp+"\"," +
                            "\"editEmail\":\""+new_email+"\"}";

        login_progress.setVisibility(View.VISIBLE);
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            login_progress.setVisibility(View.GONE);
                            //Log.d("OTPresponse", response);
                            //Log.d("OTPresponse", response);


                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                String status="",msg="";

                                status=jsonObject.getString("error");
                                msg=jsonObject.getString("msg");

                                if(status.equals("0"))
                                {
                                    final AlertDialog.Builder b;
                                    try
                                    {
                                        b = new AlertDialog.Builder(ChangeEmailActivity.this);
                                        b.setTitle("Alert");
                                        b.setCancelable(false);
                                        b.setMessage(msg);
                                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton)
                                            {
                                                Intent intent=new Intent(ChangeEmailActivity.this,LogoutActivity.class);
                                                startActivity(intent);
                                                finishScreen();
                                            }
                                        });
                                        b.create().show();
                                    }
                                    catch(Exception ex)
                                    {
                                    }


                                }

                                else
                                {
                                    final AlertDialog.Builder b;
                                    try
                                    {
                                        b = new AlertDialog.Builder(ChangeEmailActivity.this);
                                        b.setTitle("Alert");
                                        b.setCancelable(false);
                                        b.setMessage(msg);
                                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton)
                                            {

//                                                email_login_form.setVisibility(View.GONE);
//                                                rel_layout_otp.setVisibility(View.VISIBLE);
//                                                b.create().dismiss();

                                                setControl_2(true);
                                                b.create().dismiss();
                                            }
                                        });
                                        b.create().show();
                                    }
                                    catch(Exception ex)
                                    {
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    login_progress.setVisibility(View.GONE);
                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,ChangeEmailActivity.this);
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


                    //////Log.d("delievery data in",delivery_data_in.toString());
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
            //////Log.d("error...","Error");
        }


    }

    private void finishScreen(){
        this.finish();
    }

    private void setControl_1(Boolean is_enable)
    {

        edit_email.setEnabled(is_enable);
        edit_password.setEnabled(is_enable);
        email_sign_in_button.setEnabled(is_enable);

    }
    private void setControl_2(Boolean is_enable)
    {

        edit_otp.setEnabled(is_enable);
        button_otp_check.setEnabled(is_enable);

    }

}
