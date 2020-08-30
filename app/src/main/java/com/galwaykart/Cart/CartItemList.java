package com.galwaykart.Cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.CustomerAddressBook;
import com.galwaykart.BaseActivity;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Show Cart Item List to the user
 * User can update or delete cart items
 *
 * it will show recent items to the user
 * Created by ankesh on 9/13/2017.
 */

public class CartItemList extends BaseActivity {

    SharedPreferences pref;
    String url_cart_item_list = "";
    String tokenData;
    int cart_qty;
    Double total_amt=0.0;
    Double total_offer_amount=0.0;

    final String TAG_item_id = "item_id";
    final String TAG_sku = "sku";
    final String TAG_qty = "qty";
    final String TAG_name = "name";
    final String TAG_price = "price";
    final String TAG_quote_id = "quote_id";

    final String TAG_total_item_count = "items_count";
    final String TAG_total_items_qty = "items_qty";
    TextView tv_alert;
    int total_cart_qty;
    int total_cart_count;
    String item_quote_id="";
    ListAdapter lstadapter;
    ListAdapter lst_qty_adapter;
    int initial_cart_qty;
    int final_cart_qty;
    TransparentProgressDialog pDialog;
    StringRequest stringRequest;
    boolean delete_successfully = false;
    String add_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/";
    String update_to_cart_URL = "", st_get_ip_URL="",st_ip="",
            cart_item_sku = "", cart_item_quote_id = "",
            delete_to_cart_URL = "", st_qty_api = "",
            cart_item_id = "";

    String [] arr_sku_off,product_ip;
    String [] arr_qty_off,arr_thrshold_amt;
    String[] arr_sku, arr_qty, arr_quote_id, arr_name, arr_price, arr_initial_qty, arr_final_qty, arr_boolean, arr_qty_api, arr_item_id,
            arr_updated_cart_qty,arr_stock_qty;

    String[] arr_out_stock_msg;
    Boolean[] arr_in_stock;
    String[] arr_available_stock;
    Boolean[] arr_show_qty;


    Boolean is_deleted_called = false;
    int current_qty, current_index;
    Button btCheckout;
    String cart_imagepath = "";
    TextView tv_alert_continue;
    Boolean found_image_path_blank;
    ProgressBar progress_bar;
    DatabaseHandler dbh;
    TextView tv_txt_view, tv_discount;
    String total_grand_amount = "0",total_ip = "0";
    Double total_ip_f;
    Boolean data_load = false;
    String subtotal_inc_tax = "";
    String disc_amount = "";
    String base_grand_total = "";
    String base_subtotal = "";
    RecyclerView list_cart_item;
    private List<DataModelCart_v1> listof_cart_item;


    RecyclerView recyclerView_RecentItem;
    private List<DataModelRecentItem> list_recent_item;
    Boolean recent_view_item_load=false;
    TextView tv_recent_view_item,tv_total_ip;

    Boolean user_details_already_fetch;
    String st_sales_user_zone="";
    String st_magento_user_zone="";

    String[] arr_offer_cart_item,arr_non_offer_sku,arr_offer_item_price,arr_offer_item_qty;
    String st_offer_api_URL = Global_Settings.api_custom_url+"offerData.php";
    ArrayList<String> offered_cart_item;


    String st_sku_off1="",st_thresshold_price_off1="",st_qty_off1="",
            st_max_price="",
            st_thresshold_price_off2="",
            st_offer_added_sku="",st_token_data="";
    int arr_catalog_length;
    Boolean update_cart_qty = false;
    String [] arr_offered_sku;
    String [] arr_boolean_edit;
    String [] arr_max_price;

    TableRow tbl_row1,tbl_row2,tbl_row3;
    TextView btn_apply_coupon,btn_apply_code,txt_coupon,btn_remove_coupon;
    EditText ed_coupon;
    String st_apply_coupon_URL = "",st_remove_coupon_URL="";
    Boolean coupon_applied_successfully,coupon_removed_successfully,is_edit_cart=false;
    Button bt_change_cart;
    String login_group_id="";
    int total_cart_qty_new=0;
    String st_stockcheck_url=Global_Settings.api_url+"rest/V1/m-carts/mine/stockcheck";
    int total_subtotal_qty=0;
    ImageView img_empty_cart;
    Boolean is_data_load=false;
    Spinner spinner_qty;
    CartItemAdapter_v1 adapter;
    String is_error="";
    Boolean is_voucher_applied=false;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Intent intent = new Intent(CartItemList.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        initNavigationDrawer();
        pref = CommonFun.getPreferences(getApplicationContext());
        st_token_data = pref.getString("tokenData","");
        st_offer_added_sku = pref.getString("st_offer_added_sku","");
        is_deleted_called = false;



        offered_cart_item = new ArrayList<>();

        found_image_path_blank = false;
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        tv_txt_view = findViewById(R.id.tv_txt_view);
        tv_discount = findViewById(R.id.tv_discount);
        tv_discount.setVisibility(View.GONE);
        tv_total_ip= findViewById(R.id.tv_total_ip);

        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
        login_group_id=pref.getString("login_group_id","");
        if(!login_group_id.equals("4")) {
           tv_total_ip.setVisibility(View.GONE);
        }

        img_empty_cart= findViewById(R.id.img_empty_cart);
        st_offer_api_URL=st_offer_api_URL+"?customer_type="+login_group_id;


        tbl_row1 = findViewById(R.id.tbl_row1);
        tbl_row2 = findViewById(R.id.tbl_row2);
        tbl_row3 = findViewById(R.id.tbl_row3);
        btn_remove_coupon = findViewById(R.id.btn_remove_coupon);
        btn_apply_coupon = findViewById(R.id.btn_apply_coupon);
        btn_apply_code = findViewById(R.id.btn_apply_code);
        txt_coupon = findViewById(R.id.txt_coupon);
        ed_coupon= findViewById(R.id.ed_coupon);
        bt_change_cart = findViewById(R.id.bt_change_cart);

        list_cart_item = findViewById(R.id.list_cart_item);
        recyclerView_RecentItem= findViewById(R.id.recyclerView_RecentItem);
        recyclerView_RecentItem.setVisibility(View.GONE);
        list_recent_item=new ArrayList<>();
        listof_cart_item=new ArrayList<>();

        tv_recent_view_item= findViewById(R.id.tv_recent_view_item);
        tv_recent_view_item.setVisibility(View.GONE);


    }

    private void getCartId_v1() {

        pref= CommonFun.getPreferences(getApplicationContext());

        String st_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine";
        RequestQueue queue = Volley.newRequestQueue(this);


        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                st_cart_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        callCouponsApplied();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        callCouponsApplied();
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                //   params.put("Content-Type","application/json");

                return params;
            }
        };
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();


        pref = CommonFun.getPreferences(getApplicationContext());
        st_token_data = pref.getString("tokenData","");


        String st_data=pref.getString("st_dist_id","");
        user_details_already_fetch= !st_data.equals("") && (st_data != null);

        String st_data_magento=pref.getString("log_user_zone","");
        user_details_already_fetch= !st_data_magento.equals("") && (st_data_magento != null);

        /**
         * if already fetch user zone from the sales and magento
         * then get the details and store in string
         */

        if(user_details_already_fetch==true){
            st_sales_user_zone=st_data;
            st_magento_user_zone=st_data_magento;
        }

        /**
         * if cart items are not already fetched
         * then call api to get details
         */
        if (data_load == false) {
            /**
             * init controls
             */
            //list_cart_item.setAdapter(null);
            tv_alert = findViewById(R.id.tv_alert);
            tv_alert_continue = findViewById(R.id.tv_alert_continue);
            tv_alert_continue.setPaintFlags(tv_alert_continue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv_alert_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CartItemList.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    CommonFun.finishscreen(CartItemList.this);
                }
            });



            tv_alert.setVisibility(View.GONE);
            tv_alert_continue.setVisibility(View.GONE);

            img_empty_cart.setVisibility(View.GONE);
            list_cart_item.setVisibility(View.VISIBLE);

            tokenData = pref.getString("tokenData", "");
            // tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";

            btCheckout = findViewById(R.id.btCheckout);
            btCheckout.setVisibility(View.GONE);
            btCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //call_checkStock(st_stockcheck_url);

                    Boolean can_process=true;
