package com.galwaykart.Guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

//import com.freshchat.consumer.sdk.Freshchat;
//import com.freshchat.consumer.sdk.FreshchatConfig;
//import com.freshchat.consumer.sdk.FreshchatUser;
//import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.galwaykart.HomePageActivity;
import com.galwaykart.HomePageTab.DataModelHomeAPI;
import com.galwaykart.Legal.CallWebUrlActivity;
import com.galwaykart.Legal.FaqActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.MovableFloatingActionButton;
import com.galwaykart.MultiStoreSelection.StateSelectionDialog;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.app_promo.AppPromotion;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.galwaykart.R;
import com.galwaykart.SearchProductActivity;
import com.galwaykart.SplashActivity;
import com.galwaykart.ViewPagerAdapterBanner;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.productList.ShowCategoryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;


public class GuestHomePageActivity extends GuestBaseActivity
        implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener
    {
    GuestHomePageTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context context;

    private int[] tabIcons = {
            R.drawable.product_icon,
            R.drawable.product_icon,
            R.drawable.product_icon,
            R.drawable.product_icon
    };


    SearchView searchView;
    String customer_name;
    //CarouselView carouselView;
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



//    private List<DataModelHomeCategory> itemdCatList;
//    private List<DataModelHomeProduct> itemdProductList;
//    private RecyclerViewHomeProductAdapter adapter;
//    private RecyclerViewHomeCategoryAdapter cat_adapter;
//    int[] sampleImages = {R.drawable.logo};
Boolean is_rel_cross_app_visible=false;
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
    ImageView float_offer_button;
    MovableFloatingActionButton float_chat_button;
    final String API_ID = "0e3c5b88-b2b2-48ee-8509-a6db6e6df6cd";
    final String API_KEY = "ef4384c8-b541-4229-8e47-becd7aa1f6ea";


    TabItem tabTopProducts,tabTopCategory,tabShopByCategory,tabOffer;

        RelativeLayout rel_cross_app_promo;

        ImageView imageView_app_galway,imageView_app_galwaykart;
        ImageView imageView_app_galwayexam,imageView_app_galwayfoundation;

        TextView tv_app_galway,tv_app_galwaykart;
        TextView tv_app_galwayexam,tv_app_galwayfoundation;

        Button btn_glazegalway,btn_galwaykart;
        Button btn_galwayexam,btn_galwayfoundation;

        TextView tv_current_zone,tv_change_zone;

        //Pincode_code_add
        TableRow tbl_row1,tbl_row2,tbl_row3;
        TextView btn_apply_pincode,btn_save_pincode,txt_pincode,btn_change_pincode;
        EditText et_pincode;
        String strSavePincode="";
        String setPinCodeByUser="";



        private void openAppPromotionDetail(String app_id){
            Intent intent=new Intent(this, AppPromotion.class);
            intent.putExtra("app_id",app_id);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }

        private boolean is_Install_Or_Not(String appname){

            PackageManager packageManager=getPackageManager();
            try {
                packageManager.getPackageInfo(appname,PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                //e.printStackTrace();
                return false;
            }


        }
        private void nav_open_click(String appname){
            if(is_Install_Or_Not(appname)) {
                //This intent will help you to launch if the package is already installed
                Intent LaunchIntent = getPackageManager()
                        .getLaunchIntentForPackage(appname);
                startActivity(LaunchIntent);

            } else {
                //  if application not installed
                //  Redirect to play store

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appname));
                    startActivity(intent);
                }
                catch (Exception ex){

                }

            }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_guest);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context=this;
        initNavigationDrawer();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_tabs);
        tabLayout = findViewById(R.id.tabLayoutss);
        tabTopProducts = findViewById(R.id.tabTopProducts);
        tabTopCategory = findViewById(R.id.tabTopCategory);
        tabShopByCategory = findViewById(R.id.tabShopByCategory);
        tabOffer = findViewById(R.id.tabOffer);

        //Pincode
        tbl_row1 = findViewById(R.id.tbl_row1);
        tbl_row2 = findViewById(R.id.tbl_row2);
        tbl_row3 = findViewById(R.id.tbl_row3);
        txt_pincode = findViewById(R.id.txt_pincode);
        et_pincode= findViewById(R.id.et_pincode);
        btn_change_pincode = findViewById(R.id.btn_change_pincode);
        btn_apply_pincode = findViewById(R.id.btn_apply_pincode);
        btn_save_pincode = findViewById(R.id.btn_save_pincode);

        SharedPreferences prefPincode;
        prefPincode=CommonFun.getPreferences(GuestHomePageActivity.this);
        setPinCodeByUser = prefPincode.getString("current_pincode_home", "");
        txt_pincode.setText("Pincode : " + setPinCodeByUser);

        if(setPinCodeByUser.equalsIgnoreCase(""))
        {
            tbl_row1.setVisibility(View.VISIBLE);
            tbl_row2.setVisibility(View.GONE);
            tbl_row3.setVisibility(View.GONE);

        }
        else
        {
            tbl_row3.setVisibility(View.VISIBLE);
            tbl_row2.setVisibility(View.GONE);
            tbl_row1.setVisibility(View.GONE);


        }


        btn_apply_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbl_row1.setVisibility(View.GONE);
                tbl_row2.setVisibility(View.VISIBLE);

            }
        });
        btn_save_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_pincode.getText().toString().trim().equalsIgnoreCase(""))
                {
                    CommonFun.alertError(GuestHomePageActivity.this,"Please enter valid pincode...");
                }
                else if(et_pincode.getText().toString().trim().length() < 6){
                    CommonFun.alertError(GuestHomePageActivity.this,"Please enter valid pincode...");
                }

                else
                {
                    strSavePincode= et_pincode.getText().toString().trim();

                    SharedPreferences prefPincode;
                    prefPincode=CommonFun.getPreferences(GuestHomePageActivity.this);
                    SharedPreferences.Editor editor=prefPincode.edit();
                    editor.putString("current_pincode_home",strSavePincode);
                    editor.commit();

                    final String setPinCodeByUser = prefPincode.getString("current_pincode_home", "");

                    txt_pincode.setText("Pincode : " + setPinCodeByUser);
                    tbl_row1.setVisibility(View.GONE);
                    tbl_row2.setVisibility(View.GONE);
                    tbl_row3.setVisibility(View.VISIBLE);
                }

            }
        });
        btn_change_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tbl_row3.setVisibility(View.GONE);
                tbl_row1.setVisibility(View.GONE);
                tbl_row2.setVisibility(View.VISIBLE);


                SharedPreferences prefPincode;
                prefPincode=CommonFun.getPreferences(GuestHomePageActivity.this);
                SharedPreferences.Editor editor=prefPincode.edit();
                editor.putString("current_pincode_home",strSavePincode);
                editor.commit();

                final String setPinCodeByUser = prefPincode.getString("current_pincode_home", "");
                txt_pincode.setText("Pincode : " + setPinCodeByUser);

            }
        });

