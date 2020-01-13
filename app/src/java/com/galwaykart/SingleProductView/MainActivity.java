package com.galwaykart.SingleProductView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.address_book.DeliveryTypeActivity;
import com.galwaykart.BaseActivity;
import com.galwaykart.CAdapter.GridSpacingItemDecoration;
import com.galwaykart.Cart.DataModelCart;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.ViewPagerAdapterSingleProduct;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.productList.ProductListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Showing single product details
 * user can share, add to whitelist
 * See review and give rating
 */
public class MainActivity extends BaseActivity {
   Spinner spinner1, spinner2, spinner_qty;

    TextView tvItemName,tv_add_to_cart;
    TextView tvItemPrice,tvItemShort,tvItemLong;
    String short_desc,long_desc,st_product_image;
    String image_path = Global_Settings.api_url+"pub/media/catalog/product";
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
    String st_add_wish_list_URL = Global_Settings.api_url+"glaze/wishlist_add.php?customer_id=";
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

    /**
     * Check if user zone details already fetch
     */
     // Boolean user_details_already_fetch=false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            if(!comefrm.equals("")) {
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(this);
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CommonFun.finishscreen(this);
            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initNavigationDrawer();

        pref = CommonFun.getPreferences(getApplicationContext());

//        String st_data=pref.getString("st_dist_id","");
//        if(!st_data.equals("")  && (st_data!=null))
//            user_details_already_fetch=true;
//        else
//            user_details_already_fetch=false;
//
//        String st_data_magento=pref.getString("log_user_zone","");
//        if(!st_data_magento.equals("")  && (st_data_magento!=null))
//            user_details_already_fetch=true;
//        else
//            user_details_already_fetch=false;
//
//
//        if(user_details_already_fetch==true){
//            st_sales_user_zone=st_data;
//            st_magento_user_zone=st_data_magento;
//
//        }

        show_recent_item=false;
        img_close_write_review=(ImageView)findViewById(R.id.img_close_write_review);

        llayout_product=(LinearLayout)findViewById(R.id.llayout_product);
        rel_layout_review=(RelativeLayout)findViewById(R.id.rel_layout_review);
        product_rating_new=(RatingBar)findViewById(R.id.product_rating_new);
        live=(RadioButton)findViewById(R.id.live);

        local=(RadioButton)findViewById(R.id.local);
        toggle=(RadioGroup)findViewById(R.id.toggle);
        live.setChecked(true);
        live.setTextColor(getResources().getColor(R.color.colorwhite));
        local.setTextColor(getResources().getColor(R.color.black));
        llayout_product.setVisibility(View.VISIBLE);
        rel_layout_review.setVisibility(View.GONE);
        product_rating_new.setVisibility(View.VISIBLE);

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

        list_review=(ListView)findViewById(R.id.list_review);
        list_review.setVisibility(View.GONE);

        itemRatingList=new ArrayList<>();

        ed_comment=(EditText)findViewById(R.id.ed_nickname);
        ed_nickname=(EditText)findViewById(R.id.ed_comment);
        ed_rating=(RatingBar)findViewById(R.id.ed_rating);

        progressBar_write_review=(ProgressBar)findViewById(R.id.progressBar_write_review);
        progressBar_write_review.setVisibility(View.GONE);
        progressBar_read_review=(ProgressBar)findViewById(R.id.progressBar_read_review);
        progressBar_read_review.setVisibility(View.GONE);

        check_available_response = (TableLayout)findViewById(R.id.check_available_response);
        check_available = (TableLayout)findViewById(R.id.check_available);

        btn_check_availablity = (TextView)findViewById(R.id.btn_check_availablity);

        btn_change_pin= (TextView)findViewById(R.id.btn_change_pin);
        tv_availability_response= (TextView)findViewById(R.id.tv_availability_response);

        ed_pincode = (EditText)findViewById(R.id.ed_pincode);

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
                    check_pin_url=Global_Settings.api_custom_url+"pincode_check.php?postcode="+st_pincode;

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