//                    for(int i=0;i<total_cart_qty;i++){
//                        if(!arr_out_stock_msg.equals(""))
//                            can_process=false;
//                    }

                    if(!is_error.equalsIgnoreCase("false"))
                            can_process=false;

                    if(can_process==true)
                            checkout_fun();
                    else
                    {
                        CommonFun.alertError(CartItemList.this, "Must delete all out of stock items");
                    }
                }
            });

            /*
     Apply Coupon
 */


            btn_apply_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tbl_row1.setVisibility(View.GONE);
                    tbl_row2.setVisibility(View.VISIBLE);

                }
            });

            btn_apply_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String entered_code = ed_coupon.getText().toString().trim();
                    st_apply_coupon_URL = Global_Settings.api_url + "rest/V1/m-carts/mine/coupons/" + entered_code;
                    //Log.d("st_apply_coupon_URL", st_apply_coupon_URL);
                    applyCoupon(st_apply_coupon_URL);

                    /*String entered_code = ed_coupon.getText().toString().trim();
                    if(!entered_code.equals("")) {
                        st_apply_coupon_URL = Global_Settings.api_url + "rest/V1/m-carts/mine/coupons/" + entered_code;
                        Log.d("st_apply_coupon_URL", st_apply_coupon_URL);
                        applyCoupon(st_apply_coupon_URL);
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), "Forgot to enter a code", Snackbar.LENGTH_LONG).show();
                    }*/

                }
            });

            btn_remove_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    is_edit_cart = true;
                    removeCoupon(st_remove_coupon_URL);
                }
            });

            //bt_change_cart.setVisibility(View.GONE);
            bt_change_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    is_edit_cart = true;
                    removeCoupon(st_remove_coupon_URL);
                }
            });

            tbl_row2.setVisibility(View.GONE);

            total_grand_amount = "0";
            url_cart_item_list = Global_Settings.api_url
                    + "rest/V1/m-carts/mine";


            st_remove_coupon_URL = Global_Settings.api_url+"rest/V1/carts/mine/coupons";
            //Log.d("st_remove_coupon_URL",st_remove_coupon_URL);

            //removeCoupon(st_remove_coupon_URL);
            //callCartItemList();


        }


        if(is_data_load==false)
            getCartId_v1();
        else
            btCheckout.setVisibility(View.VISIBLE);
    }

    private void checkout_fun() {

        Boolean canproceed = true;


        DatabaseHandler dbh = new DatabaseHandler(CartItemList.this);
        if (dbh.getCartItemCount() > 0)
            dbh.deleteCartItem();


        SharedPreferences.Editor editor = pref.edit();
        editor.putString("addnew", "");
        editor.putString("cartamount", total_grand_amount);
        editor.commit();

        Log.e("total_grand_amount",total_grand_amount);


        for (int i = 0; i < total_cart_count; i++) {

//                    if(!arr_price[i].equals("0")  && !arr_qty[i].equals("0")) {
//                        Double total_amt= Double.parseDouble(arr_price[i]) * Integer.parseInt(arr_qty[i]);
//                        total_grand_amount =  String.valueOf(Double.parseDouble(total_grand_amount)+ Math.round(total_amt*100.0)/100.0);

            //  dbh.addCartProduct(new CartItem(arr_sku[i],arr_name[i],"",arr_qty[i],arr_price[i]));

            //                 }

            if (Integer.parseInt(arr_qty[i]) == 0 || Integer.parseInt(arr_qty[i])==-1) {
                canproceed = false;
//                            arr_qty[i] = "1";
//                            canproceed = true;
            }
        }

        if(canproceed==true)
        {


            try {
                for (int i = 0; i < arr_sku.length; i++) {

                    if (arr_in_stock[i] == false) {
                        canproceed = false;
                        arr_qty[i] = "0";
                    }

                }
                bindDatainCartList(false);
            }
            catch (NullPointerException ex){

            }

        }



        /**
         * if canproceed true
         * allow user to redirect to
         * Addreess activity
         */
        // canproceed = true;

        if (canproceed == true) {

//            if(login_group_id.equalsIgnoreCase("4")) {
//
//                //Intent intent = new Intent(CartItemList.this, AddressBookClass.class);
//                Intent intent = new Intent(CartItemList.this, CustomerAddressBook.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//            }
//            else
//            {
                Intent intent = new Intent(CartItemList.this, CustomerAddressBook.class);
                intent.putExtra("st_come_from_update","");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            //}
                CommonFun.finishscreen(CartItemList.this);

            //getDistributorDetails();

        } else {
            CommonFun.alertError(CartItemList.this, "Must delete all out of stock items...");
        }
    }

    private void callCouponsApplied(){

        String coupon_check_url=Global_Settings.api_url+"rest/V1/carts/mine/coupons/";

        //Log.d("coupon_url",coupon_check_url);
        tokenData = tokenData.replaceAll("\"", "");
        pDialog = new TransparentProgressDialog(CartItemList.this);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(!isFinishing())
         pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, coupon_check_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("responseafterapplied", response.toString());
                        //CommonFun.alertError(CartItemList.this, response.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        lstadapter=null;



                        try {

                            String st_coupon= response;
                            if(TextUtils.isEmpty(st_coupon) || st_coupon.equalsIgnoreCase("[]"))
                            {
                                callCartItemList();

                            }
                            else
                            {
                                is_voucher_applied=true;
                                callCartItemAmountAfterDiscoutApplied();

                                tbl_row2.setVisibility(View.GONE);
                                tbl_row3.setVisibility(View.VISIBLE);
                                String coupon_name = response.replaceAll("\"", "");
                                txt_coupon.setText(coupon_name+ " Voucher Applied");
                                list_cart_item.setVisibility(View.GONE);
                                //tv_recent_view_item.setVisibility(View.GONE);
                                recyclerView_RecentItem.setVisibility(View.GONE);
                                bt_change_cart.setVisibility(View.VISIBLE);
                                btn_apply_coupon.setVisibility(View.GONE);
                                btCheckout.setVisibility(View.VISIBLE);
                                //total_ip_f = Double.valueOf(0);       // ip in case of coupon applied

                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("cart_ip",""+total_ip_f);
                                editor.commit();

                                //Log.d("tv_total_ip",""+total_ip_f);
                               // tv_total_ip.setText("Total PV / BV : "+ total_ip_f);


                            }


                        } catch (Exception e) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //e.printStackTrace();
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(CartItemList.this, ExceptionError.class);
                            startActivity(intent);

                        }

                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();


