package com.galwaykart;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestScroll extends AppCompatActivity {

    LinearLayout llayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testscroll);
        llayout= findViewById(R.id.llayout);

        for (int i = 1; i <= 20; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn = new Button(this);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText("button " + id_);
            btn.setBackgroundColor(Color.rgb(70, 80, 90));
            llayout.addView(btn, params);

        }


    }
}
