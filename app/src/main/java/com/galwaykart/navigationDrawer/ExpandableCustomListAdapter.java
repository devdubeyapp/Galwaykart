package com.galwaykart.navigationDrawer;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.galwaykart.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableCustomListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableCustomListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;
        //Log.d("childText",childText);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);
        ImageView imageView = convertView.findViewById(R.id.img_icon);

        if(childText.equalsIgnoreCase("WishLIst")) {
            imageView.setImageResource(R.drawable.icon_heart);
        }else if(childText.equalsIgnoreCase("Return Order")) {
            imageView.setImageResource(R.drawable.your_order);
        }else if(childText.equalsIgnoreCase("Voucher Report")) {
            imageView.setImageResource(R.drawable.icon_report);
        }else if(childText.equalsIgnoreCase("Change Mobile Number")) {
            imageView.setImageResource(R.drawable.icon_mobile);
        }else if(childText.equalsIgnoreCase("Change Password")) {
            imageView.setImageResource(R.drawable.ic_change_pwd);
        }else if(childText.equalsIgnoreCase("Change Email")) {
            imageView.setImageResource(R.drawable.icon_email);
        }else if(childText.equalsIgnoreCase("Address Book")) {
            imageView.setImageResource(R.drawable.address_book);
        }else if(childText.equalsIgnoreCase("Logout")) {
            imageView.setImageResource(R.drawable.your_order);
        }else{
            imageView.setImageResource(R.drawable.lawicon);
        }



        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        ImageView img_icon_header = convertView.findViewById(R.id.img_icon_header);

        if(headerTitle.equalsIgnoreCase("Home")){
            img_icon_header.setImageResource(R.drawable.homeicon);
        }else if(headerTitle.equalsIgnoreCase("Your Order")){
            img_icon_header.setImageResource(R.drawable.your_order);
        }else if(headerTitle.equalsIgnoreCase("Notification")){
            img_icon_header.setImageResource(R.drawable.icon_notice);
        }
        else if(headerTitle.equalsIgnoreCase("Profile")){
            img_icon_header.setImageResource(R.drawable.icon_profile);
        }
        else if(headerTitle.equalsIgnoreCase("Settings")){
            img_icon_header.setImageResource(R.drawable.settings);
        }

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
