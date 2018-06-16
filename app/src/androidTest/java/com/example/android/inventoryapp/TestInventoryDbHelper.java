package com.example.android.inventoryapp;

import android.content.Context;

import com.example.android.inventoryapp.data.InventoryDbHelper;

public class TestInventoryDbHelper extends InventoryDbHelper {
    public static final String LOG_TAG = TestInventoryDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "test_storage.db";

    TestInventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
