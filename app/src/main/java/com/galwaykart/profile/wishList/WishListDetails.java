package com.galwaykart.profile.wishList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.annotation.Nullable;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.SplashActivity;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumitsaini on 10/4/2017.
 */

public class WishListDetails extends BaseActivity {

    ListView list_wishlist_item;
    SharedPreferences pref;
    TextView tv_alert_wishlist;
    TransparentProgressDialog pDialog;
    String  wish_list_item_URL = "",st_customer_id="";

    final String TAG_product_id = "product_id";
    final String TAG_added_at = "added_at";
    final String TAG_wishlist_qty = "qty";
    final String TAG_wishlist_item_id = "wishlist_item_id";
    final String TAG_wishlist_id = "wishlist_id";
    final String TAG_store_id = "store_id";
    final String TAG_images = "image";
    final String TAG_wishlist_sku = "sku";
    final String TAG_wishlist_price = "price";
    final String TAG_wishlist_name = "name";
    ListAdapter lstadapter;

    String product_type="";

    ArrayList<String> id_bundal = new ArrayList<String>();
    ArrayList<String> option_id_bundal = new ArrayList<String>();
    ArrayList<String> qty_bundal = new ArrayList<String>();
    int json_len_bundal=0;
    String stBundle= "";
    String stInputDataBundle="";




    String stringRequest = null;
    String st_token_data = "";
    String st_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine";
    SharedPreferences preferences;

    String[] arr_wishlist_sku, arr_wishlist_qty,
            arr_product_id,arr_added_at,arr_wishlist_price,
            arr_wishlist_item_id,arr_wishlist_id,arr_store_id,arr_images,arr_wishlist_name;

    String  delete_to_wishlist_URL = "";

