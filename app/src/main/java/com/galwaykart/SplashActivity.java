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

import com.galwaykart.MultiStoreSelection.GetCurrentZone;
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//
//    }


    String notice_title="",notice_message="";



    //    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
//        private ProductDataModelDao mAsyncTaskDao;
//
//        deleteAllWordsAsyncTask(ProductDataModelDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mAsyncTaskDao.deleteAll();
//            //Log.d("mvvmDao","deletedata");
//
//            return null;
//        }
//    }
    AppUpdateManager appUpdateManager;
    String current_user_zone="";
    boolean is_zone_called=false;
    private static final int REQUEST_CODE_EXAMPLE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Intent intent=new Intent(SplashActivity.this, .class);
//        startActivity(intent);
//        CommonFun.finishscreen(SplashActivity.this);

        //init();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        appUpdateManager = AppUpdateManagerFactory.create(SplashActivity.this);
//
//// Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//// Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // For a flexible update, use AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                      appUpdateInfo,
//                      AppUpdateType.IMMEDIATE,
//                      this,
//                      MY_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                init();
//            }
//        });

        if(is_zone_called==false) {
            final Intent intent = new Intent(SplashActivity.this, GetCurrentZone.class);
            startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
        }
        else {
            init();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CODE_EXAMPLE){

            if(resultCode==RESULT_OK)
            {
                is_zone_called=true;
                current_user_zone = data.getStringExtra(GetCurrentZone.EXTRA_DATA);

                Log.d("result_zoneActivity",current_user_zone);

                Global_Settings.api_url=Global_Settings.web_url+current_user_zone.trim().toString()+"/";
                Global_Settings.current_zone=current_user_zone.trim().toString();

            }
        }
        else if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                appUpdateManager.completeUpdate();
                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    //Realm realm_init;
    private void init() {
        pref = CommonFun.getPreferences(this);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            //bundle must contain all info sent in "data" field of the notification
//            if (getIntent().hasExtra("data")) {
//                //Log.d("New_Notification_Sp","notification");
//            }
//            //Log.d("New_Notification_Sp",getIntent().getStringExtra("notification").toString());
//        }





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

        if (!notice_title.equals("") && !notice_message.equals("")) {
            ////Log.d("New_Notification_Sp2",notice_title);
            ////Log.d("New_Notification_Sp2",notice_message);

            Intent intent = null;
            intent = new Intent(getApplicationContext(), NotificationSplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pushnotification", "yes");
            intent.putExtra("title", notice_title);
            intent.putExtra("message", notice_message);
            startActivity(intent);
            CommonFun.finishscreen(SplashActivity.this);

        } else {

            pref=CommonFun.getPreferences(SplashActivity.this);
            SharedPreferences.Editor editor=pref.edit();
            editor.putString("homePageData","");
            editor.commit();

            callHomePageAPI();


            //callNotificationData();
        }
    }


    private void callHomePageAPI()
    {

        String email = pref.getString("user_email", "");
        String login_group_id=pref.getString("login_group_id","");
        String home_page_api="";

        if (!email.equalsIgnoreCase("") && email != null) {

            home_page_api=Global_Settings.home_page_api+"?cid="+login_group_id;
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/"+login_group_id;

        }
        else
        {
            home_page_api=Global_Settings.home_page_api+"?cid=0";
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/0";
        }


        callHomeItemList(home_page_api);

    }


    private void callHomeItemList(String url_cart_item_list) {

        // progress_bar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("responsebanner", response.toString());


                        pref=CommonFun.getPreferences(SplashActivity.this);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("homePageData",response.toString());
                        editor.commit();

                        callNotificationData();


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        //progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,SplashActivity.this);

                    }
                }
        ) {

        };
        jsObjRequest.setShouldCache(false);
        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                1,
                1);
        jsObjRequest.setRetryPolicy(retryPolicy);
        queue.add(jsObjRequest);


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
                Log.i("LOG_VOLLEY_token", response);

                //CommonFun.alertError(MainActivityProductListing.this,response);

                try {

                    tokenData = response;
                    tokenData = tokenData.replaceAll("\"", "");


                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("tokenData", tokenData);

                    /**
                     * for distributor only
                     */
                    //editor.putString("user_zone","");
                    editor.putString("categorydata", "");
                    editor.putString("log_user_zone", "");
                    editor.putString("st_dist_id", "");
                    editor.putString("homepage_data", "");
                    editor.commit();
                }
                catch (Exception ex)
                {

                }

                try {
                    SharedPreferences pref_banner = getSharedPreferences("pref_banner", MODE_PRIVATE);
                    pref_banner.edit().clear().commit();
                }
                catch (Exception ex){
                    //////Log.d("cat_banner","error");
                }

                //Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);


                String guest_cart_id=pref.getString("guest_cart_id_process","");
                if(guest_cart_id!=null && !guest_cart_id.equals(""))
                {
                    getCartId_v1(guest_cart_id,tokenData);

                }
                else
                {
                    goToHomePage();
                }
                //getUserStatus(st_dist_id);




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
                //                    login_progress.setVisibility(View.INVISIBLE);
                return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
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

        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                0, 0);
        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);

        queue.add(stringRequest);


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

//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("guestCartId", guest_cart_id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//a@
//        final String mRequestBody = jsonBody.toString();


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
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
//        SharedPreferences.Editor editor=pref.edit();
//        editor.putString("call_user_currentzone","");
//        editor.commit();


        String call_user_currentzone=pref.getString("call_user_currentzone","");
        if(call_user_currentzone!=null && !call_user_currentzone.equals(""))
        {
            Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(SplashActivity.this);
        }
        else {
            Intent intent = new Intent(SplashActivity.this, StateSelectionDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(SplashActivity.this);
        }

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

    private void getUserStatus(String disributorId){

        final String input_data_sales="{\"User_id\":\""+disributorId+"\",\"spmode\":\"0\"}";
        //////Log.d("input_data_sales",input_data_sales);

        String st_User_Status_URL = Global_Settings.st_sales_api+"CheckUserAuth";
        //////Log.d("st_User_Status_URL",st_User_Status_URL);

        pDialog = new TransparentProgressDialog(SplashActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(!isFinishing())
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
                            //  //Log.d("VOLLEYgetUserStatus", response.toString());
                            try {
                                //CommonFun.alertError(Payment_Method_Activity.this,response);
                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
                                String st_msg=jsonObject.getString("msg");
                                String st_status=jsonObject.getString("Status");

                                if(st_status.equalsIgnoreCase("0")) {  // status = 0 (Successful in sales)
                                    DatabaseHandler dbh=new DatabaseHandler(SplashActivity.this);
                                    dbh.deleteAllData();

                                    //Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                                    goToHomePage();
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
                    return input_data_sales == null ? null : input_data_sales.getBytes(StandardCharsets.UTF_8);
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




    private  void alertErrorOnExit(Context ctx, String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(ctx);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    CommonFun.finishscreen(SplashActivity.this);
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

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
