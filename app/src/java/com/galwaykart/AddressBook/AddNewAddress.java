package com.galwaykart.address_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.galwaykart.BaseActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.ExceptionError;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User can add new Address Activity
 * Billing and Shipping Address
 *
 */

public class AddNewAddress extends BaseActivity {

    SharedPreferences pref;
    String customer_id = "",telephone="",postcode="",city_name="",region="",
                firstname="",st_input_data="",lastname="",company_name="",coming_from="",region_code="",region_id="",add_line1="";

    EditText first_name,last_name,company,phone_no,street_address,city,state,zip,country;
    Button button_save_address;
    TextView tv_title_address;
    CheckBox chk_same_bill;
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
            new_lastname="",new_state="",
            new_company="",new_region_code="",
            new_region="",new_region_id="",
            new_add_line1="",new_country_id="";
    int item_position,state_pos;

   // TextView tvChkText;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Intent intent=new Intent(AddNewAddress.this,AddressBook.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);
        initNavigationDrawer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        arrayList = new ArrayList<HashMap<String,String>>();

        spinner_state_profile = (Spinner)findViewById(R.id.spinner_state_profile);
        spinner_state_profile.setEnabled(false);
        st_get_State_URL = Global_Settings.api_url+"glaze/statelist.php";

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

        pref= CommonFun.getPreferences(getApplicationContext());

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
        new_state = pref.getString("new_st_state", "");
        item_position = pref.getInt("item_position",0);


        chk_same_bill=(CheckBox)findViewById(R.id.chk_same);
        tvChkText=(TextView)findViewById(R.id.tvChkText);



        add_type=pref.getString("addnew","");
        tv_title_address=(TextView)findViewById(R.id.tv_title_address);

        /**
         * Check addresss is billing or shipping
         */
        if(add_type.equalsIgnoreCase("billing")) {
            tv_title_address.setText("Billing Address");
            chk_same_bill.setVisibility(View.GONE);
            // tvChkText.setVisibility(View.GONE);
        }
        else {
            tv_title_address.setText("Shipping Address");
            chk_same_bill.setVisibility(View.VISIBLE);
            //tvChkText.setVisibility(View.VISIBLE);
        }

        tvChkText.setVisibility(View.GONE);
        chk_same_bill.setVisibility(View.GONE);


        /**
         * fetch already filled address of the user
         */
        customer_id = pref.getString("customer_id","");
        telephone = pref.getString("telephone","");
        postcode = pref.getString("postcode","");
        city_name = pref.getString("city","");
        firstname= pref.getString("firstname","");
        lastname = pref.getString("lastname","");
        company_name = pref.getString("company","");
//        region_code = pref.getString("region_code","");
//        region = pref.getString("region","");
//        region_id = pref.getString("region_id","");
        add_line1 = pref.getString("add_line1","");
        coming_from = pref.getString("st_coming_from","");



        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        company = (EditText)findViewById(R.id.company);
        phone_no = (EditText)findViewById(R.id.phone_no);
        //phone_no.setVisibility(View.VISIBLE);


        street_address = (EditText)findViewById(R.id.street_address);
        city = (EditText)findViewById(R.id.city);

        zip = (EditText)findViewById(R.id.zip);
        country = (EditText)findViewById(R.id.country);
        button_save_address = (Button)findViewById(R.id.button_save_address);
        country.setVisibility(View.GONE);


        if (new_firstname != null && !new_firstname.equals(""))
            first_name.setText(new_firstname);
        if (new_lastname != null && !new_lastname.equals(""))
            last_name.setText(new_lastname);
        if (new_telephone != null && !new_telephone.equals(""))
            phone_no.setText(new_telephone);
        if (new_add_line1 != null && !new_add_line1.equals(""))
            street_address.setText(new_add_line1);
        if (new_city != null && !new_city.equals(""))
            city.setText(new_city);
        if (new_postcode != null && !new_postcode.equals(""))
            zip.setText(new_postcode);


        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Log.d("editable",editable.toString());
                if(editable.length()==6){
                    getStateCity(editable);
                }

            }
        });



//        if(coming_from.equalsIgnoreCase("Adapter")) {
//            first_name.setText(firstname);
//            last_name.setText(lastname);
//            company.setText(company_name);
//            phone_no.setText(telephone);
//            street_address.setText(add_line1);
//            city.setText(city_name);
//            //state.setText(region);
//            zip.setText(postcode);
//        }
//    else {
//            if (firstname != null && !firstname.equals(""))
//                first_name.setText(firstname);
//            if (lastname != null && !lastname.equals(""))
//                last_name.setText(lastname);
//            if (telephone != null && !telephone.equals(""))
//                phone_no.setText(telephone);
//            if (add_line1 != null && !add_line1.equals(""))
//                street_address.setText(add_line1);
//            if (city_name != null && !city_name.equals(""))
//                city.setText(city_name);
//            if (postcode != null && !postcode.equals(""))
//                zip.setText(postcode);
//
//        }
        button_save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/**
 * Check state "punjab" can be selected only
 * as it is for rkt user only
 */
