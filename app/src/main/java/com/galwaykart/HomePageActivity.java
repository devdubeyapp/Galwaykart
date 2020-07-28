package com.galwaykart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.galwaykart.address_book.CustomerAddressBook;
import com.galwaykart.HomePageTab.DataModelHomeAPI;
import com.galwaykart.app_promo.AppPromoHome;
import com.galwaykart.app_promo.AppPromotion;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.helpdesksupport.simplecomplaint.SimpleComMainActivity;
import com.galwaykart.navigationDrawer.ExpandableCustomListAdapter;
import com.galwaykart.navigationDrawer.MenuModel;
import com.galwaykart.newsnotice.NoticeActivity;
import com.galwaykart.report.CouponReportActivity;
import com.galwaykart.testimonial.TestimonialActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.galwaykart.address_book.AddNewAddress;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Guest.GuestHomePageActivity;
import com.galwaykart.Guest.GuestMainActivity;
//import com.galwaykart.Guest.GuestProductListActivity;

import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import com.galwaykart.notification.NotificationListActivity;
import com.galwaykart.partialReturn.ReturnedOrderList;
import com.galwaykart.productList.ProductListActivity;
import com.galwaykart.productList.ShowCategoryList;
import com.galwaykart.profile.ChangeEmailActivity;
import com.galwaykart.profile.ChangeMobileActivity;
import com.galwaykart.profile.ChangePasswordActivity;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.wishList.WishListDetails;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

//No static method with(Landroid/content/Context;)Lcom/squareup/picasso/Picasso;
// in class Lcom/squareup/picasso/Picasso; or its super classes (declaration of 'com.squareup.picasso.Picasso' appears in /data/app/com.galwaykart-2/base.apk:classes2.dex)