    String st_product_id="",st_added_at="",st_wishlist_qty="",st_wishlist_item_id="",st_msg="",
            st_wishlist_id="",st_store_id="",st_images="",st_wishlist_sku="",st_wishlist_price="",st_wishlist_name="",wishList_product_id="";
    Boolean redirectCart;
    ImageView iv_image_no_details;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, SplashActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_details);
        preferences = CommonFun.getPreferences(getApplicationContext());
        st_token_data = preferences.getString("tokenData","");
    }

    @Override
    protected void onResume() {
        super.onResume();

        initNavigationDrawer();

        redirectCart=false;
        pref = CommonFun.getPreferences(getApplicationContext());

        st_customer_id = pref.getString("st_login_id","");
        ////Log.d("st_customer_id",st_customer_id);

        iv_image_no_details= findViewById(R.id.iv_image_no_details);
        iv_image_no_details.setVisibility(View.GONE);
        /**
         * init controls
         */
        list_wishlist_item = findViewById(R.id.list_wishlist_item);
        tv_alert_wishlist = findViewById(R.id.tv_alert_wishlist);

        tv_alert_wishlist.setVisibility(View.GONE);
        list_wishlist_item.setVisibility(View.VISIBLE);

        //wish_list_item_URL = Global_Settings.api_url+"glaze/wishlist_details.php?id="+st_customer_id;
        ////Log.d("wish_list_item_URL",wish_list_item_URL);

        //callWishListItem();

        wish_list_item_URL=Global_Settings.api_url+"rest/V1/m-carts/mine/addwishlistdetail";
        //Log.d("wishlist_request",wish_list_item_URL);
        callWishListItemNew();

    }

    JSONObject jsonObject_r;

    private void callWishListItemNew(){

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        jsonObject_r=new JSONObject();
        try {
            jsonObject_r.put("customerId", st_customer_id);
        }catch (JSONException ex){

        }


        Volley.newRequestQueue(WishListDetails.this).add(
                new JsonRequest<JSONArray>(Request.Method.POST, wish_list_item_URL, jsonObject_r.toString(),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                //Log.d("responsewishlist", String.valueOf(response));


                                if(pDialog.isShowing())
                                    pDialog.dismiss();

                                lstadapter=null;


                                try {
                                    //JSONObject jsonObj = null;
                                    JSONObject jsonObj=response.getJSONObject(0);

                                    int status=jsonObj.getInt("status");
                                    if(status==1) {
                                        JSONArray custom_data = jsonObj.getJSONArray("item");
                                        int total_wish_list_count = custom_data.length();

                                        arr_wishlist_id = new String[total_wish_list_count];
                                        arr_wishlist_item_id = new String[total_wish_list_count];
                                        arr_wishlist_price = new String[total_wish_list_count];
                                        arr_wishlist_qty = new String[total_wish_list_count];
                                        arr_wishlist_sku = new String[total_wish_list_count];
                                        arr_product_id = new String[total_wish_list_count];
                                        arr_added_at = new String[total_wish_list_count];
                                        arr_images = new String[total_wish_list_count];
                                        arr_store_id = new String[total_wish_list_count];
                                        arr_wishlist_name = new String[total_wish_list_count];


                                        for (int i = 0; i < total_wish_list_count; i++) {
                                            JSONObject c = custom_data.getJSONObject(i);

                                            st_added_at = c.getString(TAG_added_at);
                                            st_images = c.getString(TAG_images);
                                            st_product_id = c.getString(TAG_product_id);
                                            st_store_id = c.getString(TAG_store_id);
                                            st_wishlist_id = c.getString(TAG_wishlist_id);
                                            st_wishlist_item_id = c.getString(TAG_wishlist_item_id);
                                            st_wishlist_price = c.getString(TAG_wishlist_price);
                                            st_wishlist_qty = c.getString(TAG_wishlist_qty);
                                            st_wishlist_sku = c.getString(TAG_wishlist_sku);
                                            st_wishlist_name = c.getString(TAG_wishlist_name);



                                            if(st_wishlist_price !=null && !st_wishlist_price.equals("")){
                                                Double price_double= Double.parseDouble(st_wishlist_price);
                                                DecimalFormat d=new DecimalFormat("##.00");

                                                st_wishlist_price= String.valueOf(d.format(price_double));
                                            }


                                            arr_added_at[i] = st_added_at;
                                            arr_images[i] = st_images;
                                            arr_product_id[i] = st_product_id;
                                            arr_store_id[i] = st_store_id;
                                            arr_wishlist_id[i] = st_wishlist_id;
                                            arr_wishlist_item_id[i] = st_wishlist_item_id;
                                            arr_wishlist_price[i] = st_wishlist_price;
                                            arr_wishlist_qty[i] = st_wishlist_qty;
                                            arr_wishlist_sku[i] = st_wishlist_sku;
                                            arr_wishlist_name[i] = st_wishlist_name;

                                        }

                                        lstadapter = new WishListItemAdapter(WishListDetails.this, arr_images,
                                                arr_wishlist_name, arr_wishlist_price,arr_wishlist_sku);

                                        //Log.d("wishcount", String.valueOf(lstadapter.getCount()));

                                        if (lstadapter.getCount() > 0) {
                                            list_wishlist_item.setAdapter(lstadapter);
                                            list_wishlist_item.setVisibility(View.VISIBLE);
                                            list_wishlist_item.invalidate();
                                            iv_image_no_details.setVisibility(View.GONE);
                                        } else {

                                            list_wishlist_item.setVisibility(View.GONE);
                                            tv_alert_wishlist.setVisibility(View.VISIBLE);
                                            iv_image_no_details.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else
                                    {
                                        iv_image_no_details.setVisibility(View.VISIBLE);
                                        tv_alert_wishlist.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    if(pDialog.isShowing())
                                        pDialog.dismiss();
                                    //e.printStackTrace();
                                    Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();


                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        //Log.d("response", String.valueOf(error));
                    }
                }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("customerId", "104911");
//                        return params;
//                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                });

    }


    private void callWishListItem() {

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, wish_list_item_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        lstadapter=null;


                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            JSONArray custom_data = jsonObj.getJSONArray("wishlistitems");
                            int total_wish_list_count = custom_data.length();

                            arr_wishlist_id = new String[total_wish_list_count];
                            arr_wishlist_item_id = new String[total_wish_list_count];
                            arr_wishlist_price= new String[total_wish_list_count];
                            arr_wishlist_qty = new String[total_wish_list_count];
                            arr_wishlist_sku = new String[total_wish_list_count];
                            arr_product_id = new String[total_wish_list_count];
                            arr_added_at= new String[total_wish_list_count];
                            arr_images = new String[total_wish_list_count];
                            arr_store_id = new String[total_wish_list_count];
                            arr_wishlist_name = new String[total_wish_list_count];


                            for (int i = 0; i < total_wish_list_count; i++) {
                                JSONObject c = custom_data.getJSONObject(i);

                                st_added_at= c.getString(TAG_added_at);
                                st_images = c.getString(TAG_images);
                                st_product_id = c.getString(TAG_product_id);
                                st_store_id = c.getString(TAG_store_id);
                                st_wishlist_id = c.getString(TAG_wishlist_id);
                                st_wishlist_item_id = c.getString(TAG_wishlist_item_id);
                                st_wishlist_price = c.getString(TAG_wishlist_price);
                                st_wishlist_qty = c.getString(TAG_wishlist_qty);
                                st_wishlist_sku = c.getString(TAG_wishlist_sku);
                                st_wishlist_name = c.getString(TAG_wishlist_name);

                                arr_added_at[i] = st_added_at;
                                arr_images[i] = st_images;
                                arr_product_id[i] = st_product_id;
                                arr_store_id[i] = st_store_id;
                                arr_wishlist_id[i] = st_wishlist_id;
                                arr_wishlist_item_id[i] = st_wishlist_item_id;
                                arr_wishlist_price [i] = st_wishlist_price;
                                arr_wishlist_qty[i] = st_wishlist_qty;
                                arr_wishlist_sku[i] = st_wishlist_sku;
                                arr_wishlist_name[i] = st_wishlist_name;

                            }

                            lstadapter = new WishListItemAdapter(WishListDetails.this,arr_images,
                                    arr_wishlist_name,arr_wishlist_price,arr_wishlist_sku);

                            if (lstadapter.getCount() > 0) {
                                list_wishlist_item.setAdapter(lstadapter);
                                list_wishlist_item.setVisibility(View.VISIBLE);
                                list_wishlist_item.invalidate();
                            }
                            else
                            {
                                iv_image_no_details.setVisibility(View.VISIBLE);
                                list_wishlist_item.setVisibility(View.GONE);
                                tv_alert_wishlist.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {

                            //e.printStackTrace();
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to fetch data\nPlease try again",Snackbar.LENGTH_LONG).show();


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
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count =0;
                            //initNavigationDrawer();
                            tv_alert_wishlist.setVisibility(View.VISIBLE);
                            list_wishlist_item.setVisibility(View.GONE);

                        }
                        else {
                            //Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                            CommonFun.showVolleyException(error,WishListDetails.this);
                        }


//                        Intent intent=new Intent(getBaseContext(), com.galwaykart.essentialClass.InternetConnectivityError.class);
//                        startActivity(intent);


                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);


    }


    /**
     * Move wishlist into cart
     * @param v
     */
    public void AddCartWishListItem(View v) {

        View parentRow = (View) v.getParent();
        final int position = list_wishlist_item.getPositionForView(parentRow);

        int selindexof = position;

        wishList_product_id = arr_product_id[selindexof];
        ////Log.d("wishList_product_id",wishList_product_id);

        getCartId_v1(selindexof);

    }


    public void deleteWishListItem(View v) {

        View parentRow = (View) v.getParent();
        final int position = list_wishlist_item.getPositionForView(parentRow);

        int selindexof = position;
        alertForDelete(selindexof);


    }

    private void alertForDelete(int selindexof) {
        String errmsg = "Are you sure want to Delete product";
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(WishListDetails.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    wishList_product_id = arr_product_id[selindexof];
                    ////Log.d("wishList_product_id",wishList_product_id);

                    deleteCartItem();

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

    private void deleteCartItemAfterMoveCart(int selindexof){

        wishList_product_id = arr_product_id[selindexof];
        ////Log.d("wishList_product_id",wishList_product_id);
        redirectCart=true;
        deleteCartItem();

    }


    private void deleteCartItem() {

//        delete_to_wishlist_URL = Global_Settings.api_url+
//                "glaze/wishlist_delete.php?customer_id="+st_customer_id+"&product_id="+wishList_product_id;


        delete_to_wishlist_URL=Global_Settings.api_url+
                "rest/V1/m-carts/mine/addwishlistdelete";
        ////Log.d("delete_to_wishlist_URL",delete_to_wishlist_URL);

        String input_data=
                "{" +
                "\"customerId\": \""+st_customer_id+"\"," +
                "\"productId\": \""+wishList_product_id+"\"" +
                "}";

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest jsObjRequest = null;
        jsObjRequest = new StringRequest(Request.Method.POST, delete_to_wishlist_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("wish_delresponse", response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            JSONArray jsonObj = new JSONArray(response);



                            JSONObject jsonNode = jsonObj.getJSONObject(0);

                            String msg = jsonNode.getString("msg");
                            String status= jsonNode.getString("status");



                            String st_status = String.valueOf(status);
                            st_msg = String.valueOf(msg);

//                            if(st_status.equalsIgnoreCase("true"))
//                            {
//                                callWishListItem();
//                                CommonFun.alertError(WishListDetails.this, msg+"");
//                            }
//                            else
//                            {
//
//                                CommonFun.alertError(WishListDetails.this, st_msg);
//                            }


                      if(status.equals("1")) {
                          if (redirectCart == true) {


                              //refreshItemCount();
                              Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                              vibrator.vibrate(100);

                              final Dialog dialog = new Dialog(WishListDetails.this);
                              dialog.setContentView(R.layout.custom_alert_dialog_design);
                              TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                              tv_dialog.setText("Item moved to cart");
                              ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                              dialog.show();

                              new CountDownTimer(2000, 2000) {

                                  @Override
                                  public void onTick(long millisUntilFinished) {
                                      // TODO Auto-generated method stub

                                  }

                                  @Override
                                  public void onFinish() {
                                      // TODO Auto-generated method stub

                                      dialog.dismiss();

                                      Intent intent = new Intent(WishListDetails.this, CartItemList.class);
                                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                      startActivity(intent);
                                      CommonFun.finishscreen(WishListDetails.this);

                                  }
                              }.start();

                          } else {
                              //callWishListItem();




                              Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                              vibrator.vibrate(100);

                              final Dialog dialog = new Dialog(WishListDetails.this);
                              dialog.setContentView(R.layout.custom_alert_dialog_design);
                              TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                              tv_dialog.setText(msg);
                              ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                              dialog.show();

                              new CountDownTimer(2000, 2000) {

                                  @Override
                                  public void onTick(long millisUntilFinished) {
                                      // TODO Auto-generated method stub

                                  }

                                  @Override
                                  public void onFinish() {
                                      // TODO Auto-generated method stub

                                      dialog.dismiss();

                                      Intent intent = new Intent(WishListDetails.this, WishListDetails.class);
                                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                      startActivity(intent);
                                      CommonFun.finishscreen(WishListDetails.this);

                                  }
                              }.start();
                          }
                      }
                      else
                      {



                          Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                          vibrator.vibrate(100);

                          final Dialog dialog = new Dialog(WishListDetails.this);
                          dialog.setContentView(R.layout.custom_alert_dialog_design);
                          TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                          tv_dialog.setText(msg);
                          ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                          dialog.show();

                          new CountDownTimer(2000, 2000) {

                              @Override
                              public void onTick(long millisUntilFinished) {
                                  // TODO Auto-generated method stub

                              }

                              @Override
                              public void onFinish() {
                                  // TODO Auto-generated method stub

                                  dialog.dismiss();

                                  Intent intent = new Intent(WishListDetails.this, WishListDetails.class);
                                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(intent);
                                  CommonFun.finishscreen(WishListDetails.this);

                              }
                          }.start();
                      }

                        } catch (JSONException e) {
                            //CommonFun.alertError(WishListDetails.this, st_msg);



                            if(redirectCart==true) {

                                //refreshItemCount();
                                Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(WishListDetails.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                tv_dialog.setText("Item moved to cart");
                                ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                dialog.show();

                                new CountDownTimer(2000, 2000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub

                                        dialog.dismiss();

                                        Intent intent = new Intent(WishListDetails.this, CartItemList.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonFun.finishscreen(WishListDetails.this);

                                    }
                                }.start();
                            }



                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();
                         CommonFun.showVolleyException(error,WishListDetails.this);


                    }
                }
        ) {
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
                return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//
//
//                //////Log.d("delievery data in",delivery_data_in.toString());
//               // headers.put("Authorization", "Bearer " + tokenData);
//                //headers.put("Content-Type","application/json");
//                return headers;
//            }

        };
        queue.add(jsObjRequest);


    }


/**
 * Cart
  */

    /**
     * Working and give cart id
     */
    private void getCartId_v1(int selindexof) {

        RequestQueue queue = Volley.newRequestQueue(this);


        // st_token_data="w4svj2g3pw7k2vmtxo3wthipjy3ieig0";

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST,st_cart_URL,
                new Response.Listener<String>() {

                    @Override   public void onResponse(String response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {


                            ////Log.d("Response",response.toString());
                            //     CommonFun.alertError(MainActivity.this,response.toString());
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//                            String cart_id=jsonObj.getString("id");
//
//                            ////Log.d("cart_id",cart_id);
////
////                            SharedPreferences.Editor editor= preferences.edit();
////                            editor.putString("cart_id",cart_id);
////                            editor.commit();

                            // addItemToCart(cart_id);


                            String cart_id= response;
                            cart_id=cart_id.replaceAll("\"","");
                            callJSONAPIVolley(cart_id,selindexof);
                            //addItemToCart(cart_id,selindexof);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFun.alertError(WishListDetails.this,e.toString());
                        }

                    }


                },


                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        CommonFun.showVolleyException(error,WishListDetails.this);
                        //////Log.d("ERROR","error => "+error.toString());
                        //CommonFun.alertError(MainActivity.this,error.toString());
                    }
                }
        ) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+st_token_data);
                //   params.put("Content-Type","application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);

    }


    /**
     *
     */
    private void addItemToCart(String cart_id, final int selindexof,Boolean is_bundle) {

        String add_to_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine/items/";

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);

        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        String product_sku=arr_wishlist_sku[selindexof];

        String cartqty="1";


        String stxt="{\"cartItem\":{\"sku\": \""+product_sku+"\",\"qty\": "+cartqty+",\"quote_id\": \""+cart_id+"\"}}";

        String strProduct="";
        if(is_bundle==false)
        {
            strProduct = "{\"cartItem\":{\"sku\": \"" + product_sku + "\",\"qty\": " + cartqty + ",\"quote_id\": \"" + cart_id + "\"}}";
        }
        else
        {
            for(int i=0;i<json_len_bundal;i++) {
                if (stBundle.equalsIgnoreCase("")){
                    stBundle = "{\"option_id\":"+option_id_bundal.get(i)+"," +
                            "\"option_qty\":"+qty_bundal.get(i)+"," +
                            "\"option_selections\":["+id_bundal.get(i)+"]}";

                }
                else{
                    stBundle = stBundle+","+"{\"option_id\":"+option_id_bundal.get(i)+"," +
                            "\"option_qty\":"+qty_bundal.get(i)+"," +
                            "\"option_selections\":["+id_bundal.get(i)+"]}";
                }
            }

            strProduct = "{\"cartItem\":{\"sku\":\""+product_sku+"\"," +
                    "\"qty\":\""+cartqty+"\"," +
                    "\"quote_id\":\""+cart_id+"\"," +
                    "\"product_option\":{\"extension_attributes\":{\"bundle_options\":["+stBundle+"]}}}}";

            Log.e("strProduct", strProduct);

        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.POST, add_to_cart_URL,new JSONObject(strProduct),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            if(response!=null){
                                try {
                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                    String st_qty = jsonObj.getString("qty");
                                    deleteCartItemAfterMoveCart(selindexof);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,WishListDetails.this);

                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer "+st_token_data);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };
        } catch (JSONException e) {
            e.printStackTrace();
        }




        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));



        queue.add(jsObjRequest);

    }



    private void callJSONAPIVolley(String cart_id,int selindexof) {

        id_bundal.clear();
        option_id_bundal.clear();
        qty_bundal.clear();

       String product_sku=arr_wishlist_sku[selindexof];
        pref= CommonFun.getPreferences(getApplicationContext());
        //final String product_sku=pref.getString("showitemsku","");
        String fromurl= Global_Settings.api_url+"index.php/rest/V1/m-products/"+product_sku;
        Log.e("fromurl", fromurl);

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        //Log.d("single",response.toString());

                        //   CommonFun.alertError(MainActivity.this,response.toString());
                        if(response!=null){
                            try {

                                JSONObject jsonObj = new JSONObject(response);

                                Boolean product_type_id=false;
                                String status_product=jsonObj.getString("status");
                                product_type=jsonObj.getString("type_id");
                                if(product_type.equals("bundle"))
                                {
                                    product_type_id = true;
                                }
                                if (product_type_id==true)
                                {
                                    JSONObject extensionAttributesObj = jsonObj.getJSONObject("extension_attributes");
                                    JSONArray bundle_product_optionsArry = extensionAttributesObj.getJSONArray("bundle_product_options");
                                    for (int p = 0; p < bundle_product_optionsArry.length(); p++) {
                                        JSONObject bundleOptionData = bundle_product_optionsArry.getJSONObject(p);
                                        JSONArray product_linksJson = bundleOptionData.getJSONArray("product_links");
                                        json_len_bundal = product_linksJson.length();
                                        for(int a = 0; a < product_linksJson.length(); a++)
                                        {
                                            JSONObject jsonData = product_linksJson.getJSONObject(a);
                                            id_bundal.add(jsonData.getString("id"));
                                            option_id_bundal.add(jsonData.getString("option_id"));
                                            qty_bundal.add(jsonData.getString("qty"));

                                            Log.e("id_arry",id_bundal + "");
                                            Log.e("option_id_arry",option_id_bundal + "");
                                            Log.e("qty_arry",qty_bundal + "");

                                        }
                                    }
                                }
                                addItemToCart(cart_id,selindexof,product_type_id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                CommonFun.showVolleyException(error,WishListDetails.this);

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + st_token_data);
                headers.put("Content-Type","application/json");
                return headers;
            }

            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

}
