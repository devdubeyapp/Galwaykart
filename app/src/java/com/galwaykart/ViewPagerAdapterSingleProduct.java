package com.galwaykart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import android.widget.LinearLayout;

import com.galwaykart.SingleProductView.ZoomProductImages;
import com.galwaykart.essentialClass.Global_Settings;
import com.squareup.picasso.Picasso;


/**
 * Created by
 * Ankesh Kumar on 28/12/2017.
 */

public class ViewPagerAdapterSingleProduct extends PagerAdapter {

    Context mContext;
    String[] arr_product_images;
    String img="";


    public ViewPagerAdapterSingleProduct(Context contexts, String[] product_image)
    {
        // TODO Auto-generated constructor stub
        this.mContext = contexts;
        this.arr_product_images = product_image;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return arr_product_images.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        // TODO Auto-generated method stub
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View  view = (ViewGroup) layoutInflater.inflate(R.layout.view_pager_single_product, collection, false);
        ImageView img_banner = (ImageView) view.findViewById(R.id.img_product);


        img=arr_product_images[position].toString();

        img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        img_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                SharedPreferences pref;
                pref= CommonFun.getPreferences(mContext);

                SharedPreferences.Editor editor=pref.edit();
                editor.putString("zoomimage",img);
                editor.putString("zoomtotallength",String.valueOf(arr_product_images.length));
                for(int i=0;i<arr_product_images.length;i++)
                    editor.putString("zoomimagearray_"+i,arr_product_images[i]);
                editor.commit();

                Intent intent=new Intent(mContext,ZoomProductImages.class);
                //intent.putExtra("zoomimage",arr_product_images[position]);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });


        Picasso.get()
                .load(Global_Settings.api_url+"pub/media/catalog/product"+arr_product_images[position])
                .placeholder(R.drawable.imageloading)   // optional
                .error(R.drawable.noimage)
                .into(img_banner);

        collection.addView(view);
        return view;

    }


}




