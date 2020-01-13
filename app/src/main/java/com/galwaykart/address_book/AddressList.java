package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.galwaykart.BaseActivity;
import com.galwaykart.Cart.CartItemList;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumitsaini on 5/23/2018.
 */

public class AddressList extends BaseActivity {

    ListView list_address;
    String url_address="";

    ArrayList<HashMap<String,String>> itemList;
    String tokenData="",add_line1="",add_line2="";

    JSONArray addressArray = null;

    final String TAG_region= "region";
    final String TAG_street = "street";
    final String TAG_company= "company";
    final String TAG_telephone= "telephone";
    final String TAG_postcode = "postcode";
    final String TAG_city = "city";
    final String TAG_firstname= "firstname";
    final String TAG_lastname = "lastname";
    final String TAG_customer_id = "customer_id";
    SharedPreferences preferences;
    RelativeLayout rel_no_address;
    int length_of_street;
    HashMap<String, String> hashMap;
    Button btn_add_address;
    String st_add_line1="";
    String customer_id = "",telephone="",postcode="",city="",region="",default_billing="",default_shipping="",
            firstname="",lastname="",company="",street="",region_code="",region_id="",
            country_id = "",
            new_add_added="",
            new_telephone="",
            new_postcode="",
            new_city="",
            new_firstname="",
            new_lastname="",
            new_company="",
            new_region_code="",
            new_region="",
            new_region_id="",
            new_add_line1="",
            new_country_id="";

    TransparentProgressDialog pDialog;
    TextView tv_title_address;

    Button btn_add_new_address;
    SharedPreferences pref;
    JSONArray dist_details = null;
    Boolean isAddressLoad=false;

    Button btn_change_address;
    String city_name="",company_name="";
    String user_detail_url="";

