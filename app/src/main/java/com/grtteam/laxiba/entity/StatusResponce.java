package com.grtteam.laxiba.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleh on 29.07.16.
 */
public class StatusResponce {

    public static final String STATUS_SUCCES = "success";
    public static final String STATUS_UPDATED = "updated";

    @SerializedName("status")
    String status;

    public String getStatus() {
        return status;
    }

    public boolean isSuccessful() {
        return  STATUS_SUCCES.equals(status) || STATUS_UPDATED.equals(status);
    }
}
