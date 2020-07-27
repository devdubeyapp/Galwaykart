package com.galwaykart.helpdesksupport.mycomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComplaintDetailActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView tv_date, tv_ticket_no,tv_status, tv_order_no,tv_order_status, tv_category_type, tv_remark, tv_admin_remark;
    TextView str_date, str_ticket_no,str_status, str_order_no, str_category_type;
    LinearLayout ly_admin_remark, details_ly;

    private String complaint_id="", complaint_type="", is_show="";
    private SharedPreferences pref;
    private String st_token_data="";
    String remarks="", customer_remarks="", admin_remarks="";

    TransparentProgressDialog pDialog;
    RecyclerView complaint_details_recycler_view;
    public ComplaintItemAdapter complaintItemAdapter;

    TextView tv_cancel_complaint;
    String customer_id="";
    String setStatus_id="";
    String request_type="";

    LinearLayout ly_order_no, ly_order_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);


        Intent intent = getIntent();
        complaint_id= intent.getStringExtra("complaint_id");
        complaint_type= intent.getStringExtra("complaint_type");
        is_show= intent.getStringExtra("is_show");
        //remarks= intent.getStringExtra("remarks");
        customer_id=intent.getStringExtra("customer_id");
        setStatus_id=intent.getStringExtra("setStatus_id");
        request_type = intent.getStringExtra("request_type");

        Log.e("request_type_deta", request_type);

        tv_cancel_complaint=findViewById(R.id.tv_cancel_complaint);

        if(setStatus_id.equals("1")){
            tv_cancel_complaint.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_cancel_complaint.setVisibility(View.GONE);
        }
        tv_cancel_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder b;
                try
                {
                    b = new AlertDialog.Builder(ComplaintDetailActivity.this);
                    b.setTitle("Alert");
                    b.setCancelable(false);
                    b.setMessage("Are you sure want to cancel your complaint ?");
                    b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            b.create().dismiss();
                            String input_data="{\"requestIdHash\" : \""+complaint_id+"\", " +
                                    "\"customerId\": "+customer_id+"}";

                            String complaint_cancel_url=Global_Settings.api_url+"rest/V1/m-help-requestcancle";
                            callCancelComplaint(input_data,complaint_cancel_url);

                            /*Intent intent11 = new Intent(ComplaintDetailActivity.this, MyComplaints.class);
                            startActivity((intent11));*/

                        }
                    });
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            b.create().dismiss();
                        }
                    });
                    b.create().show();
                }
                catch(Exception ex)
                {
                }



            }
        });

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
//        String st_remarks="";
//        if (Build.VERSION.SDK_INT >= 24) {
//            st_remarks =  Html.fromHtml(st_remarks, 0);
//        } else {
//            st_remarks =  Html.fromHtml(st_remarks);
//        }


        Log.e("complaint_id",complaint_id);
        Log.e("complaint_type",complaint_type);
        Log.e("is_show",is_show);

        Init();
    }


    private void callCancelComplaint(String input_data,String url)
    {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ComplaintDetailActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("cancelComplaint", response);
                                    if (response != null) {
                                        if(Boolean.parseBoolean(response)==true){

                                            Dialog dialog = new Dialog(ComplaintDetailActivity.this);
                                            dialog.setContentView(R.layout.custom_alert_dialog_design);
                                            TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                            tv_dialog.setText("your complaint has been successfully cancelled");
                                            ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                            dialog.show();

                                            new CountDownTimer(2000, 2000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    // TODO Auto-generated method stub



                                                }
                                                @Override
                                                public void onFinish() {
                                                    if (dialog.isShowing())
                                                        dialog.dismiss();

                                                    jsonComplaintItem();
                                                    tv_cancel_complaint.setVisibility(View.GONE);

                                                }
                                            }.start();


                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            CommonFun.showVolleyException(error,ComplaintDetailActivity.this);
                            // Log.e("error_re",error.getMessage());

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
                        public byte[] getBody() throws AuthFailureError {
                            return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
                        }

                        @Override
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + st_token_data);
                            //Log.e("st_token_data..",st_token_data);
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

    void Init()
    {
        pref = CommonFun.getPreferences(ComplaintDetailActivity.this);
        st_token_data=pref.getString("tokenData","");

        tv_date = findViewById(R.id.tv_date);
        tv_ticket_no = findViewById(R.id.tv_ticket_no);
        tv_status = findViewById(R.id.tv_status);
        tv_order_no = findViewById(R.id.tv_order_no);
        tv_order_status  = findViewById(R.id.tv_order_status);
        tv_category_type = findViewById(R.id.tv_category_type);
        tv_remark = findViewById(R.id.tv_remark);
        tv_admin_remark = findViewById(R.id.tv_admin_remark);



        ly_admin_remark = findViewById(R.id.ly_admin_remark);
        ly_admin_remark.setVisibility(View.GONE);

        details_ly = findViewById(R.id.details_ly);
        details_ly.setVisibility(View.GONE);


        /*back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(this);*/


        ly_order_no = findViewById(R.id.ly_order_no);
        ly_order_status = findViewById(R.id.ly_order_status);

        if(request_type.equalsIgnoreCase("2"))
        {
            ly_order_no.setVisibility(View.GONE);
            ly_order_status.setVisibility(View.GONE);
        }



        complaint_details_recycler_view= findViewById(R.id.complaint_details_recycler_view);
        complaint_details_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        complaint_details_recycler_view.setLayoutManager(mLayoutManager);

        jsonComplaintItem();
    }



    @Override
    public void onClick(View v) {
    }

    String input_data="";
    String[] cht_name;
    String[] cht_msg;
    private void jsonComplaintItem() {

        final ArrayList<ComplModel> complModels12 = new ArrayList<>();
        String st_mycomplaint_details_url = Global_Settings.api_url + "rest/V1/m-help-detail";

        input_data = "{\"requestId\":\""+complaint_id+"\"}";
        Log.e("input_data",input_data);
        //Log.d("complaint_details_url",st_mycomplaint_details_url);

        pref = CommonFun.getPreferences(ComplaintDetailActivity.this);
        String st_token_data=pref.getString("tokenData","");

        pDialog = new TransparentProgressDialog(ComplaintDetailActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ComplaintDetailActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.POST, st_mycomplaint_details_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("MyComplaintItemResponse", response);
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            String order_status = "";
                                            String order_id = "";
                                            String complaint_status = "";
                                            String customer_name = "";
                                            String created_at = "";
                                            String customer_id = "";
                                            String request_qty = "";
                                            String sku = "";
                                            String product_name = "";


//                                            for(int j = 0; j<jsonArray.length(); j++) {
                                            JSONObject jsonObj= jsonArray.getJSONObject(0);
                                            JSONArray jsonArrayOrder = jsonObj.getJSONArray("order");
                                            for(int k=0; k<jsonArrayOrder.length();k++)
                                            {
                                                JSONObject jsonObjOrder = jsonArrayOrder.getJSONObject(k);
                                                order_status = jsonObjOrder.getString("order_status");
                                                order_id = jsonObjOrder.getString("increment_id");
                                                complaint_status = jsonObjOrder.getString("complientstatus");
                                                created_at = jsonObjOrder.getString("created_at");


//                                                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                                                    Date createDate=sdf.parse(created_at);

                                                tv_date.setText(created_at);
                                                tv_ticket_no.setText(complaint_id);
                                                tv_order_no.setText(order_id);
                                                tv_order_status.setText(order_status);
                                                tv_status.setText(complaint_status);
                                                tv_category_type.setText(complaint_type);


//                                                    if(!remarks.equals("")) {
//                                                        remarks=remarks.replaceAll("<p>","");
//                                                        remarks=remarks.replaceAll("</p>","");
//                                                        tv_remark.setText(remarks);
//                                                    }


                                                int total_msg_data=0;

                                                if(jsonObj.has("message")) {
                                                    JSONArray jsonArrayItem = jsonObj.getJSONArray("message");
                                                    total_msg_data=jsonArrayItem.length();

                                                    if (jsonArrayItem.length() > 0) {

                                                        cht_name = new String[jsonArrayItem.length()];
                                                        cht_msg = new String[jsonArrayItem.length()];

                                                        for (int i = 0; i < jsonArrayItem.length(); i++) {

                                                            JSONObject jsonObject_msg=jsonArrayItem.getJSONObject(i);
                                                            cht_msg[i] = jsonObject_msg.getString("message");
                                                            cht_name[i] = jsonObject_msg.getString("name");
                                                        }

                                                    }
                                                }

                                                Log.e("total_msg_data", total_msg_data + "");

                                                for(int i=0;i<total_msg_data;i++)
                                                {
                                                    //remarks=remarks+"<p style='font-size:16px;font-weight:bold'>"+cht_name[i]+":</p><p style='font-size:14px'>"+cht_msg[i]+"</p><br/>";

                                                    customer_remarks=cht_msg[0];
                                                    tv_remark.setText(customer_remarks);

                                                    if(total_msg_data>1)
                                                    {
                                                        admin_remarks=cht_msg[1];
                                                        ly_admin_remark.setVisibility(View.VISIBLE);
                                                        tv_admin_remark.setText(admin_remarks);
                                                    }
                                                    else
                                                    {
                                                        tv_remark.setText(customer_remarks);
                                                    }

                                                    Log.e("customer_remarks_f", customer_remarks);
                                                    Log.e("admin_remarks", admin_remarks);

                                                    if (Build.VERSION.SDK_INT >= 24) {
                                                        //remarks = String.valueOf(Html.fromHtml(remarks , 0));
                                                        customer_remarks = String.valueOf(Html.fromHtml(customer_remarks , 0));
                                                        admin_remarks = String.valueOf(Html.fromHtml(admin_remarks , 0));

                                                        tv_remark.setText(customer_remarks);
                                                        tv_admin_remark.setText(admin_remarks);
                                                    }
                                                    else {
                                                        //remarks = String.valueOf(Html.fromHtml(remarks));
                                                        customer_remarks = String.valueOf(Html.fromHtml(customer_remarks));
                                                        admin_remarks = String.valueOf(Html.fromHtml(admin_remarks));

                                                        tv_remark.setText(customer_remarks);
                                                        tv_admin_remark.setText(admin_remarks);

                                                    }

                                                    tv_remark.setText(customer_remarks);

                                                }



                                            }
                                            if(is_show.equals("1"))
                                            {

                                                //if(jsonObj.has("items"))

                                                JSONArray jsonArrayItem = jsonObj.getJSONArray("items");
                                                for(int k=0; k<jsonArrayItem.length();k++)
                                                {
                                                    JSONObject jsonObjItem = jsonArrayItem.getJSONObject(k);
                                                    request_qty = jsonObjItem.getString("request_qty");
                                                    sku = jsonObjItem.getString("sku");
                                                    product_name = jsonObjItem.getString("name");
                                                    String image=jsonObjItem.getString("image");

                                                    ComplModel complModel0 = new ComplModel();
                                                    complModel0.setRequest_qty(request_qty);
                                                    complModel0.setProduct_name(product_name);
                                                    complModel0.setProduct_url(image);
                                                    complModel0.setItem_sku(sku);
                                                    complModels12.add(complModel0);

                                                }
                                                details_ly.setVisibility(View.VISIBLE);

                                            }

                                            complaintItemAdapter = new ComplaintItemAdapter(ComplaintDetailActivity.this, complModels12);
                                            complaint_details_recycler_view.setAdapter(complaintItemAdapter);

                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no help available";
                                            //Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                                            //tv_notice.setText(err_msg);
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            CommonFun.showVolleyException(error,ComplaintDetailActivity.this);
                            // Log.e("error_re",error.getMessage());

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
                        public byte[] getBody() throws AuthFailureError {
                            return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
                        }

                        @Override
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + st_token_data);
                            //Log.e("st_token_data..",st_token_data);
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
