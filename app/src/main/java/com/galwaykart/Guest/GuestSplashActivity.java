package com.galwaykart.Guest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Essential.Notification.Version_Check_Activity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;
import com.galwaykart.ShutDown;
import com.galwaykart.SplashErrorActivity;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class GuestSplashActivity extends AppCompatActivity {

    TimerTask timerTask;
    SharedPreferences pref;
    String tokenData = "",st_dist_id="";
    TransparentProgressDialog pDialog;
    int retry_merge=0;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        pref = CommonFun.getPreferences(getApplicationContext());


        pref = CommonFun.getPreferences(getApplicationContext());
        st_dist_id = pref.getString("log_user_dist_id", "");
        //////Log.d("st_dist_id",st_dist_id);

        String reg_for_notification = pref.getString("reg_for_notification_guest", "");
        String reg_for_notification_log = pref.getString("reg_for_notification", "");
        if (!reg_for_notification.equalsIgnoreCase("registered") || !reg_for_notification_log.equalsIgnoreCase("registered")) {
            Intent innew = new Intent(GuestSplashActivity.this, GuestHomePageActivity.class);
            innew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(innew);
            CommonFun.finishscreen(GuestSplashActivity.this);


        } else {


        }
        String token = FirebaseInstanceId.getInstance().getToken();
        String   url= Global_Settings.Notification_api_url+"registerDevice.php?" +
                "devicetoken="+token+
                "&userid="+
                "&reqfrm=1" +
                "&type=3";

      //  registerToken(url);


                FirebaseMessaging.getInstance().subscribeToTopic("employee")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                            registerToken(url);
                            Log.e("RFN","FRN");

                        }
                        //Log.d("msg", msg);
                        registerToken(url);
                        //Toast.makeText(RegisterForNotification.this, msg, Toast.LENGTH_SHORT).show();
                        Log.e("RFN","FRN");
                    }
                });

        Log.e("RFN","FRN");

    }


    JsonObjectRequest jsObjRequest = null;
    String st_status="";
    private void registerToken(String url) {

        pDialog = new TransparentProgressDialog(GuestSplashActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
             jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            //Log.d("response_notification",response.toString());

                            if (response != null) {
                                try {

                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                    JSONArray arr_json = jsonObject.getJSONArray("APPSET");

                                    for(int i=0;i<arr_json.length();i++){
                                        JSONObject object = arr_json.getJSONObject(i);

                                        st_status = object.getString("status");

                                    }


                                    if (st_status.equalsIgnoreCase("0")) {
                                        //Toast.makeText(GuestSplashActivity.this,"Device registered successfully", Toast.LENGTH_LONG).show();

                                        SharedPreferences.Editor editor;
                                        editor= pref.edit();

                                        editor.putString("reg_for_notification_guest","registered");
                                        editor.commit();

                                        Intent innew = new Intent(GuestSplashActivity.this, GuestHomePageActivity.class);
                                        startActivity(innew);
                                        CommonFun.finishscreen(GuestSplashActivity.this);


                                    } else {
                                        Toast.makeText(GuestSplashActivity.this,"Something Wrong!!! Try Again", Toast.LENGTH_LONG).show();
                                        Intent innew = new Intent(GuestSplashActivity.this, GuestHomePageActivity.class);
                                        startActivity(innew);
                                        CommonFun.finishscreen(GuestSplashActivity.this);


                                    }

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

                    CommonFun.showVolleyException(error,GuestSplashActivity.this);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsObjRequest);


    }


}
