package com.galwaykart.essentialClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by ankesh on 9/13/2017.
 */

public class GetAccessToken {

    Context mctx;
    String uname;
    String pwd;
    String tokenData;

    public GetAccessToken(String username, String password, Context context){

        this.mctx=context;
        this.uname=username;
        this.pwd=password;
        String url=com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/integration/customer/token";
        getTokenFromVolley(url);
    }

    private void getTokenFromVolley(String fromurl){

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", uname);
            jsonBody.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(mctx);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, fromurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tokenData=response;
                SharedPreferences pref;
                pref= mctx.getSharedPreferences("glazekartapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("tokenData",tokenData);
                editor.commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//
//                    responseString = String.valueOf(response);
//
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }



        };

        queue.add(stringRequest);

    }


}
