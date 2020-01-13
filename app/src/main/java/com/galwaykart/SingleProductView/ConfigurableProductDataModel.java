package com.galwaykart.SingleProductView;

public class ConfigurableProductDataModel {
    String stValueLabel="";
    String stValueIndex="";
    String stAttId = "";
    String st_clicked="";


    public String getSt_clicked() {
        return st_clicked;
    }

    public void setSt_clicked(String st_clicked) {
        this.st_clicked = st_clicked;
    }



    public ConfigurableProductDataModel(String stValueLabel, String stValueIndex) {
        this.stValueLabel = stValueLabel;
        this.stValueIndex = stValueIndex;
    }

    public ConfigurableProductDataModel(String stValueLabel, String stValueIndex, String stAttId,String st_clicked) {
        this.stValueLabel = stValueLabel;
        this.stValueIndex = stValueIndex;
        this.stAttId = stAttId;
        this.st_clicked=st_clicked;
    }

    public String getStAttId() {
        return stAttId;
    }

    public void setStAttId(String stAttId) {
        this.stAttId = stAttId;
    }

    public String getStValueLabel() {
        return stValueLabel;
    }

    public void setStValueLabel(String stValueLabel) {
        this.stValueLabel = stValueLabel;
    }

    public String getStValueIndex() {
        return stValueIndex;
    }

    public void setStValueIndex(String stValueIndex) {
        this.stValueIndex = stValueIndex;
    }
}
