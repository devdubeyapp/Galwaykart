package com.galwaykart.kt_notification;

public class NotificationModelNew {

    public String id;
    public String type;
    public String title;

    public String message;
    public String schedule;
    public String expiry;
    public String status;


    public NotificationModelNew(String id,String title) {
        this.id = id;
        this.title = title;
    }


}
