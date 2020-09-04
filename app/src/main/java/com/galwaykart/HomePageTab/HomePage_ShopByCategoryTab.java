package com.galwaykart.HomePageTab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.galwaykart.R;
import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.essentialClass.CommonFun;

import com.galwaykart.essentialClass.VolleySingleton;
import com.galwaykart.listAdapters.ExpandableListViewAdapter;
import com.galwaykart.productList.ProductListActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;



public class HomePage_ShopByCategoryTab extends Fragment {

    ExpandableListView exp_category_list;
    ExpandableListViewAdapter listAdapter;

    String fromurl="";
    JSONArray childrenData = null;

    JSONArray subChildrenData = null;

    String[] arr_category_name;
    String[] arr_category_active;
    String[] arr_category_id;
    String sub_category_id = "",sub_category_name = "";


    ArrayList<String> sub_cat_length_list;
    ArrayList<String> sub_cat_get_name;
    ArrayList<String> sub_cat_get_id;

    ArrayList<String> product_name_list;
    ArrayList<String> product_id_list;
    ArrayList<String> category_name_list;
    int length_of_all_category;
    int length_of_all_active_category;


    SharedPreferences preferences;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DatabaseHandler dbh;

    String onBack="";

    SharedPreferences pref;
    int total_head_not_active=0;
    Context mContext;
    ProgressBar progress_bar_shopCategory;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.homepage_shopbycategorytab_v1, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContext=getActivity();

        //Crashlytics.getInstance().crash();
        preferences = getActivity().getSharedPreferences("GalwayKart",MODE_PRIVATE);
        pref = CommonFun.getPreferences(getActivity());

        progress_bar_shopCategory= getActivity().findViewById(R.id.progress_bar_shopCategory);
        product_name_list = new ArrayList<String>();
        product_id_list = new ArrayList<String>();
        category_name_list = new ArrayList<String>();
        sub_cat_length_list=new ArrayList<String>();
        sub_cat_get_name=new ArrayList<String>();
        sub_cat_get_id = new ArrayList<String>();

        exp_category_list = getActivity().findViewById(R.id.exp_category_list);


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            exp_category_list.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            //searchExpListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            exp_category_list.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            //searchExpListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }


        exp_categoryListener();


    }

    private void exp_categoryListener() {
        exp_category_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                //  GroupHeadContent headContent=(GroupHeadContent)listAdapter.getGroup(i);
                //  final String selname=headContent.getName().toString().toLowerCase();

                String selname=arr_category_name[i].toLowerCase();

                if(Integer.parseInt(sub_cat_length_list.get(i))<=0){

                    SharedPreferences.Editor editor = preferences.edit();

                    ////Log.d("arrcatid",arr_category_id[i].toString());
                    editor.putString("selected_id",arr_category_id[i]);
                    editor.putString("selected_name",selname);
                    editor.commit();

                    String value_email=pref.getString("login_email","");
                    //if(value_email!=null && !value_email.equals("")) {
                        Intent intent = new Intent(getActivity(), ProductListActivity.class);
                        intent.putExtra("onback", "showcatlist");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        CommonFun.finishscreen(getActivity());
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(getActivity(), GuestProductListActivity.class);
//                        intent.putExtra("onback", "showcatlist");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        startActivity(intent);
//                        CommonFun.finishscreen(getActivity());
//                    }


                }


                return false;
            }
        });

        /**
         * When any child is clicked by user
         * save category id and name
         * and redirect to ProductListActivity.class
         */

        exp_category_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {



                String sel_sub_name = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                for(int i = 0;i < sub_cat_get_name.size();i++)
                {
                    String selected_name = sub_cat_get_name.get(i);
                    ////Log.d("selected_name",selected_name);

                    if(sel_sub_name.equalsIgnoreCase(selected_name)){

                        String selected_id = sub_cat_get_id.get(i);
                        String st_sel_name = sub_cat_get_name.get(i);
                        ////Log.d("selected_id",selected_id);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("selected_id",selected_id);
                        editor.putString("selected_name",st_sel_name);
                        editor.commit();

                        String value_email=pref.getString("login_email","");
                      //  if(value_email!=null && !value_email.equals("")) {
                            Intent intent = new Intent(getActivity(), ProductListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            CommonFun.finishscreen(getActivity());
//                        }
//                        else
//                        {
//
//                            Intent intent = new Intent(getActivity(), GuestProductListActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            startActivity(intent);
//                            CommonFun.finishscreen(getActivity());
//                        }
                    }
                }
                return false;
            }
        });


        // Listview Group expanded listener
        exp_category_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        exp_category_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();

        dbh=new DatabaseHandler(getActivity());

        total_head_not_active=0;

//        if(dbh.getCategoryListCount()>0)
//            callOffline();
//         else

