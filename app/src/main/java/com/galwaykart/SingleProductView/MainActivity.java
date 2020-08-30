package com.galwaykart.SingleProductView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.Cart.DataModelCart;
import com.galwaykart.Cart.DataModelRecentItem;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.ViewPagerAdapterSingleProduct;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.productList.ProductListActivity;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


/**
 * Showing single product details
 * user can share, add to whitelist
 * See review and give rating
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
   Spinner spinner1, spinner2, spinner_qty;

    TextView tvItemName,tv_add_to_cart;
    TextView tvItemPrice,tvItemShort,tvItemLong,tv_associate_products,tv_associate_products_details;
    String short_desc,long_desc,st_product_image;
    String image_path = Global_Settings.api_url+"pub/media/catalog/product";
    String or_image_path = Global_Settings.api_url+"pub/media/catalog/product";
    String image_path1 = Global_Settings.api_url+"pub/media/catalog/product";
    ImageView img_view_product_image;
    String st_add_to_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine/items/";

    String st_token_data = "";
    String st_cart_URL = Global_Settings.api_url+"rest/V1/carts/mine";
    String add_to_cart_URL ="";
    SharedPreferences preferences;
    String token_data ="";
    String st_customer_gp_id="",st_tier_qty="",st_tier_price="";
    String cartItem;
    ArrayList<DataModelCart> arrayList;
    //    JSONArray  cartItem = null;
    TransparentProgressDialog pDialog;
    Toolbar toolbar;
    String  tokenData;
    EditText ed_qty;
    Boolean showing_txt_qty_control=false;
    String selqty="";
    DatabaseHandler dbh;
    String comefrm="";
    TextView tv_Wish_list,tvDiscPrice;
    String st_add_wish_list_URL = "";
    String st_product_id="";
    String st_customer_id="";
    String st_logged_gp_id = "";
    String product_price="";
    TextView tvItemLong_title;
    Boolean long_d_hide;
    String or_long_desc="";
    int available_stock=0;
    Boolean dataload=false;
    String ip_of_product="";
    String[] arr_product_images;
    ViewPager pager_view_products;
    Timer timer;
    int page = 0;
    int permission_status;
    private LinearLayout pager_indicator;

    ImageView[] dots;
    RatingBar product_rating,product_rating_new;
    SharedPreferences pref;
    TextView img_share;
    String[] review_title,review_detail,review_nickname,review_rating;

    ArrayList<HashMap<String,String>> detailsList;
    private String TAG_title="title";
    private String TAG_detail="detail";
    private String TAG_nickname="nickname";
    private String TAG_rating="rating";
    public static final int Request_Id_Multiple_Permissions=1;
    ListView list_review;

    RecyclerView recyclerView_Rating;

    TextView tv_review_alert,tv_availability_response,btn_change_pin;
    private List<DataModelProductReview> itemRatingList;
    TextView tv_write_review,btn_check_availablity;
    EditText ed_pincode;


    /**
     * Write review layout controls
     */
    LinearLayout linear_write_review;

    EditText ed_nickname,ed_comment;
    RatingBar ed_rating;
    Button btn_submit_review;
    ProgressBar progressBar_write_review,progressBar_read_review;

    Boolean show_recent_item;
    private EndlessRecyclerViewScrollListener scrollListener;
    JSONArray dist_details = null;
    TableLayout check_available,check_available_response;

    //String st_sales_user_zone="";
    String st_pincode="",check_pin_url="";
    RadioButton live,local;
    RadioGroup toggle;
    LinearLayout llayout_product;
    RelativeLayout rel_layout_review;

    ImageView img_close_write_review;
    //String st_magento_user_zone="";

     Dialog dialog;
     Boolean is_product_on_wishlist=false;

    String mRequestBody="";
    Boolean product_type_id=false;
    String product_type="";
    ArrayList<String> id_bundal = new ArrayList<String>();
    ArrayList<String> option_id_bundal = new ArrayList<String>();
    ArrayList<String> qty_bundal = new ArrayList<String>();
    int json_len_bundal=0;
    String stBundle= "";
    String stInputDataBundle="";

    ArrayList<String> str_name_bundal = new ArrayList<String>();
    ArrayList<String> str_sku_bundal = new ArrayList<String>();

    RecyclerView recycle_view_size;
    RelativeLayout rel_horizontal_view;
    String st_hamper_desc="";
    Realm realm;
    /**
     * Check if user zone details already fetch
     */
     // Boolean user_details_already_fetch=false;

//..................................................Configurable Product Change.......................................................................

    String stTypeId ="",stProductLabel="",product_sku="",stConfiguredSKU="",stConfigDefaultSKU="",
            stSKU="",stSelectedSKU="",stConfigredPrice="",stSelectedAttIdColor="",stSelectedAttIndexColor="",
            stDefaultProduct="",stSelectedAttIdSize="",stSelectedAttIndexSize="",
            status_product="",stSelectedValue="",stValuePosition="",stValueImage="";
    LinearLayout recycle_view_Container;
    String [] arr_label,arr_value_lable,arr_configured_sku,arr_configured_price,arr_value_index_color,arr_value_att,
            arr_value_lable_size,arr_value_lable_color,arr_value_index_size,arr_attributeId_color,arr_attributeId_size,
            arr_value_index,arr_image_value,arrClickValue;

    String[] arr_value_index_clicked_color,arr_value_index_clicked_size;
    int label_length,value_length,count = 0,value_length_color,value_length_size;
    ConfigurableProductDataModel productDataModel_size;
    ConfigurableProductDataModel productDataModel_color;
    ArrayDataModel arrayDataModel;
    ArrayList<ConfigurableProductDataModel> array_value_list_color;
    ArrayList<ConfigurableProductDataModel> array_value_list_size;
    ArrayList<ArrayDataModel> arrVlaueLabel;
    CustomAttributeSizeAdapter mAdapter_size;
    CustomAttributeColorAdapter mAdapter_color;


    String[] getHashMap;


    //    String [][] arr_item_value;
    RecyclerView dynamicRecyclerView;
    JSONObject jsonObj;
    JSONArray jsonArray_2;
    LinearLayout.LayoutParams layoutParams,layoutParams1,layoutParamsMain;
    RecyclerView recycle_view_color;
    String sel_p_sku="",sel_p_color="",sel_p_size="",sel_p_json="",sel_p_price="";
    Boolean is_color_availble=false,is_size_availble=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//            if(!comefrm.equals("")) {
