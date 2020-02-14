package com.galwaykart.address_book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddressBookRepo {

    private static AddressBookRepo instance;
    private ArrayList<AddressDataModel> dataset=new ArrayList<>();
    Boolean is_AddressDeleted;

    final String TAG_telephone= "telephone";
    final String TAG_postcode = "postcode";
    final String TAG_city = "city";
    final String TAG_firstname= "firstname";
    final String TAG_lastname = "lastname";
    final String TAG_customer_id = "customer_id";
    final String TAG_id= "id";
    String tokenData="";



    public static AddressBookRepo getInstance(){

        if(instance==null){
            instance=new AddressBookRepo();
        }
        return instance;
    }


    public MutableLiveData<List<AddressDataModel>> getAddress(Context context,String st_come_from_update){
        dataset.clear();

        MutableLiveData<List<AddressDataModel>> data=new MutableLiveData<>();
        callAddressBookList(context,data,st_come_from_update);
        data.setValue(dataset);
        return data;
    }

    private void  callAddressBookList(Context context,
                                      MutableLiveData<List<AddressDataModel>> data,
                                      String st_come_from_update){

        SharedPreferences pref;
        pref=CommonFun.getPreferences(context);
        tokenData = pref.getString("tokenData", "");
        tokenData = tokenData.replaceAll("\"", "");

        String url_address = Global_Settings.api_url + "rest/V1/customers/me";



        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_address, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Log.d("mvvm",response.toString());

                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(String.valueOf(response));

                            JSONArray addressArray = jsonObject.getJSONArray("addresses");
                            int total_address_count=addressArray.length();


                            for(int i=0;i<addressArray.length();i++) {

                                JSONObject jsonObj=addressArray.getJSONObject(i);
                                String address_id = jsonObj.getString(TAG_id);


                                String arr_default_ship="";
                                if(jsonObj.has("default_shipping"))
                                   arr_default_ship="1";
                                else
                                    arr_default_ship="0";

                                String arr_default_bill="";
                                if(jsonObj.has("default_billing"))
                                    arr_default_bill="1";
                                else
                                    arr_default_bill="0";

                                JSONObject object = jsonObj.getJSONObject("region");


                                AddressDataModel addressDataModel=new AddressDataModel(
                                                        object.getString("region"),
                                                        object.getString("region_id"),
                                                        object.getString("region_code"),
                                                        jsonObj.getString(TAG_telephone),
                                                        jsonObj.getString(TAG_postcode),
                                                        jsonObj.getString(TAG_city),
                                                        object.getString("region"),
                                                        jsonObj.getString(TAG_firstname),
                                                        jsonObj.getString(TAG_lastname),
                                                        jsonObj.getString(TAG_customer_id),
                                                        jsonObj.getString(TAG_id),
                                         "",
                                                        arr_default_ship,
                                                        arr_default_bill,
                                        st_come_from_update.equals(""),
                                                true,true);

                                dataset.add(addressDataModel);

                            }

                            data.postValue(dataset);



                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // //Log.d("ERROR", "error => " + error.toString());


                        try {
                            // CommonFun.alertError(CustomerAddressBook.this,"No address");


                            //  JSONObject JO = new JSONObject(error.toString());
                            //  CommonFun.alertError(LoginActivity.this, error.toString());

                            String body;
                            //get status code here
                            // String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            if(error.networkResponse.data!=null) {
                                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                CommonFun.alertError(context, body);
                                // JSONObject jsonObject=new JSONObject(body.toString());
                                // String message=jsonObject.getString("message");
                                //CommonFun.alertError(CustomerAddressBook.this,message);

                            }
                        }catch (Exception e){

                            e.printStackTrace();
                        }

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
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsObjRequest.setShouldCache(false);

        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    public MutableLiveData<Boolean> callDeleteAddress(Context mContext,List<AddressDataModel> listAddressDataModel,AddressDataModel saddressDataModel) {
        MutableLiveData<Boolean> is_deleted=new MutableLiveData<>();
        callDeleteAddressData(mContext,listAddressDataModel,saddressDataModel,is_deleted);
        is_deleted.setValue(is_AddressDeleted);
        return is_deleted;
    }

        private void callDeleteAddressData(Context mContext,List<AddressDataModel> listAddressDataModel,
                                           AddressDataModel saddressDataModel,MutableLiveData<Boolean> is_deleted) {



        SharedPreferences pref = CommonFun.getPreferences(mContext);
        String email = pref.getString("user_email", "");
        String tokenData = pref.getString("tokenData", "");
        String  st_attribute_value_mob =  saddressDataModel.st_telephone;
        String st_add_id="";
        String address_id=saddressDataModel.st_address_id;

        if(listAddressDataModel.size()>0){

            for(int i=0;i<listAddressDataModel.size();i++) {
                if (st_add_id.equals("")) {
                    if(! listAddressDataModel.get(i).st_address_id.equals(address_id))
                        st_add_id = "{\"id\":" + listAddressDataModel.get(i).st_address_id + "}";
                }
                else {
                    if(! listAddressDataModel.get(i).st_address_id.equals(address_id))
                        st_add_id = st_add_id + "," + "{\"id\":" + listAddressDataModel.get(i).st_address_id + "}";
                }
            }
        }

        String login_group_id=pref.getString("login_group_id","");

        String return_data = "{\"customer\":" +
                "{\"email\":\"" + email + "\"," +
                "\"lastname\":\"" + saddressDataModel.st_customer_last_name + "\",\"group_id\":"+login_group_id+","+
                "\"custom_attributes\":[{\"value\":\""+st_attribute_value_mob+"\",\"attribute_code\":\"mobile_number\"}]," +
                "\"addresses\":" +
                "[" +
                st_add_id +"]," +
                "\"website_id\":\"1\"," +
                "\"store_id\":\"1\"," +
                "\"firstname\":\"" +saddressDataModel.st_customer_first_name + "\"}}";


        //Log.d("return_data",return_data);

        try {
            String save_address_url = Global_Settings.api_url + "rest/V1/customers/me";

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, save_address_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            is_AddressDeleted=true;
                            is_deleted.postValue(is_AddressDeleted);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    is_AddressDeleted=false;
                    is_deleted.postValue(is_AddressDeleted);

                }
            }) {


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
                    return return_data == null ? null : return_data.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();


                    //////Log.d("delievery data in",delivery_data_in.toString());
                    headers.put("Authorization", "Bearer " + tokenData);
                    //headers.put("Content-Type","application/json");
                    return headers;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
            //VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            is_AddressDeleted=false;
            is_deleted.postValue(is_AddressDeleted);

            e.printStackTrace();
            //////Log.d("error...","Error");
        }

    }
}
