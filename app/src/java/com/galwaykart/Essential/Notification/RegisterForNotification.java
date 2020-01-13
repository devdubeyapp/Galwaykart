package com.galwaykart.Essential.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.freshchat.consumer.sdk.Freshchat;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.ServiceHandler;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankesh on 2/27/2017.
 */

public class RegisterForNotification extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    SharedPreferences pref;
    String url;
    JSONArray contacts = null;
    String isSuccess="";

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
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                //Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


        String token = FirebaseInstanceId.getInstance().getToken();

        Freshchat.getInstance(this).setPushRegistrationToken(token);

        String user_id=pref.getString("login_customer_id","");

        // Log and toast
        String msg = "Instance Token:";
        //Log.d(TAG, msg);


            // [END subscribe_topics]

                // Log and toast
                String msge ="Registered on Topic";
                //Log.d(TAG, msg);


       // url="";
        url= Global_Settings.Notification_api_url+"registerDevice.php?" +
                "devicetoken="+token+
                "&userid="+user_id+
                "&reqfrm=1";


//        Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
//        startActivity(innew);
//        CommonFun.finishscreen(RegisterForNotification.this);


                new RegisterToken().execute();

    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class RegisterToken extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            ServiceHandler sh = new ServiceHandler();

            /**
             *  Making a request to url and getting response
             */
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            //Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray("APPSET");

                    // looping through All Rejected data
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        // tmp hashmap for single rejected data
                        HashMap<String, String> contact = new HashMap<String, String>();

                        isSuccess=c.getString("status");

                        // adding each child node to HashMap key => value



                        // adding contact to rejected data
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


            // TODO: register the new account here.
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (isSuccess.equalsIgnoreCase("0")) {
               //Toast.makeText(RegisterForNotification.this,"Device registered successfully", Toast.LENGTH_LONG).show();

        SharedPreferences.Editor editor;
        editor= pref.edit();

        editor.putString("reg_for_notification","registered");
        editor.commit();

        Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
        startActivity(innew);
                CommonFun.finishscreen(RegisterForNotification.this);


            } else {
                Toast.makeText(RegisterForNotification.this,"Something Wrong!!! Try Again", Toast.LENGTH_LONG).show();
                Intent innew = new Intent(RegisterForNotification.this, SplashActivity.class);
                startActivity(innew);
                CommonFun.finishscreen(RegisterForNotification.this);


            }
        }

        @Override
        protected void onCancelled() {

        }
    }



}
