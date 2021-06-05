package com.galwaykart.registration;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.galwaykart.Login.LoginActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

public class RegistrationTypeActivity extends AppCompatActivity {

    String [] spinner_data = {"Customer","Distributor","I Know Distributor","Employee"};
    Spinner spinner_customer_type;
    String st_group_id="",st_coming_from="";
    ArrayAdapter<String> adapter;
    Button button_sign_up;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CommonFun.finishscreen(this);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_type);
        spinner_customer_type = findViewById(R.id.spinner_customer_type);
        adapter = new ArrayAdapter<String>(RegistrationTypeActivity.this,android.R.layout.simple_spinner_dropdown_item,
                spinner_data);
        spinner_customer_type.setAdapter(adapter);

        /**
         * Customer selection spinner
         */

        spinner_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:

                        st_group_id = "1";
                        st_coming_from = "Customer";
                        break;
                    case 1:
                        st_group_id = "4";
                        st_coming_from = "Distributor";
                        break;

                    case 2:
                        st_group_id = "2";

                        st_coming_from = "I Know Distributor";
                        break;
                    case 3:
                        st_group_id = "3";

                        st_coming_from = "Employee";
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        button_sign_up= findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(st_group_id.equals("4") || st_group_id.equals("8")) //distributor
                {
                    Intent intent = new Intent(RegistrationTypeActivity.this, RegistrationActivity.class);
                    intent.putExtra("customer_type", st_group_id);
                    startActivity(intent);
                    finishScreen();
                }
                else if(st_group_id.equals("1")) //distributor
                {
                    Intent intent = new Intent(RegistrationTypeActivity.this, RegistrationActivity.class);
                    intent.putExtra("customer_type", st_group_id);
                    startActivity(intent);
                    finishScreen();
                }
                else {
                    Intent intent = new Intent(RegistrationTypeActivity.this, RegistrationTypeActivity.class);
                    intent.putExtra("customer_type", st_group_id);
                    startActivity(intent);
                    finishScreen();
                }

            }
        });


    }

    private void finishScreen()
    {
        this.finish();

    }


}
