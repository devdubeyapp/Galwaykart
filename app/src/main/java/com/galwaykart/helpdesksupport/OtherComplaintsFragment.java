package com.galwaykart.helpdesksupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.helpdesksupport.orderdetails.CompOrderDetailsAdapter;
import com.galwaykart.helpdesksupport.orderdetails.ComplaintOrderDetailModel;
import com.galwaykart.testimonial.CreateTestimonialActivity;
import com.galwaykart.testimonial.GlobalUtilityClass;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class OtherComplaintsFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences pref;
    private String st_token_data="";
    private String deviceNumber = "7";

    LinearLayout ly_write;
    private RecyclerView items_recycler_view;
    private CompOrderDetailsAdapter compOrderDetailsAdapter;
    private TransparentProgressDialog pDialog, pDialog1;
    private String qty_ordered="";

    private String input_data = "";
    private Dialog dialog;



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


    private ImageView capture_image1, capture_image2, capture_image3, capture_image4, capture_image5, capture_image6;
    private TextView tv_compaint_submit;
    private EditText write_complaint_et;
    String write_complaint_str = "";
    private ImageView compaint_image_img1, compaint_image_img2, compaint_image_img3, compaint_image_img4, compaint_image_img5, compaint_image_img6;
    private RelativeLayout rl_image1, rl_image2, rl_image3, rl_image4, rl_image5, rl_image6;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, REQUEST_CAMERA1 = 1;
    private int SELECT_FILE = 1, SELECT_FILE2 = 2;
    private File imageFile = null;

    private String image_base_64_str_1 = "",image_base_64_str_2 = "",
            image_base_64_str_3 = "",image_base_64_str_4 = "",image_base_64_str_5 = "",image_base_64_str_6 = "";

    private Button compaint_submit_btn;

    private String entity_id = "";
    private String order_id = "";
    private String str_complaint_category_id = "";

    Handler mHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_other_complaints, container, false);

        entity_id = getArguments().getString("entity_id");
        order_id = getArguments().getString("order_id");
        str_complaint_category_id = getArguments().getString("str_complaint_category_id");

        pref = getActivity().getSharedPreferences("glazekartapp", MODE_PRIVATE);
        st_token_data=pref.getString("tokenData","");

        write_complaint_et = v.findViewById(R.id.write_complaint_et);

        ly_write = v.findViewById(R.id.ly_write);
        ly_write.setVisibility(View.GONE);

        capture_image1 = v.findViewById(R.id.capture_image1);
        capture_image2 = v.findViewById(R.id.capture_image2);
        capture_image3 = v.findViewById(R.id.capture_image3);
        capture_image4 = v.findViewById(R.id.capture_image4);
        capture_image5 = v.findViewById(R.id.capture_image5);
        capture_image6 = v.findViewById(R.id.capture_image6);

        compaint_image_img1 = v.findViewById(R.id.compaint_image_img1);
        compaint_image_img2 = v.findViewById(R.id.compaint_image_img2);
        compaint_image_img3 = v.findViewById(R.id.compaint_image_img3);
        compaint_image_img4 = v.findViewById(R.id.compaint_image_img4);
        compaint_image_img5 = v.findViewById(R.id.compaint_image_img5);
        compaint_image_img6 = v.findViewById(R.id.compaint_image_img6);


        rl_image1 = v.findViewById(R.id.rl_image1);
        rl_image2 = v.findViewById(R.id.rl_image2);
        rl_image3 = v.findViewById(R.id.rl_image3);
        rl_image4 = v.findViewById(R.id.rl_image4);
        rl_image5 = v.findViewById(R.id.rl_image5);
        rl_image6 = v.findViewById(R.id.rl_image6);


        tv_compaint_submit = v.findViewById(R.id.tv_compaint_submit);


        Init();

        return v;
    }

    void Init() {




        str_qty_array = new ArrayList<String>();
        str_resolution_array = new ArrayList<String>();
        str_reason_array = new ArrayList<String>();
        str_condition_array = new ArrayList<String>();
        str_item_id_array = new ArrayList<String>();
        str_pname_array= new ArrayList<String>();
        str_psku_array= new ArrayList<String>();

        rl_image2.setVisibility(View.GONE);
        rl_image3.setVisibility(View.GONE);
        rl_image4.setVisibility(View.GONE);
        rl_image5.setVisibility(View.GONE);
        rl_image6.setVisibility(View.GONE);

        capture_image1.setOnClickListener(this);
        capture_image2.setOnClickListener(this);
        capture_image3.setOnClickListener(this);
        capture_image4.setOnClickListener(this);
        capture_image5.setOnClickListener(this);
        capture_image6.setOnClickListener(this);

        tv_compaint_submit.setOnClickListener(this);

        jsonOrderDetails();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_image1:
                selectImage1();
                break;

            case R.id.capture_image2:
                selectImage2();
                break;

            case R.id.capture_image3:
                selectImage3();
                break;

            case R.id.capture_image4:
                selectImage4();
                break;

            case R.id.capture_image5:
                selectImage5();
                break;

            case R.id.capture_image6:
                selectImage6();
                break;


            case R.id.tv_compaint_submit:

                if(OnClickValidation())
                {
                    submitComplaintJson();
                }



                break;

        }

    }

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync1 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync1(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,1);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (imageFile.exists()) {

            } else {
                Toast.makeText(getActivity(), "Please upload Images or Files", Toast.LENGTH_LONG).show();
            }
            //if only one image, set capture image here
            compaint_image_img1.setImageBitmap(bitmap);
            //capture_image1.setVisibility(View.GONE);
            rl_image2.setVisibility(View.VISIBLE);
            tv_compaint_submit.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync2 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync2(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,2);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img2.setImageBitmap(bitmap);
            //capture_image2.setVisibility(View.GONE);
            rl_image3.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync3 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync3(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,3);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img3.setImageBitmap(bitmap);
            //capture_image3.setVisibility(View.GONE);
            rl_image4.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync4 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync4(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,4);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img4.setImageBitmap(bitmap);
            //capture_image4.setVisibility(View.GONE);
            rl_image5.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync5 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync5(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,5);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img5.setImageBitmap(bitmap);
            //capture_image5.setVisibility(View.GONE);
            rl_image6.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class OtherComplaintAsync6 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        OtherComplaintAsync6(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap,6);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img6.setImageBitmap(bitmap);
            //capture_image6.setVisibility(View.GONE);
            //end set capture image here

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture


    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent1();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent1();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage2() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent2();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent2();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage3() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent3();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent3();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage4() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent4();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent4();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage5() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent5();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent5();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage6() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent6();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent6();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 10);

    }

    private void galleryIntent2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 11);

    }

    private void galleryIntent3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 12);

    }

    private void galleryIntent4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 13);

    }

    private void galleryIntent5() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 14);

    }

    private void galleryIntent6() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 15);

    }

    private void cameraIntent1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 16);
    }

    private void cameraIntent2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 17);
    }

    private void cameraIntent3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 18);
    }

    private void cameraIntent4() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 19);
    }

    private void cameraIntent5() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 20);
    }

    private void cameraIntent6() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 21);
    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 10:
                    onSelectFromGalleryResult1(data, 1);
                    break;
                case 11:
                    onSelectFromGalleryResult2(data,2);
                    break;
                case 12:
                    onSelectFromGalleryResult3(data,3);
                    break;
                case 13:
                    onSelectFromGalleryResult4(data, 4);
                    break;
                case 14:
                    onSelectFromGalleryResult5(data, 5);
                    break;
                case 15:
                    onSelectFromGalleryResult6(data, 6);
                    break;
                case 16:
                    Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync1(bitmap1).execute();
                    storeImageFromTakePhoto(bitmap1,1);
                    break;
                case 17:
                    Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync2(bitmap2).execute();
                    storeImageFromTakePhoto(bitmap2,2);
                    break;
                case 18:
                    Bitmap bitmap3 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync3(bitmap3).execute();
                    storeImageFromTakePhoto(bitmap3,3);
                    break;
                case 19:
                    Bitmap bitmap4 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync4(bitmap4).execute();
                    storeImageFromTakePhoto(bitmap4,4);
                    break;
                case 20:
                    Bitmap bitmap5 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync5(bitmap5).execute();
                    storeImageFromTakePhoto(bitmap5,5);
                    break;
                case 21:
                    Bitmap bitmap6 = (Bitmap) data.getExtras().get("data");
                    new OtherComplaintsFragment.OtherComplaintAsync6(bitmap6).execute();
                    storeImageFromTakePhoto(bitmap6,6);
                    break;


            }

        }
    }

    private void storeImageFromTakePhoto(Bitmap bitmap,int index) {

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        path.mkdirs();
        Log.e("Image path from TakePho", path + "");
        imageFile = new File(path, "glkart" + new Date().getTime() + ".png");
        Log.e("Image File from TakePho", imageFile + "");

        //start convert path to base64 FINAL
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        switch (index) {
            case 1:
                image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
            case 2:
                image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
            case 3:
                image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
            case 4:
                image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
            case 5:
                image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
            case 6:
                image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                break;
        }


        //end convert path to base64

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult1(Intent data, int index) {
        Bitmap bm = null;
        Log.e("dataee", data.toString() + "");
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                //inputImageData(image_base_64_str_1);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }



                Log.e("encoded base64 bm", image_base_64_str_1);
                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //compaint_image_img1.setVisibility(View.VISIBLE);

        compaint_image_img1.setImageBitmap(bm);
        //capture_image1.setVisibility(View.GONE);
        tv_compaint_submit.setVisibility(View.VISIBLE);
        rl_image2.setVisibility(View.VISIBLE);

    }

    private void onSelectFromGalleryResult2(Intent data, int index) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded base64 bm", image_base_64_str_2);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }

                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);
        compaint_image_img2.setImageBitmap(bm);
        //capture_image2.setVisibility(View.GONE);
        rl_image3.setVisibility(View.VISIBLE);

    }

    private void onSelectFromGalleryResult3(Intent data, int index) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }


                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);
        compaint_image_img3.setImageBitmap(bm);
        //capture_image3.setVisibility(View.GONE);
        rl_image4.setVisibility(View.VISIBLE);

    }

    private void onSelectFromGalleryResult4(Intent data, int index) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded base64 bm", image_base_64_str_4);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }
                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);
        compaint_image_img4.setImageBitmap(bm);
        //capture_image4.setVisibility(View.GONE);
        rl_image5.setVisibility(View.VISIBLE);

    }

    private void onSelectFromGalleryResult5(Intent data, int index) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded base64 bm", image_base_64_str_5);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }
                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);
        compaint_image_img5.setImageBitmap(bm);
        //capture_image5.setVisibility(View.GONE);
        rl_image6.setVisibility(View.VISIBLE);

    }

    private void onSelectFromGalleryResult6(Intent data, int index) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded base64 bm", image_base_64_str_6);

                switch (index) {
                    case 1:
                        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 2:
                        image_base_64_str_2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 3:
                        image_base_64_str_3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 4:
                        image_base_64_str_4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 5:
                        image_base_64_str_5 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                    case 6:
                        image_base_64_str_6 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        break;
                }


                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);
        compaint_image_img6.setImageBitmap(bm);
        //capture_image6.setVisibility(View.GONE);

    }

    public String getPathGalleryImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            //Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public boolean OnClickValidation() {

        write_complaint_str = write_complaint_et.getText().toString().trim();
        if (TextUtils.isEmpty(write_complaint_et.getText().toString().trim())) {
            write_complaint_et.setError("Write your queries here");
            write_complaint_et.setFocusable(true);
            return false;
        }
        return true;
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


    /**
     *
     * Author:
     */
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
    public void inputImageData() {
        stImageData = "";

        if(!image_base_64_str_1.equals("")) {
            if (stImageData.equalsIgnoreCase("")) {


                String image_file_name = entity_id + new Date().getTime() + "_1.png";

                stImageData = "{\"name\":\"" + image_file_name + "\"," +
                        "\"ext\":\"" + ".png" + "\"," +
                        "\"image\":\"" + image_base_64_str_1.trim().replaceAll("\n", "") + "\"}";
            } else {
                if (!compaint_image_img2.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_2.png";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_2.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img3.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_3.png";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_3.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img4.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_4.png";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_4.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img5.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_5.png";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_5.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img6.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_5.png";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_6.trim().replaceAll("\n", "") + "\"}";
                }
            }
        }

        Log.e("stImageData",stImageData);
    }


    private void submitComplaintJson() {


        inputImageData();

        String str_write_complaint = write_complaint_et.getText().toString();

        SharedPreferences pref1 = getActivity().getSharedPreferences("glazekartapp", MODE_PRIVATE);
        String tokenData = pref1.getString("tokenData", "");

        String str_first_name = pref.getString("login_fname", "");
        String str_last_name = pref1.getString("login_lname", "");
        String str_customer_id = pref1.getString("login_id", "");
        //login_group_id, login_fname, login_lname

        String st_submit_complaint_url = Global_Settings.api_url + "rest/V1/m-help-requestsubmit";
        //Log.d("st_submit_complaint_url", st_submit_complaint_url);

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
                ",\"imageData\":["+stImageData+"]}";



        Log.e("input_data", input_data);

        pDialog1 = new TransparentProgressDialog(getActivity());
        pDialog1.setCancelable(false);
        pDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog1.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_submit_complaint_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog1.isShowing())
                                pDialog1.dismiss();
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
                                            // Toast.makeText(getActivity(), message11, Toast.LENGTH_LONG).show();
                                            //Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();

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
                    if (pDialog1.isShowing())
                        pDialog1.dismiss();

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

}

