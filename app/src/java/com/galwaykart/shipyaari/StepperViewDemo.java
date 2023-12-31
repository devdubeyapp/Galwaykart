package com.galwaykart.shipyaari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
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
import com.baoyachi.stepview.VerticalStepView;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.essentialClass.XmlToJson;
import com.galwaykart.profile.OrderDetails;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by sumitsaini on 5/30/2018.
 */

public class StepperViewDemo extends BaseActivity {

    VerticalStepView step_view;

    String st_request_URL = "",st_trackingnumber="",st_partnerID="",st_partnerName="",
            st_selected_shipping_type="",st_shipment_url="",
            st_avnID="",st_status="",st_description="",st_location="",st_date_time="",st_selected_Track_id="";
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    TextView tv_shipment_value,tv_tracking_no_value,tv_status_value,tv_dt_value,tv_status;
    List<String> list0,list1,list2;
    RelativeLayout common_header3;
    Animation progressAnimator;

    DocumentBuilderFactory builderFactory;
    InputStream inputStream;



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
       goBack();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_view);
        initNavigationDrawer();
        step_view = (VerticalStepView) findViewById(R.id.step_view);

        tv_shipment_value = (TextView) findViewById(R.id.tv_shipment_value);
        tv_tracking_no_value = (TextView) findViewById(R.id.tv_tracking_no_value);
        tv_status_value = (TextView) findViewById(R.id.tv_status_value);
        tv_dt_value = (TextView) findViewById(R.id.tv_dt_value);
        tv_status = (TextView)findViewById(R.id.tv_status);

        common_header3 = (RelativeLayout) findViewById(R.id.common_header3);

        pref = CommonFun.getPreferences(getApplicationContext());
        st_selected_Track_id = pref.getString("st_selected_Track_id", "");
        st_selected_shipping_type = pref.getString("st_selected_shipping_type","");

        //Log.d("st_selected_Track_id", st_selected_Track_id);
        //Log.d("st_shipping_type", st_selected_shipping_type);

        if(st_selected_Track_id.equalsIgnoreCase("") || st_selected_Track_id.equalsIgnoreCase("null") ){
            st_selected_Track_id = "0";
        }

        list0 = new ArrayList<>();
        list0.add("Order Placed");
        list0.add("Picked up and Booking Processed");
        list0.add("In Transit");
        list0.add("Out for Delivery");
        list0.add("delivered");

        list1 = new ArrayList<>();
        list1.add("Order Placed");
        list1.add("Picked up and Booking Processed");
        list1.add("In Transit");
        list1.add("Out for Delivery");
        list1.add("Delivered");

        list2 = new ArrayList<>();
        list2.add("Order Placed");
        list2.add("Cancelled");

        if(st_selected_shipping_type.equalsIgnoreCase("1")) {

        if (st_selected_Track_id.equalsIgnoreCase("0"))
            setTrackBar(list0, 4);

        else {
            common_header3.setVisibility(View.VISIBLE);
            trackShipment();
        }
        }
        else if(st_selected_shipping_type.equalsIgnoreCase("2")) {
            if (st_selected_Track_id.equalsIgnoreCase("0"))
                setTrackBar(list0, 4);

            else {
                common_header3.setVisibility(View.VISIBLE);
                trackShipmentBlueDart();
            }

        }
        else if(st_selected_shipping_type.equalsIgnoreCase("3")) {

            if (st_selected_Track_id.equalsIgnoreCase("0"))
                setTrackBar(list0, 4);

            else {
                common_header3.setVisibility(View.VISIBLE);
                trackShipmentFedex();
            }
        }
           else {
            setTrackBar(list0, 4);
        }
    }

    private void trackShipmentFedex() {
      st_shipment_url = Global_Settings.galway_api_url+"returnapi/FedexTracking/"+st_selected_Track_id;
      //Log.d("st_shipment_url",st_shipment_url);

        pDialog = new TransparentProgressDialog(StepperViewDemo.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, st_shipment_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                           if(!response.equals("")){
//                            CommonFun.alertError(StepperViewDemo.this,response.toString());
                                try {

                                    JSONObject jsonObj =  new JSONObject(response);
                                    String st_status = jsonObj.getString("TrackStatus");
                                    String courier_partner = jsonObj.getString("ServiceInfo");
                                    String tracking_no = jsonObj.getString("TrackingNumber");
//                                    CommonFun.alertError(StepperViewDemo.this, st_status.toString());

//                                    st_status=st_status.replace("null","");
                                    tv_shipment_value.setText(courier_partner);
                                    tv_tracking_no_value.setText(tracking_no);
                                    tv_status_value.setVisibility(View.VISIBLE);

                                    tv_dt_value.setVisibility(View.GONE);
//                                    tv_dt_value.setText(st_date  +  st_time);

                                if(!TextUtils.isEmpty(st_status) && !st_status.equalsIgnoreCase("null" )) {
                                    tv_status_value.setText(st_status);
                                    tv_status.setVisibility(View.VISIBLE);
                                    if (st_status.toLowerCase().contains("picked up") || st_status.toLowerCase().contains("(pu)")) {

                                        setTrackBar(list0, 3);
                                    } else if (st_status.toLowerCase().contains("in transit") || (st_status.toLowerCase().contains("(it)"))) {
                                        setTrackBar(list0, 2);
                                    } else if (st_status.toLowerCase().contains("on delivery") ||
                                            st_status.toLowerCase().contains("(od)")) {
                                        setTrackBar(list0, 1);
                                    } else if (st_status.toLowerCase().contains("delivered") || st_status.toLowerCase().contains("(dl)")) {
                                        setTrackBar(list0, 0);
                                    } else if (st_status.equalsIgnoreCase("Cancelled")) {

                                        setTrackBar(list2, 0);
                                    } else if (st_status.equalsIgnoreCase("Return")) {

                                        setTrackBar(list1, 0);
                                    }
                                }
                                else if(!tracking_no.equals("") && tracking_no!=null)
                                {
                                    setTrackBar(list0,3);
                                }
                                else{

                                    tv_status.setVisibility(View.GONE);
                                    alertError(StepperViewDemo.this,"Unable to fatch data,Please try again.");
                                }

                                } catch (Exception e) {
                                    //e.printStackTrace();
//                                CommonFun.alertError(StepperViewDemo.this,e.toString());
                                    Intent intent=new Intent(StepperViewDemo.this, ExceptionError.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                CommonFun.alertError(StepperViewDemo.this,"Please Try Again");
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,StepperViewDemo.this);
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


                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    0,
                    0);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }
    }

    private void trackShipment() {

        st_request_URL = Global_Settings.shipyaari_api_url+"trackorder.php?tracknumber="+st_selected_Track_id;
        ////Log.d("st_request_URL",st_request_URL);

        pDialog = new TransparentProgressDialog(StepperViewDemo.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_request_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            ////Log.d("VOLLEY12345", response);
                            try {

                                JSONObject object = new JSONObject(String.valueOf(response));

                                st_trackingnumber = object.getString("trackingnumber");
                                st_partnerID = object.getString("partnerID");
                                st_avnID = object.getString("avnID");
                                st_date_time = object.getString("date_time");
                                st_description = object.getString("description");
                                st_location= object.getString("location");
                                st_partnerName = object.getString("partnerName");
                                st_status = object.getString("status");

                                tv_shipment_value.setText(st_partnerName);
                                tv_tracking_no_value.setText(st_trackingnumber);


                                if(!TextUtils.isEmpty(st_status) && !st_status.equalsIgnoreCase("null" )) {

                                    tv_status.setText(st_status);
                                    tv_status.setVisibility(View.VISIBLE);

                                    if (st_status.equalsIgnoreCase("Picked up and Booking Processed")) {

                                        setTrackBar(list0, 3);
                                    } else if (st_status.equalsIgnoreCase("In-transit")) {
                                        setTrackBar(list0, 2);
                                    } else if (st_status.equalsIgnoreCase("Out for Delivery")) {
                                        setTrackBar(list0, 1);
                                    } else if (st_status.equalsIgnoreCase("delivered")) {
                                        setTrackBar(list0, 0);
                                    } else if (st_status.equalsIgnoreCase("Cancelled")) {

                                        setTrackBar(list2, 0);
                                    } else if (st_status.equalsIgnoreCase("Return")) {

                                        setTrackBar(list1, 0);
                                    }
                                }
                                else{
                                    tv_status.setVisibility(View.GONE);
                                    alertError(StepperViewDemo.this,"Unable to fatch data,Please try again.");
                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
//                                CommonFun.alertError(StepperViewDemo.this,e.toString());
                                Intent intent=new Intent(StepperViewDemo.this, ExceptionError.class);
                                startActivity(intent);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,StepperViewDemo.this);
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


                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    0,
                    0);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }




    }

    private void trackShipmentBlueDart() {

        st_request_URL = "http://api.bluedart.com/servlet/RoutingServlet?" +
                "handler=tnt&action=custawbquery&loginid=SO953293" +
                "&lickey=41e8fc6ad96373d5f2f04210716ac5b3" +
                "&verno=1.3&awb=awb" +
                "&format=xml&numbers="+st_selected_Track_id;
        ////Log.d("st_request_URL",st_request_URL);

        pDialog = new TransparentProgressDialog(StepperViewDemo.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_request_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            String xmlString=response;  // some XML String previously created

                            xmlString=xmlString.replace("ï»¿","");
                            //Fun.alertError(xmlString,XmlTestData.this);

                            XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
                            // convert to a JSONObject

                            if(!xmlString.equals("")) {

                                // convert to a formatted Json String
                                String formatted = xmlToJson.toFormattedString();
                                //Log.d("data_xml",formatted);


//                            CommonFun.alertError(StepperViewDemo.this,response.toString());
                            try {

                                JSONObject jsonObj = new JSONObject(formatted);

                                JSONObject obj1 = jsonObj.getJSONObject("ShipmentData");
                                JSONObject sub_obj = obj1.getJSONObject("Shipment");

                                String st_status = sub_obj.getString("Status");
                                String st_date = sub_obj.getString("StatusDate");
                                String st_time = sub_obj.getString("StatusTime");
//                                CommonFun.alertError(StepperViewDemo.this, status.toString());

                                tv_shipment_value.setText(st_partnerName);
                                tv_tracking_no_value.setText(st_trackingnumber);
                                tv_status_value.setVisibility(View.VISIBLE);

                                tv_dt_value.setVisibility(View.VISIBLE);
                                tv_dt_value.setText(st_date + st_time);

                                if(!TextUtils.isEmpty(st_status) && !st_status.equalsIgnoreCase("null" )) {
                                    tv_status_value.setText(st_status);
                                    tv_status.setVisibility(View.VISIBLE);
                                if (st_status.equalsIgnoreCase("SHIPMENT OUTSCANNED TO NETWORK")) {

                                    setTrackBar(list0, 3);
                                } else if (st_status.equalsIgnoreCase("Shipment Inscan")) {
                                    setTrackBar(list0, 2);
                                } else if (st_status.equalsIgnoreCase("Shipment Outscan")) {
                                    setTrackBar(list0, 1);
                                } else if (st_status.equalsIgnoreCase("Shipment delivered")) {
                                    setTrackBar(list0, 0);
                                } else if (st_status.equalsIgnoreCase("Cancelled")) {

                                    setTrackBar(list2, 0);
                                } else if (st_status.equalsIgnoreCase("Return")) {

                                    setTrackBar(list1, 0);
                                }
                            }
                                else{

                                    tv_status.setVisibility(View.GONE);
                                    alertError(StepperViewDemo.this,"Unable to fatch data,Please try again.");
                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
//                                CommonFun.alertError(StepperViewDemo.this,e.toString());
                                Intent intent=new Intent(StepperViewDemo.this, ExceptionError.class);
                                startActivity(intent);
                            }
                            }
                            else
                            {
                                CommonFun.alertError(StepperViewDemo.this,"No data found");
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,StepperViewDemo.this);
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


                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    0,
                    0);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.d("error...","Error");
        }
    }


    private void setTrackBar(List<String> list,int index) {

        step_view.setStepsViewIndicatorComplectingPosition(list.size() - index)
                .reverseDraw(false)
                .setStepViewTexts(list)
                .setLinePaddingProportion(1.25f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(StepperViewDemo.this, R.color.colorPrimary))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(StepperViewDemo.this, R.color.colorPrimary))
                .setStepViewComplectedTextColor(ContextCompat.getColor(StepperViewDemo.this, R.color.colorPrimary))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(StepperViewDemo.this, R.color.colorPrimary))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(StepperViewDemo.this, R.drawable.greencheck))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(StepperViewDemo.this, R.drawable.uncheckmark))
                .setTextSize(16)
                .setPadding(10,10,10,20);




    }

    public  void alertError(Context ctx, String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(ctx);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    goBack();
                    b.create().dismiss();
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

    private  void goBack() {
        Intent intent=new Intent(StepperViewDemo.this,OrderDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }
}
