package com.galwaykart.Guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Legal.FaqActivity;
import com.galwaykart.address_book.AddNewAddress;
import com.galwaykart.EwalletActivity;
import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;
import com.galwaykart.app_promo.AppPromoHome;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import com.galwaykart.newsnotice.NoticeActivity;
import com.galwaykart.notification.NotificationListActivity;
import com.galwaykart.partialReturn.ReturnedOrderList;
import com.galwaykart.profile.ChangePasswordActivity;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.UpdateAddressActivity;
import com.galwaykart.profile.wishList.WishListDetails;
import com.galwaykart.registration.RegistrationActivity;
import com.galwaykart.testimonial.TestimonialActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankesh on 9/14/2017.
 */

public abstract class GuestBaseActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener
        {

    private Menu menu;
    //TransparentProgressDialog pDialog;
    String tokenData;
    Toolbar toolbar;
    ProgressBar cart_progressBar;


//    protected void onCreate(Bundle savedInstanceState,int activity_layoutId)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.common_layout);
//
//    }




    public void initNavigationDrawer(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




//        actionBar.setCustomView(R.layout.my_action_bar);
//        button=(Button)actionBar.getCustomView().findViewById(R.id.button);
//        button.setText("text");

        //initNavigationDrawer(toolbar);


//        SharedPreferences pref;
//        pref= CommonFun.getPreferences(getApplicationContext());
//        String fname=pref.getString("login_fname","");
//        String lname=pref.getString("login_lname","");
//
//        String customer_name=fname+" "+lname;
//
//        String value_email=pref.getString("login_email","");
//        if (value_email != null && !value_email.equals("")) {
//
//        }
//        else
//        {
//            Intent intent=new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_guest_home_page_drawer);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);
        TextView tvCustomerName= header.findViewById(R.id.tvCustomerName);
        tvCustomerName.setText("Guest");

//        cart_progressBar=(ProgressBar)toolbar.findViewById(R.id.cart_progressBar);
//        cart_progressBar.setVisibility(View.GONE);
        updateMenuTitles(toolbar,"0","true");
        refreshItemCount(this);

        Menu menu = navigationView.getMenu();
        MenuItem verinfo = menu.findItem(R.id.version);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            verinfo.setTitle("Version - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void initNavigationDrawerWithoutToolbar(Toolbar toolbar){


//        actionBar.setCustomView(R.layout.my_action_bar);
//        button=(Button)actionBar.getCustomView().findViewById(R.id.button);
//        button.setText("text");

        //initNavigationDrawer(toolbar);

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        String fname=pref.getString("login_fname","");
        String lname=pref.getString("login_lname","");

        String customer_name=fname+" "+lname;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);
        TextView tvCustomerName= header.findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(customer_name);

        updateMenuTitles(toolbar,"0","true");

    }

    private void updateMenuTitles(Toolbar toolbar,String count,String progressShow) {

        //MenuItem cartMenuItem = menu.findItem(R.id.action_add_to_cart);
        TextView tv_cartqty;
        tv_cartqty= toolbar.findViewById(R.id.cart_icon);
        tv_cartqty.setText(count);
        tv_cartqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_cartqty.setEnabled(false);
                openCart();
            }
        });

        cart_progressBar= toolbar.findViewById(R.id.cart_progressBar);

        if(progressShow.equalsIgnoreCase("true"))
                cart_progressBar.setVisibility(View.VISIBLE);
        else
            cart_progressBar.setVisibility(View.GONE);

        ImageView imageViewIcon;
        imageViewIcon= toolbar.findViewById(R.id.image_view_title);
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageViewIcon.setEnabled(false);
                openHomeActivity();
            }
        });


    }

    private void openHomeActivity() {
        Intent intent=new Intent(this,GuestHomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    public void openCart(){

    //    callBeforeGoToCartForOffer();
        Intent intent=new Intent(this, GuestCartItemList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        // Intent intent=new Intent(this, AddressBookClass.class);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }

    public void refreshItemCount(Context context){

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        // tokenData=pref.getString("tokenData","");
        //  tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";


        String guest_cart_id=pref.getString("guest_cart_id","");
       // guest_cart_id="l0FYWs0ai3vq6T1nfur30OGnmXXCyPU7";

        if(guest_cart_id!=null && !guest_cart_id.equals("")) {
            String url_cart_item_list = Global_Settings.api_url + "rest/V1/guest-carts/" + guest_cart_id;
            //updateMenuTitles(toolbar, String.valueOf(0),"false");

            //Log.d("cartcount", url_cart_item_list);
            callCartItemList(url_cart_item_list, context);
        }
        else
        {
            updateMenuTitles(toolbar, String.valueOf(0),"false");
        }

    }



    private void callCartItemList(String url_cart_item_list, final Context context) {

        final String TAG_total_item_count = "items_qty";
     //   updateMenuTitles(toolbar, "0","true");

  //      tokenData = tokenData.replaceAll("\"", "");

//        pDialog = new TransparentProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("adresponse", response.toString());
//                        CommonFun.alertError(CartItemList.this, response.toString());

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                           int total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));

                            //initNavigationDrawer();
                            updateMenuTitles(toolbar, String.valueOf(total_cart_count),"false");

                        } catch (JSONException e) {
                            updateMenuTitles(toolbar, "","false");
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
                        ////////Log.d("ERROR", "error => " + error.toString());
                        //CommonFun.alertError(context, error.toString());

//                        Intent intent=new Intent(BaseActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);

                       // Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();

                        if (error instanceof ParseError || error instanceof ServerError) {
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count =0;
                            //initNavigationDrawer();
                            updateMenuTitles(toolbar, String.valueOf(total_cart_count),"false");

                        }
                        else {
                            Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
                        }

                    }
                }
        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + tokenData);
