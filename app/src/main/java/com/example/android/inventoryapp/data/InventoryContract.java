package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public class InventoryContract {

    private InventoryContract() {}

    /**
     * Inner class that defines constant values for the inventories database table.
     * Each entry in the table represents a single inventory.
     */
    public static final class InventoryEntry implements BaseColumns {

        /**
         * Name of database table for inventory
         */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the inventory (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Product Name.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Price in cents.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Quantity.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Supplier Phone Number.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
