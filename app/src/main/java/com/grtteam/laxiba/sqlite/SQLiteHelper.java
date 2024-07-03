package com.grtteam.laxiba.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oleh on 25.07.16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_SUB_CATEGORIES = "sub_categories";
    public static final String TABLE_FOOD = "food";
    public static final String TABLE_SELECTION_SETS = "selection_sets";

    public static final String COLUMN_CATEGORY_ID = "cat_id";
    public static final String COLUMN_CATEGORY_NAME = "cat_name";
    public static final String COLUMN_SUBCATEGORY_ID = "sub_cat_id";
    public static final String COLUMN_SUBCATEGORY_NAME = "sub_cat_name";
    public static final String COLUMN_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_FOOD_PARENT_ID = "parent_name";
    public static final String COLUMN_FOOD_ALLOWED_CONTENTS = "allowed";

    public static final String COLUMN_LANGUAGE = "lang";

    public static final String COLUMN_SELECTION_ID = "selection_id";
    public static final String COLUMN_SELECTION_NAME = "sel_name";
    public static final String COLUMN_SELECTION_IBS = "ibs";
    public static final String COLUMN_SELECTION_LACTOSE = "lactose";
    public static final String COLUMN_SELECTION_FRUCTOSE = "fructose";
    public static final String COLUMN_SELECTION_SORBITOL = "sorbitol";
    public static final String COLUMN_SELECTION_FRUCT_GALACT = "fruct_galact";

    public static final String INDEX_FOOD_NAME = "index_food_name";
    private static final String INDEX_FOOD_PARENT = "index_food_parent";

    private static final String DATABASE_NAME = "laxiba.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_CATEGORIES = "create table "
            + TABLE_CATEGORIES + "( " + COLUMN_CATEGORY_ID
            + " text not null, " + COLUMN_CATEGORY_NAME
            + " text, " + COLUMN_LANGUAGE
            + " text not null, " + COLUMN_SELECTION_ID
            + " text not null)";

    private static final String CREATE_TABLE_SUBCATEGORIES = "create table "
            + TABLE_SUB_CATEGORIES + "( " + COLUMN_SUBCATEGORY_ID
            + " text not null, " + COLUMN_CATEGORY_ID
            + " text not null, " + COLUMN_SUBCATEGORY_NAME
            + " text, " + COLUMN_LANGUAGE
            + " text not null, " + COLUMN_SELECTION_ID
            + " text not null)";

    private static final String CREATE_TABLE_FOOD = "create table "
            + TABLE_FOOD + "( " + COLUMN_FOOD_ID
            + " text not null, " + COLUMN_FOOD_PARENT_ID
            + " text not null, " + COLUMN_FOOD_NAME
            + " text, " + COLUMN_FOOD_ALLOWED_CONTENTS
            + " text, " + COLUMN_LANGUAGE
            + " text not null, " + COLUMN_SELECTION_ID
            + " text not null)";

    private static final String CREATE_FOOD_NAME_INDEX = "create index "
            + INDEX_FOOD_NAME + " on " + TABLE_FOOD + "("
            + COLUMN_FOOD_NAME + " COLLATE NOCASE)";

    private static final String CREATE_FOOD_PARENT_INDEX = "create index "
            + INDEX_FOOD_PARENT + " on " + TABLE_FOOD + "("
            + COLUMN_FOOD_PARENT_ID + ")";

    private static final String CREATE_TABLE_SELECTION_SETS = "create table "
            + TABLE_SELECTION_SETS + "( " + COLUMN_SELECTION_ID
            + " text primary key not null, " + COLUMN_SELECTION_NAME
            + " text, " + COLUMN_SELECTION_IBS
            + " text, " + COLUMN_SELECTION_LACTOSE
            + " text, " + COLUMN_SELECTION_FRUCTOSE
            + " text, " + COLUMN_SELECTION_SORBITOL
            + " text, " + COLUMN_SELECTION_FRUCT_GALACT
            + " text)";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_SUBCATEGORIES);
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_FOOD_NAME_INDEX);
        db.execSQL(CREATE_FOOD_PARENT_INDEX);
        db.execSQL(CREATE_TABLE_SELECTION_SETS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SUBCATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_FOOD);

        // create new tables
        onCreate(db);
    }

}
