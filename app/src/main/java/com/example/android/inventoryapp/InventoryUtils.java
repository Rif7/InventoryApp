package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.InventoryDbHelper;

public final class InventoryUtils {

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

//      TODO
//    private static Cursor queryData(){
//        // Query the database.
//    }
}




