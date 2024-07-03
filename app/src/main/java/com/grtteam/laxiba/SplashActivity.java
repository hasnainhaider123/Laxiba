package com.grtteam.laxiba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getResources().getConfiguration().orientation == 2) {
            //set splash image for landscape
            ImageView imageView = findViewById(R.id.splashImageView);
            imageView.setImageDrawable(getDrawable(R.drawable.logo));

            //add padding
            float scale = getResources().getDisplayMetrics().density;
            int padding = (int) (150 * scale + 0.5f);
            imageView.setPadding(padding * 2, padding, padding * 2, padding);
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1000);
    }
}