package com.galwaykart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Essential.Notification.RegisterForNotification;
import com.galwaykart.Essential.Notification.Version_Check_Activity;
import com.galwaykart.Guest.GuestHomePageActivity;
import com.galwaykart.Guest.GuestRegisterForNotification;
import com.galwaykart.HomePageTab.DataModelHomeAPI;
import com.galwaykart.Login.LogoutActivity;
//import com.galwaykart.RoomDb.GalwaykartRoomDatabase;

import com.galwaykart.MultiStoreSelection.StateSelectionDialog;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.notification.NotificationSplashActivity;
import com.galwaykart.shipyaari.TrackDetailWebViewActivity;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmObject;

import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by ankesh on 9/14/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10020;
    TimerTask timerTask;
    SharedPreferences pref =null;
    String tokenData = "",st_dist_id="";
    TransparentProgressDialog pDialog;
    int retry_merge=0;



    String notice_title="",notice_message="";


    AppUpdateManager appUpdateManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                appUpdateManager.completeUpdate();
                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    Realm realm_init;
    private void init() {
        pref = CommonFun.getPreferences(this);


        try {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {

                    Object value = bundle.get(key);
                    // //Log.d("New_Notification_Sp", String.format("%s %s (%s)", key,
                    // value.toString(), value.getClass().getName()));
                    //notice_title = getIntent().getStringExtra("title");
                    //notice_message = getIntent().getStringExtra("message");
                    if (key.equalsIgnoreCase("title")) notice_title = value.toString();
                    if (key.equalsIgnoreCase("message")) notice_message = value.toString();

                    // //Log.d("New_Notification_Sp",notice_title);
                    // //Log.d("New_Notification_Sp",notice_message);
                }
            }
        } catch (Exception ex) {
            // //Log.d("exception",ex.toString());
        }


