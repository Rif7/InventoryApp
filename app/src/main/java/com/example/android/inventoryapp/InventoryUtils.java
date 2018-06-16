package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.InventoryDbHelper;

public final class InventoryUtils {

    /**
     * Get {@link Inventory} object and store in db.
     */
    public static long insertInventory(InventoryDbHelper inventoryDbHelper, Inventory inventory){
        // Insert into database.
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, inventory.getProductName());
        values.put(InventoryEntry.COLUMN_PRICE, inventory.getPrice());
        if (inventory.getQuantity() != null) {
            values.put(InventoryEntry.COLUMN_QUANTITY, inventory.getQuantity());
        }
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, inventory.getSupplierName());
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, inventory.getSupplierPhoneNumber());

        return db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    /**
     * Default query with all fields.
     *
     */
    public static Cursor queryInventory(InventoryDbHelper inventoryDbHelper,
                                         CursorParser cursorParser) {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return queryInventory(inventoryDbHelper, projection, cursorParser);
    }

    /**
     * Specified query with fields given as parameter.
     *
     * @param projection Array of strings from {@link InventoryEntry} column names
     * @param cursorParser Object with needed method to process cursor data
     */
    private static Cursor queryInventory(InventoryDbHelper inventoryDbHelper, String[] projection,
                                         CursorParser cursorParser){
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        try {
            cursorParser.parse(cursor);
        } finally {
            cursor.close();
        }
        return cursor;
    }

    interface CursorParser {
        /**
         * Parse and process data. Does not close the cursor.
         *
         * @param cursor as db query result
         */
        void parse(Cursor cursor);
    }
}


/**
 * Default class for handling data from cursor. Proper for testing or showing results in single text.
 */
class StringCursorParser implements InventoryUtils.CursorParser {
    private String parsedQuerry;

    @Override
    public void parse(Cursor cursor) {
        StringBuilder s = new StringBuilder();
        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            for (String columnName:columnNames) {
                s.append(columnName).append(" | ");
            }
            s.append("\n");
            while ( !cursor.isAfterLast() ) {
                for (String name: columnNames) {
                    s.append(cursor.getString(cursor.getColumnIndex(name))).append(" | ");
                }
                cursor.moveToNext();
                s.append("\n");
            }
        }
        parsedQuerry = s.toString();
    }

    public String getParsedQuerry() {
        return parsedQuerry;
    }
}


