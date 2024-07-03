package com.grtteam.laxiba.entity;

/**
 * Created by oleh on 22.07.16.
 */
public class SelectionValue {

    private String code;
    private int nameResourceId;

    public SelectionValue(String code, int nameResourceId) {
        this.code = code;
        this.nameResourceId = nameResourceId;
    }

    public int getNameResourceId() {
        return nameResourceId;
    }

    public String getCode() {
        return code;
    }

}
