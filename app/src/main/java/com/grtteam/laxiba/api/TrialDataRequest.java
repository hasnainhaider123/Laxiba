package com.grtteam.laxiba.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.grtteam.laxiba.entity.DataResponce;
import com.grtteam.laxiba.util.GsonHelper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by oleh on 28.07.16.
 */
public class TrialDataRequest extends BaseRequest {

    private Call<DataResponce> request;


    public TrialDataRequest(Context context, CallbackListener callbackListener) {
        super(context, DataService.class, callbackListener);

        request = ((DataService) getService()).getData();
    }

    @Override
    public void executeAsync() {
        request.enqueue(this);
    }

    @Override
    public void cancel() {
        request.cancel();
    }

    private interface DataService {
        @POST("get_trial_data")
        Call<DataResponce> getData();
    }

}
