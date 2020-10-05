package com.galwaykart.dbfiles.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.galwaykart.RoomDb.GalwaykartRoomDatabase;
import com.galwaykart.HomePageTab.DataModelHomeAPI;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;

public class MVVM_HomePage_TopProductRepo {

    private static MVVM_HomePage_TopProductRepo instance;
    private ArrayList<ProductDataModel> dataset=new ArrayList<>();

    public static MVVM_HomePage_TopProductRepo getInstance(){
        if(instance==null){
            instance=new MVVM_HomePage_TopProductRepo();
        }
        return instance;
    }


//    /******************************************Top Products****************************************************/
//    /**
//     * Get Top Products for HomePage
//     * @param context
//     * @return
//     */
    public MutableLiveData<List<ProductDataModel>> getProduct(Context context){
       dataset.clear();
        MutableLiveData<List<ProductDataModel>> data = new MutableLiveData<>();
        callAPI(context, data);
        data.setValue(dataset);
        return data;

    }

    String login_group_id="";
    /**
     * Get Top Products for HomePage
     * @param context
     * @param data

     */
    private void callAPI(Context context,
                         MutableLiveData<List<ProductDataModel>> data
                        ) {
//        SharedPreferences pref;
//        pref= CommonFun.getPreferences(context);
//
//
//        String email = pref.getString("user_email", "");
//         login_group_id=pref.getString("login_group_id","");
//        String home_page_api="";
//
//        if (!email.equalsIgnoreCase("") && email != null) {
//            //home_page_api= Global_Settings.home_page_api+"?cid="+login_group_id;
//            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/"+login_group_id;
//        }
//        else
//        {
//            //home_page_api=Global_Settings.home_page_api+"?cid=0";
//            home_page_api= Global_Settings.api_url+"/rest/V1/mobile/home/0";
//        }

        setPostOperation(context, data);

        //        RequestQueue queue = Volley.newRequestQueue(context);
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
//                home_page_api, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //Log.d("resMVVM", response.toString());
//
//                        //setPostOperation(response.toString(),context);
//
//                        setPostOperation(context,response, data);
//
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//
//                    }
//                }
//        ) {
//        };
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        jsObjRequest.setShouldCache(false);
//        queue.add(jsObjRequest);

    }
    /**
     * Post Operation Top Products for HomePage
     * @param context
     * @param data
     */
    private void setPostOperation(Context context,
                                  MutableLiveData<List<ProductDataModel>> data) {
        try(Realm realm=Realm.getDefaultInstance()) {

            String response="";

            RealmResults<ProductDataModel> results=
                    realm.where(ProductDataModel.class)
                    .findAllAsync();

            results.load();
            response=results.asJSON();
            realm.close();


            Log.d("topProduct",response.toString());

            JSONArray jsonArray_product = new JSONArray(response);

            for(int i=0;i<jsonArray_product.length();i++){

                JSONObject jsonObject_product=jsonArray_product.getJSONObject(i);
                String pname=jsonObject_product.getString("pname");
                String pprice="₹ "+jsonObject_product.getString("price");
                String psku=jsonObject_product.getString("sku");
                String pimage=jsonObject_product.getString("imageUrl");
                String pip="PV / BV: "+jsonObject_product.getString("ip");

                SharedPreferences pref =  CommonFun.getPreferences(context);
                String login_group_id=pref.getString("login_group_id","");
                if(login_group_id!=null && !login_group_id.equals(""))
                {

                }
                else
                {
                    login_group_id="-";
                }

                //login_group_id="4";
                Log.d("topProduct","GroupID"+login_group_id);

                ProductDataModel productDataModel= new ProductDataModel(pname,pip,"",psku,pimage,
                        "","",login_group_id);

                dataset.add(productDataModel);

//                new insertProductAsyncTask(productDataModelDao)
//                        .execute(productDataModel);
                //  itemdProductList.add(new DataModelHomeProduct(pname,pprice,psku,pimage,"IP "+pip));
                // setProductAdapter();

            }

            data.postValue(dataset);

            //   data.postValue(dataset);
            // getProduct(context,false);

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }



//    private static class insertProductAsyncTask extends AsyncTask<ProductDataModel, Void, Void> {
//
//        //private ProductDataModelDao mAsyncTaskDao;
//
//        insertProductAsyncTask(ProductDataModelDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final ProductDataModel... params) {
//            mAsyncTaskDao.insert(params[0]);
//            //Log.d("new_insert","success");
//            return null;
//        }
//    }


//
//    /**************************************Category Products********************************************************/
//    /**
//     * Sort Category Product
//     */
    public MutableLiveData<List<ProductDataModel>> sortByPrice(Context context,MutableLiveData<List<ProductDataModel>> data,int sortBy){

        switch (sortBy) {

            case 1:
                Collections.sort(data.getValue(),(l1, l2)->
                        l1.getPname().compareTo(l2.getPname()));
                break;
            case 2:
            Collections.sort(data.getValue(), (l1, l2) -> {

                if (Float.parseFloat(l1.getPrice().replace("₹","")) >
                        Double.parseDouble(l2.getPrice().replace("₹","")))
                    return 1;
                else if (Float.parseFloat(l1.getPrice().replace("₹","")) <
                        Float.parseFloat(l2.getPrice().replace("₹","")))
                    return -1;
                else
                    return 0;

            });
            break;
            case 3:
                Collections.sort(data.getValue(), (l1, l2) -> {

                    if (Float.parseFloat(l1.getIp().replace("IP","")) >
                            Double.parseDouble(l2.getIp().replace("IP","")))
                        return 1;
                    else if (Float.parseFloat(l1.getIp().replace("IP","")) <
                            Float.parseFloat(l2.getIp().replace("IP","")))
                        return -1;
                    else
                        return 0;

                });
                break;

        }
        data.postValue(data.getValue());
    return data;
    }



    /******************************************Search Products And Category Products****************************************************/


    public MutableLiveData<List<ProductDataModel>> getProductDetail(Context context,String query,Boolean is_search){
        dataset.clear();
        MutableLiveData<List<ProductDataModel>> data=new MutableLiveData<>();
        MutableLiveData<Boolean> isDataLoad=new MutableLiveData<>();

        callAPIOfProductDetail(context,data,isDataLoad, query, is_search);
        data.setValue(dataset);
        return data;
    }

    /**
     * Get Category Products
     * @param context
     * @param data
     */
    private void callAPIOfProductDetail(Context context, MutableLiveData<List<ProductDataModel>> data,
                                        MutableLiveData<Boolean>isDataLoad,String query,
                                        Boolean is_search) {

        SharedPreferences preferences = context.getSharedPreferences("GalwayKart", MODE_PRIVATE);

        SharedPreferences pref;
        pref = CommonFun.getPreferences(context);
        String st_selected_id = preferences.getString("selected_id","");
        String st_selected_name = preferences.getString("selected_name","");
        if(st_selected_name!=null && !st_selected_name.equals("")){

        }
        else
        {
            st_selected_name="";
        }
        //st_customer_id = pref.getString("st_login_id","");

//        Guest ="274" 0
//        Employee" value="275" 5
//        Distributor" value="276" 4
//        General Customer" value="277 1

        //dev.galwaykart.com
        //const ProductCustomerGroupDistributor = 276;
        //const ProductCustomerGroupEmployee = 275;
        //const ProductCustomerGroupCustomer = 277;
        //const ProductCustomerGroupGuset = 274;


        String login_group_id=pref.getString("login_group_id","");
        String api_login_id="274";
        //String api_login_id="274"; //live
        //String api_login_id="284";
        if(login_group_id.equalsIgnoreCase("4"))
           // api_login_id="289";
            api_login_id="276";
          //  api_login_id="285";
        else if(login_group_id.equalsIgnoreCase("5"))
            //api_login_id="290";
            api_login_id="275";
            //api_login_id="286";
        if(login_group_id.equalsIgnoreCase("1"))
            api_login_id="277";
            //api_login_id="277";
            //api_login_id="287";
        if(login_group_id.equalsIgnoreCase("0"))
            //api_login_id="288";
            api_login_id="274";
            //api_login_id="284";
//        ProductCustomerGroupDistributor = 289;
//const ProductCustomerGroupEmployee = 290;
//const ProductCustomerGroupCustomer = 291;
//  const ProductCustomerGroupGuset = 288;
        String st_URL="";

        if(is_search==true) {
            st_URL = Global_Settings.api_url + "rest/V1/products?searchCriteria[filter_groups][0][filters][1][field]=name" +
                    "&searchCriteria[filter_groups][0][filters][1][value]=%25" + query +
                    "%25&searchCriteria[filter_groups][0][filters][1][condition_type]=like" +
                    "&searchCriteria[filter_groups][2][filters][1][field]=productgroup" +
                    "&searchCriteria[filter_groups][2][filters][1][condition_type]=finset" +
                    "&searchCriteria[filter_groups][2][filters][1][value]=" + api_login_id;
        }
        else
        {
          st_URL=Global_Settings.api_url+"rest/V1/products?"+
                    "searchCriteria[filter_groups][0][filters][0][field]=category_id"+
                    "&searchCriteria[filter_groups][0][filters][0][value]="+st_selected_id+
                    "&searchCriteria[filter_groups][0][filters][1][field]=visibility"+
                    "&searchCriteria[filter_groups][0][filters][1][value]=4"+
                    "&searchCriteria[filter_groups][1][filters][0][field]=status"+
                    "&searchCriteria[filter_groups][1][filters][0][value]=1"+
                    "&searchCriteria[sortOrders][0][field]=name"+
                    "&searchCriteria[sortOrders][0][direction]=asc"+
                    "&searchCriteria[filter_groups][2][filters][1][field]=productgroup" +
                    "&searchCriteria[filter_groups][2][filters][1][condition_type]=finset"+
                    "&searchCriteria[filter_groups][2][filters][1][value]="+api_login_id;


        }

        //Log.d("st_url_MVVM",st_URL);

        isDataLoad.postValue(true);

        RequestQueue queue = Volley.newRequestQueue(context);
        String finalSt_selected_name = st_selected_name;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        //Log.d("resMVVM_Product",response.toString());

                        setPostProductDetail(context, response,data, finalSt_selected_name);


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();

                CommonFun.showVolleyException(error,context);


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
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);
    }