        btn_submit_review=(Button)findViewById(R.id.btn_submit_review);
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


        linear_write_review=(LinearLayout)findViewById(R.id.linear_write_review);
        linear_write_review.setVisibility(View.GONE);
        img_close_write_review.setVisibility(View.GONE);

        /**
         * Write Review
         */
        tv_write_review=(TextView)findViewById(R.id.tv_write_review);
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

        tv_review_alert=(TextView)findViewById(R.id.tv_review_alert);
        tv_review_alert.setVisibility(View.GONE);
        //list_review.setNestedScrollingEnabled(false);



        recyclerView_Rating=(RecyclerView)findViewById(R.id.recyclerView_Rating);

        recyclerView_Rating.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_Rating.setNestedScrollingEnabled(true);
        //LinearLayoutManager linearLayoutManager=(LinearLayoutManager)recyclerView_Rating.getLayoutManager();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_Rating.setLayoutManager(linearLayoutManager);

//
//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//
//                Toast.makeText(MainActivity.this,"called23",Toast.LENGTH_LONG).show();
//                getProductRating(3, 3);
////                                if(show_recent_item==true) {
////
////                   // last_visible_item = linearLayoutManager.findLastVisibleItemPosition();
////                    //Toast.makeText(MainActivity.this,String.valueOf(last_visible_item),Toast.LENGTH_LONG).show();
////                    //if (last_visible_item > 3) {
////
////                    //}
////                }
//
//            }
//        };

      //  recyclerView_Rating.addOnScrollListener(scrollListener);


        recyclerView_Rating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


//                if(show_recent_item==true) {
//
//                    last_visible_item = linearLayoutManager.findLastVisibleItemPosition();
             //       Toast.makeText(MainActivity.this,"called",Toast.LENGTH_LONG).show();
//                    if (last_visible_item > 3) {
              //          getProductRating(3, 3);
//                    }
//                }


            }
        });

        detailsList=new ArrayList<HashMap<String,String>>();

        /**
         * share Images
         */

        img_share=(TextView)findViewById(R.id.img_share);
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

        pager_indicator=(LinearLayout)findViewById(R.id.viewPagerCountDots);
        product_rating=(RatingBar)findViewById(R.id.product_rating);
        product_rating.setNumStars(5);
        product_rating_new=(RatingBar)findViewById(R.id.product_rating_new);
        product_rating_new.setNumStars(5);
        //product_rating.setRating(3.4f);

        long_d_hide=false;
        //SharedPreferences preferences;
        preferences = CommonFun.getPreferences(getApplicationContext());
        tv_Wish_list=(TextView)findViewById(R.id.tv_Wish_list_new);
        //tvDiscPrice = (TextView)findViewById(R.id.tvDiscPrice);

        st_logged_gp_id = preferences.getString("login_group_id","");
        ////Log.d("st_logged_gp_id",st_logged_gp_id);

        page=0;
        pager_view_products=(ViewPager)findViewById(R.id.pager_view_products);

        pager_view_products.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


