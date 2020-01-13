package com.galwaykart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Essential.Notification.Version_Check_Activity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by ankesh on 9/14/2017.
 */

public class SplashActivity extends AppCompatActivity {

    TimerTask timerTask;
    SharedPreferences pref;
    String tokenData = "",st_dist_id="";
    TransparentProgressDialog pDialog;


    @Override
    protected void onResume() {
        super.onResume();



        pref = CommonFun.getPreferences(getApplicationContext());
        st_dist_id = pref.getString("log_user_dist_id", "");
        //////Log.d("st_dist_id",st_dist_id);

        String reg_for_notification = pref.getString("reg_for_notification", "");

        if (!reg_for_notification.equalsIgnoreCase("registered")) {
//            Intent innew = new Intent(SplashActivity.this, RegisterForNotification.class);
//            innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(innew);
//            CommonFun.finishscreen(SplashActivity.this);

            String email = pref.getString("user_email", "");
            String pass = pref.getString("user_password", "");


            //////Log.d("user ", email + " " + pass);
            String url = com.galwaykart.essentialClass.Global_Settings.api_url + "rest/V1/integration/customer/token";
            //getTokenFromVolley(url, email, pass);
            checkVersionAPI(url, email, pass);
        } else {

            String email = pref.getString("user_email", "");
            String pass = pref.getString("user_password", "");


            //////Log.d("user ", email + " " + pass);
            String url = com.galwaykart.essentialClass.Global_Settings.api_url + "rest/V1/integration/customer/token";
            //getTokenFromVolley(url, email, pass);
            checkVersionAPI(url, email, pass);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        timerTask=new TimerTask() {
//            @Override
//            public void run() {
//
//                Intent intent=new Intent(SplashActivity.this, HomePageActivity.class);
//                startActivity(intent);
//                finishScreen();
//
//            }};
//
//
//        Timer timer=new Timer();
//        timer.schedule(timerTask,5000);



//        service firebase.storage {
//            match /b/{bucket}/o {
//                match /{allPaths=**} {
//                    allow read, write: if request.auth != null;
//                }
//            }
//        }

//        exports.helloError = functions.https.onRequest((request, response) => {
            //console.log('I am a log entry!');
            //response.send('Hello World...');
//        });

        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tokenData", tokenData);

        /**
         * for distributor only
         */
        //editor.putString("user_zone","");
        editor.putString("categorydata","");
        editor.putString("log_user_zone","");
        editor.putString("st_dist_id","");
        editor.putString("homepage_data","");
        editor.commit();

    }


    private void getTokenFromVolley(String fromurl, String email, String pass) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", email);
            jsonBody.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, fromurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.i("LOG_VOLLEY", response);

                //CommonFun.alertError(MainActivityProductListing.this,response);
                tokenData = response;
                tokenData = tokenData.replaceAll("\"", "");
                SharedPreferences pref;
                pref = CommonFun.getPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tokenData", tokenData);

                /**
                 * for distributor only
                 */
                //editor.putString("user_zone","");
                editor.putString("categorydata","");
                editor.putString("log_user_zone","");
                editor.putString("st_dist_id","");
                editor.putString("homepage_data","");
                editor.commit();


                try {
                    SharedPreferences pref_banner = getSharedPreferences("pref_banner", MODE_PRIVATE);
                    pref_banner.edit().clear().commit();
                }
                catch (Exception ex){
                    //////Log.d("cat_banner","error");
                }

               getUserStatus(st_dist_id);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Intent intent = new Intent(SplashActivity.this, com.galwaykart.essentialClass.InternetConnectivityError.class);
                //startActivity(intent);
                 CommonFun.showVolleyException(error,SplashActivity.this);

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
//                    login_progress.setVisibility(View.INVISIBLE);
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//
//                    responseString = String.valueOf(response);
//
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }


        };


