package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh on 22.07.16.
 */
public class Food implements Comparable<Food> {

    @SerializedName("food_id")
    String foodId;
    @SerializedName("parent_cat_id")
    String catId;
    @SerializedName("food_name")
    String name;
    @SerializedName("allowed_contents")
    private String data;
    @SerializedName("language")
    private String language;
    @SerializedName("selection_id")
    private String selectionId;


    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }

    @Override
    public int compareTo(Food another) {
        return name.compareTo(another.getName());
    }
}
