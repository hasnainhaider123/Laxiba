package com.grtteam.laxiba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.Collections;
import java.util.List;



/**
 * Created by oleh on 04.08.16.
 */
public class SelectionSetsAdapter extends BaseAdapter {

    private Context context;
    private OnSelectionActivateListener listener;
    private List<SelectionSet> items = Collections.EMPTY_LIST;

    public SelectionSetsAdapter(Context context, List<SelectionSet> items, OnSelectionActivateListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SelectionSet getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.selection_set_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SelectionSet selectionSet = getItem(position);

        holder.selectionName.setText(selectionSet.getSelectionName());
        if (selectionSet.getSelectionId().equals(SharedPreferenceHelper.getActiveSelectionId())) {
            // Selection active
            holder.activeMsg.setVisibility(View.VISIBLE);
            holder.activateBtn.setVisibility(View.GONE);
        } else {
            holder.activeMsg.setVisibility(View.GONE);
            holder.activateBtn.setVisibility(View.VISIBLE);
            holder.activateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemActivate(selectionSet.getSelectionId());
                    }
                }
            });
        }

        return convertView;
    }


    static class ViewHolder {
        TextView selectionName;
        Button activateBtn;
        TextView activeMsg;

        public ViewHolder(View view) {
            selectionName = view.findViewById(R.id.selection_name);
            activateBtn = view.findViewById(R.id.activate_btn);
            activeMsg = view.findViewById(R.id.active_msg);
        }
    }

    public interface OnSelectionActivateListener {
        void onItemActivate(String itemId);
    }
}
