package com.grtteam.laxiba.api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.grtteam.laxiba.entity.SelectionResponce;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by oleh on 28.07.16.
 */
public class GetSelectionRequest extends BaseRequest {

    private Call<SelectionResponce> request;


    public GetSelectionRequest(Context context, String uid, CallbackListener callbackListener) {
        super(context, SelectionService.class, callbackListener);

        JsonObject json = new JsonObject();
        json.addProperty("uid", uid);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());

        request = ((SelectionService) getService()).getSelection(body);

    }

    @Override
    public void executeAsync() {
        request.enqueue(this);
    }

    @Override
    public void cancel() {
        request.cancel();
    }

    interface SelectionService {


        @POST("get_selections")
        Call<SelectionResponce> getSelection(@Body RequestBody requestBody);

    }
}
