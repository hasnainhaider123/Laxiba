package com.grtteam.laxiba.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grtteam.laxiba.entity.SubCategory;

import java.util.ArrayList;
import java.util.List;

import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_CATEGORY_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_LANGUAGE;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SUBCATEGORY_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SUBCATEGORY_NAME;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.TABLE_SUB_CATEGORIES;

/**
 * Created by oleh on 25.07.16.
 */
public class SubCategoryDataSource {

    private static final String[] ALL_COLUMNS = {
            COLUMN_SUBCATEGORY_ID,
            COLUMN_SUBCATEGORY_NAME,
            COLUMN_LANGUAGE,
            COLUMN_SELECTION_ID
    };

    private SubCategoryDataSource() {
    }


    public static void clear(SQLiteDatabase database) {
        database.delete(TABLE_SUB_CATEGORIES, "1", null);
    }

    public static long saveSubCategory(SQLiteDatabase database, SubCategory subCat) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBCATEGORY_ID, subCat.getSubcatId());
        values.put(COLUMN_CATEGORY_ID, subCat.getCatId());
        values.put(COLUMN_SUBCATEGORY_NAME, subCat.getSubcatName());
        values.put(COLUMN_LANGUAGE, subCat.getLanguage());
        values.put(COLUMN_SELECTION_ID, subCat.getSelectionId());

        long id = database.insert(TABLE_SUB_CATEGORIES, null, values);
        return id;
    }

    public static List<SubCategory> getSubCategories(SQLiteDatabase database, String categoryId, String lang, String selectionId) {
        List<SubCategory> subCategories = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SUB_CATEGORIES, ALL_COLUMNS, COLUMN_CATEGORY_ID + "=? and " + COLUMN_LANGUAGE + "=? and " + COLUMN_SELECTION_ID + "=?", new String[]{categoryId, lang, selectionId}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SubCategory subCategory = cursorToSubCategory(cursor);
            subCategories.add(subCategory);
            cursor.moveToNext();
        }

        cursor.close();
        return subCategories;
    }


    private static SubCategory cursorToSubCategory(Cursor cursor) {

        SubCategory subCategory = new SubCategory();
        subCategory.setSubcatId(cursor.getString(cursor.getColumnIndex(COLUMN_SUBCATEGORY_ID)));
        subCategory.setSubcatName(cursor.getString(cursor.getColumnIndex(COLUMN_SUBCATEGORY_NAME)));
        subCategory.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_LANGUAGE)));
        subCategory.setSelectionId(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_ID)));
        return subCategory;

    }
}
