package com.galwaykart.root;

import android.app.Application;
import android.content.Intent;

import com.galwaykart.HomePageActivity;

public class CommonRepository implements CommonRepository_i {

    @Override
    public void goToHome(Application application) {

        Intent intent=new Intent(application,HomePageActivity.class);

    }
}
