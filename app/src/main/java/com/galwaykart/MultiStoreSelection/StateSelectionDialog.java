package com.galwaykart.MultiStoreSelection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.Cart.CartItemList;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class StateSelectionDialog extends AppCompatActivity {
    private static final int REQUEST_CODE_EXAMPLE = 0;
    Spinner spinner_state_profile;
    String region_code;
    String arr_state_code[];
    String arr_state_name[];
    String arr_id[];
    final String TAG_region_code= "code";
    final String TAG_region_name= "name";
    Button button_save_address;
    SharedPreferences pref;
    String current_user_zone="";
    boolean is_zone_called=false;
    TransparentProgressDialog  pDialog;
    String selId="";



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
                selId=arr_id[i];

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

                try(Realm realm=Realm.getDefaultInstance()) {
//            realm.beginTransaction();
//
//            RealmResults<DataModelRecentItem> results = realm.where(DataModelRecentItem.class).findAllAsync();
//            results.deleteAllFromRealm();
//            realm.commitTransaction();
//            realm.close();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    });
                    Log.d("res_res","deleteData");
                }
                catch (IllegalStateException ex){
                    Log.d("res_res_1",ex.getMessage());

                }
                catch (Exception ex){
                    Log.d("res_res_2",ex.getMessage());

                }
                finally {
                    //Log.d("res_res_3",ex.getMessage());

                }

                pref = CommonFun.getPreferences(StateSelectionDialog.this);

                String email = pref.getString("user_email", "");
                if (!email.equalsIgnoreCase("") && email != null) {

                    SharedPreferences pref = CommonFun.getPreferences(StateSelectionDialog.this);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("arr_id",selId);
                    editor.commit();
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

                    String guest_cart_id=pref.getString("guest_cart_id","");
                    if(guest_cart_id!=null && !guest_cart_id.equals("")) {

                        getCartId_v1(guest_cart_id);
                    }
                    else {
                        Intent intent = new Intent(StateSelectionDialog.this, SplashActivity.class);
                        startActivity(intent);
                        CommonFun.finishscreen(StateSelectionDialog.this);
                    }
                }


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
                        if(res==true) {
                            //     Log.d("ontrue","true");
                            SharedPreferences pref;
                            pref = CommonFun.getPreferences(StateSelectionDialog.this);
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("call_user_currentzone","done");
                            editor.commit();

                        }
                            Global_Settings.api_url=Global_Settings.web_url+region_code+"/";
                            Global_Settings.current_zone=region_code;

                        Intent intent = new Intent(StateSelectionDialog.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        CommonFun.finishscreen(StateSelectionDialog.this);


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
                                    arr_id=new String[jsonArray.length()];

                                    Log.d("responseState",jsonArray.toString());

                                    for(int i=0; i<jsonArray.length(); i++)
                                    {

                                        JSONObject  order_list_obj = jsonArray.getJSONObject(i);

                                        String st_region_id = order_list_obj.getString(TAG_region_code);
                                        String st_region_name = order_list_obj.getString(TAG_region_name);

                                        arr_state_code[i] = st_region_id;
                                        arr_state_name[i] = st_region_name;
                                        arr_id[i]=order_list_obj.getString("id");

                                    }


                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StateSelectionDialog.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
                                    spinner_state_profile.setAdapter(adapter);
                                    spinner_state_profile.setEnabled(true);




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


/**
 * Card merge logic
 */
String st_token_data = "";
private void getCartId_v1(String fromQuoteId) {

    String st_cart_URL = Global_Settings.api_url+"rest/V1/guest-carts";
    RequestQueue queue = Volley.newRequestQueue(StateSelectionDialog.this);

    st_token_data = pref.getString("tokenData","");

    // st_token_data="w4svj2g3pw7k2vmtxo3wthipjy3ieig0";


    pDialog = new TransparentProgressDialog(StateSelectionDialog.this);
    pDialog.setCancelable(false);
    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pDialog.show();


    final StringRequest jsObjRequest = new StringRequest(Request.Method.POST, st_cart_URL,
            new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    try {

                        String cart_id = response;
                        cart_id = cart_id.replaceAll("\"", "");

                        SharedPreferences.Editor editor= pref.edit();
                        editor.putString("guest_cart_id",cart_id);
                        editor.commit();


                        mergeCart(fromQuoteId,cart_id);




                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonFun.alertError(StateSelectionDialog.this, e.toString());
                    }

                }


            },


            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    if (error instanceof ServerError) {
                        //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            //Log.d("log_error", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
//                                String st_code = object.getString("code");

                                //Log.d("glog","updatecartitem");
                                CommonFun.alertError(StateSelectionDialog.this,st_msg);
//                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, StateSelectionDialog.this);
                            }


                        }

                    } else
                        CommonFun.showVolleyException(error, StateSelectionDialog.this);

                    //////Log.d("ERROR","error => "+error.toString());
                    //CommonFun.alertError(GuestStateSelectionDialog.this,error.toString());
                }
            }
    ) {


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization", "Bearer " + st_token_data);
            //   params.put("Content-Type","application/json");

            return params;
        }
    };
    jsObjRequest.setShouldCache(false);
    queue.add(jsObjRequest);

}

private void mergeCart(String fromQuoteId,String toQuoteId) {

        String st_cart_URL = Global_Settings.api_url+"rest/V1/m-guest-carts/merge";
        RequestQueue queue = Volley.newRequestQueue(StateSelectionDialog.this);

        String input_data="{" +
                "\"fromQuoteId\":\""+fromQuoteId+"\"," +
                "\"toQuoteId\":\""+toQuoteId+"\"" +
                "}";

        pDialog = new TransparentProgressDialog(StateSelectionDialog.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();


        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST, st_cart_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            Boolean res=Boolean.parseBoolean(response);
                            Log.d("st_url_MVVM_search",res.toString());
                            if(res==true){
                                Intent intent = new Intent(StateSelectionDialog.this, SplashActivity.class);
                                startActivity(intent);
                                CommonFun.finishscreen(StateSelectionDialog.this);
                            }
                            else
                            {
                                Intent intent = new Intent(StateSelectionDialog.this, SplashActivity.class);
                                startActivity(intent);
                                CommonFun.finishscreen(StateSelectionDialog.this);
                            }




                        } catch (Exception e) {
                            e.printStackTrace();
                            //CommonFun.alertError(StateSelectionDialog.this, e.toString());
                            Intent intent = new Intent(StateSelectionDialog.this, SplashActivity.class);
                            startActivity(intent);
                            CommonFun.finishscreen(StateSelectionDialog.this);
                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        if (error instanceof ServerError) {
                            //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
                            NetworkResponse response = error.networkResponse;
                            String errorMsg = "";
                            if(response != null && response.data != null){
                                String errorString = new String(response.data);
                                //Log.d("log_error", errorString);

                                try {
                                    JSONObject object = new JSONObject(errorString);
                                    String st_msg = object.getString("message");
//                                String st_code = object.getString("code");

                                    //Log.d("glog","updatecartitem");
                                    CommonFun.alertError(StateSelectionDialog.this,st_msg);
//                                //Log.d("st_code",st_code);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    CommonFun.showVolleyException(error, StateSelectionDialog.this);
                                }


                            }

                        } else
                            CommonFun.showVolleyException(error, StateSelectionDialog.this);

                        //////Log.d("ERROR","error => "+error.toString());
                        //CommonFun.alertError(GuestStateSelectionDialog.this,error.toString());
                    }
                }
        ) {

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
                Log.d("st_url_MVVM_search",input_data);
                return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
            }

        };
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }


}
