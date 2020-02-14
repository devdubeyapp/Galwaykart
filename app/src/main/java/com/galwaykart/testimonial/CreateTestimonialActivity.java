package com.galwaykart.testimonial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.galwaykart.SingleProductView.MainActivity;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateTestimonialActivity extends AppCompatActivity {

    SharedPreferences pref;
    private EditText title_et, description_et;
    private String name_str = "", email_str = "", phone_str = "", city_str = "", title_str = "", description_str = "";

    CircleImageView user_img_view;
    ImageView capture_image_view;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public File imageFile = null;
    Bitmap bitmapTakePhoto;

    String tokenData = "";
    String input_data = "";
    TransparentProgressDialog pDialog;
    String image_base_64_str = "";
    //String image_base_64_str = "iVBORw0KGgoAAAANSUhEUgAAABoAAAAPCAIAAAB1M0ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAxSURBVDhPY/hPVTBqHPlg1DgUsGLFitbWViiHECBsHNAsBgZiPTHUw44kMJiN+/8fAHwdhs0FH5VIAAAAAElFTkSuQmCC";
    Button submit_btn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_testimonial);
        Init();
    }

    void Init() {

        title_et = findViewById(R.id.title_et);
        description_et = findViewById(R.id.description_et);
        capture_image_view = findViewById(R.id.capture_image_view);
        user_img_view = findViewById(R.id.user_img_view);



        ImageView ic_back=findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        submit_btn = findViewById(R.id.submit_btn);
        capture_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = GlobalUtilityClass.checkStoragePermission(CreateTestimonialActivity.this);
                selectImage();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClikValidation())
                {
                    submitTestimonialJson();


                }
            }
        });

        /*pref = CommonFun.getPreferences(getApplicationContext());
        name_str = pref.getString("new_firstname", "")+" "+pref.getString("new_lastname", "");
        email_str = pref.getString("user_email", "");
        phone_str = pref.getString("new_telephone", "");
        city_str = pref.getString("new_city", "");

        name_et.setText(name_str);
        email_et.setText(email_str);
        phone_et.setText(phone_str);
        city_et.setText(city_str);*/


    }


    //capture and save scam picture on local drice and also on server
    private class TestimonialImageAsync extends AsyncTask<String, Void, String> {
        Bitmap bitmap;
        TestimonialImageAsync(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
        @Override
        protected String doInBackground(String... params) {
            storeTestimonialFromTakePhoto(bitmap);
            return null;

        }
        @Override
        protected void onPostExecute(String result) {

            if(imageFile.exists())
            {

            }
            else
            {
                Toast.makeText(CreateTestimonialActivity.this, "Image not uploaded", Toast.LENGTH_LONG).show();
            }
            //if only one image, set capture image here
            user_img_view.setVisibility(View.VISIBLE);
            user_img_view.setImageBitmap(bitmap);

        }
        @Override
        protected void onPreExecute() {
        }
    } //end capture  picture




    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateTestimonialActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = GlobalUtilityClass.checkStoragePermission(CreateTestimonialActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                       cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
                new TestimonialImageAsync(bitmap).execute();
                storeTestimonialFromTakePhoto(bitmap);

            }
        }

    }


    private void storeTestimonialFromTakePhoto(Bitmap bitmap) {

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        path.mkdirs();
        Log.e("Image path from TakePho",path + "");
        imageFile = new File(path, "glkart" + new Date().getTime() + ".png");
        Log.e("Image File from TakePho",imageFile + "");

        //start convert path to base64 FINAL
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
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

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),data.getData());
                String path = getPathGalleryImage(bm);
                imageFile = new File(path);
                Log.e("path of gallery", imageFile.getAbsolutePath());
                Log.e("image path from gallery", path);

              //start convert path to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                image_base_64_str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded base64 bm", image_base_64_str);
                //end convert path to base64

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user_img_view.setVisibility(View.VISIBLE);
        user_img_view.setImageBitmap(bm);

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

    Dialog dialog;
    private void submitTestimonialJson() {
//        name_str = "user Name";
//        email_str = "user Email";
//        phone_str = "user phone";
//        city_str = "user City";
        title_str = title_et.getText().toString();
        description_str = description_et.getText().toString();

        pref = CommonFun.getPreferences(getApplicationContext());
        tokenData = pref.getString("tokenData", "");

//        pref = CommonFun.getPreferences(getApplicationContext());
        name_str = pref.getString("login_fname", "")+" "+pref.getString("login_lname", "");
        email_str = pref.getString("login_email", "");
        phone_str = pref.getString("login_telephone", "");
        city_str = pref.getString("address_city", "");

        String st_testimonial_url = Global_Settings.api_url + "rest/V1/testimonials/create";
        //Log.d("Testimonial_url", st_testimonial_url);

        input_data = "{\"testimonial_data\":{\"image\":\"" + image_base_64_str.trim().replaceAll("\n", "") + "\",\"author\":\"" + name_str + "\"," +
                "\"city\":\"" + city_str + "\",\"email\":\"" + email_str + "\"," +
                "\"rating\":\"80\",\"mobile_number\":\"" + phone_str + "\"," +
                "\"title\":\"" + title_str + "\"," +
                "\"testimonial_content\":\"" + description_str + "\"}}";


        Log.e("image_base_64_str", image_base_64_str);

        Log.e("input_data", input_data);

        pDialog = new TransparentProgressDialog(CreateTestimonialActivity.this);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, st_testimonial_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            //Log.d("responsePutTestimonial", response);
                            if(response.equals("true"))
                            {
                                String err_msg="Submitted successfully";
//                                Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
//                                finish();

                                Vibrator vibrator = (Vibrator) CreateTestimonialActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                                vibrator.vibrate(100);

                                dialog = new Dialog(CreateTestimonialActivity.this);
                                dialog.setContentView(R.layout.custom_alert_dialog_design);
                                TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
                                tv_dialog.setText(err_msg);
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


                                        if(dialog.isShowing())
                                            dialog.dismiss();

                                        finish();
                                    }
                                }.start();




                            }
                            else
                            {
                                String err_msg="Something went wrong!! Please try again";
                                Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
                                CommonFun.alertError(CreateTestimonialActivity.this, st_msg);
                                //Log.d("st_code",st_code);
                            } catch (JSONException e) {
                                //e.printStackTrace();
                                CommonFun.showVolleyException(error, CreateTestimonialActivity.this);
                            }


                        }
                    } else
                        CommonFun.showVolleyException(error, CreateTestimonialActivity.this);
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
                    headers.put("Authorization", "Bearer " + tokenData);
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

    public boolean onClikValidation(){

        if(TextUtils.isEmpty(title_et.getText().toString().trim())){
            title_et.setError("Title can not empty");
            title_et.setFocusable(true);
            return false;
        }

        if(TextUtils.isEmpty(description_et.getText().toString().trim())){
            description_et.setError("Title can not empty");
            description_et.setFocusable(true);
            return false;
        }
        return true;

    }


}
