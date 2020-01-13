package com.galwaykart.notification;

public class NotificationModel {

    String Id;
    String title;
    String message;
    String created;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }




}
