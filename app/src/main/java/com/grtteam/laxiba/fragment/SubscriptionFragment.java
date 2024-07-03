package com.grtteam.laxiba.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.grtteam.laxiba.LaxibaApplication;
import com.grtteam.laxiba.MainActivity;
import com.grtteam.laxiba.R;
import com.grtteam.laxiba.adapter.LanguageAdapter;
import com.grtteam.laxiba.adapter.SelectionAdapter;
import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.databinding.FragmentSubscriptionBinding;
import com.grtteam.laxiba.entity.SelectionValue;
import com.grtteam.laxiba.util.LanguageCodesHelper;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.Locale;



/**
 * Created by oleh on 18.07.16.
 */
public class SubscriptionFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    FragmentSubscriptionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubscriptionBinding.inflate(inflater,container,false);
        View v = binding.getRoot();
        validateButtons();

        binding.languagePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lang = LanguageCodesHelper.apiToAndroid(((SelectionValue) binding.languagePicker.getSelectedItem()).getCode());
                Configuration config = getActivity().getResources().getConfiguration();

                if (!TextUtils.isEmpty(lang) && !config.locale.getLanguage().equals(lang)) {
                    ((LaxibaApplication) getActivity().getApplication()).setupCustomLocale(lang);
                    SharedPreferenceHelper.saveLanguage(((SelectionValue) binding.languagePicker.getSelectedItem()).getCode());
                    getActivity().recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.languagePicker.setAdapter(new LanguageAdapter(getContext(), API.languages));
        // Enable language
        binding.languagePicker.setEnabled(true);
        ((SelectionAdapter) binding.languagePicker.getAdapter()).setEnabled(true);

        // Set current language as selected one
        Locale current = getResources().getConfiguration().locale;
        switch (current.getLanguage()) {
            case LanguageCodesHelper.LANG_ENGLISH:
                binding.languagePicker.setSelection(0);
                break;
            case LanguageCodesHelper.LANG_FRANCH:
                binding.languagePicker.setSelection(1);
                break;
            case LanguageCodesHelper.LANG_ITALIAN:
                binding.languagePicker.setSelection(2);
                break;
            case LanguageCodesHelper.LANG_SPANISH:
                binding.languagePicker.setSelection(3);
                break;
            case LanguageCodesHelper.LANG_GERMAN:
                binding.languagePicker.setSelection(4);
                break;
            default:
                // English for not supported languages
                binding.languagePicker.setSelection(0);
        }

        setPrices();
        binding.buyYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNew(view);
            }
        });
        binding.buyMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNew(view);
            }
        });
        return v;
    }
    private void setPrices(){
        String monthlyPrice = SharedPreferenceHelper.getMonthlyPrice();
        if (monthlyPrice == null ) {
            monthlyPrice = getString(R.string.monthly_price);
        }
        binding.monthlyPrice.setText(monthlyPrice);

        String yearlyPrice = SharedPreferenceHelper.getYearlyPrice();
        if (yearlyPrice == null) {
            yearlyPrice = getString(R.string.yearly_price);
        }
        binding.yearlyPrice.setText(yearlyPrice);
    }
    @Override
    public void onResume() {
        super.onResume();
//        SharedPreferenceHelper.registerSharedPreferenceChangedListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
//        SharedPreferenceHelper.unregisterSharedPreferenceChangedListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (((MainActivity) getActivity()).getIabHelper() != null) {
//            ((MainActivity) getActivity()).getIabHelper().handleActivityResult(requestCode, resultCode, data);
//        }
    }

    void onClickNew(View v) {

        ((MainActivity) getActivity()).onClick(v);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SharedPreferenceHelper.MONTHLY) || key.equals(SharedPreferenceHelper.YEARLY)) {
            validateButtons();
        }
    }

    private void validateButtons() {
        if (SharedPreferenceHelper.getIsMonthly()) {
            binding.buyMonthly.setText(getString(R.string.btn_continue));
            binding.buyYearly.setText(getString(R.string.upgrade));
        } else if (SharedPreferenceHelper.getIsYearly()) {
            binding.buyMonthly.setText(getString(R.string.downgrade));
            binding.buyYearly.setText(getString(R.string.btn_continue));
        }
    }

}
