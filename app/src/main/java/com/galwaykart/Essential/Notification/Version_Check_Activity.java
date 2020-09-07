package com.galwaykart.Essential.Notification;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.galwaykart.R;

/**
 * Created by ankesh on 8/28/2017.
 */

public class Version_Check_Activity extends AppCompatActivity {

    Class c;
    Intent intent;
    SharedPreferences pref;
    String classname;

    TextView txt_try_again;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_check);
        pref= getSharedPreferences("glazeapp", MODE_PRIVATE);

        txt_try_again= findViewById(R.id.txt_try_again);
        txt_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.galwaykart"));
            startActivity(intent);
        }
        catch (ActivityNotFoundException ex){
            viewInBrowser(Version_Check_Activity.this, "https://play.google.com/store/apps/details?id=com.galwaykart");
        }


            }
        });

    }





    public static void viewInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
        }
    }

}

