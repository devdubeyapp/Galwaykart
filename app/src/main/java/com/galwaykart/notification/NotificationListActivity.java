package com.galwaykart.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.OrderDetails;
import com.galwaykart.report.CouponReportActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationListActivity extends AppCompatActivity {

    TransparentProgressDialog pDialog;
    SharedPreferences pref;
    private String category_id ="0";
    RecyclerView notification_recycler_view;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> nf_list;
    String tokenData, st_login_id;
    String  st_notification_url = "";
    TextView tv_notice;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(NotificationListActivity.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(NotificationListActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        //initNavigationDrawer();

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });
        ImageView image_view_title=findViewById(R.id.image_view_title);
        image_view_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        tv_notice=findViewById(R.id.tv_notice);

        notification_recycler_view= findViewById(R.id.notification_recycler_view);
        notification_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notification_recycler_view.setLayoutManager(mLayoutManager);
        notification_recycler_view.addItemDecoration(new DividerItemDecoration(this,R.drawable.list_divider));

         jsonNotifiation();

//        ImageView back_img = (ImageView) findViewById(R.id.back_img);
//
//        back_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(NotificationListActivity.this, HomePageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                CommonFun.finishscreen(NotificationListActivity.this);
//            }
//        });




    }

    public void jsonNotifiation() {

        final ArrayList<NotificationModel> nf_list11 = new ArrayList<>();

        SharedPreferences preferences = CommonFun.getPreferences(getApplicationContext());
        tokenData = preferences.getString("tokenData", "");
        st_login_id = preferences.getString("st_login_id", "");
        Log.e("st_login_id", st_login_id);

        //http://uat.galwaykart.com/glaze/notify/getNotification.php?userid=71911&start=1&next=10


//<option value="0" selected="selected">All Distributor</option>
//<option value="1">All Employee</option>
//<option value="2">All Preferred Customer</option>
//<option value="3">Guest</option>
//<option value="4">All Type Customer</option>

        String st_login_group_id=preferences.getString("login_group_id","");
        int log_type = 3;
        if(st_login_group_id!=null && !st_login_group_id.equals("")) {

            switch (st_login_group_id) {
                case "4":
                    log_type = 0;
                    break;
                case "8":
                    log_type = 0;
                    break;
                case "5":
                    log_type = 1;
                    break;
                case "1":
                    log_type = 2;
                    break;
                case "0":
                    log_type = 3;
                    break;
                default:
                    log_type = 3;
            }
        }

        st_notification_url = Global_Settings.api_url +
        "glaze/notify/getNotification.php?userid="+st_login_id+"&start=0&next=50&type="+ log_type;

        //Log.d("st_notification_url",st_notification_url);
        pDialog = new TransparentProgressDialog(NotificationListActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(NotificationListActivity.this);
            // JsonObjectRequest jsObjRequest = null;
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_notification_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Log.e("url_JSON9686852", response);
                            if (response != null) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                                    for(int j = 0; j<jsonArray.length(); j++)
                                    {
                                        JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                        String id =  jsonObjFinal.getString("id");
                                        String title =  jsonObjFinal.getString("title");
                                        String description =  jsonObjFinal.getString("message");
                                        String date_added =  jsonObjFinal.getString("schedule");

                                        //Log.e("description9686852", description );

                                        NotificationModel nfModel = new NotificationModel();
                                        nfModel.setId(id);
                                        nfModel.setTitle(title);
                                        nfModel.setMessage(description);
                                        nfModel.setCreated(date_added);
                                        nf_list11.add(nfModel);

                                    }

                                    notificationAdapter = new NotificationAdapter(NotificationListActivity.this, nf_list11);
                                    notification_recycler_view.setAdapter(notificationAdapter);
                                    tv_notice.setText("Notification");
                                }

                                catch (Exception e) {
                                    //e.printStackTrace();
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    String err_msg="Currently no notification found for you.";
                                    Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                                    tv_notice.setText(err_msg);
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
                    /*params.put("Authorization", "Bearer " + tokenData);*/
                   /* params.put("userid", "71911");
                    params.put("start", "1");
                    params.put("next ", "10");*/
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
}
