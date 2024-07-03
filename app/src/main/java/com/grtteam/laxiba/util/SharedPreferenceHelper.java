package com.grtteam.laxiba.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.grtteam.laxiba.LaxibaApplication;

import java.util.UUID;

/**
 * Created by oleh on 13.06.16.
 */
public class SharedPreferenceHelper {


    public static final int STEP_EULA = 0;
    public static final int STEP_SUBSCRIPTION = 1;
    public static final int STEP_SELECTION = 2;
    public static final int STEP_INFO = 3;
    public static final int STEP_COMPLETED = 4;

    private final static String PREF_NAME = "laxiba_app_prefs";

    private static final String IS_DEMO = "pref_is_demo_key";
    public static final String MONTHLY = "monthly_bought";
    public static final String YEARLY = "yearly_bought";
    private static final String INTRO_STEP = "intro_step";

    private static final String SUBSCRIPTION_UID = "subscription_uid";

    public static final String MONTHLY_PRICE = "monthly_price";
    public static final String YEARLY_PRICE = "yearly_price";
    private static final String LANGUAGE_CODE = "language_code";
    private static final String PURCHASED_SELECTIONS = "purchased_selections";
    private static final String ACTIVE_SELECTION_ID = "active_selection_id";

    private SharedPreferenceHelper() {
    }

    public static void saveSubscriptionUid(String subscriptionUid) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putString(SUBSCRIPTION_UID, subscriptionUid)
                .apply();
    }

    public static String getSubscriptionUid() {
        return getPreferences().getString(SUBSCRIPTION_UID, UUID.randomUUID().toString());
    }

    public static void saveMonthlyPrice(String price) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putString(MONTHLY_PRICE, price)
                .apply();
    }

    public static String getMonthlyPrice() {
        return getPreferences().getString(MONTHLY_PRICE, null);
    }

    public static void saveYearlyPrice(String price) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putString(YEARLY_PRICE, price)
                .apply();
    }

    public static String getYearlyPrice() {
        return getPreferences().getString(YEARLY_PRICE, null);
    }

    public static void clearSubscriptionUid() {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .remove(SUBSCRIPTION_UID)
                .apply();
    }

    public static SharedPreferences getPreferences() {
        return LaxibaApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public static void saveIsDemo(boolean isDemo) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putBoolean(IS_DEMO, isDemo)
                .apply();
    }

    public static boolean getIsDemo() {
        return getPreferences().getBoolean(IS_DEMO, true);
    }

    public static void saveIntroStep(int step) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putInt(INTRO_STEP, step)
                .apply();
    }

    public static int getIntroStep() {
        return getPreferences().getInt(INTRO_STEP, STEP_EULA);
    }

    public static void saveIsMonthly(boolean isBought) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putBoolean(MONTHLY, isBought)
                .apply();
    }

    public static boolean getIsMonthly() {
        return getPreferences().getBoolean(MONTHLY, false);
    }

    public static void saveIsYearly(boolean isBought) {
        SharedPreferences preferences = getPreferences();
        preferences.edit()
                .putBoolean(YEARLY, isBought)
                .apply();
    }

    public static boolean getIsYearly() {
        return getPreferences().getBoolean(YEARLY, false);
    }

    public static void saveLanguage(String language) {
        getPreferences().edit()
                .putString(LANGUAGE_CODE, language)
                .apply();
    }

    public static String getLanguage() {
        return getPreferences().getString(LANGUAGE_CODE, null);
    }

    public static void savePurchasedSelections(int i) {
        getPreferences().edit()
                .putInt(PURCHASED_SELECTIONS, i)
                .apply();
    }

    public static int getPurchasedSelections() {
        return getPreferences().getInt(PURCHASED_SELECTIONS, 256);
    }

    public static void saveActiveSelectionId(String selectionId) {
        getPreferences().edit()
                .putString(ACTIVE_SELECTION_ID, selectionId)
                .apply();
    }

    public static String getActiveSelectionId() {
        return getPreferences().getString(ACTIVE_SELECTION_ID, null);
    }

    public static void registerSharedPreferenceChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterSharedPreferenceChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }
}
