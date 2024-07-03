package com.grtteam.laxiba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.grtteam.laxiba.util.LanguageCodesHelper;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale = new Locale(LanguageCodesHelper.apiToAndroid(SharedPreferenceHelper.getLanguage()));
        Configuration config = new Configuration(newBase.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        newBase.getResources().updateConfiguration(config, newBase.getResources().getDisplayMetrics());
        super.attachBaseContext(newBase);
    }
}