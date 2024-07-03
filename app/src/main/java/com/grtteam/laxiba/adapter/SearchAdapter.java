package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.Food;

import java.util.Collections;
import java.util.List;

/**
 * Created by oleh on 26.07.16.
 */
public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<Food> items = Collections.EMPTY_LIST;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Food> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Food food = (Food) getItem(position);

        convertView = infalInflater.inflate(R.layout.expandable_item, parent, false);
        TextView txtFoodName = (TextView) convertView
                .findViewById(R.id.title);
        TextView txtFoodData = (TextView) convertView
                .findViewById(R.id.data);

        txtFoodName.setText(food.getName());
        txtFoodData.setText(food.getData());


        return convertView;
    }
}