//                        if (error instanceof ParseError || error instanceof ServerError) {
//                            int total_cart_count =0;
//
//                        }
//                        else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error,CartItemList.this);
                        //}



                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);
    }
    private void removeCoupon(String st_remove_coupon_url) {

//            pDialog = new TransparentProgressDialog(CartItemList.this);
//            pDialog.setCancelable(false);
//            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            pDialog.show();

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = null;
            try {
                stringRequest = new StringRequest(Request.Method.DELETE, st_remove_coupon_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CommonFun.alertError(CartItemList.this, response.toString());
//                        if (pDialog.isShowing())
//                            pDialog.dismiss();

                        if (response != null) {

                            //CommonFun.alertError(CartItemList.this, response.toString());
                            try {
                                coupon_removed_successfully = Boolean.parseBoolean(response);

                                if(coupon_removed_successfully == true){
                                    tbl_row3.setVisibility(View.GONE);
                                    tbl_row1.setVisibility(View.VISIBLE);
                                    is_voucher_applied=false;
                                    list_cart_item.setVisibility(View.VISIBLE);
                                    //tv_recent_view_item.setVisibility(View.VISIBLE);
                                   // recyclerView_RecentItem.setVisibility(View.VISIBLE);
                                    bt_change_cart.setVisibility(View.GONE);
                                    tv_discount.setVisibility(View.GONE);
                                    //if(is_edit_cart == true) {
                                        Snackbar.make(findViewById(android.R.id.content), "Applied "+ R.string.voucher_string+ " removed.\nPlease apply again", Snackbar.LENGTH_LONG).show();
                                        Intent intent=new Intent(CartItemList.this, CartItemList.class);
                                        startActivity(intent);
                                        CommonFun.finishscreen(CartItemList.this);

                                    //}

                                   // callCartItemList();

                                }
                                else{
                                    Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                //e.printStackTrace();
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                Intent intent=new Intent(CartItemList.this, ExceptionError.class);
                                startActivity(intent);

                                Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();

                            }
                        }
                        else{
                            //Log.d("glog","removecoupon1");
                            CommonFun.alertError(CartItemList.this, response);
                            Intent intent = new Intent(getBaseContext(), ExceptionError.class);
                            startActivity(intent);
                        }


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if (pDialog.isShowing())
//                            pDialog.dismiss();

//                        CommonFun.alertError(CartItemList.this, error.toString());
//                        Intent intent = new Intent(getBaseContext(), ExceptionError.class);
//                        startActivity(intent);

                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            //Log.d("glog", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
//                                String st_code = object.getString("code");
                                //Log.d("glog","removecoupon2");
                                callCartItemList();
                               // CommonFun.alertError(CartItemList.this,st_msg);
//                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, CartItemList.this);
                            }


                        }
                    else
                            CommonFun.showVolleyException(error, CartItemList.this);

                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();

                        headers.put("Authorization", "Bearer " + tokenData);
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    protected String getParamsEncoding() {
                        return "utf-8";
                    }


//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }

                };
            } catch (Exception e) {
                //e.printStackTrace();
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Intent intent = new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
                startActivity(intent);
            }


//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);


    }
    private void applyCoupon(String st_apply_coupon_url) {

        Log.d("CoResponse",st_apply_coupon_url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, st_apply_coupon_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("CoResponse", response);

                        try {

                            coupon_applied_successfully = Boolean.parseBoolean(String.valueOf(response));

                            if (coupon_applied_successfully == true) {

//                                total_subtotal_qty=0;
//                                callCartItemAmountAfterDiscoutApplied();
//
//                                tbl_row2.setVisibility(View.GONE);
//                                tbl_row3.setVisibility(View.VISIBLE);
//                                txt_coupon.setText("Voucher Applied");
//                                is_voucher_applied=true;
//                                list_cart_item.setVisibility(View.GONE);
//                                tv_recent_view_item.setVisibility(View.GONE);
//                                //recyclerView_RecentItem.setVisibility(View.GONE);
//                                bt_change_cart.setVisibility(View.VISIBLE);
//
//                                //total_ip_f = Double.valueOf(0);       // ip in case of coupon applied
//
//                                SharedPreferences.Editor editor=pref.edit();
//                                editor.putString("cart_ip",""+total_ip_f);
//                                editor.commit();
//
//                                //Log.d("tv_total_ip",""+total_ip_f);
//                                tv_total_ip.setText("Total IP : "+ total_ip_f);


                                Intent intent=new Intent(CartItemList.this,CartItemList.class);
                                startActivity(intent);
                                CommonFun.finishscreen(CartItemList.this);
                            }

                        } catch (Exception e) {
                            //e.printStackTrace();
                            if (pDialog.isShowing())
                                pDialog.dismiss();


                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            //Log.d("log_error", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
//                                String st_code = object.getString("code");
                                //Log.d("glog","applycoupon");
                                //Log.d("glog","applycoupon");
                                CommonFun.alertError(CartItemList.this,st_msg);
//                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if(pDialog.isShowing())
                    pDialog.dismiss();
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + tokenData);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };
        putRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(putRequest);


    }


    /**
     * Get all item list from the cart
     *  and fill in the listview
     */
    /**********************start cart callCartItemAmount*****************************/

    private void callCartItemAmountAfterDiscoutApplied() {

        tokenData = tokenData.replaceAll("\"", "");
        pDialog = new TransparentProgressDialog(CartItemList.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(!isFinishing())
            pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, Global_Settings.cart_amount_api, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("responseafterapplied", response.toString());
 //                       CommonFun.alertError(CartItemList.this, response.toString());

                        lstadapter=null;

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            total_ip=jsonObj.getString("ip");
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("cart_ip",""+total_ip);
                            editor.commit();

                            //Log.d("tv_total_ip",""+total_ip);
                            tv_total_ip.setText("Total PV / BV : "+ total_ip);


                            String base_total="";
                            JSONArray jsonArray = jsonObj.getJSONArray("total_segments");
                            for(int j = 0; j<jsonArray.length(); j++)
                            {
                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);
                                String code =  jsonObjFinal.getString("code");
                                if(code.equalsIgnoreCase("subtotal"))
                                {
                                    String title =  jsonObjFinal.getString("title");
                                    String value =  jsonObjFinal.getString("value");
                                    base_total = value;
                                    Log.e("subtotal", base_total);

                                }


                            }
                            tv_txt_view.setText("Cart Subtotal :₹ " + base_total);
                            tv_txt_view.setVisibility(View.VISIBLE);

                            callCartItemList();

//                            base_grand_total=jsonObj.getString("subtotal_with_discount");
//                            base_subtotal=jsonObj.getString("base_subtotal");
//                            disc_amount=jsonObj.getString("discount_amount");
//                            String base_total=jsonObj.getString("base_subtotal");
                          //  tv_txt_view.setText("Cart Subtotal :₹ " + base_grand_total);
                           // tv_discount.setVisibility(View.VISIBLE);
                          ///  tv_discount.setText("Discount Amount :₹ " + disc_amount);

                           // Log.e("disc_amount",disc_amount);
                           // Log.e("base_grand_total",base_grand_total);


//                            try
//                             {
//                                //String st_grand_total=String.valueOf(Float.parseFloat(base_subtotal)-Float.parseFloat(disc_amount));
//                                Log.e("base_subtotal",base_subtotal);
//                                Log.e("disc_amount",disc_amount);
//                              //  Log.e("st_grand_total",st_grand_total);
//                               // tv_txt_view.setText("Cart Subtotal :₹ " +base_grand_total);
//                            }
//                            catch (NumberFormatException ex)
//                            {
//
//                            }
                            //SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                            //Log.e("st_base_grand_total", base_subtotal);


                        } catch (JSONException e) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //e.printStackTrace();
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(CartItemList.this, ExceptionError.class);
                            startActivity(intent);

                        }

                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if (error instanceof ParseError || error instanceof ServerError) {
                            int total_cart_count =0;

                        }
                        else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error,CartItemList.this);
                        }


