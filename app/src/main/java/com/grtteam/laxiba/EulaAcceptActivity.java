package com.grtteam.laxiba;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;


import com.grtteam.laxiba.databinding.ActivityAcceptEulaBinding;
import com.grtteam.laxiba.util.LanguageCodesHelper;
import com.grtteam.laxiba.util.SharedPreferenceHelper;


import java.util.Locale;



/**
 * Created by oleh on 18.07.16.
 */
public class EulaAcceptActivity extends BaseActivity {
    ActivityAcceptEulaBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceptEulaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        CharSequence sequence = Html.fromHtml(getString(R.string.eula_contents));
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        UnderlineSpan[] underlines = strBuilder.getSpans(0, sequence.length(), UnderlineSpan.class);
        for(UnderlineSpan span : underlines) {
            int start = strBuilder.getSpanStart(span);
            int end = strBuilder.getSpanEnd(span);
            int flags = strBuilder.getSpanFlags(span);
            ClickableSpan myActivityLauncher = new ClickableSpan() {
                public void onClick(View view) {
                    EulaAcceptActivity.this.startActivity(new Intent(EulaAcceptActivity.this, EulaActivity.class));
                }
            };

            strBuilder.setSpan(myActivityLauncher, start, end, flags);
        }

        binding.eulaContent.setText(strBuilder);
        binding.eulaContent.setMovementMethod(LinkMovementMethod.getInstance());

        binding.acceptEulaCheckbox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (compoundButton, checked) -> {
            if(checked){
                binding.eulaDemo.setVisibility(View.VISIBLE);
                binding.eulaFull.setVisibility(View.VISIBLE);
            } else {
                binding.eulaDemo.setVisibility(View.GONE);
                binding.eulaFull.setVisibility(View.GONE);
            }
        });

        if (SharedPreferenceHelper.getIntroStep() != SharedPreferenceHelper.STEP_EULA) {
            // allow navigation back if already accepted
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //hide accept button
            binding.eulaDemo.setVisibility(View.GONE);
            binding.eulaFull.setVisibility(View.GONE);
            binding.acceptEulaCheckbox.setVisibility(View.GONE);
        }
        binding.eulaFull.setOnClickListener(view -> onFulVersion());
        binding.eulaDemo.setOnClickListener(view -> onDemoClicked());
    }

    void onFulVersion() {
        if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_EULA) {
            SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_SUBSCRIPTION);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    void onDemoClicked(){
        //start demo selection
        SharedPreferenceHelper.saveIsDemo(true);
        SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_SELECTION);

        //set language
        String deviceLang = Locale.getDefault().getLanguage();
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

        SharedPreferenceHelper.saveLanguage(LanguageCodesHelper.androidToApi(lang));  //e
        ((LaxibaApplication) EulaAcceptActivity.this.getApplication()).setupCustomLocale(lang); //en

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.putExtra("DEMO", true);
        startActivity(i);
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
