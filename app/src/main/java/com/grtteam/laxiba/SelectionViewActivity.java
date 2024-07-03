package com.grtteam.laxiba;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.databinding.ActivitySelectionViewBinding;
import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.sqlite.SQLiteHelper;
import com.grtteam.laxiba.sqlite.SelectionSetDataSource;


/**
 * Created by oleh on 05.08.16.
 */
public class SelectionViewActivity extends BaseActivity {

    public static final String EXTRA_SELECTION_ID = "extra_selection_id";
    ActivitySelectionViewBinding binding;
    TextView[] selectors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectors = new TextView[] {binding.selector1, binding.selector2, binding.selector3, binding.selector4, binding.selector5};


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (!getIntent().hasExtra(EXTRA_SELECTION_ID)) {
            finish();
        }
        String selectionId = getIntent().getStringExtra(EXTRA_SELECTION_ID);

        if (API.TRIAL_CATEGORY_ID.equals(selectionId)) {
            // Demo selection

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.demo_selection_name));
            }

            // Disable selectors
//            for (int i = 0; i < selectors.length; i++) {
//                selectors[i].setBackgroundResource(android.R.color.darker_gray);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    selectors[i].setTextColor(getResources().getColor(android.R.color.black, getTheme()));
//                } else {
//
//                    selectors[i].setTextColor(getResources().getColor(android.R.color.black));
//                }
//            }

            // Set demo values
            selectors[0].setText(R.string.val_no);
            selectors[1].setText(R.string.val_yes);
            selectors[2].setText(R.string.val_1);
            selectors[3].setText(R.string.val_0);
            selectors[4].setText(R.string.val_no);

        } else {

            SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();

            SelectionSet selectionSet = SelectionSetDataSource.getSelectionSet(database, selectionId);

            if (selectionSet != null) {

                if (getSupportActionBar() != null) {
                    if (TextUtils.isEmpty(selectionSet.getSelectionName())) {
                        getSupportActionBar().setTitle(getString(R.string.settings_title));
                    } else {
                        getSupportActionBar().setTitle(getString(R.string.settings_title_pattern, selectionSet.getSelectionName()));
                    }
                }

                selectors[0].setText(API.getValueResId(API.ibsValues, selectionSet.getIbs()));
                selectors[1].setText(API.getValueResId(API.lactoseValues, selectionSet.getLactose()));
                selectors[2].setText(API.getValueResId(API.fructoseValues, selectionSet.getFructose()));
                selectors[3].setText(API.getValueResId(API.sorbitolValues, selectionSet.getSorbitol()));
                selectors[4].setText(API.getValueResId(API.fructansAndGalactans, selectionSet.getFructansAndGalactans()));
            }

            database.close();
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
}