//        rel_cross_app_promo=(RelativeLayout)findViewById(R.id.rel_cross_app_promo);
//        rel_cross_app_promo.setVisibility(View.GONE);
//        is_rel_cross_app_visible=false;
//
//
          pager_view_banner = findViewById(R.id.pager_view_bannerss);

          adapter = new GuestHomePageTabAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

          tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(GuestHomePageActivity.this,
                            R.color.colorAccent));

                } else if (tab.getPosition() == 2) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(GuestHomePageActivity.this,
                            android.R.color.darker_gray));

                } else {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(GuestHomePageActivity.this,
                            R.color.colorPrimary));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());



        SharedPreferences.Editor editor1= pref.edit();
        editor1.putString("guest_cart_id_process","");
        editor1.commit();

        String fname = pref.getString("login_fname", "");
        String lname = pref.getString("login_lname", "");

        String value_email=pref.getString("login_email","");

        if(value_email!=null && !value_email.equals(""))
        {
            Intent intent=new Intent(GuestHomePageActivity.this,SplashActivity.class);
            startActivity(intent);
            CommonFun.finishscreen(this);
        }


//        FreshchatConfig freshchatConfig = new FreshchatConfig(API_ID, API_KEY);
//        freshchatConfig.setCameraCaptureEnabled(false);
//        freshchatConfig.setGallerySelectionEnabled(false);
//        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);
//        FreshchatUser freshUser = Freshchat.getInstance(getApplicationContext()).getUser();
//
//        freshUser.setFirstName(fname);
//        freshUser.setLastName(lname);
//        freshUser.setEmail(value_email);
////        freshUser.setPhone("+91", "9790987495");
//
//        try {
//            Freshchat.getInstance(getApplicationContext()).setUser(freshUser);
//        } catch (MethodNotAllowedException e) {
//            e.printStackTrace();
//        }

        float_chat_button= findViewById(R.id.float_hchat_button);
        float_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GuestHomePageActivity.this, CallWebUrlActivity.class);
                intent.putExtra("comefrom","galwaychat");
                startActivity(intent);
