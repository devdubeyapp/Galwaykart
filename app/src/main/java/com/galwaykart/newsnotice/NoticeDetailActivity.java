package com.galwaykart.newsnotice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeDetailActivity extends AppCompatActivity {

    TransparentProgressDialog pDialog;
    WebView webView;
    String url_part = "";
    String url;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        CommonFun.finishscreen(NoticeDetailActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);


        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        webView = findViewById(R.id.webview);

        url = Global_Settings.api_url+"rest/V1/m-static-page/";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                url_part = extras.getString("identifier_link");
                Log.e("url_part", url_part);
            }
        }

        if (!url_part.equals("")) {
            url = url + url_part;
            jsonNoticeDetails();
            Log.e("url", url);
        }

    }



    public void jsonNoticeDetails() {

        final ArrayList<NoticeModel> notice_list1 = new ArrayList<>();

        //Log.d("st_notice_url",url);
        pDialog = new TransparentProgressDialog(NoticeDetailActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(NoticeDetailActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("newsResponse", response);
                                    if (response != null) {
                                        try {
                                            JSONArray  jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            Log.e("jsonObject",jsonObject.length() + "");

                                            for(int j = 0; j<jsonObject.length(); j++) {
                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                                String  status = jsonObjFinal.getString("status");
                                                {
                                                    if(status.equals("1"))
                                                    {
                                                        String  title = jsonObjFinal.getString("title");
                                                        String  content = jsonObjFinal.getString("content");
                                                        webView.loadData(content, "text/html", "UTF-8");
                                                    }
                                                }

                                            }
                                        }
                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            //String err_msg="currently, there is no news or notice available";
                                            //Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                                            //tv_notice.setText(err_msg);
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            return params;
                        }
                    };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
