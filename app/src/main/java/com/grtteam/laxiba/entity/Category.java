package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh on 22.07.16.
 */
public class Category implements Comparable<Category> {
    @SerializedName("category_id")
    String categoryId;
    @SerializedName("cat_name")
    private String catName;
    @SerializedName("language")
    private String language;
    @SerializedName("selection_id")
    private String selectionId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
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
    public int compareTo(Category another) {
        return catName.compareTo(another.getCatName());
    }

}
