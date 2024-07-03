package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleh on 22.07.16.
 */
public class DataResponce {

    @SerializedName("categories")
    List<Category> categories;

    @SerializedName("subcategories")
    List<SubCategory> subCategories;

    @SerializedName("food")
    List<Food> foods;

    public List<Category> getCategories() {
        return categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public List<Food> getFoods() {
        return foods;
    }

}
