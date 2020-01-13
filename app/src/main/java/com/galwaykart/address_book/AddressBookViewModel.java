package com.galwaykart.address_book;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AddressBookViewModel extends AndroidViewModel {

    private MutableLiveData<List<AddressDataModel>> addressdata;
    private LiveData<List<AddressDataModel>> liveaddressdata;

    private MutableLiveData<Boolean> DelSuccess;
    private LiveData<Boolean> liveDelSuccess;

    private AddressBookRepo repo;


    public AddressBookViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if(addressdata!=null){
            return;
        }
        repo=AddressBookRepo.getInstance();
    }

    public LiveData<List<AddressDataModel>> getLiveAddressBook(String st_come_from_update)
    {
        liveaddressdata=getAddressBook(st_come_from_update);
        return liveaddressdata;
    }

    public LiveData<List<AddressDataModel>> getAddressBook(String st_come_from_update) {

        if(addressdata==null){
            addressdata=new MutableLiveData<List<AddressDataModel>>();
        }

        loadAddressBook(st_come_from_update);

        return addressdata;
    }

    private void loadAddressBook(String st_come_from_update) {
        addressdata=repo.getAddress(getApplication(),st_come_from_update);

    }

    public LiveData<Boolean> getLiveDelAddressBook(List<AddressDataModel> listAddressDataModel,AddressDataModel addressDataModel)
    {
        liveDelSuccess=getDelAddressBook(addressDataModel,listAddressDataModel);
        return liveDelSuccess;
    }

    public LiveData<Boolean> getDelAddressBook(AddressDataModel addressDataModel,List<AddressDataModel>listAddressDataModel) {

        if(DelSuccess==null){
            DelSuccess=new MutableLiveData<Boolean>();
        }

        DelSuccess=deleteAddressBook(addressDataModel,listAddressDataModel);

        return DelSuccess;
    }


    public MutableLiveData<Boolean> deleteAddressBook(AddressDataModel addressDataModel, List<AddressDataModel>listAddressDataModel){
        MutableLiveData<Boolean> delete_success=repo.callDeleteAddress(getApplication(),listAddressDataModel,addressDataModel);
        return delete_success;
    }


}
