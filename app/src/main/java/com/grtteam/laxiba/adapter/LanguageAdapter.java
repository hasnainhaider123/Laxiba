package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.SelectionValue;

import java.util.List;

/**
 * Created by oleh on 19.07.16.
 */
public class LanguageAdapter extends SelectionAdapter {


    public LanguageAdapter(Context context, List<SelectionValue>items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView v = (TextView) view.findViewById(android.R.id.text1);
        v.setText(getItem(position).getNameResourceId());
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) context.getResources().getDimension(R.dimen.selection_text_size));
        if (!enabled) {
            v.setBackgroundResource(android.R.color.darker_gray);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme()));
            } else {

                v.setTextColor(context.getResources().getColor(android.R.color.black));
            }
        }else {
            v.setBackgroundResource(R.color.gray_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setTextColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
            } else {

                v.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
        int padding = (int) context.getResources().getDimension(R.dimen.picker_item_margin);
        v.setPadding(padding, padding/2, padding, padding/2);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView v = (TextView) view.findViewById(android.R.id.text1);
        v.setText(getItem(position).getNameResourceId());
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) context.getResources().getDimension(R.dimen.selection_text_size));

        int padding = (int) context.getResources().getDimension(R.dimen.picker_item_margin);
        v.setPadding(padding, padding, padding, padding);
        return view;
    }
}
