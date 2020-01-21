# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\adt-bundle-windows-x86-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-renamesourcefileattribute SourceFile
-ignorewarnings

-keepattributes *Annotation*, Signature
-keepattributes Exceptions

-dontwarn org.apache.lang.**
#-dontwarn com.payu.payuui.**
#-dontwarn com.synnapps.**

### -- Picasso --
 -dontwarn com.squareup.picasso.**
 -dontwarn com.squareup.okhttp.**

 #### -- OkHttp --

 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --

-dontwarn org.apache.commons.logging.**

-dontwarn com.synnapps.**

####Keep apache http legacy---
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }
-keep class com.android

#-keep class com.galwaykart.productList
-keep class com.galwaykart.essentialClass.TransparentProgressDialog
#-keep class com.galwaykart.CAdapter.GridSpacingItemDecoration

#-keep class com.galwaykart.productList.RecyclerViewAdapter
####Keep decoration---
-keep class com.galwaykart.itemdecorator
-keep public class * extends android.support.v7.widget.RecyclerView.ItemDecoration
-keep class android.support.v7.widget.RecyclerView
-keep public class * extends android.support.v7.widget.RecyclerView$LayoutManager {
    public <init>(...);
}


-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }


####Keep design---
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }

#-keep public class * extends android.app.Fragment
-keep class com.synnapps.carouselview.CarouselView.** { *; }

#For native methods, see #http://proguard.sourceforge.net/manual/examples.html
#native
#-keepclasseswithmembernames class * {
#native<methods>;
#}

#-keepclassmembers class * {
#@android.webkit.JavascriptInterface<methods>;
#}

-keepclassmembers class com.paytm.pgsdk.PaytmWebView$PaytmJavaScriptInterface {
   public *;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception