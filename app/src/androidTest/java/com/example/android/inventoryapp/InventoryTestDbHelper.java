package com.example.android.inventoryapp;

import android.content.Context;

import com.example.android.inventoryapp.data.InventoryDbHelper;

/**
 * Helper class used for creating test database.
 */
class InventoryTestDbHelper extends InventoryDbHelper {
    public static final String DATABASE_NAME = "test_storage.db";

    public InventoryTestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
