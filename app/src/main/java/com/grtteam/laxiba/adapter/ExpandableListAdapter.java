package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SubCategory;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private static final int CHILD_TYPE_SUB_CATEGORIES = 0;
    private static final int CHILD_TYPE_FOOD = 1;

    private Context context;
    private List<Category> categories;
    private HashMap<Category, List<Object>> subCategories;    // Object can be SubCategory or Food
    private HashMap<SubCategory, List<Food>> foods;

    public ExpandableListAdapter(Context context, List<Category> categories, HashMap<Category, List<Object>> subCategories, HashMap<SubCategory, List<Food>> foods) {
        this.context = context;
        this.categories = categories;
        this.subCategories = subCategories;
        this.foods = foods;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.subCategories.get(categories.get(groupPosition)).get(childPosititon);
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

        if (getChildType(groupPosition, childPosition) == CHILD_TYPE_SUB_CATEGORIES) {
            List<SubCategory> subCategories = (List<SubCategory>) getChild(groupPosition, childPosition);

            convertView = infalInflater.inflate(R.layout.expandable_sub_category, parent, false);
            ExpandableListView subExpandable = (ExpandableListView) convertView.findViewById(R.id.sub_expandable);

            ExpandableSubListAdapter subAdapter = new ExpandableSubListAdapter(context, subCategories, foods);
            subExpandable.setAdapter(subAdapter);

            subExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {

                }
            });
            subExpandable.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
        } else {
            Food food = (Food) getChild(groupPosition, childPosition);

            convertView = infalInflater.inflate(R.layout.expandable_item, parent, false);
            TextView txtFoodName = (TextView) convertView
                    .findViewById(R.id.title);
            TextView txtFoodData = (TextView) convertView
                    .findViewById(R.id.data);

            txtFoodName.setText(food.getName());
            txtFoodData.setText(food.getData());
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.subCategories.get(this.categories.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = ((Category) getGroup(groupPosition)).getCatName();
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
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if ( subCategories.get(categories.get(groupPosition)).get(childPosition) instanceof Food) {
            return CHILD_TYPE_FOOD;
        } else {
            return CHILD_TYPE_SUB_CATEGORIES;
        }
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
