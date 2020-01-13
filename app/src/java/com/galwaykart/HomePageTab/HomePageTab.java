package com.galwaykart.HomePageTab;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.CAdapter.DataModelHomeProduct;
import com.galwaykart.CAdapter.RecyclerViewHomeCategoryAdapter;
import com.galwaykart.CAdapter.RecyclerViewHomeProductAdapter;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.EwalletActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;
import com.galwaykart.ViewPagerAdapterBanner;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.partialReturn.ReturnedOrderList;
import com.galwaykart.productList.ShowCategoryList;
import com.galwaykart.profile.ChangePasswordActivity;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.UpdateAddressActivity;
import com.galwaykart.profile.wishList.WishListDetails;
import com.synnapps.carouselview.CarouselView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePageTab extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private TabAdapter adapter;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        context=this;
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager(),this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter.addFragment(new HomePage_TopProductTab(), "Top Product",tabIcons[0]);
        adapter.addFragment(new HomePage_CategoryTab(), "Top Category",tabIcons[1]);
        adapter.addFragment(new HomePage_ShopByCategoryTab(), "Shop By Category",tabIcons[2]);
        adapter.addFragment(new HomePage_OfferTab(), "Offer",tabIcons[3]);cart_progressBar = (ProgressBar) findViewById(R.id.cart_progressBar);
        pager_view_banner = (ViewPager) findViewById(R.id.pager_view_bannerss);


//        TabLayout.Tab firstTab = tabLayout.newTab();
//        firstTab.setText("Top Category"); // set the Text for the first Tab
//
//        firstTab.setIcon(R.drawable.ic_launcher); // set an icon for the
//        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
//
//        TabLayout.Tab secondTab = tabLayout.newTab();
//        secondTab.setText("Top Product"); // set the Text for the second Tab
//        secondTab.setIcon(R.drawable.ic_launcher); // set an icon for the second tab
//        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
//
//        TabLayout.Tab thirdTab = tabLayout.newTab();
//        thirdTab.setText("Shop by Category"); // set the Text for the first Tab
//        thirdTab.setIcon(R.drawable.ic_launcher); // set an icon for the first tab
//        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

//        TabAdapter.PagerAdapter adapter = new TabAdapter.PagerAdapter
//                (getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//// addOnPageChangeListener event change the tab on slide
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


 //perform setOnTabSelectedListener event on TabLayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new HomePage_TopProductTab();
                        break;
                    case 1:
                        fragment = new HomePage_CategoryTab();
                        break;
                    case 2:
                        fragment = new HomePage_ShopByCategoryTab();
                        break;
                    case 3:
                        fragment=new HomePage_OfferTab();
                        break;
                }

//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.simpleFrameLayout, fragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        highLightCurrentTab(0);


//       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//           @Override
//           public void onTabSelected(TabLayout.Tab tab) {
//
//           }
//
//           @Override
//           public void onTabUnselected(TabLayout.Tab tab) {
//
//           }
//
//           @Override
//           public void onTabReselected(TabLayout.Tab tab) {
//
//           }
//       });





        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //TextView tvCustomerName = (TextView) header.findViewById(R.id.tvCustomerName);
       // tvCustomerName.setText(customer_name);

        //    EditText editText = (EditText) findViewById(R.id.search_view);

        ImageView imageView = (ImageView) findViewById(R.id.image_view_title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(HomePageTab.this,"Home Page", Toast.LENGTH_LONG).show();

            }
        });

        Button bt_show_category = (Button) findViewById(R.id.bt_show_category);
        bt_show_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageTab.this, ShowCategoryList.class);
                intent.putExtra("onback","home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(HomePageTab.this);

            }
        });



        callHomeItemList(Global_Settings.home_page_api);
        //refreshItemCount();
    }




    private void callHomeItemList(String url_cart_item_list) {

//        progress_bar.setVisibility(View.VISIBLE);

        final String TAG_total_item_count = "items_qty";
    //    cart_progressBar.setVisibility(View.VISIBLE);

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
                                    viewPager.setVisibility(View.GONE);

                            }
//
//                            String  st_prod_head=jsonObj.getString("product_head");
//                            String  st_cat_head=jsonObj.getString("category_head");
//
//                            if(!st_prod_head.equals(""))
//                                tvProductHead.setText(st_prod_head);
//
//                            if(!st_cat_head.equals(""))
//                                tvCategoryHead.setText(st_cat_head);
//
//                            JSONArray jsonArray_product=jsonObj.getJSONArray("product");
//                            for(int i=0;i<jsonArray_product.length();i++){
//
//                                JSONObject jsonObject_product=jsonArray_product.getJSONObject(i);
//                                String pname=jsonObject_product.getString("pname");
//                                String pprice=jsonObject_product.getString("price");
//                                String psku=jsonObject_product.getString("sku");
//                                String pimage=jsonObject_product.getString("image");
//
//                                itemdProductList.add(new DataModelHomeProduct(pname,pprice,psku,pimage));
//                                setProductAdapter();
//                            }
//
//
//                            JSONArray jsonArray_category=jsonObj.getJSONArray("category");
//                            for(int i=0;i<jsonArray_category.length();i++){
//                                JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);
//
//                                String catid=jsonObject_category.getString("id");
//                                String  catimage=jsonObject_category.getString("image");
//
//                                itemdCatList.add(new DataModelHomeCategory(catid,catimage));
//                                setCategoryAdapter();
//                            }
//                            progress_bar.setVisibility(View.GONE);
//
//                            dataLoad=true;
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
                        CommonFun.showVolleyException(error,HomePageTab.this);

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




    private void carouselData() {

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

//        carouselView.setPageCount(banner_image.length);
//        carouselView.setImageListener(imageListener);

        ViewPagerAdapterBanner viewPagerAdapterBanner = new ViewPagerAdapterBanner(HomePageTab.this, banner_image,banner_image_catid,banner_image_sku);

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



    public void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }




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
        Intent intent=new Intent(this,HomePageTab.class);
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
        callCartItemList(url_cart_item_list,HomePageTab.this);

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
//                        cart_progressBar.setVisibility(View.GONE);

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

        //openSearchProduct(newText);
        return false;
    }


}



    class TabAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> mFragmentIconList = new ArrayList<>();
        private Context context;
        TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        public void addFragment(Fragment fragment, String title, int tabIcon) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentIconList.add(tabIcon);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public View getTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tabTextView = (TextView)view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));

            ImageView tabImageView = (ImageView)view.findViewById(R.id.tabImageView);
            tabImageView.setImageResource(mFragmentIconList.get(position));
            return view;
        }
        public View getSelectedTabView(final int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            final TextView tabTextView = (TextView)view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));

            tabTextView.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
            ImageView tabImageView = (ImageView)view.findViewById(R.id.tabImageView);
            tabImageView.setImageResource(mFragmentIconList.get(position));
            tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorYellow), PorterDuff.Mode.SRC_ATOP);
            return view;
        }



        public static class PagerAdapter extends FragmentStatePagerAdapter {
            int mNumOfTabs;

            public PagerAdapter(FragmentManager fm, int NumOfTabs) {
                super(fm);
                this.mNumOfTabs = NumOfTabs;
            }

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        HomePage_TopProductTab tab1 = new HomePage_TopProductTab();
                        return tab1;
                    case 1:
                        HomePage_TopProductTab tab2 = new HomePage_TopProductTab();
                        return tab2;
                    case 2:
                        HomePage_ShopByCategoryTab tab3 = new HomePage_ShopByCategoryTab();
                        return tab3;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return mNumOfTabs;
            }
        }
}