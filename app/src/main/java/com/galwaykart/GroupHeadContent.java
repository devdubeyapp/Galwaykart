package com.galwaykart;

import java.util.ArrayList;

/**
 * Created by ankesh on 7/18/2017.
 */

public class GroupHeadContent {

    private String name;
    private ArrayList<MenuHeadContent> countryList = new ArrayList<MenuHeadContent>();

    public GroupHeadContent(String name, ArrayList<MenuHeadContent> countryList) {
        super();
        this.name = name;
        this.countryList = countryList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<MenuHeadContent> getCountryList() {
        return countryList;
    }
    public void setCountryList(ArrayList<MenuHeadContent> countryList) {
        this.countryList = countryList;
    }
}
