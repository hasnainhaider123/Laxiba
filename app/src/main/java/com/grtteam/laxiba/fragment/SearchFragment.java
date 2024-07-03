package com.grtteam.laxiba.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.grtteam.laxiba.MainActivity;
import com.grtteam.laxiba.R;
import com.grtteam.laxiba.adapter.SearchAdapter;
import com.grtteam.laxiba.adapter.SelectableListAdapter;
import com.grtteam.laxiba.api.API;
import com.grtteam.laxiba.databinding.FragmentSearchBinding;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SubCategory;
import com.grtteam.laxiba.listeners.ItemClickListener;
import com.grtteam.laxiba.listeners.OnBackClickListener;
import com.grtteam.laxiba.sqlite.CategoryDataSource;
import com.grtteam.laxiba.sqlite.FoodDataSource;
import com.grtteam.laxiba.sqlite.SQLiteHelper;
import com.grtteam.laxiba.sqlite.SubCategoryDataSource;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by oleh on 18.07.16.
 */
public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;
    SearchAdapter searchAdapter;
    View demoBanner;
    ListView searchResults;
    RecyclerView listRecyclerView;
    SearchView searchView;
    private SQLiteOpenHelper sqliteHelper;
    private ArrayList<Category> categories;
    private HashMap<Category, List<Object>> subCategories;    // Object can be SubCategory or Food
    private HashMap<SubCategory, List<Food>> foods;
    private SelectableListAdapter categoriesAdapter;
    private Stack<SelectableListAdapter> stackOfAdapters = new Stack<>();
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void itemClicked(int itemPosition) {
            if(stackOfAdapters.size() == 0){
                //1st click
                Object obj  = subCategories.get(categories.get(itemPosition));
                ArrayList<SubCategory> arr = (ArrayList<SubCategory>) obj;

                if (arr.size() > 1) {
                    //food list
                    ArrayList<Food> food = new ArrayList<>();
                    for(int i=1; i<arr.size(); i++){
                        Object item = arr.get(i);
                        food.add((Food)item);
                    }
                    SelectableListAdapter adapter = new SelectableListAdapter();
                    adapter.setListener(itemClickListener);
                    adapter.setFood(food);
                    listRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //change title
                    getActivity().setTitle(getString(R.string.food));
                    stackOfAdapters.add(adapter);
                } else {
                    //subcategories
                    Object obj2 = arr.get(0);
                    ArrayList<SubCategory> arr2 = (ArrayList<SubCategory>) obj2;
                    SelectableListAdapter adapter = new SelectableListAdapter();
                    adapter.setListener(itemClickListener);
                    adapter.setSubcategories(arr2);
                    listRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //change title
                    getActivity().setTitle(getString(R.string.subcategories));
                    stackOfAdapters.add(adapter);
                }

            } else {
                SelectableListAdapter parentAdapter = stackOfAdapters.pop();
                stackOfAdapters.add(parentAdapter);
                ArrayList<SubCategory> sbc = parentAdapter.getSubcategories();

                Object obj = sbc.get(itemPosition);
                SubCategory cat = (SubCategory) obj;

                ArrayList<Food> foodT = (ArrayList<Food>) foods.get(cat);
                SelectableListAdapter adapter = new SelectableListAdapter();
                adapter.setListener(itemClickListener);
                adapter.setFood(foodT);
                listRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //change title
                getActivity().setTitle(getString(R.string.food));
                stackOfAdapters.add(adapter);
            }
        }
    };
    private OnBackClickListener onBackClickListener = new OnBackClickListener() {
        @Override
        public boolean onBackClicked() {
            if(stackOfAdapters.size() > 0){
                stackOfAdapters.pop();
                if(stackOfAdapters.size() == 0){
                    listRecyclerView.setAdapter(categoriesAdapter);
                    categoriesAdapter.notifyDataSetChanged();
                    getActivity().setTitle(getString(R.string.categories));
                } else {
                    SelectableListAdapter adapter = stackOfAdapters.pop();
                    stackOfAdapters.add(adapter);
                    listRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    getActivity().setTitle(getString(R.string.subcategories));
                }
                return false;
            } else
                return true;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        sqliteHelper = new SQLiteHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        View v = binding.getRoot(); //inflater.inflate(R.layout.fragment_search, container, false);
        demoBanner = v.findViewById(R.id.demo_banner);
        searchView = v.findViewById(R.id.search_view);
        listRecyclerView = v.findViewById(R.id.listRecyclerView);
        searchResults = v.findViewById(R.id.search_results);
        setupSearchView(v);

        if (SharedPreferenceHelper.getIsDemo()) {
            demoBanner.setVisibility(View.VISIBLE);
        } else {
            demoBanner.setVisibility(View.GONE);
        }
        getActivity().setTitle(getString(R.string.categories));

        getActivity().runOnUiThread(() -> {
            // preparing list data
            prepareListData();

            categoriesAdapter = new SelectableListAdapter();
            categoriesAdapter.setCategories(categories);
            categoriesAdapter.setListener(itemClickListener);
            listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            listRecyclerView.setAdapter(categoriesAdapter);

            searchAdapter = new SearchAdapter(getContext());
            searchResults.setAdapter(searchAdapter);

            displayAllItems();
        });
        demoBanner.setOnClickListener(view -> onBannerClick());
        return v;
    }

    public void setupSearchView(View v) {
        searchView.setQueryHint(getString(R.string.enter_food_name));
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    displayAllItems();
                } else {
                    performSearch(query);
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    displayAllItems();
                } else {
                    performSearch(newText);
                }
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            displayAllItems();
            return false;
        });

    }

    void onBannerClick() {
        ((MainActivity) getActivity()).openSubscriptionScreen();
    }

    private void performSearch(String query) {
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        List<Food> results = FoodDataSource.findFood(database, query, SharedPreferenceHelper.getLanguage(), SharedPreferenceHelper.getActiveSelectionId());
        database.close();

        searchAdapter.setItems(results);
        searchAdapter.notifyDataSetChanged();

        searchResults.setVisibility(View.VISIBLE);
        listRecyclerView.setVisibility(View.GONE);
    }

    private void displayAllItems() {
        searchResults.setVisibility(View.GONE);
        listRecyclerView.setVisibility(View.VISIBLE);
    }


    private void prepareListData() {
        categories = new ArrayList<>();
        subCategories = new HashMap<>();
        foods = new HashMap<>();

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();

        categories = CategoryDataSource.getAllCategories(database, SharedPreferenceHelper.getLanguage(), SharedPreferenceHelper.getActiveSelectionId());
        Collections.sort(categories);

        int count = 0;

        // Put trial category at the top
        for (int i = 0; i < categories.size(); i++) {
            Category trialCategory = categories.get(i);
            if (trialCategory.getCategoryId().equals(API.TRIAL_CATEGORY_ID)) {
                categories.remove(i);
                categories.add(0, trialCategory);
                break;
            }
        }

        for (Category category : categories) {
            List<SubCategory> sc = SubCategoryDataSource.getSubCategories(database, category.getCategoryId(), SharedPreferenceHelper.getLanguage(), SharedPreferenceHelper.getActiveSelectionId());
            Collections.sort(sc);

            for (SubCategory subCategory : sc) {
                List<Food> subFoods = FoodDataSource.getFood(database, subCategory.getSubcatId(), SharedPreferenceHelper.getLanguage(), SharedPreferenceHelper.getActiveSelectionId());
                Collections.sort(subFoods);
                foods.put(subCategory, subFoods);

                count += subFoods.size();
            }

            List<Food> sf = FoodDataSource.getFood(database, category.getCategoryId(), SharedPreferenceHelper.getLanguage(), SharedPreferenceHelper.getActiveSelectionId());
            Collections.sort(sf);

            List<Object> items = new ArrayList<>();
            items.add(sc);
            items.addAll(sf);

            count += sf.size();

            subCategories.put(category, items);
        }

        Log.d("--!!--", "foodCount: " + count);
        Log.d("--!!--", "actualFoodCount: " + FoodDataSource.getFoodCount(database, SharedPreferenceHelper.getLanguage()));

        database.close();
    }


    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }
}
