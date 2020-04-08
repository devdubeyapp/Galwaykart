package com.galwaykart.Payment.paytm_integrate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.OrderDetails;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.OrderListActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//implementing PaytmPaymentTransactionCallback to track the payment result.
public class  MainActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    //the textview in the interface where we have the price
    TextView textViewPrice;
    SharedPreferences pref;
    //JsonObjectRequest jsonObjRequest = null;
    TransparentProgressDialog pDialog;
    String cust_id="",order_id="";
    String total_amount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the textview
//        textViewPrice = findViewById(R.id.textViewPrice);
//
//
//        //attaching a click listener to the button buy
//        findViewById(R.id.buttonBuy).setOnClickLis tener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //calling the method generateCheckSum() which will generate the paytm checksum for payment
//                generateCheckSum();
//            }
//        });
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                total_amount= extras.getString("grand_total");
            }
        }


        if(total_amount.equals("")){
            cancelOrderEcom();
        }

        //Toast.makeText(this,total_amount.toString(),Toast.LENGTH_LONG).show();
        generateCheckSum();
    }


    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = total_amount;

        pref = CommonFun.getPreferences(getApplicationContext());

        String login_group_id=pref.getString("login_group_id","");


        if(login_group_id.equalsIgnoreCase("4"))
            cust_id = pref.getString("st_login_id","");
        else
            cust_id=pref.getString("st_login_id","");

//        cust_id = "DL222BB5";
        order_id = pref.getString("payment","");
        order_id = order_id.trim();

        //creating a retrofit object.
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(100, TimeUnit.SECONDS)
//                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                getResources().getString(R.string.paytmmid),
                Constants.CHANNEL_ID,
                order_id,
                cust_id,
                txnAmount,
                Constants.WEBSITE,
                getResources().getString(R.string.paytmcallbackurl),
                getResources().getString(R.string.paytmINDUSTRYTYPEID)
        );


        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

//                CommonFun.alertError(MainActivity.this,"Something went wrong..Please try again");
//                CommonFun.showVolleyException();

                Intent intent = new Intent(MainActivity.this, ExceptionError.class);
                startActivity(intent);
                CommonFun.finishscreen(MainActivity.this);

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        //PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put("MID",   getResources().getString(R.string.paytmmid));
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        // Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
        //Log.d("Paytmbundle",bundle.toString());

        String st_txn_status = bundle.getString("STATUS");
        String st_ORDERID = bundle.getString("ORDERID");
        String st_TXNAMOUNT = bundle.getString("TXNAMOUNT");

        //Log.d("Paytmbundle",st_txn_status);

        if(st_txn_status.equalsIgnoreCase("TXN_SUCCESS")){

            //String save_order_id_URL = Global_Settings.api_custom_url+"galwaykart/success_checkout_android_paytm.php?orderhash="+st_ORDERID;
            String save_order_id_URL=Global_Settings.api_url+"rest/V1/m-order/confirmed";
            saveOrderSales(save_order_id_URL,st_ORDERID);
        }
        else{

            pref = CommonFun.getPreferences(getApplicationContext());
            String st_Cancel_Order_URL= Global_Settings.api_url+"glaze/order_cancel.php?id="+pref.getString("orderhash","").trim();
            cancelOrder(st_Cancel_Order_URL);
        }

    }

    StringRequest stringRequest=null;
    private void saveOrderSales(String save_order_id_URL,String st_ORDERID) {

        String delivery_data_in="{" +
                "\"orderIncrementId\":\""+st_ORDERID+"\"," +
                "\"deviceNumber\":\"7\"" +
                "}";

        //Log.d("jsonObjectPay",delivery_data_in);

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            stringRequest = new StringRequest(Request.Method.POST, save_order_id_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            if (response != null) {

                                //Log.d("jsonObjectPay",response.toString());
                                try {

                                    JSONArray jsonArray=new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    //Log.d("jsonObjectPay",jsonObject+"");


                                    try
                                    {
                                        String st_save_status = jsonObject.getString("status");
                                        //String st_po_no = jsonObject.getString("PO_no");

//                                SharedPreferences.Editor editor=pref.edit();
//                                editor.putString("orderpono",st_po_no);
//                                editor.commit();



                                        Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(OrderDetails.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);

                                        final Dialog dialog = new Dialog(MainActivity.this);
                                        dialog.setContentView(R.layout.custom_alert_dialog_design);
                                        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                        ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);

                                        if(st_save_status.equalsIgnoreCase("1")) {

                                            tv_dialog.setText("Your Order placed successfully...");

                                            dialog.show();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {


                                                    goToOrderListActivity();

                                                }
                                            };


                                            Timer timer = new Timer();
                                            timer.schedule(timerTask, 4500);


                                        }
                                        else {

                                            tv_dialog.setText("Oops!!! Something Wrong\nCannot place your order\nPlease Try Again");
                                            dialog.show();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {


                                                    goToOrderListActivity();


                                                }
                                            };


                                            Timer timer = new Timer();
                                            timer.schedule(timerTask, 4500);
                                        }



                                    }
                                    catch(Exception ex)
                                    {
                                        goToOrderListActivity();

                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                    goToOrderListActivity();
                                }

                            }


                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    goToOrderListActivity();
                    //CommonFun.showVolleyException(error,MainActivity.this);

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();
                }

            }){


                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return delivery_data_in == null ? null : delivery_data_in.getBytes(StandardCharsets.UTF_8);
                }

//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<>();
//
//
//                    //////Log.d("delievery data in",delivery_data_in.toString());
//                    headers.put("Authorization", "Bearer " + tokenData);
//                    //headers.put("Content-Type","application/json");
//                    return headers;
//                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }

//        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
//                0, 0);
//        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        cancelOrderEcom();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        //Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
        cancelOrderEcom();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        //Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
        cancelOrderEcom();
    }


    private void cancelOrderEcom(){

        pref = CommonFun.getPreferences(getApplicationContext());
        //String st_Cancel_Order_URL= Global_Settings.api_url+"glaze/order_cancel.php?id="+pref.getString("orderhash","").trim();
       // cancelOrder(st_Cancel_Order_URL);
        goToOrderListActivity();
    }

    JsonObjectRequest jsonObjRequest=null;
    private void cancelOrder(String st_Cancel_Order_URL) {

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            jsonObjRequest = new JsonObjectRequest(Request.Method.GET, st_Cancel_Order_URL,null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            ////Log.d("jsonObject",jsonObject+"");

                            String st_Order_Cancel_Status = jsonObject.getString("orderdetails");
                            //Log.d("st_Order_Cancel_Status",st_Order_Cancel_Status);

                            final AlertDialog.Builder b;
                            try
                            {


                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("orderhash","");
                                editor.putString("paymentdue","");
                                editor.putString("orderpono","");
                                editor.commit();


                                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(OrderDetails.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                tv_dialog.setText("Oops!!! Something Wrong\nCannot place your order\nPlease Try Again");
                                dialog.show();

                                TimerTask timerTask=new TimerTask() {
                                    @Override
                                    public void run() {

                                        goToOrderListActivity();

                                    }};


                                Timer timer=new Timer();
                                timer.schedule(timerTask,4500);

                            }
                            catch(Exception ex)
                            {
                                goToOrderListActivity();

                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            goToOrderListActivity();
                        }

                    }


                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    goToOrderListActivity();
                    //CommonFun.showVolleyException(error,MainActivity.this);

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObjRequest.setShouldCache(false);
        queue.add(jsonObjRequest);

    }

    private void goToOrderListActivity() {
        Intent intent=new Intent(MainActivity.this, OrderListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(MainActivity.this);
    }
}
