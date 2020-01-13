package com.galwaykart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankesh on 4/16/2018.
 */

public class EwalletActivity extends BaseActivityWithoutCart {

    TextView text_ewallet;
    SharedPreferences pref;
    TransparentProgressDialog pDialog;
    JSONArray dist_details = null;
    float ewallet_amount=0;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EwalletActivity.this,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewallet);
        initNavigationDrawer();

        text_ewallet=(TextView)findViewById(R.id.text_ewallet);

        pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");

        String user_detail_url= Global_Settings.user_details_url+pref.getString("login_customer_id","");

        getUserDetails(user_detail_url);
    }



    /**
     * get User details (ID and Zone).
     * @param url
     */
    private void getUserDetails(String url){


        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        final RequestQueue requestQueue= Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        try {
                            JSONObject jsonObject=new JSONObject(String.valueOf(response));

                            JSONObject jsonObject1=jsonObject.getJSONObject("details");

                            String jsonObject_fcode=jsonObject1.getString("fcode");
                            String jsonObject_distid=jsonObject1.getString("distributor_id");


                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("log_user_id",jsonObject_distid);
                            editor.putString("log_user_zone",jsonObject_fcode);
                            editor.commit();



                            ////Log.d("distid",jsonObject_distid);
                            ////Log.d("distzone",jsonObject_fcode);
                            //getPaymentMethod(shipping_info_string);

                            /**
                             *  Fetch user zone from the sales
                             */
                            getDistributorDetails(jsonObject_distid);


                            //CommonFun.alertError(Payment_Method_Activity.this,jsonObject_fcode+" "+jsonObject_distid);

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(pDialog.isShowing())
                    pDialog.dismiss();

                CommonFun.showVolleyException(error,EwalletActivity.this);
                //CommonFun.alertError(Payment_Method_Activity.this,error.toString());
            }
        });

        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * fetch user zone from sales
     * @param st_dist_id
     *
     */
    private void getDistributorDetails(String st_dist_id) {


        pref = CommonFun.getPreferences(getApplicationContext());

        // String st_dist_id=pref.getString("st_dist_id","");

        String st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_verify_guest?ID="+st_dist_id;
        ////Log.d("st_Get_Dist_details_URL",st_Get_Dist_details_URL);

        pDialog = new TransparentProgressDialog(EwalletActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Dist_details_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){
                            try {

                                dist_details = new JSONArray(String.valueOf(response));
                                JSONObject dist_details_object =dist_details.getJSONObject(0);

                                String current_zone = dist_details_object.getString("current_zone");

                                if(!dist_details_object.getString("e_Creditamt").equals(""))
                                    ewallet_amount= Float.parseFloat(dist_details_object.getString("e_Creditamt"));

                                pref = CommonFun.getPreferences(getApplicationContext());

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("st_dist_id",current_zone);
                                editor.commit();

                                text_ewallet.setText("Your E-wallet  amount is "+ewallet_amount);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());


                                final AlertDialog.Builder b;
                                try
                                {
                                    b = new AlertDialog.Builder(EwalletActivity.this);
                                    b.setTitle("Alert");
                                    b.setCancelable(false);
                                    b.setMessage("Something Wrong!!! Try Again\nor Id is not active or id is not valid");
                                    b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton)
                                        {
                                            b.create().dismiss();

                                            Intent intent=new Intent(EwalletActivity.this,HomePageActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);
                                            CommonFun.finishscreen(EwalletActivity.this);


                                        }
                                    });

                                    b.create().show();
                                }
                                catch(Exception ex)
                                {
                                }



                            }
                        }
                    }




                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

//                CommonFun.alertError(RegistrationActivity.this,error.toString());
//                error.printStackTrace();
                CommonFun.showVolleyException(error,EwalletActivity.this);

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

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }


}