//                        Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                        startActivity(intent);


                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

    /**********************end cart callCartItemAmount*****************************/

    private void callCartItemList() {

        tokenData = tokenData.replaceAll("\"", "");

        pDialog = new TransparentProgressDialog(CartItemList.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(!isFinishing())
            pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {
                        lstadapter = null;
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        Log.d("responsecartitmelist", String.valueOf(response));


                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));


                            total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));
                            total_cart_qty = Integer.parseInt(jsonObj.getString(TAG_total_items_qty));

                            if(jsonObj.has("ip"))
                            {
                               total_ip=jsonObj.getString("ip");
                               //Log.d("totalip",total_ip);
                            }

                            is_error=jsonObj.getString("is_error");
                            JSONArray custom_data = jsonObj.getJSONArray("cart_items");

                            total_cart_qty_new=custom_data.length();

                            arr_offer_cart_item = new String[total_cart_qty_new];
                            arr_offer_item_price = new String[total_cart_qty_new];
                            arr_offer_item_qty = new String[total_cart_qty_new];

                            /**
                             * initialize all array
                             * as size as cart count
                             */
                            arr_sku = new String[total_cart_qty_new];
                            arr_qty = new String[total_cart_qty_new];
                            arr_quote_id = new String[total_cart_qty_new];
                            arr_name = new String[total_cart_qty_new];
                            arr_price = new String[total_cart_qty_new];
                            arr_initial_qty = new String[total_cart_qty_new];
                            arr_final_qty = new String[total_cart_qty_new];
                            arr_boolean = new String[total_cart_qty_new];
                            arr_boolean_edit = new String[total_cart_qty_new];
                            arr_qty_api = new String[total_cart_qty_new];
                            arr_item_id = new String[total_cart_qty_new];
                            arr_updated_cart_qty = new String[total_cart_qty_new];
                            arr_available_stock = new String[total_cart_qty_new];
                            arr_in_stock=new Boolean[total_cart_qty_new];
                            arr_out_stock_msg=new String[total_cart_qty_new];
                            arr_stock_qty=new String[total_cart_qty_new];
                            arr_show_qty=new Boolean[total_cart_qty_new];
                            product_ip=new String[total_cart_qty_new];


                            /**
                             * cart details
                             */
                            for (int i = 0; i < total_cart_qty_new; i++) {
                                JSONObject c = custom_data.getJSONObject(i);

                                String item_id = c.getString(TAG_item_id);
                                String item_sku = c.getString(TAG_sku);
                                String item_qty = c.getString(TAG_qty);
                                String item_name = c.getString(TAG_name);
                                String item_price = c.getString(TAG_price);
                                item_quote_id= c.getString(TAG_quote_id);
                                String added_string="";

                                if(c.has("product_type")) {

                                    String p_size="";
                                    String p_color="";
                                    String product_type = c.getString("product_type");
                                    if(product_type.equalsIgnoreCase("configurable")){



                                        JSONArray jsonArray_options=c.getJSONArray("options");
                                        for(int k=0;k<jsonArray_options.length();k++){
                                            JSONObject jsonObject=jsonArray_options.getJSONObject(k);
                                            if(jsonObject.has("Size"))
                                                p_size = jsonObject.getString("Size");

                                            else if(jsonObject.has("Color"))
                                                   p_color=jsonObject.getString("Color");

                                        }

                                        added_string="("+p_size+" -"+p_color+")";

                                    }
                                }

                                item_name=item_name+ " "+added_string;
                                arr_sku[i] = item_sku;
                                arr_qty[i] = item_qty;

                                arr_quote_id[i] = item_quote_id;
                                arr_name[i] = item_name;
                                arr_price[i] = item_price;
                                arr_initial_qty[i] = item_qty;
                                arr_boolean[i] = "false";
                                arr_boolean_edit[i] = "true";
                                arr_item_id[i] = item_id;
                                arr_available_stock[i] = "0";

                                arr_in_stock[i]=true;
                                arr_out_stock_msg[i]="";
                                arr_stock_qty[i]="25";
                                arr_show_qty[i]=true;

                                product_ip[i] = c.getString("ip");

                                String st_product_image=c.getString("image");
                                DatabaseHandler dbh = new DatabaseHandler(CartItemList.this);
                                dbh.addCartProductImage(new CartProductImage(item_sku, st_product_image));



                                arr_out_stock_msg[i]=c.getString("error_msg");
                                //if(!arr_out_stock_msg[i].equalsIgnoreCase(""))



                                total_subtotal_qty= total_subtotal_qty+Integer.parseInt(item_qty);
//                                /**
//                                 * Store all cart item details in the local database
//                                 */
//                                DatabaseHandler dbh = new DatabaseHandler(CartItemList.this);
//                                if (dbh.getCartProductImageSKuCount() > 0) {
//                                    List<CartProductImage> contacts = dbh.getCartProductImage(arr_sku[i]);
//
//                                    for (CartProductImage cn : contacts) {
//                                        cart_imagepath = cn.get_image().toString();
//                                    }
//                                }

                                /**
                                 * if total amount is not 0
                                 * then  calculate grand total amount
                                 */
                                total_grand_amount="0";
                                if (!arr_price[i].equals("0") && !arr_qty[i].equals("0")) {
                                    Double total_amt = Double.parseDouble(arr_price[i]) * Integer.parseInt(arr_qty[i]);
                                    total_grand_amount = String.valueOf(Double.parseDouble(total_grand_amount) + Math.round(total_amt * 100.0) / 100.0);
                                }
                                found_image_path_blank = true;
                            }

                    /**
                     * if cart item count is >0
                     *  then fetch user zone
                     */


                       int cart_subtotal=0;
                       if(jsonObj.has("total_segments")) {
                           JSONArray jsonArray_Subtotal = jsonObj.getJSONArray("total_segments");
                           for(int k=0;k<jsonArray_Subtotal.length();k++){
                               JSONObject jsonObject=jsonArray_Subtotal.getJSONObject(k);
                               if(jsonObject.getString("code").equalsIgnoreCase("subtotal")){
                                   cart_subtotal=jsonObject.getInt("value");
                               }
                           }

                           tv_txt_view.setText("Cart Subtotal :₹ " + cart_subtotal +" ("+total_subtotal_qty +(total_subtotal_qty>1?" items)":" item)"));
                           tv_txt_view.setVisibility(View.VISIBLE);

                           if(jsonObj.has("ip")) {
                               tv_total_ip.setText("Total PV / BV : " + jsonObj.getString("ip") );
                               //Log.d("totalipSegment",total_ip);
                           }

                       }
                       else
                       {
                           tv_txt_view.setText("Cart Subtotal :");
                           tv_txt_view.setVisibility(View.VISIBLE);

                       }

                            recently_view_item();




                            if (total_cart_qty_new > 0) {
                                //String user_detail_url= Global_Settings.user_details_url+pref.getString("login_customer_id","");
                                ////Log.d("user_detail_url",user_detail_url);

                                String st_multiple_sku = "";
                                for(int i = 0; i<arr_sku.length;i++) {
                                    if(st_multiple_sku.equalsIgnoreCase("")){
                                        st_multiple_sku = arr_sku[i];
                                    }
                                    else{
                                        st_multiple_sku = st_multiple_sku+","+arr_sku[i];
                                    }

                                }


                                bindDatainCartList(true);

                            } else {
                                tv_alert.setVisibility(View.VISIBLE);
                                tv_alert_continue.setVisibility(View.VISIBLE);

                                img_empty_cart.setVisibility(View.VISIBLE);
                                btn_apply_coupon.setVisibility(View.GONE);
                                list_cart_item.setVisibility(View.GONE);
                                tv_txt_view.setText("Cart Item Subtotal");
                                tv_total_ip.setVisibility(View.GONE);


                            }

                        } catch (JSONException e) {
                            //recently_view_item();
                            //e.printStackTrace();
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(CartItemList.this, ExceptionError.class);
                            startActivity(intent);

                        }
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if (pDialog.isShowing())
                            pDialog.dismiss();


                        if (error instanceof ParseError || error instanceof ServerError) {
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count = 0;
                            //initNavigationDrawer();
                            tv_alert.setVisibility(View.VISIBLE);
                            tv_alert_continue.setVisibility(View.VISIBLE);

                            img_empty_cart.setVisibility(View.VISIBLE);
                            list_cart_item.setVisibility(View.GONE);

                        } else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error, CartItemList.this);
                        }


