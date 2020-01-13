package com.galwaykart;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.essentialClass.CommonFun;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

             //   Intent intent=new Intent(MainActivity.this, com.galwaykart.detailproduct.MainActivity.class);
          //    Intent intent=new Intent(MainActivity.this, CartItemList.class);
           //     startActivity(intent);
//                CommonFun.finishscreen(MainActivity.this);

                //callJSONAPIVolley();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void callJSONAPIVolley()
    {

        String fromurl=com.galwaykart.essentialClass.Global_Settings.api_url+"index.php/rest/V1/categories";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        CommonFun.alertError(MainActivity.this,response.toString());
//                        if(response!=null){
//                            try {
//                                JSONObject jsonObj = new JSONObject(String.valueOf(response));
//                                contacts = jsonObj.getJSONArray("Values");
//
//                                for (int i = 0; i < contacts.length(); i++) {
//                                    JSONObject c = contacts.getJSONObject(i);
//
//                                    gisvalid=c.getString("isvalid_");
//                                    gnid=c.getString("Nid_");
//                                    gdoj=c.getString("JoiningDate_");
//                                    gzone=c.getString("ZN_");
//                                    grank=c.getString("Rank_");
//                                    gadminlevel=c.getString("Admin_level_");
//                                    gname=c.getString("Name_");
//                                    //guid=txtusername.getText().toString();
//                                    guid=c.getString("id_");
//                                    memid=c.getString("memberid_");
//                                    submemid=c.getString("submemberid_");
//                                    mobileno=c.getString("mobileno_");
//                                    finalmsg=c.getString("msg_");
//                                    token_data=c.getString("TokenData_");
//                                    appsoftversion=c.getString("SoftwareVersion_");
//
//                                    ////Log.d("level id"," " +gadminlevel);
//                                }
//                                if(pDialog.isShowing())
//                                    pDialog.dismiss();
//
//                                ListDrwaer();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();

                CommonFun.alertError(MainActivity.this,error.toString());
                error.printStackTrace();


            }
        }){
            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsObjRequest);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
