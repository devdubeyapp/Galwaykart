package com.galwaykart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.CAdapter.DataModelHomeProduct;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.CAdapter.RecyclerViewHomeCategoryAdapter;
import com.galwaykart.CAdapter.RecyclerViewHomeProductAdapter;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.partialReturn.ReturnedOrderList;
import com.galwaykart.productList.ProductListActivity;
import com.galwaykart.productList.ShowCategoryList;
import com.galwaykart.profile.ChangePasswordActivity;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.UpdateAddressActivity;
import com.galwaykart.profile.wishList.WishListDetails;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class HomePageActivity_v1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    SearchView searchView;
    String customer_name;
    CarouselView carouselView;
    String[] banner_image;
    String[] banner_image_sku;
    String[] banner_image_catid;
    int total_banner_item;
    ListView list_category,list_product;
    TextView tvCategoryHead,tvProductHead;

    final String TAG_pname = "pname";
    final String TAG_price = "price";
    final String TAG_sky = "sku";
    final String TAG_image = "image";

    final String TAG_catid = "id";
    final String TAG_catimage = "image";


    RecyclerView recyclerView_Product;
    RecyclerView recyclerView_Category;

    private List<DataModelHomeCategory> itemdCatList;
    private List<DataModelHomeProduct> itemdProductList;
    private RecyclerViewHomeProductAdapter adapter;
    private RecyclerViewHomeCategoryAdapter cat_adapter;
    int[] sampleImages = {R.drawable.logo};

    Boolean loadData=false;
    TextView tv_CustomerName;
    String tokenData="";
    Toolbar toolbar;
    ProgressBar cart_progressBar;
    SearchView ed_search_view;

    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progress_bar;
    Boolean dataLoad;
    ViewPager pager_view_banner;
    Timer timer;
    int page = 0;
    ImageView float_offer_button,float_chat_button;
    final String API_ID = "0e3c5b88-b2b2-48ee-8509-a6db6e6df6cd";
    final String API_KEY = "ef4384c8-b541-4229-8e47-becd7aa1f6ea";

    TabHost tabHost;


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
             super.onBackPressed();
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());


//        tabHost=(TabHost)findViewById(R.id.tablhost);
//
//        TabHost.TabSpec spec;
//        Intent intent_2;
//
//        spec = tabHost.newTabSpec("home"); // Create a new TabSpec using tab host
//        spec.setIndicator("HOME"); // set the “HOME” as an indicator
//        // Create an Intent to launch an Activity for the tab (to be reused)
//        intent_2 = new Intent(this, ProductListActivity.class);
//        spec.setContent(intent_2);
//        tabHost.addTab(spec);

        //CommonFun.alertError(HomePageActivity.this,pref.getString("login_customer_id",""));

        String fname = pref.getString("login_fname", "");
        String lname = pref.getString("login_lname", "");

        String value_email=pref.getString("login_email","");

        /**
         *  Offer action button,
         *  open popup activity shwoing all offer
         *  April 3, 2018
         *  Ankesh Kumar
         */

        /**
         *  Live Chat Button,
         *  Open a chat room with support team
         *  30 Aug 2018
         *  Sumit Saini
         *  offer button has been changed with livechat
         */

        FreshchatConfig freshchatConfig=new FreshchatConfig(API_ID,API_KEY);
        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);
        FreshchatUser freshUser=Freshchat.getInstance(getApplicationContext()).getUser();

        freshUser.setFirstName(fname);
        freshUser.setLastName(lname);
        freshUser.setEmail(value_email);
//        freshUser.setPhone("+91", "9790987495");

        Freshchat.getInstance(getApplicationContext()).setUser(freshUser);

        float_offer_button=(ImageView) findViewById(R.id.float_offer_button);
        float_offer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomePageActivity_v1.this,OfferPopUpActivity.class);
//                Intent intent=new Intent(HomePageActivity.this, com.galwaykart.Payment.paytm_integrate.MainActivity.class);

//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                CommonFun.finishscreen(HomePageActivity.this);

            }
        });


        float_chat_button=(ImageView) findViewById(R.id.float_chat_button);
        float_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //Intent intent=new Intent(HomePageActivity.this,OfferPopUpActivity.class);
//                Intent intent=new Intent(HomePageActivity.this, com.galwaykart.Payment.paytm_integrate.MainActivity.class);
//
//               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                //CommonFun.finishscreen(HomePageActivity.this);
                Freshchat.showConversations(getApplicationContext());
            }
        });


        dataLoad = false;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager_view_banner = (ViewPager) findViewById(R.id.pager_view_banner);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        ed_search_view = (SearchView) findViewById(R.id.search_view);
        EditText searchEditText = (EditText) ed_search_view.findViewById(androidx.appcompat.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));

        ed_search_view.setIconifiedByDefault(false);
        ed_search_view.setOnQueryTextListener(this);
        ed_search_view.setOnCloseListener(this);
        ed_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSearchProduct("");

            }
        });

       // tv_CustomerName = (TextView) findViewById(R.id.tv_CustomerName);

        cart_progressBar = (ProgressBar) findViewById(R.id.cart_progressBar);
        cart_progressBar.setVisibility(View.GONE);

