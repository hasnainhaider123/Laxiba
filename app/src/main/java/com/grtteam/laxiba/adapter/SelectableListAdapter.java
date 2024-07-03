package com.grtteam.laxiba.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SubCategory;
import com.grtteam.laxiba.listeners.ItemClickListener;

import java.util.ArrayList;

public class SelectableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Category> dataListCategories;
    private ArrayList<SubCategory> dataListSubcategories;
    private ArrayList<Food> dataListFood;

    public SelectableListAdapter() {
    }

    public void setCategories(ArrayList<Category> categories) {
        this.dataListCategories = categories;
    }

    public void setSubcategories(ArrayList<SubCategory> subcategories) {
        this.dataListSubcategories = subcategories;
    }

    public void setFood(ArrayList<Food> foods){
        this.dataListFood = foods;
    }

    public ArrayList<SubCategory> getSubcategories() {
        return dataListSubcategories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(dataListFood != null){
            //type food
            view = layoutInflater.inflate(R.layout.expandable_item, parent, false);
            return new FoodViewHolder(view);
        } else {
            //type categories or subcategories
            view = layoutInflater.inflate(R.layout.selectable_item, parent, false);
            return new SelectableItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item;
        if (dataListCategories != null) {
            SelectableItemViewHolder vh = (SelectableItemViewHolder) holder;
            item = dataListCategories.get(position);
            vh.setTitle(((Category) item).getCatName());
            vh.setListener(listener, position);
        } else if(dataListSubcategories != null){
            item = dataListSubcategories.get(position);
            SelectableItemViewHolder vh = (SelectableItemViewHolder) holder;
            vh.setTitle(((SubCategory) item).getSubcatName());
            vh.setListener(listener, position);
        } else {
            Food food = dataListFood.get(position);
            FoodViewHolder vh = (FoodViewHolder) holder;
            vh.setInfo(food.getName(), food.getData());
        }

    }

    @Override
    public int getItemCount() {
        if (dataListCategories != null)
            return dataListCategories.size();
        if (dataListSubcategories != null)
            return dataListSubcategories.size();
        if(dataListFood != null)
            return dataListFood.size();
        return 0;
    }

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    class SelectableItemViewHolder extends RecyclerView.ViewHolder {
        private View root;
        private TextView title;
        private ItemClickListener listener;
        private int position;

        public SelectableItemViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            title = itemView.findViewById(R.id.selectableItemTitle);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setListener(ItemClickListener listener, int i) {
            this.listener = listener;
            this.position = i;
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClicked(position);
                }
            });
        }
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView data;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
        }

        void setInfo(String title, String data){
            this.title.setText(title);
            this.data.setText(data);
        }
    }
}
