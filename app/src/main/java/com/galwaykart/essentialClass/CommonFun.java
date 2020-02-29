package com.galwaykart.essentialClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.appcompat.widget.Toolbar;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.galwaykart.address_book.AddNewAddress;
import com.galwaykart.HomePageActivity;
import com.galwaykart.Login.LogoutActivity;
import com.galwaykart.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static android.content.Context.MODE_PRIVATE;

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

    public static void alertOTPMessage(Context ctx){
        final AlertDialog.Builder b;
        try
        {
            b = new AlertDialog.Builder(ctx, R.style.AppTheme_Alert);
            b.setTitle("Info");
            b.setCancelable(true);

            b.setMessage(R.string.otp_tnc);

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

    public static boolean is_Install_Or_Not(String appname,Context context){

        try {
            PackageManager packageManager=context.getPackageManager();
            try {
                packageManager.getPackageInfo(appname,PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                //e.printStackTrace();
                return false;
            }
        }
        catch (Exception ex){
            return  false;
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
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog_design);
        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
        tv_dialog.setText(msg);
        ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
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



    public static void openHomeActivity(Context context)
    {
        Intent intent=new Intent(context,HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    public static void setToolBar(Toolbar toolbar,Context context){


        TextView tv_cartqty;
        tv_cartqty= toolbar.findViewById(R.id.cart_icon);
        tv_cartqty.setVisibility(View.GONE);
        ProgressBar cart_progressBar;
        cart_progressBar= toolbar.findViewById(R.id.cart_progressBar);
        cart_progressBar.setVisibility(View.GONE);
        ImageView imageViewIcon;
        imageViewIcon= toolbar.findViewById(R.id.image_view_title);
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.openHomeActivity(context);
            }
        });
    }

    public  static void OpenNewAddress(Context context,String intent_extra,int total_data){

        Intent intent = new Intent(context, AddNewAddress.class);

        if(intent_extra!=null && !intent_extra.equals(""))
            intent.putExtra("st_come_from_update",intent_extra);
        intent.putExtra("total_address_data",String.valueOf(total_data));

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        finishscreen((Activity) context);
    }

    public static void showProgress(TransparentProgressDialog pDialog,Context context){

    }

    public static void hideProgress(TransparentProgressDialog pDialog,Context context){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }


    public static SharedPreferences getPreferences(Context mContext){


        SharedPreferences pref = null;
        try {

            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            pref = EncryptedSharedPreferences.create(
                   "glazekartapp",
                    masterKeyAlias,
                    mContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return pref;

    }

}
