package com.galwaykart.Legal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaqActivity extends AppCompatActivity {


    TransparentProgressDialog pDialog;
    WebView webView;
  //  FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack(){
        Intent intent = new Intent(FaqActivity.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(FaqActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        //initNavigationDrawer();

//        Toolbar toolbar;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        CommonFun.setToolBar(toolbar,this);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle params = new Bundle();
//        params.putString("full_text", "faq");
//        mFirebaseAnalytics.logEvent("faq", params);


        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        webView = findViewById(R.id.webview);

        String url_part = "";
        String url = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                url_part = extras.getString("comefrom");
            }
        }

        if (!url_part.equals("") && !url_part.equals("customer-help-desk-tutorials.html")) {


//            Bundle bundle = new Bundle();
//            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "faq");
//            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "faq_name");
//            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            url = Global_Settings.terms_url_api + url_part;

            //Log.d("weburl",url);
            callAPI(url);
        }
        else
        {
            url=Global_Settings.webview_api+url_part;
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setJavaScriptEnabled(true);
            //wb.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            WebChromeClient myWebChromeClient = new MyWebChromeClient();
            webView.setWebChromeClient(myWebChromeClient);

            webView.loadUrl(url);

            pDialog = new TransparentProgressDialog(FaqActivity.this);
            //pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();
        }


    }

    private void callAPI(String url) {

        /**
         * Fetch ip of all the cart items
         */
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ////Log.d("weburl",response.toString());
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonArray.length() > 0) {

                                String webcontent=jsonObject.getString("content");
                                if(webcontent.length()>0 && !webcontent.equalsIgnoreCase("null")){
                                    setWebView(webcontent);
                                }
                                else
                                {
                                    setWebView(jsonObject.getString("title")+"<br/><br/><h3>-----</h3>");
                                }

                            }
                        }catch (JSONException ex){
                            //Log.d("weburl",ex.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                CommonFun.showVolleyException(error,FaqActivity.this);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);

    }


    private void setWebView(String webcontent){


        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        //wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        WebChromeClient myWebChromeClient = new MyWebChromeClient();
        webView.setWebChromeClient(myWebChromeClient);

        webView.loadData(webcontent, "text/html", "UTF-8");

        pDialog = new TransparentProgressDialog(FaqActivity.this);
        //pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.contains("galwaykart.com")) {
                view.loadUrl(url);
            } else {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub

            super.onPageStarted(view, url, favicon);
//                pDialog = new TransparentProgressDialog(MainActivity.this);
//                //pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(true);
//                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                pDialog.show();


        }


        /**
         * File name from URL
         *
         * @param url
         * @return
         */
        public String getFileName(String url) {
            String filenameWithoutExtension = "";
            filenameWithoutExtension = System.currentTimeMillis()
                    + "." + MimeTypeMap.getFileExtensionFromUrl(url);
            return filenameWithoutExtension;
        }


        @Override

        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);

            if (pDialog.isShowing())
                pDialog.dismiss();

        }

        // To handle "Back" key press event for WebView to go back to previous screen.

    }


    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            final JsResult finalRes = result;
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finalRes.confirm();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }
    }
}








