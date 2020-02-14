package com.galwaykart.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumitsaini on 9/26/2017.
 */

public class OrderListActivity extends BaseActivity {

    private SharedPreferences pref;
    private String st_token_data="";
    private TransparentProgressDialog pDialog;
    private RecyclerView order_list_rec_recyclerview;
    private JsonObjectRequest jsObjRequest = null;
    private String st_order_list_url = "";
    TextView tv_title, tv_my_complaint;
    ImageView iv_image_no_details;

    public OrderListAdapter orderListAdapter;




    private void goBack(){
        Intent intent=new Intent(this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_new);

        initNavigationDrawer();

        pref = getSharedPreferences("glazekartapp", MODE_PRIVATE);
        tv_title = findViewById(R.id.tv_title);
        iv_image_no_details = findViewById(R.id.iv_image_no_details);

        order_list_rec_recyclerview = findViewById(R.id.order_list_rec_recyclerview);

        order_list_rec_recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        order_list_rec_recyclerview.setLayoutManager(mLayoutManager);

        tv_my_complaint = findViewById(R.id.tv_my_complaint);
        tv_my_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderListActivity.this, MyComplaints.class);
                startActivity(intent);
            }
        });

        jsonOrderList();

    }

    @Override
    public void onBackPressed() {
     goBack();
    }




    public void jsonOrderList() {
        final ArrayList<OrderListModel> order_list = new ArrayList<>();
        st_token_data = pref.getString("tokenData","");
        //Log.d("st_token_data",st_token_data);

        st_order_list_url=Global_Settings.api_url+"rest/V1/m-order/1/10";
        //Log.d("st_order_list_url",st_order_list_url);

        pDialog = new TransparentProgressDialog(OrderListActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(OrderListActivity.this);
            // JsonObjectRequest jsObjRequest = null;
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_order_list_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("ResponseOrder_List", response);
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                                            Log.e("ArryLenght",jsonArray.length() + "");

                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);

                                                String orderid = jsonObjFinal.getString("entity_id");
                                                String increment_id = jsonObjFinal.getString("increment_id");
                                                String entity_id = jsonObjFinal.getString("entity_id");
                                                String status = jsonObjFinal.getString("status_label");
                                                String created_at = jsonObjFinal.getString("created_at");
                                                String updated_at= jsonObjFinal.getString("updated_at");
                                                String subtotal = jsonObjFinal.getString("subtotal");
                                                String grand_total = jsonObjFinal.getString("grand_total");
                                                String total_item_count = jsonObjFinal.getString("total_item_count");
                                                String total_qty_ordered = jsonObjFinal.getString("total_qty_ordered");
                                                String status_label = jsonObjFinal.getString("status_label");



                                                OrderListModel orderListModel = new OrderListModel();
                                                orderListModel.setOrderid(orderid);
                                                orderListModel.setIncrement_id(increment_id);
                                                orderListModel.setEntity_id(entity_id);
                                                orderListModel.setStatus(status);
                                                orderListModel.setCreated_at(created_at);
                                                orderListModel.setUpdated_at(updated_at);
                                                orderListModel.setSubtotal(subtotal);
                                                orderListModel.setGrand_total(grand_total);
                                                orderListModel.setTotal_item_count(total_item_count);
                                                orderListModel.setStatus_label(status_label);
                                                orderListModel.setTotal_qty_ordered(total_qty_ordered);
                                                order_list.add(orderListModel);

                                                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date dt=format1.parse(created_at);

                                            }

                                            orderListAdapter = new OrderListAdapter(OrderListActivity.this, order_list);
                                            if(orderListAdapter.getItemCount()>0)
                                            {
                                                order_list_rec_recyclerview.setVisibility(View.VISIBLE);
                                                order_list_rec_recyclerview.setAdapter(orderListAdapter);
                                                tv_title.setText("Your Order");
                                            }
                                            else
                                            {
                                                order_list_rec_recyclerview.setVisibility(View.GONE);
                                                iv_image_no_details.setVisibility(View.VISIBLE);
                                                tv_title.setText("You have not placed any order yet.");
                                            }



                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no testimonial available";
                                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                                            //tv_notice.setText(err_msg);
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            CommonFun.showVolleyException(error,OrderListActivity.this);

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
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + st_token_data);
                            params.put("Content-Type", "application/json");
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
