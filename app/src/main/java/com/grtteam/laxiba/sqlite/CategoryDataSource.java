package com.grtteam.laxiba.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grtteam.laxiba.entity.Category;

import java.util.ArrayList;
import java.util.List;

import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_CATEGORY_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_CATEGORY_NAME;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_LANGUAGE;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.TABLE_CATEGORIES;

/**
 * Created by oleh on 25.07.16.
 */
public class CategoryDataSource {

    private static final String[] ALL_COLUMNS = {
            COLUMN_CATEGORY_ID,
            COLUMN_CATEGORY_NAME,
            COLUMN_LANGUAGE,
            COLUMN_SELECTION_ID
    };

    private CategoryDataSource() {
    }


    public static void clear(SQLiteDatabase database) {
        database.delete(TABLE_CATEGORIES, "1", null);
    }

    public static long saveCategory(SQLiteDatabase database, Category cat) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, cat.getCategoryId());
        values.put(COLUMN_CATEGORY_NAME, cat.getCatName());
        values.put(COLUMN_LANGUAGE, cat.getLanguage());
        values.put(COLUMN_SELECTION_ID, cat.getSelectionId());

        long id = database.insert(TABLE_CATEGORIES, null, values);
        return id;
    }


    public static ArrayList<Category> getAllCategories(SQLiteDatabase database, String lang, String selectionId) {
        ArrayList<Category> categories = new ArrayList<>();

        Cursor cursor = database.query(TABLE_CATEGORIES, ALL_COLUMNS, COLUMN_LANGUAGE + "=? and " + COLUMN_SELECTION_ID + "=?", new String[]{lang, selectionId}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category category = cursorToCategory(cursor);
            categories.add(category);
            cursor.moveToNext();
        }

        cursor.close();
        return categories;
    }

    private static Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setCategoryId(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
        category.setCatName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
        category.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_LANGUAGE)));
        category.setSelectionId(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_ID)));
        return category;

    }
}
