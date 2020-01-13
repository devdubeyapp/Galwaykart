package com.galwaykart.app_promo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.galwaykart.App;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

public class AppPromoHome extends AppCompatActivity {

    ImageView imageView_app_galway,imageView_app_galwaykart;
    ImageView imageView_app_galwayexam,imageView_app_galwayfoundation;

    TextView tv_app_galway,tv_app_galwaykart;
    TextView tv_app_galwayexam,tv_app_galwayfoundation;

    Button btn_glazegalway,btn_galwaykart;
    Button btn_galwayexam,btn_galwayfoundation;


    private void openAppPromotionDetail(String app_id){
        Intent intent=new Intent(this, AppPromotion.class);
               intent.putExtra("app_id",app_id);
        startActivity(intent);
            CommonFun.finishscreen(this);
        }

    private boolean is_Install_Or_Not(String appname){

        PackageManager packageManager=getPackageManager();
        try {
            packageManager.getPackageInfo(appname,PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            return false;
        }


    }
    private void nav_open_click(String appname){
        if(is_Install_Or_Not(appname)) {
            //This intent will help you to launch if the package is already installed
            Intent LaunchIntent = getPackageManager()
                    .getLaunchIntentForPackage(appname);
            startActivity(LaunchIntent);

        } else {
            //  if application not installed
            //  Redirect to play store

            try {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appname));
                startActivity(intent);
            }
            catch (Exception ex){

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
    private void goBack(){
        Intent intent = new Intent(AppPromoHome.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(AppPromoHome.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apppromo_home);


        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });
        ImageView image_view_title=findViewById(R.id.image_view_title);
        image_view_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        ImageView iv_app_promo=findViewById(R.id.iv_close);
        iv_app_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              CommonFun.finishscreen(AppPromoHome.this);
            }
        });

        imageView_app_galway=findViewById(R.id.imageView_app_galway);
        tv_app_galway=findViewById(R.id.tv_app_galway);

        imageView_app_galway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glaze.admin");
            }
        });
        tv_app_galway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glaze.admin");
            }
        });

        btn_glazegalway=findViewById(R.id.btn_glazegalway);
        btn_glazegalway.setText(CommonFun.is_Install_Or_Not("com.glaze.admin",this)?"Open":"Install");
        btn_glazegalway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_open_click("com.glaze.admin");
            }
        });

        //Galwaykart
        imageView_app_galwaykart=findViewById(R.id.imageView_app_galwaykart);
        imageView_app_galwaykart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glazegalway");
            }
        });

        tv_app_galwaykart=findViewById(R.id.tv_app_galwaykart);
        tv_app_galwaykart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glazegalway");
            }
        });

        btn_galwaykart=findViewById(R.id.btn_galwaykart);
        btn_galwaykart.setText(CommonFun.is_Install_Or_Not("com.glazegalway",this)?"Open":"Install");
        btn_galwaykart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_open_click("com.glazegalway");
            }
        });


        imageView_app_galwayexam=findViewById(R.id.imageView_app_galwayexam);
        imageView_app_galwayexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glaze.galwayexamsystem");
            }
        });

        tv_app_galwayexam=findViewById(R.id.tv_app_galwayexam);
        tv_app_galwayexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.glaze.galwayexamsystem");
            }
        });


        btn_galwayexam=findViewById(R.id.btn_galwayexam);
        btn_galwayexam.setText(CommonFun.is_Install_Or_Not("com.glaze.galwayexamsystem",this)?"Open":"Install");
        btn_galwayexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_open_click("com.glaze.galwayexamsystem");
            }
        });

        imageView_app_galwayfoundation=findViewById(R.id.imageView_app_galwayfoundation);

        imageView_app_galwayfoundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.galwayfoundation.galwayfoundation");
            }
        });
        tv_app_galwayfoundation=findViewById(R.id.tv_app_galwayfoundation);
        tv_app_galwayfoundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppPromotionDetail("com.galwayfoundation.galwayfoundation");
            }
        });

        btn_galwayfoundation=findViewById(R.id.btn_galwayfoundation);
        btn_galwayfoundation.setText(CommonFun.is_Install_Or_Not("com.galwayfoundation.galwayfoundation",this)?"Open":"Install");
        btn_galwayfoundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_open_click("com.galwayfoundation.galwayfoundation");
            }
        });


    }
}
