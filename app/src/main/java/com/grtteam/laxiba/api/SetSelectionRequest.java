package com.grtteam.laxiba.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.grtteam.laxiba.entity.SelectionResponce;
import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.util.GsonHelper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by oleh on 29.07.16.
 */
public class SetSelectionRequest extends BaseRequest {
    private Call<SelectionResponce> request;


    public SetSelectionRequest(Context context, String uid, SelectionSet selectionSet, BaseRequest.CallbackListener callbackListener) {
        super(context, SelectionService.class, callbackListener);

        SetSelectionRequestBody params = new SetSelectionRequestBody(
                uid,
                selectionSet.getSelectionName(),
                selectionSet.getIbs(),
                selectionSet.getLactose(),
                selectionSet.getFructose(),
                selectionSet.getSorbitol(),
                selectionSet.getFructansAndGalactans()
        );
        String json = GsonHelper.getGsonNewInstance().toJson(params);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        request = ((SelectionService) getService()).setSelection(body);

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

        @POST("set_selection")
        Call<SelectionResponce> setSelection(@Body RequestBody requestBody);
    }


    private class SetSelectionRequestBody {

        @SerializedName("uid")
        String uid;

        @SerializedName("selection_name")
        String selectionName;

        @SerializedName("ibs")
        String ibs;

        @SerializedName("lactose")
        String lactouse;

        @SerializedName("fructose")
        String fructouse;

        @SerializedName("sorbitol")
        String sorbitol;

        @SerializedName("fructans_and_galactans")
        String fructouseLactouse;


        public SetSelectionRequestBody(String uid, String selectionName, String ibs, String lactouse, String fructouse, String sorbitol, String fructouseLactouse) {
            this.uid = uid;
            this.selectionName = selectionName;
            this.ibs = ibs;
            this.lactouse = lactouse;
            this.fructouse = fructouse;
            this.sorbitol = sorbitol;
            this.fructouseLactouse = fructouseLactouse;
        }
    }
}
