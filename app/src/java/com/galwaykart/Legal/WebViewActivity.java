package com.galwaykart.Legal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.R;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Common
 * Created by ankesh on 10/4/2017.
 */

public class WebViewActivity extends BaseActivityWithoutCart {


    TransparentProgressDialog pDialog;
    WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        initNavigationDrawer();
        webView = (WebView) findViewById(R.id.webview);

        String url_part = "";
        String url = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                url_part = extras.getString("comefrom");
            }
        }

        if (!url_part.equals("")) {
            url = Global_Settings.terms_url_api + url_part;
        }

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

        pDialog = new TransparentProgressDialog(WebViewActivity.this);
        //pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

    }

    private class MyBrowser extends WebViewClient {


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
            filenameWithoutExtension = String.valueOf(System.currentTimeMillis()
                    + "." + MimeTypeMap.getFileExtensionFromUrl(url));
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








