package com.galwaykart;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.galwaykart.essentialClass.CommonFun;

/**
 * Created by itsoftware on 18/01/2018.
 */

public class SplashErrorActivity extends AppCompatActivity {

    Button txt_try_again;
    TextView error_head,error_message;
    Button txt_go_home;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_connectivity_error_activity);

        txt_try_again= findViewById(R.id.txt_try_again);
        txt_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishScreen();
            }
        });
        error_head= findViewById(R.id.error_head);
        error_head.setText("Unable to reach Server !!! ");


        txt_go_home= findViewById(R.id.txt_go_home);
        txt_go_home.setVisibility(View.GONE);

        txt_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SplashErrorActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(SplashErrorActivity.this);

            }
        });
        //error_message=(TextView)findViewById(R.id.error_message);
        //error_message.setText("");
    }

    private void finishScreen(){
        this.finish();
    }

}