//               // params.put("Authorization", "Bearer "  );
//                params.put("Content-Type", "application/json");
//
//                return params;
//            }
        };
        queue.add(jsObjRequest);

    }

    private void callBeforeGoToCartForOffer() {

        SharedPreferences pref= CommonFun.getPreferences(getApplicationContext());
        String tokenData=pref.getString("tokenData","");

        String offer_URL = Global_Settings.api_url + "rest/V1/m-carts/product/offers";



        //final String stxt = "{\"cartItem\":{\"sku\": \"" + off_product_sku + "\",\"qty\": " + off_cartqty + ",\"quote_id\": \"" + cart_id + "\"}}";
        ////Log.d("stxt value", stxt);


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = null;
        try {
            jsObjRequest = new StringRequest(Request.Method.POST, offer_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            //Log.d("new_res",response.toString());

                            String getRes = response.replaceAll("\"", "");

                            if(getRes.equalsIgnoreCase("true"))
                            {
                                 Intent intent=new Intent(getBaseContext(), GuestCartItemList.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    // Intent intent=new Intent(this, AddressBookClass.class);
                                    startActivity(intent);
                                    CommonFun.finishscreen(GuestBaseActivity.this);
                              }
                            else
                            {
                                CommonFun.alertError(getBaseContext(),"Something Went wrong!!! Try again");
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof ServerError) {

                        CommonFun.alertError(getBaseContext(), "Please try to add maximum of 25 qty...");
                    } else
                        CommonFun.showVolleyException(error, getBaseContext());
                }


            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }


        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                0, 0);
        jsObjRequest.setRetryPolicy(retryPolicy);

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }



            @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            openHomeActivity();

        }
        else if(id==R.id.nav_Login){

            SharedPreferences pref;
            pref = CommonFun.getPreferences(getApplicationContext());

            String[] str=new String[20];
            String guest_cart_id=pref.getString("guest_cart_id","");

            if(guest_cart_id!=null && !guest_cart_id.equals("")) {

                SharedPreferences.Editor editor1 = pref.edit();
                editor1.putString("guest_cart_id_process", guest_cart_id);
                editor1.commit();

            }

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }else if (id == R.id.nav_reg) {

            Intent intent=new Intent(this, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if (id == R.id.nav_order) {

            Intent intent=new Intent(this, OrderListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }else if(id==R.id.nav_wishlist){

            Intent intent_wishlist=new Intent(this, WishListDetails.class);
            intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_wishlist);
            CommonFun.finishscreen(this);
        }
        else if(id==R.id.nav_ewallet){
            Intent intent_ewallet=new Intent(this, EwalletActivity.class);
            intent_ewallet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_ewallet);
            CommonFun.finishscreen(this);
        }
        else if(id==R.id.change_pwd){
            Intent intent=new Intent(this, ChangePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);


        }
        else if(id==R.id.other_glaze_app){
            Intent intent=new Intent(this, AppPromoHome.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent intent=new Intent(this, LogoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.nav_notification){

            Intent intent=new Intent(this, NotificationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.customerlearningPoint)
        {
            Intent intent=new Intent(GuestBaseActivity.this, FaqActivity.class);
            intent.putExtra("comefrom","customer-help-desk-tutorials");
            startActivity(intent);

            //CommonFun.finishscreen(this);
        }
        else if(id==R.id.legalabout){


            Intent intent=new Intent(this, LegalAboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.nav_guest_Notice){

            Intent intent=new Intent(this, NotificationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }

        else if(id==R.id.address_book){

            SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
            String login_group_id=pref.getString("login_group_id","");
            if(login_group_id.equals("4")) {

                Intent intent = new Intent(this, UpdateAddressActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(this);

            }
            else
            {
                Intent intent = new Intent(this, AddNewAddress.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("st_come_from_update","updateaddress");
                startActivity(intent);
                CommonFun.finishscreen(this);
            }
        }

        else if(id==R.id.return_order){

            Intent intent=new Intent(this, ReturnedOrderList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.nav_testimonial){

            Intent intent=new Intent(this, TestimonialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.nav_notice){

            Intent intent=new Intent(this, NoticeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}