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
    public void tearDown() throws Exception {
        inventoryDbHelper.close();
    }

    @Test
    public void testDb() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(inventoryDbHelper.getDatabaseName(), TestInventoryDbHelper.DATABASE_NAME);
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        Field dbVersion = InventoryDbHelper.class.getDeclaredField("DATABASE_VERSION");
        dbVersion.setAccessible(true);

        assertEquals(db.getVersion(), dbVersion.getInt(null));
        Log.e("testDb", db.getPath());

        verifyInventoryTables();
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
        Log.e("tables = ", tables.toString());
        assertTrue(isInventoryTable);
    }

}
