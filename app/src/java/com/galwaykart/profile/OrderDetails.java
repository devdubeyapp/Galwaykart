package com.galwaykart.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.shipyaari.StepperViewDemo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sumitsaini on 9/27/2017.
 */

public class OrderDetails extends BaseActivity{

    ListView lv_order_details;
    String st_Order_details_URL = "",st_Cancel_Order_URL="";
    RequestQueue queue = null;
    int req_qty=0;
    JsonObjectRequest jsObjRequest = null;
    JsonObjectRequest jsonObjRequest = null;
    JSONArray array_order_list = null;

    ArrayList<String> arrList_selected_condition,arrList_selected_reason,arrList_selected_resolution,
            arrList_selected_product_id,
            arrList_entered_qty,
            arrList_selected_sku,
            arrList_selected_order_item_id;

    ArrayList<HashMap<String, String>> arrayList;
   // ArrayList<OrderDetailsItemModel> arrayList_order;
    HashMap<String, String> hashMap;
    TransparentProgressDialog pDialog;
    SharedPreferences pref;

    final String TAG_image= "image";
    final String TAG_product_id = "product_id";
    final String TAG_name= "name";
    final String TAG_total_qty_ordered= "qty_ordered";
    final String TAG_original_price = "original_price";
    final String TAG_created_at= "created_at";
    final String TAG_boolean="Return_check";
    final String TAG_edit_rtn = "Edit_Qty";
    final String TAG_qty_rtn = "Return_qty";
    final String TAG_enter_qty_rtn = "enter_qty";
    final String TAG_isClickable = "check_box";
    final String TAG_rtn_check = "uncheck";
    final String TAG_trigger_no_sale="trigger_no_sale";
    final String TAG_shipmentType="shipmentType";
    final String TAG_sku="sku";
    final String TAG_TRACK_ORDER = "track_order";

    String TAG_year = "";
    EditText return_qty;
    Dialog dialog;
    boolean show_order_id=false;
    TextView tv_order_id,tv_cancel_order;
    String[] arr_reason,arr_resolution,arr_condition;
    int selected_reason_id,selected_resolution_id,selected_condition_id;
    ArrayAdapter<String> adapter_reason,adapter_resolution,adapter_condition;
    String st_qty="";
    String st_track_order_option="",st_order_rtn="";

    String st_order_id = "",st_product_image = "",st_product_id="",st_total_qty_ordered="",st_selected_qty="",
            st_product_amt="",st_order_status="",st_trigger_no_sale="",return_request_sales_url="",st_shipmentType=""
            ,st_product_name="",st_product_order_date="",st_order_total_amt="";

    TextView tv_title,tv_order_total;

    String[] arr_product_id={},arr_product_name={},arr_product_qty={},arr_boolean={},arr_product_order_date,
            arr_product_amt,arr_product_img,arr_edit_rtn_qty,arr_rtn_qty,arr_req_rtn_qty,arr_shipment_type,
    arr_return_index,arr_tracking_id,arr_selected_sku,arr_sku,arr_order_item_id,arr_track_order,
    arr_check_rtn_btn;
    String st_sales_url="",dataValues="",st_selected_product_id="",st_selected_reason="",st_selected_resolution="",st_selected_condition="",
            cust_id="",st_order_item_id="",st_product_sku="",
            st_selected_order_item_id="",input_data="",dataValuesSales="",input_data_sales="",
            return_request_url="";
    int current_pos;
    String arr_enter_qty[];
    Button btn_return_submit;

