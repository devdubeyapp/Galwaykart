package com.galwaykart.profile;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

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

    ListView lv_order_list;
    String st_Order_id_URL = "";
    RequestQueue queue = null;

    TextView tv_cancel_order;
    JsonObjectRequest jsObjRequest = null;
    JSONArray array_oredr_list = null;

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    final String TAG_orderid= "increment_id";
    final String TAG_status = "status_label";
    final String TAG_created_at= "created_at";
    final String TAG_total_qty_ordered= "total_qty_ordered";
    final String TAG_subtotal = "subtotal";
    String TAG_is_return= "is_return";
    final String TAG_entity_id="entity_id";

    String TAG_year = "";
    boolean show_order_id = false;

    String[] arr_order_id = {};
    String[] arr_order_status = {};
    String[] arr_order_total = {};
    String[] arr_is_return;
    String[] arr_entity_id;
    String st_orderid = "",st_status = "",st_created_at="",st_is_order_return="",
            st_total_qty_ordered="",st_subtotal="";
    TextView tv_order_id;
    TextView tv_title;
    ImageView iv_image_no_details;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            Intent intent = new Intent(OrderListActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initNavigationDrawer();

        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_image_no_details=findViewById(R.id.iv_image_no_details);
        iv_image_no_details.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        show_order_id = false;

        arrayList = new ArrayList<HashMap<String,String>>();

        queue = Volley.newRequestQueue(this);


        pref = CommonFun.getPreferences(getApplicationContext());

        String cust_id=pref.getString("st_login_id","");
        //////////Log.d("cust_id",cust_id);

        lv_order_list = findViewById(R.id.lv_order_list);
        tv_cancel_order = (TextView)findViewById(R.id.tv_cancel_order);

        //st_Order_id_URL = Global_Settings.api_url+"glaze/order_list.php?id="+cust_id;
        st_Order_id_URL= Global_Settings.api_url+"rest/V1/m-order/1/100";
        //Log.d("responselist_a",st_Order_id_URL);
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_order_id.setVisibility(View.GONE);

        callOrderList();

        lv_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String selected_order_id = arr_order_id[position];
                String selected_order_status = arr_order_status[position];
                String selected_order_total = arr_order_total[position];
                String selected_order_rtn = arr_is_return[position];


                SharedPreferences.Editor editor = pref.edit();
                editor.putString("st_status",selected_order_status);
                editor.putString("Order_ID",selected_order_id);
               // editor.putString("selected_order_rtn",selected_order_rtn);
                editor.putString("entity_id",arr_entity_id[position]);

                editor.putString("selected_order_total",selected_order_total);

                editor.commit();



                Intent intent = new Intent(OrderListActivity.this, OrderDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(OrderListActivity.this);

            }
        });
    }

    private void callOrderList() {

        pref = CommonFun.getPreferences(getApplicationContext());
        String st_token_data = pref.getString("tokenData","");

        //Log.d("responselist_a",st_token_data);
        pDialog = new TransparentProgressDialog(OrderListActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_Order_id_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                   Log.d("responselist_a",response.toString());


                    if (response != null) {
                        try {

                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            array_oredr_list = jsonObject.getJSONArray("items");
                            int order_list_length = array_oredr_list.length();
                            arr_order_id = new String[order_list_length];
                            arr_order_status = new String[order_list_length];
                            arr_order_total  = new String[order_list_length];
                            arr_is_return = new String[order_list_length];
                            arr_entity_id=new String[order_list_length];

                            Log.d("responselist_a", String.valueOf(order_list_length));

                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = array_oredr_list.getJSONObject(i);

                                st_orderid = order_list_obj.getString(TAG_orderid);
                                st_status = order_list_obj.getString(TAG_status);
                                st_created_at = order_list_obj.getString(TAG_created_at);
                                st_total_qty_ordered = order_list_obj.getString(TAG_total_qty_ordered);
                                st_subtotal = order_list_obj.getString(TAG_subtotal);

                                if(order_list_obj.has(TAG_is_return))
                                    st_is_order_return = order_list_obj.getString(TAG_is_return);

                                //String order_label=order_list_obj.getString("lable");

                                arr_entity_id[i]=order_list_obj.getString(TAG_entity_id);
                                arr_order_id[i] = st_orderid;
                                arr_order_status[i] = st_status;
                                arr_is_return[i] = st_is_order_return;

                                arr_is_return[i]="0";
                                arr_order_total[i]=st_subtotal;

                                    /*
                                       get Month,date and year from date format
                                     */
                                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dt=format1.parse(st_created_at);

                                String st_monthNumber  = (String) DateFormat.format("MMM",   dt); // month
                                String st_year         = (String) DateFormat.format("yyyy", dt); // year
                                String st_sday          = (String) DateFormat.format("dd",   dt); // date


                                hashMap = new HashMap<String, String>();

                                hashMap.put(TAG_orderid,"Order ID: "+st_orderid  +"("+st_status+")");
                                hashMap.put("date",st_sday);
                                hashMap.put(TAG_year, st_monthNumber+"-"+st_year);

                                hashMap.put(TAG_total_qty_ordered,"Total Quantity:  " +st_total_qty_ordered);
                                hashMap.put(TAG_subtotal, "Amount: ₹ "+st_subtotal);
                                hashMap.put(TAG_is_return, arr_is_return[i]);

                                arrayList.add(hashMap);
                                //////////Log.d("itemList",arrayList+"");

                            }



                            	/*
				 * Set value on List
				 */
                            ListAdapter listAdapter = new SimpleAdapter(OrderListActivity.this, arrayList,
                                    R.layout.activity_order_list_items,
                                    new String[]{TAG_orderid,"date",TAG_total_qty_ordered,TAG_subtotal,TAG_year},
                                    new int[]{R.id.order_id, R.id.text_view_date, R.id.total_qty, R.id.total_amt, R.id.tv_year});



                            if(listAdapter.getCount()>0){
                                lv_order_list.setVisibility(View.VISIBLE);
                                lv_order_list.invalidate();
                                lv_order_list.setAdapter(listAdapter);
                                tv_title.setText("Your Order");
                            }
                        else
                            {
                                lv_order_list.setVisibility(View.GONE);
                                iv_image_no_details.setVisibility(View.VISIBLE);
                                tv_title.setText("You have not placed any order yet.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

//                    CommonFun.alertError(OrderListActivity.this,error.toString());
//
//                    error.printStackTrace();
                    CommonFun.showVolleyException(error, OrderListActivity.this);
                }

            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + st_token_data);
                    //   params.put("Content-Type","application/json");

                    return params;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }
}
