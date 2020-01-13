package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by sumitsaini on 9/13/2017.
 */

public class LoginBeanClass {

    String customer_id="";
    String email_id="";
    String password="";
    String phone_no="";
    String fName="";
    String lName="";

    public LoginBeanClass()
    {

    }

    public LoginBeanClass(String customer_id, String email_id, String fName,String lName,String password,String phone_no){

        this.customer_id = customer_id;
        this.email_id = email_id;
        this.password = password;
        this.phone_no = phone_no;
        this.fName = fName;
        this.lName = lName;
    }


    // getting name
    public String getCustomer_id(){
        return this.customer_id;
    }

    // setting name
    public void setCustomer_id(String customer_id){
        this.customer_id = customer_id;
    }

    // getting name
    public String getEmail_id(){
        return this.email_id;
    }

    // setting name
    public void setEmail_id(String email_id){
        this.email_id = email_id;
    }

    // getting name
    public String getPassword(){
        return this.password;
    }

    // setting name
    public void setPassword(String password){
        this.password = password;
    }

    // getting name
    public String getPhone_no(){
        return this.phone_no;
    }

    // setting name
    public void setPhone_no(String phone_no){
        this.phone_no = phone_no;
    }

    // getting name
    public String getfName(){
        return this.fName;
    }

    // setting name
    public void setfName(String fName){
        this.fName = fName;
    }

    // getting name
    public String getlName(){
        return this.lName;
    }

    // setting name
    public void setlName(String lName){
        this.lName = lName;
    }


}