        queue.add(stringRequest);


    }

    private void finishScreen() {
        this.finish();
    }

    /**
     * Check Distributor id is valid or not
     */

    private void getUserStatus(String disributorId){

        final String input_data_sales="{\"User_id\":\""+disributorId+"\",\"spmode\":\"0\"}";
        //////Log.d("input_data_sales",input_data_sales);

        String st_User_Status_URL = Global_Settings.st_sales_api+"CheckUserAuth";
        //////Log.d("st_User_Status_URL",st_User_Status_URL);

        pDialog = new TransparentProgressDialog(SplashActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest req = new StringRequest(Request.Method.POST,
                    st_User_Status_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String  response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //////Log.d("VOLLEYgetUserStatus", response.toString());
                            try {
                                //CommonFun.alertError(Payment_Method_Activity.this,response);
                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
                                String st_msg=jsonObject.getString("msg");
                                String st_status=jsonObject.getString("Status");

                                if(st_status.equalsIgnoreCase("0")) {  // status = 0 (Successful in sales)
                                    DatabaseHandler dbh=new DatabaseHandler(SplashActivity.this);
                                    dbh.deleteAllData();

                                    //Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                                    Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    CommonFun.finishscreen(SplashActivity.this);
                                }
                                else{

                                    alertError(st_msg);


                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
//                                CommonFun.alertError(OrderDetails.this,e.toString());
                              //  Intent intent=new Intent(SplashActivity.this, ExceptionError.class);
                               // startActivity(intent);

                                Intent intent=new Intent(SplashActivity.this, SplashErrorActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    CommonFun.alertError(SplashActivity.this,"Something went wrong!!! Try Again");
                    //CommonFun.showVolleyException(error,SplashActivity.this);
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
                public byte[] getBody() {
                    try {
                        return input_data_sales == null ? null : input_data_sales.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data_sales, "utf-8");
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    1,
                    1);
            req.setRetryPolicy(retryPolicy);
            requestQueue.add(req);
        } catch (Exception e) {
            e.printStackTrace();
            //////Log.d("error...","Error");
        }


    }

    public void alertError(String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(SplashActivity.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    Intent  intent = new Intent(SplashActivity.this,LogoutActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(SplashActivity.this);
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

/**
 * Check API Version
 */

    private void checkVersionAPI(final String fromurl, final String email, final String pass) {

    String version_check_url= Global_Settings.api_custom_url+"versioncheck.php";

    RequestQueue requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, version_check_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonObject=response;
                        //////Log.d("response",response.toString());

                        try {
                            String string_status=jsonObject.getString("status");

                            if(string_status.equals("200")){

                                JSONArray jsonArray=jsonObject.getJSONArray("checksum");

                                if(jsonArray.length()>0){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                    String android_current_version=jsonObject1.getString("android_version");
                                    String android_shutdown=jsonObject1.getString("android_close");
                                    String check_stock_of=jsonObject1.getString("check_stock");
                                    String android_checkouttext=jsonObject1.getString("checkout_warning_text");

                                    //String check_stock_of="SNP";
                                    //String android_checkouttext="done";

                                    if(Integer.parseInt(android_current_version)>  Global_Settings.current_soft_version){

                                        Intent intent=new Intent(SplashActivity.this, Version_Check_Activity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonFun.finishscreen(SplashActivity.this);

                                    }

                                    else if(android_shutdown.equalsIgnoreCase("true")){

                                        String android_text=jsonObject1.getString("android_show_text");


                                        Intent intent=new Intent(SplashActivity.this, ShutDown.class);
                                        intent.putExtra("android_text",android_text);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonFun.finishscreen(SplashActivity.this);


                                    }
                                    else
                                    {
                                                SharedPreferences.Editor editor=pref.edit();
                                                editor.putString("check_stock_of",check_stock_of);
                                                editor.putString("checkout_warning_text",android_checkouttext);
                                                editor.commit();
                                                getTokenFromVolley(fromurl, email, pass);

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent=new Intent(SplashActivity.this, SplashErrorActivity.class);
                            startActivity(intent);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Intent intent=new Intent(SplashActivity.this, SplashErrorActivity.class);
                startActivity(intent);


            }
        });

        requestQueue.add(jsonObjectRequest);

    }




}
