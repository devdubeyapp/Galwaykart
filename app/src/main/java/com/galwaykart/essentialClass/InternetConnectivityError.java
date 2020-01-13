package com.galwaykart.essentialClass;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.galwaykart.HomePageActivity;
import com.galwaykart.R;

/**
 * Created by ankesh on 7/27/2017.
 */


public class InternetConnectivityError extends AppCompatActivity {
    Button txt_try_again;
    Button txt_go_home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_connectivity_error_activity);

        txt_try_again=(Button)findViewById(R.id.txt_try_again);
        txt_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishScreen();
            }
        });


        txt_go_home=(Button)findViewById(R.id.txt_go_home);
        txt_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternetConnectivityError.this, HomePageActivity.class);
                startActivity(intent);
                CommonFun.finishscreen(InternetConnectivityError.this);

            }
        });
    }

    private void finishScreen(){
        this.finish();
    }

}
