package com.galwaykart.address_book;

import java.io.Serializable;

public class AddressDataModel implements Serializable {

    public String st_region;
    public String st_region_id;
    public String st_region_code;
    public String st_telephone;
    public String st_pincode;
    public String st_city;
    public String st_state;
    public String st_customer_first_name;
    public String st_customer_last_name;
    public String st_customer_id;
    public String st_address_id;
    public String st_company_name;
    public String st_default_ship;
    public String st_default_bill;
    public boolean showSelect;
    public boolean showEdit;
    public boolean showDelete;


    public AddressDataModel(String st_region, String st_region_id, String st_region_code, String st_telephone,
                            String st_pincode, String st_city, String st_state, String st_customer_first_name,String st_customer_last_name,
                            String st_customer_id, String st_address_id, String st_company_name, String st_default_ship, String st_default_bill, boolean showSelect, boolean showEdit, boolean showDelete) {
        this.st_region = st_region;
        this.st_region_id = st_region_id;
        this.st_region_code = st_region_code;
        this.st_telephone = st_telephone;
        this.st_pincode = st_pincode;
        this.st_city = st_city;
        this.st_state = st_state;
        this.st_customer_first_name = st_customer_first_name;
        this.st_customer_last_name = st_customer_last_name;
        this.st_customer_id = st_customer_id;
        this.st_address_id = st_address_id;
        this.st_company_name = st_company_name;
        this.st_default_ship = st_default_ship;
        this.st_default_bill = st_default_bill;
        this.showSelect = showSelect;
        this.showEdit = showEdit;
        this.showDelete = showDelete;
    }



    public String getSt_region() {
        return st_region;
    }

    public void setSt_region(String st_region) {
        this.st_region = st_region;
    }

    public String getSt_region_id() {
        return st_region_id;
    }

    public void setSt_region_id(String st_region_id) {
        this.st_region_id = st_region_id;
    }

    public String getSt_region_code() {
        return st_region_code;
    }

    public void setSt_region_code(String st_region_code) {
        this.st_region_code = st_region_code;
    }

    public String getSt_telephone() {
        return st_telephone;
    }

    public void setSt_telephone(String st_telephone) {
        this.st_telephone = st_telephone;
    }

    public String getSt_pincode() {
        return st_pincode;
    }

    public void setSt_pincode(String st_pincode) {
        this.st_pincode = st_pincode;
    }

    public String getSt_city() {
        return st_city;
    }

    public void setSt_city(String st_city) {
        this.st_city = st_city;
    }

    public String getSt_state() {
        return st_state;
    }

    public void setSt_state(String st_state) {
        this.st_state = st_state;
    }

    public String getSt_customer_name() {
        return st_customer_first_name;
    }

    public void setSt_customer_name(String st_customer_first_name) {
        this.st_customer_first_name = st_customer_first_name;
    }

    public String getSt_customer_last_name() {
        return st_customer_last_name;
    }

    public void setSt_customer_last_name(String st_customer_last_name) {
        this.st_customer_last_name = st_customer_last_name;
    }



    public String getSt_customer_id() {
        return st_customer_id;
    }

    public void setSt_customer_id(String st_customer_id) {
        this.st_customer_id = st_customer_id;
    }

    public String getSt_address_id() {
        return st_address_id;
    }

    public void setSt_address_id(String st_address_id) {
        this.st_address_id = st_address_id;
    }

    public String getSt_company_name() {
        return st_company_name;
    }

    public void setSt_company_name(String st_company_name) {
        this.st_company_name = st_company_name;
    }

    public String getSt_default_ship() {
        return st_default_ship;
    }

    public void setSt_default_ship(String st_default_ship) {
        this.st_default_ship = st_default_ship;
    }

    public String getSt_default_bill() {
        return st_default_bill;
    }

    public void setSt_default_bill(String st_default_bill) {
        this.st_default_bill = st_default_bill;
    }

    public boolean isShowSelect() {
        return showSelect;
    }

    public void setShowSelect(boolean showSelect) {
        this.showSelect = showSelect;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }


}
