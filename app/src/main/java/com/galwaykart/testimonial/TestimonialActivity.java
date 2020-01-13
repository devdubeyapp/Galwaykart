package com.galwaykart.testimonial;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.newsnotice.NoticeActivity;
import com.galwaykart.notification.DividerItemDecoration;
import com.galwaykart.notification.NotificationAdapter;
import com.galwaykart.notification.NotificationListActivity;
import com.galwaykart.notification.NotificationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestimonialActivity extends AppCompatActivity {

    String st_testimonial_url="";

    TransparentProgressDialog pDialog;
    RecyclerView testimonial_recycler_view;
    public TestimonialAdapter testimonialAdapter;
    FloatingActionButton add_testimonial_fab;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack(){
        Intent intent = new Intent(TestimonialActivity.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(TestimonialActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial);

        testimonial_recycler_view= (RecyclerView) findViewById(R.id.testimonial_recycler_view);
        testimonial_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        testimonial_recycler_view.setLayoutManager(mLayoutManager);
        testimonial_recycler_view.addItemDecoration(new DividerItemDecoration(this,R.drawable.list_divider));

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        jsonTestimonial();

        add_testimonial_fab = (FloatingActionButton) findViewById(R.id.add_testimonial_fab);

        if(!isLoggedIn())
            add_testimonial_fab.hide();

        add_testimonial_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestimonialActivity.this, CreateTestimonialActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isLoggedIn(){
        boolean is_log=false;
        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());
        String value_email=pref.getString("login_email","");

        if(value_email!=null && !value_email.equals(""))
            is_log=true;

        return is_log;
    }

    public void jsonTestimonial() {

        final ArrayList<TestimonialModel> testimonial_list11 = new ArrayList<>();
        st_testimonial_url = Global_Settings.api_url + "rest/V1/testimonials/listing/1/50";

        //Log.d("st_testimonial_url",st_testimonial_url);
        pDialog = new TransparentProgressDialog(TestimonialActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(TestimonialActivity.this);
            // JsonObjectRequest jsObjRequest = null;
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_testimonial_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("testimonialResponse", response);
                                    if (response != null) {
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                                            Log.e("ArryLenght",jsonArray.length() + "");

                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                                String id = jsonObjFinal.getString("id");
                                                String title = jsonObjFinal.getString("title");
                                                String description = jsonObjFinal.getString("testimonial_content");
                                                String date_added = jsonObjFinal.getString("created_at");
                                                String author_name = jsonObjFinal.getString("author");

                                                String author_image="";

                                                if(jsonObjFinal.has("image"))
                                                {
                                                    author_image =  jsonObjFinal.getString("image");
                                                }
                                                else
                                                {
                                                    author_image="";
                                                }

                                                Log.e("id",id);
                                                Log.e("title",title);
                                                Log.e("description", description);
                                                Log.e("date_added",date_added);
                                                Log.e("author_name",author_name);
                                                Log.e("author_image",author_image);

                                                TestimonialModel testimonialModel = new TestimonialModel();
                                                testimonialModel.setId(id);
                                                testimonialModel.setTitle(title);
                                                testimonialModel.setAuthor(author_name);
                                                testimonialModel.setTestimonial_content(description);
                                                testimonialModel.setCreation_time(date_added);
                                                testimonialModel.setImage(author_image);
                                                testimonial_list11.add(testimonialModel);

                                            }

                                            testimonialAdapter = new TestimonialAdapter(TestimonialActivity.this, testimonial_list11);
                                            testimonial_recycler_view.setAdapter(testimonialAdapter);

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





    public void Test()
    {
        //select the static valune like here "mobile_number" is key and numeric is  phone no- "\"mobile_number\":\"7503237764\"  select phone no(7503237764)
        //Then put double inverted commas "" in latex
        //Then add double ++ sige
        //Then pass dynamic string in between


        String input = "{\"testimonial_data\":{\"image\":\""+st_testimonial_url+"\",\"author\":\"Rakesh Chand\",\"city\":\"aavcgjgv\"," +
                "\"email\":\"abc123@gmail.com\",\"rating\":\"80\"," +
                "\"mobile_number\":\"7503237764\",\"title\":\"This is Title\"," +
                "\"testimonial_content\":\"aa vcgj dfgdssghf lijhdsf oihdoig hioerh oidrh goidsh gv xfcgds fth fhfrta\"}}";

    }
}