//        carouselView=(CarouselView)findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);

      //  recyclerView_Product = (RecyclerView) findViewById(R.id.recyclerView_Product);
        recyclerView_Product.setNestedScrollingEnabled(false);
        recyclerView_Category = (RecyclerView) findViewById(R.id.recyclerView_Category);
        recyclerView_Category.setNestedScrollingEnabled(false);

        itemdCatList = new ArrayList<>();
        itemdProductList = new ArrayList<>();

        // list_category=(ListView) findViewById(R.id.list_category);
        //list_product=(ListView) findViewById(R.id.list_product);

        tvCategoryHead = (TextView) findViewById(R.id.tvCategoryHead);
      //  tvProductHead = (TextView) findViewById(R.id.tvProductHead);


        if (value_email != null && !value_email.equals("")) {

        }
        else
        {
            Intent intent=new Intent(HomePageActivity_v1.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(HomePageActivity_v1.this);

        }

        tokenData = pref.getString("tokenData", "");
//        fname=fname.substring(0,1);
        customer_name = fname + " " + lname;

        tv_CustomerName.setText("Hello " + fname + " ,");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tvCustomerName = (TextView) header.findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(customer_name);

        //    EditText editText = (EditText) findViewById(R.id.search_view);

        ImageView imageView = (ImageView) findViewById(R.id.image_view_title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(HomePageActivity.this,"Home Page", Toast.LENGTH_LONG).show();

            }
        });

        Button bt_show_category = (Button) findViewById(R.id.bt_show_category);
        bt_show_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity_v1.this, ShowCategoryList.class);
                intent.putExtra("onback","home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(HomePageActivity_v1.this);

            }
        });

    }

    private void openSearchProduct(String query) {
        //searchView.clearFocus();
        Intent intent=new Intent(HomePageActivity_v1.this,SearchProductActivity.class);
        intent.putExtra("searchtext",query);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(HomePageActivity_v1.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(dataLoad==false)
            callHomeItemList(Global_Settings.home_page_api);
    }

    private void callHomeItemList(String url_cart_item_list) {

        progress_bar.setVisibility(View.VISIBLE);

        final String TAG_total_item_count = "items_qty";
        cart_progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());


                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            JSONArray jsonArray_banner=jsonObj.getJSONArray("banner");

                            total_banner_item=jsonArray_banner.length();

                            if(total_banner_item>0) {
                                banner_image = new String[total_banner_item];
                                banner_image_catid= new String[total_banner_item];
                                banner_image_sku= new String[total_banner_item];

                                for (int i = 0; i < total_banner_item; i++) {

                                    JSONObject jsonObject=jsonArray_banner.getJSONObject(i);
                                     banner_image[i]=jsonObject.getString("bname");
                                    banner_image_catid[i]=jsonObject.getString("catid");
                                    banner_image_sku[i]=jsonObject.getString("sku");

                                }
                                loadData=true;

                                if(banner_image.length>0)
                                carouselData();
                                else
                                    carouselView.setVisibility(View.GONE);

                            }

                            String  st_prod_head=jsonObj.getString("product_head");
                            String  st_cat_head=jsonObj.getString("category_head");

                            if(!st_prod_head.equals(""))
                                tvProductHead.setText(st_prod_head);

                            if(!st_cat_head.equals(""))
                                tvCategoryHead.setText(st_cat_head);

                            JSONArray jsonArray_product=jsonObj.getJSONArray("product");
                            for(int i=0;i<jsonArray_product.length();i++){

                                JSONObject jsonObject_product=jsonArray_product.getJSONObject(i);
                                String pname=jsonObject_product.getString("pname");
                                String pprice=jsonObject_product.getString("price");
                                String psku=jsonObject_product.getString("sku");
                                String pimage=jsonObject_product.getString("image");

                                itemdProductList.add(new DataModelHomeProduct(pname,pprice,psku,pimage,""));
                                setProductAdapter();
                            }


                            JSONArray jsonArray_category=jsonObj.getJSONArray("category");
                            for(int i=0;i<jsonArray_category.length();i++){
                                JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);

                                String catid=jsonObject_category.getString("id");
                                String  catimage=jsonObject_category.getString("image");

                                itemdCatList.add(new DataModelHomeCategory(catid,catimage));
                                setCategoryAdapter();
                            }
                            progress_bar.setVisibility(View.GONE);

                            dataLoad=true;
                            refreshItemCount();


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

//                        ////Log.d("ERROR", "error => " + error.toString());
//                        Snackbar.make(findViewById(android.R.id.content),"Internet Connectity Not Found",Snackbar.LENGTH_LONG).show();
//                        Intent intent=new Intent(HomePageActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);
//                        CommonFun.finishscreen(HomePageActivity.this);
                        //refreshItemCount();

                        progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,HomePageActivity_v1.this);

                    }
                }
        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + tokenData);
