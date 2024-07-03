package com.grtteam.laxiba.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grtteam.laxiba.entity.SelectionSet;
import com.grtteam.laxiba.util.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_FRUCTOSE;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_FRUCT_GALACT;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_IBS;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_ID;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_LACTOSE;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_NAME;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.COLUMN_SELECTION_SORBITOL;
import static com.grtteam.laxiba.sqlite.SQLiteHelper.TABLE_SELECTION_SETS;

/**
 * Created by oleh on 02.08.16.
 */
public class SelectionSetDataSource {

    private static String[] ALL_COLUMNS = {
            COLUMN_SELECTION_ID,
            COLUMN_SELECTION_NAME,
            COLUMN_SELECTION_IBS,
            COLUMN_SELECTION_LACTOSE,
            COLUMN_SELECTION_FRUCTOSE,
            COLUMN_SELECTION_SORBITOL,
            COLUMN_SELECTION_FRUCT_GALACT
    };

    private SelectionSetDataSource() {
    }

    public static void clear(SQLiteDatabase database) {
        database.delete(TABLE_SELECTION_SETS, "1", null);
    }

    public static long saveSelectionSet(SQLiteDatabase database, SelectionSet selectionSet) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_SELECTION_ID, selectionSet.getSelectionId());
        values.put(COLUMN_SELECTION_NAME, selectionSet.getSelectionName());
        values.put(COLUMN_SELECTION_IBS, selectionSet.getIbs());
        values.put(COLUMN_SELECTION_LACTOSE, selectionSet.getLactose());
        values.put(COLUMN_SELECTION_FRUCTOSE, selectionSet.getFructose());
        values.put(COLUMN_SELECTION_SORBITOL, selectionSet.getSorbitol());
        values.put(COLUMN_SELECTION_FRUCT_GALACT, selectionSet.getFructansAndGalactans());

        long id = database.insert(TABLE_SELECTION_SETS, null, values);
        return id;
    }

    public static void saveSelectionSets(SQLiteDatabase database, List<SelectionSet> selectionSetList) {
        database.beginTransaction();

        clear(database);

        if (selectionSetList != null) {
            for (SelectionSet selectionSet : selectionSetList) {
                saveSelectionSet(database, selectionSet);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();

        // Validate saved active selection id
        if (selectionSetList != null) {

            String active = SharedPreferenceHelper.getActiveSelectionId();
            if (active != null) {
                for (SelectionSet selectionSet : selectionSetList) {
                    if (active.equals(selectionSet.getSelectionId())) {
                        // There is active selection
                        return;
                    }
                }
            }
            // if no active selection selected or selected wrong one
            if (!selectionSetList.isEmpty()) {
                SharedPreferenceHelper.saveActiveSelectionId(selectionSetList.get(0).getSelectionId());
            } else {
                SharedPreferenceHelper.saveActiveSelectionId(null);
            }
        } else {
            SharedPreferenceHelper.saveActiveSelectionId(null);
        }
    }

    public static SelectionSet getSelectionSet(SQLiteDatabase database, String selectionId) {
        SelectionSet selectionSet = null;

        Cursor cursor = database.query(TABLE_SELECTION_SETS, ALL_COLUMNS, COLUMN_SELECTION_ID + "=?", new String[]{selectionId}, null, null, null);

        if (cursor.moveToFirst()) {
            selectionSet = cursorToSelectionSet(cursor);
        }
        cursor.close();

        return selectionSet;
    }

    public static List<SelectionSet> getAllSelectionSets(SQLiteDatabase database) {
        List<SelectionSet> selectionSets = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SELECTION_SETS, ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SelectionSet selectionSet = cursorToSelectionSet(cursor);
            selectionSets.add(selectionSet);
            cursor.moveToNext();
        }

        cursor.close();
        return selectionSets;
    }

    public static int getSelectionsCount(SQLiteDatabase database) {
        int count = 0;

        Cursor cursor = database.rawQuery("select count(*) from " + TABLE_SELECTION_SETS, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        return count;
    }

    private static SelectionSet cursorToSelectionSet(Cursor cursor) {
        SelectionSet selectionSet = new SelectionSet();
        selectionSet.setSelectionId(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_ID)));
        selectionSet.setSelectionName(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_NAME)));
        selectionSet.setIbs(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_IBS)));
        selectionSet.setLactose(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_LACTOSE)));
        selectionSet.setFructose(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_FRUCTOSE)));
        selectionSet.setSorbitol(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_SORBITOL)));
        selectionSet.setFructansAndGalactans(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTION_FRUCT_GALACT)));

        return selectionSet;
    }
}
