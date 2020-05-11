package com.galwaykart.helpdesksupport;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.DateInterval;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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
import com.galwaykart.helpdesksupport.mycomplaint.ComplAdapter;
import com.galwaykart.helpdesksupport.mycomplaint.ComplModel;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.profile.OrderDetails;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.testimonial.TestimonialActivity;
import com.galwaykart.testimonial.TestimonialAdapter;
import com.galwaykart.testimonial.TestimonialModel;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class CMSMainActivity extends BaseActivityWithoutCart {

    private SharedPreferences pref;
    private String st_token_data="";

    private FrameLayout fl;
    private Spinner complaint_reasons_spinner;
    private String str_complaint_category="Select Compaint Queries", str_complaint_category_id, str_item_show;
    private ArrayList<CompCategModel> compCategModels;
    int chk = 0;

    private TransparentProgressDialog pDialog;

    FragmentManager manager;
    FragmentTransaction transaction;
    String entity_id="";
    String order_id="";

    public static final String SELECT_COMPLAINT_TYPE = "Select Help Query";
    public static final String PAYMENT_RELATED_COMPLAINTS = "Payment Related Queries";
    public static final String ACCOUNT_RELATED_COMPLAINTS = "Account Related Queries";
    public static final String COURIER_RELATED_COMPLAINT = "Courier Related Queries";
    public static final String WRONG_DELIVERY_COMPLAINTS = "Wrong Delivery Queries";
    public static final String RTO_RELATED_QUERIES = "RTO related Queries";
    public static final String DAMAGE_ORDER_COMPLAINTS = "Damage Order Queries";
    public static final String MISSING_ITEMS_COMPLAINTS = "Missing Items Queries";

    ArrayList<String> spinnerArray = new ArrayList<String>();
    String[] complaint_category_array = {SELECT_COMPLAINT_TYPE, PAYMENT_RELATED_COMPLAINTS, ACCOUNT_RELATED_COMPLAINTS, COURIER_RELATED_COMPLAINT, WRONG_DELIVERY_COMPLAINTS,
            RTO_RELATED_QUERIES, DAMAGE_ORDER_COMPLAINTS, MISSING_ITEMS_COMPLAINTS};

    private TextView tv_my_compaint;
    String update_at="";

    static String st_pool_id="ap-south-1:89ffc0bb-91c0-485f-be38-ec511fdb2661";
    String imgfilename= "";

    private Uri fileUri;
    String filePath; //image
    public static final int MEDIA_TYPE_VIDEO = 1;
    private static final int CAMERA_Camera_CAPTURE_VIDEO_REQUEST_CODE = 400;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private String pathToStoredVideo="";
    String st_video_directory="galwaykart";
    ProgressBar progress_bar_video;
    ImageView iv_complaint_video_capture;
    TextView tv_process_name;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        goBack();

    }

    private void goBack(){
        Intent intent=new Intent(this, OrderListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmsmain);

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    goBack();
            }
        });

        fl = findViewById(R.id.frame);
        complaint_reasons_spinner = findViewById(R.id.complaint_reasons_spinner);
        compCategModels = new ArrayList<>();

        pref = CommonFun.getPreferences(CMSMainActivity.this);
        st_token_data=pref.getString("tokenData","");

        /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, complaint_category_array);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        complaint_reasons_spinner.setAdapter(spinnerArrayAdapter);
*/
        Intent intent = getIntent();
        entity_id= intent.getStringExtra("entity_id");
        order_id= intent.getStringExtra("order_id");
        update_at=intent.getStringExtra("order_date");

        Log.e("entity_id_from_adp",entity_id);
        Log.e("order_id_from_adp",entity_id);


        imgfilename = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date());

        //Log.d("awsdata",imgfilename);

//        progress_bar_video=findViewById(R.id.progress_bar_video);
//        progress_bar_video.setVisibility(View.GONE);

//        tv_process_name=findViewById(R.id.tv_process_name);
//        tv_process_name.setText("");
//        tv_process_name.setVisibility(View.GONE);


