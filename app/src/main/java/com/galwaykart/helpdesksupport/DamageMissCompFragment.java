package com.galwaykart.helpdesksupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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
import com.galwaykart.essentialClass.DetectInternet;
import com.galwaykart.essentialClass.Global_Settings;
import com.galwaykart.helpdesksupport.mycomplaint.MyComplaints;
import com.galwaykart.helpdesksupport.orderdetails.CompOrderDetailsAdapter;
import com.galwaykart.helpdesksupport.orderdetails.ComplaintOrderDetailModel;
import com.galwaykart.essentialClass.TransparentProgressDialog;
import com.galwaykart.testimonial.GlobalUtilityClass;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.Context.MODE_PRIVATE;

public class DamageMissCompFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences pref;
    private String st_token_data="";
    private String deviceNumber = "7";
    private String requestTypeID = "7";





    LinearLayout ly_write;
    private RecyclerView items_recycler_view;
    private CompOrderDetailsAdapter compOrderDetailsAdapter;
    private TransparentProgressDialog pDialog, pDialog1;
    ArrayList<ComplaintOrderDetailModel> complaint_list;
    private String input_data = "";
    private Dialog dialog;

    private String qty_ordered="";
    int j;

    private String entity_id="";
    private String order_id="";
    private String str_complaint_category_id="";

    private File imageFile = null;
    private String image_base_64_str = "";


    private ImageView capture_image1, capture_image2, capture_image3, capture_image4, capture_image5, capture_image6;
    private TextView tv_compaint_submit;
    private EditText write_complaint_et;
    String write_complaint_str = "";
    private ImageView compaint_image_img1, compaint_image_img2, compaint_image_img3, compaint_image_img4, compaint_image_img5, compaint_image_img6;
    private RelativeLayout rl_image1, rl_image2, rl_image3, rl_image4, rl_image5, rl_image6;
    private String userChoosenTask;

    private String all_check="";

    private String image_base_64_str_1 = "",image_base_64_str_2 = "",
            image_base_64_str_3 = "",image_base_64_str_4 = "",image_base_64_str_5 = "",image_base_64_str_6 = "";

    private String stImageData= "";