    String[] arr_return_item;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//
        Intent intent = new Intent(OrderDetails.this, OrderListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initNavigationDrawer();
        tv_title=(TextView)findViewById(R.id.tv_title);
        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("st_qty","");
        editor.putString("st_selected_reason","");
        editor.putString("st_selected_resolution","");
        editor.putString("st_selected_condition","");
        editor.commit();
//***********************************************************************************************************************************
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_cancel_order = (TextView)findViewById(R.id.tv_cancel_order);
        tv_order_total = (TextView)findViewById(R.id.tv_order_total);
        btn_return_submit= (Button)findViewById(R.id.btn_return_submit);
        pref = CommonFun.getPreferences(getApplicationContext());
        dialog = new Dialog(OrderDetails.this);
        arrList_selected_resolution = new ArrayList<>();
        arrList_selected_condition = new ArrayList<>();
        arrList_selected_order_item_id = new ArrayList<>();
        arrList_selected_product_id = new ArrayList<>();
        arrList_selected_reason = new ArrayList<>();
        arrList_entered_qty = new ArrayList<>();
        arrList_selected_sku = new ArrayList<>();

        st_order_id = pref.getString("Order_ID","");
        st_order_status = pref.getString("st_status","");
        st_order_total_amt = pref.getString("selected_order_total","");
        st_order_rtn = pref.getString("selected_order_rtn","");
        //////Log.d("st_order_rtn",st_order_rtn);

        double number = Double.parseDouble(st_order_total_amt);
        String rounded_total_amt = String.format ("%,.2f", number);// round off decimal value upto 2 digit
        cust_id=pref.getString("st_login_id","");
       // ////Log.d("cust_id",cust_id);
        tv_order_total.setText(" Total Amount : ₹ "+rounded_total_amt);
        show_order_id = true;

        if(show_order_id == true)
            tv_order_id.setVisibility(View.VISIBLE);
        st_order_status=st_order_status.trim();

//        if(st_order_status.equalsIgnoreCase("canceled")
//                || st_order_status.equalsIgnoreCase("processing")
//                || !st_order_rtn.equalsIgnoreCase("0")
//                ) {
//            tv_cancel_order.setVisibility(View.GONE);
////            tv_cancel_order.setText("Return Order");
////            tv_cancel_order.setVisibility(View.VISIBLE);
//        }
//        else if(st_order_status.equalsIgnoreCase("complete")
//                && st_order_rtn.equalsIgnoreCase("0")) {
//            tv_cancel_order.setText("Return Order");
//            tv_cancel_order.setVisibility(View.VISIBLE);
//        }
//        else {
//            tv_cancel_order.setText("Cancel Order");
//            tv_cancel_order.setVisibility(View.VISIBLE);
//        }

        if(st_order_status.equalsIgnoreCase("pending"))
        {
            tv_cancel_order.setVisibility(View.VISIBLE);
            tv_cancel_order.setText("Cancel Order");
        }
        else if(st_order_status.equalsIgnoreCase("complete")
                && st_order_rtn.equalsIgnoreCase("0")) {
            tv_cancel_order.setText("Return Order");
            tv_cancel_order.setVisibility(View.VISIBLE);
        }
        else {
            //tv_cancel_order.setText("Cancel Order");
            tv_cancel_order.setVisibility(View.GONE);
        }


        arrayList = new ArrayList<HashMap<String,String>>();
        //arrayList_order=new ArrayList<OrderDetailsItemModel>();
        lv_order_details = (ListView) findViewById(R.id.lv_order_details);
        lv_order_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            }
        });
        btn_return_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int total_return_items=0;
                for(int i=0;i<arr_sku.length;i++)
                {
                    if(arr_return_index[i].equalsIgnoreCase("true"))
                        total_return_items++;

                }
                if(total_return_items > 0){
                String data = "{\"cid\":\""+cust_id+"\"," +
                        "\"orederid\":\""+st_order_id+"\"," +
                        "\"products\":[";
                    for (int i = 0; i < arrList_selected_product_id.size(); i++) {
                        String st_product_id = arrList_selected_product_id.get(i);
                        String st_order_item_id = arrList_selected_order_item_id.get(i);
                        String st_reason_id = arrList_selected_reason.get(i);
                        String st_resolution_id = arrList_selected_resolution.get(i);
                        String st_condition_id = arrList_selected_condition.get(i);
                        st_selected_qty = arrList_entered_qty.get(i);
                        //////Log.d("st_selected_qty",st_selected_qty);
                            if (dataValues.equals("")) {
                                dataValues = "{\"Product_id\":\"" + st_product_id + "\"," +
                                        "\"order_item_id\":\"" + st_order_item_id + "\"," +
                                        "\"reason_id\":\"" + st_reason_id + "\"," +
                                        "\"resolution_id\":\"" + st_resolution_id + "\"," +
                                        "\"condition_id\":\"" + st_condition_id + "\"," +
                                        "\"qty_requested\":\"" + st_selected_qty + "\"}";
                            } else {
                                dataValues = dataValues + "," +
                                        "{\"Product_id\":\"" + st_product_id + "\"," +
                                        "\"order_item_id\":\"" + st_order_item_id + "\"," +
                                        "\"reason_id\":\"" + st_reason_id + "\"," +
                                        "\"resolution_id\":\"" + st_resolution_id + "\"," +
                                        "\"condition_id\":\"" + st_condition_id + "\"," +
                                        "\"qty_requested\":\"" + st_selected_qty + "\"}";
                            }
                        }
                        input_data = data + dataValues + "]" + "}";
                       // ////Log.d("input_data", input_data);
//  Input Parameter sales



                    String sales_data = "{\"OrderID\":\""+st_order_id+"\"," +
                            "\"itemlist\":[";
                    //for (int i = 0; i < arrList_selected_sku.size(); i++) {
                    for (int i = 0; i < arr_sku.length; i++) {
                        if (arr_return_index[i].equalsIgnoreCase("true")){
                            String st_selected_product_sku = arr_sku[i];
                        st_selected_qty = arr_enter_qty[i];
                        //////Log.d("st_selected_qty", arr_enter_qty[i]);

                        if (dataValuesSales.equals("")) {
                            dataValuesSales = "{\"Itemcode\":\"" + st_selected_product_sku + "\"," +
                                    "\"CancelQty\":\"" + st_selected_qty + "\"}";
                        } else {
                            dataValuesSales = dataValuesSales + "," +
                                    "{\"Itemcode\":\"" + st_selected_product_sku + "\"," +
                                    "\"CancelQty\":\"" + st_selected_qty + "\"}";
                        }
                    }
                    }
                    input_data_sales = sales_data + dataValuesSales + "]}";
                    //////Log.d("input_data_sales", input_data_sales);
//                    returnRequest();
                       returnRequestSales();
                    }
                    else{
                        CommonFun.alertError(OrderDetails.this, "Please enter return request form first");
                    }
            }
        });
        st_Order_details_URL = Global_Settings.api_url+"glaze/order_details.php?id="+st_order_id;
        //Log.d("st_Order_details_URL",st_Order_details_URL);
        // st_Order_details_URL = "http://www.galwaykart.in/glaze/order_details.php?id=000018118";
        //////Log.d("st_Order_details_URL",st_Order_details_URL);
        callOrderDetails();
        tv_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st_cancel_return = tv_cancel_order.getText().toString();
                if(st_cancel_return.equalsIgnoreCase("Cancel Order")) {
                    st_Cancel_Order_URL = Global_Settings.api_url + "glaze/order_cancel.php?id=" + st_order_id;

                    st_sales_url = Global_Settings.st_sales_api + "POCancel?POId=" + st_order_id + "&spmode=0";
                    //cancelOrder_SalesAPI();
                    cancelOrder();
                }
                else if(st_cancel_return.equalsIgnoreCase("Return Order")) {
                    for(int i=0; i<arr_boolean.length; i++) {
                        if(!arr_boolean[i].toString().equalsIgnoreCase("true")) {
                            arr_boolean[i] = "true";
                            btn_return_submit.setVisibility(View.VISIBLE);
                        }
                        else{
                            arr_boolean[i] = "false";
                            btn_return_submit.setVisibility(View.GONE);
                        }
                    }
                    //("itemListafter",arrayList+"");
                    setListAdapter();
                }
            }
        });
    }
    private void returnRequestSales() {
        return_request_sales_url = Global_Settings.st_sales_api+"LoadOrderReturnApi/";
        //return_request_sales_url="http://it.galway.in/api/GalwayKartApi";
        //////Log.d("return_res_url", return_request_sales_url);
        pDialog = new TransparentProgressDialog(OrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest req = new StringRequest(Request.Method.POST,
                    return_request_sales_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String  response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //////Log.d("VOLLEY12345", response.toString());
                            try {
                                //CommonFun.alertError(Payment_Method_Activity.this,response);
                                JSONObject jsonObject=new JSONObject(String.valueOf(response));
                                String st_msg=jsonObject.getString("Message");
                                String st_status=jsonObject.getString("SaveStatus");
                                if(st_status.equalsIgnoreCase("1")) {  // status = 1 (Successful in sales)
                                    returnRequest();
                                }
                                else{

                                    alertError(st_msg);
                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
//                                CommonFun.alertError(OrderDetails.this,e.toString());
                                Intent intent=new Intent(OrderDetails.this, ExceptionError.class);
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // ////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();
                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
//                    NetworkResponse response = error.networkResponse;
//                    if (error instanceof ServerError && response != null) {
//                        try {
//                            String res = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                            // Now you can use any deserializer to make sense of data
//                            JSONObject obj = new JSONObject(res);
//                        } catch (UnsupportedEncodingException e1) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (Exception e2) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//                    }
                    CommonFun.showVolleyException(error,OrderDetails.this);
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
                public byte[] getBody() {
                    try {
                        return input_data_sales == null ? null : input_data_sales.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data_sales, "utf-8");
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    1,
                    1);
            req.setRetryPolicy(retryPolicy);
            requestQueue.add(req);
        } catch (Exception e) {
            e.printStackTrace();
            //////Log.d("error...","Error");
        }


    }

    public void alertError(String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(OrderDetails.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    Intent intent = new Intent(OrderDetails.this,OrderListActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(OrderDetails.this);
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void trackYourOrder(int position) {

        String st_selected_Track_id = arr_tracking_id[position];
        String st_selected_shipping_type = arr_shipment_type[position];

        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("st_selected_Track_id",st_selected_Track_id);
        editor.putString("st_selected_shipping_type",st_selected_shipping_type);
        editor.commit();

        Intent intent = new Intent(OrderDetails.this, StepperViewDemo.class);
        startActivity(intent);
        CommonFun.finishscreen(OrderDetails.this);

    }

    private void editRequestForReturn(int position) {
        current_pos = position;

        dialog.setContentView(R.layout.return_form_dialog);
        return_qty = (EditText) dialog.findViewById(R.id.return_qty);
        final Spinner spinner_resolution = (Spinner) dialog.findViewById(R.id.spinner_resolution);
        final Spinner spinner_reason = (Spinner)dialog.findViewById(R.id.spinner_reason);
        final Spinner spinner_condition = (Spinner)dialog.findViewById(R.id.spinner_condition);
        Button btn_done = (Button) dialog.findViewById(R.id.btn_done);
        ImageButton imgBtCancel = (ImageButton) dialog.findViewById(R.id.imgBtCancel);


        pref = CommonFun.getPreferences(getApplicationContext());

        String st_rtn_qty = pref.getString("st_qty","");
        String st_rtn_reason = pref.getString("st_selected_reason","");
        String st_rtn_resolution = pref.getString("st_selected_resolution","");
        String st_rtn_condition = pref.getString("st_selected_condition","");



        if (st_rtn_qty != null && !st_rtn_qty.equals(""))
            return_qty.setText(st_rtn_qty);

        st_selected_product_id = arr_product_id[position];
        arrList_selected_product_id.add(st_selected_product_id);


        st_selected_order_item_id = arr_order_item_id[position];
        arrList_selected_order_item_id.add(st_selected_order_item_id);




        arr_reason = new String[5];

        arr_reason[0] = "Out of Service";
        arr_reason[1] = "Don't like";
        arr_reason[2] = "Wrong Color";
        arr_reason[3] = "Wrong Size";
        arr_reason[4] = "Other";

        arr_resolution = new String[3];
        arr_resolution[0] = "Exchange";
        arr_resolution[1] = "Refund";
        arr_resolution[2] = "Store Credit";

        arr_condition = new String[3];
        arr_condition[0] = "Unopened";
        arr_condition[1] = "Opened";
        arr_condition[2] = "Damaged";


        adapter_reason = new ArrayAdapter<String>(OrderDetails.this,android.R.layout.simple_spinner_dropdown_item,arr_reason);
        spinner_reason.setAdapter(adapter_reason);

        if (st_rtn_reason != null && !st_rtn_reason.equals(""))
            spinner_reason.setSelection(adapter_reason.getPosition(st_rtn_reason));


        adapter_resolution = new ArrayAdapter<String>(OrderDetails.this,android.R.layout.simple_spinner_dropdown_item,arr_resolution);
        spinner_resolution.setAdapter(adapter_resolution);
        if (st_rtn_resolution != null && !st_rtn_resolution.equals(""))
            spinner_resolution.setSelection(adapter_resolution.getPosition(st_rtn_resolution));

        adapter_condition = new ArrayAdapter<String>(OrderDetails.this,android.R.layout.simple_spinner_dropdown_item,arr_condition);
        spinner_condition.setAdapter(adapter_condition);
        if (st_rtn_condition != null && !st_rtn_condition.equals(""))
            spinner_condition.setSelection(adapter_condition.getPosition(st_rtn_condition));

        dialog.setCancelable(false);
        dialog.show();

        imgBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!arr_check_rtn_btn[current_pos].toString().equalsIgnoreCase("notClickalbe")) {
                    arr_check_rtn_btn[current_pos] = "Clickable";
                    setListAdapter();
                }
                dialog.dismiss();

            }
        });


        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                    selected_reason_id = 1;
                else if(position == 1)
                    selected_reason_id = 2;
                else if(position == 2)
                    selected_reason_id = 3;
                else if(position == 3)
                    selected_reason_id = 4;
                else if(position == 4)
                    selected_reason_id = 5;

                arrList_selected_reason.add(String.valueOf(selected_reason_id));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_resolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                        String selected_reason = holder.arr_reason[position];
                if(position == 0)
                    selected_resolution_id = 1;
                else if(position == 1)
                    selected_resolution_id = 2;
                else if(position == 2)
                    selected_resolution_id = 3;

                arrList_selected_resolution.add(String.valueOf(selected_resolution_id));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                    selected_condition_id = 1;
                else if(position == 1)
                    selected_condition_id= 2;
                else if(position == 2)
                    selected_condition_id = 3;

                arrList_selected_condition.add(String.valueOf(selected_condition_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref = CommonFun.getPreferences(getApplicationContext());

                st_qty = return_qty.getText().toString();
                st_selected_reason = spinner_reason.getSelectedItem().toString();
                st_selected_resolution = spinner_resolution.getSelectedItem().toString();
                st_selected_condition = spinner_condition.getSelectedItem().toString();

                //////Log.d("st_qty",st_qty);
                //////Log.d("req_qty",req_qty+"");

                String st_ordered_qty = arr_product_qty[current_pos];
                String [] st_qty_value = st_ordered_qty.split("\\.");

                int ordered_qty =Integer.parseInt(st_qty_value[0]);

                if(!st_qty.equalsIgnoreCase("")) {
                    req_qty = Integer.parseInt(st_qty);


                    if(req_qty > ordered_qty || req_qty <= 0 ){
                        CommonFun.alertError(OrderDetails.this,"Request for return quantity should be less then Ordered Quantity");
                    }
                    else {

                        if (!arr_edit_rtn_qty[current_pos].toString().equalsIgnoreCase("true")) {
                            arr_edit_rtn_qty[current_pos] = "true";
                        }

                        arr_enter_qty[current_pos] = st_qty;
                        arrList_entered_qty.add(st_qty);
                        arr_check_rtn_btn[current_pos] = "notClickalbe";
                        setListAdapter();

                        arr_req_rtn_qty[current_pos] = st_qty;
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("st_qty", arr_req_rtn_qty[current_pos].toString());
                        editor.putString("st_selected_reason",st_selected_reason);
                        editor.putString("st_selected_resolution",st_selected_resolution);
                        editor.putString("st_selected_condition",st_selected_condition);
                        editor.commit();



                        dialog.dismiss();
                    }

                }
                else{
                    CommonFun.alertError(OrderDetails.this,"Must fill return requested product quantity");
                }


            }
        });
    }

    private void requestForReturn(int position,String selected_sku) {

            if(!arr_return_index[position].toString().equalsIgnoreCase("true")) {
                arr_return_index[position] = "true";

            }
            else{
                arr_return_index[position] = "false";
                arr_enter_qty[position]="-1";


//                arrList_selected_resolution.clear();
//                arrList_selected_reason.clear();
//                arrList_selected_condition.clear();
//                arrList_entered_qty.clear();
//                arrList_selected_product_id.clear();
//                arrList_selected_order_item_id.clear();
//                arrList_selected_sku.clear();

               }
               setListAdapter();
            returnValue(position,selected_sku);

      }

    private void returnValue(int position, final String selected_sku) {
        current_pos = position;
        if(arr_return_index[position].equalsIgnoreCase("true")) {

            dialog.setContentView(R.layout.return_form_dialog);
            return_qty = (EditText) dialog.findViewById(R.id.return_qty);
            final Spinner spinner_resolution = (Spinner) dialog.findViewById(R.id.spinner_resolution);
            final Spinner spinner_reason = (Spinner) dialog.findViewById(R.id.spinner_reason);
            final Spinner spinner_condition = (Spinner) dialog.findViewById(R.id.spinner_condition);
            Button btn_done = (Button) dialog.findViewById(R.id.btn_done);
            ImageButton imgBtCancel = (ImageButton) dialog.findViewById(R.id.imgBtCancel);


            pref = CommonFun.getPreferences(getApplicationContext());

            String st_rtn_qty = pref.getString("st_qty", "");
            String st_rtn_reason = pref.getString("st_selected_reason", "");
            String st_rtn_resolution = pref.getString("st_selected_resolution", "");
            String st_rtn_condition = pref.getString("st_selected_condition", "");


//        if (st_rtn_qty != null && !st_rtn_qty.equals(""))
//            return_qty.setText(st_rtn_qty);

            st_selected_product_id = arr_product_id[position];
            arrList_selected_product_id.add(st_selected_product_id);


            st_selected_order_item_id = arr_order_item_id[position];
            arrList_selected_order_item_id.add(st_selected_order_item_id);


            arr_reason = new String[5];

            arr_reason[0] = "Out of Service";
            arr_reason[1] = "Don't like";
            arr_reason[2] = "Wrong Color";
            arr_reason[3] = "Wrong Size";
            arr_reason[4] = "Other";

            arr_resolution = new String[3];
            arr_resolution[0] = "Exchange";
            arr_resolution[1] = "Refund";
            arr_resolution[2] = "Store Credit";

            arr_condition = new String[3];
            arr_condition[0] = "Unopened";
            arr_condition[1] = "Opened";
            arr_condition[2] = "Damaged";


            adapter_reason = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_spinner_dropdown_item, arr_reason);
            spinner_reason.setAdapter(adapter_reason);

//        if (st_rtn_reason != null && !st_rtn_reason.equals(""))
//            spinner_reason.setSelection(adapter_reason.getPosition(st_rtn_reason));


            adapter_resolution = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_spinner_dropdown_item, arr_resolution);
            spinner_resolution.setAdapter(adapter_resolution);
//        if (st_rtn_resolution != null && !st_rtn_resolution.equals(""))
//            spinner_resolution.setSelection(adapter_resolution.getPosition(st_rtn_resolution));

            adapter_condition = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_spinner_dropdown_item, arr_condition);
            spinner_condition.setAdapter(adapter_condition);
