package com.galwaykart.HomePageTab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import com.galwaykart.CAdapter.RecyclerViewHomeTopProductAdapter;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Login.LoginActivity;
import com.galwaykart.OfferPopUpActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.productList.ShowCategoryList;
import com.synnapps.carouselview.CarouselView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HomePage_TopProductTab extends Fragment {


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
    private RecyclerViewHomeTopProductAdapter adapter;

    int[] sampleImages = {R.drawable.logo};

    Boolean loadData=false;
    TextView tv_CustomerName;
    String tokenData="";
    Toolbar toolbar;
    ProgressBar cart_progressBar;
    SearchView ed_search_view;

    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progress_bar;
    Boolean dataLoad=false;
    ViewPager pager_view_banner;
    Timer timer;
    int page = 0;
    ImageView float_offer_button,float_chat_button;
    final String API_ID = "0e3c5b88-b2b2-48ee-8509-a6db6e6df6cd";
    final String API_KEY = "ef4384c8-b541-4229-8e47-becd7aa1f6ea";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage_topproducttab_v1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_Product = (RecyclerView)getActivity().findViewById(R.id.recyclerView_Productss);
        recyclerView_Product.setNestedScrollingEnabled(false);

        itemdProductList = new ArrayList<>();

        tvProductHead = (TextView)getActivity().findViewById(R.id.tvProductHeads);
        progress_bar = (ProgressBar)getActivity().findViewById(R.id.progress_barss);
        progress_bar.setVisibility(View.GONE);

        SharedPreferences pref;
        pref= getActivity().getSharedPreferences("glazekartapp", getActivity().MODE_PRIVATE);
        String homepage_data=pref.getString("homepage_data","");
        if(homepage_data!=null && !homepage_data.equals(""))
            setPostOperation(homepage_data);
        else
            callHomeItemList(Global_Settings.home_page_api);
    }


    private void callHomeItemList(String url_cart_item_list) {

        progress_bar.setVisibility(View.VISIBLE);

        final String TAG_total_item_count = "items_qty";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response", response.toString());

                        setPostOperation(response.toString());

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
                        CommonFun.showVolleyException(error,getActivity());

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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);


    }

    private void setPostOperation(String response) {
        progress_bar.setVisibility(View.GONE);

        try {
            JSONObject jsonObj = null;
            jsonObj = new JSONObject(String.valueOf(response));

            JSONArray jsonArray_banner=jsonObj.getJSONArray("banner");

//                            total_banner_item=jsonArray_banner.length();
//
//                            if(total_banner_item>0) {
//                                banner_image = new String[total_banner_item];
//                                banner_image_catid= new String[total_banner_item];
//                                banner_image_sku= new String[total_banner_item];
//
//                                for (int i = 0; i < total_banner_item; i++) {
//
//                                    JSONObject jsonObject=jsonArray_banner.getJSONObject(i);
//                                    banner_image[i]=jsonObject.getString("bname");
//                                    banner_image_catid[i]=jsonObject.getString("catid");
//                                    banner_image_sku[i]=jsonObject.getString("sku");
//
//                                }
//                                loadData=true;
//
//                                if(banner_image.length>0)
//                                    carouselData();
//                                else
//                                    carouselView.setVisibility(View.GONE);
//
//                            }

            String  st_prod_head=jsonObj.getString("product_head");
            String  st_cat_head=jsonObj.getString("category_head");

            if(!st_prod_head.equals(""))
                tvProductHead.setText(st_prod_head);

//                            if(!st_cat_head.equals(""))
//                                tvCategoryHead.setText(st_cat_head);

            JSONArray jsonArray_product=jsonObj.getJSONArray("product");
            for(int i=0;i<jsonArray_product.length();i++){

                JSONObject jsonObject_product=jsonArray_product.getJSONObject(i);
                String pname=jsonObject_product.getString("pname");
                String pprice=jsonObject_product.getString("price");
                String psku=jsonObject_product.getString("sku");
                String pimage=jsonObject_product.getString("image");
                String pip=jsonObject_product.getString("ip");


                itemdProductList.add(new DataModelHomeProduct(pname,pprice,psku,pimage,"IP "+pip));
                setProductAdapter();
            }

//
//                            JSONArray jsonArray_category=jsonObj.getJSONArray("category");
//                            for(int i=0;i<jsonArray_category.length();i++){
//                                JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);
//
//                                String catid=jsonObject_category.getString("id");
//                                String  catimage=jsonObject_category.getString("image");
//
////                                itemdCatList.add(new DataModelHomeCategory(catid,catimage));
//                   //             setCategoryAdapter();
//                            }
            progress_bar.setVisibility(View.GONE);

           // dataLoad=true;
            //refreshItemCount();


        } catch (JSONException e) {
            progress_bar.setVisibility(View.GONE);

            e.printStackTrace();
        }
    }


    private void setProductAdapter() {

        int spanCount = 2; // 3 columns
        int spacing = 1; // 50px
        boolean includeEdge = true;
        recyclerView_Product.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        adapter = new RecyclerViewHomeTopProductAdapter(getActivity(),itemdProductList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView_Product.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView_Product.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Product.setAdapter(adapter);
        progress_bar.setVisibility(View.GONE);

    }




}

