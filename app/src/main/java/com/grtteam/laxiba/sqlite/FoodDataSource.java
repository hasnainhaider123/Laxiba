package com.grtteam.laxiba.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grtteam.laxiba.entity.Food;

import java.util.ArrayList;
import java.util.List;

import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_FOOD_ALLOWED_CONTENTS;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_FOOD_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_FOOD_NAME;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_FOOD_PARENT_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_LANGUAGE;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.TABLE_FOOD;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.TABLE_SELECTION_SETS;

/**
 * Created by oleh on 25.07.16.
 */
public class FoodDataSource {

    private static String[] ALL_COLUMNS = {
            COLUMN_FOOD_ID,
            COLUMN_FOOD_PARENT_ID,
            COLUMN_FOOD_NAME,
            COLUMN_FOOD_ALLOWED_CONTENTS,
            COLUMN_LANGUAGE,
            COLUMN_SELECTION_ID
    };

    private FoodDataSource() {
    }


    public static void clear(SQLiteDatabase database) {
        database.delete(TABLE_FOOD, "1", null);
    }

    public static long saveFood(SQLiteDatabase database, Food food) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_ID, food.getFoodId());
        values.put(COLUMN_FOOD_PARENT_ID, food.getCatId());
        values.put(COLUMN_FOOD_NAME, food.getName());
        values.put(COLUMN_FOOD_ALLOWED_CONTENTS, food.getData());
        values.put(COLUMN_LANGUAGE, food.getLanguage());
        values.put(COLUMN_SELECTION_ID, food.getSelectionId());

        long id = database.insert(TABLE_FOOD, null, values);
        return id;
    }

    public static List<Food> findFood(SQLiteDatabase database, String query, String lang, String selectionId) {
        List<Food> foods = new ArrayList<>();

        String q = "%" + query + "%";
        Cursor cursor = database.query(TABLE_FOOD, ALL_COLUMNS, COLUMN_FOOD_NAME + " like ? and " + COLUMN_LANGUAGE + "=? and " + COLUMN_SELECTION_ID + "=?", new String[]{q, lang, selectionId}, COLUMN_FOOD_NAME, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = cursorToFood(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }


    public static List<Food> getFood(SQLiteDatabase database, String parentId, String lang, String selectionId) {
        List<Food> foods = new ArrayList<>();

        Cursor cursor = database.query(TABLE_FOOD, ALL_COLUMNS, COLUMN_FOOD_PARENT_ID + "=? and " + COLUMN_LANGUAGE + "=? and " + COLUMN_SELECTION_ID + "=?", new String[]{parentId, lang, selectionId}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = cursorToFood(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();
        return foods;
    }

    private static Food cursorToFood(Cursor cursor) {
        Food food = new Food();
        food.setFoodId(cursor.getString(cursor.getColumnIndex(COLUMN_FOOD_ID)));
        food.setCatId(cursor.getString(cursor.getColumnIndex(COLUMN_FOOD_PARENT_ID)));
        food.setName(cursor.getString(cursor.getColumnIndex(COLUMN_FOOD_NAME)));
        food.setData(cursor.getString(cursor.getColumnIndex(COLUMN_FOOD_ALLOWED_CONTENTS)));
        food.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_LANGUAGE)));
        food.setSelectionId(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_ID)));
        return food;
    }

    public static int getFoodCount(SQLiteDatabase database, String language) {

        int count = 0;

        Cursor cursor = database.rawQuery("select count(*) from " + TABLE_FOOD + " where " + COLUMN_LANGUAGE + "=?", new String[] {language});
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        return count;
    }
}
