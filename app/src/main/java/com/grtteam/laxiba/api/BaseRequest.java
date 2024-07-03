package com.grtteam.laxiba.api;

import android.content.Context;

import com.grtteam.laxiba.LaxibaApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public abstract class BaseRequest<T> implements Callback<T> {

    private Retrofit retrofit;
    private Context mContext;
    private T service;
    private CallbackListener<T> callbackListener;
    private static final String CANCELED_MSG = "Canceled";

    public interface CallbackListener<T> {

        void success(Response<T> response, Retrofit retrofit);

        void failure(Throwable t);

        void error(Response<T> error);

    }

    public Context getContext() {
        return mContext;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public BaseRequest(Context context, Class<T> request, CallbackListener<T> callbackListener) {
        retrofit = LaxibaApplication.retrofit();

        this.mContext = context;
        this.callbackListener = callbackListener;

        service = retrofit.create(request);
    }

    public T getService() {
        return service;
    }

    public abstract void executeAsync();

    public abstract void cancel();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (callbackListener != null) {
            if (response.isSuccessful()) {
                callbackListener.success(response, retrofit);
            } else {
                callbackListener.error(response);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!CANCELED_MSG.equals(t.getMessage()) && callbackListener != null) {     // Don't show if request was canceled
            callbackListener.failure(t);
        }
    }

}
