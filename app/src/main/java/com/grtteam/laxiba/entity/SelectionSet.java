package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh on 29.07.16.
 */
public class SelectionSet {

    @SerializedName("selection_id")
    private String selectionId;

    @SerializedName("selection_name")
    private String selectionName;

    @SerializedName("ibs")
    private String ibs;

    @SerializedName("sorbitol")
    private String sorbitol;

    @SerializedName("lactose")
    private String lactose;

    @SerializedName("fructans_and_galactans")
    private String fructansAndGalactans;

    @SerializedName("fructose")
    private String fructose;

    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }


    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public String getIbs() {
        return ibs;
    }

    public void setIbs(String ibs) {
        this.ibs = ibs;
    }

    public String getSorbitol() {
        return sorbitol;
    }

    public void setSorbitol(String sorbitol) {
        this.sorbitol = sorbitol;
    }

    public String getLactose() {
        return lactose;
    }

    public void setLactose(String lactose) {
        this.lactose = lactose;
    }

    public String getFructansAndGalactans() {
        return fructansAndGalactans;
    }

    public void setFructansAndGalactans(String fructansAndGalactans) {
        this.fructansAndGalactans = fructansAndGalactans;
    }

    public String getFructose() {
        return fructose;
    }

    public void setFructose(String fructose) {
        this.fructose = fructose;
    }
}
