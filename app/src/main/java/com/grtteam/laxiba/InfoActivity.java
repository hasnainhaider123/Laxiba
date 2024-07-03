package com.grtteam.laxiba;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.grtteam.laxiba.databinding.ActivityInfoBinding;
import com.grtteam.laxiba.util.SharedPreferenceHelper;


/**
 * Created by oleh on 18.07.16.
 */
public class InfoActivity extends BaseActivity {

    ActivityInfoBinding binding;
    //@BindView(R.id.info_continue)
    //Button contButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(SharedPreferenceHelper.getIntroStep() != SharedPreferenceHelper.STEP_INFO);

        if(SharedPreferenceHelper.getIntroStep() != SharedPreferenceHelper.STEP_INFO){
            binding.infoContinue.setVisibility(View.GONE);
        }
        binding.infoContinue.setOnClickListener(view -> onContinueClicked());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_info_menu, menu);

        menu.findItem(R.id.menu_item_close).setVisible(false);
        return true;
    }

    public void onContinueClicked(){
        if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_INFO) {
            SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_COMPLETED);
        }
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.menu_item_close:
                if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_INFO) {
                    SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_COMPLETED);
                }
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
