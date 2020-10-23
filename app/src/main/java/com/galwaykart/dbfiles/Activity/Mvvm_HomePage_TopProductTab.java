package com.galwaykart.dbfiles.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.R;
import com.galwaykart.databinding.MHomepagTopproducttablV1Binding;
import com.galwaykart.dbfiles.Adapter.MvvmRecyclerViewProductAdapter;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.dbfiles.ViewModel.Mvvm_ProductViewModel;

import java.util.List;

public class Mvvm_HomePage_TopProductTab extends Fragment implements LifecycleOwner {

    MHomepagTopproducttablV1Binding binding;
    Observer<List<ProductDataModel>> productObserver;
    Observer<Integer> productCountObserver;
    Boolean data_from_local=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.m_homepag_topproducttabl_v1, container, false);
        View view = binding.getRoot();
        return view;
 }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Mvvm_ProductViewModel model= ViewModelProviders.of(this)
                .get(Mvvm_ProductViewModel.class);

        model.init();
        showProgress(true);

        productCountObserver=new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                data_from_local=true;
            }
        };



        productObserver=new Observer<List<ProductDataModel>>() {
            @Override
            public void onChanged(@Nullable List<ProductDataModel> productDataModels) {
                if (productDataModels == null || productDataModels.isEmpty()) {

                } else {

                    showProgress(false);
                    changeUI(productDataModels);

                }}
        };

            model.getLiveProduct().observe(this,productObserver);

        // model.getProduct();

    }

    private void changeUI(List<ProductDataModel> productDataModels){
//        int spanCount = 2; // 3 columns
//        int spacing = 1; // 50px
//        boolean includeEdge = true;
//
        //Log.d("resMVVM","changeUI");
//        //binding.setEmpDetail(user.get(0));
//        //binding.recyclerViewProductss.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        binding.recyclerViewProductss.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//
//        binding.recyclerViewProductss.setLayoutManager(new LinearLayoutManager(getActivity()));
//        MvvmRecyclerViewProductAdapter recyclerViewAdapter=new MvvmRecyclerViewProductAdapter(productDataModels);
//        binding.recyclerViewProductss.setAdapter(recyclerViewAdapter);
//        binding.recyclerViewProductss.invalidate();
//        binding.invalidateAll();

        int spanCount = 2; // 3 columns
        int spacing = 1; // 50px
        boolean includeEdge = true;
        binding.recyclerViewProductss.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        MvvmRecyclerViewProductAdapter recyclerViewAdapter=new MvvmRecyclerViewProductAdapter(getActivity(),productDataModels);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recyclerViewProductss.setLayoutManager(mLayoutManager);
        binding.recyclerViewProductss.setAdapter(recyclerViewAdapter);
        binding.setLifecycleOwner(this);
        binding.invalidateAll();


    }



    //TransparentProgressDialog pDialog;
    private void showProgress(Boolean aboolean){

        if(aboolean==true) {
            binding.progressBarss.setVisibility(View.VISIBLE);
        }
        else
        {
         binding.progressBarss.setVisibility(View.GONE);
        }

    }


}

