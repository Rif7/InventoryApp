package com.example.android.inventoryapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
/**
 * Database helper for Inventory App.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "storage.db";

    /**
     * Database version.
     */
    protected static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link InventoryDbHelper}.
     *
     * @param context of the app
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Constructs a new instance of {@link InventoryDbHelper} with {@link SQLiteOpenHelper}
     * public constructor parameters. Used in tests
     *
     * @param context of the app
     * @param name name of DB
     * @param factory Cursor factory
     * @param version DB version
     */
    protected InventoryDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Inventory table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER);";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

}
