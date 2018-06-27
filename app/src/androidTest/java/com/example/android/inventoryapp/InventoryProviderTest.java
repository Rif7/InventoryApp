package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryProvider;

import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InventoryProviderTest extends ProviderTestCase2<InventoryProvider> {
    private static final String LOG_TAG = "InventoryProviderTest";

    private Context appContext;

    MockContentResolver mockContentResolver;

    public InventoryProviderTest() {
        super(InventoryProvider.class, InventoryContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        super.setUp();
        Log.d(LOG_TAG, "setUp: ");
        mockContentResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(LOG_TAG, "tearDown:");
    }

    public void testQuery() {
        Cursor cursor = mockContentResolver.query(InventoryContract.InventoryEntry.CONTENT_URI, null, null, null, null);
        assertNotNull(cursor);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        Log.d(LOG_TAG, parser.getParsedQuery());
        Log.d(LOG_TAG, "");
    }

}
