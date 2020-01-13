package com.galwaykart.partialReturn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.shipyaari.StepperViewDemo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumitsaini on 5/23/2018.
 */

public class ReturnedOrderDetails extends BaseActivity{

    ListView lv_return_order_list;
    String st_Order_details_URL = "",st_Cancel_Order_URL="";
    RequestQueue queue = null;

    JsonObjectRequest jsObjRequest = null;
    JsonObjectRequest jsonObjRequest = null;
    JSONArray array_order_list = null,array_product_list=null;

    ArrayList<HashMap<String, String>> arrayList;
    // ArrayList<OrderDetailsItemModel> arrayList_order;
    HashMap<String, String> hashMap;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    final String TAG_image= "image";
    final String TAG_reason_id = "reason_id";
    final String TAG_name= "name";
    final String TAG_resolution_id= "resolution_id";
    final String TAG_condition_id = "condition_id";
    final String TAG_created_at= "created_at";
    final String TAG_qty_requested = "qty_requested";

    String TAG_year = "";
    boolean show_order_id=false;
    TextView tv_order_id,tv_cancel_order;

    String st_rtn_order_id = "",st_product_image = "",st_product_id="",st_return_order_id="",st_product_amt="",st_order_status=""
            ,st_product_name="",st_product_order_date="",st_qty_requested="",
            st_order_total_amt="",st_selected_rma_id="",st_reason_id="",st_resolution_id="",st_condition_id="";

    TextView tv_title,tv_order_total;

    String st_sales_url="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//
        Intent intent = new Intent(ReturnedOrderDetails.this, ReturnedOrderList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returned_order_list);

        initNavigationDrawer();
        tv_title=(TextView)findViewById(R.id.tv_title);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_cancel_order = (TextView)findViewById(R.id.tv_cancel_order);
        tv_order_total = (TextView)findViewById(R.id.tv_order_total);
        pref = CommonFun.getPreferences(getApplicationContext());

//        st_order_id = pref.getString("Order_ID","");
//        st_order_status = pref.getString("st_status","");
//        st_order_total_amt = pref.getString("selected_order_total","");
        st_selected_rma_id = pref.getString("st_selected_rma_id","");
        st_rtn_order_id = pref.getString("st_rtn_order_id","");

        tv_order_total.setText("Rs. "+st_order_total_amt);


        show_order_id = true;

        if(show_order_id == true)
            tv_order_id.setVisibility(View.VISIBLE);

//        st_order_status=st_order_status.trim();
//        if(st_order_status.equalsIgnoreCase("canceled")
//                || st_order_status.equalsIgnoreCase("processing")||
//                st_order_status.equalsIgnoreCase("complete"))
//            tv_cancel_order.setVisibility(View.GONE);
//        else
//            tv_cancel_order.setVisibility(View.VISIBLE);


        arrayList = new ArrayList<HashMap<String,String>>();
        //arrayList_order=new ArrayList<OrderDetailsItemModel>();


        lv_return_order_list = (ListView) findViewById(R.id.lv_return_order_list);

        st_Order_details_URL = Global_Settings.api_url+"glaze/rmaorderdetail.php?rma_id="+st_selected_rma_id;

       // //Log.d("st_Order_details_URL",st_Order_details_URL);

