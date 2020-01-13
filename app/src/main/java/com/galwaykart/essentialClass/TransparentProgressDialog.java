package com.galwaykart.essentialClass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.galwaykart.R;

public class TransparentProgressDialog extends Dialog {

    //private ImageView iv;
    private WebView wv;
    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setBackgroundResource(R.drawable.blank);
//        iv = new ImageView(context);
//       iv.setImageResource(R.drawable.ticonrefresh);
//
//        layout.addView(iv, params);
//        iv.getLayoutParams().height=100;
//        iv.getLayoutParams().width=100;
//        iv.requestLayout();
        wv=new WebView(context);
//        String webviewData="<html><body>" +
//                            "<img src='"+R.drawable.a9+"'"+
//                            "</body></html>";
        wv.loadDataWithBaseURL("file:///android_asset/", "<center><img src='loadinggif.gif' height='50px' width='50px'/></center>", "text/html", "utf-8", null);
        wv.setBackgroundColor(Color.TRANSPARENT);
        layout.addView(wv);
        wv.getLayoutParams().height=200;
        wv.getLayoutParams().width=200;
        wv.requestLayout();
        addContentView(layout, params);
    }

    @Override
    public void show() {
        super.show();
//        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(3000);
//        iv.setAnimation(anim);
//        iv.startAnimation(anim);
    }
}