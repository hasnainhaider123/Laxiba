package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleh on 02.08.16.
 */
public class SelectionResponce {


    @SerializedName("has_data")
    private boolean hasData;

    @SerializedName("selections")
    private List<SelectionSet> selectionSets;

    @SerializedName("new_selection_id")
    private String newSelectionId;

    public boolean hasData() {
        return hasData;
    }

    public List<SelectionSet> getSelectionSets() {
        return selectionSets;
    }

    public String getNewSelectionId() {
        return newSelectionId;
    }
}