//        if (st_rtn_condition != null && !st_rtn_condition.equals(""))
//            spinner_condition.setSelection(adapter_condition.getPosition(st_rtn_condition));

            dialog.setCancelable(false);
            dialog.show();

            imgBtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!arr_check_rtn_btn[current_pos].toString().equalsIgnoreCase("notClickalbe")) {
                        arr_check_rtn_btn[current_pos] = "Clickable";
                        setListAdapter();
                    }
                    dialog.dismiss();

                }
            });


            spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0)
                        selected_reason_id = 1;
                    else if (position == 1)
                        selected_reason_id = 2;
                    else if (position == 2)
                        selected_reason_id = 3;
                    else if (position == 3)
                        selected_reason_id = 4;
                    else if (position == 4)
                        selected_reason_id = 5;

                    arrList_selected_reason.add(String.valueOf(selected_reason_id));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spinner_resolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                        String selected_reason = holder.arr_reason[position];
                    if (position == 0)
                        selected_resolution_id = 1;
                    else if (position == 1)
                        selected_resolution_id = 2;
                    else if (position == 2)
                        selected_resolution_id = 3;

                    arrList_selected_resolution.add(String.valueOf(selected_resolution_id));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spinner_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0)
                        selected_condition_id = 1;
                    else if (position == 1)
                        selected_condition_id = 2;
                    else if (position == 2)
                        selected_condition_id = 3;

                    arrList_selected_condition.add(String.valueOf(selected_condition_id));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pref = CommonFun.getPreferences(getApplicationContext());

                    st_qty = return_qty.getText().toString();
                    st_selected_reason = spinner_reason.getSelectedItem().toString();
                    st_selected_resolution = spinner_resolution.getSelectedItem().toString();
                    st_selected_condition = spinner_condition.getSelectedItem().toString();

                    //////Log.d("st_qty", st_qty);
                    //////Log.d("req_qty", req_qty + "");

                    String st_ordered_qty = arr_product_qty[current_pos];
                    String[] st_qty_value = st_ordered_qty.split("\\.");

                    int ordered_qty = Integer.parseInt(st_qty_value[0]);

                    if (!st_qty.equalsIgnoreCase("")) {
                        req_qty = Integer.parseInt(st_qty);

                        if (req_qty > ordered_qty || req_qty <= 0) {
                            CommonFun.alertError(OrderDetails.this, "Request for return quantity should be less then Ordered Quantity");
                        } else {

                            if (!arr_edit_rtn_qty[current_pos].toString().equalsIgnoreCase("true")) {
                                arr_edit_rtn_qty[current_pos] = "true";
                            }

                            arr_enter_qty[current_pos] = st_qty;
                            arrList_entered_qty.add(st_qty);
                            arr_check_rtn_btn[current_pos] = "notClickalbe";
                            arrList_selected_sku.add(selected_sku);
                            setListAdapter();

                            arr_req_rtn_qty[current_pos] = st_qty;
                            //////Log.d("current_qty",arr_req_rtn_qty[current_pos].toString());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("st_qty", arr_req_rtn_qty[current_pos].toString());
                            editor.putString("st_selected_reason", st_selected_reason);
                            editor.putString("st_selected_resolution", st_selected_resolution);
                            editor.putString("st_selected_condition", st_selected_condition);
                            editor.commit();

                            dialog.dismiss();
                        }

                    } else {
                        CommonFun.alertError(OrderDetails.this, "Must fill return requested product quantity");
                    }


                }
            });
        }
        else if(arr_return_index[position].equalsIgnoreCase("false")){
            arr_check_rtn_btn[current_pos] = "Clickalbe";

        }

    }


    private void returnRequest() {

        return_request_url = Global_Settings.api_url+"glaze/rmarequest.php";

        pDialog = new TransparentProgressDialog(OrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, return_request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //////Log.d("VOLLEY12345", response);
                            try {

                                //CommonFun.alertError(Payment_Method_Activity.this,response);
                                JSONObject jsonObject=new JSONObject(response);
                                String st_msg=jsonObject.getString("message");
                                String st_status=jsonObject.getString("status");

                                if(st_status.equalsIgnoreCase("0")) {  // status = 0 (Successful)

                                    Vibrator vibrator = (Vibrator) OrderDetails.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                                    vibrator.vibrate(100);

                                    final Dialog dialog = new Dialog(OrderDetails.this);
                                    dialog.setContentView(R.layout.custom_alert_dialog_design);
                                    TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
                                    ImageView image_view_dialog = (ImageView) dialog.findViewById(R.id.image_view_dialog);
                                    tv_dialog.setText(st_msg.toString());
                                    dialog.show();


                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                        Intent intent=new Intent(OrderDetails.this, OrderListActivity.class);
                                        startActivity(intent);
                                        CommonFun.finishscreen(OrderDetails.this);
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                        }
                                    };


                                    Timer timer = new Timer();
                                    timer.schedule(timerTask, 4500);

                                }

                                else{
                                    CommonFun.alertError(OrderDetails.this,st_msg);
                                  }



                            } catch (Exception e) {
                                //e.printStackTrace();
                                CommonFun.alertError(OrderDetails.this,e.toString());
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////Log.d("VOLLEY", error.toString());
                    //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                    CommonFun.showVolleyException(error,OrderDetails.this);
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
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return input_data == null ? null : input_data.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", input_data, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();


                    headers.put("Content-Type","application/json");
                    return headers;
                }

            };

            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    0,
                    0);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //////Log.d("error...","Error");
        }


    }

    private void setListAdapter() {

                  /*
				 *  Set value on List
				 */
        ListAdapter listAdapter = new CustomAdapter(OrderDetails.this, arrayList, R.layout.order_details_item_old_march21,
                new String[]{TAG_name,TAG_image,TAG_created_at,TAG_total_qty_ordered,TAG_original_price,TAG_enter_qty_rtn,TAG_boolean},
                new int[]{R.id.product_name_id,
                        R.id.img_view_ordered_product,
                        R.id.order_date_time,
                        R.id.total_product_qty,
                        R.id.total_product_amt,
                        R.id.return_qty_req,
                        R.id.check_for_return_req});


        if(listAdapter.getCount() >0 ) {
            lv_order_details.invalidate();
            lv_order_details.setAdapter(listAdapter);
            tv_order_id.setText("Order # " + st_order_id);
            //tv_cancel_order.setVisibility(View.VISIBLE);
            tv_title.setText("Your Order");
        }
        else
        {
            tv_title.setText("You have not placed any order yet.");
        }


    }

    private void cancelOrder_SalesAPI() {

        //////Log.d("salecancel",st_sales_url);

        pDialog = new TransparentProgressDialog(OrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            jsonObjRequest = new JsonObjectRequest(Request.Method.GET, st_sales_url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            //////Log.d("jsonObject",jsonObject+"");

                            String st_Order_Cancel_Status = jsonObject.getString("SaveStatus");
                            //////Log.d("st_Order_Cancel_Status",st_Order_Cancel_Status);


                            if(st_Order_Cancel_Status.equalsIgnoreCase("0")){
                                cancelOrder();
                            }
                            else {
                             Intent intent=new Intent(OrderDetails.this, ExceptionError.class);
                             startActivity(intent);
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

                    CommonFun.showVolleyException(error,OrderDetails.this);

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsonObjRequest);

    }

    private void cancelOrder() {

        //////Log.d("st_Cancel_Order_URL",st_Cancel_Order_URL);

        pDialog = new TransparentProgressDialog(OrderDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            jsonObjRequest = new JsonObjectRequest(Request.Method.GET, st_Cancel_Order_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));


                           // ////Log.d("jsonObject",jsonObject+"");

                            String st_Order_Cancel_Status = jsonObject.getString("orderdetails");
                            //////Log.d("st_Order_Cancel_Status",st_Order_Cancel_Status);

                            //CommonFun.alertError(OrderDetails.this,st_Order_Cancel_Status.toString());
                            final AlertDialog.Builder b;
                            try
                            {
//                                b = new AlertDialog.Builder(OrderDetails.this);
//                                b.setTitle("Alert");
//                                b.setCancelable(false);
//                                b.setMessage(st_Order_Cancel_Status.toString());
//                                b.setPositiveButton("OK", new DialogInterface.OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int whichButton)
//                                    {
//                                        b.create().dismiss();
//                                        CommonFun.finishscreen(OrderDetails.this);
//                                    }
//                                });
//                                b.create().show();


                                Vibrator vibrator = (Vibrator) OrderDetails.this.getSystemService(OrderDetails.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(OrderDetails.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
                                ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
                                tv_dialog.setText(st_Order_Cancel_Status.toString());
                                dialog.show();


                                TimerTask timerTask=new TimerTask() {
                                    @Override
                                    public void run() {


                                        Intent intent=new Intent(OrderDetails.this, OrderListActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonFun.finishscreen(OrderDetails.this);


                                    }};


                                Timer timer=new Timer();
                                timer.schedule(timerTask,4500);

                            }
                            catch(Exception ex)
                            {
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

                    CommonFun.showVolleyException(error,OrderDetails.this);

//                    CommonFun.alertError(OrderDetails.this,error.toString());
//
//                    error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        queue.add(jsonObjRequest);

    }

    private void callOrderDetails() {

        arrayList.clear();
        pDialog = new TransparentProgressDialog(OrderDetails.this);
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
                   //Log.d("responseDetails",response.toString());

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            array_order_list = jsonObject.getJSONArray("orderdetails");
                            int order_list_length = array_order_list.length();

                            arr_product_id = new String[order_list_length];
                            arr_product_name = new String[order_list_length];
                            arr_product_qty = new String[order_list_length];
                            arr_boolean = new String[order_list_length];
                            arr_product_amt= new String[order_list_length];
                            arr_product_order_date = new String[order_list_length];
                            arr_product_img = new String[order_list_length];
                            arr_edit_rtn_qty = new String[order_list_length];
                            arr_rtn_qty = new String[order_list_length];
                            arr_enter_qty=new String[order_list_length];
                            arr_order_item_id = new String[order_list_length];
                            arr_check_rtn_btn = new String[order_list_length];
                            arr_req_rtn_qty = new String[order_list_length];
                            arr_return_index = new String[order_list_length];
                            arr_tracking_id = new String[order_list_length];
                            arr_shipment_type = new String[order_list_length];
                            arr_selected_sku = new String[order_list_length];
                            arr_sku = new String [order_list_length];
                            arr_track_order= new String[order_list_length];


                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = array_order_list.getJSONObject(i);

                                st_product_image = order_list_obj.getString(TAG_image);
                                st_product_id = order_list_obj.getString(TAG_product_id);
                                st_product_order_date = order_list_obj.getString(TAG_created_at);
                                st_total_qty_ordered = order_list_obj.getString(TAG_total_qty_ordered);
                                st_product_amt = order_list_obj.getString(TAG_original_price);
                                st_product_name = order_list_obj.getString(TAG_name);
                                st_trigger_no_sale = order_list_obj.getString(TAG_trigger_no_sale);
                                st_shipmentType = order_list_obj.getString(TAG_shipmentType);
                                st_order_item_id = order_list_obj.getString("order_item_id");
                                st_product_sku = order_list_obj.getString(TAG_sku);

                                //////Log.d("st_trigger_no_sale",st_trigger_no_sale);

                                if(st_trigger_no_sale.equalsIgnoreCase("")) {
                                    st_trigger_no_sale = "0";
                                 //   ////Log.d("st_trigger_no_sale", st_trigger_no_sale);
                                }
                                else {
                                    st_trigger_no_sale = st_trigger_no_sale;
                                 //   ////Log.d("st_trigger_no_sale", st_trigger_no_sale);
                                }
                                arr_product_name[i] = st_product_name;
                                arr_product_id[i]=st_product_id;
                                arr_product_qty[i]=st_total_qty_ordered;
                                arr_product_order_date[i] = st_product_order_date;
                                arr_product_img[i] = st_product_image;
                                arr_product_amt[i] = st_product_amt;
                                arr_boolean[i] = "false";
                                arr_edit_rtn_qty[i] = "false";
                                arr_rtn_qty[i] = "false";
                                arr_enter_qty[i]="-1";
                                arr_check_rtn_btn[i]="isClickable";
                                arr_order_item_id[i] = st_order_item_id;
                                arr_return_index[i] = "false";
                                arr_tracking_id[i] = st_trigger_no_sale;
                                arr_shipment_type[i] = st_shipmentType;
                                arr_sku[i]= st_product_sku;


                                if(st_order_status.equalsIgnoreCase("canceled")){
                                    arr_track_order[i] = "false";
                                }
                                else{
                                    arr_track_order[i] = "true";
                                }

                                hashMap = new HashMap<String, String>();

                                hashMap.put(TAG_name,st_product_name  +" - "+st_product_id);
                                hashMap.put(TAG_image,st_product_image);
                                hashMap.put(TAG_created_at, st_product_order_date);
                                hashMap.put(TAG_total_qty_ordered,st_total_qty_ordered);
                                hashMap.put(TAG_original_price, "Amount: ₹ "+st_product_amt);
                                hashMap.put(TAG_boolean,arr_boolean[i].toString());
                                hashMap.put(TAG_rtn_check,arr_return_index[i].toString());
                                hashMap.put(TAG_edit_rtn,arr_edit_rtn_qty[i].toString());
                                hashMap.put(TAG_qty_rtn,arr_rtn_qty[i].toString());
                                hashMap.put(TAG_enter_qty_rtn,arr_enter_qty[i].toString());
                                hashMap.put(TAG_isClickable,arr_check_rtn_btn[i].toString());
                                hashMap.put(TAG_trigger_no_sale,arr_tracking_id[i].toString());
                                hashMap.put(TAG_sku,arr_sku[i].toString());
                                hashMap.put(TAG_TRACK_ORDER,arr_track_order[i].toString());


                                arrayList.add(hashMap);
                               // ////Log.d("itemList",arrayList+"");
                            }
                           // ////Log.d("itemListafter",arrayList+"");
                            setListAdapter();
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

        queue.add(jsObjRequest);

    }

  //************************************************************************************************************************************


    public class CustomAdapter extends SimpleAdapter {

        final String TAG_image= "image";
        final String TAG_product_id = "product_id";
        final String TAG_name= "name";
        final String TAG_total_qty_ordered= "qty_ordered";
        final String TAG_original_price = "original_price";
        final String TAG_created_at= "created_at";
        final String TAG_boolean="Return_check";
        final String TAG_edit_rtn = "Edit_Qty";
        final String TAG_enter_qty_rtn = "enter_qty";

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
        public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);

            this.ctx = context;
            this.arrayList = (ArrayList<HashMap<String, String>>) data;

        }


        class Holder{

            ImageView img_view_ordered_product;
            TextView product_name_id;
            TextView total_product_amt;
            TextView order_date_time;
            TextView total_product_qty,return_qty_req;
            Button bt_edit_qty_icon,bt_track_order;
            CheckBox check_for_return_req;

        }


        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

//    @Override
//    public int getCount() {
//        return arrayList.size();
//    }

//    @Override
//    public Object getItem(int position) {
//        return arrayList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final int cur_position= position;

            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.order_details_item_old_march21, null);
                holder = new Holder();

                holder.img_view_ordered_product = (ImageView)convertView.findViewById(R.id.img_view_ordered_product);

                holder.product_name_id = (TextView) convertView.findViewById(R.id.product_name_id);
                holder.total_product_amt = (TextView) convertView.findViewById(R.id.total_product_amt);
                holder.order_date_time = (TextView) convertView.findViewById(R.id.order_date_time);
                holder.total_product_qty = (TextView) convertView.findViewById(R.id.total_product_qty);
                holder.check_for_return_req = (CheckBox)convertView.findViewById(R.id.check_for_return_req);
                holder.return_qty_req = (TextView)convertView.findViewById(R.id.return_qty_req);
                holder.bt_track_order = (Button) convertView.findViewById(R.id.bt_track_order);
                holder.bt_edit_qty_icon = (Button)convertView.findViewById(R.id.bt_edit_qty_icon);
                convertView.setTag(holder);

            }
            else {

                holder = (Holder) convertView.getTag();
            }
            holder.product_name_id.setText(arrayList.get(position).get(TAG_name).toString());
            holder.total_product_amt.setText(arrayList.get(position).get(TAG_original_price).toString());
            holder.order_date_time.setText(arrayList.get(position).get(TAG_created_at).toString());
            holder.total_product_qty.setText("Total Quantity:  " +arrayList.get(position).get(TAG_total_qty_ordered).toString());

//            String st_boolean = arrayList.get(position).get(TAG_boolean).toString();
            String st_boolean = arr_boolean[position].toString();
            String st_edit_rtn = arr_edit_rtn_qty[position].toString();
            String st_track_order_option=arr_track_order[position].toString();
            //////Log.d("st_track_order_option",st_track_order_option);

            if(st_track_order_option.equalsIgnoreCase("true"))
                holder.bt_track_order.setVisibility(View.VISIBLE);
            else if(st_track_order_option.equalsIgnoreCase("false"))
                holder.bt_track_order.setVisibility(View.GONE);

            String st_isClickable = arr_check_rtn_btn[position].toString();
           // ////Log.d("st_boolean",st_boolean);

            if(st_isClickable.equalsIgnoreCase("notClickalbe")){

                holder.check_for_return_req.setChecked(true);
//                holder.check_for_return_req.setEnabled(false);
                holder.bt_edit_qty_icon.setVisibility(View.VISIBLE);
                holder.return_qty_req.setVisibility(View.VISIBLE);
                holder.return_qty_req.setText("Return Qty :"+arr_enter_qty[position].toString());
            }
            else{
                holder.check_for_return_req.setChecked(false);
                holder.bt_edit_qty_icon.setVisibility(View.GONE);
                holder.return_qty_req.setVisibility(View.GONE);

//                holder.check_for_return_req.setEnabled(true);
            }


            if(st_boolean.equalsIgnoreCase("true")) {
                holder.check_for_return_req.setVisibility(View.VISIBLE);
            }
            else {
                holder.check_for_return_req.setVisibility(View.GONE);
            }

//            if(st_edit_rtn.equalsIgnoreCase("true")) {
//                holder.bt_edit_qty_icon.setVisibility(View.VISIBLE);
//            }
//            else {
//                holder.bt_edit_qty_icon.setVisibility(View.GONE);
//            }
//
//            if(arr_enter_qty[position].toString().equals("-1"))
//            {
//                holder.return_qty_req.setVisibility(View.GONE);
//
//            }
//            else
//            {
//                holder.return_qty_req.setVisibility(View.VISIBLE);
//                holder.return_qty_req.setText("Return Qty :"+arr_enter_qty[position].toString());
//
//            }

//            if(st_rtn_qty.equalsIgnoreCase("true")) {
//                holder.return_qty_req.setVisibility(View.VISIBLE);
//                holder.return_qty_req.setText("Return Qty :"+st_qty);
//
//            }
//            else {
//                holder.return_qty_req.setVisibility(View.GONE);
//            }

            holder.check_for_return_req.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String st_selected_sku = arrayList.get(position).get(TAG_sku).toString();

                    requestForReturn(position,st_selected_sku);


                }
            });

            holder.bt_edit_qty_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editRequestForReturn(position);


                }
            });
            holder.bt_track_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    trackYourOrder(position);


                }
            });



            Picasso.with(ctx)
                    .load(arrayList.get(position).get(TAG_image).toString())
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    .resize(150, 150)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.img_view_ordered_product);


            return convertView;
        }
    }





}
