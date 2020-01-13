package com.galwaykart.partialReturn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

/**
 * Created by sumitsaini on 5/23/2018.
 */

public class ReturnedOrderList extends BaseActivity {
    ListView lv_return_order_list;
    String st_Order_id_URL = "";
    RequestQueue queue = null;

    TextView tv_cancel_order;
    JsonObjectRequest jsObjRequest = null;
    JSONArray array_oredr_list = null;

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    final String TAG_orderid= "orderid";
    final String TAG_rma_id = "rma_id";
    final String TAG_created_at= "created_at";
    final String TAG_increment_id= "increment_id";
    final String TAG_status_id = "status_id";

    String TAG_Order_date= "";
    String TAG_year = "";
    boolean show_order_id = false;

    String[] arr_order_id = {};
    String[] arr_order_status = {};
    String[] arr_order_total = {};
    String[] arr_order_rma_id={};
    String st_orderid = "",st_status_id = "",st_created_at="",st_increment_id="",st_rma_id="";
    TextView tv_order_id;
    TextView tv_title;

    ImageView iv_image_no_details;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//
        Intent intent = new Intent(ReturnedOrderList.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returned_order_list);
         initNavigationDrawer();

        arrayList = new ArrayList<HashMap<String,String>>();

        queue = Volley.newRequestQueue(this);


        iv_image_no_details=(ImageView)findViewById(R.id.iv_image_no_details);
        iv_image_no_details.setVisibility(View.GONE);
        pref = CommonFun.getPreferences(getApplicationContext());

        String cust_id=pref.getString("st_login_id","");

        lv_return_order_list = (ListView) findViewById(R.id.lv_return_order_list);
        tv_cancel_order = (TextView)findViewById(R.id.tv_cancel_order);
        st_Order_id_URL = Global_Settings.api_url+"glaze/rmaorderlist.php?cid="+cust_id;
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_order_id.setVisibility(View.GONE);

        callOrderList();

        lv_return_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String st_selected_rma_id = arr_order_rma_id[position];
                String st_order_id = arr_order_id[position];

                pref = CommonFun.getPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("st_selected_rma_id",st_selected_rma_id);
                editor.putString("st_rtn_order_id",st_order_id);
                editor.commit();

                Intent intent = new Intent(ReturnedOrderList.this, ReturnedOrderDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(ReturnedOrderList.this);

            }
        });

    }

    private void callOrderList() {

        pDialog = new TransparentProgressDialog(ReturnedOrderList.this);
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

                   // ////Log.d("123response",""+response);

                    if (response != null) {
                        try {


                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            array_oredr_list = jsonObject.getJSONArray("orderlist");
                            int order_list_length = array_oredr_list.length();
                            arr_order_id = new String[order_list_length];
                            arr_order_status = new String[order_list_length];
                            arr_order_total  = new String[order_list_length];
                            arr_order_rma_id = new String[order_list_length];

                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = array_oredr_list.getJSONObject(i);

                                st_orderid = order_list_obj.getString("order_id");
                                st_rma_id = order_list_obj.getString("rma_id");
                                st_created_at = order_list_obj.getString("created_at");
                                st_increment_id = order_list_obj.getString("increment_id");
                                st_status_id = order_list_obj.getString("status_id");

                                arr_order_id[i] = st_orderid;
                                arr_order_status[i] = st_status_id;
                                arr_order_rma_id[i] = st_rma_id;

//                                arr_order_total[i]=st_subtotal;

                                    /*
                                       get Month,date and year from date format
                                     */
                                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dt=format1.parse(st_created_at);

                                String st_monthNumber  = (String) DateFormat.format("MMM",   dt); // month
                                String st_year         = (String) DateFormat.format("yyyy", dt); // year
                                String st_sday          = (String) DateFormat.format("dd",   dt); // date


                                hashMap = new HashMap<String, String>();

                                hashMap.put(TAG_orderid,"Order ID: "+st_orderid  +"("+st_status_id+")");
                                hashMap.put("date",st_sday);
                                hashMap.put(TAG_year, st_monthNumber+"-"+st_year);
                                hashMap.put(TAG_increment_id, "Increment ID : "+st_increment_id);

//                                hashMap.put(TAG_total_qty_ordered,"Total Quantity:  " +st_total_qty_ordered);
//                                hashMap.put(TAG_subtotal, "Amount: ₹ "+st_subtotal);

                                arrayList.add(hashMap);
                                ////Log.d("itemList",arrayList+"");

                            }
                            	/*
				 * Set value on List
				 */
                            ListAdapter listAdapter = new SimpleAdapter(ReturnedOrderList.this, arrayList,
                                    R.layout.activity_return_order_list_item,
                                    new String[]{TAG_orderid,
                                            "date",
                                            TAG_increment_id,
                                            TAG_year},
                                    new int[]{R.id.return_order_id,
                                            R.id.text_view_date,
                                            R.id.increment_id,
                                            R.id.tv_month_year});

                            if(listAdapter.getCount()>0){
                                lv_return_order_list.invalidate();
                                lv_return_order_list.setAdapter(listAdapter);
                                tv_title.setText("Your Returned Order");
                                lv_return_order_list.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lv_return_order_list.setVisibility(View.GONE);
                                iv_image_no_details.setVisibility(View.VISIBLE);
                                tv_title.setText("You have not returned any order yet..");
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
                    CommonFun.showVolleyException(error,ReturnedOrderList.this);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsObjRequest);

    }

}
