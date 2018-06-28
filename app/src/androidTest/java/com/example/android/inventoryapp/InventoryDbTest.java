package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InventoryDbTest {
    private static final String LOG_TAG = "InventoryDbTest";

    private Context appContext;
    private InventoryTestDbHelper inventoryDbHelper;

    @Before
    public void createDb() {
        appContext = InstrumentationRegistry.getTargetContext();
        // Prepare fresh database
        appContext.deleteDatabase(InventoryTestDbHelper.DATABASE_NAME);
        inventoryDbHelper = new InventoryTestDbHelper(appContext);
    }

    @After
    public void tearDown() {
        inventoryDbHelper.close();
        appContext.deleteDatabase(InventoryTestDbHelper.DATABASE_NAME);
    }

    @Test
    public void testDb() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(inventoryDbHelper.getDatabaseName(), InventoryTestDbHelper.DATABASE_NAME);
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        Field dbVersion = InventoryDbHelper.class.getDeclaredField("DATABASE_VERSION");
        dbVersion.setAccessible(true);

        assertEquals(db.getVersion(), dbVersion.getInt(null));
        Log.d(LOG_TAG, db.getPath());

        verifyInventoryTables();
    }

    @Test
    public void testAddDisplayInventory() {
        insertInventory(new Inventory());
        insertInventory(new Inventory(
                "LG Leon",
                9900,
                null,
                "LG",
                "534 5420 353"
        ));
        insertInventory(new Inventory(
                "iPhone 6",
                64900,
                9,
                "Apple",
                null
        ));
        insertInventory(new Inventory(
                "Huawei Y6",
                12900,
                3,
                "Huawei",
                null
        ));
        // Use SQL syntax in db.rawQuery() method
        StringCursorParser parser1 = verifyTableContents(4);

        // Use default projection in db.query method
        StringCursorParser parser2 = new StringCursorParser();
        queryInventory( InventoryUtils.prepareProjection(), parser2);

        Log.d(LOG_TAG, "### queryInventory start #################");
        Log.d(LOG_TAG, parser2.getParsedQuery());
        Log.d(LOG_TAG, "### queryInventory end ################");

        assertEquals(parser1.getParsedQuery(), parser2.getParsedQuery());
    }

    private void verifyInventoryTables() {
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        StringBuilder tables = new StringBuilder();
        boolean isInventoryTable = false;

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                String tableName = c.getString(0);
                if (tableName.equals(InventoryContract.InventoryEntry.TABLE_NAME)) {
                    isInventoryTable = true;
                }
                tables.append(tableName).append(", ");
                c.moveToNext();
            }
        }
        c.close();
        Log.d(LOG_TAG, tables.toString());
        assertTrue(isInventoryTable);
    }

    private StringCursorParser verifyTableContents(int insertedData) {
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        String query = "SELECT * FROM " + InventoryContract.InventoryEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);

        StringCursorParser parser = new StringCursorParser();
        parser.parse(c);

        String dbContent = (parser.getParsedQuery());
        String[] rows = dbContent.split("\n");
        assertEquals(1 + insertedData,rows.length);

        Log.d(LOG_TAG, "### verifyTableContents start #################");
        Log.d(LOG_TAG, dbContent);
        Log.d(LOG_TAG, "### verifyTableContents end################");
        c.close();
        return parser;
    }


    // Moved functions from InventoryUtils after change in logic to use ContentResolver
    // instead of InventoryDbHelper
    private long insertInventory(Inventory inventory) {
        // Insert into database.
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        return db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null,
                InventoryUtils.getContentValuesForInventory(inventory));
    }

    private void queryInventory(String[] projection, InventoryUtils.CursorParser cursorParser) {
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
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
    }
}