//                params.put("Content-Type", "application/json");
//
//                return params;
//            }
        };
        queue.add(jsObjRequest);


    }




    private void setProductAdapter() {

        int spanCount = 2; // 3 columns
        int spacing = 1; // 50px
        boolean includeEdge = true;
        recyclerView_Product.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        adapter = new RecyclerViewHomeProductAdapter(HomePageActivity_v1.this,itemdProductList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView_Product.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView_Product.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Product.setAdapter(adapter);

    }


    private void setCategoryAdapter() {
       cat_adapter= new RecyclerViewHomeCategoryAdapter(HomePageActivity_v1.this,itemdCatList);

        int spanCount = 1; // 3 columns
        int spacing = 2; // 50px
        boolean includeEdge = true;
        recyclerView_Category.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        recyclerView_Category.setLayoutManager(mLayoutManager);

//        recyclerView_Category.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Category.setAdapter(cat_adapter);

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_page, menu);
//
//
//
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_add_to_cart) {
//
//            Intent intent=new Intent(this, CartItemList.class);
//            startActivity(intent);
//            CommonFun.finishscreen(HomePageActivity.this);
//
//            return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private void carouselData() {

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

        ViewPagerAdapterBanner viewPagerAdapterBanner = new ViewPagerAdapterBanner(HomePageActivity_v1.this, banner_image,banner_image_catid,banner_image_sku);

        pager_view_banner.setAdapter(viewPagerAdapterBanner);
        pageSwitcher(4);
    }



    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    public class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page == total_banner_item) { // In my case the number of pages are 5
                       page = 0;
                    } else {
                        pager_view_banner.setCurrentItem(page++);
                    }
                }
            });

        }
    }




    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

          if(loadData==false)
            imageView.setImageResource(sampleImages[position]);
            else {

              imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                          Picasso.with(HomePageActivity_v1.this)
                                  .load(banner_image[position])
                                 // .resize(200,200)
                                  .placeholder(R.drawable.product_icon)
                                  .error(R.drawable.noimage)
                                  .into(imageView);
                          imageView.setImageResource(R.drawable.logo);
                      }
        }
    };



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //openHomeActivity();

        } else if (id == R.id.nav_order) {

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
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent intent=new Intent(this, LogoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.legalabout){

            Intent intent=new Intent(this, LegalAboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }

        else if(id==R.id.address_book) {

            Intent intent = new Intent(this, UpdateAddressActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }

        else if(id==R.id.return_order){

            Intent intent=new Intent(this, ReturnedOrderList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openHomeActivity()
    {
        Intent intent=new Intent(this,HomePageActivity_v1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }


    public void refreshItemCount(){

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        tokenData=pref.getString("tokenData","");
        //  tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";

        String url_cart_item_list = Global_Settings.api_url+"rest/V1/carts/mine";
        callCartItemList(url_cart_item_list,HomePageActivity_v1.this);

    }



    private void callCartItemList(String url_cart_item_list, final Context context) {

        final String TAG_total_item_count = "items_qty";

        tokenData = tokenData.replaceAll("\"", "");

//        pDialog = new TransparentProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());
//                        CommonFun.alertError(CartItemList.this, response.toString());

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
                        cart_progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                            int total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));

                            //initNavigationDrawer();
                            updateMenuTitles(toolbar, String.valueOf(total_cart_count));

                        } catch (JSONException e) {
                            cart_progressBar.setVisibility(View.GONE);
                            //e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
                        //////Log.d("ERROR", "error => " + error.toString());
                        //CommonFun.alertError(context, error.toString());

//                        Intent intent=new Intent(BaseActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);

 //                       Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();

                        cart_progressBar.setVisibility(View.GONE);
                        if (error instanceof ParseError|| error instanceof ServerError) {
//                            Intent intent=new Intent(context, InternetConnectivityError.class);
//                            context.startActivity(intent);
                            int total_cart_count =0;
                            //initNavigationDrawer();
                            updateMenuTitles(toolbar, String.valueOf(total_cart_count));

                        }
                        else {
                            updateMenuTitles(toolbar, String.valueOf(0));
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

    private void updateMenuTitles(Toolbar toolbar,String count) {

        //MenuItem cartMenuItem = menu.findItem(R.id.action_add_to_cart);
        TextView tv_cartqty;
        tv_cartqty=(TextView)toolbar.findViewById(R.id.cart_icon);
        tv_cartqty.setText(count);
        tv_cartqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });


        ImageView imageViewIcon;
        imageViewIcon=(ImageView)toolbar.findViewById(R.id.image_view_title);
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });


    }


    public void openCart(){
        Intent intent=new Intent(this, CartItemList.class);
        // Intent intent=new Intent(this, AddressBookClass.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }


    @Override
    public boolean onClose() {

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //	listAdapter.filterData(query);
        //expandAll();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        openSearchProduct(newText);
        return false;
    }
}
