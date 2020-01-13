package com.galwaykart.app_promo;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Guest.GuestHomePageActivity;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.databinding.CrossAppPromoteLayoutBinding;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

//import androidx.databinding.BindingAdapter;
//import androidx.databinding.DataBindingUtil;
//import com.glaze.admin.databinding.CrossAppPromoteLayoutBinding;

public class AppPromotion extends AppCompatActivity {

    String app_name="",
            app_image="",
            app_install="",
            app_desc="";
    AppPromotion_Model model;
    String app_installed_or_not="";
    ImageView imageView_app;
    TextView tv_app_name,tv_app_desc;
    Button btn_app;
    String app_id="";
    CrossAppPromoteLayoutBinding binding;
    TransparentProgressDialog pDialog;

    @Override
    public void onBackPressed() {

        goBack();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.cross_app_promote_layout);
         binding= DataBindingUtil.setContentView(this, R.layout.cross_app_promote_layout);
       // ViewModelProviders.of(this).get(AppPromotion_Model.class);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                app_id= extras.getString("app_id");
            }
        }



//        imageView_app=findViewById(R.id.imageView_app);
//        tv_app_name=findViewById(R.id.tv_app_name);
//        tv_app_desc=findViewById(R.id.tv_app_desc);
        btn_app=findViewById(R.id.btn_app);
        btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonFun.is_Install_Or_Not(app_id,AppPromotion.this)) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage(app_id);
                    startActivity(LaunchIntent);

                } else {
                    //  if application not installed
                    //  Redirect to play store
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_id));
                        startActivity(intent);
                    }catch(ActivityNotFoundException ex){
                        Toast.makeText(AppPromotion.this,"Unable to open Play store",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        app_installed_or_not= CommonFun.is_Install_Or_Not(app_id,this)?"Open":"Install";


        Drawable imgPath=null;
        if(app_id.equalsIgnoreCase("com.glazegalway")){
            imgPath=getResources().getDrawable(R.drawable.nav_glazegalway);
           // imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_glazegalway));
        }
        else if(app_id.equalsIgnoreCase("com.galwaykart")){
            imgPath=getResources().getDrawable(R.drawable.nav_galwaykart);
            //imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_galwaykart));
        }
        else if(app_id.equalsIgnoreCase("com.glaze.galwayexamsystem")){
            imgPath=getResources().getDrawable(R.drawable.nav_galwayexamsystem);
            //imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_galwayexamsystem));
        }
        else if(app_id.equalsIgnoreCase("com.galwayfoundation.galwayfoundation")){
            imgPath=getResources().getDrawable(R.drawable.nav_galwayfoundation);
            //imageView_app.setBackground(getResources().getDrawable(R.drawable.ic_galwayfoundation));
        }
        else if(app_id.equalsIgnoreCase("com.glaze.admin")){
            imgPath=getResources().getDrawable(R.drawable.nav_galwayfieldofficer);
            //imageView_app.setBackground(getResources().getDrawable(R.drawable.ic_galwayfoundation));
        }

        callCrossAppPromoteData_v1(imgPath);
        //callCrossAppPromoteData();







    }

    private void callCrossAppPromoteData_v1(final Drawable app_image){

        pDialog = new TransparentProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        String url= Global_Settings.Cross_App_Promote +app_id;
        final RequestQueue requestQueue= Volley.newRequestQueue(AppPromotion.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        //Log.d("callResponse", String.valueOf(response));
                        if(response!=null){

                            String status="",app_name="",app_desc="";
                            try {
                                status=response.getString("status");
                                app_name=response.getString("app_name");
                                app_desc=response.getString("app_desc");

                                //setData(app_name, app_desc);

                                model=new AppPromotion_Model(app_image,app_name,app_installed_or_not,app_desc,"");

                                binding.setAppDescription(model);
                                binding.invalidateAll();

                            } catch (JSONException e) {
                                e.printStackTrace();

                                if(pDialog.isShowing())
                                    pDialog.dismiss();
                            }


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();

                    }
                }

        );

        requestQueue.add(jsonObjectRequest);

    }




    private void setData(String app_name, String app_desc) {
        tv_app_name.setText(app_name);

        if (Build.VERSION.SDK_INT >= 24)
            tv_app_desc.setText(""+ Html.fromHtml(app_desc,0));
        else
            tv_app_desc.setText(""+ Html.fromHtml(app_desc));

        btn_app.setText(app_installed_or_not);

        if(app_id.equalsIgnoreCase("com.glazegalway")){
            imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_glazegalway));
        }
        else if(app_id.equalsIgnoreCase("com.galwaykart")){
            imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_galwaykart));
        }
        else if(app_id.equalsIgnoreCase("com.glaze.galwayexamsystem")){
            imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_galwayexamsystem));
        }
        else if(app_id.equalsIgnoreCase("com.galwayfoundation.galwayfoundation")){
            imageView_app.setBackground(getResources().getDrawable(R.drawable.ic_galwayfoundation));
        }
        else if(app_id.equalsIgnoreCase("com.glaze.admin")){
            imageView_app.setBackground(getResources().getDrawable(R.drawable.nav_galwayfieldofficer));
        }

    }
    private void callCrossAppPromoteData(){

        String url="";//GlobalSettings.Cross_App_Promote +app_id;
        final RequestQueue requestQueue= Volley.newRequestQueue(AppPromotion.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Log.d("callResponse", String.valueOf(response));
                        if(response!=null){

                            String status="",app_name="",app_desc="";
                            try {
                                status=response.getString("status");
                                app_name=response.getString("app_name");
                                app_desc=response.getString("app_desc");

                                setData(app_name, app_desc);


//                                model=new AppPromotion_Model
//                                        (app_name,
//                                                "",
//                                                app_desc,
//                                                app_installed_or_not);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        requestQueue.add(jsonObjectRequest);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.apppromomenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_back:

                goBack();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void goBack(){
        Intent intenth = new Intent(this, AppPromoHome.class);
        startActivity(intenth);
        CommonFun.finishscreen(this);
    }
    }