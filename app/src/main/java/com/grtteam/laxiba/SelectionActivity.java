package com.grtteam.laxiba;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.grtteam.laxiba.adapter.SelectionAdapter;
import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.api.BaseRequest;
import com.grtteam.laxiba.api.SetSelectionRequest;
import com.grtteam.laxiba.api.TrialDataRequest;
import com.grtteam.laxiba.databinding.ActivitySelectionBinding;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.DataResponce;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SelectionResponce;
import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.entity.SelectionValue;
import com.grtteam.laxiba.entity.SubCategory;
import com.grtteam.laxiba.sqlite.CategoryDataSource;
import com.grtteam.laxiba.sqlite.FoodDataSource;
import com.grtteam.laxiba.sqlite.SQLiteHelper;
import com.grtteam.laxiba.sqlite.SelectionSetDataSource;
import com.grtteam.laxiba.sqlite.SubCategoryDataSource;
import com.grtteam.laxiba.util.SharedPreferenceHelper;


import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by oleh on 03.08.16.
 */
public class SelectionActivity extends BaseActivity {
    ActivitySelectionBinding binding;

    //@BindView(R.id.selection_name)
    //EditText selectionName;
    AppCompatSpinner[] selectors;
    private boolean loading;
    private BaseRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectors =  new AppCompatSpinner[]{binding.selector1,binding.selector2,binding.selector3,binding.selector4,binding.selector5};
        //ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(SharedPreferenceHelper.getIntroStep() != SharedPreferenceHelper.STEP_SELECTION);
        }

        setUpTitle();

        // Setup layout
        binding.selectionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpTitle();
            }
        });

        if (SharedPreferenceHelper.getIsDemo()) {
            binding.selectionName.setText(R.string.demo_selection_name);
            binding.selectionName.setEnabled(false);
        } else {
            binding.selectionName.setEnabled(true);
        }


        selectors[0].setAdapter(new SelectionAdapter(this, API.ibsValues));
        selectors[1].setAdapter(new SelectionAdapter(this, API.sorbitolValues));
        selectors[2].setAdapter(new SelectionAdapter(this, API.lactoseValues));
        selectors[3].setAdapter(new SelectionAdapter(this, API.fructansAndGalactans));
        selectors[4].setAdapter(new SelectionAdapter(this, API.fructoseValues));

        // IBS changed listener
        selectors[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 1; i < selectors.length; i++) {
                    selectors[i].setEnabled(!SharedPreferenceHelper.getIsDemo() && position != 0);
                    ((SelectionAdapter) selectors[i].getAdapter()).setEnabled(!SharedPreferenceHelper.getIsDemo() && position != 0);
                    ((SelectionAdapter) selectors[i].getAdapter()).notifyDataSetChanged();
                }
                if (position == 0) {

                    // set get table 0101
                    selectors[1].setSelection(3);
                    selectors[2].setSelection(2);
                    selectors[3].setSelection(3);
                    selectors[4].setSelection(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (SharedPreferenceHelper.getIsDemo()) {
            // Disable selectors
            for (int i = 0; i < selectors.length; i++) {
                selectors[i].setEnabled(false);
                ((SelectionAdapter) selectors[i].getAdapter()).setEnabled(false);
            }
        } else {
            // Enable everything
            for (int i = 0; i < selectors.length; i++) {
                selectors[i].setEnabled(true);
                ((SelectionAdapter) selectors[i].getAdapter()).setEnabled(true);
            }
        }

        if (savedInstanceState != null) {

            super.onRestoreInstanceState(savedInstanceState);
            binding.selectionName.setText(savedInstanceState.getString("name"));
            for (int i = 0; i < selectors.length; i++) {
                selectors[i].setSelection(savedInstanceState.getInt("selector" + i));
            }
        } else {

            // Fill selectors with data
            if (SharedPreferenceHelper.getIsDemo()) {
                // Set demo values
                selectors[0].setSelection(1);
                selectors[1].setSelection(0);
                selectors[2].setSelection(3);
                selectors[3].setSelection(2);
                selectors[4].setSelection(1);
            } else {
                // Fill with No
                selectors[0].setSelection(1);
                selectors[1].setSelection(1);
                selectors[2].setSelection(1);
                selectors[3].setSelection(1);
                selectors[4].setSelection(1);
            }
        }

        binding.submitBtn.setOnClickListener(view -> submit());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", binding.selectionName.getText().toString());
        for (int i = 0; i < selectors.length; i++) {
            outState.putInt("selector" + i, selectors[i].getSelectedItemPosition());
        }
    }

    private void setUpTitle() {
        if (getSupportActionBar() != null) {
            if (TextUtils.isEmpty(binding.selectionName.getText().toString())) {
                getSupportActionBar().setTitle(getString(R.string.settings_title));
            } else {
                getSupportActionBar().setTitle(getString(R.string.settings_title_pattern, binding.selectionName.getText().toString()));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void submit() {
        if (loading) {
            return;
        }
        if (!isOnline()) {
            Toast.makeText(this, R.string.no_connection_msg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(binding.selectionName.getText())) {
            Toast.makeText(this, R.string.enter_selection_name_msg, Toast.LENGTH_SHORT).show();
            binding.selectionName.requestFocus();
            return;
        }


        if (SharedPreferenceHelper.getIsDemo()) {

            // Just download demo data
            request = new TrialDataRequest(this, dataListener);
            loading = true;
            binding.progress.setVisibility(View.VISIBLE);
            request.executeAsync();

        } else {
            // Save selection
            SelectionSet selectionSet = new SelectionSet();
            selectionSet.setSelectionName(binding.selectionName.getText().toString());
            selectionSet.setIbs(((SelectionValue) selectors[0].getSelectedItem()).getCode());
            selectionSet.setLactose(((SelectionValue) selectors[1].getSelectedItem()).getCode());
            selectionSet.setFructose(((SelectionValue) selectors[2].getSelectedItem()).getCode());
            selectionSet.setSorbitol(((SelectionValue) selectors[3].getSelectedItem()).getCode());
            selectionSet.setFructansAndGalactans(((SelectionValue) selectors[4].getSelectedItem()).getCode());

            request = new SetSelectionRequest(this, SharedPreferenceHelper.getSubscriptionUid(), selectionSet, selectionListener);
            loading = true;
            binding.progress.setVisibility(View.VISIBLE);
            request.executeAsync();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading) {
            request.cancel();
            loading = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private BaseRequest.CallbackListener<SelectionResponce> selectionListener = new BaseRequest.CallbackListener<SelectionResponce>() {
        @Override
        public void success(Response<SelectionResponce> response, Retrofit retrofit) {
            SelectionResponce selectionResponce = response.body();

            SQLiteDatabase database = new SQLiteHelper(SelectionActivity.this).getWritableDatabase();
            SelectionSetDataSource.saveSelectionSets(database, selectionResponce.getSelectionSets());

            database.close();

            if (!TextUtils.isEmpty(selectionResponce.getNewSelectionId())) {
                SharedPreferenceHelper.saveActiveSelectionId(selectionResponce.getNewSelectionId());
            }

            loading = false;
            binding.progress.setVisibility(View.GONE);

            // set next step
            SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_INFO);
            Intent i = new Intent(SelectionActivity.this, InfoActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();

        }

        @Override
        public void failure(Throwable t) {
            t.printStackTrace();

            loading = false;
            binding.progress.setVisibility(View.GONE);

            Toast.makeText(SelectionActivity.this, R.string.network_error_msg, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void error(Response<SelectionResponce> error) {
            loading = false;
            binding.progress.setVisibility(View.GONE);

            Toast.makeText(SelectionActivity.this, R.string.network_error_msg, Toast.LENGTH_SHORT).show();

        }
    };

    private BaseRequest.CallbackListener<DataResponce> dataListener = new BaseRequest.CallbackListener<DataResponce>() {
        @Override
        public void success(Response<DataResponce> response, Retrofit retrofit) {


            new AsyncTask<DataResponce, Void, Void>() {

                @Override
                protected Void doInBackground(DataResponce... params) {
                    DataResponce dataResponce = params[0];

                    SQLiteDatabase database = new SQLiteHelper(SelectionActivity.this).getWritableDatabase();
                    database.beginTransaction();

                    CategoryDataSource.clear(database);
                    for (Category category : dataResponce.getCategories()) {
                        category.setSelectionId(API.TRIAL_CATEGORY_ID);
                        CategoryDataSource.saveCategory(database, category);
                    }

                    SubCategoryDataSource.clear(database);
                    for (SubCategory subCategory : dataResponce.getSubCategories()) {
                        subCategory.setSelectionId(API.TRIAL_CATEGORY_ID);
                        SubCategoryDataSource.saveSubCategory(database, subCategory);
                    }

                    FoodDataSource.clear(database);
                    for (Food food : dataResponce.getFoods()) {
                        food.setSelectionId(API.TRIAL_CATEGORY_ID);
                        FoodDataSource.saveFood(database, food);
                    }

                    database.setTransactionSuccessful();
                    database.endTransaction();
                    database.close();

                    SharedPreferenceHelper.saveActiveSelectionId(API.TRIAL_CATEGORY_ID);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    loading = false;
                    binding.progress.setVisibility(View.GONE);

                    // set next step
                    SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_INFO);
                    Intent i = new Intent(SelectionActivity.this, InfoActivity.class);
                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                    finish();
                }
            }.execute(response.body());

        }

        @Override
        public void failure(Throwable t) {
            t.printStackTrace();

            loading = false;
            binding.progress.setVisibility(View.GONE);

            Toast.makeText(SelectionActivity.this, R.string.network_error_msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void error(Response<DataResponce> error) {

            loading = false;
            error.message();
            binding.progress.setVisibility(View.GONE);

            Toast.makeText(SelectionActivity.this, R.string.network_error_msg, Toast.LENGTH_SHORT).show();
        }
    };
}
