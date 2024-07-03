package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SubCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableSubListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<SubCategory> subCategories;
    private HashMap<SubCategory, List<Food>> foods;

    public ExpandableSubListAdapter(Context context, List<SubCategory> subCategories, HashMap<SubCategory, List<Food>> foods) {
        this.context = context;
        this.subCategories = subCategories;
        this.foods = foods;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.foods.get(subCategories.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Food food = (Food) getChild(groupPosition, childPosition);

        convertView = infalInflater.inflate(R.layout.expandable_item, parent, false);
        TextView txtFoodName = (TextView) convertView
                .findViewById(R.id.title);
        TextView txtFoodData = (TextView) convertView
                .findViewById(R.id.data);

        txtFoodName.setText(food.getName());
        txtFoodData.setText(food.getData());


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.foods.get(subCategories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return subCategories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return subCategories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = ((SubCategory) getGroup(groupPosition)).getSubcatName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_category, parent, false);
        }


        ImageView arrowIcon = (ImageView) convertView.findViewById(R.id.arrow);
        if(isExpanded){
            arrowIcon.setImageResource(R.drawable.ic_expand_less_white_18dp);
        } else {
            arrowIcon.setImageResource(R.drawable.ic_expand_more_white_18dp);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.title);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
