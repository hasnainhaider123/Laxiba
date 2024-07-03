package com.grtteam.laxiba;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.material.navigation.NavigationView;
import com.grtteam.laxiba.api.BaseRequest;
import com.grtteam.laxiba.api.DataRequest;
import com.grtteam.laxiba.api.GetSelectionRequest;
import com.grtteam.laxiba.billing.products.SKUMiniFabric;
import com.grtteam.laxiba.databinding.ActivityMainBinding;
import com.grtteam.laxiba.entity.Category;
import com.grtteam.laxiba.entity.DataResponce;
import com.grtteam.laxiba.entity.Food;
import com.grtteam.laxiba.entity.SelectionResponce;
import com.grtteam.laxiba.entity.SubCategory;
import com.grtteam.laxiba.fragment.SearchFragment;
import com.grtteam.laxiba.fragment.SelectionsFragment;
import com.grtteam.laxiba.fragment.SubscriptionFragment;
import com.grtteam.laxiba.listeners.OnBackClickListener;
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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {

    ActivityMainBinding binding;
    public static final int SHOW_INFO = 4;

    private static final int REQUEST_CODE_MONTHLY = 201;
    private static final int REQUEST_CODE_YEARLY = 202;
    private static final int REQUEST_CODE_DOWNGRADE = 203;
    private static final int REQUEST_CODE_UPGRADE = 204;
    private static final int REQUEST_CODE_PURCHASE_SELECTION = 205;

    /**
     * IN-APPS PURCHASE
     */
    private BillingClient mBillingClient;
    private long mLastPurchaseClickTime = 0;
    private List<String> mSkuList = new ArrayList<>();
    private List<SkuDetails> mSkuDetailsList = new ArrayList<>();

//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
//            = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result,
//                                             Inventory inventory) {
//            Log.d("Check purchase acquired", "Success: " + result.isSuccess());
//            // here is subscription checking
//            if (result.isSuccess() & inventory != null) {
//                if (inventory.getAllSubscriptions().size() == 0) {
//                    if (SharedPreferenceHelper.getIsDemo()) {
//                        nextIntroStep();
//                        return;
//                    }
//                    // No subscription but it was before
//                    SharedPreferenceHelper.saveIsDemo(true);
//
//                    // Clear purchase user identifier (in case it's another google user)
//                    SharedPreferenceHelper.clearSubscriptionUid();
//
//                    // Drop user to beginning of app
//                    SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_EULA);
//
//                    // Clear database
//                    SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
//                    database.beginTransaction();
//                    CategoryDataSource.clear(database);
//                    SubCategoryDataSource.clear(database);
//                    FoodDataSource.clear(database);
//                    database.setTransactionSuccessful();
//                    database.endTransaction();
//                    database.close();
//
//                    nextIntroStep();
//
//                } else {
//                    // Check subscriptions
//                    SharedPreferenceHelper.saveIsDemo(true);
//                    SharedPreferenceHelper.saveIsMonthly(false);
//                    SharedPreferenceHelper.saveIsYearly(false);
//
//                    if (inventory.hasPurchase(SKUMiniFabric.MONTHLY_SUBSCRIPTION)) {
//                        SharedPreferenceHelper.saveIsMonthly(true);
//                        SharedPreferenceHelper.saveIsDemo(false);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.MONTHLY_SUBSCRIPTION).getDeveloperPayload());
//                    }
//
//                    if (inventory.hasPurchase(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {
//                        SharedPreferenceHelper.saveIsYearly(true);
//                        SharedPreferenceHelper.saveIsDemo(false);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.YEARLY_SUBSCRIPTION).getDeveloperPayload());
//                    }
//                    SharedPreferenceHelper.saveMonthlyPrice(inventory.getSkuDetails(SKUMiniFabric.MONTHLY_SUBSCRIPTION).getPrice());
//                    SharedPreferenceHelper.saveYearlyPrice(inventory.getSkuDetails(SKUMiniFabric.YEARLY_SUBSCRIPTION).getPrice());
//
//                    // Check purchased selections
//                    if (inventory.hasPurchase(SKUMiniFabric.SELECTION5_PURCHASE)) {
//                        SharedPreferenceHelper.savePurchasedSelections(5);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.SELECTION5_PURCHASE).getDeveloperPayload());
//                    } else if (inventory.hasPurchase(SKUMiniFabric.SELECTION4_PURCHASE)) {
//                        SharedPreferenceHelper.savePurchasedSelections(4);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.SELECTION4_PURCHASE).getDeveloperPayload());
//                    } else if (inventory.hasPurchase(SKUMiniFabric.SELECTION3_PURCHASE)) {
//                        SharedPreferenceHelper.savePurchasedSelections(3);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.SELECTION3_PURCHASE).getDeveloperPayload());
//                    } else if (inventory.hasPurchase(SKUMiniFabric.SELECTION2_PURCHASE)) {
//                        SharedPreferenceHelper.savePurchasedSelections(2);
//                        SharedPreferenceHelper.saveSubscriptionUid(inventory.getPurchase(SKUMiniFabric.SELECTION2_PURCHASE).getDeveloperPayload());
//                    } else {
//                        SharedPreferenceHelper.savePurchasedSelections(1);
//                    }
//
//                    if (SharedPreferenceHelper.getIsDemo()) {
//                        // User has skus but subscriptions canceled and expired
//
//                        // Drop user to beginning of app
//                        SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_EULA);
//
//                        // Clear database
//                        SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
//                        database.beginTransaction();
//                        CategoryDataSource.clear(database);
//                        SubCategoryDataSource.clear(database);
//                        FoodDataSource.clear(database);
//                        database.setTransactionSuccessful();
//                        database.endTransaction();
//                        database.close();
//
//                        nextIntroStep();
//                    } else {
//                        checkServerForSelection();
//                    }
//                }
//
//            } else {
//                // If failed to check subscription just open next step
//                nextIntroStep();
//            }
//        }
//    };

    private ActionBarDrawerToggle toggle;
    //    IabHelper.OnIabPurchaseFinishedListener mSubscriptionFinishedListener
//            = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            if (result.isSuccess() & purchase != null) {
//                SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                if (purchase.isSku(SKUMiniFabric.MONTHLY_SUBSCRIPTION)) {
//                    SharedPreferenceHelper.saveIsDemo(false);
//                    SharedPreferenceHelper.saveIsMonthly(true);
//                } else {
//                    SharedPreferenceHelper.saveIsMonthly(false);
//                }
//
//                if (purchase.isSku(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {
//                    SharedPreferenceHelper.saveIsYearly(true);
//                    SharedPreferenceHelper.saveIsDemo(false);
//                } else {
//                    SharedPreferenceHelper.saveIsYearly(false);
//                }
//
//                checkServerForSelection();
//            }
//
//        }
//    };
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
//            = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            if (result.isSuccess() & purchase != null) {
//                SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                switch (purchase.getSku()) {
//                    case SKUMiniFabric.SELECTION2_PURCHASE:
//                        SharedPreferenceHelper.savePurchasedSelections(2);
//                        break;
//                    case SKUMiniFabric.SELECTION3_PURCHASE:
//                        SharedPreferenceHelper.savePurchasedSelections(3);
//                        break;
//                    case SKUMiniFabric.SELECTION4_PURCHASE:
//                        SharedPreferenceHelper.savePurchasedSelections(4);
//                        break;
//                    case SKUMiniFabric.SELECTION5_PURCHASE:
//                        SharedPreferenceHelper.savePurchasedSelections(5);
//                        break;
//                }
//
//                // Open selection screen
//                Intent selectionIntent = new Intent(MainActivity.this, SelectionActivity.class);
//                startActivity(selectionIntent);
//
//            }
//
//        }
//    };
//    private IabHelper mHelper;
    private GetSelectionRequest getSelectionRequest;
    private DataRequest dataRequest;
    private boolean destroyed;
    private void setupBillingClient() {
        mBillingClient = BillingClient
                .newBuilder(MainActivity.this)
                .enablePendingPurchases() // Useful for physical stores
                .setListener(this)
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    getAvailableProducts();

                    mBillingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {
                        if (!list.isEmpty()) {
                            for (Purchase purchase : list) {
                                if (purchase.isAcknowledged()) {
                                    // Check subscriptions
                                    SharedPreferenceHelper.saveIsDemo(true);
                                    SharedPreferenceHelper.saveIsMonthly(false);
                                    SharedPreferenceHelper.saveIsYearly(false);
                                    if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.MONTHLY_SUBSCRIPTION)) {
                                        SharedPreferenceHelper.saveIsMonthly(true);
                                        SharedPreferenceHelper.saveIsDemo(false);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    }

                                    if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {
                                        SharedPreferenceHelper.saveIsYearly(true);
                                        SharedPreferenceHelper.saveIsDemo(false);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    }
//                                        SharedPreferenceHelper.saveMonthlyPrice(inventory.getSkuDetails(SKUMiniFabric.MONTHLY_SUBSCRIPTION).getPrice());
//                                        SharedPreferenceHelper.saveYearlyPrice(inventory.getSkuDetails(SKUMiniFabric.YEARLY_SUBSCRIPTION).getPrice());

                                    // Check purchased selections
//                                        if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION5_PURCHASE)) {
//                                            SharedPreferenceHelper.savePurchasedSelections(5);
//                                            SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                                        } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION4_PURCHASE)) {
//                                            SharedPreferenceHelper.savePurchasedSelections(4);
//                                            SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                                        } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION3_PURCHASE)) {
//                                            SharedPreferenceHelper.savePurchasedSelections(3);
//                                            SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                                        } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION2_PURCHASE)) {
//                                            SharedPreferenceHelper.savePurchasedSelections(2);
//                                            SharedPreferenceHelper.saveSubscriptionUid(purchase.getDeveloperPayload());
//                                        } else {
//                                            SharedPreferenceHelper.savePurchasedSelections(1);
//                                        }

                                    if (SharedPreferenceHelper.getIsDemo()) {
                                        // User has skus but subscriptions canceled and expired
                                        // Drop user to beginning of app
                                        SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_EULA);

                                        // Clear database
                                        SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
                                        database.beginTransaction();
                                        CategoryDataSource.clear(database);
                                        SubCategoryDataSource.clear(database);
                                        FoodDataSource.clear(database);
                                        database.setTransactionSuccessful();
                                        database.endTransaction();
                                        database.close();

                                        nextIntroStep();
                                    } else {
                                        checkServerForSelection();
                                    }
                                }
                            }
                        } else {
                            //Not buy yet
                            if (SharedPreferenceHelper.getIsDemo()) {
                                nextIntroStep();
                                return;
                            }
                            // No subscription but it was before
                            SharedPreferenceHelper.saveIsDemo(true);

                            // Clear purchase user identifier (in case it's another google user)
                            SharedPreferenceHelper.clearSubscriptionUid();

                            // Drop user to beginning of app
                            SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_EULA);

                            // Clear database
                            SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
                            database.beginTransaction();
                            CategoryDataSource.clear(database);
                            SubCategoryDataSource.clear(database);
                            FoodDataSource.clear(database);
                            database.setTransactionSuccessful();
                            database.endTransaction();
                            database.close();

                            nextIntroStep();
                        }
                    });

                    mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, (billingResult12, list) -> {
                        if (!list.isEmpty()) {
                            for (Purchase purchase : list) {
                                if (purchase.isAcknowledged()) {
                                    if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION5_PURCHASE)) {
                                        SharedPreferenceHelper.savePurchasedSelections(5);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION4_PURCHASE)) {
                                        SharedPreferenceHelper.savePurchasedSelections(4);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION3_PURCHASE)) {
                                        SharedPreferenceHelper.savePurchasedSelections(3);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    } else if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.SELECTION2_PURCHASE)) {
                                        SharedPreferenceHelper.savePurchasedSelections(2);
                                        SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                                    } else {
                                        SharedPreferenceHelper.savePurchasedSelections(1);
                                    }
                                }
                            }
                        } else {
                            SharedPreferenceHelper.savePurchasedSelections(1);
                        }
                    });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                // TODO Note: It's strongly recommended that you implement your own connection retry policy and override the onBillingServiceDisconnected() method. Make sure you maintain the BillingClient connection when executing any methods.
                nextIntroStep();
            }
        });
    }
    private void getAvailableProducts() {
        if (mBillingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(mSkuList)
                    .setType(BillingClient.SkuType.SUBS)
                    .build();

            mBillingClient.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    mSkuDetailsList.addAll(skuDetailsList);
                    for (SkuDetails skuDetails : skuDetailsList) {
                        if (skuDetails.getSku().equalsIgnoreCase(SKUMiniFabric.MONTHLY_SUBSCRIPTION)) {
                            SharedPreferenceHelper.saveMonthlyPrice(skuDetails.getPrice());
                        } else if (skuDetails.getSku().equalsIgnoreCase(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {
                            SharedPreferenceHelper.saveYearlyPrice(skuDetails.getPrice());
                        }
                    }
                }
            });

            SkuDetailsParams params2 = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(mSkuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            mBillingClient.querySkuDetailsAsync(params2, (billingResult, skuDetailsList) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    mSkuDetailsList.addAll(skuDetailsList);
                }
            });
        }
    }
    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    }
    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            applyPurchase(purchase);
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, MainActivity.this);
            }
        }
    }
    private void applyPurchase(Purchase purchase) {
        runOnUiThread(() -> {
            if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.MONTHLY_SUBSCRIPTION)
                    || purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {

                SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.MONTHLY_SUBSCRIPTION)) {
                    SharedPreferenceHelper.saveIsDemo(false);
                    SharedPreferenceHelper.saveIsMonthly(true);
                } else {
                    SharedPreferenceHelper.saveIsMonthly(false);
                }

                if (purchase.getSkus().get(0).equalsIgnoreCase(SKUMiniFabric.YEARLY_SUBSCRIPTION)) {
                    SharedPreferenceHelper.saveIsYearly(true);
                    SharedPreferenceHelper.saveIsDemo(false);
                } else {
                    SharedPreferenceHelper.saveIsYearly(false);
                }

                checkServerForSelection();
            } else {
                SharedPreferenceHelper.saveSubscriptionUid(purchase.getSkus().get(0));
                switch (purchase.getSkus().get(0)) {
                    case SKUMiniFabric.SELECTION2_PURCHASE:
                        SharedPreferenceHelper.savePurchasedSelections(2);
                        break;
                    case SKUMiniFabric.SELECTION3_PURCHASE:
                        SharedPreferenceHelper.savePurchasedSelections(3);
                        break;
                    case SKUMiniFabric.SELECTION4_PURCHASE:
                        SharedPreferenceHelper.savePurchasedSelections(4);
                        break;
                    case SKUMiniFabric.SELECTION5_PURCHASE:
                        SharedPreferenceHelper.savePurchasedSelections(5);
                        break;
                }

                // Open selection screen
                Intent selectionIntent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(selectionIntent);
            }
        });

    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//            complain(getString(R.string.purchased_success));
        }
    }

    public void purchase(String sku) {
        // Mis-clicking prevention, using threshold of 3 seconds
        if (SystemClock.elapsedRealtime() - mLastPurchaseClickTime < 3000) {
//            Log.d(TAG, "Purchase click cancelled");
            return;
        }
        mLastPurchaseClickTime = SystemClock.elapsedRealtime();

        // Retrieve the SKU details
        for (SkuDetails skuDetails : mSkuDetailsList) {
            // Find the right SKU
            if (sku.equals(skuDetails.getSku())) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build();
                mBillingClient.launchBillingFlow(MainActivity.this, flowParams);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        destroyed = false;
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSkuList.add(SKUMiniFabric.MONTHLY_SUBSCRIPTION);
        mSkuList.add(SKUMiniFabric.YEARLY_SUBSCRIPTION);
        mSkuList.add(SKUMiniFabric.SELECTION2_PURCHASE);
        mSkuList.add(SKUMiniFabric.SELECTION3_PURCHASE);
        mSkuList.add(SKUMiniFabric.SELECTION4_PURCHASE);
        mSkuList.add(SKUMiniFabric.SELECTION5_PURCHASE);
        // Initialize the billing client
        setupBillingClient();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this,binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // Hide menu at first
//            toggle.setDrawerIndicatorEnabled(false);
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
           binding.appBarLayout.progress.setVisibility(View.GONE);
        }
//        setupIabHelper(true);

        if (getIntent().getBooleanExtra("DEMO", false)) {
            nextIntroStep();
        }
        binding.appBarLayout.retryBtn.setOnClickListener(view -> retryBtnClick());

    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
//        disposeIabHelper();

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (onBackClickListener != null && onBackClickListener.onBackClicked())
                super.onBackPressed();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (getIabHelper() != null) {
//            getIabHelper().handleActivityResult(requestCode, resultCode, data);
//        }
//    }

    public void openSubscriptionScreen() {
        showFragment(R.string.subscription_title, new SubscriptionFragment());
        binding.navView.setCheckedItem(R.id.nav_subscription);
    }

    public void openSelectionsScreen() {
        showFragment(R.string.selections_title, new SelectionsFragment());
        binding.navView.setCheckedItem(R.id.nav_selection);
    }

    private OnBackClickListener onBackClickListener;

    public void openSearchScreen() {
        SearchFragment fragment = new SearchFragment();
        onBackClickListener = fragment.getOnBackClickListener();
        showFragment(R.string.search, fragment);

        binding.navView.setCheckedItem(R.id.nav_search);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        hideKeyboard();

        onBackClickListener = null;

        if (id == R.id.nav_selection) {
            showFragment(R.string.selections_title, new SelectionsFragment());
        } else if (id == R.id.nav_search) {
            openSearchScreen();
        } else if (id == R.id.nav_subscription) {
            showFragment(R.string.subscription_title, new SubscriptionFragment());
        } else if (id == R.id.nav_information) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (id == R.id.nav_eula) {
            startActivity(new Intent(this, EulaActivity.class));
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int titleRes, Fragment fragment) {
        if (!destroyed && !isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
            setTitle(titleRes);
        }
    }

    public void nextIntroStep() {

        runOnUiThread(() -> {
            showContent();

            boolean showDrawer = SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_COMPLETED;
            toggle.setDrawerIndicatorEnabled(showDrawer);
            binding.drawerLayout.setDrawerLockMode(showDrawer ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            // Show Eula first time
            switch (SharedPreferenceHelper.getIntroStep()) {
                case SharedPreferenceHelper.STEP_EULA:
                    Intent i = new Intent(MainActivity.this, EulaAcceptActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                    finish();
                    break;
                case SharedPreferenceHelper.STEP_SUBSCRIPTION:
                    openSubscriptionScreen();
                    break;
                case SharedPreferenceHelper.STEP_SELECTION:
                    Intent selectionIntent = new Intent(MainActivity.this, SelectionActivity.class);
                    selectionIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(selectionIntent);
                    finish();
                    break;
                case SharedPreferenceHelper.STEP_INFO:
                    Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                    infoIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(infoIntent);
                    finish();
                    break;
                default:
                    openSearchScreen();
            }
        });


    }

    private void checkServerForSelection() {
        runOnUiThread(() -> {
            getSelectionRequest = new GetSelectionRequest(MainActivity.this, SharedPreferenceHelper.getSubscriptionUid(), new BaseRequest.CallbackListener<SelectionResponce>() {

                @Override
                public void success(Response<SelectionResponce> response, Retrofit retrofit) {
                    SelectionResponce selectionResponce = response.body();

                    SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
                    if (selectionResponce.hasData()) {
                        SelectionSetDataSource.saveSelectionSets(database, selectionResponce.getSelectionSets());

                        requestServerData();

                    } else {
                        SelectionSetDataSource.clear(database);
                        SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_SELECTION);
                        nextIntroStep();
                    }
                    database.close();
                }

                @Override
                public void failure(Throwable t) {
                    t.printStackTrace();

                    if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_COMPLETED ||
                            SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_INFO) {
                        // just keep working offline
                        nextIntroStep();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.network_error_msg, Toast.LENGTH_LONG).show();
                        showRetryButton();
                    }
                }

                @Override
                public void error(Response<SelectionResponce> error) {

                    if (SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_COMPLETED ||
                            SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_INFO) {
                        // just keep working offline
                        nextIntroStep();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.network_error_msg, Toast.LENGTH_LONG).show();
                        showRetryButton();
                    }
                }
            });
            showProgress(false, false);
            getSelectionRequest.executeAsync();
        });
    }

    public void requestServerData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataRequest = new DataRequest(MainActivity.this,
                        new BaseRequest.CallbackListener<DataResponce>() {
                            @Override
                            public void success(Response<DataResponce> response, Retrofit retrofit) {
                                new AsyncTask<DataResponce, Void, Void>() {

                                    @Override
                                    protected Void doInBackground(DataResponce... params) {
                                        DataResponce dataResponce = params[0];

                                        SQLiteDatabase database = new SQLiteHelper(MainActivity.this).getWritableDatabase();
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
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

                                        // set next step
                                        SharedPreferenceHelper.saveIntroStep(SharedPreferenceHelper.STEP_COMPLETED);
                                        nextIntroStep();
                                    }
                                }.execute(response.body());
                            }

                            @Override
                            public void failure(Throwable t) {
                                Toast.makeText(MainActivity.this, R.string.network_error_msg, Toast.LENGTH_LONG).show();
                                showRetryButton();
                            }

                            @Override
                            public void error(Response<DataResponce> error) {
                                Toast.makeText(MainActivity.this, R.string.network_error_msg, Toast.LENGTH_LONG).show();
                                showRetryButton();
                            }
                        }
                );
                showProgress(false, false);
                dataRequest.executeAsync();
            }
        });
    }

    void retryBtnClick() {
        if (!SharedPreferenceHelper.getIsDemo()) {
            checkServerForSelection();
        }
    }


    public void showProgress(boolean contentVsible, boolean menuVisible) {
        binding.appBarLayout.fragmentContainer.setVisibility(contentVsible ? View.VISIBLE : View.GONE);
        binding.appBarLayout.retryBtn.setVisibility(View.GONE);
        binding.appBarLayout.progress.setVisibility(View.VISIBLE);

        if (!menuVisible) {
            // Disable navigation
            toggle.setDrawerIndicatorEnabled(false);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void showRetryButton() {
        binding.appBarLayout.fragmentContainer.setVisibility(View.GONE);
        binding.appBarLayout.retryBtn.setVisibility(View.VISIBLE);
        binding.appBarLayout.progress.setVisibility(View.GONE);

        // Disable navigation
        toggle.setDrawerIndicatorEnabled(false);
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void showContent() {
        binding.appBarLayout.fragmentContainer.setVisibility(View.VISIBLE);
        binding.appBarLayout.retryBtn.setVisibility(View.GONE);
        binding.appBarLayout.progress.setVisibility(View.GONE);
//
        boolean showDrawer = SharedPreferenceHelper.getIntroStep() == SharedPreferenceHelper.STEP_COMPLETED;
        toggle.setDrawerIndicatorEnabled(showDrawer);
        binding.drawerLayout.setDrawerLockMode(showDrawer ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

//    public void setupIabHelper(final boolean checkInventory) {
//        mHelper = new IabHelper(this, this.getString(R.string.public_key));
//
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                Log.d("Connect billing", "Success: " + result.isSuccess());
//                if (result.isSuccess() & checkInventory) {
//                    checkForSubscription();
//                } else {
//                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
//                    showContent();
//                }
//            }
//        });
//    }

//    public void disposeIabHelper() {
//        if (mHelper != null) {
//            if (mHelper.isSetupDone()) {
//                try {
//                    if (mHelper.isAsyncInProgress()) {
//                        mHelper.flagEndAsync();
//                    }
//                    mHelper.dispose();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        mHelper = null;
//    }

//    private void checkForSubscription() {
//        List<String> skus = new ArrayList<>();
//        skus.add(SKUMiniFabric.MONTHLY_SUBSCRIPTION);
//        skus.add(SKUMiniFabric.YEARLY_SUBSCRIPTION);
//        skus.add(SKUMiniFabric.SELECTION2_PURCHASE);
//        skus.add(SKUMiniFabric.SELECTION3_PURCHASE);
//        skus.add(SKUMiniFabric.SELECTION4_PURCHASE);
//        skus.add(SKUMiniFabric.SELECTION5_PURCHASE);
//        try {
//            mHelper.queryInventoryAsync(true, new ArrayList<String>(), skus, mGotInventoryListener);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public IabHelper getIabHelper() {
//        return mHelper;
//    }
//
//    public void purchase(String itemSKU, int requestCode, IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener, String subscriptionUid) {
//        try {
//            if (getIabHelper() != null) {
//                getIabHelper().launchSubscriptionPurchaseFlow(this, itemSKU, requestCode,
//                        purchaseFinishedListener, subscriptionUid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void replacePurchasedSku(String itemSKU, String itemOldSKU, int requestCode, IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener, String subscriptionUid) {
//        List<String> oldSkus = new ArrayList<>();
//        oldSkus.add(itemOldSKU);
//        try {
//            if (getIabHelper() != null) {
//                getIabHelper().launchPurchaseFlow(this, IabHelper.ITEM_TYPE_SUBS, itemSKU, oldSkus, requestCode,
//                        purchaseFinishedListener, subscriptionUid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void purchaseSelection(String purchaseSku, String subscriptionUid) {
//        try {
//            if (getIabHelper() != null) {
//                getIabHelper().launchPurchaseFlow(this, purchaseSku, REQUEST_CODE_PURCHASE_SELECTION,
//                        mPurchaseFinishedListener, subscriptionUid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_monthly:
//                if (((Button) v).getText().toString().equals(getString(R.string.downgrade))) {
//                    replacePurchasedSku(SKUMiniFabric.MONTHLY_SUBSCRIPTION, SKUMiniFabric.YEARLY_SUBSCRIPTION, REQUEST_CODE_DOWNGRADE, mSubscriptionFinishedListener, SharedPreferenceHelper.getSubscriptionUid());
//                } else {
//                    purchase(SKUMiniFabric.MONTHLY_SUBSCRIPTION, REQUEST_CODE_MONTHLY, mSubscriptionFinishedListener, SharedPreferenceHelper.getSubscriptionUid());
//                }
                purchase(SKUMiniFabric.MONTHLY_SUBSCRIPTION);

                break;
            case R.id.buy_yearly:
//                if (((Button) v).getText().toString().equals(getString(R.string.upgrade))) {
//                    replacePurchasedSku(SKUMiniFabric.YEARLY_SUBSCRIPTION, SKUMiniFabric.MONTHLY_SUBSCRIPTION, REQUEST_CODE_UPGRADE, mSubscriptionFinishedListener, SharedPreferenceHelper.getSubscriptionUid());
//                } else {
//                    purchase(SKUMiniFabric.YEARLY_SUBSCRIPTION, REQUEST_CODE_YEARLY, mSubscriptionFinishedListener, SharedPreferenceHelper.getSubscriptionUid());
//                }
                purchase(SKUMiniFabric.YEARLY_SUBSCRIPTION);

                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
