package com.galwaykart.helpdesksupport.simplecomplaint;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.BaseActivityWithoutCart;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.helpdesksupport.CompCategModel;
import com.galwaykart.profile.OrderListActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleComMainActivity extends BaseActivityWithoutCart {

    //RBLSH1228946
    private SharedPreferences pref;
    private String st_token_data="";

    public static final String SELECT_COMPLAINT_TYPE = "---Please Choose--- ";
    public static final String SELECT_ORDER = "--Select Orders--";
    private FrameLayout fl;
    private Spinner complaint_reasons_spinner, order_label_spinner;
    private String str_complaint_category="Select Compaint Queries", str_complaint_category_id="", str_item_show="", str_request_type="";
    private String str_entity_id="", str_order_label="", str_order_list_value="";
    private ArrayList<CompCategModel> compCategModels;
    private ArrayList<OrderLabelListModel> orderLabelModels;
    int chk = 0;
    int chk1 = 0;

    private TransparentProgressDialog pDialog;


    FragmentManager manager;
    FragmentTransaction transaction;
    String entity_id="";
    String order_id="";

    String update_at="";

    LinearLayout ly_order_label_list;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_com_main);

        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });


        fl = findViewById(R.id.frame);
        complaint_reasons_spinner = findViewById(R.id.complaint_reasons_spinner);
        compCategModels = new ArrayList<>();

        ly_order_label_list = findViewById(R.id.ly_order_label_list);
        ly_order_label_list.setVisibility(View.GONE);

        order_label_spinner = findViewById(R.id.order_label_spinner);
        orderLabelModels = new ArrayList<>();

        pref = CommonFun.getPreferences(SimpleComMainActivity.this);
        st_token_data=pref.getString("tokenData","");

        getComplaintCategory();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack(){
        Intent intent=new Intent(this, OrderListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        CommonFun.finishscreen(this);
    }


    private void getComplaintCategory() {

        complaint_reasons_spinner.setSelection(0, false);
        complaint_reasons_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_complaint_category = parent.getItemAtPosition(position).toString();

                chk += 1;
                if (chk > 1)
                {
                    str_complaint_category_id = compCategModels.get(position).getComplaintCategory_id();
                    str_item_show = compCategModels.get(position).getShow();
                    str_request_type = compCategModels.get(position).getRequest_type();
                }

                Log.e("str_complaint_category", str_complaint_category);
                Log.e("str_complaint_cat_id", str_complaint_category_id + "");
                Log.e("str_item_show", str_item_show + "");
                Log.e("str_request_type", str_request_type + "");

                if(str_complaint_category.equals(SELECT_COMPLAINT_TYPE))
                {
                    String err_msg="Please select complaint type";
                   // Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                }

                else if(str_request_type.equalsIgnoreCase("0") || str_request_type.equalsIgnoreCase("1"))
                {
                    ly_order_label_list.setVisibility(View.VISIBLE);
                    getOrdersLabelList();
                }
                else
                {

                    ly_order_label_list.setVisibility(View.GONE);

                    fl.setVisibility(View.VISIBLE);
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    SimpleComplaintFragment fragment = new SimpleComplaintFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("entity_id", str_entity_id);
                    bundle.putString("order_id", order_id);
                    bundle.putString("request_type", str_request_type);
                    bundle.putString("str_complaint_category_id", str_complaint_category_id);

                    fragment.setArguments(bundle);

                    transaction.replace(R.id.frame, fragment);
                    transaction.commit();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        complaintCategoryJson();
    }

    public void complaintCategoryJson() {

        String st_complaint_category_url = Global_Settings.api_url + "rest/V1/m-help-request-list";

        //Log.d("st_complaint_url",st_complaint_category_url);
        pDialog = new TransparentProgressDialog(SimpleComMainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SimpleComMainActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_complaint_category_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("Category_Response", response);
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObj= jsonArray.getJSONObject(j);
                                                String value_complaint_id = jsonObj.optString("value", "");
                                                String label_title = jsonObj.optString("label", "");
                                                String is_items_show = jsonObj.optString("is_items", "");
                                                String match_code = jsonObj.optString("match_code", "");
                                                String request_type = jsonObj.optString("request_type", "");

                                                Log.e("value_complaint_id",value_complaint_id);
                                                Log.e("label_title",label_title);
                                                Log.e("is_items_show",is_items_show);
                                                Log.e("match_code", match_code);
                                                Log.e("request_type",request_type);


                                                CompCategModel categorymodel = new CompCategModel();
                                                categorymodel.setComplaintCategory_id(value_complaint_id);
                                                categorymodel.setTitle(label_title);
                                                categorymodel.setShow(is_items_show);
                                                categorymodel.setMatch_code(match_code);
                                                categorymodel.setRequest_type(request_type);
                                                compCategModels.add(categorymodel);

                                            }


                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no help available";
                                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();

                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SimpleComMainActivity.this, android.R.layout.simple_spinner_item) {

                                            @Override
                                            public View getView(int position, View convertView, ViewGroup parent) {

                                                View v = super.getView(position, convertView, parent);
                                                if (position == getCount()) {
                                                  ((TextView) v.findViewById(android.R.id.text1)).setText("---Please Choose--- ");
                                                   ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                                                }

                                                return v;
                                            }
                                            @Override
                                            public int getCount() {
                                                return super.getCount()-1 ; // you dont display last item. It is used as hint.
                                            }
                                        };

                                        //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                        adapter.setDropDownViewResource(R.layout.spinner_item_custom_layout);

                                        if(compCategModels.size()>0) {
                                            for (int j = 0; j < compCategModels.size(); j++) {
                                                adapter.add(compCategModels.get(j).getTitle());
                                            }
                                            adapter.add("---Please Choose--- ");
                                            complaint_reasons_spinner.setAdapter(adapter);
                                            complaint_reasons_spinner.setSelection(adapter.getCount());
                                        }
                                        else
                                        {
                                            CommonFun.alertError(SimpleComMainActivity.this,"No option available right now");
                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            String err_msg="currently, there is no complaint available";
                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + st_token_data);
                            return params;
                        }
                    };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void getOrdersLabelList() {

        order_label_spinner.setSelection(0, false);
        order_label_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_order_label= parent.getItemAtPosition(position).toString();

                chk += 1;
                if (chk > 1) {
                    str_order_label= orderLabelModels.get(position).getOrder_label_list();
                    str_entity_id = orderLabelModels.get(position).getEntity_id();
                }
                Log.e("str_entity_id", str_entity_id);
                Log.e("str_order_label", str_order_label + "");

               if(str_order_label.equalsIgnoreCase(SELECT_ORDER))
                {
                    String err_msg="Please select #order";
                    //Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                }
               else if(str_request_type.equalsIgnoreCase("1"))
               {

                   fl.setVisibility(View.VISIBLE);
                   manager = getSupportFragmentManager();
                   transaction = manager.beginTransaction();
                   GeneralComplaintFragment fragment1 = new GeneralComplaintFragment();

                   Bundle bundle = new Bundle();
                   bundle.putString("entity_id", str_entity_id);
                   bundle.putString("order_id", order_id);
                   bundle.putString("request_type", str_request_type);

                   bundle.putString("str_complaint_category_id", str_complaint_category_id);
                   Log.e("entity_id_CMSA_Sim", str_entity_id);
                   fragment1.setArguments(bundle);
                   transaction.replace(R.id.frame, fragment1);
                   transaction.commit();

               }
               else if(str_request_type.equalsIgnoreCase("0"))
               {
                   fl.setVisibility(View.VISIBLE);
                   manager = getSupportFragmentManager();
                   transaction = manager.beginTransaction();
                   ItemsRelatedcomFragment fragment = new ItemsRelatedcomFragment();

                   Bundle bundle = new Bundle();
                   bundle.putString("entity_id", str_entity_id);
                   bundle.putString("order_id", order_id);
                   bundle.putString("request_type", str_request_type);
                   bundle.putString("str_complaint_category_id", str_complaint_category_id);

                   Log.e("entity_id_CMSA", str_entity_id);
                   Log.e("order_id_CMSA", order_id);
                   Log.e("str_compl_category_id",  str_complaint_category_id);
                   Log.e("request_type", str_request_type);
                   fragment.setArguments(bundle);

                   transaction.replace(R.id.frame, fragment);
                   transaction.commit();
               }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        complaintOrderListJson();
    }
    public void complaintOrderListJson() {

        String st_order_list_url = Global_Settings.api_url + "rest/V1/m-help-request-order-list";

        final String input_data="{\"request_type\":\""+str_request_type+"\"}";

        Log.e("input_data_request", input_data);

        //Log.d("st_order_list_url",st_complaint_category_url);
        pDialog = new TransparentProgressDialog(SimpleComMainActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SimpleComMainActivity.this);
            StringRequest stringRequest =
                    new StringRequest(Request.Method.POST, st_order_list_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    Log.e("Orders_Response", response);
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObj= jsonArray.getJSONObject(j);
                                                String value_order_id = jsonObj.optString("value", "");
                                                String order_label = jsonObj.optString("label", "");

                                                OrderLabelListModel orderLabelListModel = new OrderLabelListModel();
                                                orderLabelListModel.setEntity_id(value_order_id);
                                                orderLabelListModel.setOrder_label_list(order_label);
                                                orderLabelModels.add(orderLabelListModel);

                                            }


                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no orders";
                                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();

                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SimpleComMainActivity.this, android.R.layout.simple_spinner_item) {

                                            @Override
                                            public View getView(int position, View convertView, ViewGroup parent) {

                                                View v = super.getView(position, convertView, parent);
                                                if (position == getCount()) {
                                                    //((TextView) v.findViewById(android.R.id.text1)).setText("--Select Orders--");
                                                    //((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                                                }

                                                return v;
                                            }
                                            @Override
                                            public int getCount() {
                                                return super.getCount()- 2 ; // you dont display last item. It is used as hint.
                                            }
                                        };
                                        adapter.setDropDownViewResource(R.layout.spinner_item_custom_layout);
                                        if(orderLabelModels.size()>0) {
                                            for (int j = 0; j < orderLabelModels.size(); j++) {
                                                adapter.add(orderLabelModels.get(j).getOrder_label_list());
                                            }
                                            //adapter.add("--Select Orders--");
                                            order_label_spinner.setAdapter(adapter);
                                            order_label_spinner.setSelection(adapter.getCount());
                                        }
                                        else
                                        {
                                            CommonFun.alertError(SimpleComMainActivity.this,"No option available right now");
                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            String err_msg="currently, there is no orders";
                            Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
                            return input_data == null ? null : input_data.getBytes(StandardCharsets.UTF_8);
                        }

                        @Override
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + st_token_data);
                            return params;
                        }
                    };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
