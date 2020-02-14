package com.galwaykart.partialReturn;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumitsaini on 5/30/2018.
 */

public class TrackYourOrderRtn extends BaseActivity {


    String st_request_URL = "", st_trackingnumber = "", st_partnerID = "", st_partnerName = "",
            st_avnID = "", st_status = "", st_description = "", st_location = "", st_date_time = "", st_selected_Track_id;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    TextView tv_shipment_value, tv_tracking_no_value,
            tv_out_of_delivery, tv_in_transit, tv_pick_up_process, tv_order_placed, tv_delivered;

    ProgressBar tracking_vertical_bar;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(TrackYourOrderRtn.this, ReturnedOrderDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_rtn_order);
        initNavigationDrawer();
        pref = CommonFun.getPreferences(getApplicationContext());
        st_selected_Track_id = pref.getString("st_selected_Track_id", "");
        tracking_vertical_bar = findViewById(R.id.tracking_vertical_bar);


        tv_shipment_value = findViewById(R.id.tv_shipment_value);
        tv_tracking_no_value = findViewById(R.id.tv_tracking_no_value);

        tv_out_of_delivery = findViewById(R.id.tv_out_of_delivery);
        tv_in_transit = findViewById(R.id.tv_in_transit);
        tv_pick_up_process = findViewById(R.id.tv_pick_up_process);
        tv_order_placed = findViewById(R.id.tv_order_placed);
        tv_delivered = findViewById(R.id.tv_delivered);


        trackShipment();
    }

    private void trackShipment() {

        st_request_URL = Global_Settings.shipyaari_api_url + "trackorder.php?tracknumber=" + st_selected_Track_id;
        ////Log.d("st_request_URL", st_request_URL);

        pDialog = new TransparentProgressDialog(TrackYourOrderRtn.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_request_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            ////Log.d("VOLLEY12345", response);
                            try {

                                JSONObject object = new JSONObject(String.valueOf(response));

                                st_trackingnumber = object.getString("trackingnumber");
                                st_partnerID = object.getString("partnerID");
                                st_avnID = object.getString("avnID");
                                st_date_time = object.getString("date_time");
                                st_description = object.getString("description");
                                st_location = object.getString("location");
                                st_partnerName = object.getString("partnerName");
                                st_status = object.getString("status");

                                tv_shipment_value.setText(st_partnerName);
                                tv_tracking_no_value.setText(st_trackingnumber);

                                if (st_status.equalsIgnoreCase("Order Placed")) {
                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 0);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();

                                } else if (st_status.equalsIgnoreCase("Picked up and Booking Processed")) {
                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 25);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();
                                } else if (st_status.equalsIgnoreCase("In-transit")) {
//                                    tracking_vertical_bar.setProgress(50,true);
                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 50);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();
                                } else if (st_status.equalsIgnoreCase("Out for Delivery")) {
                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 75);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();
                                } else if (st_status.equalsIgnoreCase("Delivered")) {
                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 15);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();
                                } else if (st_status.equalsIgnoreCase("Cancelled")) {

                                    tv_delivered.setText("Cancelled");
                                    tv_in_transit.setVisibility(View.GONE);
                                    tv_out_of_delivery.setVisibility(View.GONE);
                                    tv_pick_up_process.setVisibility(View.GONE);

                                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(tracking_vertical_bar, "progress", 0, 100);
                                    progressAnimator.setDuration(3000);
                                    progressAnimator.setInterpolator(new LinearInterpolator());
                                    progressAnimator.start();
                                }


                            } catch (Exception e) {
                                //e.printStackTrace();
                                CommonFun.alertError(TrackYourOrderRtn.this, e.toString());
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error, TrackYourOrderRtn.this);
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

//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return input_data == null ? null : input_data.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data, "utf-8");
//                        return null;
//                    }
//                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();


                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };

            RetryPolicy retryPolicy = new DefaultRetryPolicy(1000 * 60,
                    0,
                    0);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...", "Error");
        }


    }
}

