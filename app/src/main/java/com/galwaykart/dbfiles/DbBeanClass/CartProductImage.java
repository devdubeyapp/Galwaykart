package com.galwaykart.dbfiles.DbBeanClass;

/**
 * Created by ankesh on 9/28/2017.
 */

public class CartProductImage {

    String _image;
    String _sku;

    public CartProductImage() {

    }
    public CartProductImage(String _sku,String _image) {
        this._image = _image;
        this._sku = _sku;
    }


    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_sku() {
        return _sku;
    }

    public void set_sku(String _sku) {
        this._sku = _sku;
    }


}
