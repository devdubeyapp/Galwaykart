package com.galwaykart.profile.wishList;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    final String TAG_images = "images";
    final String TAG_wishlist_sku = "sku";
    final String TAG_wishlist_price = "price";
    final String TAG_wishlist_name = "name";
    ListAdapter lstadapter;

    String stringRequest = null;
    String st_token_data = "";
    String st_cart_URL = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/carts/mine";
    SharedPreferences preferences;

    String[] arr_wishlist_sku, arr_wishlist_qty,
            arr_product_id,arr_added_at,arr_wishlist_price,
            arr_wishlist_item_id,arr_wishlist_id,arr_store_id,arr_images,arr_wishlist_name;

    String  delete_to_wishlist_URL = "";

    String st_product_id="",st_added_at="",st_wishlist_qty="",st_wishlist_item_id="",st_msg="",
            st_wishlist_id="",st_store_id="",st_images="",st_wishlist_sku="",st_wishlist_price="",st_wishlist_name="",wishList_product_id="";
    Boolean redirectCart;


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

        /**
         * init controls
         */
        list_wishlist_item = (ListView) findViewById(R.id.list_wishlist_item);
        tv_alert_wishlist = (TextView)findViewById(R.id.tv_alert_wishlist);

        tv_alert_wishlist.setVisibility(View.GONE);
        list_wishlist_item.setVisibility(View.VISIBLE);

        wish_list_item_URL = Global_Settings.api_url+"glaze/wishlist_details.php?id="+st_customer_id;
        ////Log.d("wish_list_item_URL",wish_list_item_URL);

        callWishListItem();

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
                                    arr_wishlist_name,arr_wishlist_price);

                            if (lstadapter.getCount() > 0) {
                                list_wishlist_item.setAdapter(lstadapter);
                                list_wishlist_item.setVisibility(View.VISIBLE);
                                list_wishlist_item.invalidate();
                            }
                            else
                            {

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

        wishList_product_id = arr_product_id[selindexof];
        ////Log.d("wishList_product_id",wishList_product_id);

        deleteCartItem();

    }

    private void deleteCartItemAfterMoveCart(int selindexof){

        wishList_product_id = arr_product_id[selindexof];
        ////Log.d("wishList_product_id",wishList_product_id);
        redirectCart=true;
        deleteCartItem();

    }


    private void deleteCartItem() {

        delete_to_wishlist_URL = Global_Settings.api_url+
                "glaze/wishlist_delete.php?customer_id="+st_customer_id+"&product_id="+wishList_product_id;

        ////Log.d("delete_to_wishlist_URL",delete_to_wishlist_URL);

        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = null;
        jsObjRequest = new JsonObjectRequest(Request.Method.GET, delete_to_wishlist_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));


                            JSONObject jsonNode = jsonObj.getJSONObject("deleteitems");

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


                            if(redirectCart==true){




                                //refreshItemCount();
                                Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(WishListDetails.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
                                tv_dialog.setText("Item moved to cart");
                                ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
                                dialog.show();

                                new CountDownTimer(2000,2000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub

                                        dialog.dismiss();

                                        Intent intent=new Intent(WishListDetails.this, CartItemList.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CommonFun.finishscreen(WishListDetails.this);

                                    }
                                }.start();

                            }
                            else
                            {
                                callWishListItem();
                            }


                        } catch (JSONException e) {
                            //CommonFun.alertError(WishListDetails.this, st_msg);



                            if(redirectCart==true) {

                                //refreshItemCount();
                                Vibrator vibrator = (Vibrator) WishListDetails.this.getSystemService(WishListDetails.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(WishListDetails.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
                                tv_dialog.setText("Item moved to cart");
                                ImageView image_view_dialog = (ImageView) dialog.findViewById(R.id.image_view_dialog);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);


    }


/**
 * Cart
  */

    /**
     * Working and give cart id
     */
    private void getCartId_v1(final int selindexof) {

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


                            String cart_id=response.toString();
                            cart_id=cart_id.replaceAll("\"","");
                            addItemToCart(cart_id,selindexof);
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
    private void addItemToCart(String cart_id, final int selindexof) {

        String add_to_cart_URL = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/carts/mine/items/";

        //    String qty = "2";
        //  String sku = "GRP08100";


        pDialog = new TransparentProgressDialog(WishListDetails.this);
        pDialog.setCancelable(false);

        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        //String product_sku=preferences.getString("showitemsku","");

        String product_sku=arr_wishlist_sku[selindexof];

        String cartqty="1";


        final String stxt="{\"cartItem\":{\"sku\": \""+product_sku+"\",\"qty\": "+cartqty+",\"quote_id\": \""+cart_id+"\"}}";
        ////Log.d("stxt value",stxt);


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonObjectRequest(Request.Method.POST, add_to_cart_URL,new JSONObject(stxt),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if(pDialog.isShowing())
                                pDialog.dismiss();

                            if(response!=null){
                                try {
                                    JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                    String st_qty = jsonObj.getString("qty");
                                    ////Log.d("st_qty",st_qty);

                                    deleteCartItemAfterMoveCart(selindexof);
                                    /**
                                     * Update cart quantity
                                     */





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

}
