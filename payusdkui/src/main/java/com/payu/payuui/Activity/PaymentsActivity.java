package com.payu.payuui.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.util.Log;

import com.payu.gpay.GPay;
import com.payu.gpay.callbacks.PayUGPayCallback;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Payu.PayuConstants;

public class PaymentsActivity extends FragmentActivity {
    Bundle bundle;
    String url;
    PayuConfig payuConfig;
    String txnId = null;
    String merchantKey;
    String userCredentials;
    String TAG = "PayU";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "oncreate class name:" + getClass().getCanonicalName());
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
            url = payuConfig.getEnvironment() == PayuConstants.PRODUCTION_ENV ? PayuConstants.PRODUCTION_PAYMENT_URL : PayuConstants.TEST_PAYMENT_URL;
            String[] list = payuConfig.getData().split("&");

            for (String item : list) {
                String[] items = item.split("=");
                if (items.length >= 2) {
                    String id = items[0];
                    switch (id) {
                        case "txnid":
                            txnId = items[1];
                            break;
                        case "key":
                            merchantKey = items[1];
                            break;
                        case "var1":
                            userCredentials = items[1];
                            break;
                    }
                }
            }

        }

        paymentThroughGPayStandalone();

    }

    private void paymentThroughGPayStandalone(){
        GPay.getInstance().makePayment(this,payuConfig.getData(),payUGPayCallback,merchantKey,null);
    }

    // Callback for GPay
    PayUGPayCallback payUGPayCallback = new PayUGPayCallback() {
        @Override
        public void onPaymentFailure(String payuResponse, String merchantResponse) {
            Intent intent = new Intent();
            intent.putExtra("payu_response", payuResponse);
            intent.putExtra("result",merchantResponse);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }



        @Override
        public void onPaymentSuccess(String payuResponse, String merchantResponse) {
            //Called when Payment Succeeded

            Intent intent = new Intent();
            intent.putExtra("payu_response", payuResponse);
            intent.putExtra("result",merchantResponse);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();

        }

        @Override
        public void onGpayErrorReceived(int errorCode, String description) {

        }
    };
}
