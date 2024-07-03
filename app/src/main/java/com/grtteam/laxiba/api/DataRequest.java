package com.grtteam.laxiba.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.grtteam.laxiba.entity.DataResponce;
import com.grtteam.laxiba.util.GsonHelper;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by oleh on 28.07.16.
 */
public class DataRequest extends BaseRequest {

    private Call<DataResponce> request;


    public DataRequest(Context context, CallbackListener callbackListener) {
        super(context, DataService.class, callbackListener);

        DataRequestBody params = new DataRequestBody(
                SharedPreferenceHelper.getSubscriptionUid()
        );
        String json = GsonHelper.getGsonNewInstance().toJson(params);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        request = ((DataService) getService()).getData(body);
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
        @POST("get_data")
        Call<DataResponce> getData(@Body RequestBody requestBody);
    }

    private class DataRequestBody {

        @SerializedName("uid")
        String userId;

        public DataRequestBody(String userId) {
            this.userId = userId;
        }
    }

}
