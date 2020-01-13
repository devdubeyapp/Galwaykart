package com.galwaykart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.galwaykart.databinding.ActivitySearchProductBinding;
import com.galwaykart.dbfiles.Adapter.MvvmRecyclerViewProductAdapter;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.dbfiles.Repository.MVVM_HomePage_TopProductRepo;
import com.galwaykart.dbfiles.ViewModel.Mvvm_ProductViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import java.util.List;

/**
 * Created by ankesh on 10/3/2017.
 */

public class SearchProductActivity extends BaseActivityCommon
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    SearchView search_view;
    TransparentProgressDialog pDialog;

    String st_search_txt="";
    ActivitySearchProductBinding binding;
    Mvvm_ProductViewModel viewModel;
    Observer<List<ProductDataModel>> searchObserver;
    Observer<Boolean> isLoadingObserver;

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
       // setContentView(R.layout.activity_search_product);

       binding= DataBindingUtil.setContentView(this,R.layout.activity_search_product);
       initNavigationDrawer();

       viewModel= ViewModelProviders.of(this).get(Mvvm_ProductViewModel.class);
       viewModel.init();
//       viewModel.getSearchProduct("none").observe(this, new Observer<List<ProductDataModel>>() {
//           @Override
//           public void onChanged(List<ProductDataModel> productDataModels) {
//
//               changeUI(productDataModels);
//
//           }
//       });

        // Create the observer which updates the UI.
        searchObserver = new Observer<List<ProductDataModel>>() {
            @Override
            public void onChanged(List<ProductDataModel> productDataModels) {

               // if (productDataModels != null && !productDataModels.isEmpty()) {
                    hideProgress();
                    changeUI(productDataModels);
               // }
            }
        };


        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

       // showProgress(true);
       // viewModel.getSearchProduct("none").observe(this, searchObserver);




        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                st_search_txt= extras.getString("searchtext");
            }
        }

        EditText searchEditText = (EditText) binding.searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));


        if(!st_search_txt.equals(""))
            binding.searchView.setQuery(st_search_txt,false);


        binding.searchView.requestFocus();


        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setOnQueryTextListener(this);
        binding.searchView.setOnCloseListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        binding.searchView.clearFocus();
        query=query.trim();
        if(query.length()>2) {
           // //Log.d("mvvm","querylisten");

            showProgress();
            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//            viewModel.isDataLoading()
//                    .observe(this,isLoadingObserver);

            viewModel.getProductOfCategory(query,true)
                     .observe(this, searchObserver);


            // viewModel.getSearchProduct(query);

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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


    private void changeUI(List<ProductDataModel> productDataModels){


        if(productDataModels.size()>0) {
            // hideProgress();

            if(!productDataModels.get(0).getPname().equals("")) {

                ////Log.d("mvvm","changeui");
                int spanCount = 2; // 3 columns
                int spacing = 1; // 50px
                boolean includeEdge = true;

                binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                MvvmRecyclerViewProductAdapter recyclerViewAdapter = new MvvmRecyclerViewProductAdapter(this, productDataModels);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                binding.recyclerView.setLayoutManager(mLayoutManager);
                binding.recyclerView.setAdapter(recyclerViewAdapter);
                binding.setLifecycleOwner(this);
                binding.invalidateAll();
                binding.tvAlert.setVisibility(View.GONE);

            }
            else
            {
                ////Log.d("mvvm","changeui");
                binding.recyclerView.setAdapter(null);

                binding.setProductDetail(productDataModels.get(0));
                binding.tvAlert.setText("Product is not available");
                binding.tvAlert.setVisibility(View.VISIBLE);

            }

        }
        else
        {
            ////Log.d("resMVVMCatName","h");
            // binding.setProductDetails(productDataModels.get(0));
            //hideProgress();
            binding.tvAlert.setVisibility(View.GONE);
            binding.tvAlert.setText("Product is not available");
            //spinner_sort.setVisibility(View.GONE);
        }

    }




    private void showProgress(){
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        binding.progressBar.setVisibility(View.GONE);
    }



}
