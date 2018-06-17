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
    private TestInventoryDbHelper inventoryDbHelper;

    @Before
    public void createDb() {
        appContext = InstrumentationRegistry.getTargetContext();
        // Prepare fresh database
        appContext.deleteDatabase(TestInventoryDbHelper.DATABASE_NAME);
        inventoryDbHelper = new TestInventoryDbHelper(appContext);
    }

    @After
    public void tearDown() {
        inventoryDbHelper.close();
        appContext.deleteDatabase(TestInventoryDbHelper.DATABASE_NAME);
    }

    @Test
    public void testDb() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(inventoryDbHelper.getDatabaseName(), TestInventoryDbHelper.DATABASE_NAME);
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        Field dbVersion = InventoryDbHelper.class.getDeclaredField("DATABASE_VERSION");
        dbVersion.setAccessible(true);

        assertEquals(db.getVersion(), dbVersion.getInt(null));
        Log.e(LOG_TAG, db.getPath());

        verifyInventoryTables();
    }

    @Test
    public void testAddDisplayInventory() {
        InventoryUtils.insertInventory(inventoryDbHelper, new Inventory());
        InventoryUtils.insertInventory(inventoryDbHelper, new Inventory(
                "LG Leon",
                9900,
                null,
                "LG",
                "534 5420 353"
        ));
        InventoryUtils.insertInventory(inventoryDbHelper, new Inventory(
                "iPhone 6",
                64900,
                9,
                "Apple",
                null
        ));
        InventoryUtils.insertInventory(inventoryDbHelper, new Inventory(
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
        InventoryUtils.queryInventory(inventoryDbHelper, parser2);

        Log.e(LOG_TAG, "### queryInventory start #################");
        Log.e(LOG_TAG, parser2.getParsedQuery());
        Log.e(LOG_TAG, "### queryInventory end ################");

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
        Log.e(LOG_TAG, tables.toString());
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

        Log.e(LOG_TAG, "### verifyTableContents start #################");
        Log.e(LOG_TAG, dbContent);
        Log.e(LOG_TAG, "### verifyTableContents end################");
        c.close();
        return parser;
    }

}
