package com.galwaykart.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.galwaykart.SplashActivity;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChangeMobileActivity extends AppCompatActivity {


    EditText mobile_et, edit_otp;
    Button continue_btn,button_otp_check;
    TransparentProgressDialog pDialog;
    String tokenData="",st_otp="",st_mobile="";
    RelativeLayout rel_lay_mobile,rel_layout_otp;
    Button button_otp_resend;
    TextView tverror;
    int count_otp_request=0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, SplashActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile_v1);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //initNavigationDrawer();
        Init();

    }

    void Init()
    {

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        ImageView image_view_title=findViewById(R.id.image_view_title);
        image_view_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mobile_et= findViewById(R.id.mobile_et);
        edit_otp= findViewById(R.id.edit_otp);
        continue_btn= findViewById(R.id.continue_btn);
        button_otp_check= findViewById(R.id.button_otp_check);

        rel_layout_otp = findViewById(R.id.rel_layout_otp);
        rel_lay_mobile = findViewById(R.id.rel_lay_mobile);

        tverror= findViewById(R.id.tverror);

        rel_lay_mobile.setVisibility(View.VISIBLE);
        rel_layout_otp.setVisibility(View.GONE);
        ImageView iv_info=findViewById(R.id.iv_info);
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFun.alertOTPMessage(ChangeMobileActivity.this);
            }
        });


        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_mobile = mobile_et.getText().toString();
                if(!st_mobile.equals(""))
                {
                    sendOtpJSON();
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your new Mobile No", Snackbar.LENGTH_LONG).show();
                }



            }
        });


        button_otp_resend=findViewById(R.id.button_otp_resend);
        button_otp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtpJSON();

            }
        });

        button_otp_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 st_otp = edit_otp.getText().toString().trim();

                if(!st_otp.equals(""))
                {
                    changeMobileJSON();
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid OTP!!!", Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }


    private void setTimer(){


        button_otp_resend.setVisibility(View.GONE);

        new CountDownTimer(count_otp_request<=2?1000*60:1000*60*5, 1000) {

            public void onTick(long millisUntilFinished) {
                tverror.setText("Wait for Seconds : " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                button_otp_resend.setText("Resend OTP");
                button_otp_resend.setVisibility(View.VISIBLE);
                tverror.setText("");
                button_otp_resend.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }.start();
    }

    private void sendOtpJSON() {

        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");
        String st_user_mobile = mobile_et.getText().toString();

        String st_mobile_opt_url = Global_Settings.api_url+"rest/V1/m-send-otp/login-user";
        String json_input_data1s =  "{\"mobile_number\":\""+st_user_mobile+"\", \"type\":\"mobile_authentication\"}";

        pDialog = new TransparentProgressDialog(ChangeMobileActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ChangeMobileActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_mobile_opt_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            Log.e("response_OTP", response);
                            if (response != null) {
                                try {
                                    final JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("1"))
                                    {
                                        Log.e("status", status);
                                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

                                        rel_lay_mobile.setVisibility(View.GONE);
                                        rel_layout_otp.setVisibility(View.VISIBLE);
                                        setTimer();
                                    }
                                    else
                                    {
                                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                    }



                                } catch (Exception e) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Toast.makeText(ChangeMobileActivity.this, "Error is comming", Toast.LENGTH_LONG).show();
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,ChangeMobileActivity.this);


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
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    /*params.put("Authorization", "Bearer " + tokenData);*/
                    params.put("Authorization", "Bearer " + tokenData);
                    //  params.put("Content-Type", "application/json");
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.e("mobile_number", json_input_data1s);
                    return json_input_data1s == null ? null : json_input_data1s.getBytes(StandardCharsets.UTF_8);
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
    private void changeMobileJSON() {

        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");
        String st_user_email = mobile_et.getText().toString();

        st_otp = edit_otp.getText().toString().trim();

        String st_mobile_opt_url = Global_Settings.api_url + "rest/V1/m-update-mobile/otp";
        final String json_input_data1s = "{\"editMobile\":\""+st_user_email+"\",\"otp\":\""+st_otp+"\"}";

        pDialog = new TransparentProgressDialog(ChangeMobileActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ChangeMobileActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_mobile_opt_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            Log.e("responseeditOTP", response);
                            if (response != null) {
                                try {
                                    final JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String status = jsonObject.getString("error");
                                    String message = jsonObject.getString("msg");
                                    if(status.equals("0"))
                                    {
                                        //Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                        Toast.makeText(ChangeMobileActivity.this,message,Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ChangeMobileActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // Toast.makeText(ChangeMobileActivity.this, "Error is coming", Toast.LENGTH_LONG).show();
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (error instanceof ServerError) {
                        //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        /* if(response == null && response.data = null){*/
                        String errorString = new String(response.data);
                        Log.e("log_error", errorString);
                        try {
                            JSONObject object = new JSONObject(errorString);
                            String st_msg = object.getString("message");

                            Log.e("glog","updatecartitem");
                            CommonFun.alertError(ChangeMobileActivity.this,st_msg);

                        } catch (JSONException e) {
                            //e.printStackTrace();
                            CommonFun.showVolleyException(error, ChangeMobileActivity.this);
                        }

                    }

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
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    /*params.put("Authorization", "Bearer " + tokenData);*/
                    params.put("Authorization", "Bearer " + tokenData);
                    //  params.put("Content-Type", "application/json");
                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.e("mobile_number",json_input_data1s);
                    return json_input_data1s == null ? null : json_input_data1s.getBytes(StandardCharsets.UTF_8);
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
}
