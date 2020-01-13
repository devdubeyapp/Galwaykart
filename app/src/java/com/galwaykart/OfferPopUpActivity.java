package com.galwaykart;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.CAdapter.RecyclerViewHomeCategoryAdapter;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankesh on 4/3/2018.
 */

public class OfferPopUpActivity extends Activity {


    RecyclerView recyclerView_offer_product;
    private RecyclerViewHomeCategoryAdapter cat_adapter;
    private List<DataModelHomeCategory> itemdCatList;
    ProgressBar progress_bar;
    ImageView imageView_close;
    String json_url=Global_Settings.terms_url_api +"offerbanners.php";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.offer_homepage_layout);

        itemdCatList = new ArrayList<>();
//        llayout_banner=(LinearLayout)findViewById(R.id.llayout_banner);
//        ImageView imageView=new ImageView(this);
//        imageView.setBackground(getDrawable(R.drawable.customer));
//        imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,160));
//        imageView.setMaxHeight(80);
//        imageView.setMaxWidth(60);
//
//        llayout_banner.addView(imageView);

        recyclerView_offer_product=(RecyclerView)findViewById(R.id.recyclerView_offer_product);

        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        imageView_close=(ImageView)findViewById(R.id.iv_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CommonFun.finishscreen(OfferPopUpActivity.this);
            }
        });

        callHomeItemList(Global_Settings.home_page_api);
       // recyclerView_offer_product.setAdapter(cat_adapter);

    }

    private void setCategoryAdapter() {
        cat_adapter= new RecyclerViewHomeCategoryAdapter(OfferPopUpActivity.this,itemdCatList);

        int spanCount = 1; // 3 columns
        int spacing = 2; // 50px
        boolean includeEdge = true;
        recyclerView_offer_product.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        recyclerView_offer_product.setLayoutManager(mLayoutManager);

//        recyclerView_Category.setItemAnimator(new DefaultItemAnimator());
        recyclerView_offer_product.setAdapter(cat_adapter);

    }


    private void callHomeItemList(String url_cart_item_list) {

        progress_bar.setVisibility(View.VISIBLE);
        final String TAG_total_item_count = "items_qty";

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, json_url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.d("response", response.toString());


                        try {
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(String.valueOf(response));

                         //   JSONArray jsonArray_banner=jsonObj.getJSONArray("banners");



                            JSONArray jsonArray_category=jsonObj.getJSONArray("banners");
                            for(int i=0;i<jsonArray_category.length();i++){
                                //JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);

                                String jsonObject_category=jsonArray_category.getString(i);

                                String catid="";
                                String catimage=jsonObject_category.toString();
                                //String catid=jsonObject_category.getString("id");
                                //String  catimage=jsonObject_category.getString("image");

                                itemdCatList.add(new DataModelHomeCategory(catid,catimage));
                                setCategoryAdapter();
                                progress_bar.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            progress_bar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        progress_bar.setVisibility(View.GONE);
//                        ////Log.d("ERROR", "error => " + error.toString());
//                        Snackbar.make(findViewById(android.R.id.content),"Internet Connectity Not Found",Snackbar.LENGTH_LONG).show();
//                        Intent intent=new Intent(HomePageActivity.this, InternetConnectivityError.class);
//                        startActivity(intent);
//                        CommonFun.finishscreen(HomePageActivity.this);
                        //refreshItemCount();

                    //    progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,OfferPopUpActivity.this);

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


}
