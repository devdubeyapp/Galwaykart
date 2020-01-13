package com.galwaykart.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.essentialClass.CommonFun;

public class NotificationSplashActivity extends AppCompatActivity {

    String title="", message="";
    String pushnotify="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonFun.finishscreen(NotificationSplashActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_splash);

//        String pushnotification = getIntent().hasExtra("pushnotification");
//        Log.e("pushnotification",pushnotification + "");

//        if (getIntent().hasExtra("pushnotification")) {
//
//            //Log.d("New_Notification_S2","notification");
//            title = getIntent().getStringExtra("title");
//            message = getIntent().getStringExtra("message");
//            showNotificationDialog();
//        }
//        else
//        {
//            //Log.d("New_Notification_S","notification");
//        }

        try {


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {

                    Object value = bundle.get(key);
                    ////Log.d("New_Notification_S3", String.format("%s %s (%s)", key,
                           // value.toString(), value.getClass().getName()));
                    //notice_title = getIntent().getStringExtra("title");
                    //notice_message = getIntent().getStringExtra("message");
                    if(key.equalsIgnoreCase("pushnotification"))pushnotify="yes";

                   // if(pushnotify.equalsIgnoreCase("yes")) {
                        if (key.equalsIgnoreCase("title")) title = value.toString();
                        if (key.equalsIgnoreCase("message")) message = value.toString();
                  // }
                }

                if(pushnotify.equalsIgnoreCase("yes")) {
                    showNotificationDialog();
                }
                else
                {
                    Intent intent=new Intent(NotificationSplashActivity.this, SplashActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(NotificationSplashActivity.this);
                }

            }
        }
        catch (Exception ex){
            ////Log.d("exception",ex.toString());
        }


    }


    public void showNotificationDialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.notification_dialog_layout,null);

        final TextView noti_title = (TextView)view.findViewById(R.id.noti_title);
        final TextView title_tv = (TextView)view.findViewById(R.id.title_tv);
        final TextView description_tv = (TextView)view.findViewById(R.id.description_tv);
        final TextView continue_tv = (TextView)view.findViewById(R.id.continue_tv);
        final TextView skip_tv = (TextView)view.findViewById(R.id.skip_tv);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setCancelable(false);
        final AlertDialog view_dialog = dialog.create();
        view_dialog.show();

        title_tv.setText(title);
        description_tv.setText(message);

        skip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_dialog.dismiss();
                finish();
            }
        });

        continue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLoggedIn()) {
                    Intent intent = new Intent(NotificationSplashActivity.this, NotificationListActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(NotificationSplashActivity.this);
                }
                else
                {
                    Intent intent = new Intent(NotificationSplashActivity.this, NotificationListActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(NotificationSplashActivity.this);
                }
            }
        });

    }

    private boolean isLoggedIn(){
        boolean is_log=false;
        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());
        String value_email=pref.getString("login_email","");

        if(value_email!=null && !value_email.equals(""))
        is_log=true;

        return is_log;
    }
}