//                Freshchat.showConversations(getApplicationContext());

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //TextView tvCustomerName = (TextView) header.findViewById(R.id.tvCustomerName);
        // tvCustomerName.setText(customer_name);

        //    EditText editText = (EditText) findViewById(R.id.search_view);

        ImageView imageView = findViewById(R.id.image_view_title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(GuestHomePageActivity.this,"Home Page", Toast.LENGTH_LONG).show();

            }
        });

        Button bt_show_category = findViewById(R.id.bt_show_category);
        bt_show_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuestHomePageActivity.this, ShowCategoryList.class);
                intent.putExtra("onback","home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(GuestHomePageActivity.this);

            }
        });

        ed_search_view = findViewById(R.id.search_view);
        EditText searchEditText = ed_search_view.findViewById(R.id.search_src_text);
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

        ed_search_view.clearFocus();

        String email = pref.getString("user_email", "");
        String login_group_id=pref.getString("login_group_id","");
        String home_page_api="";

        if (!email.equalsIgnoreCase("") && email != null) {

            //home_page_api=Global_Settings.home_page_api+"?cid="+login_group_id;
            home_page_api= Global_Settings.api_url+"rest/V1/mobile/home/0";
        }
        else
        {
            home_page_api=Global_Settings.home_page_api+"?cid=0";
            home_page_api= Global_Settings.api_url+"rest/V1/mobile/home/0";
        }

        LinearLayout linear_zone_layout=findViewById(R.id.linear_zone_layout);
        if(Global_Settings.multi_store==true) {

            tv_current_zone = findViewById(R.id.tv_current_zone);
            tv_change_zone = findViewById(R.id.tv_change_zone);
            tv_change_zone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(GuestHomePageActivity.this, StateSelectionDialog.class);
                    intent.putExtra("fromcome", "guest");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    CommonFun.finishscreen(GuestHomePageActivity.this);

                }
            });

            if(Global_Settings.multi_store==true) {
                String guest_current_zone = pref.getString("guest_current_zone", "");
                if (guest_current_zone != null && !guest_current_zone.equals("")) {

                    Global_Settings.current_zone = guest_current_zone;
                    Global_Settings.api_url = Global_Settings.web_url + guest_current_zone + '/';
                    tv_current_zone.setText(Global_Settings.current_zone);

                    callHomeItemList(home_page_api);
                } else {
                    getState(home_page_api);
                }
            }
            else
            {
                callHomeItemList(home_page_api);
            }
        }
        else {
            linear_zone_layout.setVisibility(View.GONE);
            callHomeItemList(home_page_api);
        }

        //callHomeItemList(home_page_api);
        //refreshItemCount();
    }


        TransparentProgressDialog pDialog;
        final String TAG_region_code= "code";
        String st_region_code="";

        private void getState(String home_page_api) {

            String st_get_State_URL=Global_Settings.web_url + "rest/V1/website/list";

            pDialog = new TransparentProgressDialog(GuestHomePageActivity.this);
            pDialog.setCancelable(true);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest jsObjRequest=null;
            try {
                jsObjRequest = new StringRequest(Request.Method.GET, st_get_State_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(pDialog.isShowing())
                                    pDialog.dismiss();
                                Log.d("responseState",response);

                                if (response != null) {
                                    try {

                                        JSONArray jsonArray=new JSONArray(response);

                                        Log.d("responseState",jsonArray.toString());



                                        JSONObject  order_list_obj = jsonArray.getJSONObject(0);

                                        st_region_code = order_list_obj.getString(TAG_region_code);

                                        Global_Settings.current_zone=st_region_code;
                                        Global_Settings.api_url=Global_Settings.web_url+st_region_code+'/';
                                        tv_current_zone.setText(Global_Settings.current_zone);

                                        SharedPreferences pref = CommonFun.getPreferences(GuestHomePageActivity.this);
                                        SharedPreferences.Editor editor=pref.edit();
                                        editor.putString("guest_zone",st_region_code);
                                        editor.commit();



                                    } catch (Exception e) {
                                        if(pDialog.isShowing())
                                            pDialog.dismiss();
                                        e.printStackTrace();
                                        // Intent intent=new Intent(StateSelectionDialog.this, ExceptionError.class);
                                        //  startActivity(intent);
                                    }
                                    callHomeItemList(home_page_api);

                                }


                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        callHomeItemList(home_page_api);
                        CommonFun.showVolleyException(error,GuestHomePageActivity.this);
                        //CommonFun.alertError(StateSelectionDialog.this,error.toString());

                        // error.printStackTrace();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            jsObjRequest.setShouldCache(false);
            RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                    1,
                    1);
            jsObjRequest.setRetryPolicy(retryPolicy);
            queue.add(jsObjRequest);




        }



        private void openSearchProduct(String query) {
        //searchView.clearFocus();
        Intent intent=new Intent(this,SearchProductActivity.class);
        intent.putExtra("searchtext",query);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }




    @Override
    protected void onResume() {
        super.onResume();
        ed_search_view.clearFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

        private void callHomeItemList(String url_cart_item_list) {

            String response="";
            Log.d("responsebanner", response.toString());

            SharedPreferences pref;
            pref=CommonFun.getPreferences(GuestHomePageActivity.this);
            response=pref.getString("homePageData","");

            JSONArray jsonArray_banner= null;
            try {
                JSONObject jsonObj = null;
                jsonObj = new JSONObject(String.valueOf(response));
                jsonArray_banner=jsonObj.getJSONArray("banners");

                //Log.d("res_res", String.valueOf(total_banner_item)+ jsonArray_banner.length());
                total_banner_item=jsonArray_banner.length();

                if(total_banner_item>0) {
                    banner_image = new String[total_banner_item];
                    banner_image_catid= new String[total_banner_item];
                    banner_image_sku= new String[total_banner_item];

                    int k=0;
                    for (int i = 0; i < jsonArray_banner.length(); i++) {
                        JSONObject jsonObject = jsonArray_banner.getJSONObject(i);

                        String is_banner_category_offer=jsonObject.getString("banner_category");

                        //if(is_banner_category_offer.equalsIgnoreCase("banner")) {
                            banner_image[k] = jsonObject.getString("banner_name");
                            banner_image_catid[k] = jsonObject.getString("cat_id");
                            banner_image_sku[k] = jsonObject.getString("sku");
                            //Log.d("total_banner_item", String.valueOf(banner_image[k]+" "));
                            k++;
                       // }
                    }

                    loadData=true;

                    if(banner_image.length>0)
                        carouselData();
                    else
                        viewPager.setVisibility(View.GONE);



                }
                viewPager.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }




//            RequestQueue queue = Volley.newRequestQueue(this);
//        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list,
//                null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ////Log.d("response", response.toString());
//
//
//                        try {
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//
////                            SharedPreferences pref;
////                            pref= CommonFun.getPreferences(getApplicationContext());
////                            if(String.valueOf(response)!=null)
////                            {
////                                SharedPreferences.Editor editor=pref.edit();
////                                editor.putString("homepage_data", String.valueOf(response));
////                                editor.commit();
////                            }
//
//                            /**Set Adapter viewpager tab
//                             *
//                             */
//
//
//                          // highLightCurrentTab(0);
//
//
//                            JSONArray jsonArray_banner=jsonObj.getJSONArray("banners");
//
//                            total_banner_item=0;
//
//
//
//                            for (int i = 0; i < jsonArray_banner.length(); i++) {
//                                JSONObject jsonObject_category=jsonArray_banner.getJSONObject(i);
//                                String is_banner_category_offer = jsonObject_category.getString("banner_category");
//                                if (is_banner_category_offer.equalsIgnoreCase("banner")) {
//                                    total_banner_item++;
//                                }
//                            }
//                            //Log.d("total_banner_item", String.valueOf(total_banner_item)+ jsonArray_banner.length());
//                            if(total_banner_item>0) {
//                                banner_image = new String[total_banner_item];
//                                banner_image_catid= new String[total_banner_item];
//                                banner_image_sku= new String[total_banner_item];
//
//                                int k=0;
//                                for (int i = 0; i < jsonArray_banner.length(); i++) {
//                                    JSONObject jsonObject = jsonArray_banner.getJSONObject(i);
//                                    String is_banner_category_offer = jsonObject.getString("banner_category");
//                                    if (is_banner_category_offer.equalsIgnoreCase("banner")) {
//                                        banner_image[k] = jsonObject.getString("banner_name");
//                                        banner_image_catid[k] = jsonObject.getString("cat_id");
//                                        banner_image_sku[k] = jsonObject.getString("sku");
//
//
//                                        //Log.d("total_banner_item", String.valueOf(banner_image[k]+" "));
//                                        k++;
//                                    }
//                                }
//
//                                loadData=true;
//
//                                if(banner_image.length>0)
//                                    carouselData();
//                                else
//                                    viewPager.setVisibility(View.GONE);
//
//                            }
//                            viewPager.setAdapter(adapter);
//                           // tabLayout.setupWithViewPager(viewPager);
//
////
////                            String  st_prod_head=jsonObj.getString("product_head");
////                            String  st_cat_head=jsonObj.getString("category_head");
////
////                            if(!st_prod_head.equals(""))
////                                tvProductHead.setText(st_prod_head);
////
////                            if(!st_cat_head.equals(""))
////                                tvCategoryHead.setText(st_cat_head);
////
////                            JSONArray jsonArray_product=jsonObj.getJSONArray("product");
////                            for(int i=0;i<jsonArray_product.length();i++){
////
////                                JSONObject jsonObject_product=jsonArray_product.getJSONObject(i);
////                                String pname=jsonObject_product.getString("pname");
////                                String pprice=jsonObject_product.getString("price");
////                                String psku=jsonObject_product.getString("sku");
////                                String pimage=jsonObject_product.getString("image");
////
////                                itemdProductList.add(new DataModelHomeProduct(pname,pprice,psku,pimage));
////                                setProductAdapter();
////                            }
////
////
////                            JSONArray jsonArray_category=jsonObj.getJSONArray("category");
////                            for(int i=0;i<jsonArray_category.length();i++){
////                                JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);
////
////                                String catid=jsonObject_category.getString("id");
////                                String  catimage=jsonObject_category.getString("image");
////
////                                itemdCatList.add(new DataModelHomeCategory(catid,catimage));
////                                setCategoryAdapter();
////                            }
////                            progress_bar.setVisibility(View.GONE);
////
////                            dataLoad=true;
//                            refreshItemCount();
//
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
////                        ////Log.d("ERROR", "error => " + error.toString());
////                        Snackbar.make(findViewById(android.R.id.content),"Internet Connectity Not Found",Snackbar.LENGTH_LONG).show();
////                        Intent intent=new Intent(HomePageActivity.this, InternetConnectivityError.class);
////                        startActivity(intent);
////                        CommonFun.finishscreen(HomePageActivity.this);
//                        //refreshItemCount();
//
//                       // progress_bar.setVisibility(View.GONE);
//                        //refreshItemCount();
//                        CommonFun.showVolleyException(error,HomePageActivity.this);
//
//                    }
//                }
//        ) {
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("Authorization", "Bearer " + tokenData);
////                params.put("Content-Type", "application/json");
////
////                return params;
////            }
//        };
//        jsObjRequest.setShouldCache(false);
//        queue.add(jsObjRequest);


        }



        private void carouselData() {

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

        ViewPagerAdapterBanner viewPagerAdapterBanner = new ViewPagerAdapterBanner(GuestHomePageActivity.this, banner_image,banner_image_catid,banner_image_sku);

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



//    public void highLightCurrentTab(int position) {
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            assert tab != null;
//            tab.setCustomView(null);
//            tab.setCustomView(adapter.getTabView(i));
//        }
//        TabLayout.Tab tab = tabLayout.getTabAt(position);
//        assert tab != null;
//        tab.setCustomView(null);
//        tab.setCustomView(adapter.getSelectedTabView(position));
//    }




//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            //openHomeActivity();
//
//        } else if (id == R.id.nav_order) {
//
//            Intent intent=new Intent(this, OrderListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//        }else if(id==R.id.nav_wishlist){
//
//            Intent intent_wishlist=new Intent(this, WishListDetails.class);
//            intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent_wishlist);
//            CommonFun.finishscreen(this);
//        }
//        else if(id==R.id.nav_ewallet){
//            Intent intent_ewallet=new Intent(this, EwalletActivity.class);
//            intent_ewallet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent_ewallet);
//            CommonFun.finishscreen(this);
//        }
//        else if(id==R.id.change_pwd){
//            Intent intent=new Intent(this, ChangePasswordActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//
//        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_logout) {
//            Intent intent=new Intent(this, LogoutActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//        }
//        else if(id==R.id.legalabout){
//
//            Intent intent=new Intent(this, LegalAboutActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//        }
//
//        else if(id==R.id.address_book) {
//
//            SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
//            String login_group_id=pref.getString("login_group_id","");
//            if(login_group_id.equals("4")) {
//
//                Intent intent = new Intent(this, UpdateAddressActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                CommonFun.finishscreen(this);
//
//            }
//            else
//            {
//                Intent intent = new Intent(this, AddNewAddress.class);
//                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                intent.putExtra("st_come_from_update","updateaddress");
//                startActivity(intent);
//                CommonFun.finishscreen(this);
//            }
//        }
//
//        else if(id==R.id.return_order){
//
//            Intent intent=new Intent(this, ReturnedOrderList.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);
//
//        }
//
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    private void openHomeActivity()
    {
        Intent intent=new Intent(this,GuestHomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }


//    public void refreshItemCount(){
//
//        SharedPreferences pref;
//        pref= CommonFun.getPreferences(getApplicationContext());
//        tokenData=pref.getString("tokenData","");
//        //  tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";
//
//        String url_cart_item_list = Global_Settings.api_url+"rest/V1/carts/mine";
//        callCartItemList(url_cart_item_list,GuestHomePageActivity.this);
//
//    }



//    private void callCartItemList(String url_cart_item_list, final Context context) {
//
//        final String TAG_total_item_count = "items_qty";
//
//        tokenData = tokenData.replaceAll("\"", "");
//
////
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ////Log.d("response", response.toString());
////                        CommonFun.alertError(CartItemList.this, response.toString());
//
////                        if(pDialog.isShowing())
////                            pDialog.dismiss();
//                        cart_progressBar.setVisibility(View.GONE);
//
//                        try {
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//                            int total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));
//
//                            //initNavigationDrawer();
//                            updateMenuTitles(toolbar, String.valueOf(total_cart_count));
//
//                        } catch (JSONException e) {
//                            cart_progressBar.setVisibility(View.GONE);
//                            //e.printStackTrace();
//                        }
//
//
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
////                        if(pDialog.isShowing())
////                            pDialog.dismiss();
//                        //////Log.d("ERROR", "error => " + error.toString());
//                        //CommonFun.alertError(context, error.toString());
//
////                        Intent intent=new Intent(BaseActivity.this, InternetConnectivityError.class);
////                        startActivity(intent);
//
//                        //                       Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
//
//                        cart_progressBar.setVisibility(View.GONE);
//                        if (error instanceof ParseError || error instanceof ServerError) {
////                            Intent intent=new Intent(context, InternetConnectivityError.class);
////                            context.startActivity(intent);
//                            int total_cart_count =0;
//                            //initNavigationDrawer();
//                            updateMenuTitles(toolbar, String.valueOf(total_cart_count));
//
//                        }
//                        else {
//                            updateMenuTitles(toolbar, String.valueOf(0));
//                            Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();
//                        }
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + tokenData);
//                // params.put("Authorization", "Bearer "  );
//                params.put("Content-Type", "application/json");
//
//                return params;
//            }
//        };
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        jsObjRequest.setShouldCache(false);
//
//        queue.add(jsObjRequest);
//
//    }

//    private void updateMenuTitles(Toolbar toolbar,String count) {
//
//        //MenuItem cartMenuItem = menu.findItem(R.id.action_add_to_cart);
//        TextView tv_cartqty;
//        tv_cartqty=(TextView)toolbar.findViewById(R.id.cart_icon);
//        tv_cartqty.setText(count);
//        tv_cartqty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openCart();
//            }
//        });
//
//
//        ImageView imageViewIcon;
//        imageViewIcon=(ImageView)toolbar.findViewById(R.id.image_view_title);
//        imageViewIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openHomeActivity();
//            }
//        });
//
//
//    }


//    public void openCart(){
//        Intent intent=new Intent(this, CartItemList.class);
//        // Intent intent=new Intent(this, AddressBookClass.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        CommonFun.finishscreen(this);
//    }


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

        //openSearchProduct(newText);
        return false;
    }


}

//
// class TabAdapter2 extends FragmentStatePagerAdapter {
//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
//    private final List<Integer> mFragmentIconList = new ArrayList<>();
//    private Context context;
//    TabAdapter2(FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//    }
//    @Override
//    public Fragment getItem(int position) {
//        return mFragmentList.get(position);
//    }
//    public void addFragment(Fragment fragment, String title, int tabIcon) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//        mFragmentIconList.add(tabIcon);
//    }
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        //return mFragmentTitleList.get(position);
//        return null;
//    }
//    @Override
//    public int getCount() {
//        return mFragmentList.size();
//    }
//    public View getTabView(final int position) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        TextView tabTextView = (TextView)view.findViewById(R.id.tabTextView);
//        tabTextView.setText(mFragmentTitleList.get(position));
//
////        tabTextView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(context,position,Toast.LENGTH_LONG).show();
////            }
////        });
//
//
//        ImageView tabImageView = (ImageView)view.findViewById(R.id.tabImageView);
//        tabImageView.setImageResource(mFragmentIconList.get(position));
//        return view;
//
//    }
//
//    public View getSelectedTabView(final int position) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        final TextView tabTextView = (TextView)view.findViewById(R.id.tabTextView);
//        tabTextView.setText(mFragmentTitleList.get(position));
//
//        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
//        ImageView tabImageView = (ImageView)view.findViewById(R.id.tabImageView);
//        tabImageView.setImageResource(mFragmentIconList.get(position));
//        tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorYellow), PorterDuff.Mode.SRC_ATOP);
//        return view;
//
//    }
//
//
//
//    public static class PagerAdapter extends FragmentStatePagerAdapter {
//        int mNumOfTabs;
//
//        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
//            super(fm);
//            this.mNumOfTabs = NumOfTabs;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            switch (position) {
//                case 0:
//                    return tab1;
//                case 1:
//                    return tab2;
//                case 2:
//                    HomePage_ShopByCategoryTab tab3 = new HomePage_ShopByCategoryTab();
//                    return tab3;
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return mNumOfTabs;
//        }
//    }
//
//
//
// }