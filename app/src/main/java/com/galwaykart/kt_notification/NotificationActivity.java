package com.galwaykart.kt_notification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.galwaykart.R;

public class NotificationActivity extends AppCompatActivity {


    NotificationViewModelNew notificationViewModel;
    TextView tv_text_23;
    Button btn_click;

    int i=10;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_listmodel);
        notificationViewModel= ViewModelProviders.of(this).get(NotificationViewModelNew.class);


        tv_text_23=findViewById(R.id.tv_text_23);
        //tv_text_23.setText(notificationViewModel.score);

        btn_click=findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // notificationViewModel.score++;
              //  tv_text_23.setText(String.valueOf(notificationViewModel.score));
            }
        });





    }
}
