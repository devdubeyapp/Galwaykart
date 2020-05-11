package com.galwaykart.Login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;

import com.galwaykart.Cart.DataModelRecentItem;
import com.galwaykart.profile.ChangeMobileActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Essential.Notification.RegisterForNotification;
import com.galwaykart.Guest.GuestHomePageActivity;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.ChangeEmailActivity;
import com.galwaykart.registration.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import static com.galwaykart.essentialClass.Global_Settings.api_url;
//import static com.google.android.gms.internal.zzdmy.checkNotNull;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


//    public native  String getData();
//
//    static {
//        System.loadLibrary("funadd");
//    }
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    TextView tv_send_otp, tv_forget_pwd,tv_create_an_acc;
    LinearLayout ly_opt_forgot;
    TransparentProgressDialog pDialog;
    StringRequest stringRequest = null;
    RelativeLayout common_header;

    String tokenData = "";
    String loginUrl = "";
    String mRequestBody = "";
    String st_user_email = "",user_detail_url="",st_user_pwd = "";
    TimerTask timerTask;

    String st_Forget_pwd_URL = "",login_email="",
            login_fname="",
            login_lname="",
            login_id="",login_group_id="";

    String login_telephone="",login_customer_id="",address_id="",address_region_id="",address_country_id="",
            address_telephone="",address_postcode="",address_city="",address_firstname="",
            address_lastname="",address_default_shipping="",region_code="",region="",region_id="",st_street="";


    SharedPreferences pref;
    ProgressBar login_progress;
    Button mEmailSignInButton;
    Boolean showingForget;
    TextInputLayout lay2;
    TextInputLayout lay1;
    ImageView img_logo;
    TextView tv_forget_email;

    Button button_otp_resend;
    TextView tverror;

    RelativeLayout rel_layout_otp;
    ImageView iv_close;
    int count_otp_request=0;
    Button button_otp_check;
    EditText edit_otp;
    RelativeLayout email_login_form;
    ImageView iv_info;

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        if(showingForget==true){

            mPasswordView.setVisibility(View.VISIBLE);
            mEmailSignInButton.setText("Sign In");
            tv_forget_pwd.setVisibility(View.VISIBLE);
            tv_send_otp.setVisibility(View.VISIBLE);
            lay2.setVisibility(View.VISIBLE);
            tv_send_otp.setTextColor(getResources().getColor(R.color.colorPrimary));
            iv_info.setVisibility(View.VISIBLE);
            tv_send_otp.setEnabled(true);
            mEmailView.setHint(getResources().getString(R.string.prompt_email));
            lay1.setHint(getResources().getString(R.string.prompt_email));
        }
        else
            this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        showingForget=false;


        pref=CommonFun.getPreferences(this);


        email_login_form=findViewById(R.id.email_login_form);

        login_progress = findViewById(R.id.login_progress);
        pDialog = new TransparentProgressDialog(this);
        tv_forget_pwd = findViewById(R.id.tv_forget_pwd);
        ly_opt_forgot = findViewById(R.id.ly_opt_forgot);
        tv_send_otp = findViewById(R.id.tv_send_otp);
        edit_otp=findViewById(R.id.edit_otp);



        common_header = findViewById(R.id.common_header);

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        tv_create_an_acc = findViewById(R.id.tv_create_an_acc);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);

        button_otp_check=findViewById(R.id.button_otp_check);
        button_otp_check.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                st_user_email = mEmailView.getText().toString().trim();
                st_user_pwd = mPasswordView.getText().toString().trim();

                if (!edit_otp.getText().toString().equalsIgnoreCase("") ) {
                    mPasswordView.setText(edit_otp.getText().toString());
                    attemptLogin();
                }
                else
                    CommonFun.alertError(LoginActivity.this, "Must enter otp to proceed");
            }
        });
        // Set up the login form.

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        rel_layout_otp = findViewById(R.id.rel_layout_otp);
        rel_layout_otp.setVisibility(View.GONE);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count_otp_request=0;
                rel_layout_otp.setVisibility(View.GONE);
                timer.cancel();
                email_login_form.setVisibility(View.VISIBLE);
            }
        });
        tverror=findViewById(R.id.tverror);


        iv_info=findViewById(R.id.iv_info);
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFun.alertOTPMessage(LoginActivity.this);
            }
        });
        button_otp_resend=findViewById(R.id.button_otp_resend);
        button_otp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtpJSON();

            }
        });


        tv_forget_email= findViewById(R.id.tv_forget_email);
        tv_forget_email.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ChangeEmailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(LoginActivity.this);

            }
        });

        img_logo= findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, GuestHomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(LoginActivity.this);

            }
        });

        lay2= findViewById(R.id.lay2);

        lay1= findViewById(R.id.lay1);

        tv_create_an_acc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(LoginActivity.this);

            }
        });


        tv_forget_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //mEmailView.setHint("");
                mEmailSignInButton.setText("Request Password");
                mPasswordView.setVisibility(View.GONE);
                tv_forget_pwd.setVisibility(View.GONE);
                tv_send_otp.setVisibility(View.GONE);
                lay2.setVisibility(View.GONE);
                mEmailView.setHint("Email");
                lay1.setHint("Email");
                common_header.setVisibility(View.VISIBLE);




                if(timer!=null) {
                    timer.cancel();
                    tverror.setText("");
                }
                iv_info.setVisibility(View.GONE);

                showingForget=true;

            }
        });


        String value = pref.getString("onetime", "");
        if (value != null && !value.equals("")) {

            String reg_for_notification = pref.getString("reg_for_notification", "");

            if (!reg_for_notification.equalsIgnoreCase("registered")) {
                Intent innew = new Intent(LoginActivity.this, RegisterForNotification.class);
                innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(innew);
                CommonFun.finishscreen(LoginActivity.this);
            } else {
                Intent innew = new Intent(LoginActivity.this, SplashActivity.class);
                innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(innew);
                CommonFun.finishscreen(LoginActivity.this);
            }
        } else {


//            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                        attemptLogin();
//                        return true;
//                    }
//                    return false;
//                }
//            });


            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mEmailSignInButton.getText().toString().equalsIgnoreCase("Sign In")) {
                        st_user_email = mEmailView.getText().toString().trim();
                        st_user_pwd = mPasswordView.getText().toString().trim();

                        if (!st_user_email.equalsIgnoreCase("") && !st_user_pwd.equalsIgnoreCase(""))
                            attemptLogin();
                        else
                            CommonFun.alertError(LoginActivity.this, "Fill Mandatory field first...");
                     }
                     else
                    {
                        st_user_email = mEmailView.getText().toString().trim();
                        st_user_pwd = mPasswordView.getText().toString().trim();

                        if(!st_user_email.equalsIgnoreCase(""))
                            sendForgetPwd();
                        else
                            CommonFun.alertError(LoginActivity.this, "Enter your Registered Email Id or Mobile Number..");
                    }

                }
            });
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }

        tv_send_otp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_send_otp.setEnabled(false);
                tv_send_otp.setTextColor(getResources().getColor(R.color.colorSub));
                String user_id = mEmailView.getText().toString().trim();

                if(!user_id.contains("@") && user_id.matches("[0-9]+") && !user_id.equals("") )
                {
                    sendOtpJSON();
                }
                else
                {
                    tv_send_otp.setEnabled(true);
                    tv_send_otp.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Snackbar.make(findViewById(android.R.id.content), "Invalid Mobile No", Snackbar.LENGTH_LONG).show();

                }


            }
        });


    }

    private void goBack() {
        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(LoginActivity.this);
    }


    CountDownTimer timer=null;
    private void setTimer(){


        //button_otp_resend.setVisibility(View.GONE);
        tv_send_otp.setEnabled(false);
        tv_send_otp.setTextColor(getResources().getColor(R.color.colorSub));

        timer=new CountDownTimer(count_otp_request<=2?1000*60:1000*60*5, 1000) {

            public void onTick(long millisUntilFinished) {
                tverror.setText("Wait for minute : " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                tv_send_otp.setEnabled(true);
                tv_send_otp.setTextColor(getResources().getColor(R.color.colorPrimary));
//                button_otp_resend.setText("Resend OTP");
//                button_otp_resend.setVisibility(View.VISIBLE);
                tverror.setText("");
                //button_otp_resend.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }.start();
    }


    private void sendOtpJSON() {

        String st_user_email = mEmailView.getText().toString().trim();
        Log.e(st_user_email, st_user_email);

        String ss = "{\"mobile_number\":\""+st_user_email+"\"}";

        String st_mobile_opt_url = Global_Settings.api_url+"rest/V1/m-send-otp";
        final String json_input_data1s = "{\"mobile_number\":\""+st_user_email+"\"}";

        pDialog = new TransparentProgressDialog(LoginActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    st_mobile_opt_url,
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
                                    Log.e("status", status);
                                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                    tv_send_otp.setEnabled(true);
                                    tv_send_otp.setTextColor(getResources().getColor(R.color.colorPrimary));

                                    if(status.equals("1")) {
//                                        count_otp_request++;
//                                        email_login_form.setVisibility(View.GONE);
//                                        rel_layout_otp.setVisibility(View.VISIBLE);

                                        count_otp_request++;
                                        tv_send_otp.setTextColor(getResources().getColor(R.color.colorSub));

                                        tv_send_otp.setText("Resend OTP");
                                        setTimer();
                                    }

                                   /* if(status.equals("1"))
                                    {
                                        String message = jsonObject.getString("message");
                                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                                    }*/

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


                    ////Log.d("dataerror",error.getMessage());
                    //Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    CommonFun.showVolleyException(error,LoginActivity.this);
                    if (pDialog.isShowing())
                        pDialog.dismiss();

//                    if (error instanceof ServerError) {
//                        //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
//                        NetworkResponse response = error.networkResponse;
//                        String errorMsg = "";
//                        /* if(response == null && response.data = null){*/
//                        String errorString = new String(response.data);
//                        Log.e("log_error", errorString);
//                        try {
//                            JSONObject object = new JSONObject(errorString);
//                            String st_msg = object.getString("message");
//
//                            Log.e("glog","updatecartitem");
//                            CommonFun.alertError(LoginActivity.this,st_msg);
//
//                        } catch (JSONException e) {
//                            //e.printStackTrace();
//                            CommonFun.showVolleyException(error, LoginActivity.this);
//                        }
//
//                    }

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
//                @Override
//                public Map<String, String> getHeaders () throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    /*params.put("Authorization", "Bearer " + tokenData);*/
//                    params.put("Authorization", "Bearer " + tokenData);
//                    //  params.put("Content-Type", "application/json");
//                    return params;
//                }

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

    /**
     * call forget password api,
     * then user will get reset password instruction in hi email
     */
    private void sendForgetPwd() {

        st_Forget_pwd_URL = api_url + "rest/V1/customers/password";

        mRequestBody = "{\"email\":\"" + st_user_email
                        + "\",\"template\":\"" + Global_Settings.st_Tamplate
                        + "\",\"websiteId\":\"" + Global_Settings.st_WebSiteID
                        + "\"}";
        ////Log.d("mRequestBody", mRequestBody);

        pDialog = new TransparentProgressDialog(LoginActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        try {

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, st_Forget_pwd_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
//                            CommonFun.alertError(LoginActivity.this, response.toString());
                            ////Log.d("VOLLEY", response);
                            try {

                                boolean check_mail_exist = Boolean.parseBoolean(String.valueOf(response));
                                ////Log.d("check_mail_exist", check_mail_exist + "");

                                if(check_mail_exist== true){

                                    Vibrator vibrator = (Vibrator) LoginActivity.this.getSystemService(LoginActivity.VIBRATOR_SERVICE);
                                    vibrator.vibrate(100);

                                    final Dialog dialog = new Dialog(LoginActivity.this);
                                    dialog.setContentView(R.layout.custom_alert_dialog_design);
                                    TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                    ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                    tv_dialog.setText("Reset Password Instruction has been sent to your registered Email ID..");
                                    dialog.show();


                                    timerTask=new TimerTask() {
                                        @Override
                                        public void run() {

                                            Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);
                                            CommonFun.finishscreen(LoginActivity.this);
                                        }};


                                    Timer timer=new Timer();
                                    timer.schedule(timerTask,6000);
                                }


                            } catch (Exception e) {
                                //e.printStackTrace();
                                //CommonFun.alertError(LoginActivity.this, e.toString());

                                if(pDialog.isShowing())
                                    pDialog.dismiss();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    ////Log.d("VOLLEY", error.toString());
//                    CommonFun.alertError(LoginActivity.this, error.toString());

                    if(pDialog.isShowing())
                        pDialog.dismiss();
                    //CommonFun.showVolleyException(error,LoginActivity.this);

                    final AlertDialog.Builder b;
                    try
                    {
                        b = new AlertDialog.Builder(LoginActivity.this);
                        b.setTitle("Alert");
                        b.setCancelable(false);
                        b.setMessage("Please check email and try again.");
                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                CommonFun.finishscreen(LoginActivity.this);
                            }
                        });
                        b.create().show();
                    }
                    catch(Exception ex)
                    {
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
                public byte[] getBody() throws AuthFailureError {
                    return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
//                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            e.printStackTrace();
            if(pDialog.isShowing())
                pDialog.dismiss();
            ////Log.d("error...", "Error");
        }


    }


private void callLogin(){
    mPasswordView.setVisibility(View.VISIBLE);
    tv_forget_pwd.setVisibility(View.VISIBLE);
    tv_send_otp.setVisibility(View.VISIBLE);
    mEmailSignInButton.setText("Sign In");

}

    /**
     * call api to login
     * first get the token
     * and then call loginurl to get details
     */
    private void attemptLogin(){

        loginUrl=api_url+"index.php/rest/V1/customers/me";
        //loginUrl=api_url+getData();
//        String url=api_url+"rest/V1/integration/customer/token";
        String url=api_url+"rest/V1/m-integration/customer/token";

        String st_mail,st_pass;
        st_mail=mEmailView.getText().toString().trim();
        st_pass=mPasswordView.getText().toString().trim();




        if( (!st_mail.equals(""))  && (!st_pass.equals("")))
            getTokenFromVolley(url);
        else
        {
            Snackbar.make(findViewById(android.R.id.content),"Must enter username and password",Snackbar.LENGTH_LONG).show();
        }

    }




    /**
     * Get token for the user
     * @param fromurl
     */

    private void getTokenFromVolley(String fromurl){

        login_progress.setVisibility(View.VISIBLE);

        JSONObject jsonBody = new JSONObject();
//        try {
//
////            jsonBody.put("username", mEmailView.getText().toString());
////            jsonBody.put("password", mPasswordView.getText().toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            //CommonFun.showVolleyException(e.toString(),LoginActivity.this);
//        }

        String device_os_version = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        String build_version="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            build_version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String mRequestBody = "{"+
                "\"username\":\""+mEmailView.getText().toString().trim()+"\","+
                "\"password\":\""+mPasswordView.getText().toString().trim()+"\","+
                "\"otherDetails\":{"+
                "\"deviece\":7,"+
                "\"version\":\""+device_os_version+","+build_version+"\""+
                "}"+
                "}";
        //Log.d("request_body",mRequestBody);

        RequestQueue queue = Volley.newRequestQueue(this);
        login_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fromurl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("LOG_VOLLEY_token", response);

                //CommonFun.alertError(LoginActivity.this,response);

                try {
                    JSONObject jsonObject_msg=new JSONObject(response);
                    String msg=jsonObject_msg.getString("message");
                    CommonFun.alertError(LoginActivity.this,msg);


                } catch (JSONException e) {
                    //e.printStackTrace();

                    tokenData=response;
                    SharedPreferences pref;
                    pref= CommonFun.getPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=pref.edit();
                    tokenData=tokenData.replaceAll("\"","");
                    editor.putString("tokenData",tokenData);
                    editor.commit();
                    callLoginAPI();
            }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                login_progress.setVisibility(View.GONE);


                //                Context context=LoginActivity.this;
                if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
//                    Intent intent=new Intent(context, InternetConnectivityError.class);
//                    context.startActivity(intent);
                    CommonFun.alertError(LoginActivity.this, "Internet Connectivity Error");

                }
                else {
                    try {

                        //  JSONObject JO = new JSONObject(error.toString());
                        //  CommonFun.alertError(LoginActivity.this, error.toString());

                        String body;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            // CommonFun.alertError(LoginActivity.this,body.toString());
                            JSONObject jsonObject = new JSONObject(body);
                            String message = jsonObject.getString("message");
                            CommonFun.alertError(LoginActivity.this, message);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }



            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //                    login_progress.setVisibility(View.INVISIBLE);
                return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                1000*60,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);



    }

    /**
     * call login api to fetch user details
     *
     */

    private void callLoginAPI(){

        tokenData=tokenData.replaceAll("\"","");

        login_progress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,loginUrl,null,
                new Response.Listener<JSONObject>() {

                    @Override   public void onResponse(JSONObject response) {
                       // Log.d("response_login",response.toString());
                        //CommonFun.alertError(LoginActivity.this,response.toString());
//                        login_progress.setVisibility(View.INVISIBLE);

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                             login_email=jsonObj.getString("email");
                             login_fname=jsonObj.getString("firstname");
                             login_lname=jsonObj.getString("lastname");
                             login_id=jsonObj.getString("id");
                             login_group_id=jsonObj.getString("group_id");

                            //String default_shipping = jsonObj.getString("default_shipping");
                            //String default_billing = jsonObj.getString("default_billing");
                            String created_at = jsonObj.getString("created_at");
                            String updated_at = jsonObj.getString("updated_at");
                            String created_in = jsonObj.getString("created_in");


                            String store_id = jsonObj.getString("store_id");
                            String website_id = jsonObj.getString("website_id");
                            String disable_auto_group_change = jsonObj.getString("disable_auto_group_change");


/**
 * Now commented
 */

if(jsonObj.has("addresses")) {
    JSONArray custom_data = jsonObj.getJSONArray("addresses");

    if (custom_data.length() > 0) {

        for (int i = 0; i < custom_data.length(); i++) {

            JSONObject c = custom_data.getJSONObject(i);

            //login_telephone = c.getString("telephone");
            login_customer_id = c.getString("customer_id");

            ////Log.d("login_customer_id",login_customer_id);
            //address_id = c.getString("id");
            //address_region_id = c.getString("region_id");

//            address_country_id = c.getString("country_id");
//            address_telephone = c.getString("telephone");
//            address_postcode = c.getString("postcode");
//            address_city = c.getString("city");
//            address_firstname = c.getString("firstname");
//            address_lastname = c.getString("lastname");
//            //address_default_shipping = c.getString("default_shipping");
//            address_default_shipping = "";
//            JSONObject c_region = c.getJSONObject("region");
//
//            region_code = c_region.getString("region_code");
//            region = c_region.getString("region");
//            region_id = c_region.getString("region_id");

//                                    JSONArray arr_Street = c.getJSONArray("street");
//                                    int arr_street_length = arr_Street.length();
//                                    for(int j=0; j < arr_street_length; j++){
//
//                                       st_street = arr_Street.getString(0);
//                                    }

            st_street = "";

        }


    }

}

                            String crm_status="",crm_msg="",crm_irid="",ir_id="";
                            JSONArray jsonArray=jsonObj.getJSONArray("custom_attributes");

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                String attr_name=jsonObject.getString("attribute_code");
                                if(attr_name.equalsIgnoreCase("crm_status"))
                                {
                                    crm_status=jsonObject.getString("value");

                                }
                                else if(attr_name.equalsIgnoreCase("crm_msg"))
                                {
                                    crm_msg=jsonObject.getString("value");
                                }
                                else if(attr_name.equalsIgnoreCase("irid"))
                                {
                                    crm_msg=jsonObject.getString("value");
                                    ir_id=crm_msg;
                                }
                                else if(attr_name.equalsIgnoreCase("mobile_number"))
                                {
                                    login_telephone=jsonObject.getString("value");
                                }
                            }


/**sure
 *
 *
 *
 * Store all user details in temp storage
 *
 */

if(crm_status.equals("1")||crm_status.equals("")) {
    SharedPreferences pref;
    pref = CommonFun.getPreferences(getApplicationContext());
    SharedPreferences.Editor editor = pref.edit();

    editor.putString("login_email", login_email);
    editor.putString("login_fname", login_fname);
    editor.putString("login_lname", login_lname);
    editor.putString("login_id", login_id);
    editor.putString("login_group_id", login_group_id);
    editor.putString("default_shipping", "");
    editor.putString("created_at", created_at);
    editor.putString("updated_at", updated_at);
    editor.putString("created_in", created_in);
    editor.putString("store_id", store_id);
    //editor.putString("default_billing",default_billing);
    editor.putString("default_billing", "");
    editor.putString("website_id", website_id);
    editor.putString("disable_auto_group_change", disable_auto_group_change);
    editor.putString("login_telephone", login_telephone);
    editor.putString("login_customer_id", login_customer_id);
    editor.putString("address_id", address_id);
    editor.putString("address_region_id", address_region_id);
    editor.putString("address_country_id", address_country_id);
    editor.putString("address_telephone", address_telephone);
    editor.putString("address_postcode", address_postcode);
    editor.putString("address_city", address_city);

    editor.putString("address_firstname", address_firstname);
    editor.putString("address_lastname", address_lastname);
    editor.putString("address_default_shipping", address_default_shipping);
    editor.putString("region_code", region_code);
    editor.putString("region", region);
    editor.putString("region_id", region_id);
    editor.putString("st_street", st_street);
    editor.putString("st_dist_id",crm_irid);
    editor.putString("log_user_dist_id",crm_irid);
    editor.putString("user_irid",ir_id);



    editor.commit();

    //Log.d("user_detail_url",login_customer_id);
    storeDataLocalTest(login_email,login_fname,login_lname,login_id,login_group_id);
}
else
{
    CommonFun.alertError(LoginActivity.this,crm_msg);
}

                           // CommonFun.alertError(LoginActivity.this,login_customer_id);

//
//                            if(login_group_id.equalsIgnoreCase("4"))
//                                getDistributorId(login_customer_id);
//                            else if(login_group_id.equalsIgnoreCase("5"))
                              //  getEmployeeDetails()

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

//                new Response.ErrorListener() {
//            @Override         public void onErrorResponse(VolleyError error) {
//                ////Log.d("response",error.toString());
//
//
//            }
//        });

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        login_progress.setVisibility(View.INVISIBLE);
                        //CommonFun.showVolleyException(error,LoginActivity.this);
                        CommonFun.alertError(LoginActivity.this, "Something went wrong!!! Try Again.");

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

        jsObjRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        1000*60,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }


    private void getEmployeeDetails(String st_emp_code) {

        String st_Get_Employee_Details_URL = Global_Settings.galway_api_url+"returnapi/Employee_Details?Emp_Code="+st_emp_code;

        pDialog = new TransparentProgressDialog(LoginActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Employee_Details_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){
                     //       try {

                             //   dist_details = new JSONArray(String.valueOf(response));
                               // JSONObject dist_details_object =dist_details.getJSONObject(0);

//                                st_Fname = dist_details_object.getString("employee_name");
//                                st_emailId = dist_details_object.getString("employee_email");
//                                st_phone_no = dist_details_object.getString("contact_no");

                            //    //Log.d("st_Fname",st_Fname);

//                                if(!st_Fname.equalsIgnoreCase("")) {
//                                    rel_layout4.setVisibility(View.VISIBLE);
//                                    rel_layout3.setVisibility(View.GONE);
//                                    sendOtpViaAPI();
//                                }
//                                else
//                                    CommonFun.alertError(LoginActivity.this,"Employee code doesn't exist..Try again..");

//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                ////Log.d("error",e.toString());
//
//
//                            }
                        }
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                CommonFun.showVolleyException(error,LoginActivity.this);
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

//    private void getDistributorId(String login_customer_id) {
//
//        user_detail_url= Global_Settings.user_details_url+login_customer_id;
//
////            pDialog = new TransparentProgressDialog(this);
////            pDialog.setCancelable(false);
////            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
////            pDialog.show();
//
//            final RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
//
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, user_detail_url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
////                            if(pDialog.isShowing())
////                                pDialog.dismiss();
//
//                            ////Log.d("getUserDetails",response.toString());
//
//
//                            try {
//                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
//
//                                JSONObject jsonObject1=jsonObject.getJSONObject("details");
//
//                                String jsonObject_fcode=jsonObject1.getString("fcode");
//                                String jsonObject_distid=jsonObject1.getString("distributor_id");
//
//                                ////Log.d("distid",jsonObject_distid);
//                                ////Log.d("distzone",jsonObject_fcode);
//
//                                pref = CommonFun.getPreferences(getApplicationContext());
//                                SharedPreferences.Editor editor=pref.edit();
//                                editor.putString("log_user_dist_id",jsonObject_distid);
//                                editor.commit();
//
//                               // getUserStatus(jsonObject_distid);
////
//                            } catch (JSONException e) {
//                                e.printStackTrace();
////                                Intent intent=new Intent(LoginActivity.this, ExceptionError.class);
////                                startActivity(intent);
//                                login_progress.setVisibility(View.INVISIBLE);
//                                CommonFun.alertError(LoginActivity.this, "Something went wrong!!! Try Again");
//
//
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
////                    if(pDialog.isShowing())
////                        pDialog.dismiss();
//                    login_progress.setVisibility(View.INVISIBLE);
//
//                    CommonFun.alertError(LoginActivity.this, "Something went wrong!!! Try Again");
//
//                    //CommonFun.showVolleyException(error,LoginActivity.this);
//                    //CommonFun.alertError(Payment_Method_Activity.this,error.toString());
//                }
//            });
//
//            jsonObjectRequest.setShouldCache(false);
//            requestQueue.add(jsonObjectRequest);
//        }

//    private void getUserStatus(String disributorId){
//
//        final String input_data_sales="{\"User_id\":\""+disributorId+"\",\"spmode\":\"0\"}";
//        //Log.d("input_data_sales",input_data_sales);
//
//        String st_User_Status_URL = Global_Settings.st_sales_api+"CheckUserAuth";
//        //Log.d("st_User_Status_URL",st_User_Status_URL);
//
////        pDialog = new TransparentProgressDialog(LoginActivity.this);
////        pDialog.setCancelable(false);
////        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
////        pDialog.show();
//
//        try {
//            final RequestQueue requestQueue = Volley.newRequestQueue(this);
//            StringRequest req = new StringRequest(Request.Method.POST,
//                    st_User_Status_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String  response) {
////                            if(pDialog.isShowing())
////                                pDialog.dismiss();
//                            login_progress.setVisibility(View.INVISIBLE);
//                            ////Log.d("VOLLEYgetUserStatus", response.toString());
//                            try {
//                                //CommonFun.alertError(Payment_Method_Activity.this,response);
//                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
//                                String st_msg=jsonObject.getString("msg");
//                                String st_status=jsonObject.getString("Status");
//                                if(st_status.equalsIgnoreCase("0")) {  // status = 0 (Successful in sales)
//                                    storeDataLocalTest(login_email,login_fname,login_lname,login_id,login_group_id);
//                                }
//                                else{
//                                    //Temp Change
//
//                                    CommonFun.alertError(LoginActivity.this,
//                                            st_msg);
//
//                                }
//                            } catch (Exception e) {
//                                //e.printStackTrace();
////                                CommonFun.alertError(OrderDetails.this,e.toString());
////                                Intent intent=new Intent(LoginActivity.this, ExceptionError.class);
////                                startActivity(intent);
//                                login_progress.setVisibility(View.INVISIBLE);
//                                CommonFun.alertError(LoginActivity.this, "Something went wrong!!! Try Again");
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    ////Log.d("VOLLEY", error.toString());
//                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
////                    if(pDialog.isShowing())
////                        pDialog.dismiss();
//
//                    login_progress.setVisibility(View.INVISIBLE);
//                    CommonFun.alertError(LoginActivity.this, "Something went wrong!!! Try Again");
//
//                    //CommonFun.showVolleyException(error,LoginActivity.this);
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//                @Override
//                protected String getParamsEncoding() {
//                    return "utf-8";
//                }
//                @Override
//                public byte[] getBody() {
//                    try {
//                        return input_data_sales == null ? null : input_data_sales.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data_sales, "utf-8");
//                        return null;
//                    }
//                }
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type", "application/json; charset=utf-8");
//                    return headers;
//                }
//            };
//
//            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
//                    1,
//                    1);
//            req.setRetryPolicy(retryPolicy);
//            requestQueue.add(req);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ////Log.d("error...","Error");
//        }
//
//
//    }

    private void storeDataLocalTest(String login_email, String login_fname, String login_lname, String login_id,String login_group_id){

        SharedPreferences.Editor editor= pref.edit();
        //editor.putString("login_id",login_id);
        editor.putString("login_email",login_email);
        editor.putString("login_fname",login_fname);
        editor.putString("login_lname",login_lname);
        editor.putString("st_login_id",login_id);
        editor.putString("login_group_id",login_group_id);
        editor.putString("user_email",login_email);
        editor.putString("user_email_v1",login_email);
        editor.putString("user_password",mPasswordView.getText().toString());


        //editor.putString("login_telephone",login_telephone);
        //editor.putString("login_customer_id",login_customer_id);
        editor.putString("onetime", "Confirmed");
        editor.commit();

        callRegisterForNotification();

    }

    private void storeDataLocal(String login_id,String login_email,String login_fname,String login_lname,String login_telephone,String login_customer_id){

        SharedPreferences.Editor editor= pref.edit();
        editor.putString("login_id",login_id);
        editor.putString("login_email",login_email);
        editor.putString("login_fname",login_fname);
        editor.putString("login_lname",login_lname);
        editor.putString("login_telephone",login_telephone);
        editor.putString("login_customer_id",login_customer_id);
        editor.putString("onetime", "Confirmed");
        editor.commit();

        callRegisterForNotification();

    }

    private void callRegisterForNotification(){

        Realm realm = Realm.getDefaultInstance(); // opens "gkart.realm"
        try {
            realm.beginTransaction();

            RealmResults<DataModelRecentItem> results = realm.where(DataModelRecentItem.class).findAllAsync();
            results.deleteAllFromRealm();
            realm.commitTransaction();
            realm.close();

        }
        catch (IllegalStateException ex){
            //Log.d("res_res",ex.getMessage());
            realm.close();
        }
        catch (Exception ex){
            //Log.d("res_res",ex.getMessage());
            realm.close();
        }
        finally {
            realm.close();
        }

        Intent intent=new Intent(this, RegisterForNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }


}

