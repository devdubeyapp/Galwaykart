package com.galwaykart.SingleProductView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.galwaykart.R;
import com.galwaykart.ViewPagerAdapterSingleProduct;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.squareup.picasso.Picasso;

/**
 * Zoom Product images and show all images in bottom bar
 * Created by itsoftware on 28/12/2017.
 */

public class ZoomProductImages extends Activity {

    String[] arr_product_images;
   // ViewPager pager_view_products;
    int page = 0;
    ImageView[] dots;
    private LinearLayout pager_indicator;
    ImageView imageView;
    int current_index=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zoom_image_layout);


        page=0;
      //  pager_view_products=(ViewPager)findViewById(R.id.pager_view_products);


        SharedPreferences pref;
        pref= CommonFun.getPreferences(getApplicationContext());

        String img_name=pref.getString("zoomimage","");

        int total_length= Integer.parseInt(pref.getString("zoomtotallength",""));
        arr_product_images=new String[total_length];
        for(int i=0;i<total_length;i++)
            arr_product_images[i]= pref.getString("zoomimagearray_"+i,"");

        imageView=(ImageView)findViewById(R.id.imgProduct);
        setImage(img_name,imageView);
        setProductImagePager();

        ImageView imageView_Close=(ImageView)findViewById(R.id.imgClose);
        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.finishscreen(ZoomProductImages.this);
            }
        });


    }

    /**
     * set Image on Imageview
     * @param image
     * @param imageView
     */
    private void setImage(String image,ImageView imageView){

        Picasso.with(ZoomProductImages.this)
                .load(Global_Settings.api_url+"pub/media/catalog/product"+image)
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)
                .into(imageView);

    }

    /**
     * Set Product Image Pager Content
     */
    private  void  setProductImagePager(){


        pager_indicator=(LinearLayout)findViewById(R.id.viewPagerCountDots);

        dots=new ImageView[arr_product_images.length];
        for (int i = 0; i < arr_product_images.length; i++) {

            dots[i] = new ImageView(this);
            dots[i].setClickable(true);

            final int finalI = i;
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    current_index=finalI;
                    //Toast.makeText(ZoomProductImages.this,String.valueOf(current_index),Toast.LENGTH_LONG).show();

                    setImage(arr_product_images[current_index],imageView);

                }
            });

            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        for(int i=0;i<arr_product_images.length;i++){

            setNewImage(arr_product_images[i],dots[i]);

        }

        //dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        //ViewPagerAdapterSingleProduct viewPagerAdapterSingleProduct=new ViewPagerAdapterSingleProduct(this,arr_product_images);
        //pager_view_products.setAdapter(viewPagerAdapterSingleProduct);


    }


    private void setNewImage(String image,ImageView imageView){

        Picasso.with(ZoomProductImages.this)
                .load(Global_Settings.api_url+"pub/media/catalog/product"+image)
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)
                .resize(100,100)
                .into(imageView);

    }
}
