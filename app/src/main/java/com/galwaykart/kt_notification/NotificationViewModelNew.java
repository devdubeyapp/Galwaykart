package com.galwaykart.kt_notification;


import androidx.lifecycle.ViewModel;

public class NotificationViewModelNew extends ViewModel {

    private int count=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }





//    private MutableLiveData<List<NotificationModelNew>> notification;
//
//    public LiveData<List<NotificationModelNew>> getNotification(){
//
//        if(notification!=null){
//            notification=new MutableLiveData<List<NotificationModelNew>>();
//
//
//        }
//        return notification;
//
//    }
//
//    public void loadNotification(){
//
//        new NotificationModelNew("1","new notification");
//    }


}