//                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                CommonFun.finishscreen(this);
//            }
//            else
//            {
//                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                CommonFun.finishscreen(this);
//            }
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm.isClosed()){

        }
        else
        {
            realm.close();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initNavigationDrawer();

        realm = Realm.getDefaultInstance();
        pref = CommonFun.getPreferences(getApplicationContext());

        id_bundal = new ArrayList<String>();
        option_id_bundal = new ArrayList<String>();
        qty_bundal = new ArrayList<String>();
        str_name_bundal = new ArrayList<String>();
        str_sku_bundal = new ArrayList<String>();

        show_recent_item=false;
        img_close_write_review= findViewById(R.id.img_close_write_review);

        llayout_product= findViewById(R.id.llayout_product);
        rel_layout_review= findViewById(R.id.rel_layout_review);
        product_rating_new= findViewById(R.id.product_rating_new);
        live= findViewById(R.id.live);

        //recycle_view_size = (RecyclerView)findViewById(R.id.recycle_view_size);
        //recycle_view_Container = (LinearLayout)findViewById(R.id.recycle_view_Container);

        recycle_view_size= findViewById(R.id.recycle_view_size);
        recycle_view_color= findViewById(R.id.recycle_view_color);
        rel_horizontal_view= findViewById(R.id.rel_horizontal_view);
        rel_horizontal_view.setVisibility(View.GONE);


        local= findViewById(R.id.local);
        toggle= findViewById(R.id.toggle);
        live.setChecked(true);
        live.setTextColor(getResources().getColor(R.color.colorwhite));
        local.setTextColor(getResources().getColor(R.color.black));
        llayout_product.setVisibility(View.VISIBLE);
        rel_layout_review.setVisibility(View.GONE);
        product_rating_new.setVisibility(View.VISIBLE);


        tokenData = pref.getString("tokenData", "");
        //Log.d("tokenData:::::",tokenData);

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.live) {
                    //do work when radioButton1 is active
                    live.setChecked(true);
                    live.setTextColor(getResources().getColor(R.color.colorwhite));
                    local.setTextColor(getResources().getColor(R.color.black));
                    llayout_product.setVisibility(View.VISIBLE);
                    rel_layout_review.setVisibility(View.GONE);
                    product_rating_new.setVisibility(View.VISIBLE);

                } else  if (checkedId == R.id.local) {
                    //do work when radioButton2 is active
                    live.setChecked(false);
                    live.setTextColor(getResources().getColor(R.color.black));
                    local.setTextColor(getResources().getColor(R.color.colorwhite));
                    llayout_product.setVisibility(View.GONE);
                    rel_layout_review.setVisibility(View.VISIBLE);
                    product_rating_new.setVisibility(View.GONE);
                }

            }
        });

        list_review= findViewById(R.id.list_review);
        list_review.setVisibility(View.GONE);

        itemRatingList=new ArrayList<>();

        ed_comment= findViewById(R.id.ed_nickname);
        ed_nickname= findViewById(R.id.ed_comment);
        ed_rating= findViewById(R.id.ed_rating);

        progressBar_write_review= findViewById(R.id.progressBar_write_review);
        progressBar_write_review.setVisibility(View.GONE);
        progressBar_read_review= findViewById(R.id.progressBar_read_review);
        progressBar_read_review.setVisibility(View.GONE);

        check_available_response = findViewById(R.id.check_available_response);
        check_available = findViewById(R.id.check_available);

        btn_check_availablity = findViewById(R.id.btn_check_availablity);

        btn_change_pin= findViewById(R.id.btn_change_pin);
        tv_availability_response= findViewById(R.id.tv_availability_response);

        ed_pincode = findViewById(R.id.ed_pincode);

        btn_check_availablity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                st_pincode = ed_pincode.getText().toString();
                //Log.d("st_pincode",st_pincode);

                if(st_pincode.equalsIgnoreCase(""))
                    CommonFun.alertError(MainActivity.this,"Please enter valid pincode...");
                 else if(st_pincode.length() < 6){
                    CommonFun.alertError(MainActivity.this,"Please enter valid pincode...");
                }
                else{
                    pref= CommonFun.getPreferences(getApplicationContext());
                    final String product_sku=pref.getString("showitemsku","");
                    check_pin_url=Global_Settings.api_custom_url+"pincode_check.php?postcode="+
                            st_pincode+"&sku="+product_sku;

                    checkPinCode();
                }



            }
        });
        btn_change_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_available_response.setVisibility(View.GONE);
                check_available.setVisibility(View.VISIBLE);
            }
        });
        btn_submit_review= findViewById(R.id.btn_submit_review);
        btn_submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * submit review check
                 */
                int r_bar= (int) ed_rating.getRating();
                String st_com=ed_comment.getText().toString();
                String st_nick=ed_nickname.getText().toString();

                if(r_bar==0 || st_com.equals("") || st_nick.equals(""))
                {
                    CommonFun.alertError(MainActivity.this,"All fields are mandatory to submit review");
                }
                else
                    submitReview(st_com,st_nick);
            }
        });

        linear_write_review= findViewById(R.id.linear_write_review);
        linear_write_review.setVisibility(View.GONE);
        img_close_write_review.setVisibility(View.GONE);

        /**
         * Write Review
         */
        tv_write_review= findViewById(R.id.tv_write_review);
        tv_write_review.setPaintFlags(tv_write_review.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linear_write_review.setVisibility(View.VISIBLE);
                ed_comment.setText("");
                ed_nickname.setText("");
                ed_rating.setRating(0.0f);
                img_close_write_review.setVisibility(View.VISIBLE);

            }
        });

        img_close_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_write_review.setVisibility(View.GONE);
                img_close_write_review.setVisibility(View.GONE);
            }
        });

        tv_review_alert= findViewById(R.id.tv_review_alert);
        tv_review_alert.setVisibility(View.GONE);
        //list_review.setNestedScrollingEnabled(false);

        recyclerView_Rating= findViewById(R.id.recyclerView_Rating);

        recyclerView_Rating.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_Rating.setNestedScrollingEnabled(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_Rating.setLayoutManager(linearLayoutManager);
        recyclerView_Rating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        detailsList=new ArrayList<HashMap<String,String>>();

        /**
         * share Images
         */

        img_share= findViewById(R.id.img_share);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ////Log.d("permission","check");
                //permission_status = checkRunTimePermission(MainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                    {
                        ////Log.d("permission","not");
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Request_Id_Multiple_Permissions);
                    }
                    else
                    {
                        onPermissionAccess();
                    }
                }
                else
                {
                    ////Log.d("permission","yes");
                    onPermissionAccess();
                }



            }
        });

        pager_indicator= findViewById(R.id.viewPagerCountDots);
        product_rating= findViewById(R.id.product_rating);
        product_rating.setNumStars(5);
        product_rating_new= findViewById(R.id.product_rating_new);
        product_rating_new.setNumStars(5);
        //product_rating.setRating(3.4f);

        long_d_hide=false;
        //SharedPreferences preferences;
        preferences = CommonFun.getPreferences(getApplicationContext());
        tv_Wish_list= findViewById(R.id.tv_Wish_list_new);
        //tvDiscPrice = (TextView)findViewById(R.id.tvDiscPrice);

        st_logged_gp_id = preferences.getString("login_group_id","");
        ////Log.d("st_logged_gp_id",st_logged_gp_id);

        page=0;
        pager_view_products= findViewById(R.id.pager_view_products);

        pager_view_products.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                for (int i = 0; i < arr_product_images.length; i++) {
                    if (i == position) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    } else {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                comefrm= extras.getString("comefrom");
            }
        }

        /**
         * Wish List
         */

        tv_Wish_list.setEnabled(true);
        tv_Wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_customer_id = preferences.getString("st_login_id","");


                //Log.d("wishlists",st_customer_id+"---"+st_product_id);


                st_add_wish_list_URL=Global_Settings.api_url+"rest/V1/m-carts/mine/addwishlistadd";
                if(is_product_on_wishlist==false)
                    addToWishList();
                else
                    removeItemWishList();
            }
        });

        dbh=new DatabaseHandler(MainActivity.this);

        preferences = CommonFun.getPreferences(getApplicationContext());
        tokenData=preferences.getString("tokenData","");

        tvItemName= findViewById(R.id.tvItemName);
        tvItemPrice= findViewById(R.id.tvItemPrice);
        tvItemShort= findViewById(R.id.tvItemShort);
        tvItemLong= findViewById(R.id.tvItemLong);
        tv_add_to_cart= findViewById(R.id.tv_add_to_cart);
        tvItemLong_title= findViewById(R.id.tvItemLong_title);
        tv_associate_products = findViewById(R.id.tv_associate_products);
        tv_associate_products_details = findViewById(R.id.tv_associate_products_details);



        tvItemLong_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(long_d_hide==true){
                    //tvItemLong.setVisibility(View.GONE);
                    if(or_long_desc.length()>250){
                        if (Build.VERSION.SDK_INT >= 24)
                            tvItemLong.setText(""+Html.fromHtml(or_long_desc.substring(0,240)+".....",0));
                        else
                            tvItemLong.setText(""+Html.fromHtml(or_long_desc.substring(0,240)+"....."));

                    }
                    else
                        tvItemLong.setVisibility(View.GONE);

                    tvItemLong_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downdropdown, 0);
                    long_d_hide=false;
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= 24)
                        tvItemLong.setText(""+Html.fromHtml(or_long_desc,0));
                    else
                        tvItemLong.setText(""+Html.fromHtml(or_long_desc));


                    tvItemLong.setVisibility(View.VISIBLE);
                    long_d_hide=true;


                    tvItemLong_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.updropdown, 0);
                }

            }
        });

        arrayList = new ArrayList<DataModelCart>();

        st_token_data = preferences.getString("tokenData","");
        //Log.d("wishlists",st_token_data);

        ed_qty= findViewById(R.id.ed_qty);
        ed_qty.setVisibility(View.GONE);

        img_view_product_image= findViewById(R.id.img_view_product_image);

        spinner_qty = findViewById(R.id.spinner_qty);

        String[] items3 = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9", "10", "10+"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, items3);
        spinner_qty.setAdapter(adapter3);

        short_desc="";
        long_desc="";

        /**
         * Add to cart
         */
        tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String cartqty="";
                if(showing_txt_qty_control==true)
                    cartqty=ed_qty.getText().toString();
                else {
                    cartqty = selqty;
                    cartqty=cartqty.replace("QUANTITY ","");
                }
             if(Integer.parseInt(cartqty)>0){

                    if(Integer.parseInt(cartqty)>25)
                        CommonFun.alertError(MainActivity.this,"maximum 25 quantity is allowed");
                    else {

                        getCartId_v1();

                    }

                }
            }
        });

        /**
         * showing quantity
         */


        spinner_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                selqty=spinner_qty.getSelectedItem().toString();
                // Toast.makeText(MainActivity.this,selqty,Toast.LENGTH_LONG).show();
                if(selqty.equalsIgnoreCase("10+")){
                    spinner_qty.setVisibility(View.GONE);
                    ed_qty.setText("10");
                    ed_qty.setVisibility(View.VISIBLE);
                    showing_txt_qty_control=true;
                }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    int nextID=0;
    private void insertToDbRecentItem(String product_name,String product_sku,String image_path,String product_price) {
        /**
         * Insert data into db
         * for recent view item
         */
       // opens "gkart.realm"
        try {

           // long count = realm.where(DataModelRecentItem.class).count();

            try {
                 nextID = Integer.parseInt(String.valueOf(realm.where(DataModelRecentItem.class)
                         .max("p_id")))+1;
            }
            catch (NullPointerException ex){
                nextID=1;
            }
            catch (Exception ex){
                nextID=1;
            }


            // increatement index


            if(!realm.isClosed()){
                realm.close();

            }
            //Log.d("res_res", String.valueOf(count));

            if(count>4) {

                try {
                    realm.beginTransaction();
                    int p_id = Integer.parseInt(String.valueOf(realm.where(DataModelRecentItem.class)
                            .min("p_id")));
                    //Log.d("res_res", "min pid" + p_id);
                    RealmResults<DataModelRecentItem> results = realm.where(DataModelRecentItem.class)
                            .equalTo("p_id", p_id).findAll();


                    results.deleteAllFromRealm();
                    realm.commitTransaction();
                    realm.close();
                }
                catch (Exception ex){
                    realm.close();
                }
                finally {
                    realm.close();
                }

            }


            if(realm.isClosed())
                realm=Realm.getDefaultInstance();

            realm.beginTransaction();

            // Add a product
            DataModelRecentItem r_product = realm.createObject(DataModelRecentItem.class, product_sku);

            //r_product=new DataModelRecentItem(product_name,product_sku,image_path);
            r_product.setP_id(nextID);
            r_product.setP_name(product_name);
            r_product.setP_price("₹ " +product_price);
            //r_product.setP_sku(product_sku);
            r_product.setP_img(image_path);
            realm.commitTransaction();
            realm.close();


        }
        catch (RealmPrimaryKeyConstraintException ex){
            //Log.d("res_res",ex.getMessage().toString());
            realm.close();
        }
        catch (IllegalStateException ex){
            //Log.d("res_res",ex.getMessage());
            realm.close();
        }
        catch (Exception ex){
            //Log.d("res_res",ex.getMessage());
            realm.close();
        }
        finally {
            realm.close();
        }




    }


    private void checkPinCode() {

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, check_pin_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(pDialog.isShowing())
                                pDialog.dismiss();
                            //Log.d("response", response);
                            //Log.d("response", response);
                            try {
                             JSONArray array = new JSONArray(response);

                                JSONObject json_pincode=array.getJSONObject(0);
                                String check_servicable=json_pincode.getString("status");
                                if(!check_servicable.equals(""))
                                {
                                    if(check_servicable.equals("0")) {
//                                        ed_pincode.setText("Service is available");
//                                        btn_check_availablity.setText("Change Location");
                                        check_available_response.setVisibility(View.VISIBLE);
                                        check_available.setVisibility(View.GONE);
                                        tv_availability_response.setText("we serve in this area "+ "("+st_pincode+")");
                                        tv_availability_response.setTextColor(Color.rgb(0,128,0));
                                    }
                                    else
                                    {
                                        check_available_response.setVisibility(View.VISIBLE);
                                        check_available.setVisibility(View.GONE);
                                        tv_availability_response.setText("Sorry, we don't have services in this area "+"("+st_pincode+")");
                                        tv_availability_response.setTextColor(Color.RED);
                                    }

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Something went wrong.\nPlease try again",Toast.LENGTH_LONG).show();
                                    //onBackPressed();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this,"Something went wrong.\nPlease try again",Toast.LENGTH_LONG).show();
                                //onBackPressed();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    Toast.makeText(MainActivity.this,"Something went wrong.\nPlease try again",Toast.LENGTH_LONG).show();
                    //onBackPressed();
                    //CommonFun.showVolleyException(error,DeliveryTypeActivity.this);
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



            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //////Log.d("error...","Error");
        }
    }
    /**
     * Check runtime permisison for sharing the product
     */

    private void onPermissionAccess() {
        String p_img = Global_Settings.api_url + "pub/media/catalog/product" + st_product_image;
        String p_name = tvItemName.getText().toString();
        String share_body = p_name;
        //String s_link=p_name.replaceAll(" ","-");
        //s_link=s_link+".html";
        String s_link = "";
        ////Log.d("Main","Qwerty");
        if (Build.VERSION.SDK_INT >= 24) {
            share_body = p_name + "\n" + Html.fromHtml(short_desc + "\nhttp://galwaykart.in/" + s_link, 0);
        } else {
            share_body = p_name + "\n" + Html.fromHtml(short_desc + "\nhttp://galwaykart.in/" + s_link);
//                    share_body=p_name +"<br/>"+Html.fromHtml(short_desc,0);
        }

        BitmapDrawable bitmapDrawable;
        Bitmap bitmap1;
        //write this code in your share button or function

        bitmapDrawable = (BitmapDrawable) img_view_product_image.getDrawable();// get the from imageview or use your drawable from drawable folder
        bitmap1 = bitmapDrawable.getBitmap();
        String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "title", null);
        Uri imgBitmapUri = Uri.parse(imgBitmapPath);
        //String shareText="Share image and text";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, share_body);
        startActivity(Intent.createChooser(shareIntent, "Share product"));
    }
    /**
     * Submit Customer Review
     */
    private void submitReview(String st_comment,String st_nickname){

        Float float_rate=ed_rating.getRating();

        pref= CommonFun.getPreferences(getApplicationContext());
        final String product_sku=pref.getString("showitemsku","");
        String customer_id=pref.getString("login_id","");
        String customer_name=pref.getString("login_fname","");

        customer_name=customer_name.replaceAll(" ","%20");
        st_nickname=st_nickname.replaceAll(" ","%20");
        st_comment=st_comment.replaceAll(" ","%20");

        //http://qa.galwaykart.com/glaze/reviewset.php?sku=GSG02100&cid=7405
        // &name=test&title=average&comment=v%20good%20product&star=4

        String submit_review_url=
                Global_Settings.api_url+"rest/V1/m-product/review";

        String input_string="{" +
                "\"sku\": \""+product_sku+"\"," +
                "\"cid\": "+customer_id+"," +
                "\"name\": \""+customer_name+"\"," +
                "\"title\": \""+st_nickname+"\"," +
                "\"comment\": \""+st_comment+"\"," +
                "\"star\":\"" +Math.round(float_rate)+"\""+
                "}";

        //StringEscapeUtils.unescapeJava(stringToUnEscape);
        //Log.d("input_string_ss",input_string);

//                        Global_Settings.api_custom_url+
//                             "reviewset.php?sku="+product_sku+
//                            "&cid="+customer_id+
//                            "&name="+customer_name+
//                            "&title="+st_nickname+
//                            "&comment="+st_comment+
//                            "&star="+float_rate.toString();


        progressBar_write_review.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST,
                                    submit_review_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ////Log.d("response",response.toString());
                if(response!=null) {
                    try {

                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        progressBar_write_review.setVisibility(View.GONE);

                        //JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        String getResult=jsonObject.getString("maessage");

                        final AlertDialog.Builder b;
                        CommonFun.showDialog(MainActivity.this,getResult);
                        linear_write_review.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonFun.showVolleyException(error,MainActivity.this);

            }
        }){
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
                return input_string == null ? null : input_string.getBytes(StandardCharsets.UTF_8);
            }

        };


        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);




    }
    /**
     * Add product to wishlist
     *
     */
    StringRequest stringRequest;
    JSONObject jsonObject_r;

    private void checkWishList() {
        st_customer_id = preferences.getString("st_login_id","");
    String check_wish_list_url=Global_Settings.api_url+"rest/V1/m-carts/mine/checkwishlistitem";

        jsonObject_r=new JSONObject();
        try {

            jsonObject_r.put("customerId", st_customer_id);
            jsonObject_r.put("productId",st_product_id);

        }catch (JSONException ex){

        }


        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        JsonRequest jsonRequest= new JsonRequest<JSONArray>(Request.Method.POST,
                check_wish_list_url,
                jsonObject_r.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Log.d("response_checkwishlist", String.valueOf(response));
                        try {
                            JSONObject jsonObject=response.getJSONObject(0);
                            int status=jsonObject.getInt("status");
                            if(status==1){
                                is_product_on_wishlist=true;

                                Drawable img_heart_fill=getResources().getDrawable(R.drawable.icon_heart_fill);
                                tv_Wish_list.setCompoundDrawablesRelativeWithIntrinsicBounds(img_heart_fill,null,null,null);

                            }

                            getProductRating(1, 50);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getProductRating(1, 50);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getProductRating(1, 50);
                //Log.d("response_checkwishlist", String.valueOf(error));
            }
        }) {

            @Override
            protected Response<JSONArray> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser
                                    .parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser
                                    .parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }

            }
        };


        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsonRequest.setShouldCache(false);
        requestQueue.add(jsonRequest);


    }
    private void addToWishList() {


            jsonObject_r=new JSONObject();
            try {
                jsonObject_r.put("customerId", st_customer_id);
                jsonObject_r.put("productId",st_product_id);
            }catch (JSONException ex){

            }


            RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

             JsonRequest jsonRequest= new JsonRequest<JSONArray>(Request.Method.POST, st_add_wish_list_URL,
                     jsonObject_r.toString(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //Log.d("response_wishlist", String.valueOf(response));
                                    try {
                                        JSONObject jsonObject=response.getJSONObject(0);
                                        int status=jsonObject.getInt("status");
                                        if(status==1){
                                            Drawable img_heart_fill=getResources().getDrawable(R.drawable.icon_heart_fill);
                                            tv_Wish_list.setCompoundDrawablesRelativeWithIntrinsicBounds(img_heart_fill,null,null,null);
                                            is_product_on_wishlist=true;
                                            Toast.makeText(MainActivity.this, "Item added on wishlist", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d("response", String.valueOf(error));
                        }
                    }) {

                        @Override
                        protected Response<JSONArray> parseNetworkResponse(
                                NetworkResponse response) {
                            try {
                                String jsonString = new String(response.data,
                                        HttpHeaderParser
                                                .parseCharset(response.headers));
                                return Response.success(new JSONArray(jsonString),
                                        HttpHeaderParser
                                                .parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                return Response.error(new ParseError(e));
                            } catch (JSONException je) {
                                return Response.error(new ParseError(je));
                            }

                        }
             };


        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsonRequest.setShouldCache(false);
        requestQueue.add(jsonRequest);


    }
    private void removeItemWishList() {


        String st_remove_wish_list_url=Global_Settings.api_url+"rest/V1/m-carts/mine/addwishlistdelete";
        jsonObject_r=new JSONObject();
        try {
            jsonObject_r.put("customerId", st_customer_id);
            jsonObject_r.put("productId",st_product_id);
        }catch (JSONException ex){

        }

        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

        JsonRequest jsonRequest= new JsonRequest<JSONArray>(Request.Method.POST, st_remove_wish_list_url,
                jsonObject_r.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Log.d("response_wishlist_d", String.valueOf(response));
                        try {
                            JSONObject jsonObject=response.getJSONObject(0);
                            int status=jsonObject.getInt("status");
                            if(status==1){
                                Drawable img_heart_fill=getResources().getDrawable(R.drawable.icon_heart);
                                tv_Wish_list.setCompoundDrawablesRelativeWithIntrinsicBounds(img_heart_fill,null,null,null);
                                is_product_on_wishlist=false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("response", String.valueOf(error));
            }
        }) {

            @Override
            protected Response<JSONArray> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser
                                    .parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser
                                    .parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }

            }
        };


        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsonRequest.setShouldCache(false);
        requestQueue.add(jsonRequest);


    }


    @Override
    protected void onResume() {
        super.onResume();

        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(10,5,10,0);
        layoutParams.weight = 1.8f;

        layoutParams1 = new LinearLayout.LayoutParams(30,70);
//        layoutParams1.setMargins(10,10,0,0);
        layoutParams1.gravity = Gravity.RIGHT;
        layoutParams1.weight = .2f;

        layoutParamsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsMain.weight = 2f;

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Added by Sumit

        String stConfigProductDetailsURL = Global_Settings.api_url+"rest/V1/m-products/configurable product";

        if(dataload==false) {
//            callConfigurableProductDetails(stConfigProductDetailsURL);
            callJSONAPIVolley();
        }

    }
//
//    private void callConfigurableProductDetails(String stConfigProductDetailsURL) {
//        pref= CommonFun.getPreferences(getApplicationContext());
//        product_sku=pref.getString("showitemsku","");
//
//        pDialog = new TransparentProgressDialog(MainActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, stConfigProductDetailsURL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        //Log.d("ConfigResponse",response.toString());
//
//                        //   CommonFun.alertError(MainActivity.this,response.toString());
//                        if(response!=null){
//                            try {
//
//                                JSONObject jsonObj = new JSONObject(String.valueOf(response));
//                                stTypeId = jsonObj.getString("type_id");
//                                status_product=jsonObj.getString("status");
//                                stSKU = jsonObj.getString("sku");
//
//                                    product_type_id = true;
////                                    updateUIWithApiData();
////                                    callJSONAPIVolley();
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//                CommonFun.showVolleyException(error,MainActivity.this);
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + tokenData);
//                headers.put("Content-Type","application/json");
//                return headers;
//            }
//            @Override
//            protected String getParamsEncoding() {
//                return "utf-8";
//            }
//        };
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                1000*60,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//        jsObjRequest.setShouldCache(false);
//        queue.add(jsObjRequest);
//    }

    private void setDataOnListSize(int i) {

        mAdapter_size = new CustomAttributeSizeAdapter(MainActivity.this, array_value_list_size);

        RecyclerView.LayoutManager mLayoutManager_size = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycle_view_size.setLayoutManager(mLayoutManager_size);
        recycle_view_size.setAdapter(mAdapter_size);
        rel_horizontal_view.setVisibility(View.VISIBLE);
//          String[] arrValue = arrayDataModel.getArr_valueLabel();
        //  String[] arr_click = arrayDataModel.getArrClickValue();


//        recycle_view_color.addOnItemTouchListener(
//                    new RecyclerTouchListener(getApplicationContext(),
//                            recycle_view_color, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
////                //Log.d("arrValue",arrValue[position]);
////                //Log.d("arr_click1",arr_click[position]);
////
////                arr_click[position] = "true";
////                //Log.d("arr_click1",arr_click[position]);
////
////
////                //resetAdapter();
////
////                 stSelectedValue = arrValue[position];
////                //Log.d("stSelectedValue",stSelectedValue);
////
////                for(int i=0;i<arr_value_lable_color.length;i++){
////
////                    String stSelectedColor = arr_value_lable_color[i].toString();
////                    //Log.d("stSelectedColor",stSelectedColor);
////
////                    if(arr_value_lable_color[i].toString().equalsIgnoreCase(stSelectedValue)){
////                        stSelectedSKU = stSKU+"-"+stSelectedValue;
////                        stSelectedAttIdColor = arr_attributeId_color[i];
////                        stSelectedAttIndexColor= arr_value_index_color[i];
////                        //Log.d("stSelectedSKUColor",stSelectedSKU);
////                    }
////                }
////
////                for(int j=0;j<arr_value_lable_size.length;j++){
////                    if(stSelectedValue.equalsIgnoreCase(arr_value_lable_size[j].toString())){
////                        stSelectedSKU = stSKU+"-"+stDefaultProduct+"-"+stSelectedValue;
////
////                        stSelectedAttIdSize = arr_attributeId_size[j];
////                        stSelectedAttIndexSize= arr_value_index_size[j];
////                        //Log.d("stSelectedSKUSize",stSelectedSKU);
////                    }
////                }
////                String st_config_price="";
////                for(int k = 0;k<arr_configured_sku.length;k++){
////                    try {
////
////                    if(arr_configured_sku[k].contains(stSelectedSKU)) {
////
////                        JSONObject obj_custom_attribute = jsonArray_2.getJSONObject(k);
////
////                        if (st_config_price.equalsIgnoreCase("")){
////                            st_config_price = obj_custom_attribute.getString("price");
////                            tvItemPrice.setText(st_config_price);
////                        }
////
////                            ////Log.d("custom_config_price",arr_configured_price[k]);
////
////                            JSONArray arr_custom_attribute = obj_custom_attribute.getJSONArray("custom_attributes");
//////                            if(arr_custom_attribute.length()>0) {
////                                for (int i = 0; i < arr_custom_attribute.length(); i++) {
////                                    JSONObject obj_attribute = arr_custom_attribute.getJSONObject(i);
////                                    String stAttributeCode = obj_attribute.getString("attribute_code");
////                                    if (stAttributeCode.equalsIgnoreCase("image")) {
////                                        stValueImage = obj_attribute.getString("value");
////                                    }
////                                    break;
////                                }
//////                            }
////
////                    }
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                        //Log.d("Error:::",e.toString());
////                    }
////                }
////
////                if(!stValueImage.equalsIgnoreCase("")) {
////                    //Log.d("stValueImage",stValueImage);
////                    pager_view_products.setVisibility(View.GONE);
////                    img_view_product_image.setVisibility(View.VISIBLE);
////                    image_path = image_path1 + stValueImage;
////                  //  mAdapter.notify();
////                    setImage();
////                }
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

//            LinearLayout l2 = new LinearLayout(this);
//            l2.setOrientation(LinearLayout.VERTICAL);
//            TextView text_lable = new TextView(this);
//            text_lable.setText(arr_label[i]);
//
//            LinearLayout l3 = new LinearLayout(this);
//            l3.setOrientation(LinearLayout.VERTICAL);



//            l1.addView(dynamicRecyclerView);
//            l2.addView(text_lable);
//            l3.addView(l2);
//            l3.addView(l1);
//
//            recycle_view_Container.addView(l3);


    }


    private void setDataOnListColor(int i) {

//            mAdapter_size = new CustomAttributeSizeAdapter(MainActivity.this, array_value_list_size);
            mAdapter_color= new CustomAttributeColorAdapter(MainActivity.this, array_value_list_color);
            //LinearLayout l1 = new LinearLayout(this);
            //l1.setOrientation(LinearLayout.VERTICAL);
            //dynamicRecyclerView = new RecyclerView(this);

            RecyclerView.LayoutManager mLayoutManager_color = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recycle_view_color.setLayoutManager(mLayoutManager_color);
            recycle_view_color.setAdapter(mAdapter_color);


//            RecyclerView.LayoutManager mLayoutManager_size = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//            recycle_view_size.setLayoutManager(mLayoutManager_size);
//            recycle_view_size.setAdapter(mAdapter_size);

            rel_horizontal_view.setVisibility(View.VISIBLE);
          //    String[] arrValue = arrayDataModel.getArr_valueLabel();
            //  String[] arr_click = arrayDataModel.getArrClickValue();


//        recycle_view_color.addOnItemTouchListener(
//                    new RecyclerTouchListener(getApplicationContext(),
//                            recycle_view_color, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
////                //Log.d("arrValue",arrValue[position]);
////                //Log.d("arr_click1",arr_click[position]);
////
////                arr_click[position] = "true";
////                //Log.d("arr_click1",arr_click[position]);
////
////
////                //resetAdapter();
////
////                 stSelectedValue = arrValue[position];
////                //Log.d("stSelectedValue",stSelectedValue);
////
////                for(int i=0;i<arr_value_lable_color.length;i++){
////
////                    String stSelectedColor = arr_value_lable_color[i].toString();
////                    //Log.d("stSelectedColor",stSelectedColor);
////
////                    if(arr_value_lable_color[i].toString().equalsIgnoreCase(stSelectedValue)){
////                        stSelectedSKU = stSKU+"-"+stSelectedValue;
////                        stSelectedAttIdColor = arr_attributeId_color[i];
////                        stSelectedAttIndexColor= arr_value_index_color[i];
////                        //Log.d("stSelectedSKUColor",stSelectedSKU);
////                    }
////                }
////
////                for(int j=0;j<arr_value_lable_size.length;j++){
////                    if(stSelectedValue.equalsIgnoreCase(arr_value_lable_size[j].toString())){
////                        stSelectedSKU = stSKU+"-"+stDefaultProduct+"-"+stSelectedValue;
////
////                        stSelectedAttIdSize = arr_attributeId_size[j];
////                        stSelectedAttIndexSize= arr_value_index_size[j];
////                        //Log.d("stSelectedSKUSize",stSelectedSKU);
////                    }
////                }
////                String st_config_price="";
////                for(int k = 0;k<arr_configured_sku.length;k++){
////                    try {
////
////                    if(arr_configured_sku[k].contains(stSelectedSKU)) {
////
////                        JSONObject obj_custom_attribute = jsonArray_2.getJSONObject(k);
////
////                        if (st_config_price.equalsIgnoreCase("")){
////                            st_config_price = obj_custom_attribute.getString("price");
////                            tvItemPrice.setText(st_config_price);
////                        }
////
////                            ////Log.d("custom_config_price",arr_configured_price[k]);
////
////                            JSONArray arr_custom_attribute = obj_custom_attribute.getJSONArray("custom_attributes");
//////                            if(arr_custom_attribute.length()>0) {
////                                for (int i = 0; i < arr_custom_attribute.length(); i++) {
////                                    JSONObject obj_attribute = arr_custom_attribute.getJSONObject(i);
////                                    String stAttributeCode = obj_attribute.getString("attribute_code");
////                                    if (stAttributeCode.equalsIgnoreCase("image")) {
////                                        stValueImage = obj_attribute.getString("value");
////                                    }
////                                    break;
////                                }
//////                            }
////
////                    }
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                        //Log.d("Error:::",e.toString());
////                    }
////                }
////
////                if(!stValueImage.equalsIgnoreCase("")) {
////                    //Log.d("stValueImage",stValueImage);
////                    pager_view_products.setVisibility(View.GONE);
////                    img_view_product_image.setVisibility(View.VISIBLE);
////                    image_path = image_path1 + stValueImage;
////                  //  mAdapter.notify();
////                    setImage();
////                }
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

//            LinearLayout l2 = new LinearLayout(this);
//            l2.setOrientation(LinearLayout.VERTICAL);
//            TextView text_lable = new TextView(this);
//            text_lable.setText(arr_label[i]);
//
//            LinearLayout l3 = new LinearLayout(this);
//            l3.setOrientation(LinearLayout.VERTICAL);



//            l1.addView(dynamicRecyclerView);
//            l2.addView(text_lable);
//            l3.addView(l2);
//            l3.addView(l1);
//
//            recycle_view_Container.addView(l3);


    }

