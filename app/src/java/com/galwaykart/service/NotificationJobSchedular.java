package com.galwaykart.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankesh on 4/3/2018.
 */

public class NotificationJobSchedular extends JobService {

    JobParameters jobParameters;
    String tokenData="";

    @Override
    public boolean onStartJob(JobParameters params) {


        Toast.makeText(this,"job start",Toast.LENGTH_LONG).show();
        this.jobParameters=params;
        refreshItemCount(this);



        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public void refreshItemCount(Context context){

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        tokenData=pref.getString("tokenData","");

        String url_cart_item_list = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/carts/mine";
        callCartItemList(url_cart_item_list,context);

    }

    private void callCartItemList(String url_cart_item_list, final Context context) {

        final String TAG_total_item_count = "items_qty";
        tokenData = tokenData.replaceAll("\"", "");


        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            int total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));

                            if(total_cart_count>0)
                            {
                                showNotificationToUser(total_cart_count);
                            }
                            //initNavigationDrawer();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
                        //////Log.d("ERROR", "error => " + error.toString());
                        //CommonFun.alertError(context, error.toString());

//                        Intent intent=new Intent(BaseActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);

                        // Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();

                        if (error instanceof ParseError || error instanceof ServerError) {
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count =0;
                            //initNavigationDrawer();
                           // updateMenuTitles(toolbar, String.valueOf(total_cart_count),"false");

                        }
                        else {
                          //  Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                // params.put("Authorization", "Bearer "  );
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);

    }

    /**
     * show notification to user
     * that there are items in cart and
     * he can proceed to checkout
     */
    private void showNotificationToUser(int total_cart_amount){

        RemoteViews contentViewBig,contentViewSmall;
        contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification_panel);

        long when = System.currentTimeMillis();


        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activityIntent = new Intent(this, SplashActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(this.getString(R.string.app_name))
                        //.setStyle(new NotificationCompat.BigTextStyle()
                         .setCustomContentView(contentViewBig)
                        .setCustomBigContentView(contentViewBig)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setWhen(when)
                        .setContentIntent(contentIntent)
                        //.setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setAutoCancel(true);

        //          .bigText("You have added "+total_cart_amount+" items in your cart\nPlease enjoy shopping"))
        // .setContentText("You have added "+total_cart_amount+" items in your cart\nPlease enjoy shopping")



        contentViewBig.setTextViewText(R.id.title,"Galwaykart");
        contentViewBig.setTextViewText(R.id.text,"You have "+total_cart_amount+" items in your cart\nPlease enjoy shopping");

        notificationManager.notify(0, mBuilder.build());

    }

}
