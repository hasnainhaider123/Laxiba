package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.SelectionValue;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.List;

/**
 * Created by oleh on 21.07.16.
 */
public class SelectionAdapter extends BaseAdapter {

    protected Context context;
    protected List<SelectionValue> items;
    protected boolean enabled = true;

    public SelectionAdapter(Context context, List<SelectionValue> items) {
        this.context = context;
        this.items = items;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SelectionValue getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView v = (TextView) view.findViewById(android.R.id.text1);
        v.setText(getItem(position).getNameResourceId());
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) context.getResources().getDimension(R.dimen.selection_text_size));
        if (!enabled) {
            view.setBackgroundResource(R.color.gray_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme()));
            } else {

                v.setTextColor(context.getResources().getColor(android.R.color.black));
            }
        }else {
            view.setBackgroundResource(android.R.color.transparent);
            v.setTextColor(ContextCompat.getColor(context, R.color.black));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                v.setTextColor(ContextCompat.getColor(context, R.color.black));
//            } else {
//                v.setTextColor(context.getResources().getColor(android.R.color.white));
//            }
        }
        int padding = (int) context.getResources().getDimension(R.dimen.picker_item_margin);
        v.setPadding(padding, padding, padding, padding);
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
