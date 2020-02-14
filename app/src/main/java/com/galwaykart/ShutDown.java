package com.galwaykart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.essentialClass.CommonFun;
import com.squareup.picasso.Picasso;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 * Created by ankesh on 8/28/2017.
 */

public class ShutDown extends AppCompatActivity {

    Class c;
    Intent intent;
    SharedPreferences pref;
    String classname;

    TextView text_t;
    ImageView imageView;

    String shutdown_text;

    @Override
    public void onBackPressed() {
        try
        {
            CommonFun.finishscreen(ShutDown.this);
        }
        catch(Exception ex)
        {

        }

        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shutdown);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                shutdown_text= extras.getString("android_text");
            }
        }

        text_t= findViewById(R.id.text_t);
        imageView= findViewById(R.id.imageView);

        text_t.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);



        if(shutdown_text.contains("http:")){
            imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(shutdown_text)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    // .resize(200, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(imageView);
        }
        else
        {
            text_t.setVisibility(View.VISIBLE);
            text_t.setText(shutdown_text);
        }



    }


}

