package com.galwaykart.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.freshchat.consumer.sdk.Freshchat;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankesh on 10/4/2017.
 */

public class LogoutActivity extends AppCompatActivity{
    /**
     * logout user and clear basic records
     */

    SharedPreferences pref;
    String url;
    JSONArray contacts = null;
    String isSuccess="";

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


         new DeRegisterToken().execute();





    }
    private void finishscreen() {
        this.finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DeRegisterToken extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            ServiceHandler sh = new ServiceHandler();

            /**
             *  Making a request to url and getting response
             */
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            ////Log.d("Response: ", "> " + jsonStr);

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
        }

        @Override
        protected void onCancelled() {

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


        editor.commit();

        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finishscreen();
    }
}



