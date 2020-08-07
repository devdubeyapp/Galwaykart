package com.galwaykart.Essential.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.freshchat.consumer.sdk.Freshchat;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ankesh on 2/27/2017.
 */

public class RegisterForNotification extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    SharedPreferences pref;
    String url="",st_status="";
    JSONArray contacts = null;
    TransparentProgressDialog pDialog;
    JsonObjectRequest jsObjRequest = null;
    String isSuccess="";
    String type_api="",type_firebase="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_notification);
        pref= CommonFun.getPreferences(getApplicationContext());


        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]

        String login_group_id=pref.getString("login_group_id","");

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                //Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());

                            goToNextActivity();

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        registerNotification(token,login_group_id);

                    }
                });




    }

    private void goToNextActivity(){
        Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
        startActivity(innew);
        CommonFun.finishscreen(RegisterForNotification.this);
    }


    private void registerNotification(String token,String login_group_id){
        Freshchat.getInstance(this).setPushRegistrationToken(token);

        Log.e("firebase_token", token);
        String user_id=pref.getString("login_customer_id","");

        // Log and toast
        String msg = "Instance Token:";
        //Log.d(TAG, msg);


        // [END subscribe_topics]

        // Log and toast
        String msge ="Registered on Topic";
        //Log.d(TAG, msg);


        // url="";



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

        type_firebase="abcdTest";

        url= Global_Settings.Notification_api_url+"registerDevice.php?" +
                "devicetoken="+token+
                "&userid="+user_id+
                "&reqfrm=1" +
                "&type="+type_api;


//        Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
//        startActivity(innew);
//        CommonFun.finishscreen(RegisterForNotification.this);


        //    new RegisterToken().execute();



        String st_reg_for_notification_guest= pref.getString("reg_for_notification_guest","");
        if(!st_reg_for_notification_guest.equals("") && st_reg_for_notification_guest!=null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("guest")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribe_failed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribed);
                                //registerToken(url);
                                SharedPreferences.Editor editor;
                                editor= pref.edit();

                                editor.putString("reg_for_notification_guest","");
                                editor.commit();

                                subscribeToType();
                            }
                            //Log.d(TAG, msg);
                           // registerToken(url);
                            subscribeToType();
                            //Toast.makeText(RegisterForNotification.this, msg, Toast.LENGTH_SHORT).show();


                        }
                    });
        }
        else
        {

            subscribeToType();
          //  registerToken(url);

        }

  }

  private void subscribeToType(){
      FirebaseMessaging.getInstance().subscribeToTopic(type_firebase)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      String msg = getString(R.string.msg_subscribed);


                      if (!task.isSuccessful()) {
                          msg = getString(R.string.msg_subscribe_failed);
                          registerToken(url);


                      }
                      else {
                          //Log.d(TAG, msg);
                          registerToken(url);
                      }


                  }
              });
  }


    private void registerToken(String url) {

        pDialog = new TransparentProgressDialog(RegisterForNotification.this);
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
                                    //Toast.makeText(RegisterForNotification.this,"Device registered successfully", Toast.LENGTH_LONG).show();

                                    SharedPreferences.Editor editor;
                                    editor= pref.edit();

                                    editor.putString("reg_for_notification","registered");
                                    editor.commit();

                                        goToNextActivity();


                                } else {
                                    Toast.makeText(RegisterForNotification.this,"Something Wrong!!! Try Again", Toast.LENGTH_LONG).show();
//                                    Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
//                                    startActivity(innew);
//                                    CommonFun.finishscreen(RegisterForNotification.this);
                                    goToNextActivity();


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

                        CommonFun.showVolleyException(error,RegisterForNotification.this);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
            queue.add(jsObjRequest);


    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

//    public class RegisterToken extends AsyncTask<Void, Void, Boolean> {
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
//            //Log.d("Response: ", "> " + jsonStr);
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
//               //Toast.makeText(RegisterForNotification.this,"Device registered successfully", Toast.LENGTH_LONG).show();
//
//        SharedPreferences.Editor editor;
//        editor= pref.edit();
//
//        editor.putString("reg_for_notification","registered");
//        editor.commit();
//
//        Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
//        startActivity(innew);
//                CommonFun.finishscreen(RegisterForNotification.this);
//
//
//            } else {
//                Toast.makeText(RegisterForNotification.this,"Something Wrong!!! Try Again", Toast.LENGTH_LONG).show();
//                Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
//                startActivity(innew);
//                CommonFun.finishscreen(RegisterForNotification.this);
//
//
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//
//        }
//    }



}
