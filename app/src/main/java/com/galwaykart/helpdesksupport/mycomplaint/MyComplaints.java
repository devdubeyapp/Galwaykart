package com.galwaykart.helpdesksupport.mycomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.OrderListActivityNew;
import com.galwaykart.profile.OrderListAdapter;
import com.galwaykart.testimonial.TestimonialActivity;
import com.galwaykart.testimonial.TestimonialAdapter;
import com.galwaykart.testimonial.TestimonialModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyComplaints extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private String st_token_data="";
    String customer_id ="";

    private ImageView back_img;
    TextView tv_title;

    TransparentProgressDialog pDialog;
    RecyclerView complaint_recycler_view;
    public ComplAdapter complAdapter;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goBack();
    }

    private void goBack(){
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaint);


        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Init();
    }

    void Init()
    {

        pref = getSharedPreferences("glazekartapp", MODE_PRIVATE);
        st_token_data=pref.getString("tokenData","");
        customer_id =pref.getString("login_id","");



        tv_title = findViewById(R.id.tv_title);

        complaint_recycler_view= findViewById(R.id.complaint_recycler_view);
        complaint_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        complaint_recycler_view.setLayoutManager(mLayoutManager);
        // complaint_recycler_view.addItemDecoration(new DividerItemDecoration(this,R.drawable.list_divider));

        jsonMyComplaint();



    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back_img:
                finish();
                break;

        }

    }
    String input_data="";
    public void jsonMyComplaint() {

        final ArrayList<ComplModel> complModels11 = new ArrayList<>();
        String st_mycomplaint_url = Global_Settings.api_url + "rest/V1/m-help-history";

        input_data = "{\"customerId\":\""+customer_id+"\"}";
        Log.e("input_data",input_data);
        Log.e("customerId",input_data);
        //Log.d("st_mycomplaint_url",st_mycomplaint_url);

        pDialog = new TransparentProgressDialog(MyComplaints.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(MyComplaints.this);
            // JsonObjectRequest jsObjRequest = null;
            StringRequest stringRequest =
                    new StringRequest(Request.Method.POST, st_mycomplaint_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("MyComplaintResponse", response);
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObj= jsonArray.getJSONObject(j);
                                                String complaint_id = jsonObj.getString("request_id");
                                                String order_id = jsonObj.getString("order_id");
                                                String created_at = jsonObj.getString("created_at");
                                                String modified_at = jsonObj.getString("modified_at");
                                                String status_id = jsonObj.getString("status_id");
                                                String status_label = jsonObj.getString("label");
                                                String customer_id = jsonObj.getString("customer_id");
                                                String customer_name = jsonObj.getString("customer_name");
                                                String description = jsonObj.getString("description");
                                                String complaint_type = jsonObj.getString("complaint_type");
                                                String isShow = jsonObj.getString("show");

                                                Log.e("complaint_id",complaint_id);
                                                Log.e("order_id",order_id);
                                                Log.e("created_at", created_at);
                                                Log.e("modified_at",modified_at);
                                                Log.e("status_id",status_id);
                                                Log.e("status_label",status_label);
                                                Log.e("customer_id",customer_id);
                                                Log.e("customer_name",customer_name);
                                                Log.e("description",description);
                                                Log.e("complaint_type","complaint_type");

                                                ComplModel complModel = new ComplModel();
                                                complModel.setComplaint_id(complaint_id);
                                                complModel.setOrder_id(order_id);
                                                complModel.setCreated_at(created_at);
                                                complModel.setModified_at(modified_at);
                                                complModel.setStatus_id(status_id);
                                                complModel.setStatus_label(status_label);
                                                complModel.setCustomer_id(customer_id);
                                                complModel.setCustomer_name(customer_name);
                                                complModel.setDescription(description);
                                                complModel.setComplaint_type(complaint_type);
                                                complModel.setIsShow(isShow);
                                                complModels11.add(complModel);
                                            }

                                            complAdapter = new ComplAdapter(MyComplaints.this, complModels11);
                                            //complaint_recycler_view.setAdapter(complAdapter);
                                            if(complAdapter.getItemCount()>0)
                                            {
                                                complaint_recycler_view.setVisibility(View.VISIBLE);
                                                complaint_recycler_view.setAdapter(complAdapter);
                                                //tv_title.setText("Your ");
                                            }
                                            else
                                            {
                                                complaint_recycler_view.setVisibility(View.GONE);
                                                tv_title.setText("You have not raised any order help.");
                                            }


                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no help available";
                                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
                        public byte[] getBody() throws AuthFailureError {
                            return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
                        }

                        @Override
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + st_token_data);
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