//    static String st_pool_id="ap-south-1:89ffc0bb-91c0-485f-be38-ec511fdb2661";
//    static String bucketName="galwaykart-video-bucket";
    static String st_pool_id="ap-south-1:f791d48b-e8fe-4add-bede-d94e8efd5494";
    static String bucketName="galwaykart-helpdesk-videos";
    String imgfilename= "";

    private Uri fileUri;
    String filePath; //image
    public static final int MEDIA_TYPE_VIDEO = 1;
    private static final int CAMERA_Camera_CAPTURE_VIDEO_REQUEST_CODE = 400;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private String pathToStoredVideo="";
    String st_video_directory="galwaykart";
    ProgressBar progress_bar_video;
    ImageView iv_complaint_video_capture;
    ImageView iv_complaint_video;
    TextView tv_process_name;

   String finalVideoFileName="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        entity_id = getArguments().getString("entity_id");
        order_id = getArguments().getString("order_id");
        str_complaint_category_id = getArguments().getString("str_complaint_category_id");
        Log.e("str_category_id", str_complaint_category_id);


        View v =inflater.inflate(R.layout.fragment_damage_missing, container, false);

        ly_write = v.findViewById(R.id.ly_write);
        ly_write.setVisibility(View.GONE);



        imgfilename = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date());

        Log.d("awsdata",imgfilename);

        progress_bar_video=v.findViewById(R.id.progress_bar_video);
        progress_bar_video.setVisibility(View.GONE);

        tv_process_name=v.findViewById(R.id.tv_process_name);
        tv_process_name.setText("");
        tv_process_name.setVisibility(View.GONE);

        iv_complaint_video=v.findViewById(R.id.iv_complaint_video);
        iv_complaint_video.setVisibility(View.VISIBLE);

        iv_complaint_video_capture=v.findViewById(R.id.iv_complaint_video_capture);
        iv_complaint_video_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();

            }
        });



        pref = getActivity().getSharedPreferences("glazekartapp", MODE_PRIVATE);
        st_token_data=pref.getString("tokenData","");

        items_recycler_view= v.findViewById(R.id.items_recycler_view);
        items_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        items_recycler_view.setLayoutManager(mLayoutManager);

        write_complaint_et = v.findViewById(R.id.write_complaint_et);

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

        complaint_list = new ArrayList<>();

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

                Log.e("all_check",all_check);


                if(DetectInternet.checkInternetConnection(getActivity())) {
                    if (onClikValidation()) {
                        if (qtyValidation()) {

                            all_check = "";


                            if (inputImageData()) {
                                if (!finalVideoFileName.equals("")) {
                                    tv_compaint_submit.setText("Processing...");
                                    tv_compaint_submit.setEnabled(false);


                                    for (int i = 0; i < complaint_list.size(); i++) {
                                        ComplaintOrderDetailModel model = complaint_list.get(i);
                                        if (model.getCheck_for_return_req() == true) {

                                            finalVideoFileName = "https://galwaykart-helpdesk-videos.s3.ap-south-1.amazonaws.com/" + finalVideoFileName;

                                            if (all_check.equals("")) {
                                                all_check = "{\"qty\":" + model.getReturn_qty_req() +
                                                        ",\"resolution\":" + str_complaint_category_id +
                                                        ",\"order_qty\":" + model.getQty_ordered() +
                                                        ",\"reason\":" + str_complaint_category_id +
                                                        ",\"condition\":" + str_complaint_category_id +
                                                        ",\"videoUrl\":\"" + finalVideoFileName + "\"" +
                                                        ",\"product_name\":\"" + model.getProduct_name() + "\"" +
                                                        ",\"product_sku\":\"" + model.getSku() + "\"" +
                                                        ",\"orderItemId\":" + model.getQuote_item_id() +
                                                        "}";
                                            } else {
                                                all_check = all_check + ",{\"qty\":" + model.getReturn_qty_req() +
                                                        ",\"resolution\":" + str_complaint_category_id +
                                                        ",\"order_qty\":" + model.getQty_ordered() +
                                                        ",\"reason\":" + str_complaint_category_id +
                                                        ",\"videoUrl\":\"" + finalVideoFileName + "\"" +
                                                        ",\"condition\":" + str_complaint_category_id +
                                                        ",\"product_name\":\"" + model.getProduct_name() + "\"" +
                                                        ",\"product_sku\":\"" + model.getSku() + "\"" +
                                                        ",\"orderItemId\":" + model.getQuote_item_id() +
                                                        "}";


                                            }

                                        }
                                    }

                                    compressVideoAndUpload(finalVideoFileName);
                                } else
                                    CommonFun.alertError(getActivity(), "video attachment is required");
                            } else {
                                CommonFun.alertError(getActivity(), "atleast one attachment is required");
                            }


                        } else {
                            CommonFun.alertError(getActivity(), "Must enter quantity for checked item");
                        }

                    } else {
                        CommonFun.alertError(getActivity(), "Must enter your query");
                    }
                }
                else
                {
                    CommonFun.alertError(getActivity(),"Please check your internet connection");
                }
                break;

        }

    }

    //capture and save scam picture on local drice and also on server
    public class DamageMissQueriestAsync1 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync1(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 1);
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
            tv_compaint_submit.setVisibility(View.VISIBLE);
            rl_image2.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class DamageMissQueriestAsync2 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync2(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 2);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img2.setImageBitmap(bitmap);
           // capture_image2.setVisibility(View.GONE);
            rl_image3.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    //end capture  picture

    //capture and save scam picture on local drice and also on server
    public class DamageMissQueriestAsync3 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync3(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 3);
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
    public class DamageMissQueriestAsync4 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync4(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 4);
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
    public class DamageMissQueriestAsync5 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync5(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 5);
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
    public class DamageMissQueriestAsync6 extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        DamageMissQueriestAsync6(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... strings) {
            storeImageFromTakePhoto(bitmap, 6);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //if only one image, set capture image here
            compaint_image_img6.setImageBitmap(bitmap);
           // capture_image6.setVisibility(View.GONE);
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
                    onSelectFromGalleryResult2(data, 2);
                    break;
                case 12:
                    onSelectFromGalleryResult3(data, 3);
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
                    new DamageMissCompFragment.DamageMissQueriestAsync1(bitmap1).execute();
                    storeImageFromTakePhoto(bitmap1, 1);
                    break;
                case 17:
                    Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                    new DamageMissCompFragment.DamageMissQueriestAsync2(bitmap2).execute();
                    storeImageFromTakePhoto(bitmap2,2);
                    break;
                case 18:
                    Bitmap bitmap3 = (Bitmap) data.getExtras().get("data");
                    new DamageMissCompFragment.DamageMissQueriestAsync3(bitmap3).execute();
                    storeImageFromTakePhoto(bitmap3,3);
                    break;
                case 19:
                    Bitmap bitmap4 = (Bitmap) data.getExtras().get("data");
                    new DamageMissCompFragment.DamageMissQueriestAsync4(bitmap4).execute();
                    storeImageFromTakePhoto(bitmap4,4);
                    break;
                case 20:
                    Bitmap bitmap5 = (Bitmap) data.getExtras().get("data");
                    new DamageMissCompFragment.DamageMissQueriestAsync5(bitmap5).execute();
                    storeImageFromTakePhoto(bitmap5,5);
                    break;
                case 21:
                    Bitmap bitmap6 = (Bitmap) data.getExtras().get("data");
                    new DamageMissCompFragment.DamageMissQueriestAsync6(bitmap6).execute();
                    storeImageFromTakePhoto(bitmap6,6);
                    break;
                case CAMERA_CAPTURE_VIDEO_REQUEST_CODE:

                    if(data!=null) {
                        fileUri = data.getData();

                        pathToStoredVideo = getRealPathFromURIPath(fileUri, getActivity());
                        //Log.d("vid", "Recorded Video Path " + pathToStoredVideo);

                        pref = CommonFun.getPreferences(getActivity());
                        String dist_id=pref.getString("log_user_id", "").toLowerCase();
                        String videoFileName=dist_id+"_"+imgfilename+".mp4";


                        if (pathToStoredVideo != null && !pathToStoredVideo.equalsIgnoreCase("")) {
                            //String filePath = SiliCompressor.with(this).compressVideo(pathToStoredVideo, destinationDirectory);

                            finalVideoFileName=videoFileName;
                           // compressVideoAndUpload(videoFileName);

                            Bitmap bitmap_video= ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                            iv_complaint_video.setImageBitmap(bitmap_video);

                        } else {
                            Toast.makeText(getActivity(), "Please select video file from gallery", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    break;
            }
        }

        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE && resultCode==Activity.RESULT_OK) {

            if(data!=null) {
                fileUri = data.getData();

                pathToStoredVideo = getRealPathFromURIPath(fileUri, getActivity());
                //Log.d("vid", "Recorded Video Path " + pathToStoredVideo);
                String videoFileName=imgfilename+".mp4";

                if (pathToStoredVideo != null && !pathToStoredVideo.equalsIgnoreCase("")) {
                    //String filePath = SiliCompressor.with(this).compressVideo(pathToStoredVideo, destinationDirectory);

                    finalVideoFileName=videoFileName;
                    //compressVideoAndUpload(videoFileName);

                } else {
                    Toast.makeText(getActivity(), "Please select video file from gallery", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        else if (requestCode == CAMERA_Camera_CAPTURE_VIDEO_REQUEST_CODE && resultCode==Activity.RESULT_OK) {

            if(data!=null) {
                fileUri = data.getData();
                filePath=fileUri.getPath();
                pathToStoredVideo=filePath;
                //pathToStoredVideo = getRealPathFromURIPath(fileUri, this);
                Log.d("vid", "Recorded Video Path " + pathToStoredVideo);
                String videoFileName=imgfilename+".mp4";
                finalVideoFileName=videoFileName;
                //compressVideoAndUpload(videoFileName);

            }
        }


    }

    private void storeImageFromTakePhoto(Bitmap bitmap, int index) {

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + File.separator);
        path.mkdirs();
        Log.e("Image path from TakePho", path + "");
        imageFile = new File(path, "glkart" + new Date().getTime() + ".png");
        Log.e("Image File from TakePho", imageFile + "");

        //start convert path to base64 FINAL
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image_base_64_str_1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.e("encoded", image_base_64_str);

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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // compaint_image_img1.setVisibility(View.VISIBLE);

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
        tv_compaint_submit.setVisibility(View.VISIBLE);
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
                Log.e("encoded base64 bm", image_base_64_str);

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
                Log.e("encoded base64 bm", image_base_64_str);
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
        //compaint_image_img1.setVisibility(View.VISIBLE);
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



    public void jsonOrderDetails() {


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

                                            for(int j = 0; j<jsonArray.length(); j++) {
                                                JSONObject jsonObjFinal = jsonArray.getJSONObject(j);

                                                String order_id = jsonObjFinal.getString("order_id");
                                                String product_id = jsonObjFinal.getString("product_id");
                                                String sku = jsonObjFinal.getString("sku");
                                                String product_type= jsonObjFinal.getString("product_type");
                                                String product_name= jsonObjFinal.getString("name");
                                                String product_price= jsonObjFinal.getString("original_price");
                                                qty_ordered= jsonObjFinal.getString("qty_ordered");
                                                String order_date_time= jsonObjFinal.getString("updated_at");
                                                String image = jsonObjFinal.getString("image");

//                                                Log.e("order_id",order_id);
//                                                Log.e("product_id",product_id);
//                                                Log.e("sku",sku);
//                                                Log.e("product_type",product_type);
//                                                Log.e("product_name",product_name);
//                                                Log.e("product_price",product_price);
//                                                Log.e("qty_ordered_res",qty_ordered);
//                                                Log.e("order_date_time",order_date_time);
//                                                Log.e("image",image);

                                                ComplaintOrderDetailModel complaintModel= new ComplaintOrderDetailModel();
                                                complaintModel.setOrder_id(order_id);
                                                complaintModel.setProduct_id(product_id);
                                                complaintModel.setSku(sku);
                                                complaintModel.setProduct_type(product_type);
                                                complaintModel.setProduct_name(product_name);
                                                complaintModel.setProduct_price(product_price);
                                                complaintModel.setQty_ordered(qty_ordered);
                                                complaintModel.setCheck_for_return_req(false);
                                                complaintModel.setOrder_date_time(order_date_time);
                                                complaintModel.setQuote_item_id(jsonObjFinal.getString("item_id"));
                                                complaintModel.setImage(image);

                                                complaint_list.add(complaintModel);

                                            }

                                            compOrderDetailsAdapter = new CompOrderDetailsAdapter(getActivity(), complaint_list);
                                            items_recycler_view.setAdapter(compOrderDetailsAdapter);


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

    private boolean inputImageData() {
        boolean can_process=false;
        stImageData = "";

        if(!image_base_64_str_1.equals("")) {
            if (stImageData.equalsIgnoreCase("")) {
                String image_file_name = entity_id + new Date().getTime() + "_1";

                //image_file_name="whatsapp-logo";
                String ext=".jpg";

                stImageData = "{\"name\":\"" + image_file_name + "\"," +
                        "\"ext\":\"" + ext + "\"," +
                        "\"image\":\"" + image_base_64_str_1.trim().replaceAll("\n", "") + "\"}";
                can_process=true;
            } else {
                if (!compaint_image_img2.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_2";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_2.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img3.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_3";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_3.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img4.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_4";
                    stImageData = stImageData + "," + "{\"name\":\"" + image_file_name + "\"," +
                            "\"ext\":\"" + ".png" + "\"," +
                            "\"image\":\"" + image_base_64_str_4.trim().replaceAll("\n", "") + "\"}";
                } else if (!compaint_image_img5.equals("")) {
                    String image_file_name = entity_id + new Date().getTime() + "_5";
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
        else
        {
            can_process=false;

        }


        Log.e("stImageData",stImageData);
        return can_process;
    }


    private void submitComplaintJson() {

       if(inputImageData()) {
           String str_write_complaint = write_complaint_et.getText().toString();

           SharedPreferences pref1 = getActivity().getSharedPreferences("glazekartapp", MODE_PRIVATE);
           String tokenData = pref1.getString("tokenData", "");

           String str_first_name = pref.getString("login_fname", "");
           String str_last_name = pref1.getString("login_lname", "");
           String str_customer_id = pref1.getString("login_id", "");

           String st_submit_complaint_url = Global_Settings.api_url + "rest/V1/m-help-requestsubmit";
           //Log.d("st_submit_complaint_url", st_submit_complaint_url);


           input_data= "{\"storeId\":1," +
                       "\"orderEntityId\":" + entity_id + "," +
                       "\"customerFirstName\":\"" + str_first_name + "\"," +
                       "\"customerlastName\":\"" + str_last_name + "\"," +
                       "\"videoUrl\":\"" + finalVideoFileName + "\"," +
                       "\"productData\":[" + all_check + "]," +
                       "\"customerId\":" + str_customer_id + "," +
                       "\"comment\":\"" + str_write_complaint + "\"" +
                       ",\"sourceId\":\"" + deviceNumber + "\"" +
                       ",\"requestTypeId\":\"" + str_complaint_category_id + "\"" +
                       ",\"imageData\":[" + stImageData + "]}";


           Log.d("input_data",input_data);
           callSubmitAPI(st_submit_complaint_url);


       }
       else
       {
           CommonFun.alertError(getActivity(),"atleast one attachment is required");
       }


    }

    private void callSubmitAPI(String st_submit_complaint_url) {
        Log.e("input_data", input_data);

        pDialog1 = new TransparentProgressDialog(getActivity());
        pDialog1.setCancelable(false);
        pDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObj = jsonArray.getJSONObject(j);
                                        String respo_status = jsonObj.getString("status");
                                        String message11 = jsonObj.getString("message");
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();

                                        if (respo_status.equals("1")) {
                                            //Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();

                                            Toast.makeText(getActivity(), message11, Toast.LENGTH_LONG).show();
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
                                                    Intent intent = new Intent(getActivity(), MyComplaints.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            }.start();

                                        } else {
                                            tv_compaint_submit.setText("Upload and Submit");
                                            tv_compaint_submit.setEnabled(true);
                                            //String err_msg = "Something went wrong!! Please try again";
                                           // Snackbar.make(getActivity().findViewById(android.R.id.content), message11, Snackbar.LENGTH_LONG).show();
                                            CommonFun.alertError(getActivity(),message11);
                                        }
                                    }


                                } catch (Exception e) {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    tv_compaint_submit.setText("Upload and Submit");
                                    tv_compaint_submit.setEnabled(true);
                                    String err_msg = "currently, there is no help available";
                                }
                            } else {
                                String err_msg = "Something went wrong!! Please try again";
                                tv_compaint_submit.setText("Upload and Submit");
                                tv_compaint_submit.setEnabled(true);
                                Snackbar.make(getActivity().findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_LONG).show();
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
                                //Log.d("st_code", st_code);
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


    public boolean onClikValidation() {

        boolean isvalid=true;
        write_complaint_str = write_complaint_et.getText().toString().trim();


        if (TextUtils.isEmpty(write_complaint_et.getText().toString().trim())) {
            write_complaint_et.setError("Write your queries here");
            write_complaint_et.setFocusable(true);
            isvalid=false;
        }
        return isvalid;
    }

    public boolean qtyValidation(){
        boolean isvalid=true;

        int total_checked_items=0;

        for(int i=0;i<complaint_list.size();i++){
            ComplaintOrderDetailModel model=complaint_list.get(i);
            //Log.d("returnqty", String.valueOf(model.getCheck_for_return_req()));
            if(model.getCheck_for_return_req()==true){

                total_checked_items++;
                if(model.getReturn_qty_req().equalsIgnoreCase("qty")){
                    isvalid=false;
                }


//                if(all_check.equals("")){
//                    all_check="{\"qty\":" +model.getReturn_qty_req()+
//                            ",\"resolution\":" + str_complaint_category_id+
//                            ",\"reason\":" + str_complaint_category_id+
//                            ",\"condition\":" +str_complaint_category_id+
//                            ",\"orderItemId\":" +model.getProduct_id()+
//                            "}";
//                }
//                else
//                {
//                    all_check=all_check+",{\"qty\":" +model.getReturn_qty_req()+
//                            ",\"resolution\":" + str_complaint_category_id+
//                            ",\"reason\":" + str_complaint_category_id+
//                            ",\"condition\":" +str_complaint_category_id+
//                            ",\"orderItemId\":" +model.getProduct_id()+
//                            "}";
//
//                    Log.e("reture_qty", model.getReturn_qty_req());
//                }

            }
        }

        if(total_checked_items==0)
            isvalid=false;

        Log.e("all_check",all_check);
        return isvalid;
    }




    private void captureImage() {

        final CharSequence[] options = {"Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if(options[item].equals("Camera"))
                    callVideoCaptureAction();
                else if(options[item].equals("Choose from Gallery"))
                    callVideoPickAction();

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();


    }

    private void callVideoPickAction() {
        try {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select a Video "), CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
        }
        catch (Exception e){

            Log.d("Error",e.toString());
        }

    }

    private void callVideoCaptureAction(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_Camera_CAPTURE_VIDEO_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private  File getOutputMediaFile(int type) {

        /**
         *  External sdcard location
         */
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                st_video_directory
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }


        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + imgfilename + ".mp4");


        } else {
            return null;
        }

        return mediaFile;
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable("file_uri", fileUri);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }


//    @Override
//    public void onActivity(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(savedInstanceState!=null)
//        {
//            fileUri = savedInstanceState.getParcelable("file_uri");
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("file_uri", fileUri);
//    }



    private void compressVideoAndUpload(String videoFileName)
    {
        try {
            // filePath = SiliCompressor.with(getActivity()).compressVideo(fileUri, getFileDestinationPath());

            GiraffeCompressor.init(getActivity());

            tv_process_name.setText("Please wait....The video is processing!");
            tv_process_name.setVisibility(View.VISIBLE);
            GiraffeCompressor.create() //two implementations: mediacodec and ffmpeg,default is mediacodec
                    .input(pathToStoredVideo) //set video to be compressed
                    .output(getFileDestinationPath()+"/"+videoFileName) //set compressed video output
                    .bitRate(2573600)
                    .resizeFactor(1.0f)
                    .ready()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GiraffeCompressor.Result>() {
                        @Override
                        public void onCompleted() {

                        }
                        @Override
                        public void onError(Throwable e) {

                            tv_process_name.setText("");
                            tv_process_name.setVisibility(View.GONE);

                            e.printStackTrace();

                        }

                        @Override
                        public void onNext(GiraffeCompressor.Result s) {

                            tv_process_name.setText("");
                            tv_process_name.setVisibility(View.GONE);

                            Log.d("File_Path_1",getFileDestinationPath()+"/"+finalVideoFileName);
                            File f_upload=new File(getFileDestinationPath()+"/"+finalVideoFileName);


                            //File f_upload=new File(pathToStoredVideo);
                            int video_size = 50;

                            // Get length of file in bytes
                            long fileSizeInBytes = f_upload.length();
                            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                            long fileSizeInKB = fileSizeInBytes / 1024;
                            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                            long fileSizeInMB = fileSizeInKB / 1024;


                            progress_bar_video.setMax((int) fileSizeInBytes);

                            if (fileSizeInMB > video_size)
                            {
                                tv_compaint_submit.setText("Upload and Submit");
                                tv_compaint_submit.setEnabled(true);
                                Toast.makeText(getActivity(), "file max size is " + video_size + " mb", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                tv_compaint_submit.setText("Uploading...");
                                tv_compaint_submit.setEnabled(false);
                                uploadtos3(getActivity(),f_upload);
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            tv_compaint_submit.setText("Upload and Submit");
            tv_compaint_submit.setEnabled(true);
        }

    }


    String compressFileUpload="";

    private String getFileDestinationPath(){
        //String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");

        if(!directoryFolder.exists()){
            directoryFolder.mkdir();
        }
        Log.d("File_Path", "Full path " + filePathEnvironment + "/video");
        compressFileUpload=filePathEnvironment + "/video" ;
        return compressFileUpload;
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentURI, projection,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void uploadtos3 (final Context context, final File file) {

        progress_bar_video.setVisibility(View.VISIBLE);

        tv_process_name.setText("Uploading...");
        tv_process_name.setVisibility(View.VISIBLE);

        if(file !=null){
//            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                    context,
//                    st_pool_id, // Identity pool ID
//                    Regions.AP_SOUTH_1 // Region
//            );

            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    st_pool_id, // Identity pool ID
                    Regions.AP_SOUTH_1 // Region
            );

            AmazonS3 s3 = new AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_SOUTH_1));

            TransferNetworkLossHandler.getInstance(context);

            TransferUtility transferUtility = new TransferUtility(s3, context);
            final TransferObserver observer = transferUtility.upload(
                    bucketName,
                    file.getName(),
                    file,
                    CannedAccessControlList.BucketOwnerFullControl //to make the file public
            );

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state.equals(TransferState.COMPLETED)) {
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        Log.d("aws","success");
                        tv_process_name.setText("");
                        tv_process_name.setVisibility(View.GONE);
                        progress_bar_video.setVisibility(View.GONE);

                        tv_compaint_submit.setText("Submitting Data...");
                        tv_compaint_submit.setEnabled(false);

                        submitComplaintJson();

                    } else if (state.equals(TransferState.FAILED)) {

                        tv_compaint_submit.setText("Upload and Submit");
                        tv_compaint_submit.setEnabled(true);
                        Toast.makeText(context,"Failed to upload",Toast.LENGTH_LONG).show();
                        Log.d("aws","failed");
                        tv_process_name.setText("");
                        tv_process_name.setVisibility(View.GONE);
                        progress_bar_video.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                    progress_bar_video.setMax((int) bytesTotal);
                    progress_bar_video.setProgress((int) bytesCurrent);
                    //Log.d("progress", String.valueOf((bytesCurrent/bytesTotal)*100)+'%');
                    Log.d("progress", ((bytesTotal - bytesCurrent) / 2048) / 1024 +"%");

                }

                @Override
                public void onError(int id, Exception ex) {
                    tv_process_name.setText("");
                    tv_process_name.setVisibility(View.GONE);
                    Log.d("aws",ex.getMessage());
                    tv_compaint_submit.setText("Upload and Submit");
                    tv_compaint_submit.setEnabled(true);
                }
            });
        }
    }




}
