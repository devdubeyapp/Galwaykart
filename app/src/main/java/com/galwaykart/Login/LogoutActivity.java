package com.galwaykart.Login;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.freshchat.consumer.sdk.Freshchat;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import firebaseTopic.TAG;

/**
 * Created by ankesh on 10/4/2017.
 */

public class LogoutActivity extends AppCompatActivity{
    /**
     * logout user and clear basic records
     */

    SharedPreferences pref;
    String url="",st_status="";
    JSONArray contacts = null;
    String isSuccess="";
    TransparentProgressDialog pDialog;
    JsonObjectRequest jsObjRequest = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_activity);

        Freshchat.resetUser(getApplicationContext());

        pref= CommonFun.getPreferences(getApplicationContext());

        String user_id=pref.getString("login_customer_id","");

        url= Global_Settings.Notification_api_url+"unRegisterDevice.php?&devicetoken=abcd" +
                "&userid="+user_id+"&reqfrm=1";




        callDeRegistration();


//         new DeRegisterToken().execute();




    }



    private void callDeRegistration() {
        String login_group_id=pref.getString("login_group_id","");
        String type_api="",type_firebase="";
        switch (login_group_id)
        {
            case "4":
                type_api="0";
                type_firebase="distributor";
                break;
            case "5":
                type_api="1";
                type_firebase="employee";
                break;
            case "1":
                type_api="2";
                type_firebase="customer";
                break;
            default:
                type_api="3";
                type_firebase="guest";
        }


        FirebaseMessaging.getInstance().unsubscribeFromTopic(type_firebase)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                            //registerToken(url);
                            //   registerToken(url);
                        }
                        //Log.d("fbTag", msg);
                        deRegisterToken(url);


                        //Toast.makeText(RegisterForNotification.this, msg, Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void deRegisterToken(String url) {


            pDialog = new TransparentProgressDialog(LogoutActivity.this);
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
           if(!isFinishing())
            pDialog.show();

            RequestQueue queue = Volley.newRequestQueue(this);

            try {
                jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                //Log.d("notification_logout",response.toString());

                                if (response != null) {
                                    try {

                                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                        JSONArray arr_json = jsonObject.getJSONArray("APPSET");

                                        for(int i=0;i<arr_json.length();i++){
                                            JSONObject object = arr_json.getJSONObject(i);
                                            st_status = object.getString("status");

                                        }

                                        if (st_status.equalsIgnoreCase("0")) {
                                                Toast.makeText(LogoutActivity.this,"Logout successfully",Toast.LENGTH_LONG).show();


                                                pref= CommonFun.getPreferences(getApplicationContext());
                                                SharedPreferences.Editor editor;
                                                editor= pref.edit();
                                                editor.putString("login_email","");
                                                editor.putString("login_fname","");
                                                editor.putString("login_lname","");
                                                editor.putString("st_login_id","");
                                                editor.putString("user_email","");
                                                editor.putString("user_password","");
                                                editor.putString("onetime", "");
                                                editor.putString("log_user_id","");

                                                editor.commit();
                                                clearSharedPreferences();

                                                //clearAppData();
                                               // trimCache();

                                                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                                startActivity(intent);
                                                finishscreen();

                                            } else {
                                                Toast.makeText(LogoutActivity.this,"Logout failed",Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(LogoutActivity.this, HomePageActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                                startActivity(intent);
                                                CommonFun.finishscreen(LogoutActivity.this);
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

                        CommonFun.showVolleyException(error,LogoutActivity.this);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            queue.add(jsObjRequest);



    }

    private void finishscreen() {
        this.finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class DeRegisterToken extends AsyncTask<Void, Void, Boolean> {
//
//
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            ServiceHandler sh = new ServiceHandler();
//
//            /**
//             *  Making a request to url and getting response
//             */
//            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
//
//            ////Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    contacts = jsonObj.getJSONArray("APPSET");
//
//                    // looping through All Rejected data
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//
//                        // tmp hashmap for single rejected data
//                        HashMap<String, String> contact = new HashMap<String, String>();
//
//                        isSuccess=c.getString("status");
//
//                        // adding each child node to HashMap key => value
//
//
//
//                        // adding contact to rejected data
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e("ServiceHandler", "Couldn't get any data from the url");
//            }
//
//
//            // TODO: register the new account here.
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//
//            if (isSuccess.equalsIgnoreCase("0")) {
//                Toast.makeText(LogoutActivity.this,"Logout successfully",Toast.LENGTH_LONG).show();
//
//
//                pref= CommonFun.getPreferences(getApplicationContext());
//                SharedPreferences.Editor editor;
//                editor= pref.edit();
//                editor.putString("login_email","");
//                editor.putString("login_fname","");
//                editor.putString("login_lname","");
//                editor.putString("st_login_id","");
//                editor.putString("user_email","");
//                editor.putString("user_password","");
//                editor.putString("onetime", "");
//                editor.putString("log_user_id","");
//
//                editor.commit();
//                clearSharedPreferences();
//
//                //clearAppData();
//               // trimCache();
//
//                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(intent);
//                finishscreen();
//
//            } else {
//                Toast.makeText(LogoutActivity.this,"Logout failed",Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(LogoutActivity.this, HomePageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(intent);
//                CommonFun.finishscreen(LogoutActivity.this);
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//
//        }
//    }



    public void trimCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }
        return dir.delete();
    }


    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear "+packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void clearSharedPreferences(){
        pref= CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor;
        editor= pref.edit();
        editor.putString("login_email","");
        editor.putString("login_fname","");
        editor.putString("login_lname","");
        editor.putString("st_login_id","");
        editor.putString("user_email","");
        editor.putString("user_password","");

        editor.putString("onetime", "");
        editor.putString("new_postcode","");
        editor.putString("new_city","");
        editor.putString("new_firstname","");
        editor.putString("new_lastname","");
        editor.putString("new_company","");
        editor.putString("new_region_code","");
        editor.putString("new_region", "");

        editor.putString("new_region_id","");
        editor.putString("new_add_line1","");
        editor.putString("new_country_id","");
        editor.putString("new_telephone","");
        editor.putString("new_st_state", "");


        editor.putString("customer_id", "");
        editor.putString("new_telephone", "");
        editor.putString("new_postcode", "");
        editor.putString("new_default_shipping", "");
        editor.putString("new_default_billing", "");
        editor.putString("new_add_added", "");
        editor.putString("new_st_state", "");
        editor.putInt("item_position", 0);
        editor.putString("addnew", "");
        editor.putString("reg_for_notification", "");
        editor.putString("reg_for_notification_guest", "");

        editor.clear();
        editor.remove("glazekartapp");
        editor.commit();


        SharedPreferences settings = CommonFun.getPreferences(LogoutActivity.this);
        settings.edit().clear().commit();

    }
}



