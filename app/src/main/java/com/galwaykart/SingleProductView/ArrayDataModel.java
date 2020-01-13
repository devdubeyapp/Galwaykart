package com.galwaykart.SingleProductView;

import java.util.ArrayList;

public class ArrayDataModel {

    String [] arr_valueLabel;
    String [] arrClickValue;

    public ArrayDataModel(String[] arr_valueLabel, String[]  arrClickValue) {
        this.arr_valueLabel = arr_valueLabel;
        this.arrClickValue = arrClickValue;
    }

    public String[] getArrClickValue() {
        return arrClickValue;
    }

    public void setArrClickValue(String[] arrClickValue) {
        this.arrClickValue = arrClickValue;
    }

    public String[] getArr_valueLabel() {
        return arr_valueLabel;
    }

    public void setArr_valueLabel(String[] arr_valueLabel) {
        this.arr_valueLabel = arr_valueLabel;
    }
}
