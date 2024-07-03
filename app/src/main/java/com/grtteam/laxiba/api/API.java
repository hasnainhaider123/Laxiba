package com.grtteam.laxiba.api;

import com.grtteam.laxiba.R;
import com.grtteam.laxiba.entity.SelectionValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 21.07.16.
 */
public class API {

    private API() {
    }

//    public static final String BASE_URL = "http://db.laxiba.com/public/index.php/";
    public static final String BASE_URL = "https://app.laxiba.com/public/index.php/";

    public static final String TRIAL_CATEGORY_ID = "TRIAL";

    public static final String API_LANG_ENGLISH = "e";
    public static final String API_LANG_GERMAN = "g";
    public static final String API_LANG_SPANISH = "s";
    public static final String API_LANG_FRANCH = "f";
    public static final String API_LANG_ITALIAN = "i";

    public static final List<SelectionValue> languages = new ArrayList<>();
    public static final List<SelectionValue> ibsValues = new ArrayList<>();
    public static final List<SelectionValue> sorbitolValues = new ArrayList<>();
    public static final List<SelectionValue> lactoseValues = new ArrayList<>();
    public static final List<SelectionValue> fructansAndGalactans = new ArrayList<>();
    public static final List<SelectionValue> fructoseValues = new ArrayList<>();


    static {
        languages.add(new SelectionValue(API_LANG_ENGLISH, R.string.lang_en));
        languages.add(new SelectionValue(API_LANG_FRANCH, R.string.lang_fr));
        languages.add(new SelectionValue(API_LANG_ITALIAN, R.string.lang_it));
        languages.add(new SelectionValue(API_LANG_SPANISH, R.string.lang_es));
        languages.add(new SelectionValue(API_LANG_GERMAN, R.string.lang_de));

        ibsValues.add(new SelectionValue("yes", R.string.val_yes));
        ibsValues.add(new SelectionValue("no", R.string.val_no));

        sorbitolValues.add(new SelectionValue("yes", R.string.val_yes));
        sorbitolValues.add(new SelectionValue("no", R.string.val_no));
        sorbitolValues.add(new SelectionValue("0", R.string.val_0));
        sorbitolValues.add(new SelectionValue("1", R.string.val_1));
        sorbitolValues.add(new SelectionValue("2", R.string.val_2));

        lactoseValues.add(new SelectionValue("yes", R.string.val_yes));
        lactoseValues.add(new SelectionValue("no", R.string.val_no));
        lactoseValues.add(new SelectionValue("0", R.string.val_0));
        lactoseValues.add(new SelectionValue("1", R.string.val_1));
        lactoseValues.add(new SelectionValue("2", R.string.val_2));

        fructansAndGalactans.add(new SelectionValue("yes", R.string.val_yes));
        fructansAndGalactans.add(new SelectionValue("no", R.string.val_no));
        fructansAndGalactans.add(new SelectionValue("0", R.string.val_0));
        fructansAndGalactans.add(new SelectionValue("1", R.string.val_1));
        fructansAndGalactans.add(new SelectionValue("2", R.string.val_2));

        fructoseValues.add(new SelectionValue("yes", R.string.val_yes));
        fructoseValues.add(new SelectionValue("no", R.string.val_no));
        fructoseValues.add(new SelectionValue("0", R.string.val_0));
        fructoseValues.add(new SelectionValue("1", R.string.val_1));
        fructoseValues.add(new SelectionValue("2", R.string.val_2));

    }

    public static int getValueIndex(List<SelectionValue> values, String code) {
        for (int i = values.size() - 1; i >= 0; i--) {
            if (code.equals(values.get(i).getCode())) {
                return i;
            }
        }
        return 0;
    }

    public static int getValueResId(List<SelectionValue> values, String code) {
        for (int i = values.size() - 1; i >= 0; i--) {
            if (code.equals(values.get(i).getCode())) {
                return values.get(i).getNameResourceId();
            }
        }
        return -1;
    }
}
