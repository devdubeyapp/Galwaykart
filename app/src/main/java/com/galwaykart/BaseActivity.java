package com.galwaykart;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.galwaykart.address_book.CustomerAddressBook;
import com.galwaykart.app_promo.AppPromoHome;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.helpdesksupport.simplecomplaint.ComplaintMainActivity;
import com.galwaykart.navigationDrawer.ExpandableCustomListAdapter;
import com.galwaykart.navigationDrawer.MenuModel;
import com.galwaykart.newsnotice.NoticeActivity;
import com.galwaykart.report.CouponReportActivity;
import com.galwaykart.testimonial.TestimonialActivity;
import com.galwaykart.vendormanagement.VendorRegistration;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.notification.NotificationListActivity;
import com.galwaykart.partialReturn.ReturnedOrderList;
import com.galwaykart.profile.ChangeEmailActivity;
import com.galwaykart.profile.ChangeMobileActivity;
import com.galwaykart.profile.ChangePasswordActivity;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.wishList.WishListDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ankesh on 9/14/2017.
 */

public abstract class BaseActivity extends AppCompatActivity
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

    ExpandableCustomListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    String dist_id="";

    public void initNavigationDrawer(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        actionBar.setCustomView(R.layout.my_action_bar);
//        button=(Button)actionBar.getCustomView().findViewById(R.id.button);
//        button.setText("text");

        //initNavigationDrawer(toolbar);

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        String fname=pref.getString("login_fname","");
        String lname=pref.getString("login_lname","");


        String customer_name=fname+" "+lname;

        String value_email=pref.getString("login_email","");
        if (value_email != null && !value_email.equals("")) {

        }
        else
        {
            Intent intent=new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String st_dist_id=pref.getString("user_irid","");
        if(st_dist_id!=null && !st_dist_id.equals("")){
            dist_id=st_dist_id;
        }


        View header=navigationView.getHeaderView(0);
        TextView tvCustomerName= header.findViewById(R.id.tvCustomerName);
        if(!dist_id.equals(""))
            tvCustomerName.setText(customer_name +" \n("+dist_id+")");
        else
            tvCustomerName.setText(customer_name);


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

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableCustomListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        //Log.d("groupPosition",groupPosition+"");
                        if(groupPosition == 0){
                            Intent intent=new Intent(BaseActivity.this, HomePageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }else if(groupPosition == 1){
                            Intent intent=new Intent(BaseActivity.this, OrderListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }else if(groupPosition == 2){
                            Intent intent=new Intent(BaseActivity.this, NotificationListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }

                        else if(groupPosition==4)
                        {
                            Intent intent_wishlist=new Intent(BaseActivity.this, WebViewActivity.class);
                            intent_wishlist.putExtra("comefrom","customer-help-desk-tutorials");
                            intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_wishlist);
                            CommonFun.finishscreen(BaseActivity.this);
                        }

                        else if(groupPosition == 6){
                            Intent intent=new Intent(BaseActivity.this, TestimonialActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }
                        else if(groupPosition == 7){
                            Intent intent=new Intent(BaseActivity.this, NoticeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }

                        else if(groupPosition == 8){
                            Intent intent=new Intent(BaseActivity.this, VendorRegistration.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(BaseActivity.this);
                        }


                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    //Log.d("groupPosition",groupPosition+"");
                    //Log.d("childPosition",childPosition+"");
                    if(groupPosition==3 && childPosition == 0){
                        Intent intent_wishlist=new Intent(BaseActivity.this, WishListDetails.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }

                    else if(groupPosition==3 && childPosition == 1){
                        Intent intent_wishlist=new Intent(BaseActivity.this, ComplaintMainActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }

                    else if(groupPosition==3 && childPosition == 2){
                        Intent intent_wishlist=new Intent(BaseActivity.this, MyComplaints.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 3){
                        Intent intent_wishlist=new Intent(BaseActivity.this, CouponReportActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 4){
                        Intent intent_wishlist=new Intent(BaseActivity.this, ChangeMobileActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 5){
                        Intent intent_wishlist=new Intent(BaseActivity.this, ChangePasswordActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 6){
                        Intent intent_wishlist=new Intent(BaseActivity.this, ChangeEmailActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 7){
                        SharedPreferences pref;
                        pref= CommonFun.getPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("st_come_from_update","updateaddress");
                        editor.commit();

                        Intent intent = new Intent(BaseActivity.this, CustomerAddressBook.class);
                        intent.putExtra("st_come_from_update","updateaddress");

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        CommonFun.finishscreen(BaseActivity.this);

                    }
                    else if(groupPosition==3 && childPosition == 8){
                        alertMsg();
                    }
                    /*else if(groupPosition==4 && childPosition == 0){
                        Intent intent_wishlist=new Intent(BaseActivity.this, WebViewActivity.class);
                        intent_wishlist.putExtra("comefrom","customer-help-desk-tutorials.html");
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }*/

                    else if(groupPosition==5 && childPosition == 0){
                        Intent intent_wishlist=new Intent(BaseActivity.this, LegalAboutActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);

                    }else if(groupPosition==5 && childPosition == 1){
                        Intent intent_wishlist=new Intent(BaseActivity.this, AppPromoHome.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(BaseActivity.this);
                    }else if(groupPosition==5 && childPosition == 2){
//                        Intent intent_wishlist=new Intent(HomePageActivity.this, LogoutActivity.class);
//                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent_wishlist);
//                        CommonFun.finishscreen(HomePageActivity.this);
                    }

                }

                return false;
            }
        });
    }


    private void prepareMenuData() {

        MenuModel menuModel;

        menuModel = new MenuModel("Home", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Your Order", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Notifications", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("My Profile", true, true); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();

        MenuModel childModel = new MenuModel("Wishlist", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Complaint Generation", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Complaint Status", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Voucher Report", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Change Mobile Number", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Change Password", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Change Email", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Address Book", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Logout", false, false);
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
           // //Log.d("API123","here");
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Customer HelpDesk", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Settings", true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsSettingList = new ArrayList<>();

        /*childModel = new MenuModel("Customer HelpDesk", false, false);
        childModelsList.add(childModel);*/


        childModel = new MenuModel("Legal and About", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Other Galway App", false, false);
        childModelsList.add(childModel);



        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
        menuModel = new MenuModel("Testimonial", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("News", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        PackageInfo pInfo = null;
        String version="";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version ="Version: "+ pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        menuModel = new MenuModel("Become a Vendor", true, false);
        headerList.add(menuModel);

        menuModel = new MenuModel(version, true, false);
        headerList.add(menuModel);

    }

    private void alertMsg() {
        String errmsg = "Are you sure want to logout";
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(BaseActivity.this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    Intent intent_wishlist=new Intent(BaseActivity.this, LogoutActivity.class);
                    intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent_wishlist);
                    CommonFun.finishscreen(BaseActivity.this);


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
    TextView tv_cartqty;
    private void updateMenuTitles(Toolbar toolbar,String count,String progressShow) {

        //MenuItem cartMenuItem = menu.findItem(R.id.action_add_to_cart);

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


    private void openHomeActivity()
    {
        Intent intent=new Intent(this,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }


    public void openCart(){

        callBeforeGoToCartForOffer();
//        Intent intent=new Intent(this, CartItemList.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        // Intent intent=new Intent(this, AddressBookClass.class);
//        startActivity(intent);
//        CommonFun.finishscreen(this);
    }

    public void refreshItemCount(Context context){

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
         tokenData=pref.getString("tokenData","");
        //  tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";

        String url_cart_item_list = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/m-carts/mine";
        callCartItemList(url_cart_item_list,context);

    }



    private void callCartItemList(String url_cart_item_list, final Context context) {

        final String TAG_total_item_count = "items_qty";
        updateMenuTitles(toolbar, "0","true");

        tokenData = tokenData.replaceAll("\"", "");

//        pDialog = new TransparentProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //////Log.d("response", response.toString());
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
               // params.put("Authorization", "Bearer "  );
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        queue.add(jsObjRequest);

    }

    private void callBeforeGoToCartForOffer()
    {

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
                                 Intent intent=new Intent(getBaseContext(), CartItemList.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    // Intent intent=new Intent(this, AddressBookClass.class);
                                    startActivity(intent);
                                    CommonFun.finishscreen(BaseActivity.this);
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

        } else if (id == R.id.nav_order) {

            Intent intent=new Intent(this, OrderListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.other_glaze_app){
            Intent intent=new Intent(this, AppPromoHome.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(id==R.id.nav_wishlist){

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
        else if(id==R.id.nav_change_email){
            Intent intent=new Intent(this, ChangeEmailActivity.class);
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
        else if(id==R.id.nav_coupon_report){

            Intent intent=new Intent(this, CouponReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.change_pwd){
            Intent intent=new Intent(this, ChangePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);


        }
        else if(id==R.id.nav_change_mobile){
            Intent intent=new Intent(this, ChangeMobileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent intent=new Intent(this, LogoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.customerlearningPoint)
        {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Global_Settings.web_url+"customer-help-desk-tutorials"));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(BaseActivity.this, "No application can handle this request." + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


           /* Intent intent=new Intent(this,WebViewActivity.class);
            intent.putExtra("comefrom","customer-help-desk-tutorials");
            startActivity(intent);*/
            //CommonFun.finishscreen(this);
        }
        else if(id==R.id.legalabout){

            Intent intent=new Intent(this, LegalAboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.address_book){

            Intent intent = new Intent(this, CustomerAddressBook.class);
            intent.putExtra("st_come_from_update","updateaddress");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.return_order){

            Intent intent=new Intent(this, ReturnedOrderList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}