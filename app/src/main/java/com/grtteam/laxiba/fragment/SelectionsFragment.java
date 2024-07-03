package com.grtteam.laxiba.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grtteam.laxiba.MainActivity;
import com.grtteam.laxiba.R;
import com.grtteam.laxiba.SelectionActivity;
import com.grtteam.laxiba.SelectionViewActivity;
import com.grtteam.laxiba.adapter.SelectionSetsAdapter;
import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.api.BaseRequest;
import com.grtteam.laxiba.api.GetSelectionRequest;
import com.grtteam.laxiba.billing.products.SKUMiniFabric;
import com.grtteam.laxiba.databinding.FragmentSelectionsBinding;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.DataResponce;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SelectionResponce;
import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.entity.SubCategory;
import com.grtteam.laxiba.sqlite.CategoryDataSource;
import com.grtteam.laxiba.sqlite.FoodDataSource;
import com.grtteam.laxiba.sqlite.SQLiteHelper;
import com.grtteam.laxiba.sqlite.SelectionSetDataSource;
import com.grtteam.laxiba.sqlite.SubCategoryDataSource;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by oleh on 18.07.16.
 */
public class SelectionsFragment extends Fragment {
    FragmentSelectionsBinding binding;
    private BaseRequest request;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectionsBinding.inflate(inflater,container,false);
        View v = binding.getRoot();

        setHasOptionsMenu(true);

        if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_COMPLETED && SharedPreferenceHelper.getIsDemo()) {
            binding.demoBanner.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.demoBanner.getRoot().setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final List<SelectionSet> selectionSets = new ArrayList<>();

        if (SharedPreferenceHelper.getIsDemo()) {
            // Display only test selection
            SelectionSet selectionSet = new SelectionSet();
            selectionSet.setSelectionId(API.TRIAL_CATEGORY_ID);
            selectionSet.setSelectionName(getString(R.string.demo_selection_name));

            selectionSets.add(selectionSet);

        } else {
            // Display list of selections
            SQLiteDatabase database = new SQLiteHelper(getContext()).getReadableDatabase();
            selectionSets.addAll(SelectionSetDataSource.getAllSelectionSets(database));
            database.close();

        }

        binding.selectionsIst.setAdapter(new SelectionSetsAdapter(getActivity(), selectionSets, itemId -> {
            SharedPreferenceHelper.saveActiveSelectionId(itemId);
            ((MainActivity) getActivity()).openSearchScreen();
        }));

        binding.selectionsIst.setOnItemClickListener((parent, view1, position, id) -> {
            Intent i = new Intent(getContext(), SelectionViewActivity.class);
            i.putExtra(SelectionViewActivity.EXTRA_SELECTION_ID, selectionSets.get(position).getSelectionId());
            startActivity(i);
        });
        binding.demoBanner.getRoot().setOnClickListener(view12 -> onBannerClick());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.selections_screen_menu, menu);

        SQLiteDatabase database = new SQLiteHelper(getContext()).getReadableDatabase();
        int selectonsCount = SelectionSetDataSource.getSelectionsCount(database);
        database.close();
        boolean showAddBtn = !SharedPreferenceHelper.getIsDemo() && (
                SharedPreferenceHelper.getPurchasedSelections() > selectonsCount ||        // There are purchased selections but not used
                        SharedPreferenceHelper.getPurchasedSelections() < 5);              // Selection purchase available

        menu.findItem(R.id.menu_item_add_selection).setVisible(showAddBtn);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_selection) {
            SQLiteDatabase database = new SQLiteHelper(getContext()).getReadableDatabase();
            int selectonsCount = SelectionSetDataSource.getSelectionsCount(database);
            database.close();
            if (SharedPreferenceHelper.getPurchasedSelections() > selectonsCount) {
                // There are purchased selections but not used
                // Open new selection screen
                Intent selectionIntent = new Intent(getContext(), SelectionActivity.class);
                startActivity(selectionIntent) ;
            } else {
                // start purchase flow
                String purchaseSku;
                switch (SharedPreferenceHelper.getPurchasedSelections()) {
                    case 1:
                        // Buy second
                        purchaseSku = SKUMiniFabric.SELECTION2_PURCHASE;
                        break;
                    case 2:
                        // Buy third
                        purchaseSku = SKUMiniFabric.SELECTION3_PURCHASE;
                        break;
                    case 3:
                        // Buy fourth
                        purchaseSku = SKUMiniFabric.SELECTION4_PURCHASE;
                        break;
                    case 4:
                        // Buy fifth
                        purchaseSku = SKUMiniFabric.SELECTION5_PURCHASE;
                        break;
                    default:
                        return false;
                }
                ((MainActivity) getActivity()).purchase(purchaseSku);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (request != null) {
            request.cancel();
        }
    }

    void onBannerClick() {
        ((MainActivity) getActivity()).openSubscriptionScreen();
    }

    private BaseRequest.CallbackListener selectionsListener = new BaseRequest.CallbackListener<SelectionResponce>() {
        @Override
        public void success(Response<SelectionResponce> response, Retrofit retrofit) {
            SelectionResponce selectionResponce = response.body();

            SQLiteDatabase database = new SQLiteHelper(getContext()).getWritableDatabase();
            SelectionSetDataSource.saveSelectionSets(database, selectionResponce.getSelectionSets());

            database.close();

            ((MainActivity) getActivity()).showContent();
            ((MainActivity) getActivity()).openSearchScreen();
        }

        @Override
        public void failure(Throwable t) {
            ((MainActivity) getActivity()).showContent();
            Toast.makeText(getContext(), R.string.network_error_msg, Toast.LENGTH_LONG).show();

        }

        @Override
        public void error(Response error) {
            ((MainActivity) getActivity()).showContent();
            Toast.makeText(getContext(), R.string.network_error_msg, Toast.LENGTH_LONG).show();

        }
    };


    private BaseRequest.CallbackListener dataListener = new BaseRequest.CallbackListener<DataResponce>() {
        @Override
        public void success(final Response<DataResponce> response, Retrofit retrofit) {

            DataResponce dataResponce = response.body();

            SQLiteDatabase database = new SQLiteHelper(getContext()).getWritableDatabase();
            database.beginTransaction();

            CategoryDataSource.clear(database);
            for (Category category : dataResponce.getCategories()) {
                CategoryDataSource.saveCategory(database, category);
            }

            SubCategoryDataSource.clear(database);
            for (SubCategory subCategory : dataResponce.getSubCategories()) {
                SubCategoryDataSource.saveSubCategory(database, subCategory);
            }

            FoodDataSource.clear(database);
            for (Food food : dataResponce.getFoods()) {
                FoodDataSource.saveFood(database, food);
            }

            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();


            request = new GetSelectionRequest(getContext(), SharedPreferenceHelper.getSubscriptionUid(), selectionsListener);
            request.executeAsync();
        }

        @Override
        public void failure(Throwable t) {
            ((MainActivity) getActivity()).showContent();
            Toast.makeText(getContext(), R.string.network_error_msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void error(Response error) {
            ((MainActivity) getActivity()).showContent();
            Toast.makeText(getContext(), R.string.network_error_msg, Toast.LENGTH_LONG).show();
        }
    };

}