//                if(region.equalsIgnoreCase("punjab")){
                //CommonFun.alertError(AddNewAddress.this,region.toString());

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
//                //Log.d("st_state",st_state);
//                //Log.d("item_position",""+item_position);
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


                /**
                 * check all the entered address is valid
                 * if yes, please procceed.
                 */
                if(valid_pin == true) {
                    if (isValid == true) {
                        if (chk_same_bill.isChecked() == false) {
                            SharedPreferences pref;
                            pref = CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("customer_id", "");
                            editor.putString("new_telephone", st_tel);
                            editor.putString("new_postcode", st_postcode);
                            editor.putString("new_city", st_city);
                            editor.putString("new_firstname", st_fname);
                            editor.putString("new_lastname", st_lname);
                            editor.putString("new_company", comp_name);
                            editor.putString("new_region_code", region_code);
                            editor.putString("new_country_id", "IN");
                            editor.putString("new_region", region);
                            editor.putString("new_region_id", region_id);
                            editor.putString("new_add_line1", st_address);
                            editor.putString("new_default_shipping", "1");
                            editor.putString("new_default_billing", "1");
                            editor.putString("new_add_added", "true");
                            editor.putString("new_st_state", st_state);
                            editor.putInt("item_position", item_position);
                            editor.commit();


                            if (add_type.equalsIgnoreCase("billing")) {
                                tv_title_address.setText("Billing Address");
                                Intent intent = new Intent(AddNewAddress.this, AddressBook.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                CommonFun.finishscreen(AddNewAddress.this);
                            } else {
                                tv_title_address.setText("Shipping Address");
                                Intent intent = new Intent(AddNewAddress.this, AddressBook.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                CommonFun.finishscreen(AddNewAddress.this);
                            }
                        } else {

                            /**
                             * Save all address details into temp storage
                             */
                            SharedPreferences pref;
                            pref = CommonFun.getPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("new_customer_id", "");
                            editor.putString("new_telephone", phone_no.getText().toString());
                            editor.putString("new_postcode", zip.getText().toString());
                            editor.putString("new_city", city.getText().toString());
                            editor.putString("new_firstname", first_name.getText().toString());
                            editor.putString("new_lastname", last_name.getText().toString());
                            editor.putString("new_company", comp_name);
                            editor.putString("new_region_code", region_code);
                            editor.putString("new_country_id", "IN");
                            editor.putString("new_region", region);
                            editor.putString("new_region_id", region_id);
                            editor.putString("new_add_line1", street_address.getText().toString());
                            editor.putString("new_default_shipping", "1");
                            editor.putString("new_default_billing", "1");
                            editor.putString("new_st_state", st_state);
                            editor.putInt("item_position", item_position);

                            editor.putString("new_bill_customer_id", "");
                            editor.putString("new_bill_telephone", phone_no.getText().toString());
                            editor.putString("new_bill_postcode", zip.getText().toString());
                            editor.putString("new_bill_city", city.getText().toString());
                            editor.putString("new_bill_firstname", first_name.getText().toString());
                            editor.putString("new_bill_lastname", last_name.getText().toString());
                            editor.putString("new_bill_company", comp_name);
                            editor.putString("new_bill_region_code", region_code);

                            editor.putString("new_bill_region", region);
                            editor.putString("new_bill_region_id", region_id);
                            editor.putString("new_bill_add_line1", street_address.getText().toString());

                            editor.commit();
/**
 * Now open delievery Type Activity to the user
 *
 */
                            Intent intent = new Intent(AddNewAddress.this, AddressBook.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            CommonFun.finishscreen(AddNewAddress.this);

                        }
                    } else {
                        CommonFun.alertError(AddNewAddress.this, "Must enter all required fields");
                    }
                }
                else {
                    CommonFun.alertError(AddNewAddress.this, "Must enter proper mobile no or pincode");
                }

//            }
//            else
//                {
//                    CommonFun.alertError(AddNewAddress.this,"Not applicable on this state");
//                }

            }

        });


        getState();


    }

    private void getStateCity(Editable editable) {

     String st_get_state_city_URL =  "Distributor.svc/GetPincodeDetails/"+editable+"/0";
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
//                        //Log.d("VOLLEY", error.toString());
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

                        try {

                            return st_input_data == null ? null : st_input_data.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", st_input_data, "utf-8");
                            return null;
                        }
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


        }
        else{
            zip.setText("");
            spinner_state_profile.setEnabled(true);
            Toast.makeText(this,"Service is not available in this pincode",Toast.LENGTH_LONG).show();
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

        pDialog = new TransparentProgressDialog(AddNewAddress.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest=null;
        try {
             jsObjRequest = new JsonObjectRequest(Request.Method.GET, st_get_State_URL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    if (response != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray arr_get_state_obj = jsonObject.getJSONArray("statlist");
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


                            if (new_state != null && !new_state.equals("")){
                              //  //Log.d("item_position1312312",""+item_position);
                                spinner_state_profile.setSelection(item_position);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent=new Intent(AddNewAddress.this, ExceptionError.class);
                            startActivity(intent);
                        }
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();

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

}