//    private void resetAdapter() {
//        for(int i=0;i<arr_value_index.length;i++) {
//            productDataModel = new ConfigurableProductDataModel(
//                    arr_value_lable[i],
//                    arr_value_index[i],
//                     arr_value_att[i],
//                     arrClickValue[i]);
//
//            array_value_list.add(productDataModel);
//        }
//        mAdapter = new CustomAttributeAdapter(MainActivity.this, array_value_list);
//        LinearLayout l1 = new LinearLayout(this);
//        l1.setOrientation(LinearLayout.VERTICAL);
//        dynamicRecyclerView = new RecyclerView(this);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        dynamicRecyclerView.setLayoutManager(mLayoutManager);
//        dynamicRecyclerView.setAdapter(mAdapter);
//    }
//

    /**
     * Get Product details from API
     */
    private void callJSONAPIVolley() {
        pref= CommonFun.getPreferences(getApplicationContext());
        product_sku=pref.getString("showitemsku","");
//        product_sku = "configurable product";
        String fromurl= Global_Settings.api_url+"index.php/rest/V1/m-products/"+product_sku;
        //Log.d("fromurl",fromurl);

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();

                        sel_p_json=response;

                        Log.d("singleProDetails", response);

                        //   CommonFun.alertError(MainActivity.this,response.toString());
                        if(response!=null){
                            try {

                                jsonObj = new JSONObject(response);

                                status_product=jsonObj.getString("status");
                                product_type=jsonObj.getString("type_id");
                                stSKU = jsonObj.getString("sku");
                                if(product_type.equals("bundle"))
                                {
                                    product_type_id = true;

                                    if (product_type_id == true) {
                                        JSONObject extensionAttributesObj = jsonObj.getJSONObject("extension_attributes");
                                        JSONArray bundle_product_optionsArry = extensionAttributesObj.getJSONArray("bundle_product_options");

                                        for (int p = 0; p < bundle_product_optionsArry.length(); p++) {
                                            JSONObject bundleOptionData = bundle_product_optionsArry.getJSONObject(p);
                                            JSONArray product_linksJson = bundleOptionData.getJSONArray("product_links");
                                            json_len_bundal = product_linksJson.length();

                                            for (int a = 0; a < product_linksJson.length(); a++) {
                                                JSONObject jsonData = product_linksJson.getJSONObject(a);
                                                id_bundal.add(jsonData.getString("id"));
                                                option_id_bundal.add(jsonData.getString("option_id"));
                                                qty_bundal.add(jsonData.getString("qty"));
                                                str_sku_bundal.add(jsonData.getString("sku"));

                                                Log.e("id_arry", id_bundal + "");
                                                Log.e("option_id_arry", option_id_bundal + "");
                                                Log.e("qty_arry", qty_bundal + "");

                                            }
                                        }
                                    }

                                }
                                else if(product_type.equalsIgnoreCase("configurable")){
                                    /**
                                     * Configurable product data
                                     */
                                    ConfigurableProductAPI();

                                }


                                updateUIWithApiData();
                                //setImage();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d("Error::qwerty",e.toString());
                            }

                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                CommonFun.showVolleyException(error,MainActivity.this);


            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenData);
                headers.put("Content-Type","application/json");
                return headers;
            }

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

    private void ConfigurableProductAPI() throws JSONException {


        JSONArray jArray_1 = jsonObj.getJSONArray("custom_configurable_product_options");
       jsonArray_2 = jsonObj.getJSONArray("custom_product_links");
        arr_image_value = new String[jsonArray_2.length()];
        arr_configured_sku = new String[jsonArray_2.length()];
        arr_configured_price= new String[jsonArray_2.length()];
        arr_label = new String[jArray_1.length()];
        label_length = jArray_1.length();

        array_value_list_color=new ArrayList<ConfigurableProductDataModel>();
        array_value_list_size=new ArrayList<ConfigurableProductDataModel>();

        for(int i = 0;i<jArray_1.length();i++) {

            JSONObject jObj_1 = jArray_1.getJSONObject(i);
            stProductLabel = jObj_1.getString("label");
            stValuePosition = jObj_1.getString("position");
            arr_label[i] = stProductLabel;
            JSONArray jArray_value = jObj_1.getJSONArray("values");
            arr_value_index = new String[jArray_value.length()];
            arr_value_lable = new String[jArray_value.length()];
            arr_value_att = new String[jArray_value.length()];
            arrClickValue = new String[jArray_value.length()];
            value_length = jArray_value.length();

            if(stProductLabel.equalsIgnoreCase("Color")){

            //if(stValuePosition.equals("0")) {
                JSONArray jArray_value_color = jObj_1.getJSONArray("values");

                arr_value_lable_color = new String[jArray_value_color.length()];
                arr_attributeId_color = new String[jArray_value_color.length()];
                arr_value_index_color = new String[jArray_value_color.length()];
                arr_value_index_clicked_color = new String[jArray_value_color.length()];

                value_length_color = jArray_value_color.length();
                if(value_length_color>0)
                    is_color_availble=true;
                for (int j = 0; j < jArray_value_color.length(); j++) {
                    JSONObject jObj_values = jArray_value_color.getJSONObject(j);
                    arr_value_lable_color[j] = jObj_values.getString("value_label");
                    arr_attributeId_color[j] = jObj_values.getString("attribute_id");
                    arr_value_index_color[j] = jObj_values.getString("value_index");
                    arr_value_index_clicked_color[j]="false";
                    //Log.d("arr_value_lable", arr_value_lable_color[j]);
                    stDefaultProduct = arr_value_lable_color[0];


                    stConfigDefaultSKU  = stSKU+"-"+stDefaultProduct;
                    //Log.d("stConfigDefaultSKU:::",stConfigDefaultSKU);

                    productDataModel_color = new ConfigurableProductDataModel(
                            arr_value_lable_color[j],
                            arr_value_index_color[j] ,
                            arr_attributeId_color[j],
                            arr_value_index_clicked_color[j]);
                    array_value_list_color.add(productDataModel_color);
                }
            }


//            for(int t=0;t<array_value_list_color.size();t++){
//                //Log.d("color_list", String.valueOf(array_value_list_color.size()));
//            }
            else  if(stProductLabel.equalsIgnoreCase("size")) {

                if(value_length_size>0)
                    is_size_availble=true;

                JSONArray jArray_value_size = jObj_1.getJSONArray("values");
                arr_value_lable_size = new String[jArray_value_size.length()];
                arr_attributeId_size = new String[jArray_value_size.length()];
                arr_value_index_size = new String[jArray_value_size.length()];
                arr_value_index_clicked_size = new String[jArray_value_size.length()];
                value_length_size = jArray_value_size.length();
                for (int j = 0; j < jArray_value_size.length(); j++) {
                    JSONObject jObj_values = jArray_value_size.getJSONObject(j);
                    arr_value_lable_size[j] = jObj_values.getString("value_label");
                    arr_attributeId_size[j] = jObj_values.getString("attribute_id");
                    arr_value_index_size[j] = jObj_values.getString("value_index");
                    arr_value_index_clicked_size[j]="false";

                    //Log.d("arr_value_lable_size", arr_value_lable_size[j]);
                    stDefaultProduct = arr_value_lable_size[0];

                    stConfigDefaultSKU  = stSKU+"-"+stDefaultProduct;
                    //Log.d("stConfigDefaultSKU:::",stConfigDefaultSKU);

                    productDataModel_size = new ConfigurableProductDataModel(
                            jObj_values.getString("value_label"),
                            jObj_values.getString("value_index"),
                            jObj_values.getString("attribute_id"),
                            arr_value_index_clicked_size[j]);
                    array_value_list_size.add(productDataModel_size);

                }
            }
//            //Log.d("value_length",value_length+"");

//            array_value_list = new ArrayList<ConfigurableProductDataModel>();
//            for (int j = 0; j < jArray_value.length(); j++) {
//
//                JSONObject jObj_values = jArray_value.getJSONObject(j);
//                arr_value_index[j] = jObj_values.getString("value_index");
//                arr_value_lable[j] = jObj_values.getString("value_label");
//                arr_value_att[j] = jObj_values.getString("attribute_id");
//                arrClickValue[j] = "false";
//
//                if(stValuePosition.equals("0")) {
//                    arr_value_lable_color[j] = jObj_values.getString("value_label");
//                    arr_attributeId_color[j] = jObj_values.getString("attribute_id");
//                    arr_value_index_color[j] = jObj_values.getString("value_index");
//                    //Log.d("arr_value_lable",arr_value_lable_color[j]);
//                    stDefaultProduct = arr_value_lable_color[0];
//                }else if(stValuePosition.equals("1")){
//                    arr_value_lable_size[j] = jObj_values.getString("value_label");
//                    arr_attributeId_size[j] = jObj_values.getString("attribute_id");
//                    arr_value_index_size[j] = jObj_values.getString("value_index");
//                    //Log.d("arr_value_lable_size",arr_value_lable_size[j]);
//                }
//
//
//                stConfigDefaultSKU  = stSKU+"-"+stDefaultProduct;
//                //Log.d("stConfigDefaultSKU:::",stConfigDefaultSKU);
//
//                productDataModel = new ConfigurableProductDataModel(
//                        jObj_values.getString("value_label"),
//                        jObj_values.getString("value_index"),
//                        jObj_values.getString("attribute_id"),"false");
//                array_value_list.add(productDataModel);
//
//            }
//            arrVlaueLabel = new ArrayList<ArrayDataModel>();
//
//            arrayDataModel = new ArrayDataModel(arr_value_lable,arrClickValue);
//            arrVlaueLabel.add(arrayDataModel);

        }
        for(int k=0;k<jsonArray_2.length();k++){
            JSONObject jsonObject = jsonArray_2.getJSONObject(k);
            stConfiguredSKU = jsonObject.getString("sku");
            stConfigredPrice = jsonObject.getString("price");
            arr_configured_sku[k] = stConfiguredSKU;
            arr_configured_price[k] = stConfigredPrice;
            //Log.d("arr_configured_sku",arr_configured_sku[k]);

        }
//
//
////                                        updateUIWithApiData();
//        String[] arrValue = arrayDataModel.getArr_valueLabel();

        setDataOnListSize(0);
        setDataOnListColor(0);
    }




    private void updateUIWithApiData() {
        try {

            if (status_product.equals("1")) {

                String product_name = jsonObj.getString("name");
                st_product_id = jsonObj.getString("id");
                //////Log.d("Name",product_name);
                tvItemName.setText(product_name);
                //tvItemPrice.setText("Rs :"+jsonObj.getString("price"));
                JSONArray custom_data = jsonObj.getJSONArray("custom_attributes");
                Boolean is_salable = true;
                String is_salable_text = "";
                for (int x = 0; x < custom_data.length(); x++) {

                    JSONObject c = custom_data.getJSONObject(x);

                    if (c.getString("attribute_code").equalsIgnoreCase("description")) {
                        long_desc = c.getString("value");
                    } else if (c.getString("attribute_code").equalsIgnoreCase("short_description")) {
                        short_desc = c.getString("value");
                    } else if (c.getString("attribute_code").equalsIgnoreCase("image")) {
                        st_product_image = c.getString("value");
                    } else if (c.getString("attribute_code").equalsIgnoreCase("ip")) {
                        ip_of_product = c.getString("value");
                    } else if (c.getString("attribute_code").equalsIgnoreCase("not_salable")) {
                        String salable_value = c.getString("value");
                        if (salable_value.equals("1")) {

                            is_salable = false;

                        }
                    } else if (c.getString("attribute_code").equalsIgnoreCase("not_salable_text")) {

                        is_salable_text = c.getString("value");

                    }
                    if (c.getString("attribute_code").equalsIgnoreCase("hamper_description")) {
                        st_hamper_desc = c.getString("value");
                    }
                }

                if (is_salable == false) {
                    RelativeLayout spinnerlinear = findViewById(R.id.spinnerlinear);
                    spinnerlinear.setVisibility(View.GONE);

                    TableLayout check_available = findViewById(R.id.check_available);
                    check_available.setVisibility(View.GONE);
                    tv_add_to_cart.setEnabled(false);
                    if (is_salable_text != null && !is_salable_text.equalsIgnoreCase("")) {
                        tv_add_to_cart.setText(is_salable_text);
                    } else {
                        tv_add_to_cart.setText("Not for Sale");
                    }
                }


                SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                String login_group_id = pref.getString("login_group_id", "");
                if (login_group_id.equals("4")) {
                    short_desc = "PV / BV: " + ip_of_product + "\n\n" + short_desc + "\n\n";

                } else {
                    short_desc = "\n" + short_desc;
                }

                if (product_type.equalsIgnoreCase("bundle")) {


                    JSONArray bundle_products = jsonObj.getJSONArray("child_name");


                    tv_associate_products.setVisibility(View.VISIBLE);
                    tv_associate_products_details.setVisibility(View.VISIBLE);

                    for (int g = 0; g < bundle_products.length(); g++) {
                        //String ap = qty_bundal.get(g)+str_sku_bundal.get(g);

                        String str = bundle_products.get(g).toString();
                        tv_associate_products_details.append(str.replaceAll("\\\\n", ""));
                        tv_associate_products_details.append("\n\n");
                    }
                } else {
                    tv_associate_products.setVisibility(View.GONE);
                    tv_associate_products_details.setVisibility(View.GONE);
                }

                JSONArray custom_images = jsonObj.getJSONArray("media_gallery_entries");
                int tier_image_length = custom_images.length();

                if (tier_image_length > 0) {

                    arr_product_images = new String[tier_image_length];

                    for (int i = 0; i < tier_image_length; i++) {
                        JSONObject custom_obj = custom_images.getJSONObject(i);
                        arr_product_images[i] = custom_obj.getString("file");
                    }
                }

                /**
                 * Tier Price if available
                 */
                JSONArray custom_price = jsonObj.getJSONArray("tier_prices");
                int tier_price_length = custom_price.length();

                if (tier_price_length > 0) {

                    for (int i = 0; i < tier_price_length; i++) {

                        JSONObject custom_obj = custom_price.getJSONObject(i);
                        st_customer_gp_id = custom_obj.getString("customer_group_id");
                        st_tier_qty = custom_obj.getString("qty");
                        st_tier_price = custom_obj.getString("value");

                        if (st_customer_gp_id.equalsIgnoreCase(st_logged_gp_id)) {
                            product_price = st_tier_price;
                        }
                    }
                }

                if (product_price.equals("")) {

                    if(product_type.equalsIgnoreCase("configurable"))
                        product_price = jsonObj.getString("custom_price");
                    else
                        product_price = jsonObj.getString("price");

                }





                tvItemPrice.setText("₹ " + product_price);
                if (arr_product_images != null) {
                    if (arr_product_images.length > 0) {
                        setProductImages();
                        image_path = image_path + st_product_image;
                        setImage();
                    } else
                        image_path = image_path + st_product_image;
                }
                dbh.addCartProductImage(new CartProductImage(product_sku, image_path));

                short_desc = short_desc.replaceAll("</li>", "<br/>");

                if (Build.VERSION.SDK_INT >= 24) {

                    tvItemShort.setText("" + Html.fromHtml(short_desc, 0));
                } else {
                    tvItemShort.setText("" + Html.fromHtml(short_desc));
                }

                or_long_desc = long_desc;
                if (Build.VERSION.SDK_INT >= 24) {
                    tvItemLong.setText("" + Html.fromHtml(long_desc + "\n" + st_hamper_desc, 0));
                } else {
                    tvItemLong.setText("" + Html.fromHtml(long_desc + "\n" + st_hamper_desc));
                }

                dataload = true;
                /**
                 *Added on Ankesh Kumar
                 * Jan 15
                 */
                //

//                                    String user_detail_url = Global_Settings.user_details_url + pref.getString("login_customer_id", "");
//                                    ////Log.d("user_detail_url", user_detail_url);
//
//                                    if (user_details_already_fetch == false)
//                                        getUserDetails(user_detail_url);
//                                    else {


                insertToDbRecentItem(product_name, product_sku, image_path, product_price);

                checkWishList();



//                                        if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                                                st_magento_user_zone.equalsIgnoreCase("rkt")) {
//
//                                            checkStockAvailability();
//
//                                        } else {
//                                            getProductRating(0, 50);
//                                        }
//                                    }
            } else {

                final AlertDialog.Builder b;
                try {
                    b = new AlertDialog.Builder(MainActivity.this);
                    b.setTitle("Alert");
                    b.setCancelable(false);
                    b.setMessage("Product currently not available");
                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CommonFun.finishscreen(MainActivity.this);
                        }
                    });
                    b.create().show();
                } catch (Exception ex) {
                    //Log.d("Error::qwerty",ex.toString());
                }


            }
        }catch (Exception e){
            //Log.d("Error::qwerty",e.toString());
        }

    }

    private void setImage() {

        //Log.d("image_path:::",image_path);

        Picasso.get()
                .load(image_path)
                .placeholder(R.drawable.imageloading)
                .error(R.drawable.noimage)
                .into(img_view_product_image);

    }


    /**
     * Set array of proudct images, if available
     */
    private void setProductImages(){

        ViewPagerAdapterSingleProduct viewPagerAdapterSingleProduct=new ViewPagerAdapterSingleProduct(this,arr_product_images);
        pager_view_products.setAdapter(viewPagerAdapterSingleProduct);
        pageSwitcher(10);

        //setPagerDots();
        dots=new ImageView[arr_product_images.length];
        for (int i = 0; i < arr_product_images.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    public void pageSwitcher(int seconds) {
//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    // this is an inner class...
    public class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {
                    int j=0;
                    if (page == arr_product_images.length) { // In my case the number of pages are 5
                        page = 0;
                        j=0;
                    } else {
                        j=page++;
                        //pager_view_products.setCurrentItem(j);
                    }
                    pager_view_products.setCurrentItem(j);

                    for (int i = 0; i < arr_product_images.length; i++) {
                            if (i == j) {
                                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                            } else {
                                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                            }
                    }

                }
            });

        }
    }

