package com.galwaykart.helpdesksupport.mycomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComplaintDetailActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView tv_date, tv_ticket_no,tv_status, tv_order_no,tv_order_status, tv_category_type, tv_remark;
    TextView str_date, str_ticket_no,str_status, str_order_no, str_category_type;
    LinearLayout details_ly;

    private String complaint_id="", complaint_type="", is_show="";
    private SharedPreferences pref;
    private String st_token_data="";
    String remarks="";

    TransparentProgressDialog pDialog;
    RecyclerView complaint_details_recycler_view;
    public ComplaintItemAdapter complaintItemAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);


        Intent intent = getIntent();
        complaint_id= intent.getStringExtra("complaint_id");
        complaint_type= intent.getStringExtra("complaint_type");
        is_show= intent.getStringExtra("is_show");
        remarks= intent.getStringExtra("remarks");



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



    void Init()
    {
        pref = getSharedPreferences("glazekartapp", MODE_PRIVATE);
        st_token_data=pref.getString("tokenData","");

        tv_date = (TextView) findViewById(R.id.tv_date) ;
        tv_ticket_no = (TextView) findViewById(R.id.tv_ticket_no) ;
        tv_status = (TextView) findViewById(R.id.tv_status) ;
        tv_order_no = (TextView) findViewById(R.id.tv_order_no) ;
        tv_order_status  = (TextView) findViewById(R.id.tv_order_status) ;
        tv_category_type = (TextView) findViewById(R.id.tv_category_type) ;
        tv_remark = (TextView) findViewById(R.id.tv_remark) ;
        details_ly =(LinearLayout) findViewById(R.id.details_ly) ;



        /*back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(this);*/



        complaint_details_recycler_view= (RecyclerView) findViewById(R.id.complaint_details_recycler_view);
        complaint_details_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        complaint_details_recycler_view.setLayoutManager(mLayoutManager);

        jsonComplaintItem();
    }



    @Override
    public void onClick(View v) {
        }

    String input_data="";
    private void jsonComplaintItem() {

        final ArrayList<ComplModel> complModels12 = new ArrayList<>();
        String st_mycomplaint_details_url = Global_Settings.api_url + "rest/V1/m-help-detail";

        input_data = "{\"requestId\":\""+complaint_id+"\"}";
        Log.e("input_data",input_data);
        Log.d("complaint_details_url",st_mycomplaint_details_url);

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

                                                    tv_date.setText(created_at);
                                                    tv_ticket_no.setText(complaint_id);
                                                    tv_order_no.setText(order_id);
                                                    tv_order_status.setText(order_status);
                                                    tv_status.setText(complaint_status);
                                                    tv_category_type.setText(complaint_type);


                                                    tv_remark.setText(remarks);

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
                                                          ComplModel complModel0 = new ComplModel();
                                                          complModel0.setRequest_qty(request_qty);
                                                          complModel0.setProduct_name(product_name);
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
                            Log.e("error_re",error.getMessage());

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
                            try {
                                return input_data == null ? null : input_data.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data, "utf-8");
                                return null;
                            }
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