//                int j=0;
//                if (page == arr_product_images.length) { // In my case the number of pages are 5
//                    page = 0;
//                    j=page;
//                } else {
//                    j=page++;
//                    //pager_view_products.setCurrentItem(j);
//                }


                for (int i = 0; i < arr_product_images.length; i++) {
                    if (i == position) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    } else {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }
                }

                //pager_view_products.setCurrentItem(j);

            }

            @Override
            public void onPageSelected(int position) {

//                SharedPreferences pref;
//                pref= CommonFun.getPreferences(getApplicationContext());
//
//                SharedPreferences.Editor editor=pref.edit();
//                editor.putString("zoomimage",arr_product_images[position]);
//                editor.commit();
//
//                Intent intent=new Intent(MainActivity.this,ZoomProductImages.class);
//                //intent.putExtra("zoomimage",arr_product_images[position]);
//                startActivity(intent);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        pager_view_products.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                int j=0;
//                if (page == arr_product_images.length) { // In my case the number of pages are 5
//                    page = 0;
//                    j=page;
//                } else {
//                    j=page++;
//                    //pager_view_products.setCurrentItem(j);
//                }
//
//
//                for (int i = 0; i < arr_product_images.length; i++) {
//                    if (i == j) {
//                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
//                    } else {
//                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
//                    }
//                }
//
//                pager_view_products.setCurrentItem(j);
//
//                return false;
//            }
//        });


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

                st_add_wish_list_URL = st_add_wish_list_URL+st_customer_id+"&product_id="+st_product_id;
                ////Log.d("st_add_wish_list_URL",st_add_wish_list_URL);

                addToWishList();
            }
        });

        //refreshItemCount(com.galwaykart.SingleProductView.MainActivity.this);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //initNavigationDrawerWithoutToolbar(toolbar);

        dbh=new DatabaseHandler(MainActivity.this);

        preferences = CommonFun.getPreferences(getApplicationContext());
        tokenData=preferences.getString("tokenData","");

        tvItemName=(TextView)findViewById(R.id.tvItemName);
        tvItemPrice=(TextView)findViewById(R.id.tvItemPrice);
        tvItemShort=(TextView)findViewById(R.id.tvItemShort);
        tvItemLong=(TextView)findViewById(R.id.tvItemLong);
        tv_add_to_cart=(TextView)findViewById(R.id.tv_add_to_cart);
        tvItemLong_title=(TextView)findViewById(R.id.tvItemLong_title);

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
        ////Log.d("MainActivity",st_token_data);

        ed_qty=(EditText)findViewById(R.id.ed_qty);
        ed_qty.setVisibility(View.GONE);

        img_view_product_image=(ImageView)findViewById(R.id.img_view_product_image);

