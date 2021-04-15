package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.profile.OrderDetailsAdapter;
import com.galwaykart.profile.OrderListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Show Order status to the user
 * Created by ankesh on 9/26/2017.
 */

public class OrderDetails extends BaseActivityWithoutCart{

    SharedPreferences pref;
    TextView tv_order_status;
    TextView tvContinueShop;
    TextView tvOrderList;

    String orderhash="";

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;
    TransparentProgressDialog pDialog;
    final String TAG_image= "image";
    final String TAG_product_id = "product_id";
    final String TAG_name= "name";
    final String TAG_total_qty_ordered= "qty_ordered";
    final String TAG_original_price = "original_price";
    final String TAG_created_at= "created_at";
    String TAG_year = "";
    boolean show_order_id=false;
    TextView tv_order_id,tv_cancel_order;

    String st_order_id = "",st_product_image = "",st_product_id="",st_total_qty_ordered="",st_product_amt="",st_order_status=""
            ,st_product_name="",st_product_order_date="",st_order_total_amt="";

    JsonObjectRequest jsObjRequest = null;
    TextView tv_title,tv_order_total;
    JSONArray array_order_list = null;

    ListView lv_order_list;
    View underline3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        initNavigationDrawer();


        pref = CommonFun.getPreferences(getApplicationContext());
        orderhash=pref.getString("orderhash","");

        String orderpono=pref.getString("orderpono","");
        tv_order_status=(TextView)findViewById(R.id.tv_order_status);

        tvOrderList=(TextView)findViewById(R.id.tvOrderList);
        tvOrderList.setPaintFlags(tv_order_status.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(OrderDetails.this, OrderListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(OrderDetails.this);
            }
        });

        String payment_status=pref.getString("paymentdue","");

        /**
         * check payment status
         * failed or pass
         */

        if(payment_status.equalsIgnoreCase("fail"))
            tv_order_status.setText("Your payment for the order has been failed\nPlease try again.");
        else
            tv_order_status.setText("Your order has been Placed:\nYour Order # is: " +orderhash);

        /**
         * reset temp storage of the order details
         * and delete cart item details from db
         */
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("orderhash","");
        editor.putString("paymentdue","");
        editor.putString("orderpono","");
        editor.commit();

        DatabaseHandler dbh=new DatabaseHandler(this);
        dbh.deleteCartItem();
        dbh.deleteAllData();


        tvContinueShop=(TextView)findViewById(R.id.tvContinueShop);
        tvContinueShop.setPaintFlags(tv_order_status.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvContinueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(OrderDetails.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(OrderDetails.this);

            }
        });


        underline3=(View)findViewById(R.id.underline3);
        lv_order_list=(ListView)findViewById(R.id.lv_order_list);
        lv_order_list.setVisibility(View.GONE);
        //callOrderDetails();

    }

    private void callOrderDetails() {

        String st_Order_details_URL = Global_Settings.api_url+"glaze/order_details.php?id="+orderhash;
        arrayList = new ArrayList<HashMap<String,String>>();

        pDialog = new TransparentProgressDialog(OrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
           jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_Order_details_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    ////Log.d("response",response.toString());


                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            array_order_list = jsonObject.getJSONArray("orderdetails");
                            int order_list_length = array_order_list.length();

                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = array_order_list.getJSONObject(i);

                                st_product_image = order_list_obj.getString(TAG_image);
                                st_product_id = order_list_obj.getString(TAG_product_id);
                                st_product_order_date = order_list_obj.getString(TAG_created_at);
                                st_total_qty_ordered = order_list_obj.getString(TAG_total_qty_ordered);
                                st_product_amt = order_list_obj.getString(TAG_original_price);
                                st_product_name = order_list_obj.getString(TAG_name);


                                hashMap = new HashMap<String, String>();

                                hashMap.put(TAG_name,st_product_name  +" - "+st_product_id);
                                hashMap.put(TAG_image,st_product_image);
                                hashMap.put(TAG_created_at, st_product_order_date);
                                hashMap.put(TAG_total_qty_ordered,"Total Quantity:  " +st_total_qty_ordered);
                                hashMap.put(TAG_original_price, "Amount: "+st_product_amt);

                                arrayList.add(hashMap);
                                ////Log.d("itemList",arrayList+"");

                            }



                            	/*
				 * Set value on List
				 */
                            ListAdapter listAdapter = new OrderDetailsAdapter(OrderDetails.this, arrayList, R.layout.order_details_item_old_march21,
                                    new String[]{TAG_name,TAG_image,TAG_created_at,TAG_total_qty_ordered,TAG_original_price},
                                    new int[]{R.id.product_name_id,
                                            R.id.img_view_ordered_product,
                                            R.id.order_date_time,
                                            R.id.total_product_qty,
                                            R.id.total_product_amt});


                            if(listAdapter.getCount() >0 ) {
                                lv_order_list.invalidate();
                                lv_order_list.setAdapter(listAdapter);
                                tv_order_id.setText("Order # " + st_order_id);
                                //tv_cancel_order.setVisibility(View.VISIBLE);
                                tv_title.setText("Your Order");
                                lv_order_list.setVisibility(View.VISIBLE);
                                underline3.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tv_title.setText("You have not placed any order yet.");
                                lv_order_list.setVisibility(View.GONE);
                                underline3.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent=new Intent(OrderDetails.this, ExceptionError.class);
                            startActivity(intent);
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();

                    CommonFun.showVolleyException(error,OrderDetails.this);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

}