public class HomePageActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    HomePageTabPageAdapter adapter;
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


    TabItem tabTopProducts,tabTopCategory,tabShopByCategory,tabOffer;

    RelativeLayout rel_cross_app_promo;

    ImageView imageView_app_galway,imageView_app_galwaykart;
    ImageView imageView_app_galwayexam,imageView_app_galwayfoundation;

    TextView tv_app_galway,tv_app_galwaykart;
    TextView tv_app_galwayexam,tv_app_galwayfoundation;

    Button btn_glazegalway,btn_galwaykart;
    Button btn_galwayexam,btn_galwayfoundation;
    Boolean is_rel_cross_app_visible=false;

 //*******************************************************************************

    ExpandableCustomListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    String dist_id="";
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
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_tabs);
        tabLayout = findViewById(R.id.tabLayoutss);
        tabTopProducts = findViewById(R.id.tabTopProducts);
        tabTopCategory = findViewById(R.id.tabTopCategory);
        tabShopByCategory = findViewById(R.id.tabShopByCategory);
        tabOffer = findViewById(R.id.tabOffer);


        cart_progressBar = findViewById(R.id.cart_progressBar);
        pager_view_banner = findViewById(R.id.pager_view_bannerss);

        adapter = new HomePageTabPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this,
                            R.color.colorAccent));

                } else if (tab.getPosition() == 2) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this,
                            android.R.color.darker_gray));

                } else {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this,
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

        String fname = pref.getString("login_fname", "");
        String lname = pref.getString("login_lname", "");

        String value_email = pref.getString("login_email", "");

        FreshchatConfig freshchatConfig = new FreshchatConfig(API_ID, API_KEY);
        freshchatConfig.setCameraCaptureEnabled(false);
        freshchatConfig.setGallerySelectionEnabled(false);
        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);
        FreshchatUser freshUser = Freshchat.getInstance(getApplicationContext()).getUser();


        freshUser.setFirstName(fname);
        freshUser.setLastName(lname);
        freshUser.setEmail(value_email);


        try {
            Freshchat.getInstance(getApplicationContext()).setUser(freshUser);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }

        float_chat_button = findViewById(R.id.float_hchat_button);
        float_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Freshchat.showConversations(getApplicationContext());
            }
        });


        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

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
            tvCustomerName.setText(fname + " "+ lname +" \n("+dist_id+")");
        else
            tvCustomerName.setText(fname + " "+ lname);

        Menu menu = navigationView.getMenu();
        MenuItem verinfo = menu.findItem(R.id.version);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            verinfo.setTitle("Version - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //    EditText editText = (EditText) findViewById(R.id.search_view);

        ImageView imageView = findViewById(R.id.image_view_title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(HomePageActivity.this,"Home Page", Toast.LENGTH_LONG).show();

            }
        });

        Button bt_show_category = findViewById(R.id.bt_show_category);
        bt_show_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, ShowCategoryList.class);
                intent.putExtra("onback", "home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(HomePageActivity.this);

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


        Boolean is_logged_in_user=false;
        if(value_email!=null && !value_email.equals(""))
            is_logged_in_user=true;



        if(is_logged_in_user==true) {
            app_deep_link(is_logged_in_user);


            String email = pref.getString("user_email", "");
            String login_group_id=pref.getString("login_group_id","");
            String home_page_api="";

            if (!email.equalsIgnoreCase("") && email != null) {

                home_page_api=Global_Settings.home_page_api+"?cid="+login_group_id;
                home_page_api= Global_Settings.api_url+"rest/V1/mobile/home/"+login_group_id;

            }
            else
            {
                home_page_api=Global_Settings.home_page_api+"?cid=0";
                home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/0";
            }
            callHomeItemList(home_page_api);
        }
        else
        {
            Intent intent = new Intent(HomePageActivity.this, GuestHomePageActivity.class);
            startActivity(intent);
            CommonFun.finishscreen(HomePageActivity.this);
        }

        //checkForUpdate();

    }


    private int MY_REQUEST_CODE=459865;
    private void checkForUpdate()
    {

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                //log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
                            Intent intent=new Intent(HomePageActivity.this, HomePageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(HomePageActivity.this);
                        }else if(groupPosition == 1){
                            Intent intent=new Intent(HomePageActivity.this, OrderListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(HomePageActivity.this);
                        }else if(groupPosition == 2){
                            Intent intent=new Intent(HomePageActivity.this, NotificationListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(HomePageActivity.this);
                        }
                        else if(groupPosition==4)
                        {
                            Intent intent_wishlist=new Intent(HomePageActivity.this, WebViewActivity.class);
                            intent_wishlist.putExtra("comefrom","customer-help-desk-tutorials.html");
                            intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_wishlist);
                            CommonFun.finishscreen(HomePageActivity.this);
                        }
                        else if(groupPosition == 6){
                            Intent intent=new Intent(HomePageActivity.this, TestimonialActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(HomePageActivity.this);
                        }
                        else if(groupPosition == 7){
                            Intent intent=new Intent(HomePageActivity.this, NoticeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(HomePageActivity.this);
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
                        Intent intent_wishlist=new Intent(HomePageActivity.this, WishListDetails.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }

                    if(groupPosition==3 && childPosition == 1){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, SimpleComMainActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }


                    else if(groupPosition==3 && childPosition == 2){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, MyComplaints.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 3){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, CouponReportActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 4){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, ChangeMobileActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 5){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, ChangePasswordActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 6){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, ChangeEmailActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }
                    else if(groupPosition==3 && childPosition == 7){
                        SharedPreferences pref;
                        pref= CommonFun.getPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("st_come_from_update","updateaddress");
                        editor.commit();

                        Intent intent = new Intent(HomePageActivity.this, CustomerAddressBook.class);
                        intent.putExtra("st_come_from_update","updateaddress");

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        CommonFun.finishscreen(HomePageActivity.this);

                    }
                    else if(groupPosition==3 && childPosition == 8){
                        alertMsg();
                    }
                    /*else if(groupPosition==4 && childPosition == 0)
                    {
                        Intent intent_wishlist=new Intent(HomePageActivity.this, WebViewActivity.class);
                        intent_wishlist.putExtra("comefrom","customer-help-desk-tutorials.html");
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }*/
                    else if(groupPosition==5 && childPosition == 0){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, LegalAboutActivity.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }else if(groupPosition==5 && childPosition == 1){
                        Intent intent_wishlist=new Intent(HomePageActivity.this, AppPromoHome.class);
                        intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_wishlist);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }else if(groupPosition==5 && childPosition == 3){
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

        childModel = new MenuModel("Request Simple Help", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("My Help", false, false);
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
            //Log.d("API123","here");
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel("Customer HelpDesk", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Settings", true, true);
        headerList.add(menuModel);
        List<MenuModel> childModelsSettingList = new ArrayList<>();

           /* childModel = new MenuModel("Customer HelpDesk", false, false);
            childModelsSettingList.add(childModel);*/

        childModel = new MenuModel("Legal and About", false, false);
        childModelsSettingList.add(childModel);

        childModel = new MenuModel("Other Galway App", false, false);
        childModelsSettingList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsSettingList);
        }
        menuModel = new MenuModel("Testimonial", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("News", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        String version="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        menuModel = new MenuModel("Version-"+version, true, false);
        headerList.add(menuModel);



    }

    private void alertMsg() {
        String errmsg = "Are you sure want to logout";
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(this);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                    Intent intent_wishlist=new Intent(HomePageActivity.this, LogoutActivity.class);
                    intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent_wishlist);
                    CommonFun.finishscreen(HomePageActivity.this);


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


    private void app_deep_link(Boolean is_logged_in_user) {

        try {
            SharedPreferences pref;

            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();

            Intent intent = null;
            pref = CommonFun.getPreferences(getApplicationContext());

            if (appLinkIntent.getData() != null && !appLinkData.equals("")) {
                if (appLinkData != null && !appLinkData.equals("")) {
                    String url_link = appLinkData.toString();

                    url_link = url_link.replaceAll("https://www.galwaykart.com/", "");
                    int l = url_link.indexOf("-");
                    String product_id = url_link.substring(0, l);

                    l++;
                    String gp_id = url_link.substring(l, l + 2);

                    SharedPreferences.Editor editor = pref.edit();
                    if (gp_id.equalsIgnoreCase("gp")) {
                        editor.putString("showitemsku", product_id);
                        intent = new Intent(HomePageActivity.this, is_logged_in_user ? MainActivity.class : GuestMainActivity.class);
                        editor.commit();
                        startActivity(intent);
                        CommonFun.finishscreen(HomePageActivity.this);
                    } else if (gp_id.equalsIgnoreCase("gc")) {
                        editor.putString("selected_id", product_id);
                        intent = new Intent(HomePageActivity.this,  ProductListActivity.class );
                        editor.commit();
                        startActivity(intent);
                        CommonFun.finishscreen(HomePageActivity.this);
                    }


                }

            }
        }
        catch (StringIndexOutOfBoundsException ex){

        }
        catch (IllegalArgumentException ex){

        }




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


        Realm realm = Realm.getDefaultInstance(); // opens "gkart.realm"
        try {
            RealmResults<DataModelHomeAPI> results =
                    realm.where(DataModelHomeAPI.class)
                            .equalTo("p_banner_category","banner")
                            .findAllAsync();

            //fetching the data
            results.load();
            total_banner_item=results.size();
            response = results.asJSON();
            //Log.d("res_res", response);
        }
        finally {
            realm.close();
        }


        JSONArray jsonArray_banner= null;
        try {
            jsonArray_banner = new JSONArray(response);

            //Log.d("res_res", String.valueOf(total_banner_item)+ jsonArray_banner.length());

            if(total_banner_item>0) {
                banner_image = new String[total_banner_item];
                banner_image_catid= new String[total_banner_item];
                banner_image_sku= new String[total_banner_item];

                int k=0;
                for (int i = 0; i < jsonArray_banner.length(); i++) {
                    JSONObject jsonObject = jsonArray_banner.getJSONObject(i);
                        banner_image[k] = jsonObject.getString("p_image");
                        banner_image_catid[k] = jsonObject.getString("p_catid");
                        banner_image_sku[k] = jsonObject.getString("p_sku");
                        //Log.d("total_banner_item", String.valueOf(banner_image[k]+" "));
                        k++;
                }

                loadData=true;

                if(banner_image.length>0)
                    carouselData();
                else
                    viewPager.setVisibility(View.GONE);

                refreshItemCount();

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

        ViewPagerAdapterBanner viewPagerAdapterBanner = new ViewPagerAdapterBanner(HomePageActivity.this, banner_image,banner_image_catid,banner_image_sku);

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




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //openHomeActivity();

        } /*else if (id == R.id.nav_order) {

            Intent intent=new Intent(this, OrderListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }
        else if(id==R.id.other_glaze_app){
            Intent intent=new Intent(this, AppPromoHome.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }*/
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

        else if(id==R.id.nav_change_mobile){
            Intent intent=new Intent(this, ChangeMobileActivity.class);
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
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_notification) {
            Intent intent=new Intent(this, NotificationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }


        else if (id == R.id.nav_logout) {
            Intent intent=new Intent(this, LogoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CommonFun.finishscreen(this);

        }


        else if(id==R.id.customerlearningPoint)
        {
            Intent intent=new Intent(this,WebViewActivity.class);
            intent.putExtra("comefrom","customer-help-desk-tutorials.html");
            startActivity(intent);
            //CommonFun.finishscreen(this);
        }
        else if(id==R.id.legalabout){
            Intent intent=new Intent(this, LegalAboutActivity.class);
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
        else if(id==R.id.version){




        }

        else if(id==R.id.address_book) {

//            Intent intent = new Intent(this, AddNewAddress.class);
//            intent.putExtra("st_come_from_update","updateaddress");
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(this);

            SharedPreferences pref;
            pref= CommonFun.getPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("st_come_from_update","updateaddress");
            editor.commit();

            Intent intent = new Intent(this, AddNewAddress.class);
//            intent.putExtra("st_come_from_update","updateaddress");

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
    private void openHomeActivity()
    {
        Intent intent=new Intent(this,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }





    public void refreshItemCount(){

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        tokenData=pref.getString("tokenData","");
        //  tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";

       getCartId_v1();
    }

    SharedPreferences pref;
    private void getCartId_v1() {

        pref= CommonFun.getPreferences(getApplicationContext());

        String st_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine";
        RequestQueue queue = Volley.newRequestQueue(HomePageActivity.this);



        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST,
                st_cart_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                            //Log.d("onCartResponse", response.toString());
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


                            String cart_id = response;
                            cart_id = cart_id.replaceAll("\"", "");

                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("guest_cart_id",cart_id);
                            editor.commit();


                            String url_cart_item_list = Global_Settings.api_url+"rest/V1/m-carts/mine";
                            callCartItemList(url_cart_item_list,HomePageActivity.this);


                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFun.alertError(HomePageActivity.this, e.toString());
                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

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
                                    CommonFun.alertError(HomePageActivity.this,st_msg);
//                                //Log.d("st_code",st_code);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    CommonFun.showVolleyException(error, HomePageActivity.this);
                                }


                            }

                        } else
                            CommonFun.showVolleyException(error, HomePageActivity.this);

                        //////Log.d("ERROR","error => "+error.toString());
                        //CommonFun.alertError(MainActivity.this,error.toString());
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



    private void callCartItemList(String url_cart_item_list, final Context context) {


        //Log.d("log_test",url_cart_item_list);
        final String TAG_total_item_count = "items_qty";

        tokenData = tokenData.replaceAll("\"", "");


        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());
//                        CommonFun.alertError(CartItemList.this, response.toString());

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();

                        //Log.d("log_test",response.toString());
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

//                        Intent intent=new Intent(HomePageActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);

                        //                       Snackbar.make(findViewById(android.R.id.content),"Unable to Fetch Cart\nCheck Your Internet Connectivity",Snackbar.LENGTH_LONG).show();

                        cart_progressBar.setVisibility(View.GONE);
                        if (error instanceof ParseError || error instanceof ServerError) {
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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);

        queue.add(jsObjRequest);

    }

    private void updateMenuTitles(Toolbar toolbar,String count) {

        //MenuItem cartMenuItem = menu.findItem(R.id.action_add_to_cart);
        TextView tv_cartqty;
        tv_cartqty= toolbar.findViewById(R.id.cart_icon);
        tv_cartqty.setText(count);
        tv_cartqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });


        ImageView imageViewIcon;
        imageViewIcon= toolbar.findViewById(R.id.image_view_title);
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

        //openSearchProduct(newText);
        return false;
    }


}