    String [] arr_add_line,arr_telephone,arr_postcode,arr_region,
            arr_firstname,arr_lastname,arr_state,arr_customer_id,arr_company;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(AddressList.this, CartItemList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onResume() {
        super.onResume();



        //if(isAddressLoad==false)

        // getUserDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_shipping_address);

        preferences = CommonFun.getPreferences(getApplicationContext());
        pref = CommonFun.getPreferences(getApplicationContext());
        list_address = (ListView) findViewById(R.id.list_address);


        arr_telephone = new String[2];
        arr_postcode = new String[2];
        arr_firstname = new String[2];
        arr_lastname = new String[2];
        arr_region = new String[2];
        arr_state = new String[2];
        arr_customer_id = new String[2];
        arr_add_line = new String[2];
        arr_company = new String[2];


        itemList=new ArrayList<HashMap<String, String>>();

        new_telephone = pref.getString("new_telephone", "");
        new_postcode = pref.getString("new_postcode", "");
        new_city = pref.getString("new_city", "");
        new_firstname = pref.getString("new_firstname", "");
        new_lastname = pref.getString("new_lastname", "");
        new_company = pref.getString("new_company", "");
        new_region_code = pref.getString("new_region_code", "");
        new_region = pref.getString("new_region", "");
        new_region_id = pref.getString("new_region_id", "");
        new_add_line1 = pref.getString("new_add_line1", "");
        new_country_id = pref.getString("new_country_id", "");


        telephone = pref.getString("telephone", "");
        postcode = pref.getString("postcode", "");
        city_name = pref.getString("city", "");
        firstname = pref.getString("firstname", "");
        lastname = pref.getString("lastname", "");
        company_name = pref.getString("company", "");
        region_code = pref.getString("region_code", "");
        region = pref.getString("region", "");
        region_id = pref.getString("region_id", "");
        add_line1 = pref.getString("add_line1", "");
        country_id = pref.getString("country_id", "");

            arr_telephone[0] = telephone;
            arr_postcode[0] = postcode;
            arr_firstname[0] = firstname;
            arr_lastname[0] = lastname;
            arr_region[0] = city;
            arr_state[0] = region;
            arr_customer_id[0] = country_id;
            arr_add_line[0] = add_line1;
            arr_company[0] = company_name;

            arr_telephone[1] = new_telephone;
            arr_postcode[1] = new_postcode;
            arr_firstname[1] = new_firstname;
            arr_lastname[1] = new_lastname;
            arr_region[1] = new_city;
            arr_state[1] = new_region;
            arr_customer_id[1] = new_country_id;
            arr_add_line[1] = new_add_line1;
            arr_company[1]=new_company;


        for(int i=0;i<arr_add_line.length;i++) {

            hashMap = new HashMap<String, String>();

                hashMap.put(TAG_customer_id, arr_customer_id[i]);
                hashMap.put(TAG_street, arr_add_line[i]);
                hashMap.put(TAG_telephone, "T: " + arr_telephone[i]);
                hashMap.put(TAG_postcode, "Pin: " + arr_postcode[i]);
                hashMap.put(TAG_city,  arr_region[i]);
                hashMap.put(TAG_firstname, arr_firstname[i] + " " + arr_lastname[i]);
                hashMap.put(TAG_company, arr_company[i]);
                hashMap.put(TAG_region, arr_state[i]);

            itemList.add(hashMap);

            //Log.d("arr_customer_id",arr_customer_id[i].toString());
            //Log.d("arr_add_line",arr_add_line[i].toString());
            //Log.d("arr_telephone",arr_telephone[i].toString());
            //Log.d("arr_postcode",arr_postcode[i].toString());
            //Log.d("arr_region",arr_region[i].toString());
            //Log.d("arr_firstname",arr_firstname[i].toString());
            //Log.d("arr_lastname",arr_lastname[i].toString());
            //Log.d("arr_company",arr_company[i].toString());
            //Log.d("arr_state",arr_state[i].toString());

            SharedPreferences pref;
            pref = CommonFun.getPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("arr_customer_id", arr_customer_id[i].toString());
            editor.putString("arr_telephone", arr_telephone[i].toString());
            editor.putString("arr_postcode", arr_postcode[i].toString());
            editor.putString("arr_region", arr_region[i].toString());
            editor.putString("arr_firstname", arr_firstname[i].toString());
            editor.putString("arr_lastname", arr_lastname[i].toString());
            editor.putString("arr_company", arr_company[i].toString());
            editor.putString("arr_state", arr_state[i].toString());
            editor.commit();




        }

        updateAddressList();

        SharedPreferences.Editor editor=pref.edit();
        editor.putString("st_dist_id","");
        editor.putString("log_user_zone","");
        editor.commit();

        SharedPreferences.Editor editor_p=preferences.edit();
        editor_p.putString("st_dist_id","");
        editor_p.putString("log_user_zone","");
        editor_p.commit();

        initNavigationDrawer();

        rel_no_address = (RelativeLayout) findViewById(R.id.rel_no_address);
        btn_add_address = (Button) findViewById(R.id.btn_add_address);
        btn_add_address.setVisibility(View.GONE);

     tv_title_address = (TextView) findViewById(R.id.tv_title_address);

        String add_type = preferences.getString("addnew", "");
        if (add_type != null && !add_type.equals("")) {
            if (add_type.equalsIgnoreCase("billing"))
                tv_title_address.setText("Billing Address");
            else
                tv_title_address.setText("Shipping Address");
        } else
            tv_title_address.setText("Shipping Address");




        btn_add_new_address = (Button) findViewById(R.id.btn_add_new_address);
        btn_add_new_address.setVisibility(View.GONE);


        btn_change_address=(Button)findViewById(R.id.btn_change_address);
        btn_change_address.setVisibility(View.GONE);

    }




    @Override
    protected void onStop() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
        super.onStop();

    }




    private void updateAddressList() {


        ListAdapter lstadapter = new AddressBookAdapter(AddressList.this, itemList, R.layout.activity_default_shipping_address_list,
                new String[]{TAG_street, TAG_telephone, TAG_postcode, TAG_city, TAG_firstname, TAG_company},
                new int[]{R.id.textStreet_name,
                        R.id.textTelephone_name,
                        R.id.textPincode_name,
                        R.id.textCity_name,
                        R.id.textCustomer_name,
                        R.id.textCompany_name

                }
        );

        if (lstadapter.getCount() > 0) {
            list_address.invalidate();
            list_address.setAdapter(lstadapter);



        }
    }



}