    /**
     * Post Operation Category Products
     * @param context
     * @param response
     * @param data
     */
    private void setPostProductDetail(Context context,String response,
                                      MutableLiveData<List<ProductDataModel>> data,String st_selected_name
    ) {

        if(response!=null){
            //Log.d("resProduct",response.toString());
            dataset.clear();
            try {

                JSONObject jsonObject = new JSONObject(response);

                int total_count= Integer.parseInt(jsonObject.getString("total_count"));
                if(total_count>0) {


                    JSONArray custom_data = jsonObject.getJSONArray("items");


                    int length_of_sku_code = custom_data.length();

                    for (int i = 0; i < length_of_sku_code; i++) {

                        //  id":"2166","sku":"GRP29250","name":"BODY LOTION- ALMOND & SHEA BUTTER","image":"\/h\/o\/honey-butter-body-lotion.jpg","price":25

                        JSONObject sku_c = custom_data.getJSONObject(i);

                        String p_status = sku_c.getString("status");
                        if(p_status.equals("1")) {
                            String p_sku = sku_c.getString("sku");
                            String p_name = sku_c.getString("name");
                            // String p_image = sku_c.getString("image");


                            String p_price = "";
                            if (sku_c.has("price")) {
                                p_price = sku_c.getString("price");
                                Double d_price = Double.parseDouble(p_price);
                                d_price = Math.round(d_price * 100.0) / 100.0;
                                p_price = d_price.toString();
                            }

                            p_name = p_name.toUpperCase();


                            String p_image = "";
                            String p_ip = "";
                            JSONArray jsonArray_custom_attrib = sku_c.getJSONArray("custom_attributes");

                            if (jsonArray_custom_attrib.length() > 0) {
                                for (int k = 0; k < jsonArray_custom_attrib.length(); k++) {

                                    JSONObject jsonObject_image = jsonArray_custom_attrib.getJSONObject(k);

                                    String attribute_code = jsonObject_image.getString("attribute_code");
                                    if (attribute_code.equalsIgnoreCase("thumbnail")) {

                                        p_image = jsonObject_image.getString("value");
                                        //Log.d("resMVVM", p_image);
                                    }

                                    if (attribute_code.equalsIgnoreCase("ip")) {

                                        p_ip = jsonObject_image.getString("value");
                                        //Log.d("resMVVM", p_image);
                                    }
                                }


                            }

                            if (!p_image.equals("")) {
                                p_image = Global_Settings.image_url + p_image;
                            }


                            SharedPreferences pref = CommonFun.getPreferences(context);
                            String login_group_id = pref.getString("login_group_id", "");
                            if (login_group_id != null && !login_group_id.equals("")) {

                            } else {
                                login_group_id = "-";
                            }
                            // login_group_id="4";


                            jsonObject = custom_data.getJSONObject(i);

                            JSONArray json_custom_price = jsonObject.getJSONArray("tier_prices");

                            // ////Log.d("MyLog",jsonArraysunject+"");


                            int tier_price_length = json_custom_price.length();
                            if (tier_price_length > 0) {

                                for (int j = 0; j < tier_price_length; j++) {

                                    JSONObject custom_obj = json_custom_price.getJSONObject(j);
                                    String st_customer_gp_id = custom_obj.getString("customer_group_id");
                                    String st_tier_qty = custom_obj.getString("qty");
                                    String st_tier_price = custom_obj.getString("value");

                                    if (st_customer_gp_id.equalsIgnoreCase(login_group_id)) {
                                        p_price = st_tier_price;
                                    }
                                }
                            }


                            ////Log.d("mvvmlog",login_group_id+" "+st_selected_name);

                            dataset.add(new ProductDataModel(p_name, "PV / BV " + p_ip, "₹ " + p_price, p_sku, p_image,
                                    "", st_selected_name, login_group_id));

                        }
                        //data.postValue(dataset);
                    }

                    data.postValue(dataset);
                }
                else
                {
                    dataset.add(new ProductDataModel("", "", "", "", "",
                            "", st_selected_name, ""));

                    data.postValue(dataset);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                ////Log.d("error",e.toString());
            }

        }
    }

}
