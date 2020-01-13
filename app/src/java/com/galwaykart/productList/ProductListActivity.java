package com.galwaykart.productList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.ProductBeanClass;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

/**
 * Show Product list of the given category
 * Created by sumitsaini on 9/12/2017.
 */

public class ProductListActivity extends BaseActivity implements RecyclerViewAdapter.ItemListener {

    SharedPreferences preferences;
    String st_selected_id = "",st_selected_name = "",st_customer_id="";
    //String st_URL = com.galwaykart.essentialClass.Global_Settings.api_url+"index.php/rest/V1/categories/";
    String st_URL = "";
    TransparentProgressDialog pDialog;
    JSONArray sku_code = null;
    String product_code = "";
    ArrayList<String> arr_product_oode;
    TextView tvItemName;
    TextView tvItemPrice;
    ImageView img_view_product_image;
    String st_product_image ="",product_name="",product_price="";
    String image_path = Global_Settings.api_url+"pub/media/catalog/product/";

    RecyclerView recyclerView;
    private List<DataModel> arrayList;
    DataModel model;
    private RecyclerViewAdapter adapter;
    TextView tv_category_view;
    TextView tv_alert;
    ImageView product_view;
    DatabaseHandler dbh;
    Timer timer;
    int page = 0;
    String onBack="";
    String [] banner_image = {"",
            ""};
    int total_banner_item;

    String category_banner_path="";

    /**
     * Added by Ankesh Kumar
     * Dec 27
     */
    Spinner spinner_sort;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent;

        /**
         * Check the previous page
         *
         */
        if (onBack.equalsIgnoreCase("home")) {
            intent = new Intent(ProductListActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            CommonFun.finishscreen(this);
        }
            else
            {

             intent = new Intent(ProductListActivity.this, ShowCategoryList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            CommonFun.finishscreen(this);

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        product_view = (ImageView) findViewById(R.id.img_cat_banner);



        initNavigationDrawer();
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras != null) {
                    onBack= extras.getString("onback");
                }
            }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /**
         * Sorting product dropdown
         */
        spinner_sort=(Spinner)findViewById(R.id.spinner_sorting);
            String[] item_sort_by=new String[]{"Sort","Sort By Name","Sort By Price","Sort By IP"};

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_item,item_sort_by);
            spinner_sort.setAdapter(arrayAdapter);
            spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==1) {
                       sortByName();
                        //recyclerView.invalidate();
                        //callOffline();
                        setDataArray();
                        recyclerView.invalidate();
                    }
                    else if(i==2) {
                        sortByPrice();
                        //recyclerView.invalidate();
                        //callOffline();
                        setDataArray();
                        recyclerView.invalidate();
                    }
                    else if(i==3) {
                        sortByIP();
                        //recyclerView.invalidate();
                        //callOffline();
                        setDataArray();
                        recyclerView.invalidate();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }


            });


            arrayAdapter.notifyDataSetChanged();

        }

    /**
     * Sort the product list by name
     */
    private void sortByName(){

        //Collections.sort(arrayList,(l1, l2)->l1.getst_tv_product_name().compareTo(l2.getst_tv_product_name()));

    }

    /**
     * sort the product list by price
     */
    private void sortByPrice(){

//        Collections.sort(arrayList,(l1, l2)-> {
//
//            if(Float.parseFloat(l1.getst_tv_product_price())>
//                    Double.parseDouble(l2.getst_tv_product_price()))
//                return 1;
//            else if(Float.parseFloat(l1.getst_tv_product_price())<
//                    Float.parseFloat(l2.getst_tv_product_price()))
//                return -1;
//            else
//                return 0;
//
//        });

    }


    /**
     * sort the product list by price
     */
    private void sortByIP(){

//        Collections.sort(arrayList,(l1, l2)-> {
//
//            if(Float.parseFloat(l1.getIP())>
//                    Double.parseDouble(l2.getIP()))
//                return 1;
//            else if(Float.parseFloat(l1.getIP())<
//                    Float.parseFloat(l2.getIP()))
//                return -1;
//            else
//                return 0;
//
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        dbh=new DatabaseHandler(ProductListActivity.this);


        arrayList = new ArrayList<>();
        arrayList.clear();
        arr_product_oode = new ArrayList<String>();

        tv_alert=(TextView)findViewById(R.id.tv_alert);

        preferences = getSharedPreferences("GalwayKart",MODE_PRIVATE);

        SharedPreferences pref;
        pref = CommonFun.getPreferences(getApplicationContext());
        st_selected_id = preferences.getString("selected_id","");
        st_selected_name = preferences.getString("selected_name","");
        st_customer_id = pref.getString("st_login_id","");

        tv_category_view=(TextView)findViewById(R.id.tv_category_view);
        tv_category_view.setText(st_selected_name);


        st_URL =  Global_Settings.api_url+"glaze/category_products_v1.php?id="+st_selected_id+"&cid="+st_customer_id;
        //st_URL =  "http://qa.galwaykart.com/"+"glaze/category_products_v1.php?id="+st_selected_id+"&cid="+st_customer_id;
        ////Log.d("st_URLdasfdsf",st_URL);

        /**
         * if data already fetched then call offline
         * get product details from the local database
         */
//        if(dbh.getProductCountByCatId(st_selected_id)>0) {
//            ////Log.d("call","calloffline");
//            callOffline();
//
//        }
//        else {
            ////Log.d("call","productcode");
            /**
             * call api to fetch details
             */
            getProductCode();
       // }

        // Product view as banner
        total_banner_item= banner_image.length;

        //ViewPagerAdapterBanner viewPagerAdapterBanner = new ViewPagerAdapterBanner(ProductListActivity.this, banner_image);

        //product_view.setAdapter(viewPagerAdapterBanner);
       // pageSwitcher(4);
    }

