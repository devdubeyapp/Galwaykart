package com.galwaykart.SingleProductView;

/**
 * Created by itsoftware on 04/01/2018.
 */

public class DataModelProductReview {

    String TAG_title="title";
    String TAG_detail="detail";

    public String getTAG_title() {
        return TAG_title;
    }

    public void setTAG_title(String TAG_title) {
        this.TAG_title = TAG_title;
    }

    public String getTAG_detail() {
        return TAG_detail;
    }

    public void setTAG_detail(String TAG_detail) {
        this.TAG_detail = TAG_detail;
    }

    public String getTAG_nickname() {
        return TAG_nickname;
    }

    public void setTAG_nickname(String TAG_nickname) {
        this.TAG_nickname = TAG_nickname;
    }

    public String getTAG_rating() {
        return TAG_rating;
    }

    public void setTAG_rating(String TAG_rating) {
        this.TAG_rating = TAG_rating;
    }

    String TAG_nickname="nickname";
    String TAG_rating="rating";

    public DataModelProductReview(String TAG_title, String TAG_detail, String TAG_nickname, String TAG_rating) {
        this.TAG_title = TAG_title;
        this.TAG_detail = TAG_detail;
        this.TAG_nickname = TAG_nickname;
        this.TAG_rating = TAG_rating;
    }



}
