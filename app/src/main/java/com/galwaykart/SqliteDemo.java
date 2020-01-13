package com.galwaykart;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.galwaykart.dbfiles.DatabaseHandler;
import com.galwaykart.dbfiles.DbBeanClass.LoginBeanClass;

/**
 * Created by sumitsaini on 9/13/2017.
 */

public class SqliteDemo extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    LoginBeanClass aClass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        databaseHandler = new DatabaseHandler(SqliteDemo.this);
        aClass = new LoginBeanClass();

        databaseHandler.insertLoginDetails(new LoginBeanClass("5",
                "enggsumi.05@gmail.com",
                "S","P",
                "2705","9999999999"));

//        databaseHandler.insertProductDetails()

//        deleteData();

        getData();

    }

    private void deleteData() {

        databaseHandler.deleteSingleUser("5");


    }

    private void getData() {

        databaseHandler.getSingleUser("5");

        aClass.getCustomer_id();
        aClass.getEmail_id();
        aClass.getfName();
        aClass.getlName();
        aClass.getPassword();
        aClass.getPhone_no();

    }
}
