package com.galwaykart.MultiStoreSelection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class GetCurrentZone extends AppCompatActivity {

    public static final String EXTRA_DATA = "current_zone";
    String tokenData="";
    TransparentProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_loading);
        getCurrentZone(GetCurrentZone.this);
    }

    private  void getCurrentZone(Context context)
    {

        SharedPreferences pref= CommonFun.getPreferences(context);
        tokenData=pref.getString("tokenData","");
        String st_current_zone= Global_Settings.api_url+"rest/V1/website/code";
        tokenData = tokenData.replaceAll("\"", "");


        RequestQueue queue = Volley.newRequestQueue(context);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET,
                st_current_zone,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("responseCurrentZone", response.toString());



                        response = response.replaceAll("\"", "");
                        final Intent data=new Intent();
                        data.putExtra(EXTRA_DATA,response.trim().toString());
                        setResult(Activity.RESULT_OK,data);
                        finish();


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        //progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,GetCurrentZone.this);

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }


        };
        jsObjRequest.setShouldCache(false);
        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                1,
                1);
        jsObjRequest.setRetryPolicy(retryPolicy);
        queue.add(jsObjRequest);


    }

}
