package com.galwaykart.vendormanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galwaykart.HomePageActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.DetectInternet;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.testimonial.GlobalUtilityClass;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VendorRegistration extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private String st_token_data="";

    private EditText first_name_et, last_name_et, email_et, phone_no_et, otp_et, password_et, confirm_password_et, store_name_et,
            address_et, gst_no_et, pan_et, account_no_et, account_holder_name_et, ifcs_et, store_url_et;

    private TextView tv_send_otp;
    private Spinner sp_shipping_method;
    private ImageView ic_back, capture_image1, set_img1, capture_trademark_image1, set_trademark_img1;
    private CheckBox tnc_check_box;
    private LinearLayout term_a_condition_ly;
    private Button submit_btn;

    private String input_data1="";

    private String userChoosenTask;
    private File imageFile = null;
    private File imageFileTradeMark = null;

    private String image_base_64_str = "";
    private String image_base_64_trademark_str = "";

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int REQUEST_CAMERA_TRADEMARK = 2, SELECT_FILE_TRADEMARK = 3;


    private TransparentProgressDialog pDialog, pDialog1;
    private String stImageData= "";
    private String stTradeMarkImageData="";
    private String randon_no="236479500033588955";

    private  int  otp_random_no;
    private String st_get_otp_URL="";

    private String [] spinner_data = {"Select Shipping Method", "Self","By Glaze"};
    private ArrayAdapter<String> adapterShippingMethod;
    private String str_shipping_method_id="", str_shipping_method="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_registration);


        init();
    }


    void init() {

        pref = CommonFun.getPreferences(VendorRegistration.this);
        st_token_data=pref.getString("tokenData","");

        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(this);


        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        email_et = findViewById(R.id.email_et);
        phone_no_et = findViewById(R.id.phone_no_et);
        otp_et = findViewById(R.id.otp_et);


        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);
        store_name_et = findViewById(R.id.store_name_et);
        address_et = findViewById(R.id.address_et);
        gst_no_et = findViewById(R.id.gst_no_et);
        pan_et = findViewById(R.id.pan_et);
        account_no_et = findViewById(R.id.account_no_et);
        account_holder_name_et = findViewById(R.id.account_holder_name_et);
        ifcs_et = findViewById(R.id.ifcs_et);
        store_url_et= findViewById(R.id.ifcs_et);

        tv_send_otp= findViewById(R.id.tv_send_otp);
        tv_send_otp.setOnClickListener(this);

        tnc_check_box = findViewById(R.id.tnc_check_box);

        capture_image1 = findViewById(R.id.capture_image1);
        capture_image1.setOnClickListener(this);

        set_img1 = findViewById(R.id.set_img1);

        capture_trademark_image1 = findViewById(R.id.capture_trademark_image1);
        capture_trademark_image1.setOnClickListener(this);

        set_trademark_img1 = findViewById(R.id.set_trademark_img1);

        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);

        setDataSpinner();

    }

    private void setDataSpinner() {
        sp_shipping_method = findViewById(R.id.sp_shipping_method);
        adapterShippingMethod = new ArrayAdapter<String>(VendorRegistration.this,android.R.layout.simple_spinner_dropdown_item,
                spinner_data);
        sp_shipping_method.setAdapter(adapterShippingMethod);

        sp_shipping_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        str_shipping_method_id = "1";
                        str_shipping_method = "Select Shipping Method";
                        break;
                    case 1:
                        str_shipping_method_id = "2";
                        str_shipping_method = "Self";
                        break;
                    case 2:
                        str_shipping_method_id = "3";
                        str_shipping_method = "By Glaze";
                        break;
                    default:
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ic_back:
                finish();
                break;

            case R.id.tv_send_otp:
                if(onPhoneFieldValidation())
                {
                    sendOtpViaAPI();
                }

                break;

            case R.id.capture_image1:
                selectImage();
            break;

            case R.id.capture_trademark_image1:
                selectImageTrade();
                break;

            case R.id.submit_btn:

                if(DetectInternet.checkInternetConnection(VendorRegistration.this)) {

                    submitRegistration();

                  /*  if (onClikValidation()) {
                        submitRegistration();
                    }*/
                }
                else
                {
                    CommonFun.alertError(VendorRegistration.this,"Please check your internet connection");
                }
                break;

            }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                new CaptureImageAsync(bitmap).execute();
                storeImageFromTakePhoto(bitmap);

            }
           else if (requestCode == SELECT_FILE_TRADEMARK)
                onSelectFromGalleryResultTrade(data);
            else if (requestCode == REQUEST_CAMERA_TRADEMARK)
            {
                Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                new CaptureImageTradeMarkAsync(bitmap1).execute();
                storeImageFromTakePhotoTradeMark(bitmap1);

            }

        }

       /* if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 1:
                    onSelectFromGalleryResult(data);
                    break;
                case 2:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    new CaptureImageAsync(bitmap).execute();
                    storeImageFromTakePhoto(bitmap);
                    break;

            }
        }*/


    }

    //capture and save scam picture on local drice and also on server
    public class CaptureImageAsync extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        CaptureImageAsync(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (imageFile.exists()) {

            } else {
                Toast.makeText(VendorRegistration.this, "Please upload Images or Files", Toast.LENGTH_LONG).show();
            }
            //if only one image, set capture image here
            set_img1.setImageBitmap(bitmap);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture


    //capture and save scam picture on local drice and also on server
    public class CaptureImageTradeMarkAsync extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        CaptureImageTradeMarkAsync(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (imageFileTradeMark.exists()) {

            } else {
                Toast.makeText(VendorRegistration.this, "Please upload Images or Files", Toast.LENGTH_LONG).show();
            }
            //if only one image, set capture image here
            set_trademark_img1.setImageBitmap(bitmap);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(VendorRegistration.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void storeImageFromTakePhoto(Bitmap bitmap) {

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        path.mkdirs();
        Log.e("Image path from TakePho", path + "");
        imageFile = new File(path, "glkart" + new Date().getTime() + ".png");
        Log.e("Image File from TakePho", imageFile + "");

        //start convert path to base64 FINAL
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image_base_64_str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.e("encoded", image_base_64_str);

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
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        Log.e("dataee", data.toString() + "");
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //compaint_image_img1.setVisibility(View.VISIBLE);

        set_img1.setImageBitmap(bm);

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
            MediaScannerConnection.scanFile(this,
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


    //for TradeMark

    private void selectImageTrade() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = GlobalUtilityClass.checkStoragePermission(VendorRegistration.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntentTradeMark();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntentTradeMark();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntentTradeMark() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_TRADEMARK);
    }

    private void galleryIntentTradeMark() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_TRADEMARK);
    }

    private void storeImageFromTakePhotoTradeMark(Bitmap bitmap) {

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        path.mkdirs();
        Log.e("Image path from TakePho", path + "");
        imageFileTradeMark = new File(path, "glkart" + new Date().getTime() + ".png");
        Log.e("Image File from TakePho", imageFileTradeMark + "");

        //start convert path to base64 FINAL
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image_base_64_trademark_str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.e("encoded", image_base_64_trademark_str);

        //end convert path to base64

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFileTradeMark);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResultTrade(Intent data) {
        Bitmap bm = null;
        Log.e("dataee", data.toString() + "");
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                String path = getPathGalleryImage(bm);
                imageFileTradeMark = new File(path);
                Log.e("path of gallery", imageFileTradeMark.getAbsolutePath());
                Log.e("image path from gallery", path);

                //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                image_base_64_str = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //compaint_image_img1.setVisibility(View.VISIBLE);

        set_trademark_img1.setImageBitmap(bm);

    }

    public boolean onPhoneFieldValidation() {

        boolean isvalid=true;
        String phone_no = phone_no_et.getText().toString().trim();

        if(TextUtils.isEmpty(phone_no_et.getText().toString().trim())){
            phone_no_et.setError("Required Field");
            phone_no_et.setFocusable(true);
            return false;
        }

        if(phone_no_et.length()>10 || phone_no_et.length()<10)
        {
            phone_no_et.setError("Enter 10 digit no");
            phone_no_et.setFocusable(true);
            return false;
        }

       return isvalid;

    }

    public boolean onClikValidation() {

        boolean isvalid=true;
        String first_name = first_name_et.getText().toString().trim();
        String last_name = last_name_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String phone_no = phone_no_et.getText().toString().trim();
        String otp = otp_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirm_password = confirm_password_et.getText().toString().trim();
        String store_name = store_name_et.getText().toString().trim();
        String address = address_et.getText().toString().trim();
        String gst_no = gst_no_et.getText().toString().trim();
        String pan = pan_et.getText().toString().trim();
        String account_no = account_no_et.getText().toString().trim();
        String account_holder_name = account_holder_name_et.getText().toString().trim();
        String ifcs = ifcs_et.getText().toString().trim();

        Log.e("otp_string", otp);
        Log.e("randon_no_string", otp_random_no + "");

        if (TextUtils.isEmpty(first_name_et.getText().toString().trim())) {
            first_name_et.setError("Required Field");
            first_name_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(last_name_et.getText().toString().trim())) {
            last_name_et.setError("Required Field");
            last_name_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(email_et.getText().toString().trim())) {
            email_et.setError("Required Field");
            email_et.setFocusable(true);
            isvalid=false;
        }

        if(!GlobalUtilityClass.isEmailValid(email_et.getText().toString().trim())){
            email_et.setError("Email Id is Invalid");
            email_et.setFocusable(true);
            return false;
        }

        if(TextUtils.isEmpty(phone_no_et.getText().toString().trim())){
            phone_no_et.setError("Required Field");
            phone_no_et.setFocusable(true);
            return false;
        }

        if(phone_no_et.length()>10 || phone_no_et.length()<10)
        {
            phone_no_et.setError("Enter 10 digit no");
            phone_no_et.setFocusable(true);
            return false;
        }

        if(TextUtils.isEmpty(otp_et.getText().toString().trim())){
            otp_et.setError("OTP Required Field");
            otp_et.setFocusable(true);
            return false;
        }

        if(!otp.equalsIgnoreCase(String.valueOf(otp_random_no))){
            otp_et.setError("OTP Mismatch. Please fill correct OTP");
            otp_et.setFocusable(true);
            return false;
        }

        if (TextUtils.isEmpty(password_et.getText().toString().trim())) {
            password_et.setError("Required Field");
            password_et.setFocusable(true);
            isvalid=false;
        }

        if(!password.equals(confirm_password)) {
            Toast.makeText(VendorRegistration.this, "Password Not matching", Toast.LENGTH_SHORT).show();
            isvalid = false;
        }

        if (TextUtils.isEmpty(confirm_password_et.getText().toString().trim())) {
            confirm_password_et.setError("Required Field");
            confirm_password_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(store_name_et.getText().toString().trim())) {
            store_name_et.setError("Required Field");
            store_name_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(address_et.getText().toString().trim())) {
            address_et.setError("Required Field");
            address_et.setFocusable(true);
            isvalid=false;
        }


        if (TextUtils.isEmpty(gst_no_et.getText().toString().trim())) {
            gst_no_et.setError("Required Field");
            gst_no_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(pan_et.getText().toString().trim())) {
            pan_et.setError("Required Field");
            pan_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(account_no_et.getText().toString().trim())) {
            account_no_et.setError("Required Field");
            account_no_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(account_no_et.getText().toString().trim())) {
            account_no_et.setError("Required Field");
            account_no_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(account_holder_name_et.getText().toString().trim())) {
            account_holder_name_et.setError("Required Field");
            account_holder_name_et.setFocusable(true);
            isvalid=false;
        }

        if (TextUtils.isEmpty(ifcs_et.getText().toString().trim())) {
            ifcs_et.setError("Required Field");
            ifcs_et.setFocusable(true);
            isvalid=false;
        }


        if (str_shipping_method.equals("Select Shipping Method"))
        {
            Toast.makeText(VendorRegistration.this, "Please Select Shipment Method", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(set_img1.getDrawable() == null)                    {
            Toast.makeText(VendorRegistration.this, "Image not captured", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!tnc_check_box.isChecked())                    {
            Toast.makeText(VendorRegistration.this, "Terms and condition not checked", Toast.LENGTH_LONG).show();
            return false;
        }

        return isvalid;

    }

    private boolean inputImageData() {
        boolean can_process=false;
        stImageData = "";

        if(!image_base_64_str.equals("")) {
            if (stImageData.equalsIgnoreCase("")) {
                String image_file_name = randon_no + new Date().getTime() + "_1";
                String ext=".png";
                stImageData = "{\"name\":\"" + image_file_name + "\"," +
                        "\"ext\":\"" + ext + "\"," +
                        "\"image\":\"" + image_base_64_str.trim().replaceAll("\n", "") + "\"}";
                can_process=true;
            }
        }
        else
        {
            can_process=false;

        }

       Log.e("stImageData",stImageData);
        return can_process;
    }

    private boolean inputTradeMarkImageData() {
        boolean can_process_trade=false;
        stTradeMarkImageData = "";

        if(!image_base_64_trademark_str.equals("")) {
            if (stTradeMarkImageData.equalsIgnoreCase("")) {
                String image_file_name = randon_no + new Date().getTime() + "_1";
                String ext=".png";
                stTradeMarkImageData = "{\"name\":\"" + image_file_name + "\"," +
                        "\"ext\":\"" + ext + "\"," +
                        "\"image\":\"" + image_base_64_trademark_str.trim().replaceAll("\n", "") + "\"}";
                can_process_trade=true;
            }
        }
        else
        {
            can_process_trade=false;

        }


        Log.e("stTradeMarkImageData",stTradeMarkImageData);
        return can_process_trade;
    }


    private void submitRegistration()
    {
        String first_name = first_name_et.getText().toString().trim();
        String last_name = last_name_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String phone_no = phone_no_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirm_password = confirm_password_et.getText().toString().trim();
        String store_name = store_name_et.getText().toString().trim();
        String address = address_et.getText().toString().trim();
        String gst_no = gst_no_et.getText().toString().trim();
        String pan = pan_et.getText().toString().trim();
        String account_no = account_no_et.getText().toString().trim();
        String account_holder_name = account_holder_name_et.getText().toString().trim();
        String ifcs = ifcs_et.getText().toString().trim();
        String store_url = store_url_et.getText().toString().trim();
        String otp = otp_et.getText().toString().trim();

        SharedPreferences pref1 = CommonFun.getPreferences(VendorRegistration.this);
        String tokenData = pref1.getString("tokenData", "");

        String st_vendor_registration_url = Global_Settings.api_url + "rest/V1/customer/vendor_registration";

        input_data1 = "{\"data\":{\"fname\":\""+first_name+"\"," +
                "\"lname\":\""+last_name+"\"," +
                "\"email\":\""+email+"\"," +
                "\"password\":\""+password+"\"," +
                "\"business\":\""+store_name+"\"," +
                "\"address1\":\""+address+"\"," +
                "\"address2\":\""+address+"\"," +
                "\"mobile\":\""+phone_no+"\"," +
                "\"account_holder_name\":\""+account_holder_name+"\"," +
                "\"account_number\": \""+account_no+"\"," +
                "\"account_ifsc\":\""+ifcs+"\"," +
                "\"gst_number\":\""+gst_no+"\"," +
                "\"pan_number\":\""+pan+"\"," +
                "\"signature\":["+stImageData+"]," +
                //"\"signature\":[{\"name\":\"tester1\",\"ext\":\".png\",\"image\":\""+address+"\"}],"+
                "\"shop_url\":\""+store_url+"\"}}";


        Log.e("stImageData", stImageData);
        Log.e("stTradeMarkImageData", stTradeMarkImageData);

        Log.e("input_data", input_data1);
        Log.d("input_data", input_data1);

        Log.e("otp_s", otp);
        Log.e("randon_no_string_s", otp_random_no + "");


        if(inputImageData()) {
            //inputTradeMarkImageData();
            vendorRegistrationJson(st_vendor_registration_url);
        }
        else
        {
            CommonFun.alertError(VendorRegistration.this,"Signature is required");
        }
    }

    private void vendorRegistrationJson(String st_vendor_registration_url) {

            pDialog = new TransparentProgressDialog(VendorRegistration.this);
            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();

            try {
                RequestQueue requestQueue = Volley.newRequestQueue(VendorRegistration.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, st_vendor_registration_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                Log.e("VendorRegistration", response);
                                if (response != null) {
                                    try{
                                        JSONArray jsonArray = new JSONArray(response);
                                        for(int j = 0; j<jsonArray.length(); j++)
                                        {
                                            JSONObject jsonObj= jsonArray.getJSONObject(j);
                                            String status = jsonObj.getString("status");
                                            String message11 = jsonObj.getString("msg");
                                            if(status.equalsIgnoreCase("0"))
                                            {
                                                Snackbar.make(findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                Snackbar.make(findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();

                                                Intent intent = new Intent(VendorRegistration.this, HomePageActivity.class);
                                                startActivity(intent);

                                            }

                                        }

                                    }catch (Exception e)
                                    {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                        String err_msg="Somthing went worng";
                                    }
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        if (error instanceof ServerError) {
                            NetworkResponse response = error.networkResponse;
                            String errorMsg = "";
                            if (response != null && response.data != null) {
                                String errorString = new String(response.data);

                                try {
                                    JSONObject object = new JSONObject(errorString);
                                    String st_msg = object.getString("message");
                                    String st_code = object.getString("code");
                                    CommonFun.alertError(VendorRegistration.this, st_msg);
                                    //Log.d("st_code",st_code);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                    CommonFun.showVolleyException(error, VendorRegistration.this);
                                }


                            }
                        } else
                            CommonFun.showVolleyException(error, VendorRegistration.this);
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
                        return input_data1 == null ? null : input_data1.getBytes(StandardCharsets.UTF_8);
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


    private void sendOtpViaAPI() {

        Random rand = new Random();
        String phone_no = phone_no_et.getText().toString().trim();

        otp_random_no = rand.nextInt(9999) + 1000;
        Log.e("otp_random_no",""+otp_random_no);
        String st_text_msg = "Your verification code is "+otp_random_no+" sent on" +phone_no+ ". Please enter it and continue your registration."+"\n"+"Best regards-Galway";

        st_get_otp_URL= Global_Settings.otp_url+"?mobile="+phone_no+"&otp="+otp_random_no;
        //st_get_otp_URL= Global_Settings.otp_url+"?mobile=9958170517&otp="+otp_random_no;

        Log.e("st_get_otp_URL", st_get_otp_URL);

        //CommonFun.alertError(VendorRegistration.this,st_get_otp_URL);


        pDialog = new TransparentProgressDialog(VendorRegistration.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, st_get_otp_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing())
                            pDialog.dismiss();
                          Log.e("responseOTP",response);


                        if(response!=null){
                            try {


                            } catch (Exception e) {
                                e.printStackTrace();
                                //Log.d("error", e.toString());

                            }
                        }
                    }



                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog.isShowing())
                    pDialog.dismiss();

                CommonFun.showVolleyException(error, VendorRegistration.this);
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

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }
}