        callOrderDetails();


    }


    private void trackYourOrder(int position) {

        String st_selected_Track_id = "785038481040";

        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("st_selected_Track_id",st_selected_Track_id);
        editor.commit();

        Intent intent = new Intent(ReturnedOrderDetails.this, StepperViewDemo.class);
        startActivity(intent);
        CommonFun.finishscreen(ReturnedOrderDetails.this);

    }

    private void callOrderDetails() {

        arrayList.clear();
        pDialog = new TransparentProgressDialog(ReturnedOrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        queue = Volley.newRequestQueue(this);
        // RequestQueue queue = Volley.newRequestQueue(this);

        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_Order_details_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            array_product_list = jsonObject.getJSONArray("productdetails");
                            array_order_list = jsonObject.getJSONArray("orderdetails");

                            int product_list_length = array_product_list.length();
                            int order_list_length = array_order_list.length();

                            JSONObject obj = array_order_list.getJSONObject(0);
                            st_return_order_id = obj.getString("order_id");

                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = array_product_list.getJSONObject(i);

                                st_product_image = order_list_obj.getString("images");
                                st_product_id = order_list_obj.getString("product_id");
                                st_product_order_date = order_list_obj.getString("created_at");

                                st_reason_id = order_list_obj.getString("reason_id");
                                st_resolution_id = order_list_obj.getString("resolution_id");
                                st_product_name = order_list_obj.getString("name");
                                st_condition_id = order_list_obj.getString("condition_id");
                                st_qty_requested = order_list_obj.getString("qty_requested");




                                hashMap = new HashMap<String, String>();

                                hashMap.put(TAG_name,st_product_name  +" - "+st_product_id);
                                hashMap.put(TAG_image,st_product_image);
                                hashMap.put(TAG_created_at, st_product_order_date);
                                hashMap.put(TAG_reason_id ,"Reason:    "+st_reason_id);
                                hashMap.put(TAG_resolution_id,"Resolution ID:    "+st_resolution_id);
                                hashMap.put(TAG_condition_id ,"Product Condition:    "+st_condition_id);
                                hashMap.put(TAG_qty_requested ,"Requested Qty:    "+st_qty_requested);



                                arrayList.add(hashMap);
                              //  //Log.d("itemList",arrayList+"");

//                                  OrderDetailsItemModel orderDetailsItemModel=new OrderDetailsItemModel(st_product_image,
//                                          st_product_name  +" - "+st_product_id,
//                                          "Amount: ₹ "+st_product_amt,
//                                        st_product_order_date,
//                                          "Total Quantity:  " +st_total_qty_ordered);
//
//                                arrayList_order.add(orderDetailsItemModel);
                            }

                        //    //Log.d("itemListafter",arrayList+"");

                            //   OrderDetailsAdapter orderDetailsAdapter=
                            //new OrderDetailsAdapter(arrayList_order);

                            //  lv_order_list.setAdapter(orderDetailsAdapter);
                  /*
				 *  Set value on List
				 */
                            ListAdapter listAdapter = new OrderReturnDetailsAdapter(ReturnedOrderDetails.this, arrayList, R.layout.activity_returned_order_details,
                                    new String[]{TAG_name,
                                            TAG_image,
                                            TAG_created_at,
                                            TAG_reason_id,
                                            TAG_resolution_id,
                                            TAG_condition_id,
                                            TAG_qty_requested
                                    },
                                    new int[]{R.id.product_name_id,
                                            R.id.img_view_ordered_product,
                                            R.id.req_date_time,
                                            R.id.reason_id,
                                            R.id.resolution_id,
                                            R.id.condition_id,
                                            R.id.total_product_qty});


                            if(listAdapter.getCount() >0 ) {
                                lv_return_order_list.invalidate();
                                lv_return_order_list.setAdapter(listAdapter);
                                tv_order_id.setText("Order # " + st_rtn_order_id);
                                //tv_cancel_order.setVisibility(View.VISIBLE);
                                tv_title.setText("Your Retured Order");
                            }
                            else
                            {
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

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();

                    CommonFun.showVolleyException(error,ReturnedOrderDetails.this);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsObjRequest);

    }

//**********************************************************************************************************************************

    public class OrderReturnDetailsAdapter extends SimpleAdapter {

        final String TAG_image= "image";
        final String TAG_reason_id = "reason_id";
        final String TAG_name= "name";
        final String TAG_resolution_id= "resolution_id";
        final String TAG_condition_id = "condition_id";
        final String TAG_created_at= "created_at";
        final String TAG_qty_requested = "qty_requested";
        final String TAG_boolean="Return_check";

        Holder holder = null;
        Context ctx;

        ArrayList<HashMap<String, String>> arrayList;
        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public OrderReturnDetailsAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);

            this.ctx = context;
            this.arrayList = (ArrayList<HashMap<String, String>>) data;

        }


        class Holder{

            ImageView img_view_ordered_product;
            TextView product_name_id;
            TextView total_product_amt;
            TextView req_date_time;
            TextView reason_id;
            TextView resolution_id;
            TextView condition_id;
            TextView total_product_qty;
            Button bt_track_order;



        }


        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            int cur_position= position;

            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_returned_order_details, null);
                holder = new Holder();

                holder.img_view_ordered_product = (ImageView)convertView.findViewById(R.id.img_view_ordered_product);

                holder.product_name_id = (TextView) convertView.findViewById(R.id.product_name_id);
                holder.total_product_amt = (TextView) convertView.findViewById(R.id.total_product_amt);
                holder.total_product_qty = (TextView) convertView.findViewById(R.id.total_product_qty);

                holder.req_date_time = (TextView) convertView.findViewById(R.id.req_date_time);
                holder.reason_id = (TextView) convertView.findViewById(R.id.reason_id);
                holder.resolution_id = (TextView) convertView.findViewById(R.id.resolution_id);

                holder.condition_id = (TextView) convertView.findViewById(R.id.condition_id);
                holder.total_product_qty = (TextView) convertView.findViewById(R.id.total_product_qty);
                holder.bt_track_order = (Button) convertView.findViewById(R.id.bt_track_order);


                convertView.setTag(holder);

            }
            else {

                holder = (Holder) convertView.getTag();
            }


            holder.product_name_id.setText(arrayList.get(position).get(TAG_name).toString());
            holder.reason_id.setText(arrayList.get(position).get(TAG_reason_id).toString());
            holder.req_date_time.setText(arrayList.get(position).get(TAG_created_at).toString());
            holder.resolution_id.setText(arrayList.get(position).get(TAG_resolution_id).toString());
            holder.condition_id.setText(arrayList.get(position).get(TAG_condition_id).toString());
            holder.total_product_qty.setText(arrayList.get(position).get(TAG_qty_requested).toString());





            Picasso.get()
                    .load(arrayList.get(position).get(TAG_image).toString())
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(150, 150)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.img_view_ordered_product);


            holder.bt_track_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    trackYourOrder(position);

                }
            });




            return convertView;
        }
    }



}
