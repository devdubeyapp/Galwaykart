package com.galwaykart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.productList.ProductListActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by sumitsaini on 10/11/2017.
 */

public class ViewPagerAdapterBanner extends PagerAdapter
{

    Context mContext;
    String [] banner_image;
    String [] banner_image_catid;
    String [] banner_image_sku;


    public ViewPagerAdapterBanner(Context contexts, String[] banner_image,String[] banner_image_catid,String[] banner_image_sku)
    {
        // TODO Auto-generated constructor stub
        this.mContext = contexts;
        this.banner_image = banner_image;
        this.banner_image_catid=banner_image_catid;
        this.banner_image_sku=banner_image_sku;
    }

    public ViewPagerAdapterBanner(Context contexts, String[] banner_image)
    {
        // TODO Auto-generated constructor stub
        this.mContext = contexts;
        this.banner_image = banner_image;

    }


    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return banner_image.length;
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
    public Object instantiateItem(ViewGroup collection, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View  view = (ViewGroup) layoutInflater.inflate(R.layout.view_pager_items, collection, false);
        ImageView img_banner = (ImageView) view.findViewById(R.id.img_banner);

        img_banner.setScaleType(ImageView.ScaleType.FIT_XY);
        img_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        ////////Log.d("clicked", String.valueOf(position));


                if(!banner_image_catid[position].equalsIgnoreCase(""))
                {
                    //////Log.d("clicked", banner_image_catid[position]);
                    SharedPreferences pref;
                    pref= mContext.getSharedPreferences("GalwayKart", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("selected_id",banner_image_catid[position]);
                    editor.putString("selected_name","");
                    editor.commit();

                    Intent intent = new Intent(mContext,ProductListActivity.class);
                    intent.putExtra("onback","home");
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();

                }
                else if(!banner_image_sku[position].equalsIgnoreCase(""))
                {
                    //////Log.d("clicked", banner_image_sku[position]);
                    SharedPreferences pref;
                    pref= CommonFun.getPreferences(mContext);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("showitemsku",banner_image_sku[position]);
                    editor.commit();

                    Intent intent=new Intent(mContext, com.galwaykart.SingleProductView.MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }
                else
                {
                    //////Log.d("clicked", "");
                }

            }
        });

        //////Log.d("banner_image",banner_image[position]);

        if(!banner_image[position].equalsIgnoreCase("")) {
            Picasso.get()
                    .load(banner_image[position])
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)
                    .into(img_banner);
        }
        else{
            Picasso.get()
                    .load(R.drawable.noimage)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)
                    .into(img_banner);
        }
        collection.addView(view);
        return view;

    }

}