package com.galwaykart.kt_notification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.galwaykart.R;

public class NotificationAct extends AppCompatActivity {

    NotificationViewModelNew model;
    Button btn_click;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_listmodel);

        model= ViewModelProviders.of(this).get(NotificationViewModelNew.class);
        textView = findViewById(R.id.tv_text_23);

        if(model!=null) {

            textView.setText(String.valueOf(model.getCount()));
        }

        btn_click=findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setCount(model.getCount()+1);
                textView.setText(String.valueOf(model.getCount()));
            }
        });



    }
}
