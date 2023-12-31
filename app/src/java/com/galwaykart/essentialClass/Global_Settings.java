package com.galwaykart.essentialClass;

/**
 * Created by ankesh on 9/5/2017.
 */

public class Global_Settings {

// public native static  String commonGalwayKartData();
//public native static String commonSalesData();
//public native static String otpUrlData();

 static {
  System.loadLibrary("commonfun");
 }

    public static  final int current_soft_version=106;
   // public static final String test_offer_url="http://qa.galwaykart.com/24nov/galway_sus/";
  //public static final String Notification_api_url="http://test.glazegalway.com:81/ios_galwaykart/";

 //  public static final String galway_api_url=commonSalesData();


     public static final String galway_api_url="https://ecom.galway.co.in/";   // Live
     //public static final String galway_api_url="http://it.galway.in/";      // Test


 //   public static final String st_sales_api=commonSalesData()+"returnapi/";

    public static final String st_sales_api="https://ecom.galway.co.in/returnapi/";     // Live
    //public static final String st_sales_api="http://it.galway.in/returnapi/";          // Test

    public static final String api_url="https://www.galwaykart.com/";                 // Live
    //public static final String api_url="http://qa.galwaykart.com/";               // Test

    public static final String online_payment_hash_key_url=api_url;
    //public static final String online_payment_hash_key_url="https://www.galwaykart.com/";

    // public static final String api_url="https://www.galwaykart.com/";
   //public static final String api_url=commonsuGalwayKartData();

   // public static final String api_glaze_url="http://wcf.glazegalway.net/Distributor.svc/";
     public static final String api_glaze_url="https://wcf.galway.in/Distributor.svc/";

   //    public static final String shipyaari_api_url="http://192.168.10.6:81/glaze/";
    public static final String shipyaari_api_url=api_url+"glaze/";

   public static final String api_custom_url=api_url+"glaze/";

    public static final String Notification_api_url=api_url+"glaze/notify/";

    public static final String cart_amount_api=api_url+"rest/V1/carts/mine/totals";

    public static final String image_url= api_url+"pub/media/catalog/product/";

    public static final String terms_url_api=api_url+"glaze/galwaykart/";
    public static final String home_page_api=api_url+"glaze/galwaykart/homepage_details.php";
    public static final String payment_success=api_url+"glaze/galwaykart/success_checkout_android.php";
    public static final String payment_failed=api_url+"glaze/galwaykart/failed_checkout.php";

   // public static final String payment_success="http://app.galway.in:81/android_Galway_NewLook_Notification/success_checkout.php";
   // public static final String payment_failed="http://app.galway.in:81/android_Galway_NewLook_Notification/failed_checkout.php";

    public static final String otp_url=api_custom_url+"galwaykart/api_get_otp.php";

    public static final String st_Tamplate="email_reset";

    public static final String user_details_url=api_url+"glaze/fcode.php?id=";

   // public static final String wms_checkout="http://192.168.10.34:81/rateit/galwaykart/wmscheckout.php";

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

    public static final String st_paytm_checksum_api=api_url+"glaze/paytm_checksum/generateChecksum.php/";

}