//        GalwaykartRoomDatabase db= GalwaykartRoomDatabase.getDatabase(getApplication());
//        ProductDataModelDao productDataModelDao=db.productDataModelDao();
//          new deleteAllWordsAsyncTask(productDataModelDao).execute();

        if (!notice_title.equals("") && !notice_message.equals(""))
        {

            Intent intent = null;
            intent = new Intent(getApplicationContext(), NotificationSplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pushnotification", "yes");
            intent.putExtra("title", notice_title);
            intent.putExtra("message", notice_message);
            startActivity(intent);
            CommonFun.finishscreen(SplashActivity.this);

        }
        else
        {

//            if(realm_init!=null){
//                if(realm_init.isClosed())
//                    realm_init= Realm.getDefaultInstance();
//            }
//            else
//                realm_init= Realm.getDefaultInstance();
//
//
//            try {
//                long total_data = realm.where(DataModelHomeAPI.class).count();
//                if (total_data > 0) {
//                    realm.beginTransaction();
//                    realm.delete(DataModelHomeAPI.class);
//                    realm.commitTransaction();
//                    realm.close();
//                }
//
//                if(realm.isClosed())
//                    realm = Realm.getDefaultInstance();
//
//                long total_data_top_product = realm.where(ProductDataModel.class).count();
//                if (total_data_top_product > 0) {
//                    realm.beginTransaction();
//                    realm.delete(ProductDataModel.class);
//                    realm.commitTransaction();
//                    realm.close();
//                }
//
//            } catch (IllegalStateException ex) {
//                if(realm!=null)
//                realm.close();
//                // //Log.d("res_res",ex.getMessage());
//            } catch (Exception ex) {
//                if(realm!=null)
//                realm.close();
//                // //Log.d("res_res",ex.getMessage());
//            } finally {
//                if(realm!=null)
//                realm.close();
//            }
//            //callHomePageAPI();

            callNotificationData();


        }
    }




    private void callNotificationData() {

        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("tokenData", tokenData);

        /**
         * for distributor only
         */
        //editor.putString("user_zone","");
        editor.putString("categorydata", "");
        editor.putString("log_user_zone", "");
        //editor.putString("st_dist_id","");
        editor.putString("homepage_data", "");
        editor.commit();



        st_dist_id = pref.getString("log_user_dist_id", "");
        //////Log.d("st_dist_id",st_dist_id);

        String reg_for_notification = pref.getString("reg_for_notification", "");

        String email = pref.getString("user_email", "");

        if (!email.equalsIgnoreCase("") && email != null) {
            if (!reg_for_notification.equalsIgnoreCase("registered")) {
                Intent innew = new Intent(SplashActivity.this, RegisterForNotification.class);
                innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(innew);
                CommonFun.finishscreen(SplashActivity.this);

//
//                String pass = pref.getString("user_password", "");
//
//
//                //////Log.d("user ", email + " " + pass);
//                String url = com.galwaykart.essentialClass.Global_Settings.api_url + "rest/V1/integration/customer/token";
//                //getTokenFromVolley(url, email, pass);
//                checkVersionAPI(url, email, pass);
            } else {


                String pass = pref.getString("user_password", "");


                tokenData = pref.getString("tokenData", "");
                //////Log.d("user ", email + " " + pass);
                String url = Global_Settings.api_url + "rest/V1/integration/customer/token";
                //getTokenFromVolley(url, email, pass);
                checkVersionAPI(url, email, pass);
            }
        } else {
            String reg_for_notification_guest = pref.getString("reg_for_notification_guest", "");
            if (!reg_for_notification_guest.equalsIgnoreCase("registered")) {


                Intent innew = new Intent(SplashActivity.this, GuestRegisterForNotification.class);
                innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(innew);
                CommonFun.finishscreen(SplashActivity.this);


            } else {



                Intent innew = new Intent(SplashActivity.this, GuestHomePageActivity.class);
                innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(innew);
                CommonFun.finishscreen(SplashActivity.this);


            }
        }
    }




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
                                        //getTokenFromVolley(fromurl, email, pass);
                                        goToHome();


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


        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                0, 0);
        jsonObjectRequest.setRetryPolicy(retryPolicy);

        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);

    }

    private void sync_Cart(String guest_cart_id,String token_data)
    {
       String cart_merge_url=Global_Settings.api_url+"rest/V1/mobile/cart/merge";


        String mRequestBody="{" +
                "\"guestCartId\": \""+guest_cart_id+"\"" +
                "}";

        //Log.d("sync_LOG_VOLLEY",mRequestBody);
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, cart_merge_url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("sync_LOG_VOLLEY", response);

                //CommonFun.alertError(MainActivityProductListing.this,response);

                try {

                    Boolean sync_successfully = Boolean.parseBoolean(response);
                    if(sync_successfully==true)
                    {

                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("guest_cart_id_process","");
                        editor.putString("guest_cart_id","");
                        editor.commit();

                        goToCartPage();

                       // goToHomePage();
                    }
                    else
                    {
                        goToHomePage();
                        //CommonFun.alertError(SplashActivity.this,"");
                    }
                }
                catch (Exception ex)
                {

                    goToHomePage();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


               // if(retry_merge<2) {

//                }
//                else
//                {
                    //Intent intent = new Intent(SplashActivity.this, com.galwaykart.essentialClass.InternetConnectivityError.class);
                    //startActivity(intent);
                    // CommonFun.showVolleyException(error,SplashActivity.this);

                    goToHomePage();
               // }

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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();


                //////Log.d("delievery data in",delivery_data_in.toString());
                headers.put("Authorization", "Bearer " + token_data);
                //headers.put("Content-Type","application/json");
                return headers;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000*60,
                                         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }


    private void getCartId_v1(String guest_cart_id,String tokenData) {


        retry_merge++;
        String st_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine";
        RequestQueue queue = Volley.newRequestQueue(this);

        pDialog = new TransparentProgressDialog(this);

        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.show();


        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                st_cart_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        try {


                           // //Log.d("onCartResponse", response.toString());
                            //     CommonFun.alertError(MainActivity.this,response.toString());
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//                            String cart_id=jsonObj.getString("id");
//
//                            ////Log.d("cart_id",cart_id);
////
////                            SharedPreferences.Editor editor= preferences.edit();
////                            editor.putString("cart_id",cart_id);
////                            editor.commit();

                            // addItemToCart(cart_id);


                            String cart_id = response;
                            cart_id = cart_id.replaceAll("\"", "");

                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("guest_cart_id",cart_id);
                            editor.commit();


                            String guest_cart_id=pref.getString("guest_cart_id_process","");
                            if(guest_cart_id!=null && !guest_cart_id.equals("")) {
                                sync_Cart(guest_cart_id, tokenData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFun.alertError(SplashActivity.this, e.toString());
                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        if (error instanceof ServerError) {
                            //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                            NetworkResponse response = error.networkResponse;
                            String errorMsg = "";
                            if(response != null && response.data != null){
                                String errorString = new String(response.data);
                               // //Log.d("log_error", errorString);

                                try {
                                    JSONObject object = new JSONObject(errorString);
                                    String st_msg = object.getString("message");
//                                String st_code = object.getString("code");

                                  //  //Log.d("glog","updatecartitem");
                                    CommonFun.alertError(SplashActivity.this,st_msg);
//                                //Log.d("st_code",st_code);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    CommonFun.showVolleyException(error, SplashActivity.this);
                                }


                            }

                        } else
                            CommonFun.showVolleyException(error, SplashActivity.this);

                        //////Log.d("ERROR","error => "+error.toString());
                        //CommonFun.alertError(MainActivity.this,error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                //   params.put("Content-Type","application/json");

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


    private void goToHomePage() {

        SharedPreferences pref;
        pref = CommonFun.getPreferences(SplashActivity.this);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("call_user_currentzone","");
        editor.commit();



        Intent intent = new Intent(SplashActivity.this, StateSelectionDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(SplashActivity.this);


    }


    private void goToCartPage() {

        Intent intent = new Intent(SplashActivity.this, CartItemList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(SplashActivity.this);
    }

    private void finishScreen() {
        this.finish();
    }

    /**
     * Check Distributor id is valid or not
     */


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





    private void goToHome()
    {
        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("categorydata", "");
        editor.putString("log_user_zone", "");
        editor.putString("st_dist_id", "");
        editor.putString("homepage_data", "");
        editor.commit();

        try {
            SharedPreferences pref_banner = getSharedPreferences("pref_banner", MODE_PRIVATE);
            pref_banner.edit().clear().commit();
        }
        catch (Exception ex){
            //////Log.d("cat_banner","error");
        }

        //Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);

        pref = CommonFun.getPreferences(getApplicationContext());
        String guest_cart_id=pref.getString("guest_cart_id_process","");
        if(guest_cart_id!=null && !guest_cart_id.equals(""))
        {
            getCartId_v1(guest_cart_id,tokenData);

        }
        else
        {
            goToHomePage();
        }
    }



}
