package com.galwaykart.helpdesksupport;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.helpdesksupport.orderdetails.ComplaintOrderDetailModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RTORelatedComplaint extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;
    private String st_token_data="";
    private String deviceNumber = "7";
    private String write_complaint_str = "";

    private TransparentProgressDialog pDialog, pDialog1;

    private TextView tv_compaint_submit;
    private EditText write_complaint_et;
    LinearLayout ly_write;

    private String input_data = "";
    private Dialog dialog;

    private String entity_id = "";
    private String request_type = "";
    private String str_complaint_category_id = "";


    private int json_len_arry=0;
    private String stProductData= "";
    private String stImageData= "";
    ArrayList<String> str_qty_array = new ArrayList<String>();
    ArrayList<String> str_resolution_array = new ArrayList<String>();
    ArrayList<String> str_reason_array = new ArrayList<String>();
    ArrayList<String> str_condition_array = new ArrayList<String>();
    ArrayList<String> str_item_id_array = new ArrayList<String>();
    ArrayList<String> str_pname_array = new ArrayList<String>();
    ArrayList<String> str_psku_array = new ArrayList<String>();


    private String qty_ordered="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_simple_complaint, container, false);

        entity_id = getArguments().getString("entity_id");
        request_type = getArguments().getString("request_type");
        str_complaint_category_id = getArguments().getString("str_complaint_category_id");

        pref = CommonFun.getPreferences(getActivity());
        st_token_data=pref.getString("tokenData","");

        write_complaint_et = v.findViewById(R.id.write_complaint_et);
        ly_write = v.findViewById(R.id.ly_write);

        tv_compaint_submit = v.findViewById(R.id.tv_compaint_submit);
        tv_compaint_submit.setOnClickListener(this);

        jsonOrderDetails();

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tv_compaint_submit:

                if(OnClickValidation())
                {
                    submitComplaintJson();
                }

                break;
        }

    }



    private void submitComplaintJson() {

        String str_write_complaint = write_complaint_et.getText().toString();

        SharedPreferences pref1 = CommonFun.getPreferences(getActivity());
        String tokenData = pref1.getString("tokenData", "");

        String str_first_name = pref.getString("login_fname", "");
        String str_last_name = pref1.getString("login_lname", "");
        String str_customer_id = pref1.getString("login_id", "");


        String st_submit_complaint_url = Global_Settings.api_url + "rest/V1/m-help-requestsubmit";


        //Log.d("input_data",input_data);

        input_data ="{\"storeId\":1," +
                "\"orderEntityId\":"+entity_id+"," +
                "\"customerFirstName\":\""+str_first_name+"\"," +
                "\"customerlastName\":\""+str_last_name+"\"," +
                "\"productData\":["+stProductData+"]," +
                "\"customerId\":"+str_customer_id+"," +
                "\"sourceId\":\"" + deviceNumber + "\"" +
                ",\"videoUrl\":\"" +"\""+
                ",\"requestTypeId\":\"" + str_complaint_category_id + "\"" +
                ",\"comment\":\""+str_write_complaint+"\"" +
                ",\"imageData\":["+""+"]}";




        Log.e("input_data", input_data);

        pDialog = new TransparentProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_submit_complaint_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Log.e("responsePutComplaint", response);
                            if (response != null) {
                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int j = 0; j<jsonArray.length(); j++)
                                    {
                                        JSONObject jsonObj= jsonArray.getJSONObject(j);
                                        String respo_status = jsonObj.getString("status");
                                        String message11 = jsonObj.getString("message");
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();

                                        if (respo_status.equals("1"))
                                        {
                                            dialog = new Dialog(getActivity());
                                            dialog.setContentView(R.layout.custom_alert_dialog_design);
                                            TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                            tv_dialog.setText(message11);
                                            ImageView image_view_dialog = dialog.findViewById(R.id.image_view_dialog);
                                            dialog.show();

                                            new CountDownTimer(2000, 2000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    // TODO Auto-generated method stub

                                                }

                                                @Override
                                                public void onFinish() {
                                                    if (dialog.isShowing())
                                                        dialog.dismiss();

                                                    Intent intent=new Intent(getActivity(), MyComplaints.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            }.start();

                                        } else {
                                            //Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();
                                            CommonFun.alertError(getActivity(),message11);
                                        }
                                    }


                                }catch (Exception e)
                                {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    String err_msg="currently, there is no help available";
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
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            //Log.d("log_error", errorString);

                            try {
                                JSONObject object = new JSONObject(errorString);
                                String st_msg = object.getString("message");
                                String st_code = object.getString("code");
                                CommonFun.alertError(getActivity(), st_msg);
                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, getActivity());
                            }


                        }
                    } else
                        CommonFun.showVolleyException(error, getActivity());
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + st_token_data);
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

        }

    }


    public void jsonOrderDetails() {

        final ArrayList<ComplaintOrderDetailModel> complaint_list = new ArrayList<>();
        String st_products_details_url = Global_Settings.api_url+"rest/V1/m-order/view/"+ entity_id;
        //String st_products_details_url = "https://www.galwaykart.com/rest/V1/m-order/view/" + entity_id;
        Log.e("st_products_details_url",st_products_details_url);


        pDialog = new TransparentProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, st_products_details_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();

                                    ly_write.setVisibility(View.VISIBLE);

                                    Log.e("Order_Details_Response", response);
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                                            Log.e("ArryLenght",jsonArray.length() + "");

                                            json_len_arry = jsonArray.length();
                                            for(int j = 0; j<json_len_arry; j++) {
                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);

                                                str_qty_array.add(jsonObjFinal.getString("qty_ordered"));
                                                str_resolution_array.add(str_complaint_category_id);
                                                str_reason_array.add(str_complaint_category_id);
                                                str_condition_array.add(str_complaint_category_id);
                                                str_item_id_array.add(jsonObjFinal.getString("item_id"));


                                                String order_id = jsonObjFinal.getString("order_id");
                                                String product_id = jsonObjFinal.getString("product_id");
                                                String sku = jsonObjFinal.getString("sku");
                                                String product_type= jsonObjFinal.getString("product_type");
                                                String product_name= jsonObjFinal.getString("name");
                                                String product_price= jsonObjFinal.getString("original_price");
                                                qty_ordered= jsonObjFinal.getString("qty_ordered");
                                                String order_date_time= jsonObjFinal.getString("updated_at");
                                                String image = jsonObjFinal.getString("image");

                                                str_pname_array.add(product_name);
                                                str_psku_array.add(sku);


                                            }
                                            Log.e("str_qty_array",str_qty_array + "");
                                            Log.e("str_item_id_array",str_item_id_array + "");


                                            inputProductsDetails();





                                        }

                                        catch (Exception e) {
                                            //e.printStackTrace();
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                            String err_msg="currently, there is no items available";
                                            Snackbar.make(getActivity().findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
                                            //tv_notice.setText(err_msg);
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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
                            params.put("Authorization", "Bearer " + st_token_data);
                            params.put("Content-Type", "application/json");
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

    public void inputProductsDetails() {
        stProductData = "";
        for(int i=0;i<json_len_arry;i++)
        {
            if (stProductData.equalsIgnoreCase("")){



                stProductData = "{\"qty\":"+str_qty_array.get(i)+"," +
                        "\"resolution\":"+str_resolution_array.get(i)+"," +
                        "\"order_qty\":"+str_qty_array.get(i)+"," +
                        "\"reason\":"+str_reason_array.get(i)+
                        ",\"product_name\":\"" +str_pname_array.get(i)+"\""+
                        ",\"product_sku\":\"" +str_psku_array.get(i)+"\""+
                        ",\"condition\":"+str_condition_array.get(i)+"," +
                        "\"orderItemId\":"+str_item_id_array.get(i)+"}";

            }
            else
            {
                stProductData = stProductData+","+"{\"qty\":"+str_qty_array.get(i)+"," +
                        "\"resolution\":"+str_resolution_array.get(i)+"," +
                        "\"order_qty\":"+str_qty_array.get(i)+"," +
                        "\"reason\":"+str_reason_array.get(i)+
                        ",\"product_name\":\"" +str_pname_array.get(i)+"\""+
                        ",\"product_sku\":\"" +str_psku_array.get(i)+"\""+
                        ",\"condition\":"+str_condition_array.get(i)+"," +
                        "\"orderItemId\":"+str_item_id_array.get(i)+"}";
            }
        }
    }


    public boolean OnClickValidation() {

        write_complaint_str = write_complaint_et.getText().toString().trim();
        if (TextUtils.isEmpty(write_complaint_et.getText().toString().trim())) {
            write_complaint_et.setError("Write your Address here");
            write_complaint_et.setFocusable(true);
            return false;
        }
        return true;
    }
}
