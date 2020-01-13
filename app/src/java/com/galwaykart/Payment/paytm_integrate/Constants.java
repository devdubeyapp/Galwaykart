package com.galwaykart.Payment.paytm_integrate;

/**
 * Created by Ankesh Kumar
 */

public class Constants {

    public static final String M_ID = "GlazeT05920717888492"; //Paytm Merchand Id we got it in paytm credentials
    public static final String M_KEY="KWDs8qfufdfV!MQA";
    public static final String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail109"; //Paytm industry type got it in paytm credential

    //public static final String WEBSITE = "APPSTAGING";
    public static final String WEBSITE = "WEBPROD";
    //public static final String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";
    public static final String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";

//  public static final String CALLBACK_URL = "http://192.168.10.6:81/galwaykart/paytm_checksum/callback.php";
}