//        String[] items1 = new String[]{"BLACK - GOLD", "RED - GOLD", "PURPLE - GOLD"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, items1);
//        dropdown1.setAdapter(adapter1);
//
//        String[] items2 = new String[]{"SIZE", "8", "9", "10", "11"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, items2);
//        dropdown2.setAdapter(adapter2);

        spinner_qty = (Spinner)findViewById(R.id.spinner_qty);

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

                //getCartId(); //depr.
                String cartqty="";
                if(showing_txt_qty_control==true)
                    cartqty=ed_qty.getText().toString();
                else {
                    cartqty = selqty;
                    cartqty=cartqty.replace("QUANTITY ","");
                }
                //if(Integer.parseInt(cartqty)>0 && (Integer.parseInt(cartqty)<=available_stock)) {

                if(Integer.parseInt(cartqty)>0){

                    if(Integer.parseInt(cartqty)>25)
                        CommonFun.alertError(MainActivity.this,"maximum 25 quantity is allowed");
                    else {

                        getCartId_v1();
//
//                        st_sales_user_zone=pref.getString("st_dist_id","").toLowerCase();
//                        st_magento_user_zone=pref.getString("log_user_zone","").toLowerCase();
//
//                        if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                                st_magento_user_zone.equalsIgnoreCase("rkt")) {
//                            if (Integer.parseInt(cartqty) <= available_stock)
//                                getCartId_v1();
//                            else
//                                CommonFun.alertError(MainActivity.this, "Cart Limitation upto " + available_stock);
//                        }
//                        else
//                        {
//                            getCartId_v1();
//                        }
//
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

    private void checkPinCode() {

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    1000 * 60,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            //));
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
            share_body = p_name + "\n" + Html.fromHtml(short_desc + "\nhttps://galwaykart.com/" + s_link, 0);
        } else {
            share_body = p_name + "\n" + Html.fromHtml(short_desc + "\nhttps://galwaykart.com/" + s_link);
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
        String customer_id=pref.getString("login_customer_id","");
        String customer_name=pref.getString("login_fname","");

        customer_name=customer_name.replaceAll(" ","%20");
        st_nickname=st_nickname.replaceAll(" ","%20");
        st_comment=st_comment.replaceAll(" ","%20");

        //http://qa.galwaykart.com/glaze/reviewset.php?sku=GSG02100&cid=7405
        // &name=test&title=average&comment=v%20good%20product&star=4

        String submit_review_url=Global_Settings.api_custom_url+
                             "reviewset.php?sku="+product_sku+
                            "&cid="+customer_id+
                            "&name="+customer_name+
                            "&title="+st_nickname+
                            "&comment="+st_comment+
                            "&star="+float_rate.toString();


        progressBar_write_review.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                                    submit_review_url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                ////Log.d("response",response.toString());
                if(response!=null) {
                    try {

                        progressBar_write_review.setVisibility(View.GONE);

                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        String getResult=jsonObject.getString("result");

                        final AlertDialog.Builder b;
//                        try
//                        {
//                            b = new AlertDialog.Builder(MainActivity.this);
//                            b.setTitle("Alert");
//                            b.setCancelable(false);
//                            b.setMessage(getResult);
//                            b.setPositiveButton("OK", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int whichButton)
//                                {
//                                    b.create().dismiss();
//@n
//                                    linear_write_review.setVisibility(View.GONE);
//
//                                }
//                            });
//                            b.create().show();
//                        }
//                        catch(Exception ex)
//                        {
//                        }

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
        });


        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);




    }

    /**
     * Add product to wishlist
     */

    private void addToWishList() {

        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_add_wish_list_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();


//                           CommonFun.alertError(MainActivity.this,response.toString());
                        if(response!=null){
                            try {

                                JSONObject jsonObj = new JSONObject(String.valueOf(response));
                                JSONArray jsonArray = jsonObj.getJSONArray("additems");

                                String st_get_wishList_Status = jsonArray.getString(0);


                               // CommonFun.alertError(MainActivity.this,st_get_wishList_Status);


                                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                final Dialog dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
                                ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
                                tv_dialog.setText(st_get_wishList_Status.toString());
                                dialog.show();


                                TimerTask timerTask=new TimerTask() {
                                    @Override
                                    public void run() {


//                                        Intent intent=new Intent(MainActivity.this, OrderListActivity.class);
//                                        startActivity(intent);
//                                        CommonFun.finishscreen(OrderDetails.this);


                                        if(dialog.isShowing())
                                            dialog.dismiss();
                                    }};


                                Timer timer=new Timer();
                                timer.schedule(timerTask,4500);





                            } catch (JSONException e) {
                                e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();


        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if(dataload==false)
           callJSONAPIVolley();

    }


    /**
     * Get Product details from API
     */

    private void callJSONAPIVolley()
    {


        pref= CommonFun.getPreferences(getApplicationContext());
        final String product_sku=pref.getString("showitemsku","");
        String fromurl= Global_Settings.api_url+"index.php/rest/V1/products/"+product_sku;


        pDialog = new TransparentProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();


                        ////Log.d("single",response.toString());

                        //   CommonFun.alertError(MainActivity.this,response.toString());
                        if(response!=null){
                            try {

                                JSONObject jsonObj = new JSONObject(String.valueOf(response));

                                String status_product=jsonObj.getString("status");

                                if(status_product.equals("1")) {

                                    String product_name = jsonObj.getString("name");
                                    st_product_id = jsonObj.getString("id");
                                    //////Log.d("Name",product_name);

                                    tvItemName.setText(product_name);
                                    //tvItemPrice.setText("Rs :"+jsonObj.getString("price"));


                                    JSONArray custom_data = jsonObj.getJSONArray("custom_attributes");

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
                                        }


                                    }
                                    short_desc = "IP: " + ip_of_product + "\n\n" + short_desc;

//                                    JSONObject c = custom_data.getJSONObject(0);
//                                    long_desc = c.getString("value");
//                                    ////Log.d("Long Desc", long_desc);
//
//                                    JSONObject c_short = custom_data.getJSONObject(1);
//                                    short_desc = c_short.getString("value");
//                                    ////Log.d("Short Desc", short_desc);

//
//                                    JSONObject c_product_img = custom_data.getJSONObject(2);
//                                    st_product_image = c_product_img.getString("value");


                                    /**
                                     * Media Gallery Images
                                     * Added on Dec 28, 2017
                                     * Ankesh Kumar
                                     */
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

//                                if(st_customer_gp_id.equalsIgnoreCase(st_logged_gp_id)){
//
//                                    tvItemPrice.setText("Rs :"+jsonObj.getString("st_tier_price"));
////                                    tvItemPrice.setText("Rs :" + jsonObj.getString("price"));
////                                    tvItemPrice.setPaintFlags(tvItemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//                                }
//                                else {
//                                    tvItemPrice.setText("Rs :" + jsonObj.getString("price"));
//
//                                }

                                    if (product_price.equals(""))
                                        product_price = jsonObj.getString("price");


                                    tvItemPrice.setText("₹ " + product_price);

                                    ////Log.d("st_customer_gp_id", st_customer_gp_id);
                                    ////Log.d("st_tier_price", st_tier_price);


                                    if (arr_product_images.length > 0) {
                                        setProductImages();
                                        image_path = image_path + st_product_image;
                                        setImage();
                                    } else
                                        image_path = image_path + st_product_image;

                                    dbh.addCartProductImage(new CartProductImage(product_sku.toString(), image_path.toString()));

                                    short_desc = short_desc.replaceAll("</li>", "<br/>");

                                    if (Build.VERSION.SDK_INT >= 24) {

                                        tvItemShort.setText("" + Html.fromHtml(short_desc, 0));
                                    } else {
                                        tvItemShort.setText("" + Html.fromHtml(short_desc));
                                    }

                                    or_long_desc = long_desc;
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        tvItemLong.setText("" + Html.fromHtml(long_desc, 0));
                                    } else {
                                        tvItemLong.setText("" + Html.fromHtml(long_desc));
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
                                        getProductRating(0, 50);
//                                        if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                                                st_magento_user_zone.equalsIgnoreCase("rkt")) {
//
//                                            checkStockAvailability();
//
//                                        } else {
//                                            getProductRating(0, 50);
//                                        }
//                                    }
                                }
                                else
                                {

                                    final AlertDialog.Builder b;
                                    try
                                    {
                                        b = new AlertDialog.Builder(MainActivity.this);
                                        b.setTitle("Alert");
                                        b.setCancelable(false);
                                        b.setMessage("Product currently not available");
                                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton)
                                            {
                                                Intent intent=new Intent(MainActivity.this,HomePageActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                CommonFun.finishscreen(MainActivity.this);
                                            }
                                        });
                                        b.create().show();
                                    }
                                    catch(Exception ex)
                                    {
                                    }



                                }

                                //setImage();

                            } catch (JSONException e) {
                                e.printStackTrace();
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

    private void setImage() {

        Picasso.with(MainActivity.this)
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

//    private void setPagerDots(){
//
//
//            dots = new ImageView[arr_product_images.length];
//            for (int i = 0; i < arr_product_images.length; i++) {
//                dots[i] = new ImageView(MainActivity.this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(5, 0, 5, 0);
//                dots[i].setLayoutParams(params);
//                dots[i].setImageResource(R.drawable.selecteditem_dot);
//                //ivArrayDotsPager[i].setAlpha(0.4f);
//                dots[i].setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View view) {
//                        view.setAlpha(1);
//                    }
//                });
//                pager_indicator.addView(dots[i]);
//                pager_indicator.bringToFront();
//
//        }
//
//    }

    public void pageSwitcher(int seconds) {
//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
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


                                ////Log.d("Response", response.toString());
                                //     CommonFun.alertError(MainActivity.this,response.toString());
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//                            String cart_id=jsonObj.getString("id");
//
//                            ////Log.d("cart_id",cart_id);
////
////                            SharedPreferences.Editor editor= preferences.edit();
////                            editor.putString("cart_id",cart_id);
////                            editor.commit();

                                // addItemToCart(cart_id);


                                String cart_id = response.toString();
                                cart_id = cart_id.replaceAll("\"", "");
                                addItemToCart(cart_id);

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
                            //////Log.d("ERROR","error => "+error.toString());
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


        /**
         * Depreceated as per bug on process
         */
//        private void getCartId() {
//
//            RequestQueue queue = Volley.newRequestQueue(this);
//
//
//            // st_token_data="w4svj2g3pw7k2vmtxo3wthipjy3ieig0";
//
//            pDialog = new TransparentProgressDialog(MainActivity.this);
//            pDialog.setCancelable(false);
//            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            pDialog.show();
//
//
//            final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_cart_URL, null,
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            if (pDialog.isShowing())
//                                pDialog.dismiss();
//
//                            try {
//                                JSONObject jsonObj = null;
//                                jsonObj = new JSONObject(String.valueOf(response));
//
//                                String cart_id = jsonObj.getString("id");
//
//                                ////Log.d("cart_id", cart_id);
////
////                            SharedPreferences.Editor editor= preferences.edit();
////                            editor.putString("cart_id",cart_id);
////                            editor.commit();
//
//                                addItemToCart(cart_id);
//
//                            } catch (JSONException e) {
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
//
//                            //////Log.d("ERROR","error => "+error.toString());
//                            //CommonFun.alertError(MainActivity.this,error.toString());
//                        }
//                    }
//            ) {
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

        private void addItemToCart(String cart_id) {

            add_to_cart_URL = Global_Settings.api_url + "rest/V1/carts/mine/items/";

            //    String qty = "2";
            //  String sku = "GRP08100";


            pDialog = new TransparentProgressDialog(MainActivity.this);
            pDialog.setCancelable(false);

            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();


            String product_sku = preferences.getString("showitemsku", "");

            String cartqty = "";
            if (showing_txt_qty_control == true)
                cartqty = ed_qty.getText().toString();
            else {
                cartqty = selqty;
                cartqty = cartqty.replace("QUANTITY ", "");
            }


            final String stxt = "{\"cartItem\":{\"sku\": \"" + product_sku + "\",\"qty\": " + cartqty + ",\"quote_id\": \"" + cart_id + "\"}}";

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = null;
            try {
                jsObjRequest = new JsonObjectRequest(Request.Method.POST, add_to_cart_URL, new JSONObject(stxt),
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

                                            final Dialog dialog = new Dialog(MainActivity.this);
                                            dialog.setContentView(R.layout.custom_alert_dialog_design);
                                            TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
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

                                                    String value = preferences.getString("currentitemprice", "");
                                                    if (value != null && !value.equals("")) {
                                                    } else {
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("currentitemprice", product_price);
                                                        editor.commit();
                                                    }

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
            }


            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            jsObjRequest.setShouldCache(false);
            queue.add(jsObjRequest);

        }

//    private void refreshItemCount(){
//
//
//      tokenData = "jqb3cv661kcx69qc300icrxaco8573h0";
//
//        String url_cart_item_list = com.galwaykart.essentialClass.Global_Settings.api_url+"rest/V1/carts/mine";
//        callCartItemList(url_cart_item_list,this);
//
//
//    }


//    private void callCartItemList(String url_cart_item_list, final Context context) {
//
//        final String TAG_total_item_count = "items_count";
//
//        tokenData = tokenData.replaceAll("\"", "");
//
//        pDialog = new TransparentProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url_cart_item_list, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ////Log.d("response", response.toString());
////                        CommonFun.alertError(CartItemList.this, response.toString());
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        try {
//                            JSONObject jsonObj = null;
//                            jsonObj = new JSONObject(String.valueOf(response));
//
//                            int total_cart_count = Integer.parseInt(jsonObj.getString(TAG_total_item_count));
//
//                            refreshItemCount(MainActivity.this);
//                            //updateQty(String.valueOf(total_cart_count));
//                            //initNavigationDrawer();
//                            //updateMenuTitles(toolbar,total_cart_count);
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//                        ////Log.d("ERROR", "error => " + error.toString());
//                        CommonFun.alertError(context, error.toString());
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + tokenData);
//                params.put("Content-Type", "application/json");
//
//                return params;
//            }
//        };
//        queue.add(jsObjRequest);
//
//    }
//
////    private void updateQty(String total_cart_count) {
////        TextView tv_cartqty;
////        tv_cartqty=(TextView)toolbar.findViewById(R.id.cart_icon);
////        tv_cartqty.setText(total_cart_count);
////        tv_cartqty.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                openCart();
////            }
////        });
////    }

        /**
         * Check stock availability of the product in the given zone
         */

        private void checkStockAvailability() {

            pDialog.show();
            //String user_zone = preferences.getString("user_zone", "");

//            String sales_user_zone=pref.getString("st_dist_id","").toLowerCase();
            String user_zone=pref.getString("log_user_zone","").toLowerCase();

//            if (sales_user_zone.equalsIgnoreCase("rkt") &&
//                    magento_user_zone.equalsIgnoreCase("rkt")) {

                String product_sku = preferences.getString("showitemsku", "");
               // String check_stock_of=preferences.getString("check_stock_of","");
            String check_stock_of="snp";
            String stock_check_url = Global_Settings.st_sales_api +
                    "LoadProduct_stock_SearchlistbyProduct_code_Api?" +
                    "franchiseeCode=" + check_stock_of +
                    "&productCode=" + product_sku +
                    "&Itype=247" +
                    "&stocktype=-1" +
                    "&spmode=0";

            ////Log.d("checkstockurl",stock_check_url.toString());

            RequestQueue queue = Volley.newRequestQueue(this);

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, stock_check_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();


                            if (response != null) {
                                try {

                                    ////Log.d("checkstock",response.toString());

                                    response = response.replaceAll("\\[", "");
                                    response = response.replaceAll("]", "");

                                    JSONObject jsonObject = new JSONObject(response);
                                    String st_stock_available = jsonObject.getString("Stock");

                                    //CommonFun.alertError(MainActivity.this,st_stock_available.toString());

                                    available_stock = Integer.parseInt(st_stock_available);
                                    //available_stock=5;

                                    if (available_stock <= 0) {
                                        tv_add_to_cart.setBackgroundColor(Color.RED);
                                        tv_add_to_cart.setText("Out of Stock");
                                        tv_add_to_cart.setEnabled(false);
                                        spinner_qty.setEnabled(false);
                                    }

                              //      setImage();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if(show_recent_item==false)
                                getProductRating(0,50);

                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                      CommonFun.showVolleyException(error, MainActivity.this);


                }
            })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + tokenData);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }

            };


            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            jsObjRequest.setShouldCache(false);
            queue.add(jsObjRequest);

        }

    /**
     * added on Dec 30, 2017
     * Ankesh Kumar
     *
     * Get Overall product rating and reviews
     */
    private void getProductRating(int startrange,int limitrange){


        ////Log.d("Rating Call","start");

        final String product_sku=pref.getString("showitemsku","");
        String rating_url=Global_Settings.api_custom_url+"reviewget.php?start="+startrange+"&limit="+limitrange+"&sku="+product_sku;

        progressBar_read_review.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);

        JsonRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, rating_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                ////Log.d("response",response.toString());
                if(response!=null) {
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(response));


                        JSONArray getResult=jsonObject.getJSONArray("result");

                        if(getResult.length()>0){
                            String getRate=jsonObject.getString("rate");

                            ////Log.d("Rating",getRate);

                            if(!getRate.equals("") && getRate!=null && !getRate.equals("0")){
                                product_rating.setRating(Float.parseFloat(getRate)/20.0f);
                                product_rating_new.setRating(Float.parseFloat(getRate)/20.0f);
                            }
                            else {
                                product_rating.setVisibility(View.GONE);
                                product_rating_new.setVisibility(View.GONE);
                            }

                        }
                        else {
                            product_rating.setVisibility(View.GONE);
                            product_rating_new.setVisibility(View.GONE);
                        }

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
                            //review_title,review_detail,review_nickname,review_rating

//                            HashMap<String,String> dlist=new HashMap<String, String>();
//                            dlist.put(TAG_detail,review_detail[i]);
//                            dlist.put(TAG_title,review_title[i]);
//                            dlist.put(TAG_nickname,review_nickname[i]);
//                            dlist.put(TAG_rating,review_rating[i]);
//
//                            detailsList.add(dlist);

                            if(!jsonObject1.getString("review_value").equals("")&&jsonObject1.getString("review_value")!=null)
                                review_rating[i]=String.valueOf(Integer.parseInt(jsonObject1.getString("review_value"))/20);

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

//    private void getUserDetails(String url){
//
//
//        pDialog = new TransparentProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        final RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        try {
//                            JSONObject jsonObject=new JSONObject(String.valueOf(response));
//
//                            JSONObject jsonObject1=jsonObject.getJSONObject("details");
//
//                            String jsonObject_fcode=jsonObject1.getString("fcode");
//                            String jsonObject_distid=jsonObject1.getString("distributor_id");
//
//
//                            SharedPreferences.Editor editor=pref.edit();
//                            editor.putString("log_user_id",jsonObject_distid);
//                            editor.putString("log_user_zone",jsonObject_fcode);
//                            editor.commit();
//
//
//
//                            ////Log.d("distid",jsonObject_distid);
//                            ////Log.d("distzone",jsonObject_fcode);
//                            //getPaymentMethod(shipping_info_string);
//                            getDistributorDetails(jsonObject_distid);
//
//
//                            //CommonFun.alertError(AddressBookClass.this,jsonObject_fcode+" "+jsonObject_distid);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
//                CommonFun.alertError(MainActivity.this,error.toString());
//            }
//        });
//
//        jsonObjectRequest.setShouldCache(false);
//        requestQueue.add(jsonObjectRequest);
//    }



//    private void getDistributorDetails(String st_dist_id) {
//
//
//        pref = CommonFun.getPreferences(getApplicationContext());
//
//        // String st_dist_id=pref.getString("st_dist_id","");
//
//        String st_Get_Dist_details_URL = Global_Settings.galway_api_url+"returnapi/Load_verify_guest?ID="+st_dist_id;
//        ////Log.d("st_Get_Dist_details_URL",st_Get_Dist_details_URL);
//
//        pDialog = new TransparentProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_Get_Dist_details_URL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
//
//
//                        if(response!=null){
//                            try {
//
//                                dist_details = new JSONArray(String.valueOf(response));
//                                JSONObject dist_details_object =dist_details.getJSONObject(0);
//
//                                String current_zone = dist_details_object.getString("current_zone");
//
//
//                                pref = CommonFun.getPreferences(getApplicationContext());
//
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("st_dist_id",current_zone);
//                                editor.commit();
//
//                              st_sales_user_zone=pref.getString("st_dist_id","").toLowerCase();
//                              st_magento_user_zone=pref.getString("log_user_zone","").toLowerCase();
//
//            if (st_sales_user_zone.equalsIgnoreCase("rkt") &&
//                    st_magento_user_zone.equalsIgnoreCase("rkt")) {
//
//                    checkStockAvailability();
//
//            }
//            else
//            {
//                getProductRating(0,50);
//            }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                ////Log.d("error",e.toString());
//
//
//
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("st_dist_id","");
//                                editor.commit();
//
//
//
//                            }
//                        }
//                    }
//
//
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();
//
////                CommonFun.alertError(RegistrationActivity.this,error.toString());
////                error.printStackTrace();
//
//                CommonFun.showVolleyException(error,MainActivity.this);
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
//        jsObjRequest.setShouldCache(false);
//        queue.add(jsObjRequest);
//
//    }



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

}
