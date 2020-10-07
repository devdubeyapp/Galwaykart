package com.galwaykart.essentialClass;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ankesh on 9/5/2017.
 */

public class Global_Settings {

// public native static  String commonGalwayKartData();
//public native static String commonSalesData();
//public native static String otpUrlData();

// static {
//    System.loadLibrary("commonfun");
// }


    public static final int current_soft_version=97;
    // public static final String test_offer_url="http://qa.galwaykart.com/24nov/galway_sus/";
    //public static final String Notification_api_url="http://test.glazegalway.com:81/ios_galwaykart/";
    //public static final String galway_api_url=commonSalesData();
   // public static final String Cross_App_Promote="http://192.168.10.72:81//SSL_PHP_API/galway_app_promotion.php?app_id=";
   public static final String Cross_App_Promote="http://app.galway.in:81//SSL_PHP_API/galway_app_promotion.php?app_id=";
   public static final String TAG_LOG="glazekartapp";

   public static final String chat_url="https://chatbot.glazegalway.com/mobile.html";

     public static final String galway_wcf_api_url="https://wcf.galway.in/"; //live
    //public static final String galway_wcf_api_url="http://devecom.galway.co.in/";
    //public static final String galway_wcf_api_url="http://wcf.glazegalway.net/";

    public static final String galway_api_url="https://ecom.galway.co.in/";   // Live
   // public static final String galway_api_url="http://it.galway.in/";      // Test

    //public static final String st_sales_api=commonSalesData()+"returnapi/";
    public static final String st_sales_api=galway_api_url+"returnapi/";     // Live
    //public static final String st_sales_api="http://it.galway.in/returnapi/";// Test

    //public static final String api_url="https://www.galwaykart.com/";                 // Live
    //public static String web_url="https://www.galwaykart.com/";

    //public static final String api_url="http://192.168.10.130/galwaykart/";               // Test
    public static String web_url="http://qa.galwaykart.com/";
    public static String api_url="http://qa.galwaykart.com/";      // new qa server
    public static String current_zone="";


    //public static final String online_payment_hash_key_url="https://www.galwaykart.com/";
    // public static final String api_url="https://www.galwaykart.com/";
    //public static final String api_url=commonsuGalwayKartData();

    //public static final String api_glaze_url="http://wcf.glazegalway.net/Distributor.svc/";
    public static final String api_glaze_url=galway_wcf_api_url+"Distributor.svc/";

    //public static final String shipyaari_api_url="http://192.168.10.6:81/glaze/";
    //public static final String shipyaari_api_url="https://seller.shipyaari.com/logistic/webservice/shipyaaritrackNew.php?";
    public static final String shipyaari_api_url="https://seller.shipyaari.com/avn_ci/siteadmin/track/track_api/";

    public static final String api_custom_url=api_url+"glaze/";
    public static final String Notification_api_url=api_url+"glaze/notify/";
    public static final String cart_amount_api=api_url+"rest/V1/m-carts/mine/totals";

  //  public static final String cart_amount_api_v1=api_url+"rest/V1/carts/mine/totals";

    public static final String webview_api=api_url+"glaze/galwaykart/";
    public static final String image_url= api_url+"pub/media/catalog/product/";
    public static final String terms_url_api=api_url+"rest/V1/m-static-page/";
    public static final String home_page_api=api_url+"glaze/galwaykart/homepage_details_v1.php";
    public static final String otp_url=api_custom_url+"galwaykart/api_get_otp.php";
    public static final String st_Tamplate="email_reset";
    public static final String st_check_user=api_url+"rest/V1/customer/irid/exits/";


    //public static final String user_details_url=api_url+"glaze/fcode.php?id=";

    //public static final String wms_checkout="http://192.168.10.34:81/rateit/galwaykart/wmscheckout.php";

    public static final String st_WebSiteID="1";

    public static final String sales_checkout_url=galway_api_url+"returnapi/Purchase_OrderInsert/";
    //public static final String sales_checkout_url="http://it.galway.in/returnapi/Purchase_OrderInsert/";

    static final String TAG = "GCM Android";
    static final String DISPLAY_MESSAGE_ACTION =
            "com.galwaykart.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
   public static final String PUSH_NOTIFICATION = "pushNotification";

 /**
  * Payment Gateway
  */
    public static final String online_payment_hash_key_url=web_url ;
    public static final String st_paytm_checksum_api="https://www.galwaykart.com/"+"glaze/paytm_checksum/generateChecksum.php/";
    public static final String payment_failed=web_url+"glaze/galwaykart/failed_checkout.php";
    public static final String payment_success=web_url+"glaze/galwaykart/success_checkout_android.php";
    public static final String payu_merchant_key="MAZyiB"; //live
    //public static final String payu_merchant_key="fB7m8s"; //sandbox


    /**
     * Address Change
     */
    public static final String st_address=api_url+"rest/V1/website/list";

}
