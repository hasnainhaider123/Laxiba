package com.grtteam.laxiba;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.util.LanguageCodesHelper;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oleh on 18.07.16.
 */
public class LaxibaApplication extends Application {

    // context can be static here because Application class exists always.
    private static Context context;
    private static Retrofit retrofit;

    private Locale locale = null;

    public static Context getContext() {
        return context;
    }

    public static Retrofit retrofit() {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        if (SharedPreferenceHelper.getLanguage() == null) {
            saveLangToPrefs();
        } else {
            setupCustomLocale(LanguageCodesHelper.apiToAndroid(SharedPreferenceHelper.getLanguage()));
        }
    }

    private void saveLangToPrefs() {
        Configuration config = getBaseContext().getResources().getConfiguration();

        String deviceLang = config.locale.getLanguage();
        String lang = "en";
        if(deviceLang.startsWith("de")) {
            lang = "de";
        }  else if(deviceLang.startsWith("fr")) {
            lang = "fr";
        }  else if(deviceLang.startsWith("it")) {
            lang = "it";
        }  else if(deviceLang.startsWith("es")) {
            lang = "es";
        }

        SharedPreferenceHelper.saveLanguage(LanguageCodesHelper.androidToApi(lang));
    }

    public Configuration setupCustomLocale(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();

        if (!TextUtils.isEmpty(lang) && !config.locale.getLanguage().equals(lang)) {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
//
        return config;
    }

}
