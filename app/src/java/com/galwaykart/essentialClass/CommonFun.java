package com.galwaykart.essentialClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;

/**
 * Created by ankesh on 9/5/2017.
 */

public class CommonFun {

    public static void finishscreen(Activity context){
        context.finish();
    }

    public static void alertError(Context ctx,String errmsg){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(ctx);
            b.setTitle("Alert");
            b.setCancelable(false);
            b.setMessage(errmsg);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    b.create().dismiss();
                }
            });
            b.create().show();
        }
        catch(Exception ex)
        {
        }

    }

    public static void showVolleyException(VolleyError error,Context context){
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            Intent intent=new Intent(context, InternetConnectivityError.class);
            context.startActivity(intent);

        }
        else if (error instanceof AuthFailureError) {
            //Intent intent=new Intent(context, SplashActivity.class);
            //context.startActivity(intent);

            Intent intent=new Intent(context, LogoutActivity.class);
            context.startActivity(intent);


            //TODO
        } else if (error instanceof ServerError) {
            //TODO
            Intent intent=new Intent(context, ServerErrorActivity.class);
            context.startActivity(intent);

        }
        else if(error instanceof ParseError)
        {
            Intent intent=new Intent(context, ExceptionError.class);
            context.startActivity(intent);
        }
    }


    public static void showDialog(Context context,String msg) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog_design);
        TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
        tv_dialog.setText(msg);
        ImageView image_view_dialog = (ImageView) dialog.findViewById(R.id.image_view_dialog);
        dialog.show();

        new CountDownTimer(2000, 2000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        }.start();
    }
}
