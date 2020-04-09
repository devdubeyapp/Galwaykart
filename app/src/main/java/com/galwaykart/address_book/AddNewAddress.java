package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User can add new Address Activity
 * Billing and Shipping Address
 *
 * Ankesh Kumar
 */

public class AddNewAddress extends BaseActivity {

    SharedPreferences pref;
    String customer_id = "",telephone="",postcode="",city_name="",region="",
                firstname="",st_input_data="",lastname="",company_name="",coming_from="",region_code="",region_id="",add_line1="";

    EditText first_name,last_name,company,phone_no,street_address,city,state,zip,country;
    Button button_save_address;
    TextView tv_title_address;
    CheckBox chk_shipping,chk_billing;
    String add_type="";
    TextView tvChkText;

    final String TAG_region_id= "region_id";
    final String TAG_country_id = "country_id";
    final String TAG_code= "code";

    final String TAG_default_name= "default_name";

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;
    String [] arr_state_name,
            arr_state_code,
            arr_pin_state_id,
            arr_region_id;
    Spinner spinner_state_profile;
    String st_get_State_URL="";
    TransparentProgressDialog pDialog;
    String new_telephone="",new_postcode="",st_pin_city="",st_pin_capital="",
            new_city="",new_firstname="",st_pin_state="",st_pin_state_id="",
            new_lastname="",new_state="",getRegAddressURL = "",
            new_company="",new_region_code="",
            new_region="",new_region_id="",
            new_add_line1="",new_country_id="";
    int item_position,state_pos;

    String tokenData="";
    String is_new="";
    String login_group_id="";
    String return_data="";

    String st_come_from_update="";



//***************************************************************************************************************************

    String add_telephone = "",
            add_customer_id = "",
            address_id = "",
            address_region_id = "",
            address_country_id = "",
            address_telephone = "",
            address_postcode = "",onLoad="false",
            address_city ="",login_email="",
            address_firstname = "",login_lname="",login_fname="",
            address_lastname = "",add_street="",
            st_attribute_code="",st_attribute_value_mob="",
            address_default_shipping="";

            String is_default_shipping ="";
            String is_default_ship_edit = "no";

   // TextView tvChkText;

    Boolean calling_first=false;
    int total_address_data=0;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        Intent intent=null;
        //Log.d("stst_comeFrom",st_come_from_update);
        if(st_come_from_update.equalsIgnoreCase("updateaddress")) {
            intent = new Intent(AddNewAddress.this, CustomerAddressBook.class);
            intent.putExtra("st_come_from_update","updateaddress");

        }
        else
        {
            intent = new Intent(AddNewAddress.this, CustomerAddressBook.class);
            intent.putExtra("st_come_from_update","");
        }

/**
 * Add new address
 */
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);
    }
    public Bundle bundle;
    Boolean is_data_for_distributor=false;
    String add_new="";
    String st_default_ship="",st_default_bill="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);
        initNavigationDrawer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        chk_shipping= findViewById(R.id.chk_shipping);
        chk_billing= findViewById(R.id.chk_billing);
        chk_shipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chk_billing.setChecked(true);

            }
        });


        bundle = getIntent().getExtras();

        if (bundle != null) {
            String st_come_from_update1=bundle.getString("st_come_from_update");
            if(st_come_from_update1!=null && !st_come_from_update1.equals(""))
                st_come_from_update=st_come_from_update1;

//            String data_shipping = bundle.getString("selecteddata");
//            if(data_shipping!=null && !data_shipping.equals(""))
//                is_data_for_distributor=true;

            String add_new1 = bundle.getString("add_new");
            if(add_new1!=null && !add_new1.equals(""))
                add_new=add_new1;

            if(add_new.equalsIgnoreCase("no")){
               address_id=bundle.getString("address_id");
               address_firstname=bundle.getString("first_name");
               address_lastname=bundle.getString("last_name");
               address_telephone=bundle.getString("phone_no");

               address_postcode=bundle.getString("zip");
               address_city=bundle.getString("city");
               add_street=bundle.getString("street_address");
               new_state=bundle.getString("region");
               st_default_ship=bundle.getString("default_ship");
               st_default_bill=bundle.getString("default_bill");

                //is_default_shipping = bundle.getString("is_default_shipping");


            }



             is_default_ship_edit= bundle.getString("is_default_ship_edit");
              if(is_default_ship_edit!=null && !is_default_ship_edit.equals(""))
                  is_default_ship_edit=is_default_ship_edit;



            String total_data = bundle.getString("total_address_data");
            Log.e("total_address_data",total_address_data + "");

            if(total_data!=null && !total_data.equals(""))
                total_address_data= Integer.parseInt(total_data);

        }

        //Log.d("total_data", String.valueOf(total_address_data));
        calling_first=true;
        pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");


        arrayList = new ArrayList<HashMap<String,String>>();
        spinner_state_profile = findViewById(R.id.spinner_state_profile);
        spinner_state_profile.setEnabled(false);
        st_get_State_URL = Global_Settings.api_url+"rest/V1/m-state/statelist";

        coming_from = pref.getString("st_coming_from","");
        if (coming_from != null && !coming_from.equals("")) {

            is_new="";

        }
        else
        {
            coming_from="";
        }


        spinner_state_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                region_code=arr_state_code[i];
                region_id=arr_region_id[i];
                region=arr_state_name[i];

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        login_group_id=pref.getString("login_group_id","");



        if(is_default_ship_edit.equalsIgnoreCase("yes") || total_address_data==0)
        {
            chk_shipping.setChecked(true);
            chk_billing.setChecked(true);

            chk_shipping.setEnabled(false);
            chk_billing.setEnabled(false);
        }
        else
        {
            if(st_default_ship.equals("1"))
                chk_shipping.setChecked(true);
            else
                chk_shipping.setChecked(false);

            if(st_default_bill.equals("1"))
                chk_billing.setChecked(true);
            else
                chk_billing.setChecked(false);
        }


        chk_shipping.setVisibility(View.VISIBLE);
        chk_billing.setVisibility(View.GONE);
        tvChkText= findViewById(R.id.tvChkText);



        //add_type=pref.getString("addnew","");
        tv_title_address= findViewById(R.id.tv_title_address);



        tv_title_address.setText("Shipping Address");
        tvChkText.setVisibility(View.GONE);


        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        company = findViewById(R.id.company);
        phone_no = findViewById(R.id.phone_no);
        //phone_no.setVisibility(View.VISIBLE);


        street_address = findViewById(R.id.street_address);
        city = findViewById(R.id.city);

        zip = findViewById(R.id.zip);
        country = findViewById(R.id.country);
        button_save_address = findViewById(R.id.button_save_address);
        country.setVisibility(View.GONE);


        button_save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences pref = CommonFun.getPreferences(getApplicationContext());
                String login_group_id=pref.getString("login_group_id","");
