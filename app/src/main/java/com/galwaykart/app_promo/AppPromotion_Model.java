package com.galwaykart.app_promo;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;

public class AppPromotion_Model {

    public Drawable app_image;
    public String app_name;
    public String app_install;
    public String app_desc;
    public String app_id;

    public Drawable getApp_image() {
        return app_image;
    }

    public void setApp_image(Drawable app_image) {
        this.app_image = app_image;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_install() {
        return app_install;
    }

    public void setApp_install(String app_install) {
        this.app_install = app_install;
    }

    public String getApp_desc() {

        if (Build.VERSION.SDK_INT >= 24)
            app_desc=(""+ Html.fromHtml(app_desc,0));
        else
            app_desc=(""+ Html.fromHtml(app_desc));

        return app_desc;
    }

    public void setApp_desc(String app_desc) {
        this.app_desc = app_desc;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }




    public AppPromotion_Model(Drawable app_image, String app_name, String app_install, String app_desc, String app_id) {
        this.app_image = app_image;
        this.app_name = app_name;
        this.app_install = app_install;
        this.app_desc = app_desc;
        this.app_id = app_id;
    }


    public void btn_app_open_or_install(){


//        if(Fun.is_Install_Or_Not(app_id ,view)) {
//                    //This intent will help you to launch if the package is already installed
//                    Intent LaunchIntent = view.getPackageManager()
//                            .getLaunchIntentForPackage(app_id);
//                    startActivity(LaunchIntent);
//
//                } else {
//                    //  if application not installed
//                    //  Redirect to play store
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+app_id));
//                    startActivity(intent);
//
//                }
        //Log.d("msg_click","done");

    }

}