//                        Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                        startActivity(intent);


                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }
    private void  bindDatainCartList(Boolean updatePrice) {


        is_data_load=true;

        ////Log.d("binddata","start");
        total_grand_amount="0";

        listof_cart_item.clear();
        for(int i=0;i<arr_sku.length;i++){

            /**
             * add all cart item in listview
             */
            listof_cart_item.add(new DataModelCart_v1(arr_qty[i],arr_name[i],arr_price[i],arr_boolean[i],arr_sku[i],product_ip[i],arr_boolean_edit[i],arr_out_stock_msg[i]));

        }


        adapter = new CartItemAdapter_v1(CartItemList.this, listof_cart_item);

        //Log.d("total_cart_count",total_cart_qty_new+"");
        for (int i = 0; i < total_cart_qty_new ; i++) {

            if (!arr_price[i].equals("0") && !arr_qty[i].equals("-1") && !arr_qty[i].equals("0") && !product_ip[i].equals("0") ) {

                Double total_amt = Double.parseDouble(arr_price[i]) * Integer.parseInt(arr_qty[i]);
                total_grand_amount = String.valueOf(Double.parseDouble(total_grand_amount) + Math.round(total_amt * 100.0) / 100.0);

                total_cart_qty = total_cart_qty + Integer.parseInt(arr_qty[i]);
            }


        }



        if (adapter.getItemCount() > 0) {


            final int spanCount = 1; //  columns
            final int spacing = 2; // 50px
            boolean includeEdge = true;

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CartItemList.this, spanCount);
            list_cart_item.setLayoutManager(mLayoutManager);
            list_cart_item.invalidate();

            list_cart_item.setAdapter(adapter);

            tv_alert_continue.setVisibility(View.GONE);

            img_empty_cart.setVisibility(View.GONE);
            btCheckout.setVisibility(View.VISIBLE);
            list_cart_item.setVisibility(View.VISIBLE);


            if(is_voucher_applied==true){
                list_cart_item.setVisibility(View.GONE);
                recyclerView_RecentItem.setVisibility(View.GONE);
            }

            /**
             * if user update any cart quantity
             * then calculate cart item quantitiy and subtotal
             */
            if(updatePrice==true) {


            }


        } else
        {
            list_cart_item.setVisibility(View.GONE);
            list_cart_item.setVisibility(View.GONE);
            tv_alert.setVisibility(View.VISIBLE);
            tv_alert_continue.setVisibility(View.VISIBLE);

            img_empty_cart.setVisibility(View.VISIBLE);
            btCheckout.setVisibility(View.GONE);
            //tv_review_alert.setVisibility(View.VISIBLE);
        }


    }

    public void lessCart(int position) {

        int selindexof = position;

        cart_qty = Integer.parseInt(arr_qty[selindexof]);

        ////Log.d("cart_qty", "" + cart_qty);

        if (cart_qty > 1)
            cart_qty = cart_qty - 1;
        else
            cart_qty = 1;

        arr_qty[selindexof] = String.valueOf(cart_qty);

        current_qty = Integer.parseInt(arr_qty[selindexof]);
        ////Log.d("current_qty", "" + current_qty);

        arr_final_qty[selindexof] = String.valueOf(current_qty);

        initial_cart_qty = Integer.parseInt(arr_initial_qty[selindexof]);
        ////Log.d("initial_cart_qty", "" + initial_cart_qty);

        if (current_qty == initial_cart_qty)
            arr_boolean[selindexof] = "false";
        else
            arr_boolean[selindexof] = "true";


//        lstadapter = new CartItemAdapter(CartItemList.this, arr_sku, arr_qty, arr_name, arr_price, arr_boolean);
//
//        if (lstadapter.getCount() > 0) {
//            list_cart_item.setAdapter(lstadapter);
//        }
        bindDatainCartList(false);
    }
    public void addCart(int position) {


        //View parentRow = (View) v.getParent();
        //final int position = list_cart_item.getPositionForView(parentRow);
        //final int position=list_cart_item.getChildLayoutPosition(parentRow);
        int selindexof = position;


        cart_qty = Integer.parseInt(arr_qty[selindexof]);
        ////Log.d("cart_qty", "" + cart_qty);

        if(cart_qty==-1)
            cart_qty=1;
        else
            cart_qty = cart_qty + 1;
        int st_av_stock=25;
        //arr_available_stock[selindexof] = "25";
//        if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                st_magento_user_zone.equalsIgnoreCase("rkt")) {
//            st_av_stock = Integer.parseInt(arr_available_stock[selindexof]);
//            ////Log.d("available", String.valueOf(st_av_stock));
//        }
//        else
//        {
//            // arr_available_stock[selindexof] = "25";
//            st_av_stock= 25;
//        }

        try {

//            if(cart_qty<=25) {

//                if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                        st_magento_user_zone.equalsIgnoreCase("rkt")) {

                    if (cart_qty <= st_av_stock) {

                        arr_qty[selindexof] = String.valueOf(cart_qty);

                        current_qty = Integer.parseInt(arr_qty[selindexof]);
                        ////Log.d("current_qty", "" + current_qty);

                        arr_final_qty[selindexof] = String.valueOf(current_qty);

                        initial_cart_qty = Integer.parseInt(arr_initial_qty[selindexof]);
                        ////Log.d("initial_cart_qty", "" + initial_cart_qty);

                        if (current_qty == initial_cart_qty)
                            arr_boolean[selindexof] = "false";
                        else
                            arr_boolean[selindexof] = "true";

//                lstadapter = new CartItemAdapter(CartItemList.this, arr_sku, arr_qty, arr_name, arr_price, arr_boolean);
//
//                if (lstadapter.getCount() > 0) {
//                    list_cart_item.setAdapter(lstadapter);
//                }

                        bindDatainCartList(false);
                    }
                    else
                    {
                        CommonFun.alertError(CartItemList.this, "Unable to add\nStock limitation");
                    }

//                }
//                else {
//
//                    arr_qty[selindexof] = String.valueOf(cart_qty);
//
//                    current_qty = Integer.parseInt(arr_qty[selindexof]);
//                    ////Log.d("current_qty", "" + current_qty);
//
//                    arr_final_qty[selindexof] = String.valueOf(current_qty);
//
//                    initial_cart_qty = Integer.parseInt(arr_initial_qty[selindexof]);
//                    ////Log.d("initial_cart_qty", "" + initial_cart_qty);
//
//                    if (current_qty == initial_cart_qty)
//                        arr_boolean[selindexof] = "false";
//                    else
//                        arr_boolean[selindexof] = "true";
//
////                lstadapter = new CartItemAdapter(CartItemList.this, arr_sku, arr_qty, arr_name, arr_price, arr_boolean);
////
////                if (lstadapter.getCount() > 0) {
////                    list_cart_item.setAdapter(lstadapter);
////                }
//
//                    bindDatainCartList(false);
//
//
//                }
//            }
//            else
//            {
//                CommonFun.alertError(CartItemList.this, "Unable to add more\nStock limitation");
//            }
        } catch (Exception ex) {
            //Log.d("glog","addcart");
            CommonFun.alertError(CartItemList.this, ex.toString());
        }

        // callOffline();


    }
    public void updateCart(int position,int new_qty) {

        //View parentRow = (View) v.getParent();
        // final int position = list_cart_item.getPositionForView(parentRow);

        //final  int position=list_cart_item.getChildLayoutPosition(parentRow);
        int selindexof = position;

        cart_item_sku = arr_sku[selindexof];
        ////Log.d("cart_item_sku", "" + cart_item_sku);

        cart_item_quote_id = arr_quote_id[selindexof];
        ////Log.d("cart_item_sku", "" + cart_item_quote_id);

        cart_item_id = arr_item_id[selindexof];


        initial_cart_qty = Integer.parseInt(arr_initial_qty[selindexof]);
        //final_cart_qty = Integer.parseInt(arr_final_qty[selindexof]);

        final_cart_qty=new_qty;
        if(initial_cart_qty!=new_qty){


            current_index = selindexof;
            current_qty = final_cart_qty;

            update_cart_qty = true;

            updateCartItem(cart_item_sku);

        }




    }

    public void openItem(View v) {

        View parentRow = (View) v.getParent();
        // final int position = list_cart_item.getPositionForView(parentRow);
        final  int position=list_cart_item.getChildLayoutPosition(parentRow);
        int selindexof = position;

        String selItemSku = arr_sku[selindexof];
        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("showitemsku", selItemSku);
        editor.commit();

        Intent intent = new Intent(CartItemList.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(CartItemList.this);

    }
    public void deleteItem(int position) {
        int selindexof = position;
        cart_item_id = arr_item_id[selindexof];

        alertForDelete(selindexof);



    }
    private void alertForDelete(int selindexof) {
        String errmsg = "Do you want to Delete product";
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(CartItemList.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    deleteCartItem(selindexof,false,"");

                }
            });
            b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    b.create().dismiss();

                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }
    }
    private void deleteCartItem(final int current_index, final Boolean is_redirect, final String sku_product) {

        delete_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/" + cart_item_id;

        ////Log.d("delete_to_cart_URL",delete_to_cart_URL);

        pDialog = new TransparentProgressDialog(CartItemList.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(!isFinishing())
            pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            stringRequest = new StringRequest(Request.Method.DELETE, delete_to_cart_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i("LOG_VOLLEY", response);
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {

                        //CommonFun.alertError(CartItemList.this,response.toString());

                        try {

                            delete_successfully = Boolean.parseBoolean(response);



                        } catch (Exception e) {
                            //e.printStackTrace();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong.\nPlease try again", Snackbar.LENGTH_LONG).show();

                        }
                    }

                    if(is_redirect==true){
                        SharedPreferences pref;
                        pref =  CommonFun.getPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("showitemsku", sku_product);
                        editor.commit();

                        Intent intent = new Intent(CartItemList.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        CommonFun.finishscreen(CartItemList.this);
                    }
                    else {
                        if (delete_successfully == true)
//                                callCartItemList();
//                                refreshItemCount(CartItemList.this);


                        arr_price[current_index] = "0";
                        arr_qty[current_index]="0";
                        arr_sku[current_index] = "0";
                        data_load = false;
                        is_deleted_called = true;
//                        callCartItemList();
//                        onResume();
                        Intent intent=new Intent(CartItemList.this,CartItemList.class);
                        startActivity(intent);
                        finish();



                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();


                    if (error instanceof ServerError) {
                        //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            //Log.d("log_error", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
//                                String st_code = object.getString("code");

                                //Log.d("glog","updatecartitem");
                                CommonFun.alertError(CartItemList.this,st_msg);
//                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, CartItemList.this);
                            }


                        }

                    } else {


                        if (is_redirect == true) {
                            SharedPreferences pref;
                            pref = CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("showitemsku", sku_product);
                            editor.commit();

                            Intent intent = new Intent(CartItemList.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            CommonFun.finishscreen(CartItemList.this);
                        } else {
                            data_load = false;
                            onResume();
                        }
                    }
//                    Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                    startActivity(intent);

                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }


//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }

            };
        } catch (Exception e) {
            //e.printStackTrace();
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intent = new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
            startActivity(intent);
        }


//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }

    private void updateCartItem(String st_selected_sku) {

        update_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/" + cart_item_id;
        pDialog = new TransparentProgressDialog(CartItemList.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(!isFinishing())
            pDialog.show();



//        final String stxt = "{\"cartItem\":{\"qty\": " + final_cart_qty + ",\"quote_id\": \"" + cart_item_quote_id + "\"}}";
        final String stxt = "{\"cartItem\": {\"sku\": \""+st_selected_sku+"\"," +
                            "\"qty\": "+final_cart_qty+"," +
                            "\"quote_id\": \""+cart_item_quote_id+"\"}}";

        //Log.d("stxt_value_update", stxt);


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.PUT, update_to_cart_URL, new JSONObject(stxt),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            if (pDialog.isShowing())
                                pDialog.dismiss();


                           // CommonFun.alertError(CartItemList.this,response.toString());
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                    st_qty_api = jsonObj.getString("qty");
                                    ////Log.d("st_qty", st_qty_api);

                                    data_load = false;

                                    Intent intent=new Intent(CartItemList.this,CartItemList.class);
                                    startActivity(intent);
                                    CommonFun.finishscreen(CartItemList.this);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Intent intent=new Intent(CartItemList.this,CartItemList.class);
                                    startActivity(intent);
                                    CommonFun.finishscreen(CartItemList.this);
                                }
                            }


                            //asdgasdgsagasg
//                            if (current_qty == Integer.parseInt(st_qty_api)) {
//
//                                arr_boolean[current_index] = "false";
//                                callOffline();
//                            } else
//
//                                callCartItemList();
//
//                            //callCartItemList();
//                            refreshItemCount(CartItemList.this);
                        }

                    },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();


