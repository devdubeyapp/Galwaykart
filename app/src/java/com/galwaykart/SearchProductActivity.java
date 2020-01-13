package com.galwaykart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.productList.DataModel;
import com.galwaykart.productList.RecyclerViewAdapter;
import com.galwaykart.profile.OrderListActivity;
import com.galwaykart.profile.wishList.WishListDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankesh on 10/3/2017.
 */

public class SearchProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    SearchView search_view;
    Toolbar toolbar;
    TextView tv_CustomerName;
    String tokenData="";
    String customer_name;
    String search_url="";
    ListView list_product_search;


    RecyclerView recyclerView;
    private List<DataModel> arrayList;
    DataModel model;
    private RecyclerViewAdapter adapter;
    TextView tv_category_view;
    ArrayList<String> arr_product_oode;
    TransparentProgressDialog pDialog;

    TextView cart_icon;
    ProgressBar cart_progressBar;
    String st_search_txt;
    TextView tv_alert;


    String st_customer_gp_id="",st_tier_qty="",st_tier_price="";
    String st_logged_gp_id = "";
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(SearchProductActivity.this,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                st_search_txt= extras.getString("searchtext");
            }
        }


        tv_alert=(TextView)findViewById(R.id.tv_alert);
        cart_icon=(TextView)findViewById(R.id.cart_icon);
        cart_progressBar=(ProgressBar)findViewById(R.id.cart_progressBar);
         cart_icon.setVisibility(View.GONE);
        cart_progressBar.setVisibility(View.GONE);

        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());
        String fname=pref.getString("login_fname","");
        String lname=pref.getString("login_lname","");

        tokenData=pref.getString("tokenData","");
        customer_name=fname+" "+lname;

        st_logged_gp_id = pref.getString("login_group_id","");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView tvCustomerName=(TextView)header.findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(customer_name);

        //EditText editText = (EditText) findViewById(R.id.search_view);

        ImageView imageView = (ImageView)findViewById(R.id.image_view_title);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(HomePageActivity.this,"Home Page", Toast.LENGTH_LONG).show();

            }
        });

        ImageView image_View = (ImageView)findViewById(R.id.image_view_title);

        image_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHomeActivity();

            }
        });


        search_view=(SearchView) findViewById(R.id.search_view);
        EditText searchEditText = (EditText) search_view.findViewById(androidx.appcompat.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));


        if(!st_search_txt.equals(""))
            search_view.setQuery(st_search_txt,false);


            search_view.requestFocus();


        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.setOnCloseListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        arrayList.clear();
        arr_product_oode = new ArrayList<String>();


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

        }else if(id==R.id.nav_wishlist){

            Intent intent_wishlist=new Intent(this, WishListDetails.class);
            intent_wishlist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_wishlist);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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


    private void getProductCode() {

        tv_alert.setText("");
        pDialog = new TransparentProgressDialog(SearchProductActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, search_url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        ////Log.d("response",response.toString());
                        if(response!=null){


                            try {



                                JSONObject jsonObject = new JSONObject(String.valueOf(response));

                                int total_count= Integer.parseInt(jsonObject.getString("total_count"));
                                if(total_count>0)

                                {


                                    JSONArray custom_data = jsonObject.getJSONArray("items");


                                    int length_of_sku_code = custom_data.length();

                                    for (int i = 0; i < length_of_sku_code; i++) {
                                        //  id":"2166","sku":"GRP29250","name":"BODY LOTION- ALMOND & SHEA BUTTER","image":"\/h\/o\/honey-butter-body-lotion.jpg","price":25

                                        JSONObject sku_c = custom_data.getJSONObject(i);

                                        String p_sku = sku_c.getString("sku");
                                        String p_name = sku_c.getString("name");
                                        String p_price = sku_c.getString("price");
                                        String p_status=sku_c.getString("status");



                                        String p_image="";

                                        JSONArray jsonArray_custom_attrib=sku_c.getJSONArray("custom_attributes");

                                        if(jsonArray_custom_attrib.length()>0) {
                                            for (int k = 0; k < jsonArray_custom_attrib.length(); k++) {

                                                JSONObject jsonObject_image=jsonArray_custom_attrib.getJSONObject(k);

                                                String attribute_code=jsonObject_image.getString("attribute_code");
                                                if(attribute_code.equalsIgnoreCase("image")) {

                                                    p_image=jsonObject_image.getString("value");

                                                }}
                                        }


                                        //for(int m=0;m<custom_data.length();m++) {
                                            jsonObject = custom_data.getJSONObject(i);
                                            JSONArray json_custom_price = jsonObject.getJSONArray("tier_prices");

                                            // ////Log.d("MyLog",jsonArraysunject+"");


                                            int tier_price_length = json_custom_price.length();
                                            if (tier_price_length > 0) {

                                                for (int j = 0; j < tier_price_length; j++) {

                                                    JSONObject custom_obj = json_custom_price.getJSONObject(j);
                                                    st_customer_gp_id = custom_obj.getString("customer_group_id");
                                                    st_tier_qty = custom_obj.getString("qty");
                                                    st_tier_price = custom_obj.getString("value");

                                                    if (st_customer_gp_id.equalsIgnoreCase(st_logged_gp_id)) {
                                                        p_price = st_tier_price;
                                                    }
                                                }
                                            }
                                        //}
                                        if(p_status.equals("1")) {

                                            arr_product_oode.add(p_sku);
                                            arrayList.add(new DataModel(Global_Settings.image_url + p_image, p_name, p_price, p_sku,""));

                                        }
                                    }

                                    setDataArray();
                                }
                                else
                                {
                                    tv_alert.setText("No Matches found");


                                }
                                // callJSONAPIVolley();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());
                            }

                        }



                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                    CommonFun.showVolleyException(error,SearchProductActivity.this);


            }
        }){
            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsObjRequest);

    }


    private void setDataArray() {

//        arrayList.add(new DataModel(image_path,product_name,product_price,product_code));
//        arrayList.add(new DataModel(com.galwaykart.essentialClass.Global_Settings.api_url+"pub/media/catalog/product/h/o/honey-almond-body-wash.jpg",
//                "BODY WASH- ALMOND &amp; HONEY","250",product_code));
//        ////Log.d("arrayList",""+arrayList);

        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        adapter = new RecyclerViewAdapter(SearchProductActivity.this,arrayList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }




//    @Override
//    public void onItemClick(DataModel item) {
//        Toast.makeText(getApplicationContext(), item.st_tv_product_name + " is clicked", Toast.LENGTH_SHORT).show();
//
//
//    }

//    @Override
//    public boolean onClose() {
//
////        search_view.clearFocus();
////        search_view.setQuery("",false);
////
//
//        Intent intent=new Intent(SearchProductActivity.this,HomePageActivity.class);
//        //intent.putExtra("searchtext",search_view.getQuery());
//        startActivity(intent);
//        CommonFun.finishscreen(SearchProductActivity.this);
//       // search_view.clearFocus();
//        return false;
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //	listAdapter.filterData(query);
        //expandAll();
        arrayList.clear();
    // list_product_search.invalidate();
        search_view.clearFocus();
        query=query.trim();
        if(query.length()>2) {
            search_url = Global_Settings.api_url + "rest/V1/products?searchCriteria[filter_groups][0][filters][1][field]=name&searchCriteria[filter_groups][0][filters][1][value]=%25" +
                    query +
                    "%25&searchCriteria[filter_groups][0][filters][1][condition_type]=like";

            getProductCode();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //	listAdapter.filterData(query);
        //expandAll();
//        if(newText.length()>3) {
//
//
//
//        }
//        else {
//            //search_view.clearFocus();
//
//        }
        return false;
    }

    @Override
    public boolean onClose() {

        search_view.clearFocus();
        goBack();

        return false;
    }

    private void goBack(){
        Intent intent=new Intent(SearchProductActivity.this,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("searchtext",search_view.getQuery());
        startActivity(intent);
        CommonFun.finishscreen(SearchProductActivity.this);
    }
}
