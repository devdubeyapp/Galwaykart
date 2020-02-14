package com.galwaykart.profile;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ankesh on 10/14/2017.
 */

public class ChangePasswordActivity extends BaseActivityWithoutCart {


    EditText ed_current_password;
    EditText password,confirm_password;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;
    String change_pwd_url= Global_Settings.api_url+"rest/V1/customers/me/password";
    String  delivery_data_in="";
    String tokenData="";
    Button button_change_pwd;
    String PASSWORD_PATTERN ="(.{8,20})";
    Pattern pattern;
    Matcher matcher;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChangePasswordActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        initNavigationDrawer();


    }

    @Override
    protected void onResume() {
        super.onResume();

        pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");



        ed_current_password= findViewById(R.id.ed_current_password);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        button_change_pwd= findViewById(R.id.button_change_pwd);
        button_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callChangePassword();
            }
        });


    }

    private void callChangePassword(){
        String current_password=ed_current_password.getText().toString().trim();
        String st_password= password.getText().toString();
        String st_comfirm_pwd = confirm_password.getText().toString();
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(st_password);
        boolean password_format = matcher.matches();
        boolean password_current=false;


        if(current_password.equals(pref.getString("user_password","").trim()))
            password_current=true;

        if(password_current==true) {
            if (password_format == true) {

                if (st_comfirm_pwd.equals(st_password))
                    changePassword(current_password,st_comfirm_pwd);
                else
                    CommonFun.alertError(ChangePasswordActivity.this, "Password not matched..Try Again..");


            } else
                CommonFun.alertError(ChangePasswordActivity.this, "Password must contain atleast 8 character");
        }
        else {
            CommonFun.alertError(ChangePasswordActivity.this, "Current Password not correct..Try Again..");
        }
    }

    /**
     * call api to change password
     *
     * Ankesh Kumar
     */
    private void changePassword(String current_pwd, final String new_pwd) {

        delivery_data_in ="{" +
                                                "\"currentPassword\": \""+current_pwd+"\"," +
                                                "\"newPassword\": \""+new_pwd+"\"" +
                                                "}";

        //////Log.d("url",delivery_data_in);

        pDialog = new TransparentProgressDialog(ChangePasswordActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, change_pwd_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //////Log.d("VOLLEY", response);
                            //CommonFun.alertError(ChangePasswordActivity.this,response);

                            if(response.equals("true"))
                                sendToSplash("true");

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                   // CommonFun.showVolleyException(error,ChangePasswordActivity.this);

                    //sendToSplash("error");


                    NetworkResponse response = error.networkResponse;
                    String errorMsg = "";
                    if(response != null && response.data != null){
                        String errorString = new String(response.data);
                        //Log.d("glog", errorString);

                        try {
                            JSONObject object = new JSONObject(errorString);
                            String st_msg = object.getString("message");

                             CommonFun.alertError(ChangePasswordActivity.this,st_msg);
//                                //Log.d("st_code",st_code);
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            CommonFun.showVolleyException(error, ChangePasswordActivity.this);
                        }


                    }
                    else
                        CommonFun.showVolleyException(error, ChangePasswordActivity.this);



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
                    return delivery_data_in == null ? null : delivery_data_in.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //////Log.d("error...","Error");
        }
    }

    private void sendToSplash(final String new_pwd) {


        Vibrator vibrator = (Vibrator) ChangePasswordActivity.this.getSystemService(ChangePasswordActivity.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        final Dialog dialog = new Dialog(ChangePasswordActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_design);
        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
        ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
        if(new_pwd.equalsIgnoreCase("true"))
            tv_dialog.setText("Your password has been changed successfully...");
        else
            tv_dialog.setText(new_pwd);

        dialog.show();

        new CountDownTimer(2000,2000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub


                dialog.dismiss();
                SharedPreferences pref;
                pref = CommonFun.getPreferences(getApplicationContext());

                SharedPreferences.Editor editor=pref.edit();
                editor.putString("user_password",new_pwd);
                editor.commit();

                Intent intent=new Intent(ChangePasswordActivity.this, LogoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(ChangePasswordActivity.this);
            }
        }.start();



    }
}
