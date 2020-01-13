package com.galwaykart;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.galwaykart.HomePageTab.HomePage_CategoryTab;
import com.galwaykart.HomePageTab.HomePage_OfferTab;
import com.galwaykart.HomePageTab.HomePage_ShopByCategoryTab;
import com.galwaykart.dbfiles.Activity.Mvvm_HomePage_TopProductTab;

/**
 * Created by abdalla on 2/18/18.
 */

public class HomePageTabPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

     HomePageTabPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomePage_ShopByCategoryTab();
            case 1:
                return new Mvvm_HomePage_TopProductTab();
            case 2:
                return new HomePage_CategoryTab();
            case 3:
                return new HomePage_OfferTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
