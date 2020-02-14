package com.galwaykart.newsnotice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.notification.DividerItemDecoration;
import com.galwaykart.testimonial.TestimonialActivity;
import com.galwaykart.testimonial.TestimonialAdapter;
import com.galwaykart.testimonial.TestimonialModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {


    String st_notice_url="";

    TransparentProgressDialog pDialog;
    RecyclerView news_recycler_view;
    public NoticeAdapter newsAdapter;
    String title, identifier="0";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack(){
        Intent intent = new Intent(NoticeActivity.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(NoticeActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        news_recycler_view= findViewById(R.id.news_recycler_view);
        news_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        news_recycler_view.setLayoutManager(mLayoutManager);
        news_recycler_view.addItemDecoration(new DividerItemDecoration(this,R.drawable.list_divider));

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        jsonNOtice();

    }


    public void jsonNOtice() {

        final ArrayList<NoticeModel> notice_list1 = new ArrayList<>();
        st_notice_url = Global_Settings.api_url + "rest/V1/information/text";

        //Log.d("st_notice_url",st_notice_url);
        pDialog = new TransparentProgressDialog(NoticeActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(NoticeActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_notice_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("newsResponse", response);
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                                            Log.e("ArryLenght",jsonArray.length() + "");

                                            for(int j = 0; j<jsonArray.length(); j++) {

                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                                title = jsonObjFinal.getString("text");
                                                identifier = jsonObjFinal.getString("identifier");
                                                NoticeModel noticeModel = new NoticeModel();

                                                if(identifier.equals("") || identifier==null)
                                                {
                                                    identifier ="0";
                                                    noticeModel.setIdentifier("0");
                                                    Log.e("identifier if",identifier);
                                                }
                                                else
                                                {
                                                    noticeModel.setIdentifier(identifier);
                                                    Log.e("identifier else",identifier);
                                                }
                                                noticeModel.setTitle(title);
                                                notice_list1.add(noticeModel);
                                            }

                                            newsAdapter = new NoticeAdapter(NoticeActivity.this, notice_list1);
                                            news_recycler_view.setAdapter(newsAdapter);

                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no news or notice available";
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
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
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
