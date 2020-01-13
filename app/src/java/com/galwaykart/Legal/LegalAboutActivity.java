package com.galwaykart.Legal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

/**
 * Shows Legal and all details by fetch from html, as comefrom param
 * Created by ankesh on 10/4/2017.
 */

public class LegalAboutActivity extends BaseActivityWithoutCart{

    TextView tvPrivacy,tvShip,tvReturn;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(LegalAboutActivity.this, HomePageActivity.class);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition_activity);
        initNavigationDrawer();

        tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
        tvShip = (TextView) findViewById(R.id.tvShip);
        tvReturn = (TextView) findViewById(R.id.tvReturn);

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                        intent.putExtra("comefrom","privacypolicy.html");
                        startActivity(intent);

            }
        });

        tvShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","shipping.html");
                startActivity(intent);

            }
        });


        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","returnexchange.html");
                startActivity(intent);

            }
        });

    }


}
