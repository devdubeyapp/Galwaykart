package com.galwaykart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by itsoftware on 02/01/2018.
 */

public class ApplicationUpdateActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView txt_try_again;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        //pref= getSharedPreferences("glazeapp", MODE_PRIVATE);

        txt_try_again= findViewById(R.id.txt_try_again);
        txt_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.galwaykart"));
                startActivity(intent);

            }
        });

    }

}
