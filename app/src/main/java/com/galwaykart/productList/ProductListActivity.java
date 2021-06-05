package com.galwaykart.productList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.galwaykart.BaseActivity;
import com.galwaykart.BaseActivityCommon;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.Guest.GuestHomePageActivity;

import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.databinding.ActivityProductListBinding;
import com.galwaykart.dbfiles.Adapter.MvvmRecyclerViewProductAdapter;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.ProductDataModel;

import com.galwaykart.dbfiles.ViewModel.Mvvm_ProductViewModel;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Show Product list of the given category
 */

public class ProductListActivity extends AppCompatActivity {

     String onBack="";

    /**
     * Added by Ankesh Kumar
     * Dec 27
     */
    Spinner spinner_sort;
    Boolean is_loggedin=false;
    ActivityProductListBinding binding;
    Mvvm_ProductViewModel model;


    //Pincode_code_add
    TableRow tbl_row1,tbl_row2,tbl_row3;
    TextView btn_apply_pincode,btn_save_pincode,txt_pincode,btn_change_pincode;
    EditText et_pincode;
    String strSavePincode="";



    int i=0;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent;

        /**
         * Check the previous page
         *
         */

        if(is_loggedin==true) {
            if (onBack.equalsIgnoreCase("home")) {
                intent = new Intent(ProductListActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(this);
            } else {

                intent = new Intent(ProductListActivity.this, ShowCategoryList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                CommonFun.finishscreen(this);

            }
        }
        else
        {

                intent = new Intent(ProductListActivity.this, GuestHomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(this);

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_list);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_product_list);

        //initNavigationDrawer();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                onBack= extras.getString("onback");
            }
        }

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        model= ViewModelProviders.of(this)
                .get(Mvvm_ProductViewModel.class);

        model.init();
        showProgress();
        model.getProductOfCategory("",false).observe(this,
                new Observer<List<ProductDataModel>>() {
            @Override
            public void onChanged(@Nullable List<ProductDataModel> productDataModels) {

                if(productDataModels==null || productDataModels.isEmpty()){

                }
                else
                {
                    hideProgress();
                    changeUI(productDataModels);
                }
            }
        });


        /**
         * Sorting product dropdown
         */
        spinner_sort= findViewById(R.id.spinner_sorting);

        String[] item_sort_by;
        SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());

        String value_email = pref.getString("login_email", "");
        if(value_email!=null && !value_email.equals(""))
            is_loggedin=true;

        String login_group_id=pref.getString("login_group_id","");
        if(login_group_id.equals("4") || login_group_id.equals("8"))
        {
                //item_sort_by = new String[]{"Sort", "Sort By Name", "Sort By Price", "Sort By IP"};
            item_sort_by = new String[]{"Sort", "Sort By Name", "Sort By Price"};
        }
        else
        {
            item_sort_by = new String[]{"Sort", "Sort By Name", "Sort By Price"};
        }

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_item,item_sort_by);
            spinner_sort.setAdapter(arrayAdapter);
            spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==1) {
                       sortProduct(1);
                    }
                    else if(i==2) {
                        sortProduct(2);
                    }
                    else if(i==3) {
                        sortProduct(3);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }


            });


            arrayAdapter.notifyDataSetChanged();

        }



    /**
     * sort the product
     */
    private void sortProduct(int sortBy){

        model.sortProducts(sortBy).observe(this, new Observer<List<ProductDataModel>>() {
            @Override
            public void onChanged(@Nullable List<ProductDataModel> productDataModels) {
                changeUI(productDataModels);
            }
        });
    }







    @Override
    protected void onResume() {
        super.onResume();


    }


    private void changeUI(List<ProductDataModel> productDataModels){



        if(productDataModels.size()>0) {
          // hideProgress();

            if(!productDataModels.get(0).getPname().equals("")) {
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
                binding.setProductDetails(productDataModels.get(0));
                //Log.d("resMVVMCatName",productDataModels.get(0).p_category_name);
            }
            else
            {
                //Log.d("resMVVMCatName",productDataModels.get(0).p_category_name);
                binding.setProductDetails(productDataModels.get(0));
                //hideProgress();
                binding.tvAlert.setVisibility(View.VISIBLE);
                spinner_sort.setVisibility(View.GONE);
            }

        }
        else
        {
            //Log.d("resMVVMCatName","none");
           // binding.setProductDetails(productDataModels.get(0));
            //hideProgress();
            binding.tvAlert.setVisibility(View.VISIBLE);
            spinner_sort.setVisibility(View.GONE);
        }

    }



    private void showProgress(){
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        binding.progressBar.setVisibility(View.GONE);
    }

}
