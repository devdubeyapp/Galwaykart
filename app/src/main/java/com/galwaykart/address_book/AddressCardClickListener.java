package com.galwaykart.address_book;

public interface AddressCardClickListener {

    void clickListener(AddressDataModel addressDataModel);
    void EditClickListener(AddressDataModel addressDataModel,int selPosition);
    void DeleteClickListener(AddressDataModel addressDataModel);


}
