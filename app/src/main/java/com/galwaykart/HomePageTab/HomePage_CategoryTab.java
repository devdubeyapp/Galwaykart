package com.galwaykart.HomePageTab;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.CAdapter.DataModelHomeCategory;
import com.galwaykart.CAdapter.DataModelHomeProduct;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.CAdapter.RecyclerViewHomeCategoryAdapter;
import com.galwaykart.CAdapter.RecyclerViewHomeProductAdapter;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomePage_CategoryTab extends Fragment {

    SearchView searchView;
    String customer_name;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage_categorytab_v1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences pref;
        pref = getActivity().getSharedPreferences("glazekartapp",
                getActivity().MODE_PRIVATE);

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


        dataLoad = false;

        progress_bar = (ProgressBar) getActivity().findViewById(R.id.progress_barss);
        progress_bar.setVisibility(View.GONE);

      //  ed_search_view = (SearchView)  getActivity().findViewById(R.id.search_view);
         recyclerView_Category = (RecyclerView)getActivity().findViewById(R.id.recyclerView_Categoryss);
        recyclerView_Category.setNestedScrollingEnabled(false);

        itemdCatList = new ArrayList<>();
        itemdProductList = new ArrayList<>();

        // list_category=(ListView) findViewById(R.id.list_category);
        //list_product=(ListView) findViewById(R.id.list_product);

        tvCategoryHead = (TextView) getActivity().findViewById(R.id.tvCategoryHeadss);

//        if (value_email != null && !value_email.equals("")) {
//
//        }
//        else
//        {
//            Intent intent=new Intent(getActivity(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            CommonFun.finishscreen(getActivity());
//
//        }

        tokenData = pref.getString("tokenData", "");

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences pref;
        pref= getActivity().getSharedPreferences("glazekartapp", getActivity().MODE_PRIVATE);
//        String homepage_data=pref.getString("homepage_data","");
//        if(homepage_data!=null && !homepage_data.equals(""))
//            setPostOperation(homepage_data);
//        else

        String email = pref.getString("user_email", "");
        String login_group_id=pref.getString("login_group_id","");
        String home_page_api="";

        if (!email.equalsIgnoreCase("") && email != null) {

            home_page_api=Global_Settings.home_page_api+"?cid="+login_group_id;
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/"+login_group_id;

        }
        else
        {
            home_page_api=Global_Settings.home_page_api+"?cid=0";
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/0";
        }




        callHomeItemList(home_page_api);
    }

    private void callHomeItemList(String url_cart)
    {
        String response="";
        Realm realm=Realm.getDefaultInstance();
        RealmResults<DataModelHomeAPI> results=
                realm.where(DataModelHomeAPI.class)
                        .equalTo("p_banner_category","category")
                        .findAllAsync();
        results.load();
        response=results.asJSON();

        try {
            JSONArray jsonArray_category = new JSONArray(response);
            for (int i = 0; i < jsonArray_category.length(); i++) {

                JSONObject jsonObject_category=jsonArray_category.getJSONObject(i);
                String catid = "";
                String catimage = "";

                catid = jsonObject_category.getString("p_catid");
                catimage = jsonObject_category.getString("p_image");

                itemdCatList.add(new DataModelHomeCategory(catid, catimage));

                String  st_cat_head=jsonObject_category.getString("cat_title");
                if(!st_cat_head.equals(""))
                    tvCategoryHead.setText(st_cat_head);

            }
            setCategoryAdapter();
            dataLoad=true;
        }
        catch (JSONException ex){

        }

        setCategoryAdapter();

    }



    private void setCategoryAdapter() {

        cat_adapter= new RecyclerViewHomeCategoryAdapter(getActivity(),itemdCatList);

        int spanCount = 1; // 3 columns
        int spacing = 2; // 50px
        boolean includeEdge = true;
        recyclerView_Category.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView_Category.setLayoutManager(mLayoutManager);

//      recyclerView_Category.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Category.setAdapter(cat_adapter);


    }
}
