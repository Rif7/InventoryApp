package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;


import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

final class InventoryUtils {

    /**
     * Get {@link Inventory} object and store in in db, using {@link ContentResolver}.
     */
    public static void insertInventory(ContentResolver contentResolver, Inventory inventory) {
        // Insert into database.
        Uri uri = contentResolver.insert(InventoryEntry.CONTENT_URI,
                getContentValuesForInventory(inventory));
    }

    public static ContentValues getContentValuesForInventory(Inventory inventory) {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, inventory.getProductName());
        values.put(InventoryEntry.COLUMN_PRICE, inventory.getPrice());
        if (inventory.getQuantity() != null) {
            values.put(InventoryEntry.COLUMN_QUANTITY, inventory.getQuantity());
        }
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, inventory.getSupplierName());
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, inventory.getSupplierPhoneNumber());
        return values;
    }

    /**
     * Default query with all fields.
     */
    public static String[] prepareProjection() {

        return new String[]{
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
    }

    /**
     * Query for {@link MainActivity}
     */
    public static String[] prepareMainActivityProjection() {

        return new String[]{
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY
        };
    }

    /**
     * Create {@link CursorLoader} for Inventory DB with given projection
     */
    public static CursorLoader getCursorLoader(Context context, String[] projection) {
        return new CursorLoader(context,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    public static void deleteAllInventory(ContentResolver contentResolver) {
        contentResolver.delete(InventoryEntry.CONTENT_URI, null, null);
    }

    interface CursorParser {
        /**
         * Parse and process data. Depending of implementation can close the cursor
         *
         * @param cursor as db query result
         */
        void parse(Cursor cursor);
    }
}


/**
 * Default class for handling data from cursor. Proper for testing or showing results in single text.
 * Close the cursor.
 */
class StringCursorParser implements InventoryUtils.CursorParser {
    private String parsedQuery;

    @Override
    public void parse(Cursor cursor) {
        try {
            parseToString(cursor);
        } finally {
            cursor.close();
        }
    }

    private void parseToString(Cursor cursor) {
        StringBuilder s = new StringBuilder();
        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                s.append(columnName).append(" | ");
            }
            s.append("\n");
            while (!cursor.isAfterLast()) {
                for (String name : columnNames) {
                    s.append(cursor.getString(cursor.getColumnIndex(name))).append(" | ");
                }
                cursor.moveToNext();
                s.append("\n");
            }
        }
        parsedQuery = s.toString();
    }

    public String getParsedQuery() {
        return parsedQuery;
    }
}




