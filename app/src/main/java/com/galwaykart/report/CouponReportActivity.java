package com.galwaykart.report;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.EndlessRecyclerViewScrollListener;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.notification.DividerItemDecoration;
import com.galwaykart.profile.ChangePasswordActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponReportActivity extends BaseActivityWithoutCart {

    TransparentProgressDialog pDialog;
    ProgressBar progress_bar_pegination;
    SharedPreferences pref;
    RecyclerView coupon_report_recycler_view;
    ArrayList<CouponReportModel> cc_list;
    private CouponReportAdapter couponReportAdapter;
    private ArrayList<CouponReportModel> couponReportModels;
    String tokenData;
    String  st_coupon_url = "";

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    String total_row_data="";
    String page_size="";
    int TOTAL_PAGES =1;

    private static final int PAGE_START =1;
    private int currentPage = PAGE_START;
    ImageView iv_image_no_details;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CouponReportActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_report_activiry);
        initNavigationDrawer();


        coupon_report_recycler_view= findViewById(R.id.coupon_report_recycler_view);
        coupon_report_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.isSmoothScrollbarEnabled();
        coupon_report_recycler_view.setLayoutManager(mLayoutManager);
        coupon_report_recycler_view.addItemDecoration(new DividerItemDecoration(this,R.drawable.list_divider));

        cc_list = new ArrayList<>();

        iv_image_no_details= findViewById(R.id.iv_image_no_details);
        iv_image_no_details.setVisibility(View.GONE);

        progress_bar_pegination = findViewById(R.id.progress_bar_pegination);
      //  loadNextDataFromApi(currentPage);
//        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//
//
//                Log.e("st_coupon_url_intial", st_coupon_url);
//                Log.e("currentPage_Initial", currentPage +"");
//
//            }
//        };
       // coupon_report_recycler_view.addOnScrollListener(scrollListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonCouponLog(false);
        Log.e("st_coupon_url_onResume", st_coupon_url);
    }

    private void loadNextDataFromApi(int cPage)
    {
     //   TOTAL_PAGES = Integer.parseInt(total_row_data)/10;
        currentPage+=1;
        Log.e("TOTAL_PAGES", TOTAL_PAGES +"");
        Log.e("currentPage_api", currentPage +"");
        if(currentPage<=TOTAL_PAGES)
        {
            SharedPreferences preferences = CommonFun.getPreferences(getApplicationContext());
            tokenData = preferences.getString("tokenData", "");
            st_coupon_url = Global_Settings.api_url + "rest/V1/customer/coupon/details?current_page="+currentPage+"&&page_size=10";
            Log.e("st_coupon_url_onload", st_coupon_url);
            jsonCouponLog(true);
            progress_bar_pegination.setVisibility(View.VISIBLE);

        }
    }



    public void jsonCouponLog(boolean isLoadMore) {

        SharedPreferences preferences = CommonFun.getPreferences(getApplicationContext());
        tokenData = preferences.getString("tokenData", "");
        st_coupon_url = Global_Settings.api_url + "rest/V1/customer/coupon/details?current_page="+currentPage+"&&page_size=10";

        if(currentPage==1)
        {
            pDialog = new TransparentProgressDialog(CouponReportActivity.this);
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.show();
        }
        else
        {
            if (pDialog.isShowing())
                pDialog.dismiss();
                progress_bar_pegination.setVisibility(View.VISIBLE);
            Log.e("st_coupon_url_api", st_coupon_url);
        }


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(CouponReportActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_coupon_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                        progress_bar_pegination.setVisibility(View.GONE);
                                    Log.e("response_coupon", response);
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArrayCd = jsonObject.getJSONArray("coupon_data");
                                            JSONArray jsonArrayP = jsonObject.getJSONArray("pagination");

                                            for(int j = 0; j<jsonArrayCd.length(); j++)
                                            {
                                                JSONObject jsonObjectCd = jsonArrayCd.getJSONObject(j);
                                                String s_no =  jsonObjectCd.getString("s_no");
                                                String coupon_type =  jsonObjectCd.getString("coupon_type");
                                                String amount =  jsonObjectCd.getString("amount");
                                                String created_date =  jsonObjectCd.getString("created_date");
                                                String expired_date =  jsonObjectCd.getString("expired_date");
                                                String coupon_code =  jsonObjectCd.getString("coupon_code");
                                                String used_reference =  jsonObjectCd.getString("used_reference");
                                                String is_active =  jsonObjectCd.getString("is_active");
                                                Log.e("coupon_code", coupon_code );

                                                CouponReportModel ccModel = new CouponReportModel();
                                                ccModel.setSr_no(s_no);
                                                ccModel.setCoupon_type(coupon_type);
                                                ccModel.setAmount(amount);
                                                ccModel.setCreated_date(created_date);
                                                ccModel.setExpired_date(expired_date);
                                                ccModel.setCoupon_code(coupon_code);
                                                ccModel.setUsed_reference(used_reference);
                                                ccModel.setIs_active(is_active);
                                                cc_list.add(ccModel);
                                            }



                                            for(int k = 0; k<jsonArrayP.length(); k++)
                                            {
                                                JSONObject jsonObjectP = jsonArrayP.getJSONObject(k);
                                                total_row_data =  jsonObjectP.getString("total_count");
                                                String cur_page_server =  jsonObjectP.getString("current_page");
                                                page_size =  jsonObjectP.getString("page_size");
                                                Log.e("total_data_server", total_row_data );
                                                Log.e("cur_page_server", cur_page_server);
                                                Log.e("page_size_server", page_size );

                                                TOTAL_PAGES=Integer.parseInt(total_row_data)/2;

                                            }

                                            if(Integer.parseInt(total_row_data)>0)
                                                setCouponAdapter(isLoadMore);
                                            else
                                            {
                                                TextView tv_txt_view=findViewById(R.id.tv_txt_view);
                                                tv_txt_view.setText("You don’t have any Voucher report");

                                                iv_image_no_details.setVisibility(View.VISIBLE);

                                            }
                                        }


                                        catch (Exception e) {
//                                          if (pDialog.isShowing())
//                                             pDialog.dismiss();
                                            progress_bar_pegination.setVisibility(View.GONE);
                                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();

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
                            params.put("Authorization", "Bearer " + tokenData);
                            params.put("Content-Type", "application/json");
                            return params;
                        }

              /*  @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.e("Order_ID",json_input_data1s);
                        return json_input_data1s == null ? null : json_input_data1s.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json_input_data1s, "utf-8");
                        return null;
                    }
                }*/
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


    private void setCouponAdapter(Boolean is_load_more){
     if(is_load_more==true){
         couponReportAdapter.notifyDataSetChanged();
     }
     else {

         couponReportAdapter = new CouponReportAdapter(CouponReportActivity.this, cc_list);
         mLayoutManager = new LinearLayoutManager(CouponReportActivity.this);

         mLayoutManager.isSmoothScrollbarEnabled();

         coupon_report_recycler_view.setLayoutManager(mLayoutManager);
         coupon_report_recycler_view.setItemAnimator(new DefaultItemAnimator());
         scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
             @Override
             public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                 // Triggered only when new data needs to be appended to the list
                 // Add whatever code is needed to append new items to the bottom of the list

                 loadNextDataFromApi(currentPage);

             }
         };

         coupon_report_recycler_view.addOnScrollListener(scrollListener);
         coupon_report_recycler_view.setAdapter(couponReportAdapter);
         couponReportAdapter.notifyDataSetChanged();

     }


    }


}
