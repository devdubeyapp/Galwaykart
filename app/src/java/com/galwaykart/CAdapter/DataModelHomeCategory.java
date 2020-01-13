package com.galwaykart.CAdapter;

/**
 * Created by ankesh on 9/20/2017.
 */

public class DataModelHomeCategory {

    public DataModelHomeCategory(String id, String image) {
        this.id = id;
        this.image = image;
    }

    String id,image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
