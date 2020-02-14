package com.galwaykart.Legal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.BaseActivityCommon;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

/**
 * Shows Legal and all details by fetch from html, as comefrom param
 * Created by ankesh on 10/4/2017.
 */

public class LegalAboutActivity extends AppCompatActivity {

    TextView tvPrivacy,tvShip,tvReturn;
    TextView tvPayment,tvTerms,tvCancel;
    TextView tvActiveDistributor,tvPreferredCustomer;
    TextView tvDonationDisclaimer;



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
       // initNavigationDrawer();
//        Toolbar toolbar;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        CommonFun.setToolBar(toolbar,this);
        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();

            }
        });


        tvPrivacy = findViewById(R.id.tvPrivacy);
        tvShip = findViewById(R.id.tvShip);
        tvReturn = findViewById(R.id.tvReturn);
        tvPayment= findViewById(R.id.tvPayment);
        tvTerms= findViewById(R.id.tvTerms);
        tvCancel= findViewById(R.id.tvCancel);

        tvActiveDistributor= findViewById(R.id.tvActiveDistributor);
        tvPreferredCustomer= findViewById(R.id.tvPreferredCustomer);
        tvDonationDisclaimer= findViewById(R.id.tvDonationDisclaimer);


        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                        intent.putExtra("comefrom","privacy-policy");
                        startActivity(intent);

            }
        });

        tvShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","shippingpolicy");
                startActivity(intent);

            }
        });


        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","returnpolicy");
                startActivity(intent);

            }
        });

        tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","payment-security-policy");
                startActivity(intent);

            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","terms-and-conditions");
                startActivity(intent);

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","cancellation_policy");
                startActivity(intent);

            }
        });


        tvActiveDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","active-distributor-policy");
                startActivity(intent);

            }
        });

        tvPreferredCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","preferred-customer");
                startActivity(intent);

            }
        });


        tvDonationDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LegalAboutActivity.this,WebViewActivity.class);
                intent.putExtra("comefrom","gaushala-donation");
                startActivity(intent);

            }
        });




    }


}
