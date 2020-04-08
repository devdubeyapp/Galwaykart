package com.galwaykart.Payment;

//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.galwaykart.R;
//import com.galwaykart.essentialClass.CommonFun;
//import com.galwaykart.essentialClass.Global_Settings;
//import com.galwaykart.essentialClass.TransparentProgressDialog;
//import com.galwaykart.profile.OrderListActivity;
//import com.payu.india.Extras.PayUChecksum;
//import com.payu.india.Extras.PayUSdkDetails;
//import com.payu.india.Model.PayuConfig;
//import com.payu.india.Model.PayuHashes;
//import com.payu.india.Payu.Payu;
//import com.payu.india.Payu.PayuConstants;
//import com.payu.india.Payu.PayuErrors;
//import com.payu.paymentparamhelper.PaymentParams;
//import com.payu.paymentparamhelper.PostData;
//import com.payu.payuui.Activity.PayUBaseActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Iterator;

//import com.payu.india.Model.PaymentParams;
//import com.payu.india.Model.PostData;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity prepares PaymentParams, fetches hashes from server and send it to PayuBaseActivity.java.
 */
public class PayUMainActivity extends AppCompatActivity {

//    private String merchantKey, userCredentials;
//
//    // These will hold all the payment parameters
//    private PaymentParams mPaymentParams;
//
//    // This sets the configuration
//    private PayuConfig payuConfig;
//
//    private Spinner environmentSpinner;
//
//    // Used when generating hash from SDK
//    private PayUChecksum checksum;
//    String salt = null;
//    String total_amount="";
//    String order_hash="";
//    String del_address="",st_city="",st_state="",st_pincode="",ship_fname="",ship_lname="",st_cust_name="",login_tel="";
//
//    String dist_id="";
//    SharedPreferences pref;
//    TransparentProgressDialog pDialog;
//    JsonObjectRequest jsonObjRequest = null;
//    EditText editTextEmail;
//
//    String firstname,email;
//    EditText editTextAmount;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_test);
//
//        //TODO Must write below code in your activity to set up initial context for PayU
//        Payu.setInstance(this);
//
//
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras != null) {
//                total_amount= extras.getString("grand_total");
//            }
//        }
//
//        if(total_amount.equals("")){
//
//            cancelOrderEcom();
//
//        }
//        // lets set up the tool bar;
////        Toolbar toolBar = (Toolbar) findViewById(R.id.app_bar);
////        toolBar.setTitle("PayU Demo App");
////        toolBar.setTitleTextColor(Color.WHITE);
////        setSupportActionBar(toolBar);
//
//        // lets tell the people what version of sdk we are using
//        PayUSdkDetails payUSdkDetails = new PayUSdkDetails();
//        pref = CommonFun.getPreferences(getApplicationContext());
//
//
//        String user_email=pref.getString("login_email","");
//        editTextEmail= findViewById(R.id.editTextEmail);
//        editTextEmail.setText(user_email);
//        editTextAmount= findViewById(R.id.editTextAmount);
//
//        email=user_email;
//
//        firstname=pref.getString("login_fname","");
//
//        //Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();
//
//        //Lets setup the environment spinner
//        environmentSpinner = (Spinner) findViewById(R.id.spinner_environment);
//        //  List<String> list = new ArrayList<String>();
//        String[] environmentArray = getResources().getStringArray(R.array.environment_array);
///*        list.add("Test");
//        list.add("Production");*/
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, environmentArray);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        environmentSpinner.setAdapter(dataAdapter);
//        environmentSpinner.setSelection(0);
//
//        environmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (environmentSpinner.getSelectedItem().equals("Production")) {
//                    Toast.makeText(PayUMainActivity.this, getString(R.string.use_live_key_in_production_environment), Toast.LENGTH_SHORT).show();
//
//                    /* For test keys, please contact mobile.integration@payu.in with your app name and registered email id
//                     */
//                     ((EditText) findViewById(R.id.editTextMerchantKey)).setText("0MQaQP");
//                   // ((EditText) findViewById(R.id.editTextMerchantKey)).setText("smsplus");
//                   // ((EditText) findViewById(R.id.editTextMerchantKey)).setText("Ux7giI");
//                }
//                else{
//                    //set the test key in test environment
//                    ((EditText) findViewById(R.id.editTextMerchantKey)).setText("gtKFFX");
//                    // ((EditText) findViewById(R.id.editTextMerchantKey)).setText("VgZldf");
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        editTextAmount.setText(total_amount);
//        startPaymentProcess();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
//        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
//            if (data != null) {
//
//                /**
//                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
//                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
//                 *
//                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
//                 * for identifying status of transaction. There are two possible status like, success or failure
//                 * */
//                new AlertDialog.Builder(this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//
//            } else {
//                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    /**
//     * This method prepares all the payments params to be sent to PayuBaseActivity.java
//     */
//    public void startPaymentProcess() {
//
//
//        // merchantKey="";
//        merchantKey = ((EditText) findViewById(R.id.editTextMerchantKey)).getText().toString();
//        String amount = ((EditText) findViewById(R.id.editTextAmount)).getText().toString();
//        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
//
//        String value = environmentSpinner.getSelectedItem().toString();
//        int environment;
//        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
////
////        if (value.equals(TEST_ENVIRONMENT))
////            environment = PayuConstants.STAGING_ENV;
////        else
////            environment = PayuConstants.PRODUCTION_ENV;
//
//        environment=PayuConstants.PRODUCTION_ENV;
//        //environment=PayuConstants.STAGING_ENV;
//        userCredentials = merchantKey + ":" + email;
//
//        //userCredentials = "ra:ra";
//
//        //TODO Below are mandatory params for hash genetation
//        mPaymentParams = new PaymentParams();
//        /**
//         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id
//
//         */
//        mPaymentParams.setKey(Global_Settings.payu_merchant_key);
//        mPaymentParams.setAmount(amount);
//        mPaymentParams.setProductInfo("product_info");
//        mPaymentParams.setFirstName(firstname);
//        mPaymentParams.setEmail(email);
//        mPaymentParams.setPhone("");
//
//
//        /*
//         * Transaction Id should be kept unique for each transaction.
//         * */
//        mPaymentParams.setTxnId(order_hash);
//
//        /**
//         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
//         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
//         */
//        mPaymentParams.setSurl(Global_Settings.payment_success+"?orderhash="+order_hash);
//        mPaymentParams.setFurl(Global_Settings.payment_failed+"?orderhash="+order_hash);
//        mPaymentParams.setNotifyURL(Global_Settings.payment_success+"?orderhash="+order_hash);  //for lazy pay
//
//        /*
//         * udf1 to udf5 are options params where you can pass additional information related to transaction.
//         * If you don't want to use it, then send them as empty string like, udf1=""
//         * */
//        mPaymentParams.setUdf1(dist_id);
//        mPaymentParams.setUdf2("");
//        mPaymentParams.setUdf3("");
//        mPaymentParams.setUdf4("");
//        mPaymentParams.setUdf5("");
//
//        /**
//         * These are used for store card feature. If you are not using it then user_credentials = "default"
//         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
//         * here merchant_key = your merchant key,
//         * user_id = unique id related to user like, email, phone number, etc.
//         * */
//        mPaymentParams.setUserCredentials(userCredentials);
//
//        //TODO Pass this param only if using offer key
//        //mPaymentParams.setOfferKey("cardnumber@8370");
//
//        //TODO Sets the payment environment in PayuConfig object
//        payuConfig = new PayuConfig();
//        payuConfig.setEnvironment(environment);
//        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
//        // generateHashFromServer(mPaymentParams);
//
//        /**
//         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
//         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
//         * should not be used.
//         * */
//        if(environment== PayuConstants.STAGING_ENV){
//            salt = "eCwWELxi";
//            //salt = "wpAo1AgO";
//        }else {
//            //Production Env
//          //  salt = "1b1b0";
//            salt = "13p0PXZk";
//        }
//        salt=Global_Settings.payu_merchant_key;
////        String salt = "eCwWELxi";
//        // String salt = "13p0PXZk";
//        // String salt = "1b1b0";
//        //
//        //generateHashFromSDK(mPaymentParams, salt);
//
//        generateHashFromServer(mPaymentParams);
//    }
//
//
//    public void generateHashFromServer(PaymentParams mPaymentParams) {
//        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.
//
//        // lets create the post params
//        StringBuffer postParamsBuffer = new StringBuffer();
//        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
//        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
//        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
//        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
//        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
//        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
//        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
//        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
//        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
//        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
//        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
//        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));
//
////        postParamsBuffer.append(concatParams(PayuConstants.KEY, "MAZyiB"));
////        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, "2"));
////        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
////        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
////        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
////        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
////        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
////        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
////        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
////        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
////        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
////        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));
//
//        // for offer_key
//        if (null != mPaymentParams.getOfferKey())
//            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));
//
//        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1) : postParamsBuffer.toString();
//
//        // lets make an api call
//        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
//        getHashesFromServerTask.execute(postParams);
//    }
//
//    protected String concatParams(String key, String value) {
//        return key + "=" + value + "&";
//    }
//    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(PayUMainActivity.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
//        }
//
//        @Override
//        protected PayuHashes doInBackground(String... postParams) {
//            PayuHashes payuHashes = new PayuHashes();
//            try {
//
//                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
//                //URL url = new URL("https://payu.herokuapp.com/get_hash");
//                //URL url = new URL("https://info.payu.in/merchant/postservice.php?form=2");
//                //URL url = new URL("http://app.galway.in:81/android_Galway_NewLook_Notification/genkeyhash.php");
//
//
//                //URL url = new URL(Global_Settings.api_url+"glaze/galwaykart/genkeyhash.php");
//                URL url = new URL(Global_Settings.online_payment_hash_key_url+"glaze/galwaykart/genkeyhash.php");
//                // get the payuConfig first
//                String postParam = postParams[0];
//
//                byte[] postParamsByte = postParam.getBytes(StandardCharsets.UTF_8);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
//                conn.setDoOutput(true);
//                conn.getOutputStream().write(postParamsByte);
//
//                InputStream responseInputStream = conn.getInputStream();
//                StringBuffer responseStringBuffer = new StringBuffer();
//                byte[] byteContainer = new byte[1024];
//                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
//                    responseStringBuffer.append(new String(byteContainer, 0, i));
//                }
//
//
//                ////Log.d("Resonse String Buffer",responseStringBuffer.toString());
//                JSONObject response = new JSONObject(responseStringBuffer.toString());
//
//                Iterator<String> payuHashIterator = response.keys();
//                while (payuHashIterator.hasNext()) {
//                    String key = payuHashIterator.next();
//                    switch (key) {
//                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
//                        /**
//                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
//                         * Below is formula for generating payment_hash -
//                         *
//                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
//                         *
//                         */
//                        case "payment_hash":
//                            payuHashes.setPaymentHash(response.getString(key));
//                            break;
//                        /**
//                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
//                         * Below is formula for generating vas_for_mobile_sdk_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be "default"
//                         *
//                         */
//                        case "vas_for_mobile_sdk_hash":
//                            payuHashes.setVasForMobileSdkHash(response.getString(key));
//                            break;
//                        /**
//                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
//                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
//                         *
//                         */
//                        case "payment_related_details_for_mobile_sdk_hash":
//                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
//                            break;
//
//                        //TODO Below hashes only needs to be generated if you are using Store card feature
//                        /**
//                         * delete_user_card_hash is used while deleting a stored card.
//                         * Below is formula for generating delete_user_card_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
//                         *
//                         */
//                        case "delete_user_card_hash":
//                            payuHashes.setDeleteCardHash(response.getString(key));
//                            break;
//                        /**
//                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
//                         * Below is formula for generating get_user_cards_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
//                         *
//                         */
//                        case "get_user_cards_hash":
//                            payuHashes.setStoredCardsHash(response.getString(key));
//                            break;
//                        /**
//                         * edit_user_card_hash is used while editing details of existing stored card.
//                         * Below is formula for generating edit_user_card_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
//                         *
//                         */
//                        case "edit_user_card_hash":
//                            payuHashes.setEditCardHash(response.getString(key));
//                            break;
//                        /**
//                         * save_user_card_hash is used while saving card to the vault
//                         * Below is formula for generating save_user_card_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
//                         *
//                         */
//                        case "save_user_card_hash":
//                            payuHashes.setSaveCardHash(response.getString(key));
//                            break;
//
//                        //TODO This hash needs to be generated if you are using any offer key
//                        /**
//                         * check_offer_status_hash is used while using check_offer_status api
//                         * Below is formula for generating check_offer_status_hash -
//                         *
//                         * sha512(key|command|var1|salt)
//                         *
//                         * here, var1 will be Offer Key.
//                         *
//                         */
//                        case "check_offer_status_hash":
//                            payuHashes.setCheckOfferStatusHash(response.getString(key));
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return payuHashes;
//        }
//
//        @Override
//        protected void onPostExecute(PayuHashes payuHashes) {
//            super.onPostExecute(payuHashes);
//
//            progressDialog.dismiss();
//            launchSdkUI(payuHashes);
//        }
//    }
//
//
//    /******************************
//     * Client hash generation
//     ***********************************/
//    // Do not use this, you may use this only for testing.
//    // lets generate hashes.
//    // This should be done from server side..
//    // Do not keep salt anywhere in app.
//    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
//        PayuHashes payuHashes = new PayuHashes();
//        PostData postData = new PostData();
//
//        // payment Hash;
//        checksum = null;
//        checksum = new PayUChecksum();
//        checksum.setAmount(mPaymentParams.getAmount());
//        checksum.setKey(mPaymentParams.getKey());
//        checksum.setTxnid(mPaymentParams.getTxnId());
//        checksum.setEmail(mPaymentParams.getEmail());
//        checksum.setSalt(salt);
//        checksum.setProductinfo(mPaymentParams.getProductInfo());
//        checksum.setFirstname(mPaymentParams.getFirstName());
//        checksum.setUdf1(mPaymentParams.getUdf1());
//        checksum.setUdf2(mPaymentParams.getUdf2());
//        checksum.setUdf3(mPaymentParams.getUdf3());
//        checksum.setUdf4(mPaymentParams.getUdf4());
//        checksum.setUdf5(mPaymentParams.getUdf5());
//
//        postData = checksum.getHash();
//        if (postData.getCode() == PayuErrors.NO_ERROR) {
//            payuHashes.setPaymentHash(postData.getResult());
//        }
//
//        // checksum for payemnt related details
//        // var1 should be either user credentials or default
//        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
//        String key = mPaymentParams.getKey();
//
//        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
//            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
//        //vas
//        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//            payuHashes.setVasForMobileSdkHash(postData.getResult());
//
//        // getIbibocodes
//        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//            payuHashes.setMerchantIbiboCodesHash(postData.getResult());
//
//        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
//            // get user card
//            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
//                payuHashes.setStoredCardsHash(postData.getResult());
//            // save user card
//            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setSaveCardHash(postData.getResult());
//            // delete user card
//            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setDeleteCardHash(postData.getResult());
//            // edit user card
//            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setEditCardHash(postData.getResult());
//        }
//
//        if (mPaymentParams.getOfferKey() != null) {
//            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
//            if (postData.getCode() == PayuErrors.NO_ERROR) {
//                payuHashes.setCheckOfferStatusHash(postData.getResult());
//            }
//        }
//
//        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
//            payuHashes.setCheckOfferStatusHash(postData.getResult());
//        }
//
//        // we have generated all the hases now lest launch sdk's ui
//        launchSdkUI(payuHashes);
//    }
//
//    // deprecated, should be used only for testing.
//    private PostData calculateHash(String key, String command, String var1, String salt) {
//        checksum = null;
//        checksum = new PayUChecksum();
//        checksum.setKey(key);
//        checksum.setCommand(command);
//        checksum.setVar1(var1);
//        checksum.setSalt(salt);
//        return checksum.getHash();
//    }
//    /**
//     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
//     *
//     * @param payuHashes it contains all the hashes generated from merchant server
//     */
//    public void launchSdkUI(PayuHashes payuHashes) {
//
//        Intent intent = new Intent(this, PayUBaseActivity.class);
//        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
//        intent.putExtra(PayuConstants.SALT,salt);
//        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
//        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
//    }
//
//
//
//
//    private void cancelOrderEcom()
//    {
//        pref = CommonFun.getPreferences(PayUMainActivity.this);
//        // String st_Cancel_Order_URL=Global_Settings.api_url+"glaze/order_cancel.php?id="+pref.getString("orderhash","").trim();
//        // cancelOrder(st_Cancel_Order_URL);
//        goToOrderListActivity();
//    }
//
//    private void cancelOrder(String st_Cancel_Order_URL) {
//
//        pDialog = new TransparentProgressDialog(PayUMainActivity.this);
//        pDialog.setCancelable(false);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pDialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        try {
//            jsonObjRequest = new JsonObjectRequest(Request.Method.GET, st_Cancel_Order_URL,null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    if (response != null) {
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                            ////Log.d("jsonObject",jsonObject+"");
//
//                            String st_Order_Cancel_Status = jsonObject.getString("orderdetails");
//                            //Log.d("st_Order_Cancel_Status",st_Order_Cancel_Status);
//
////                            final AlertDialog.Builder b;
////                            try
////                            {
////
////
////                                SharedPreferences.Editor editor=pref.edit();
////                                editor.putString("orderhash","");
////                                editor.putString("paymentdue","");
////                                editor.putString("orderpono","");
////                                editor.commit();
////
////
////                                Vibrator vibrator = (Vibrator) PayUMainActivity.this.getSystemService(OrderDetails.VIBRATOR_SERVICE);
////                                vibrator.vibrate(100);
////
////                                final Dialog dialog = new Dialog(PayUMainActivity.this);
////                                dialog.setContentView(R.layout.custom_alert_dialog_design);
////                                TextView tv_dialog = (TextView)dialog.findViewById(R.id.tv_dialog);
////                                ImageView image_view_dialog = (ImageView)dialog.findViewById(R.id.image_view_dialog);
////                                tv_dialog.setText("Oops!!! Something Wrong\nCannot place your order\nPlease Try Again");
////                                dialog.show();
////
////
////                                TimerTask timerTask=new TimerTask() {
////                                    @Override
////                                    public void run() {
////
////                                        goToOrderListActivity();
////
////                                    }};
////
////                                Timer timer=new Timer();
////                                timer.schedule(timerTask,4500);
////
////                            }
////                            catch(Exception ex)
////                            {
////
////
////                            }
//                            goToOrderListActivity();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            goToOrderListActivity();
//                        }
//
//                    }
//                }
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//
//                    goToOrderListActivity();
//                    //CommonFun.showVolleyException(error,PayUMainActivity.this);
//
////                    CommonFun.alertError(OrderDetails.this,error.toString());
////
////                    error.printStackTrace();
//                }
//
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        queue.add(jsonObjRequest);
//
//    }
//
//    private void goToOrderListActivity() {
//        Intent intent=new Intent(PayUMainActivity.this, OrderListActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(intent);
//        CommonFun.finishscreen(PayUMainActivity.this);
//    }
}
