package com.galwaykart.dbfiles.Repository;

import com.galwaykart.dbfiles.ProductDataModel;

import java.util.ArrayList;

public interface ProductReceiveListener {
    void onDataReceived(ArrayList<ProductDataModel> list);
    void onError(int error);

}