//        pref = getActivity().CommonFun.getPreferences(getApplicationContext());
//          String st_categorydata=pref.getString("categorydata","");
//                if(!st_categorydata.equals("")  && (st_categorydata!=null)) {
//                    fillJSONDataToExpList(st_categorydata);
//                }
//        else {
                    /**
                     * call api to fetch all category
                     */
                    callJSONAPIVolley();
     //           }
    }


    /**
     * fill fetch data from api to expandable list
     * @param response
     */
    private void fillJSONDataToExpList(String response){

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
        } catch (JSONException e1) {
            // e1.printStackTrace();
            callJSONAPIVolley();
        }



        try {

            childrenData = jsonObj.getJSONArray("children_data");

        } catch (JSONException e1) {
            //e1.printStackTrace();
            callJSONAPIVolley();
        }

        if(total_head_not_active==0)
        {
            for (int i = 0; i < length_of_all_category; i++) {
                JSONObject c = null;
                try {
                    c = childrenData.getJSONObject(i);
                    String category_name=c.getString("name"); // Get category name
                    String is_active=c.getString("is_active");

                    if(is_active.equalsIgnoreCase("false"))
                        total_head_not_active++;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        //arr_category_name=new String[length_of_all_category-total_head_not_active];

        length_of_all_category=childrenData.length();
        arr_category_name=new String[length_of_all_category-total_head_not_active];
        arr_category_active=new String[length_of_all_category-total_head_not_active];

        arr_category_id=new String[length_of_all_category-total_head_not_active];

        int data_loop=-1;

        for (int i = 0; i < length_of_all_category; i++) {

            JSONObject c = null;
            try {
                c = childrenData.getJSONObject(i);
            } catch (JSONException e1) {
                // e1.printStackTrace();
                callJSONAPIVolley();
            }
            String category_name = null; // Get category name
            String category_id = null;
            String category_active = "";
            try {
                category_name = c.getString("name");
                category_id = c.getString("id");
                category_active = c.getString("is_active");
            } catch (JSONException e1) {
                // e1.printStackTrace();
                callJSONAPIVolley();
            }
            // arr_category_active[i] = category_active;


            /**
             * check if category is active,
             * only then store details in temp
             */
            if (!category_active.equalsIgnoreCase("false")) {

                try {
                    data_loop++;

                    arr_category_name[data_loop] = category_name;
                    arr_category_id[data_loop] = category_id;
                }
                catch (ArrayIndexOutOfBoundsException ex){
                    data_loop--;
                }


                ////Log.d("category", arr_category_name[data_loop] + " " + arr_category_id[data_loop]);
                try {
                    subChildrenData = c.getJSONArray("children_data");
                } catch (JSONException e1) {
                    //e1.printStackTrace();
                    callJSONAPIVolley();
                }


                int length_of_subcategory = subChildrenData.length();
                ////Log.d("lengthofsub", String.valueOf(length_of_subcategory));

                sub_cat_length_list.add(String.valueOf(length_of_subcategory));
                ////Log.d("category", sub_cat_length_list.get(data_loop));

                for (int j = 0; j < length_of_subcategory; j++) {


                    JSONObject j_sub_data = null;
                    try {
                        j_sub_data = subChildrenData.getJSONObject(j);
                    } catch (JSONException e1) {
                        // e1.printStackTrace();
                        callJSONAPIVolley();
                    }

                    try {
                        sub_category_name = j_sub_data.getString("name");
                    } catch (JSONException e1) {
                        //e1.printStackTrace();
                        callJSONAPIVolley();
                    }
                    try {
                        sub_category_id = j_sub_data.getString("id");
                    } catch (JSONException e1) {
                        //e1.printStackTrace();
                        callJSONAPIVolley();
                    }

                    ////Log.d("sub", sub_category_name);
                    ////Log.d("sub ID", sub_category_id);

                    String sub_cat_active = "";
                    try {
                        sub_cat_active = j_sub_data.getString("is_active");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!sub_cat_active.equalsIgnoreCase("false")) {
                        sub_cat_get_name.add(sub_category_name);
                        sub_cat_get_id.add(sub_category_id);
                        ////Log.d("category", sub_cat_get_name.get(j));
                        ////Log.d("category id", sub_cat_get_id.get(j));
                    }
                    //dbh.addCategoryList(new CategoryList(category_name,sub_category_name,sub_category_id));

                }
            }
        }

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListViewAdapter(getActivity(), listDataHeader, listDataChild);
        exp_category_list.setAdapter(listAdapter);
    }

    private void callJSONAPIVolley()
    {

        fromurl=com.galwaykart.essentialClass.Global_Settings.api_url+"index.php/rest/V1/categories";

        // pDialog = new TransparentProgressDialog(HomePage_ShopByCategoryTab.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        progress_bar_shopCategory.setVisibility(View.VISIBLE);

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, fromurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

//                        if(pDialog.isShowing())
//                            pDialog.dismiss();
                        progress_bar_shopCategory.setVisibility(View.GONE);


                        if(response!=null){


                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("categorydata",response);
                            editor.commit();
                            try {


                                JSONObject jsonObj = new JSONObject(response);
                                childrenData = jsonObj.getJSONArray("children_data");

                                length_of_all_category=childrenData.length();

                                for (int i = 0; i < length_of_all_category; i++) {
                                    JSONObject c = childrenData.getJSONObject(i);
                                    String category_name=c.getString("name"); // Get category name
                                    String is_active=c.getString("is_active");

                                    if(is_active.equalsIgnoreCase("false"))
                                        total_head_not_active++;
                                }

                                //length_of_all_active_category=length_of_all_category-total_head_not_active;

                                arr_category_name=new String[length_of_all_category-total_head_not_active];

                                //total_head_not_active=length_of_all_category;
                                int data_loop=-1;

                                for (int i = 0; i < length_of_all_category; i++) {


                                    JSONObject c = childrenData.getJSONObject(i);
                                    String category_name=c.getString("name"); // Get category name
                                    String is_active=c.getString("is_active");

//                                    if(is_active.equalsIgnoreCase("false"))
//                                        total_head_not_active++;

                                    if(!is_active.equalsIgnoreCase("false")) {

                                        data_loop++;

                                        try {


                                            arr_category_name[data_loop] = category_name;


                                            ////Log.d("category", arr_category_name[data_loop]);

                                            if (c.has("children_data")) {
                                                subChildrenData = c.getJSONArray("children_data");


                                                int length_of_subcategory = subChildrenData.length();
                                                //Log.d("lengthofsub", String.valueOf(length_of_subcategory));

                                                sub_cat_length_list.add(String.valueOf(length_of_subcategory));
                                                // ////Log.d("category", sub_cat_length_list.get(i));

                                                for (int j = 0; j < length_of_subcategory; j++) {


                                                    JSONObject j_sub_data = subChildrenData.getJSONObject(j);

                                                    String sub_category_active = j_sub_data.getString("is_active");

                                                    // if(!sub_category_active.equalsIgnoreCase("false")) {
                                                    sub_category_name = j_sub_data.getString("name");
                                                    sub_category_id = j_sub_data.getString("id");

                                                    ////Log.d("sub", sub_category_name);
                                                    ////Log.d("sub ID", sub_category_id);

                                                    sub_cat_get_name.add(sub_category_name);
                                                    sub_cat_get_id.add(sub_category_id);
                                                    ////Log.d("category", sub_cat_get_name.get(j));
                                                    ////Log.d("category id", sub_cat_get_id.get(j));
                                                    //    }
                                                    //dbh.addCategoryList(new CategoryList(category_name,sub_category_name,sub_category_id));
                                                }
                                            }

                                        }catch (IndexOutOfBoundsException ex){



                                        }


                                    }
                                }

                                fillJSONDataToExpList(response);




                            } catch (JSONException e) {
                                e.printStackTrace();
                                ////Log.d("error",e.toString());
//                                if(pDialog.isShowing())
//                                    pDialog.dismiss();
                                progress_bar_shopCategory.setVisibility(View.GONE);

                            }
                        }

                        // preparing list data
                        prepareListData();

                        listAdapter = new ExpandableListViewAdapter(getContext(), listDataHeader, listDataChild);
                        exp_category_list.setAdapter(listAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if(pDialog.isShowing())
//                    pDialog.dismiss();

                progress_bar_shopCategory.setVisibility(View.GONE);

                if(getContext()!=null)
                    CommonFun.showVolleyException(error,getContext());


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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }



    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        List<String> arrayList;
        int k=0;
        int length_of_subcat=0;

        for(int i = 0; i< arr_category_name.length; i++) {


            //   if (!arr_category_active[i].equalsIgnoreCase("false")) {
            listDataHeader.add(arr_category_name[i]);

            arrayList = new ArrayList<String>();

            if (i != 0)
                k = k + length_of_subcat;

            length_of_subcat = Integer.parseInt(sub_cat_length_list.get(i));

            for (int j = k; j < length_of_subcat + k; j++) {
                arrayList.add(sub_cat_get_name.get(j));
            }

            listDataChild.put(listDataHeader.get(i), arrayList);
            ////Log.d("arraylist", String.valueOf(arrayList));

//         }
//            else
//            {
//                //listDataHeader.add("");
//                if (i != 0)
//                    k = k + length_of_subcat;
//
//                length_of_subcat = Integer.parseInt(sub_cat_length_list.get(i));
//
//            }
        }
//        ////Log.d("listDataHeader",""+listDataHeader);
//        ////Log.d("listDataChild",""+listDataChild);
//        ////Log.d("listDataHeader",""+listDataHeader);
    }


    private void callOffline(){

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        List<String> arrayList;
        int k=0;
        int length_of_subcat=0;

        for(int i = 0; i< length_of_all_category; i++) {

            listDataHeader.add(arr_category_name[i]);



            arrayList=new ArrayList<String>();

            if(i!=0)
                k=k+length_of_subcat;

            length_of_subcat= Integer.parseInt(sub_cat_length_list.get(i));

            for(int j=k;j<length_of_subcat+k;j++) {
                arrayList.add(sub_cat_get_name.get(j));
            }

            listDataChild.put(listDataHeader.get(i), arrayList);
            ////Log.d("arraylist", String.valueOf(arrayList));

        }


        listAdapter = new ExpandableListViewAdapter(getActivity(), listDataHeader, listDataChild);
        exp_category_list.setAdapter(listAdapter);
    }

}
