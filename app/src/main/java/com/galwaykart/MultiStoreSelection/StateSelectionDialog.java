package com.galwaykart.MultiStoreSelection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.Guest.GuestHomePageActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.HomePageTab.DataModelHomeAPI;
import com.galwaykart.R;
import com.galwaykart.SplashActivity;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class StateSelectionDialog extends AppCompatActivity {
    private static final int REQUEST_CODE_EXAMPLE = 0;
    Spinner spinner_state_profile;
    String region_code;
    String arr_state_code[];
    String arr_state_name[];
    final String TAG_region_code= "code";
    final String TAG_region_name= "name";
    Button button_save_address;
    SharedPreferences pref;
    String current_user_zone="";
    boolean is_zone_called=false;
    TransparentProgressDialog  pDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_state_selection);

        pref = CommonFun.getPreferences(getApplicationContext());



        spinner_state_profile = findViewById(R.id.spinner_state_profile);
        spinner_state_profile.setEnabled(false);

        spinner_state_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                region_code = arr_state_code[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StateSelectionDialog.this, android.R.layout.simple_spinner_dropdown_item, arr_state_name);
//        spinner_state_profile.setAdapter(adapter);

        //spinner_state_profile.setSelection(state_pos);
        spinner_state_profile.setEnabled(false);


        button_save_address=findViewById(R.id.button_save_address);
        button_save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                