//
//               if(login_group_id.equals("4"))
//                   // onAddressSaveContinue();
//                   saveAddressAndContinue();
//               else
//               {
                    saveAddressAndContinue();

              // }


            }

        });


//        getRegAddressURL = api_url+"rest/V1/customers/me";
//        getRegisteredAddress();


        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //  Log.d("editable",editable.toString());

                    if (editable.length() == 6) {
                        getStateCity(editable);
                    } else {

                    }

                }



        });

        if(add_new.equalsIgnoreCase("no"))
        {
            showExistingData();
        }
        else
            getState();


    }

    private void getStateCity(Editable editable) {

     String st_get_state_city_URL = Global_Settings.galway_wcf_api_url+"Distributor.svc/GetPincodeDetails/"+editable+"/0";
     //Log.d("st_get_state_city_URL",st_get_state_city_URL);

            try {


                final RequestQueue requestQueue = Volley.newRequestQueue(this);


                pDialog = new TransparentProgressDialog(AddNewAddress.this);
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();


                StringRequest stringRequest = new StringRequest(Request.Method.GET, st_get_state_city_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(pDialog.isShowing())
                                    pDialog.dismiss();

                                try {

                                    if(response!=null)
                                    {
                                        //Log.d("VOLLEYresponse", response.toString());
                                        JSONArray array = new JSONArray(response);

                                        arr_pin_state_id = new String[array.length()];
                                        for(int i = 0; i< array.length(); i++){

                                            JSONObject object = array.getJSONObject(i);
                                            st_pin_city = object.getString("City");
                                            st_pin_capital = object.getString("Capital");
                                            st_pin_state = object.getString("State");
                                            st_pin_state_id = object.getString("statecode");

                                            arr_pin_state_id[i] = st_pin_state_id;
                                        }

                                        setStateCityDefault();
                                    }


                                } catch (Exception e) {
                                    //e.printStackTrace();

                                    //CommonFun.alertError(AddNewAddress.this,e.toString());
                                    Intent intent=new Intent(AddNewAddress.this, ExceptionError.class);
                                    startActivity(intent);

                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pDialog.isShowing())
                            pDialog.dismiss();
//                        Log.d("VOLLEY", error.toString());
//                        CommonFun.alertError(AddNewAddress.this,error.toString());

                        CommonFun.showVolleyException(error,AddNewAddress.this);

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

                        return st_input_data == null ? null : st_input_data.getBytes(StandardCharsets.UTF_8);
                        //return new byte[0];
                    }


                };

                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                //Log.d("error...","Error");

            }
    }

    private void setStateCityDefault() {
        if(!st_pin_state_id.equalsIgnoreCase("null")) {
            for (int i = 0; i < arr_state_code.length; i++) {
                if (st_pin_state_id.equalsIgnoreCase(arr_region_id[i])) {

                    state_pos = i;
                }
            }
            //Log.d("state_pos", "" + state_pos);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNewAddress.this, android.R.layout.simple_spinner_dropdown_item, arr_state_name);
            spinner_state_profile.setAdapter(adapter);

            spinner_state_profile.setSelection(state_pos);
            spinner_state_profile.setEnabled(false);
//            spinner_state_profile.setEnabled(true);


        }
        else{
            //zip.setText("");
            spinner_state_profile.setEnabled(true);
            //Toast.makeText(this,"Service is not available in this pincode",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Fetch all state list from api
     */
    private void getState() {

        //Log.d("st_get_State_URL",st_get_State_URL);
//        pDialog = new TransparentProgressDialog(AddNewAddress.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsObjRequest=null;
        try {
           jsObjRequest = new StringRequest(Request.Method.GET, st_get_State_URL,
                   new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
                    //Log.d("st_get_State_URL",response.toString());

                    if (response != null) {
                        try {

                            JSONArray jsonArray=new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            JSONArray arr_get_state_obj = jsonObject.getJSONArray("statelist");

                            int order_list_length = arr_get_state_obj.length();

                            arr_state_name = new String[order_list_length];
                            arr_state_code = new String[order_list_length];
                            arr_region_id = new String[order_list_length];

                            for(int i=0; i<order_list_length; i++) {

                                JSONObject  order_list_obj = arr_get_state_obj.getJSONObject(i);

                                String st_region_id = order_list_obj.getString(TAG_region_id);
                                String  country_id = order_list_obj.getString(TAG_country_id);
                                String code = order_list_obj.getString(TAG_code);
                                String default_name = order_list_obj.getString(TAG_default_name);

                                int region_code = Integer.parseInt(st_region_id);
                                arr_state_name[i] = default_name;
                                arr_state_code[i] = code;
                                if(region_code < 10)
                                    arr_region_id[i] = "0"+st_region_id;
                                else
                                    arr_region_id[i] = st_region_id;

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNewAddress.this,android.R.layout.simple_spinner_dropdown_item,arr_state_name);
                            spinner_state_profile.setAdapter(adapter);

                            //Log.d("itemdata",new_state);


                            if (new_state != null && !new_state.equals("")){
                                //  Log.d("item_position1312312",""+item_position);
                                spinner_state_profile.setSelection(adapter.getPosition(new_state));

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                           // Intent intent=new Intent(AddNewAddress.this, ExceptionError.class);
                          //  startActivity(intent);
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();

                    CommonFun.showVolleyException(error,AddNewAddress.this);
                    //CommonFun.alertError(AddNewAddress.this,error.toString());

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
        queue.add(jsObjRequest);




    }

    private void showExistingData() {

        first_name.setText(address_firstname);
        last_name.setText(address_lastname);
        phone_no.setText(address_telephone);
        //zip.setHint(address_postcode);
        zip.setText(address_postcode);
        city.setText(address_city);
        street_address.setText(add_street);
        getState();
//        getState();

//        SharedPreferences pref;
//
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putString("new_telephone", address_telephone);
//        editor.putString("new_postcode", address_postcode);
//        editor.putString("new_city", address_city);
//        editor.putString("new_firstname", login_fname);
//        editor.putString("new_lastname", login_lname);
//        editor.putString("new_add_line1", add_street);
//
//        editor.putString("new_st_state", region);
//        editor.putInt("item_position", item_position);
//        editor.commit();

//        getState();
    }


    private void saveAddressAndContinue(){
        String comp_name = company.getText().toString();
        if (comp_name.equals(""))
            comp_name = "-";
        //region_code="";


        String st_tel = phone_no.getText().toString();
        String st_postcode = zip.getText().toString();
        String st_city = city.getText().toString();
        String st_fname = first_name.getText().toString();
        String st_lname = last_name.getText().toString();
        String st_address = street_address.getText().toString();
        String st_state = spinner_state_profile.getSelectedItem().toString();
        int item_position = spinner_state_profile.getSelectedItemPosition();

        int pin_code_length = st_postcode.length();
        int mobile_no_length = st_tel.length();


        Boolean isValid = false;
        Boolean valid_pin = false;
        if ((!st_tel.equals("")) && (!st_postcode.equals("")) && (!st_city.equals("")) && (!st_fname.equals("")) &&
                (!st_lname.equals("")) && (!st_address.equals("")) ) {
            if ((pin_code_length == 6 && mobile_no_length == 10)) {
                valid_pin = true;
                isValid = true;
            }
        }




        if(isValid == true) {
            if (valid_pin == true) {
                tokenData = pref.getString("tokenData", "");

                pref = CommonFun.getPreferences(getApplicationContext());
                String email = pref.getString("user_email", "");


                String save_address_url = Global_Settings.api_url + "rest/V1/customers/me";
                return_data="";

                if (st_attribute_value_mob != null && !st_attribute_value_mob.equals("")){

                }else{
                    st_attribute_value_mob = phone_no.getText().toString().trim();
                }



                String st_default_ship_bill="";
                    if(chk_shipping.isChecked()) {
                       st_default_ship_bill= "\"Default_shipping\":\"" +  "1" + "\"";
                    }

                    if(chk_billing.isChecked()) {
                        if(st_default_ship_bill.equals(""))
                            st_default_ship_bill= "\"default_billing\":\"" + "1" + "\"";
                        else
                            st_default_ship_bill= st_default_ship_bill+ ","+ "\"default_billing\":\"" + "1" + "\"";


                    }



                Set<String> all_address_id;
                all_address_id=new HashSet<String>();
                all_address_id=pref.getStringSet("all_address_id",null);
                String default_address_id=pref.getString("default_address_id","");


                String st_add_id = "";
                if(all_address_id!=null) {

                    if (all_address_id.size() > 0) {
                        ArrayList<String> address_list = new ArrayList<String>(all_address_id);


                        if (address_list.size() > 0) {

                            for (int i = 0; i < address_list.size(); i++) {
                                if (st_add_id.equals("")) {
                                    if (!address_list.get(i).equals(address_id)) {

//                                        if(address_list.get(i).equals(default_address_id))
//                                            st_add_id = "{\"id\":" + address_list.get(i) +
//                                                    ",\"Default_shipping\":\"" +  "1" + "\""+
//                                                    ",\"Default_billing\":\"" +  "1" + "\""+
//                                                    "}";
//                                        else
                                            st_add_id = "{\"id\":" + address_list.get(i) + "}";
                                    }
                                } else {
                                    if (!address_list.get(i).equals(address_id)) {

//                                        if(address_list.get(i).equals(default_address_id))
//                                            st_add_id = st_add_id + "," + "{\"id\":" + address_list.get(i) +
//                                                    ",\"Default_shipping\":\"" +  "1" + "\""+
//                                                    ",\"Default_billing\":\"" +  "1" + "\""+
//                                                    "}";
//                                        else
                                            st_add_id = st_add_id + "," + "{\"id\":" + address_list.get(i) + "}";
                                    }
                                }


                            }

                        }
                    } else {
                        total_address_data = 0;


                    }
                }
                else
                {
                    total_address_data=0;
                }

                if(total_address_data==0  || chk_shipping.isChecked())
                {
                    st_default_ship_bill= "\"Default_shipping\":\"" +  "1" + "\"";
                    st_default_ship_bill= st_default_ship_bill+ ","+ "\"default_billing\":\"" + "1" + "\"";

                    chk_billing.setEnabled(false);
                    chk_shipping.setEnabled(false);

                    Log.e("st_default_ship_bill", st_default_ship_bill);
                }
//                else
//                {
//                    st_default_ship_bill= "\"Default_shipping\":\"" +  "0" + "\"";
//                    st_default_ship_bill= st_default_ship_bill+ ","+ "\"default_billing\":\"" + "0" + "\"";
//
//                    chk_billing.setEnabled(false);
//                    chk_shipping.setEnabled(false);
//
//                    Log.e("st_default_ship_bill", st_default_ship_bill);
//                }



                String all_st_address_id="";
                if(st_add_id.equals(""))
                    all_st_address_id="";
                else
                   all_st_address_id=st_add_id+",";



                String all_ship_default="";
                if(st_default_ship_bill.equals(""))
                    all_ship_default="";
                else
                    all_ship_default=st_default_ship_bill+",";

                Log.e("all_ship_default",all_ship_default);


                String login_telephone=pref.getString("login_telephone","");
                String login_email=pref.getString("login_email","");
                String login_fname=pref.getString("login_fname","");
                String login_lname=pref.getString("login_lname","");

    if (!address_id.equalsIgnoreCase("")) {
        return_data = "{\"customer\":" +
                "{\"email\":\"" + login_email + "\"," +
                "\"lastname\":\"" + login_lname + "\",\"group_id\":" + login_group_id + "," +
                "\"custom_attributes\":[{\"value\":\"" + login_telephone + "\",\"attribute_code\":\"mobile_number\"}]," +
                "\"addresses\":" +
                "[" +
                    all_st_address_id+
                "{\"street\":[\"" + street_address.getText().toString().trim().replaceAll("\n", " ") + "\"]," +
                "\"city\":\"" + city.getText().toString() + "\"," +
                "\"region\":"+
                "{\"region\":\"" + region + "\"," +
                "\"region_id\":\"" + region_id + "\"," +
                "\"region_code\":\"" + region_code + "\"}," +
                "\"lastname\":\"" + last_name.getText().toString().trim() + "\"," +
                "\"id\":\"" + address_id + "\"," +
                "\"postcode\":\"" + zip.getText().toString().trim() + "\"," +
                "\"firstname\":\"" + first_name.getText().toString() + "\"," +
                all_ship_default+
                "\"country_id\":\"IN\"," +
                "\"telephone\":\"" + phone_no.getText().toString().trim() + "\"" +
                "}]," +
                "\"website_id\":\"1\"," +
                "\"store_id\":\"1\"," +
                "\"firstname\":\"" + login_fname + "\"}}";
    } else {
        return_data = "{\"customer\":" +
                "{\"email\":\"" + login_email + "\"," +
                "\"lastname\":\"" + login_lname + "\",\"group_id\":" + login_group_id + "," +
                "\"custom_attributes\":[{\"value\":\"" + login_telephone + "\",\"attribute_code\":\"mobile_number\"}]," +
                "\"addresses\":" +
                "[" +
                    all_st_address_id+
                "{\"street\":[\"" + street_address.getText().toString().trim().replaceAll("\n", " ") + "\"]," +
                "\"city\":\"" + city.getText().toString() + "\"," +
                "\"region\":" +
                "{\"region\":\"" + region + "\"," +
                "\"region_id\":\"" + region_id + "\"," +
                "\"region_code\":\"" + region_code + "\"}," +
                "\"lastname\":\"" + last_name.getText().toString().trim() + "\"," +
                "\"postcode\":\"" + zip.getText().toString().trim() + "\"," +
                "\"firstname\":\"" + first_name.getText().toString() + "\"," +
                all_ship_default+
                "\"country_id\":\"IN\"," +
                "\"telephone\":\"" + phone_no.getText().toString().trim() + "\"" +
                "}]," +
                "\"website_id\":\"1\"," +
                "\"store_id\":\"1\"," +
                "\"firstname\":\"" + login_fname + "\"}}";
    }
Log.d("return_data",return_data);

                pDialog = new TransparentProgressDialog(AddNewAddress.this);
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.show();

                try {

                    RequestQueue requestQueue = Volley.newRequestQueue(this);

                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, save_address_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    //Log.d("responsePut", response);
                                           if(st_come_from_update.equalsIgnoreCase("updateaddress"))
                                        {
                                            Intent intent = new Intent(AddNewAddress.this, CustomerAddressBook.class);
                                            intent.putExtra("st_come_from_update","updateaddress");
                                            startActivity(intent);
                                            CommonFun.finishscreen(AddNewAddress.this);
                                        }
                                        else {
                                            Intent intent = new Intent(AddNewAddress.this, CustomerAddressBook.class);
                                            intent.putExtra("st_come_from_update","");
                                            startActivity(intent);
                                            CommonFun.finishscreen(AddNewAddress.this);
                                        }
//


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ////Log.d("VOLLEY", error.toString());
                            //   CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            //CommonFun.alertError(DeliveryTypeActivity.this,error.toString());
                            CommonFun.showVolleyException(error, AddNewAddress.this);
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


                            ////Log.d("delievery data in",delivery_data_in.toString());
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
                } catch (Exception e) {
                    e.printStackTrace();
                    ////Log.d("error...","Error");
                }
            }
            else {
                CommonFun.alertError(AddNewAddress.this, "Must enter proper mobile no or pincode");
            }
        }
        else {
            CommonFun.alertError(AddNewAddress.this, "Must enter all required fields");
    }



    }


}