//                    CommonFun.alertError(CartItemList.this,error.toString());
//                    //Log.d("CartItemListerror",error.toString());
                    if (error instanceof ServerError) {
                        //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            //Log.d("log_error", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
//                                String st_code = object.getString("code");

                                //Log.d("glog","updatecartitem");
                                CommonFun.alertError(CartItemList.this,st_msg);

                             adapter.notifyDataSetChanged();
                             list_cart_item.invalidate();



//                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, CartItemList.this);
                            }


                        }

                    } else
                        CommonFun.showVolleyException(error, CartItemList.this);


                }


            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                   // headers.put("Content-Type","application/json");
                    return headers;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    //Log.d("mStatusCode",mStatusCode+"");
                    return super.parseNetworkResponse(response);
                }

            };
        } catch (JSONException e) {
            e.printStackTrace();
        }


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);


    }




    /**
     * Recently View Item
     */
    private void recently_view_item() {


        Realm realm = Realm.getDefaultInstance(); // opens "gkart.realm"
        try {
            RealmResults<DataModelRecentItem> results =
                    realm.where(DataModelRecentItem.class).findAllAsync().sort("p_id");

            //fetching the data
            results.load();

            String string=results.asJSON();
            //Log.d("res_res", string);

            JSONArray jsonArray_result = new JSONArray(string);

            for (int i = 0; i < jsonArray_result.length(); i++) {

                //Log.d("res_res", String.valueOf(jsonArray_result.length()));
                JSONObject jsonObject1 = jsonArray_result.getJSONObject(i);

                String prod_name = jsonObject1.getString("p_name");
                String prod_sku = jsonObject1.getString("p_sku");
                String prod_img = jsonObject1.getString("p_img");
                String prod_price=jsonObject1.getString("p_price");
                int p_id=jsonObject1.getInt("p_id");

                list_recent_item.add(new DataModelRecentItem(prod_name, prod_sku, prod_img,prod_price,p_id));

            }



            recent_view_item_load=true;
            /**
             * set all recent view item into listview
             */

            realm.close();

            setRecentViewAdapter();

        }
        catch (JSONException ex){
            realm.close();
            //Log.d("res_res",ex.toString());
        }
        finally {
            realm.close();
            //Log.d("res_res","complete");
        }

//        ////Log.d("responseo","recentcall");
//        SharedPreferences pref;
//        pref = CommonFun.getPreferences(getApplicationContext());
//
//        String recent_item_url = Global_Settings.api_custom_url + "recently.php?id=" + pref.getString("login_customer_id", "");
//
//        RequestQueue requestQueue=Volley.newRequestQueue(this);
//
//        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
//                recent_item_url,null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                //Log.d("response",response.toString());
//                if(response!=null){
//
//                    JSONObject jsonObject=response;
//                    try {
//                        JSONArray jsonArray_result=jsonObject.getJSONArray("recent");
//
//                        for(int i=0;i<jsonArray_result.length();i++){
//
//                            JSONObject jsonObject1=jsonArray_result.getJSONObject(i);
//
//                            String prod_name=jsonObject1.getString("name");
//                            String prod_sku=jsonObject1.getString("sku");
//                            String prod_img=jsonObject1.getString("image");
//
//                            list_recent_item.add(new DataModelRecentItem(prod_name,prod_sku,prod_img));
//
//
//                        }
//
//                        recent_view_item_load=true;
//                        /**
//                         * set all recent view item into listview
//                         */
//                        setRecentViewAdapter();
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Intent intent=new Intent(CartItemList.this, ExceptionError.class);
//                        startActivity(intent);
//                    }
//
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        jsonObjectRequest.setShouldCache(false);
//        requestQueue.add(jsonObjectRequest);




    }
    private void setRecentViewAdapter(){

        RecentItemAdapter adapter;
        adapter = new RecentItemAdapter(CartItemList.this, list_recent_item);


        if (adapter.getItemCount() > 0) {


            //tv_recent_view_item.setVisibility(View.VISIBLE);
            int spanCount = 1; //  columns
            int spacing = 2; // 50px
            boolean includeEdge = true;
            recyclerView_RecentItem.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setReverseLayout(true);
//            linearLayoutManager.setStackFromEnd(true);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CartItemList.this, spanCount);
            ((GridLayoutManager) mLayoutManager).setReverseLayout(true);
            //((GridLayoutManager) mLayoutManager).setStackFromEnd(true);
            recyclerView_RecentItem.setLayoutManager(mLayoutManager);
            recyclerView_RecentItem.setAdapter(adapter);

            tv_recent_view_item.setVisibility(View.VISIBLE);
            recyclerView_RecentItem.setVisibility(View.VISIBLE);
            //tv_review_alert.setVisibility(View.GONE);
        } else
        {
            tv_recent_view_item.setVisibility(View.GONE);
            recyclerView_RecentItem.setVisibility(View.GONE);

        }

        //loadCartProductImage();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pDialog!=null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }

    public class CartItemAdapter_v1 extends RecyclerView.Adapter<CartItemAdapter_v1.ViewHolder> {


        private List<DataModelCart_v1> mValues;
        Context mContext;
        // protected ItemListener mListener;
        View view;


        public CartItemAdapter_v1(Context context,
                                  List<DataModelCart_v1> values) {

            mContext=context;
            mValues=values;


        }



        @Override
        public int getItemCount() {

            ////Log.d("All size", String.valueOf(mValues.size()));
            return mValues.size();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);

            return new ViewHolder(view);

        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView textView_name;
            TextView textView_itemprice;
            TextView itemqty;
            TextView textView_price,textView_ip,textView_ip_value;
            TextView tv_msg;
            Button tv_update_qty_alert;

            ImageView iv_minus_cart_item;
            ImageView add_item;
            Button btUpdate;
            Button btDelete;
            ImageView imageView_Item;
            Spinner spinner_qty;
            EditText ed_qty;
            int position;
            Boolean is_start=true;
            int totalCount=0;
            ArrayList<String> items3=new ArrayList<>();
            public ViewHolder(View convertView) {

                super(convertView);

                textView_name = convertView.findViewById(R.id.textView_name);
                imageView_Item= convertView.findViewById(R.id.imageView_Item);
                textView_ip_value = convertView.findViewById(R.id.textView_ip_value);
                textView_ip = convertView.findViewById(R.id.textView_ip);
                textView_itemprice = convertView.findViewById(R.id.textView_itemprice);
                itemqty = convertView.findViewById(R.id.itemqty);
                iv_minus_cart_item = convertView.findViewById(R.id.iv_minus_cart_item);
                add_item = convertView.findViewById(R.id.add_item);
                btUpdate = convertView.findViewById(R.id.btUpdate);
                btDelete= convertView.findViewById(R.id.btDelete);
                ed_qty= convertView.findViewById(R.id.ed_qty);
                spinner_qty= convertView.findViewById(R.id.spinner_qty);
                //textView_name.setText(arr_item_name[position]);
                textView_price= convertView.findViewById(R.id.textView_price);
                // textView_price.setVisibility(View.GONE);
                tv_update_qty_alert= convertView.findViewById(R.id.btUpdateQty);
                tv_msg= convertView.findViewById(R.id.tvv_msg);

            }


            @Override
            public void onClick(View view) {

                position=(int)view.getTag();
            }
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final DataModelCart_v1 dataModel = mValues.get(position);

            final String prod_name= dataModel.getArr_item_name();

            final String prod_price= dataModel.getArr_item_price();

            final  String prod_ip = dataModel.getProduct_ip();


            login_group_id=pref.getString("login_group_id","");
            if(login_group_id.equals("4")) {
                holder.textView_ip_value.setText(prod_ip);
            }
            else
            {
                holder.textView_ip_value.setVisibility(View.GONE);
                holder.textView_ip.setVisibility(View.GONE);
            }


            holder.textView_name.setText(prod_name);
            holder.textView_itemprice.setText(dataModel.getArr_item_price());

            /**
             * set value to spinner
             */
            //holder.itemqty.setText(dataModel.getArr_cart_qty());

            holder.tv_msg.setVisibility(View.GONE);


//            String[] items3 = new String[6];
//
            holder.items3.clear();
            int value=1;
//            for(int i=0;i<5;i++){
//                items3[i]= String.valueOf(value);
//                value++;
//            }
//            items3[5]="more";


            for(int i=1;i<6;i++){
                holder.items3.add(String.valueOf(value));
                value++;
            }
            holder.items3.add("more");


//          ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, items3);
            AdapterWithCustomItem adapter3=new AdapterWithCustomItem(mContext,holder);
            holder.spinner_qty.setAdapter(adapter3);

            adapter3.setCustomText(dataModel.getArr_cart_qty());
            holder.spinner_qty.setSelection(adapter3.getPosition(dataModel.getArr_cart_qty()));

            holder.spinner_qty.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                 //   if(holder.spinner_qty.isShown()){
                        holder.is_start=false;
                 //   }
                    return false;
                }

            });

           // ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, holder.items3);
           // holder.spinner_qty.setAdapter(adapter3);

            holder.spinner_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    String selqty=holder.spinner_qty.getSelectedItem().toString();
                    //Log.d("selqty",selqty);

                    holder.totalCount++;
                    holder.is_start= holder.totalCount <= 1;

                        try {
                            if (Integer.parseInt(holder.spinner_qty.getSelectedItem().toString()) < 6) {

                                if(holder.is_start==false)
                                    updateCart(position, Integer.parseInt(holder.spinner_qty.getSelectedItem().toString()));

                            }
                        } catch (NumberFormatException ex) {
                            //Log.d("selqty","NumberFormatException");
                            //Log.d("getData", holder.spinner_qty.getSelectedItem().toString());
                            openAlertQtyList(position,holder, adapter3, dataModel);

                        }catch (NullPointerException ex) {

                            //Log.d("selqty","NullPointerException");

                        //Log.d("exception", ex.toString());
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    //Log.d("selqty","noselected");

                }


            });



            if(!dataModel.getArr_error_message().equals("")){
                holder.tv_msg.setVisibility(View.VISIBLE);

                //  holder.iv_minus_cart_item.setVisibility(View.VISIBLE);
                holder.add_item.setVisibility(View.GONE);
                holder.tv_msg.setText(arr_out_stock_msg[position]);
                //  holder.itemqty.setTextColor(Color.RED);
                //holder.itemqty.getLayoutParams().width = 400;
                holder.tv_update_qty_alert.setVisibility(View.GONE);
//                }
//                else if(arr_show_qty[position]==false)
//                {
//                    holder.tv_update_qty_alert.setVisibility(View.GONE);
//                    holder.tv_msg.setText(arr_out_stock_msg[position]);
//                }
            }
            else if(Integer.parseInt(dataModel.getArr_cart_qty())==-1){
                holder.spinner_qty.setVisibility(View.INVISIBLE);
                holder.tv_update_qty_alert.setText("Update Qty");

                //holder.ed_qty.setVisibility(View.GONE);
                //holder.iv_minus_cart_item.setVisibility(View.INVISIBLE);
                //holder.add_item.setVisibility(View.INVISIBLE);
                //holder.ed_qty.setText("Out Of Stock");

//                holder.iv_minus_cart_item.setVisibility(View.VISIBLE);
//                holder.add_item.setVisibility(View.VISIBLE);
//                holder.itemqty.getLayoutParams().width=96;
//                holder.itemqty.setText("0");
//                holder.itemqty.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//                holder.itemqty.setBackground(ContextCompat.getDrawable(mContext,R.drawable.cart));
                holder.iv_minus_cart_item.setVisibility(View.INVISIBLE);
                holder.itemqty.setText("Stock Limit");

                holder.itemqty.setTextColor(Color.RED);
                //holder.itemqty.getLayoutParams().width=400;
                holder.itemqty.setBackground(ContextCompat.getDrawable(mContext,R.drawable.blank));
                //holder.tv_update_qty_alert.setText("");

                holder.tv_update_qty_alert.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.spinner_qty.setVisibility(View.VISIBLE);
                holder.tv_update_qty_alert.setText("");
                //holder.ed_qty.setVisibility(View.GONE);

                //holder.iv_minus_cart_item.setVisibility(View.VISIBLE);
                //holder.add_item.setVisibility(View.VISIBLE);

                //holder.itemqty.getLayoutParams().width=96;
                //holder.itemqty.setText("1997");
                holder.itemqty.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.itemqty.setBackground(ContextCompat.getDrawable(mContext,R.drawable.cart));
                // holder.tv_update_qty_alert.setText("");
                holder.tv_update_qty_alert.setVisibility(View.GONE);
            }

            holder.tv_update_qty_alert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //openItem(dataModel.getArr_sku().toString());
                    //updateCartQtyTo(dataModel.getArr_sku().toString());
                    int selindexof = position;
                    cart_item_id = arr_item_id[selindexof];


                    deleteCartItem(selindexof,true, dataModel.getArr_sku());
                }
            });

            holder.btUpdate.setVisibility(View.GONE);


            if(prod_price.equalsIgnoreCase("0")) {
                holder.textView_itemprice.setVisibility(View.GONE);
                holder.textView_price.setVisibility(View.GONE);
                holder.iv_minus_cart_item.setVisibility(View.INVISIBLE);
                holder.add_item.setVisibility(View.INVISIBLE);
                holder.textView_ip_value.setVisibility(View.GONE);
                holder.textView_ip.setVisibility(View.GONE);
                //holder.spinner_qty.setVisibility(View.INVISIBLE);
                holder.tv_update_qty_alert.setText("");
                holder.spinner_qty.setEnabled(false);
            }