//                if(region_code.equalsIgnoreCase(current_user_zone))
//                {
//
//                    Global_Settings.api_url=Global_Settings.web_url+region_code+"/";
//                    Global_Settings.current_zone=region_code;
//                    callHomePageAPI();
//
//                }
//                else
//                {

                    /**
                     * Update Current Zone on Server
                     */

                pref = CommonFun.getPreferences(StateSelectionDialog.this);

                String email = pref.getString("user_email", "");
                if (!email.equalsIgnoreCase("") && email != null) {

                    updateUserZone(StateSelectionDialog.this);
                }
                else
                {
                    Global_Settings.current_zone=region_code;
                    Global_Settings.api_url=Global_Settings.web_url+region_code+'/';

                    SharedPreferences pref = CommonFun.getPreferences(StateSelectionDialog.this);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("guest_current_zone",region_code);
                    editor.commit();
                    Intent intent=new Intent(StateSelectionDialog.this,SplashActivity.class);
                    startActivity(intent);
                    CommonFun.finishscreen(StateSelectionDialog.this);
                }



               // }





            }
        });


        st_get_State_URL= Global_Settings.web_url + "rest/V1/website/list";




    }

    String st_get_State_URL="";

    @Override
    protected void onResume() {
        super.onResume();

//        if(is_zone_called==false) {
//            final Intent intent = new Intent(StateSelectionDialog.this, GetCurrentZone.class);
//            startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
//        }
//        else
//        {
            getState();
        //}


    }


    String tokenData="";
    private void updateUserZone(Context context) {


        String updateUserZoneUrl=Global_Settings.api_url+"rest/V1/website/code";

        SharedPreferences pref= CommonFun.getPreferences(context);
        tokenData=pref.getString("tokenData","");
        String st_current_zone= Global_Settings.api_url+"rest/V1/website/code";
        tokenData = tokenData.replaceAll("\"", "");

        String st_input_data="{" +
                "\"code\": \"" + region_code +  "\"" +
                "}";

        Log.d("responseCurrentZone",st_input_data);

        pDialog = new TransparentProgressDialog(context);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.PUT,
                updateUserZoneUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("responseCurrentZone", response.toString());

                        if(pDialog!=null & pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        Boolean res=Boolean.parseBoolean(response);
                        if(res==true){
                            Log.d("ontrue","true");
                            Global_Settings.api_url=Global_Settings.web_url+region_code+"/";
                            Global_Settings.current_zone=region_code;

                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("call_user_current_zone","done");
                            editor.commit();

                            Intent intent=new Intent(StateSelectionDialog.this, SplashActivity.class);
                            startActivity(intent);
                            CommonFun.finishscreen(StateSelectionDialog.this);

                            //callHomePageAPI();

                        }
                        else
                        {
                            Log.d("ontrue","else");
//                            Global_Settings.api_url=Global_Settings.web_url+region_code+"/";
//                            Global_Settings.current_zone=region_code;
                            callHomePageAPI();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        //progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,StateSelectionDialog.this);

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + tokenData);
                params.put("Content-Type", "application/json");

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {

                    return st_input_data == null ? null : st_input_data.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", st_input_data, "utf-8");
                    return null;
                }
                //return new byte[0];
            }


        };
        jsObjRequest.setShouldCache(false);
        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                1,
                1);
        jsObjRequest.setRetryPolicy(retryPolicy);
        queue.add(jsObjRequest);




    }


    private void getState() {



        pDialog = new TransparentProgressDialog(StateSelectionDialog.this);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsObjRequest=null;
        try {
            jsObjRequest = new StringRequest(Request.Method.GET, st_get_State_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            Log.d("responseState",response);

                            if (response != null) {
                                try {

                                    JSONArray jsonArray=new JSONArray(response);

                                    //JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    //JSONArray jsonStateList=jsonObject.getJSONArray("statelist");

                                    arr_state_code=new String[jsonArray.length()];
                                    arr_state_name=new String[jsonArray.length()];

                                    Log.d("responseState",jsonArray.toString());

                                    for(int i=0; i<jsonArray.length(); i++)
                                    {

                                        JSONObject  order_list_obj = jsonArray.getJSONObject(i);

                                        String st_region_id = order_list_obj.getString(TAG_region_code);
                                        String st_region_name = order_list_obj.getString(TAG_region_name);

                                        arr_state_code[i] = st_region_id;
                                        arr_state_name[i] = st_region_name;

                                    }


                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StateSelectionDialog.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
                                    spinner_state_profile.setAdapter(adapter);
                                    spinner_state_profile.setEnabled(true);

                                    String current_user_zone_name="";
                                    for(int i=0;i<arr_state_name.length;i++){
                                        if(arr_state_code[i].equalsIgnoreCase(current_user_zone))
                                            current_user_zone_name=arr_state_name[i];
                                    }

                                    spinner_state_profile.setSelection(adapter.getPosition(current_user_zone_name));



                                } catch (Exception e) {
                                    if(pDialog.isShowing())
                                        pDialog.dismiss();
                                    e.printStackTrace();
                                    // Intent intent=new Intent(StateSelectionDialog.this, ExceptionError.class);
                                    //  startActivity(intent);
                                }
                            }


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
                    if(pDialog.isShowing())
                        pDialog.dismiss();
                    CommonFun.showVolleyException(error,StateSelectionDialog.this);
                    //CommonFun.alertError(StateSelectionDialog.this,error.toString());

                    // error.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        jsObjRequest.setShouldCache(false);
        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                1,
                1);
        jsObjRequest.setRetryPolicy(retryPolicy);
        queue.add(jsObjRequest);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_EXAMPLE){

            if(resultCode==RESULT_OK)
            {
                is_zone_called=true;
               current_user_zone = data.getStringExtra(GetCurrentZone.EXTRA_DATA);

               Log.d("result_zoneActivity",current_user_zone);

                Global_Settings.api_url=Global_Settings.web_url+current_user_zone.trim().toString()+"/";
                Global_Settings.current_zone=current_user_zone.trim().toString();

               // getState();
            }
        }
    }

    private void callHomePageAPI()
    {

        String email = pref.getString("user_email", "");
        String login_group_id=pref.getString("login_group_id","");
        String home_page_api="";

        if (!email.equalsIgnoreCase("") && email != null) {

            home_page_api=Global_Settings.home_page_api+"?cid="+login_group_id;
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/"+login_group_id;

        }
        else
        {
            home_page_api=Global_Settings.home_page_api+"?cid=0";
            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/0";
        }


        /**
         * Commenting for Selection of Zone
         * Ankesh Kumar
         * Sep 28, 2020
         */
        callHomeItemList(home_page_api);

    }


    private void callHomeItemList(String url_cart_item_list) {

        // progress_bar.setVisibility(View.VISIBLE);

        pDialog = new TransparentProgressDialog(StateSelectionDialog.this);
        pDialog.setCancelable(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                url_cart_item_list, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("responsebanner", response.toString());

                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        setPostOperation(response.toString());

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        //progress_bar.setVisibility(View.GONE);
                        //refreshItemCount();
                        CommonFun.showVolleyException(error,StateSelectionDialog.this);

                    }
                }
        ) {

        };
        jsObjRequest.setShouldCache(false);
        RetryPolicy retryPolicy=new DefaultRetryPolicy(1000*60,
                1,
                1);
        jsObjRequest.setRetryPolicy(retryPolicy);
        queue.add(jsObjRequest);


    }

    Realm realm;
    private void setPostOperation(String response) {

        //    progress_bar.setVisibility(View.GONE);
        // dataLoad=true;




        try
        {

            if(realm!=null) {
                if (realm.isClosed())
                    realm = Realm.getDefaultInstance();
            }
            else
            {
                realm=Realm.getDefaultInstance();
            }


            JSONObject jsonObj = null;
            jsonObj = new JSONObject(String.valueOf(response));
            JSONArray jsonArray_banner=jsonObj.getJSONArray("banners");
            String  st_cat_head=jsonObj.getString("category_title");



            if(realm.isClosed())
                realm=Realm.getDefaultInstance();


            realm.beginTransaction();
            for(int i=0;i<jsonArray_banner.length();i++){
                JSONObject jsonObject_category=jsonArray_banner.getJSONObject(i);

                String is_banner_category_offer=jsonObject_category.getString("banner_category");
                String catid = jsonObject_category.getString("cat_id");
                String catimage = jsonObject_category.getString("banner_name");
                String banner_image_sku= jsonObject_category.getString("sku");


                DataModelHomeAPI dataModelHomeAPI=realm.createObject(DataModelHomeAPI.class);

                dataModelHomeAPI.setCat_title(st_cat_head);
                dataModelHomeAPI.setP_catid(catid);
                dataModelHomeAPI.setP_image(catimage);
                dataModelHomeAPI.setP_banner_category(is_banner_category_offer);
                dataModelHomeAPI.setP_sku(banner_image_sku);
                dataModelHomeAPI.setP_name("");
                dataModelHomeAPI.setP_price("");


            }
            realm.commitTransaction();
            realm.close();


            Realm realm_1=Realm.getDefaultInstance();
            realm_1.beginTransaction();

            try{


                JSONObject jsonObj_p = jsonObj.getJSONObject("product_details");
                JSONArray jsonArray_product = jsonObj_p.getJSONArray("product_items");
                for (int i = 0; i < jsonArray_product.length(); i++) {

                    JSONObject jsonObject_product = jsonArray_product.getJSONObject(i);
                    String pname = jsonObject_product.getString("pname");
                    String pprice = "₹ " + jsonObject_product.getString("price");
                    String psku = jsonObject_product.getString("sku");
                    String pimage = jsonObject_product.getString("image");
                    String pip =  jsonObject_product.getString("ip");


                    String login_group_id = pref.getString("login_group_id", "");
                    if (login_group_id != null && !login_group_id.equals("")) {

                    } else {
                        login_group_id = "-";
                    }
                    //login_group_id="4";

//                    //Log.d("mvvmlog", login_group_id);
//
//                    ProductDataModel productDataModel = new ProductDataModel(pname, pip, "", psku, pimage,
//                            "", "", login_group_id);


                    ProductDataModel productDataModel=realm_1.createObject(ProductDataModel.class);
                    productDataModel.setPname(pname);
                    productDataModel.setIp(pip);
                    productDataModel.setPrice("");
                    productDataModel.setSku(psku);
                    productDataModel.setImage(pimage);
                    productDataModel.setP_category_id("");
                    productDataModel.setP_category_name("");
                    productDataModel.setLogin_user_id(login_group_id);



                    //  //Log.d("res_res", "commit_trans");
                }
                realm_1.commitTransaction();
            }
            catch (JSONException ex){

                realm_1.close();
            }

            //callNotificationData();
            goToHomePage();


        } catch (JSONException e) {


            e.printStackTrace();
        }
    }


    private void goToHomePage()
    {

        Intent intent = new Intent(StateSelectionDialog.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(StateSelectionDialog.this);

    }



}
