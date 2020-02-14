package com.galwaykart.shipyaari;

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
import com.galwaykart.Legal.FaqActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Common
 * Created by ankesh on 10/4/2017.
 */

public class TrackDetailWebViewActivity extends AppCompatActivity {


    TransparentProgressDialog pDialog;
    WebView webView;
    FirebaseAnalytics mFirebaseAnalytics;

    private void goBack(){
//        Intent intent = new Intent(TrackDetailWebViewActivity.this, HomePageActivity.class);
//        startActivity(intent);
        CommonFun.finishscreen(TrackDetailWebViewActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);


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
                url_part = extras.getString("trackUrl");
            }
        }



        if(url_part!=null && !url_part.equals("")){
            url=url_part;



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

            pDialog = new TransparentProgressDialog(TrackDetailWebViewActivity.this);
            pDialog.setCancelable(true);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();
        }

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

        pDialog = new TransparentProgressDialog(TrackDetailWebViewActivity.this);
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








