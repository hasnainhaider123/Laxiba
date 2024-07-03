package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh on 25.07.16.
 */
public class SubCategory implements Comparable<SubCategory> {

    @SerializedName("subcategory_id")
    String subcatId;
    @SerializedName("subcat_name")
    String subcatName;
    @SerializedName("cat_id")
    String catId;
    @SerializedName("language")
    private String language;
    @SerializedName("selection_id")
    private String selectionId;

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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
    public int compareTo(SubCategory another) {
        return subcatName.compareTo(another.getSubcatName());
    }
}