//        /**
//         * Working and give cart id
//         */
//        private void getCartId_v1() {
//
//            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//
//            pDialog = new TransparentProgressDialog(MainActivity.this);
//            pDialog.setCancelable(false);
//            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            pDialog.show();
//
//
//            final StringRequest jsObjRequest = new StringRequest(Request.Method.POST, st_cart_URL,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            if (pDialog.isShowing())
//                                pDialog.dismiss();
//
//                            try {
//
//
//                                //Log.d("onCartResponse", response.toString());
//
//                                String cart_id = response.toString();
//                                cart_id = cart_id.replaceAll("\"", "");
//
//                                SharedPreferences.Editor editor=pref.edit();
//                                editor.putString("guest_cart_id",cart_id);
//                                editor.commit();
//
//
//                                addItemToCart(cart_id);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                CommonFun.alertError(MainActivity.this, e.toString());
//                            }
//
//                        }
//
//
//                    },
//
//
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // TODO Auto-generated method stub
//
//                            if (pDialog.isShowing())
//                                pDialog.dismiss();
//                            if (error instanceof ServerError) {
//                                //CommonFun.alertError(CartItemList.this, "Please try to add maximum of 25 qty");
//                                NetworkResponse response = error.networkResponse;
//                                String errorMsg = "";
//                                if(response != null && response.data != null){
//                                    String errorString = new String(response.data);
//                                    //Log.d("log_error", errorString);
//
//                                    try {
//                                        JSONObject object = new JSONObject(errorString);
//                                        String st_msg = object.getString("message");
////                                String st_code = object.getString("code");
//
//                                        //Log.d("glog","updatecartitem");
//                                        CommonFun.alertError(MainActivity.this,st_msg);
////                                //Log.d("st_code",st_code);
//                                    } catch (JSONException e) {
//                                        //e.printStackTrace();
//                                        CommonFun.showVolleyException(error, MainActivity.this);
//                                    }
//
//
//                                }
//
//                            } else
//                                CommonFun.showVolleyException(error, MainActivity.this);
//
//                            //////Log.d("ERROR","error => "+error.toString());
//                            //CommonFun.alertError(MainActivity.this,error.toString());
//                        }
//                    }
//            ) {
//
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", "Bearer " + st_token_data);
//                    //   params.put("Content-Type","application/json");
//
//                    return params;
//                }
//            };
//            jsObjRequest.setShouldCache(false);
//            queue.add(jsObjRequest);
//
//        }
//

    /**
     * Working and give cart id
     */
    private void getCartId_v1() {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        // st_token_data="w4svj2g3pw7k2vmtxo3wthipjy3ieig0";

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();


        final StringRequest jsObjRequest = new StringRequest(Request.Method.POST, st_cart_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        try {

                           if(product_type.equalsIgnoreCase("configurable")) {
                               String cart_id = response;
                               cart_id = cart_id.replaceAll("\"", "");

                               Boolean can_colorproceed=true,can_csizeproceed=true;

                               if(is_color_availble==true){
                                   if(sel_p_color.equalsIgnoreCase(""))
                                       can_colorproceed=false;
                               }
                               if(is_size_availble==true) {
                                   if (sel_p_size.equalsIgnoreCase(""))
                                       can_csizeproceed = false;
                               }


                               if (can_colorproceed==true && can_csizeproceed==true)
                               {
                                       addItemToCart(cart_id);
                               }
                               else if(can_colorproceed==false)
                               {
                                       CommonFun.alertError(MainActivity.this, "Please select any color of product first!");
                               }
                               else if(can_csizeproceed==false) {
                                   CommonFun.alertError(MainActivity.this, "Please select any size of product first!");
                               }
                           }
                           else
                           {
                               String cart_id = response;
                               cart_id = cart_id.replaceAll("\"", "");
                               addItemToCart(cart_id);
                           }

                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonFun.alertError(MainActivity.this, e.toString());
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

                            CommonFun.alertError(MainActivity.this, "Please try to add maximum of 25 qty");
                        } else
                            CommonFun.showVolleyException(error, MainActivity.this);
                           Log.d("ERROR","error => "+error.toString());
                        //CommonFun.alertError(MainActivity.this,error.toString());
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

    private void addItemToCart(String cart_id) {

            add_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/";

            //    String qty = "2";
            //  String sku = "GRP08100";


            pDialog = new TransparentProgressDialog(MainActivity.this);
            pDialog.setCancelable(false);

            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.show();

            String product_sku = preferences.getString("showitemsku", "");

            String cartqty = "";
            if (showing_txt_qty_control == true)
                cartqty = ed_qty.getText().toString();
            else {
                cartqty = selqty;
                cartqty = cartqty.replace("QUANTITY ", "");
            }


            String strProduct="";
            if(product_type.equalsIgnoreCase("configurable"))
            {
//                strProduct = "{\"cartItem\":{\"sku\": \"" + product_sku + "\"," +
//                        "\"qty\": " + cartqty + "," +
//                        "\"quote_id\": \"" + cart_id + "\"}}";

                try {
                    //JSONObject jsonObj_2 = new JSONObject(String.valueOf(sel_p_json));
                    JSONArray jArray_1 = jsonObj.getJSONArray("custom_configurable_product_options");
                    for(int jsonlable=0;jsonlable<jArray_1.length();jsonlable++){
                        JSONObject jsonObject=jArray_1.getJSONObject(jsonlable);
                        String p_label=jsonObject.getString("label");

                        if(p_label.equalsIgnoreCase("color")){

                            JSONArray jsonArray=jsonObject.getJSONArray("values");
                            for(int j_val=0;j_val<jsonArray.length();j_val++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(j_val);
                                String st_value_label=jsonObject1.getString("value_label");
                                if(st_value_label.equalsIgnoreCase(sel_p_color))
                                {
                                    stSelectedAttIdColor=jsonObject1.getString("attribute_id");
                                    stSelectedAttIndexColor=jsonObject1.getString("value_index");

                                }
                            }

                        }


                        else if(p_label.equalsIgnoreCase("size")){

                            JSONArray jsonArray=jsonObject.getJSONArray("values");
                            for(int j_val=0;j_val<jsonArray.length();j_val++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(j_val);
                                String st_value_label=jsonObject1.getString("value_label");
                                if(st_value_label.equalsIgnoreCase(sel_p_size))
                                {
                                    stSelectedAttIdSize=jsonObject1.getString("attribute_id");
                                    stSelectedAttIndexSize=jsonObject1.getString("value_index");

                                }
                            }

                        }
                    }
                }
                catch (JSONException ex){

                }

                String st_configurable_json="";
                if(!stSelectedAttIdColor.equals(""))
                {

                    st_configurable_json="{\"optionId\":"+stSelectedAttIdColor+"," +
                        "\"optionValue\":"+stSelectedAttIndexColor+"}" ;

                }
                if(!stSelectedAttIdSize.equals("")) {
                    if (st_configurable_json.equals("")){
                                st_configurable_json = "{\"optionId\":" + stSelectedAttIdSize + "," +
                                "\"optionValue\":" + stSelectedAttIndexSize + "}";
                        }
                    else
                    {
                        st_configurable_json = st_configurable_json+",{\"optionId\":" + stSelectedAttIdSize + "," +
                                "\"optionValue\":" + stSelectedAttIndexSize + "}";
                    }
                }

                strProduct = "{\"cartItem\": {\"sku\": \""+product_sku+"\"," +
                        "\"qty\": \""+cartqty+"\"," +
                        "\"quote_id\": \""+cart_id+"\"," +
                        "\"productOption\":{\"extensionAttributes\":" +
                        "{\"configurable_item_options\":" +
                            "[" + st_configurable_json+ "]}}}}";
                Log.d("strProduct",strProduct);


            }
            else if(product_type.equalsIgnoreCase("bundle"))
            {
                for(int i=0;i<json_len_bundal;i++) {
                    if (stBundle.equalsIgnoreCase("")){
                        stBundle = "{\"option_id\":"+option_id_bundal.get(i)+"," +
                                "\"option_qty\":"+qty_bundal.get(i)+"," +
                                "\"option_selections\":["+id_bundal.get(i)+"]}";

                    }
                    else{
                        stBundle = stBundle+","+"{\"option_id\":"+option_id_bundal.get(i)+"," +
                                "\"option_qty\":"+qty_bundal.get(i)+"," +
                                "\"option_selections\":["+id_bundal.get(i)+"]}";
                    }
                }

                strProduct = "{\"cartItem\":{\"sku\":\""+product_sku+"\"," +
                        "\"qty\":\""+cartqty+"\"," +
                        "\"quote_id\":\""+cart_id+"\"," +
                        "\"product_option\":{\"extension_attributes\":{\"bundle_options\":["+stBundle+"]}}}}";

                Log.e("strProduct", strProduct);

            }
            else {
                   strProduct = "{\"cartItem\":{\"sku\": \"" + product_sku + "\"," +
                        "\"qty\": " + cartqty + "," +
                        "\"quote_id\": \"" + cart_id + "\"}}";

            }


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = null;
            try {
                jsObjRequest = new JsonObjectRequest(Request.Method.POST, add_to_cart_URL, new JSONObject(strProduct),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                if (response != null) {
                                    try {
                                        JSONObject jsonObj = new JSONObject(String.valueOf(response));
                                        if(jsonObj.has("qty")) {

                                            String st_qty = jsonObj.getString("qty");
                                            ////Log.d("st_qty", st_qty);

                                            /**
                                             * Update cart quantity
                                             */
                                            refreshItemCount(MainActivity.this);
                                            //refreshItemCount();
                                            Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                                            vibrator.vibrate(100);

                                            dialog = new Dialog(MainActivity.this);
                                            dialog.setContentView(R.layout.custom_alert_dialog_design);
                                            TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
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

                                                    String value = preferences.getString("currentitemprice", "");
                                                    if (value != null && !value.equals("")) {
                                                    } else {
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("currentitemprice", product_price);
                                                        editor.commit();
                                                    }
                                                    if(dialog.isShowing())
                                                    dialog.dismiss();
                                                }
                                            }.start();
                                        }
                                        else if(jsonObj.has("message"))
                                        {
                                            String st_msg = jsonObj.getString("message");
                                            CommonFun.alertError(MainActivity.this,st_msg);

                                        }

                                    } catch (JSONException e) {
                                        //e.printStackTrace();




                                    }

                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        if (error instanceof ServerError) {
                           // CommonFun.alertError(MainActivity.this, "Please try to add maximum of 25 qty");
                            NetworkResponse response = error.networkResponse;
                            String errorMsg = "";
                            if(response != null && response.data != null){
                                String errorString = new String(response.data);
                                //Log.d("log_error", errorString);

                                try {
                                    JSONObject object = new JSONObject(errorString);
                                    String st_msg = object.getString("message");
//                                String st_code = object.getString("code");
                                    CommonFun.alertError(MainActivity.this,st_msg);
//                                //Log.d("st_code",st_code);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    CommonFun.showVolleyException(error, MainActivity.this);
                                }


                            }
                        } else
                            CommonFun.showVolleyException(error, MainActivity.this);
                    }


                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();

                        headers.put("Authorization", "Bearer " + st_token_data);
                        //headers.put("Content-Type","application/json");
                        return headers;
                    }

                };
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.d("Error::qwerty",e.toString());
            }


            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            jsObjRequest.setShouldCache(false);
            queue.add(jsObjRequest);

        }

    /**
     * added on                                                                                                                                                              Dec 30, 2017
     * Ankesh Kumar
     *
     * Get Overall product rating and reviews
     */
    private void getProductRating(int startrange,int limitrange){

        ////Log.d("Rating Call","start");

        final String product_sku=pref.getString("showitemsku","");
        String rating_url="";
        //Global_Settings.api_custom_url+"reviewget.php?start="+startrange+"&limit="+limitrange+"&sku="+product_sku;

        rating_url=Global_Settings.api_url+"rest/V1/m-product/review/"+product_sku+"/"+startrange+"/"+limitrange;

        //Log.d("rating_url",rating_url);

        progressBar_read_review.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

        StringRequest jsonRequest=new StringRequest(Request.Method.GET, rating_url,
                 new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //Log.d("rating_response",response.toString());
                if(response!=null) {
                    try {

                        JSONArray json_getResult=new JSONArray(response);

                        JSONObject jsonObject = json_getResult.getJSONObject(0);


                        JSONArray getResult=jsonObject.getJSONArray("result");

//                        if(getResult.length()>0){
//                            //String getRate=jsonObject.getString("rate");
//
//                            ////Log.d("Rating",getRate);
//
//                            if(!getRate.equals("") && getRate!=null && !getRate.equals("0")){
//                                product_rating.setRating(Float.parseFloat(getRate)/20.0f);
//                                product_rating_new.setRating(Float.parseFloat(getRate)/20.0f);
//                            }
//                            else {
//                                product_rating.setVisibility(View.GONE);
//                                product_rating_new.setVisibility(View.GONE);
//                            }
//
//                        }
//                        else {
//                            product_rating.setVisibility(View.GONE);
//                            product_rating_new.setVisibility(View.GONE);
//                        }

                        review_title=new String[getResult.length()];
                        review_detail=new String[getResult.length()];
                        review_nickname=new String[getResult.length()];
                        review_rating=new String[getResult.length()];

                        for(int i=0;i<getResult.length();i++){

                            JSONObject jsonObject1=getResult.getJSONObject(i);


                            review_detail[i]=jsonObject1.getString(TAG_detail);
                            review_title[i]=jsonObject1.getString(TAG_title);
                            review_nickname[i]=jsonObject1.getString(TAG_nickname);
                            review_rating[i]= String.valueOf(i);


//                            if(!jsonObject1.getString("review_value").equals("")&&jsonObject1.getString("review_value")!=null)
//                                review_rating[i]=String.valueOf(Integer.parseInt(jsonObject1.getString("review_value"))/20);

                            itemRatingList.add(new DataModelProductReview(review_title[i],review_detail[i],review_nickname[i],review_rating[i]));

                            setReviewAdapter();


                        }

                       // ListAdapter lstadapter;
                       // lstadapter=null;

//                        lstadapter = new CustomerReviewAdapter(MainActivity.this,
//                                     detailsList,R.layout.product_customer_review,
//                                     new String[]{TAG_detail,TAG_nickname,TAG_rating,TAG_title},
//                                     new int[]{R.id.text_desc,R.id.text_nickname,R.id.text_nickname,R.id.text_title});
//


                     //   if(lstadapter.getCount()>0){

//                            list_review.invalidate();
//                            list_review.setVisibility(View.VISIBLE);
//                            list_review.setAdapter(lstadapter);




//                        }
//                        else
//                        {
                            list_review.setEnabled(false);
                         //}

                    } catch (JSONException e) {
                       // e.printStackTrace();
                    }
                }
                progressBar_read_review.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_read_review.setVisibility(View.GONE);


            }
        });

        jsonRequest.setShouldCache(false);
        requestQueue.add(jsonRequest);




        }

    private void setReviewAdapter() {

        CustomerReviewAdapter adapter;
        adapter = new CustomerReviewAdapter(MainActivity.this, itemRatingList);
        progressBar_read_review.setVisibility(View.GONE);
        if (adapter.getItemCount() > 0) {

            int spanCount = 1; //  columns
            int spacing = 2; // 50px
            boolean includeEdge = true;



            recyclerView_Rating.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
            recyclerView_Rating.setLayoutManager(mLayoutManager);
            //recyclerView_Rating.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true))
            //  recyclerView_Rating.setItemAnimator(new DefaultItemAnimator());
            recyclerView_Rating.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            recyclerView_Rating.setVisibility(View.VISIBLE);
            tv_review_alert.setVisibility(View.GONE);
            show_recent_item=true;
        } else
        {
            recyclerView_Rating.setVisibility(View.GONE);
            tv_review_alert.setVisibility(View.VISIBLE);
            show_recent_item=false;
        }


    }


    /**
     *  Get User Details
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==Request_Id_Multiple_Permissions) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                onPermissionAccess();
            } else {

                CommonFun.alertError(MainActivity.this, "Must allow Storage permission to Share Product");
            }
        }
    }



    private String inputBundleProductJson(){

        /*id_bundal;
        option_id_bundal;
        qty_bundal;*/

        for(int i=0;i<json_len_bundal;i++) {
           if (stBundle.equalsIgnoreCase("")){
               stBundle = "{\"option_id\":"+option_id_bundal.get(i)+"," +
                       "\"option_qty\":"+qty_bundal.get(i)+"," +
                       "\"option_selections\":["+id_bundal.get(i)+"]}";

           }
           else{
               stBundle = stBundle+","+"{\"option_id\":"+option_id_bundal.get(i)+"," +
                       "\"option_qty\":"+qty_bundal.get(i)+"," +
                       "\"option_selections\":["+id_bundal.get(i)+"]}";
           }
        }

        stInputDataBundle = "{\"cartItem\":{\"sku\":\"24-WG080\"," +
                "\"qty\":\"1\"," +
                "\"quote_id\":\"4\"," +
                "\"product_option\":{\"extension_attributes\":{\"bundle_options\":["+stBundle+"]}}}}";


        //Log.d("stInputData",stInputDataBundle);

        return stInputDataBundle;

    }


    public class CustomAttributeSizeAdapter extends RecyclerView.Adapter<CustomAttributeSizeAdapter.MyViewHolder>{

        public ArrayList<ConfigurableProductDataModel> array_value_list_size;

        Context mContext;
        View view;

        public CustomAttributeSizeAdapter(Context context,ArrayList<ConfigurableProductDataModel> array_value_list) {

            this.array_value_list_size = array_value_list;
            this.mContext = context;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.arr_list_item, parent, false);

            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            //Log.d("len_value",i+"");

            String stLabelValue = array_value_list_size.get(i).st_clicked;
            //Log.d("stLabelValuest_clicked",stLabelValue);


            if(arr_value_index_clicked_size[i].equalsIgnoreCase("true"))
                myViewHolder.tv_product_value.setBackgroundResource(R.color.colorYellow);
            else
                myViewHolder.tv_product_value.setBackgroundResource(R.drawable.shape_line);


            myViewHolder.tv_product_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.d("label_value",array_value_list_size.get(i).stValueLabel);

                    for(int j=0;j<arr_value_index_clicked_size.length;j++) {
                        if(i==j)
                            arr_value_index_clicked_size[j] = "true";
                        else
                            arr_value_index_clicked_size[j] = "false";
                    }

                    mAdapter_size.notifyDataSetChanged();
                    sel_p_size=arr_value_lable_size[i];
                    getSelProductData(false);


                    /**
                     * set Image on Size Change
                     */

               //stSelectedValue = arr_value_index_color[i];
                ////Log.d("stSelectedValue",stSelectedValue);

//                for(int i=0;i<arr_value_lable_size.length;i++){
//
//                    String stSelectedColor = arr_value_lable_size[i].toString();
//                    //Log.d("stSelectedColor",stSelectedColor);
//
//                    if(arr_value_lable_size[i].toString().equalsIgnoreCase(stSelectedValue)){
//                        stSelectedSKU = stSKU+"-"+stSelectedValue;
//                        stSelectedAttIdSize = arr_attributeId_size[i];
//                        stSelectedAttIndexSize= arr_value_index_size[i];
//                        //Log.d("stSelectedSKUColor",stSelectedSKU);
//                    }
//                }

//                for(int j=0;j<arr_value_lable_size.length;j++){
//                    if(stSelectedValue.equalsIgnoreCase(arr_value_lable_size[j].toString())){
//                        stSelectedSKU = stSKU+"-"+stDefaultProduct+"-"+stSelectedValue;
//
//                        stSelectedAttIdSize = arr_attributeId_size[j];
//                        stSelectedAttIndexSize= arr_value_index_size[j];
//                        //Log.d("stSelectedSKUSize",stSelectedSKU);
//                    }
//                }
//                String st_config_price="";
//                for(int k = 0;k<arr_configured_sku.length;k++){
//                    try {
//
//                    if(arr_configured_sku[k].contains(stSelectedSKU)) {
//
//                        JSONObject obj_custom_attribute = jsonArray_2.getJSONObject(k);
//
//                        if (st_config_price.equalsIgnoreCase("")){
//                            st_config_price = obj_custom_attribute.getString("price");
//                            tvItemPrice.setText(st_config_price);
//                        }
//
//                            ////Log.d("custom_config_price",arr_configured_price[k]);
//
//                            JSONArray arr_custom_attribute = obj_custom_attribute.getJSONArray("custom_attributes");
////                            if(arr_custom_attribute.length()>0) {
//                                for (int i = 0; i < arr_custom_attribute.length(); i++) {
//                                    JSONObject obj_attribute = arr_custom_attribute.getJSONObject(i);
//                                    String stAttributeCode = obj_attribute.getString("attribute_code");
//                                    if (stAttributeCode.equalsIgnoreCase("image")) {
//                                        stValueImage = obj_attribute.getString("value");
//                                    }
//                                    break;
//                                }
////                            }
//
//                    }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        //Log.d("Error:::",e.toString());
//                    }
//                }
//
//                if(!stValueImage.equalsIgnoreCase("")) {
//                    //Log.d("stValueImage",stValueImage);
//                    pager_view_products.setVisibility(View.GONE);
//                    img_view_product_image.setVisibility(View.VISIBLE);
//                    image_path = image_path1 + stValueImage;
//                  //  mAdapter.notify();
//                    setImage();
//                }


                }
            });

            myViewHolder.tv_product_value.setText(array_value_list_size.get(i).stValueLabel);

        }

        @Override
        public int getItemCount() {
            return array_value_list_size.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView tv_product_value;

            public MyViewHolder(View view) {
                super(view);

                tv_product_value = view.findViewById(R.id.tv_product_value);

            }
        }



    }



    public class CustomAttributeColorAdapter extends
                 RecyclerView.Adapter<CustomAttributeColorAdapter.MyViewHolder>{

        //public ArrayList<ConfigurableProductDataModel> array_value_list_color;
        Context mContext;
        View view;

        public CustomAttributeColorAdapter(Context context,ArrayList<ConfigurableProductDataModel> array_value_list) {

            //this.array_value_list_color = array_value_list;
            this.mContext = context;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.arr_list_item, parent, false);

            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            //Log.d("len_value",i+"");

            String stLabelValue = array_value_list_color.get(i).st_clicked;
            //Log.d("stLabelValuest_clicked",stLabelValue);


            if(arr_value_index_clicked_color[i].equalsIgnoreCase("true"))
                        myViewHolder.tv_product_value.setBackgroundResource(R.color.colorYellow);
            else
                myViewHolder.tv_product_value.setBackgroundResource(R.drawable.shape_line);


            myViewHolder.tv_product_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.d("label_value",array_value_list_color.get(i).stValueLabel);

                    for(int j=0;j<arr_value_index_clicked_color.length;j++) {
                     if(i==j)
                        arr_value_index_clicked_color[j] = "true";
                     else
                         arr_value_index_clicked_color[j] = "false";
                    }

                    mAdapter_color.notifyDataSetChanged();
                    sel_p_color=arr_value_lable_color[i];
                    getSelProductData(true);


//                    mAdapter_color.notifyAll();
                    //if(stLabelValue.equalsIgnoreCase("93"))
//                    if(myViewHolder.tv_product_value.getText().toString().equalsIgnoreCase(array_value_list_color.get(i).stValueLabel))
//                        myViewHolder.tv_product_value.setBackgroundResource(R.color.colorYellow);
//                    else
//                        myViewHolder.tv_product_value.setBackgroundResource(R.color.colorWhite);
//            else if(stLabelValue.equalsIgnoreCase("141"))
//                    myViewHolder.tv_product_value.setBackgroundResource(R.color.colorblue);

                }
            });

            myViewHolder.tv_product_value.setText(array_value_list_color.get(i).stValueLabel);

        }

        @Override
        public int getItemCount() {
            return array_value_list_color.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView tv_product_value;

            public MyViewHolder(View view) {
                super(view);

                tv_product_value = view.findViewById(R.id.tv_product_value);

            }
        }



    }


    private void getSelProductData(Boolean is_from_color) {



        if(is_from_color==true)
        {
            String sel_p_color_sku=product_sku+(!sel_p_color.equals("")?"-"+sel_p_color:"");
            try {

                for(int i=0;i<jsonArray_2.length();i++) {
                    JSONObject json_data = jsonArray_2.getJSONObject(i);
                    String sku=json_data.getString("sku");
                    if(sku.contains(sel_p_color_sku))
                    {

                        JSONArray custom_data = json_data.getJSONArray("custom_attributes");
                        Boolean is_salable = true;
                        String is_salable_text = "";
                        for (int x = 0; x < custom_data.length(); x++) {

                            JSONObject c = custom_data.getJSONObject(x);

                            if (c.getString("attribute_code").equalsIgnoreCase("image")) {
                                st_product_image = c.getString("value");
                                //Log.d("sel_p_sku",st_product_image);
                                image_path = or_image_path + st_product_image;
                                setImage();
                                pager_view_products.setVisibility(View.GONE);
                                img_view_product_image.setVisibility(View.VISIBLE);
                            }
                        }



                    }


                }

            }
            catch (Exception ex){

            }


        }

        if (!sel_p_json.equalsIgnoreCase("")) {

            sel_p_sku=product_sku+(!sel_p_color.equals("")?"-"+sel_p_color:"")+
                    (!sel_p_size.equals("")?"-"+sel_p_size:"");
            //Log.d("sel_p_sku",sel_p_sku);

            //CommonFun.alertError(MainActivity.this,response.toString());

                try {

                   for(int i=0;i<jsonArray_2.length();i++) {
                       JSONObject json_data = jsonArray_2.getJSONObject(i);
                       String sku=json_data.getString("sku");
                       Boolean is_salable=true;


                       if(sku.equalsIgnoreCase(sel_p_sku))
                       {
                           //sel_p_price=json_data.getString("price");
                           ////Log.d("sel_p_sku",sel_p_price);


                           String can_sel=json_data.getString("is_salable");
                           if(can_sel.equals("1"))
                           {
                               // tv_add_to_cart.setBackgroundColor(Color.RED);
                               tv_add_to_cart.setText("ADD TO CART");
                               tv_add_to_cart.setEnabled(true);
                           }
                           else
                           {
                               //tv_add_to_cart.setBackgroundColor(Color.RED);
                               tv_add_to_cart.setText("Out of Stock");
                               tv_add_to_cart.setEnabled(false);
                           }

                           product_price=json_data.getString("price");
                           JSONArray custom_price = json_data.getJSONArray("tier_prices");
                           int tier_price_length = custom_price.length();

                           if (tier_price_length > 0) {

                               for (int tier_p = 0; tier_p < tier_price_length; tier_p++) {

                                   JSONObject custom_obj = custom_price.getJSONObject(tier_p);
                                   st_customer_gp_id = custom_obj.getString("customer_group_id");
                                   st_tier_qty = custom_obj.getString("qty");
                                   st_tier_price = custom_obj.getString("value");

                                   if (st_customer_gp_id.equalsIgnoreCase(st_logged_gp_id)) {
                                       product_price = st_tier_price;
                                   }
                               }
                           }

//                           if (product_price.equals(""))
//                               product_price = jsonObj.getString("price");

                           tvItemPrice.setText("₹ " + product_price);



                           JSONArray custom_data = json_data.getJSONArray("custom_attributes");
                         //  Boolean is_salable = true;
                          // String is_salable_text = "";
                           for (int x = 0; x < custom_data.length(); x++) {

                               JSONObject c = custom_data.getJSONObject(x);
//
//                               if (c.getString("attribute_code").equalsIgnoreCase("description")) {
//                                   long_desc = c.getString("value");
//                               } else if (c.getString("attribute_code").equalsIgnoreCase("short_description")) {
//                                   short_desc = c.getString("value");
//                               } else
                               if (c.getString("attribute_code").equalsIgnoreCase("image")) {
                                   st_product_image = c.getString("value");
                                   //Log.d("sel_p_sku",st_product_image);
                                   image_path = or_image_path + st_product_image;
                                   setImage();
                                   pager_view_products.setVisibility(View.GONE);
                                   img_view_product_image.setVisibility(View.VISIBLE);
                               } else if (c.getString("attribute_code").equalsIgnoreCase("ip")) {
                                   ip_of_product = c.getString("value");
                               }
//                               else if (c.getString("attribute_code").equalsIgnoreCase("not_salable")) {
//                                   String salable_value = c.getString("value");
//                                   if (salable_value.equals("1")) {
//
//                                       is_salable = false;
//
//                                   }
//                               } else if (c.getString("attribute_code").equalsIgnoreCase("not_salable_text")) {
//
//                                   is_salable_text = c.getString("value");
//
//                               }
//                               if (c.getString("attribute_code").equalsIgnoreCase("hamper_description")) {
//                                   st_hamper_desc = c.getString("value");
//                               }
                           }



                       }

                   }

                }
                catch (Exception ex){

                }

        }
    }
}