//            String is_offer_edit = dataModel.getArr_boolean_edit();
//
//            if(is_offer_edit.equalsIgnoreCase("false")) {
//                holder.iv_minus_cart_item.setVisibility(View.INVISIBLE);
//                holder.add_item.setVisibility(View.INVISIBLE);
//                holder.btDelete.setVisibility(View.GONE);
//            }
            String update_cart = dataModel.getArr_boolean();

            if(update_cart.equalsIgnoreCase("true")) {
                holder.btUpdate.setVisibility(View.VISIBLE);
            }
            String sku_prod=dataModel.getArr_sku();
            String imagepath="";
            DatabaseHandler dbh=new DatabaseHandler(mContext);
            if(dbh.getCartProductImageSKuCount()>0) {
                List<CartProductImage> contacts = dbh.getCartProductImage(sku_prod);

                for (CartProductImage cn : contacts) {
                    imagepath = cn.get_image();
                }
            }


            if(!imagepath.equals("")) {
                Picasso.get()
                        .load(imagepath)
                        .placeholder(R.drawable.imageloading)   // optional
                        .error(R.drawable.noimage)      // optional
                        .into(holder.imageView_Item);
            }

            holder.textView_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openItem(dataModel.getArr_sku());
                }
            });

            holder.btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //updateCart(position);
                }
            });

            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });

            holder.iv_minus_cart_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lessCart(position);
                }
            });

            holder.add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addCart(position);
                }
            });
            holder.imageView_Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Toast.makeText(view.getContext(),dataModel.getArr_item_name(),Toast.LENGTH_LONG).show();
                    openItem(dataModel.getArr_sku());

                }
            });

        }

        class AdapterWithCustomItem extends ArrayAdapter<String>
        {
            private final static int POSITION_USER_DEFINED = 0;

            private String mCustomText = "";

            public AdapterWithCustomItem(Context context,ViewHolder holder){
                super(context, android.R.layout.simple_spinner_dropdown_item, holder.items3);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == POSITION_USER_DEFINED) {
                    TextView tv = view.findViewById(android.R.id.text1);
                    tv.setText(mCustomText);
                }

                return view;
            }

            public void setCustomText(String customText) {
                // Call to set the text that must be shown in the spinner for the custom option.
                mCustomText = customText;
                notifyDataSetChanged();
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // No need for this override, actually. It's just to clarify the difference.
                return super.getDropDownView(position, convertView, parent);
            }
        }


        Dialog dialog;
        private void openAlertQtyList(int position,ViewHolder holder, ArrayAdapter<String> adapter3, DataModelCart_v1 dataModel) {
            dialog = new Dialog(CartItemList.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_qty_dialog_design);
            EditText edit_qty=dialog.findViewById(R.id.edit_qty);
            TextView tv_add_to_cart=dialog.findViewById(R.id.tv_add_to_cart);
            tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String st_enter_qty=edit_qty.getText().toString();
                    if(!st_enter_qty.equals("")) {
                        int enter_qty = Integer.parseInt(edit_qty.getText().toString());
                            if (enter_qty != current_qty) {
                                updateCart(position, enter_qty);
                            } else {

                                holder.spinner_qty.setSelection(adapter3.getPosition(dataModel.getArr_cart_qty()));
                                adapter3.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                    }

                }
            });


            TextView tv_cancel_cart=dialog.findViewById(R.id.tv_cancel_cart);
            tv_cancel_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.spinner_qty.setSelection(adapter3.getPosition(dataModel.getArr_cart_qty()));
                    adapter3.notifyDataSetChanged();
                    dialog.dismiss();

                }
            });


            dialog.show();
        }

        private void finishScreen(){

        }


        private void openItem(String selItemSku){
            //String selItemSku = data;
            SharedPreferences pref;
            pref =  CommonFun.getPreferences(mContext);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("showitemsku", selItemSku);
            editor.commit();

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
            //CommonFun.finishscreen(mContext);
        }









    }

}