//        iv_complaint_video_capture=findViewById(R.id.iv_complaint_video_capture);
//        iv_complaint_video_capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                captureImage();
//
//            }
//        });



        getComplaintCategory();



    }


    private void getComplaintCategory() {

        complaint_reasons_spinner.setSelection(0, false);
        complaint_reasons_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_complaint_category = parent.getItemAtPosition(position).toString();
                chk += 1;
                if (chk > 1)
                {
                    str_complaint_category_id = compCategModels.get(position).getComplaintCategory_id();
                    str_item_show = compCategModels.get(position).getShow();
                }

                Log.e("str_complaint_category", str_complaint_category);
                Log.e("str_complaint_cat_id", str_complaint_category_id + "");
                Log.e("str_item_show", str_item_show + "");


                if(str_complaint_category.equals(SELECT_COMPLAINT_TYPE))
                {
                    String err_msg="Please select help query";
                    //Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                }

                else if(str_item_show.equals("0"))
                {
                    fl.setVisibility(View.VISIBLE);
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    OtherComplaintsFragment fragment = new OtherComplaintsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("entity_id", entity_id);
                    bundle.putString("order_id", order_id);
                    bundle.putString("str_complaint_category_id", str_complaint_category_id);
                    Log.e("entity_id_CMSA", entity_id);
                    Log.e("order_id_CMSA", order_id);
                    Log.e("str_compl_category_id",  str_complaint_category_id);
                    fragment.setArguments(bundle);

                    transaction.replace(R.id.frame, fragment);
                    transaction.commit();
                }
                else if(str_item_show.equals("1"))
                {
                    fl.setVisibility(View.VISIBLE);
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    DamageMissCompFragment fragment1 = new DamageMissCompFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("entity_id", entity_id);
                    bundle.putString("order_id", order_id);
                    bundle.putString("str_complaint_category_id", str_complaint_category_id);
                    Log.e("entity_id_CMSA", entity_id);
                    Log.e("order_id_CMSA", order_id);
                    Log.e("str_compl_category_id",  str_complaint_category_id);
                    fragment1.setArguments(bundle);
                    transaction.replace(R.id.frame, fragment1);
                    transaction.commit();
                }






            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        complaintCategoryJson();
    }

    public void complaintCategoryJson() {

        String st_complaint_category_url = Global_Settings.api_url + "rest/V1/m-help-detail";

        //Log.d("st_complaint_url",st_complaint_category_url);
        pDialog = new TransparentProgressDialog(CMSMainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(CMSMainActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_complaint_category_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("Category_Response", response);
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObj= jsonArray.getJSONObject(j);
                                                String complaint_cate_id = jsonObj.getString("id");
                                                String title = jsonObj.getString("title");
                                                String status = jsonObj.getString("status");
                                                String position = jsonObj.getString("position");

                                                String match_code = jsonObj.getString("match_code");
                                                String show = jsonObj.getString("show");

                                                Log.e("complaint_id",complaint_cate_id);
                                                Log.e("title",title);
                                                Log.e("status", status);
                                                Log.e("position",position);
                                                //Log.e("days",days);
                                                Log.e("match_code","match_code");
                                                Log.e("show","show");

                                                String days = jsonObj.getString("days");
                                                String current_date=jsonObj.getString("currentDate");

                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                Date dt_CurrentDate=sdf.parse(current_date);
                                                Date dt_OrderDate=sdf.parse(update_at);

                                                //Log.d("cdate", current_date);
                                                //Log.d("udate", update_at);
                                                //Log.d("cdate", String.valueOf(dt_CurrentDate));
                                                //Log.d("udate", String.valueOf(dt_OrderDate));

                                                long diffInMillies = dt_CurrentDate.getTime() - dt_OrderDate.getTime();
                                                long diff = (diffInMillies / 1000L / 60L / 60L / 24L);

                                                //Log.d("differnece", String.valueOf(diff));




                                                //if(Integer.parseInt(days)>=diff) {

                                                    CompCategModel categorymodel = new CompCategModel();
                                                    categorymodel.setComplaintCategory_id(complaint_cate_id);
                                                    categorymodel.setTitle(title);
                                                    categorymodel.setStatus(status);
                                                    categorymodel.setPosition(position);
                                                    categorymodel.setDays(days);
                                                    categorymodel.setMatch_code(match_code);
                                                    categorymodel.setShow(show);
                                                    compCategModels.add(categorymodel);

                                               // }



                                            }


                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no help available";
                                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();

                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CMSMainActivity.this, android.R.layout.simple_spinner_item) {

                                            @Override
                                            public View getView(int position, View convertView, ViewGroup parent) {

                                                View v = super.getView(position, convertView, parent);
                                                if (position == getCount()) {
                                                    ((TextView) v.findViewById(android.R.id.text1)).setText("Select Help Query");
                                                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                                                }

                                                return v;
                                            }
                                            @Override
                                            public int getCount() {
                                                return super.getCount() - 1; // you dont display last item. It is used as hint.
                                            }
                                        };

                                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

                                        if(compCategModels.size()>0) {
                                            for (int j = 0; j < compCategModels.size(); j++) {
                                                adapter.add(compCategModels.get(j).getTitle());
                                            }
                                            adapter.add("Select Help Query");
                                            complaint_reasons_spinner.setAdapter(adapter);
                                            complaint_reasons_spinner.setSelection(adapter.getCount());
                                        }
                                        else
                                        {
                                            CommonFun.alertError(CMSMainActivity.this,"No option available right now");
                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            String err_msg="currently, there is no complaint available";
                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
