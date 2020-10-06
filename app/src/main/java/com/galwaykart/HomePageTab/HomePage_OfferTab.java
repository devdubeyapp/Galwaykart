package com.galwaykart.HomePageTab;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomePage_OfferTab extends Fragment {

    RecyclerView recyclerView_offer_product;
    private RecyclerViewOfferCategoryAdapter cat_adapter;
    private List<DataModelHomeCategory> itemdCatList;
    ProgressBar progress_bar;
    ImageView imageView_close;
    String json_url= Global_Settings.webview_api +"offerbanners.php";
    ImageView iv_image_no_details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offer_homepage_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        itemdCatList = new ArrayList<>();

        recyclerView_offer_product= getActivity().findViewById(R.id.recyclerView_offer_product);

        iv_image_no_details= getActivity().findViewById(R.id.iv_image_no_details);
        iv_image_no_details.setVisibility(View.GONE);

        progress_bar= getActivity().findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        imageView_close= getActivity().findViewById(R.id.iv_close);
        imageView_close.setVisibility(View.GONE);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFun.finishscreen(getActivity());
            }
        });


        callHomeItemList("");


    }

    private void setCategoryAdapter() {
        if(itemdCatList.size()>0) {
            cat_adapter = new RecyclerViewOfferCategoryAdapter(getActivity(), itemdCatList);

            int spanCount = 1; // 3 columns
            int spacing = 2; // 50px
            boolean includeEdge = true;
            recyclerView_offer_product.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recyclerView_offer_product.setLayoutManager(mLayoutManager);

//        recyclerView_Category.setItemAnimator(new DefaultItemAnimator());
            recyclerView_offer_product.setAdapter(cat_adapter);
            recyclerView_offer_product.setVisibility(View.VISIBLE);
            iv_image_no_details.setVisibility(View.GONE);
        }
        else
        {
            recyclerView_offer_product.setVisibility(View.GONE);
            iv_image_no_details.setVisibility(View.VISIBLE);
        }

    }


    private void callHomeItemList(String url_cart){

        String response="";
        SharedPreferences pref=CommonFun.getPreferences(getActivity());
        response=pref.getString("homePageData","");



        try {
            JSONObject jsonObj = null;
            jsonObj = new JSONObject(String.valueOf(response));
            JSONArray jsonArray_banner=jsonObj.getJSONArray("banners");
            for (int i = 0; i < jsonArray_banner.length(); i++) {

                JSONObject jsonObject_category=jsonArray_banner.getJSONObject(i);
                String catid = "";
                String catimage = "";
                String is_banner_category_offer=jsonObject_category.getString("banner_category");
                if(is_banner_category_offer.equalsIgnoreCase("offer")) {

                    catid = jsonObject_category.getString("cat_id");
                    catimage = jsonObject_category.getString("banner_name");

                    itemdCatList.add(new DataModelHomeCategory(catid, catimage));
                }
            }
        }
        catch (JSONException ex){

        }

        setCategoryAdapter();




    }


    }