//    public void pageSwitcher(int seconds) {
//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new ProductListActivity.RemindTask(), 0, seconds * 2000); // delay
//        // in
//        // milliseconds
//    }

//    // this is an inner class...
//    public class RemindTask extends TimerTask {
//
//        @Override
//        public void run() {
//
//            // As the TimerTask run on a seprate thread from UI thread we have
//            // to call runOnUiThread to do work on UI thread.
//            runOnUiThread(new Runnable() {
//                public void run() {
//
//                    if (page == total_banner_item) { // In my case the number of pages are 5
//                        page = 0;
//                    } else {
//                        product_view.setCurrentItem(page++);
//                    }
//                }
//            });
//
//        }
//    }



    /**
     * if data already fetched then call offline
     * get product details from the local database
     * and fill in the adapter
     */
//    private void callOffline(){
//
//
//            List<ProductBeanClass> contacts = dbh.getAllProductByCatId(st_selected_id);
//
//
//
//            for (ProductBeanClass cn : contacts) {
//
//                arr_product_oode.add(cn.getSku());
//                arrayList.add(new DataModel(Global_Settings.image_url + cn.getImage(), cn.getName(), cn.getPrice(), cn.getSku()));
//
//            }
//
//        /**
//         * set all  data in the array and fill listview
//         */
//        setDataArray();
//
//        SharedPreferences pref_banner = getSharedPreferences("pref_banner",MODE_PRIVATE);
//
//        category_banner_path=pref_banner.getString(st_selected_id,"");
//        if(!category_banner_path.equals("") && !category_banner_path.equals("false") && category_banner_path!=null) {
//
//            ////Log.d("cat_banner","offline");
//            /**
//             * fetch and set category banner image
//             */
//            setCategoryBanner();
//        }
//        else
//        {
//            product_view.setVisibility(View.GONE);
//        }
//
//
//    }


    /**
     * fetch all product and store it in the local database
     */

    private void getProductCode() {



        pDialog = new TransparentProgressDialog(ProductListActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        if(response!=null){
                            try {

                                //Log.d("response",response.toString());

                                JSONObject jsonObject = new JSONObject(String.valueOf(response));

                                JSONArray custom_data= jsonObject.getJSONArray("categoryinfo");

                                int length_of_sku_code=custom_data.length();

                                String cat_image=jsonObject.getString(String.valueOf("categoryimage"));
                                ////Log.d("cat_image",cat_image);
                                if(!cat_image.equalsIgnoreCase("")&&!cat_image.equalsIgnoreCase("false"))
                                    category_banner_path=cat_image;


                                SharedPreferences pref_banner = getSharedPreferences("pref_banner",MODE_PRIVATE);

                                /**
                                 * save the banner url in temp storage
                                 */
                                SharedPreferences.Editor editor=pref_banner.edit();
                                editor.putString(st_selected_id,cat_image);
                                editor.commit();

                                if(length_of_sku_code>0) {

                                    for (int i = 0; i < length_of_sku_code; i++) {

                                        //  id":"2166","sku":"GRP29250","name":"BODY LOTION- ALMOND & SHEA BUTTER","image":"\/h\/o\/honey-butter-body-lotion.jpg","price":25

                                        JSONObject sku_c = custom_data.getJSONObject(i);

                                        String p_sku = sku_c.getString("sku");
                                        String p_name = sku_c.getString("name");
                                        String p_image = sku_c.getString("image");
                                        String p_price = sku_c.getString("price");
                                        String p_ip=sku_c.getString("ip");


                                        p_name=p_name.toUpperCase();

                                        Double d_price=Double.parseDouble(p_price);
                                        d_price=Math.round(d_price*100.0)/100.0;
                                        p_price=d_price.toString();



                                        dbh.insertProductDetails(new ProductBeanClass(st_selected_id,p_sku,p_name,p_image,p_price,"0"));
                                        arr_product_oode.add(p_sku);
                                        arrayList.add(new DataModel(Global_Settings.image_url + p_image, p_name, p_price, p_sku,p_ip));

                                    }
                                    spinner_sort.setVisibility(View.VISIBLE);
                                    setDataArray();
                                }
                                else
                                {
                                    String cat_name=st_selected_name.toLowerCase();

                                    /**
                                     * if category name is offer
                                     * then hide No Products Available message
                                     */
                                    if(cat_name.contains("offer"))
                                        tv_alert.setText("");
                                    else
                                        tv_alert.setText("No Products Available");

                                    tv_alert.setVisibility(View.VISIBLE);
                                    spinner_sort.setVisibility(View.GONE);
                                }
                                //callJSONAPIVolley();

                                /**
                                 * show category banner image
                                 */
                                setCategoryBanner();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());
                            }

                        }



                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                         CommonFun.showVolleyException(error,ProductListActivity.this);


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

    /**
     * show category banner image
     */

private void setCategoryBanner(){


        if(!category_banner_path.equalsIgnoreCase(""))
        {

            Picasso.with(this)
                    .load(category_banner_path)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    //.resize(200, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    //.fit()
                    .into(product_view);

        }

}

//    private void callJSONAPIVolley() {
//
//        String fromurl= Global_Settings.api_url+"index.php/rest/V1/products/GSG06100";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        //   CommonFun.alertError(MainActivity.this,response.toString());
//                        if(response!=null){
//                            try {
//
//                                JSONObject jsonObj = new JSONObject(String.valueOf(response));
//                                product_name=jsonObj.getString("name");
//
//                                product_price=jsonObj.getString("price");
//
//
//                                JSONArray custom_data= jsonObj.getJSONArray("custom_attributes");
//
//                                JSONObject c_product_img = custom_data.getJSONObject(2);
//                                st_product_image=c_product_img.getString("value");
//
//                                image_path = image_path+st_product_image;
//                                ////Log.d("image_path",image_path);
//
//                                setDataArray();
//
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//
//                    }
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
//                CommonFun.alertError(ProductListActivity.this,error.toString());
//                error.printStackTrace();
//
//
//            }
//        }){
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//
//        };
//
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        queue.add(jsObjRequest);
//
//
//    }

    /**
     * fill all product details in adapter
     * and show in listview
     */
    private void setDataArray() {

//        arrayList.add(new DataModel(image_path,product_name,product_price,product_code));
//        arrayList.add(new DataModel(com.galwaykart.essentialClass.Global_Settings.api_url+"pub/media/catalog/product/h/o/honey-almond-body-wash.jpg",
//                "BODY WASH- ALMOND &amp; HONEY","250",product_code));
//        ////Log.d("arrayList",""+arrayList);

        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
      //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        adapter = new RecyclerViewAdapter(ProductListActivity.this,arrayList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }




    @Override
    public void onItemClick(DataModel item) {
        Toast.makeText(getApplicationContext(), item.st_tv_product_name + " is clicked", Toast.LENGTH_SHORT).show();


    }
}
