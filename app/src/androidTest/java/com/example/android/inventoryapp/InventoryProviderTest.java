package com.example.android.inventoryapp;

import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryProvider;

import org.junit.Test;

import static com.example.android.inventoryapp.InventoryUtils.getContentValuesFromInventory;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InventoryProviderTest extends ProviderTestCase2<InventoryProvider> {
    private static final String LOG_TAG = "InventoryProviderTest";

    private MockContentResolver mockContentResolver;

    public InventoryProviderTest() {
        super(InventoryProvider.class, InventoryContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(LOG_TAG, "setUp: ");
        mockContentResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(LOG_TAG, "tearDown:");
    }

    @Test
    public void testQuery() {
        insertSomeData();
        Cursor cursor = mockContentResolver.query(InventoryContract.InventoryEntry.CONTENT_URI, null, null, null, null);
        assertNotNull(cursor);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        String parsedCursor = parser.getParsedQuery();
        Log.d(LOG_TAG, parsedCursor);
        assertEquals(true, parsedCursor.contains("2 | LG Leon | 9900 | 0 | LG | 534 5420 353 |"));
        assertEquals(true, parsedCursor.contains("3 | iPhone 6 | 64900 | 9 | Apple | null |"));
        assertEquals(true, parsedCursor.contains("4 | Huawei Y6 | 12900 | 3 | Huawei | null |"));
    }

    @Test
    public void testSingleItemQuery() {
        insertSomeData();
        Cursor cursor = mockContentResolver.query(
                Uri.withAppendedPath(InventoryContract.InventoryEntry.CONTENT_URI, "2"),
                null, null, null, null);
        assertNotNull(cursor);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        String parsedCursor = parser.getParsedQuery();
        Log.d(LOG_TAG, parsedCursor);
        assertEquals(true, parsedCursor.contains("2 | LG Leon | 9900 | 0 | LG | 534 5420 353 |"));
        assertEquals(false, parsedCursor.contains("3 | iPhone 6 | 64900 | 9 | Apple | null |"));
        assertEquals(false, parsedCursor.contains("4 | Huawei Y6 | 12900 | 3 | Huawei | null |"));
    }

    @Test
    public void testUpdateSingleItem() {
        insertSomeData();
        Uri secondItemURI = Uri.withAppendedPath(InventoryContract.InventoryEntry.CONTENT_URI, "2");
        int affectedRows = mockContentResolver.update(secondItemURI,
                getContentValuesFromInventory(new Inventory(
                        "Sony Xperia",
                        44400,
                        1,
                        "Sony",
                        null)), null, null);
        assertEquals(1, affectedRows);
        Cursor cursor = mockContentResolver.query(
                secondItemURI, null, null, null, null);
        assertNotNull(cursor);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        String parsedCursor = parser.getParsedQuery();
        Log.d(LOG_TAG, parsedCursor);
        assertEquals(false, parsedCursor.contains("2 | LG Leon | 9900 | 0 | LG | 534 5420 353 |"));
        assertEquals(true, parsedCursor.contains("2 | Sony Xperia | 44400 | 1 | Sony | null |"));
    }

    @Test
    public void testDeleteSingleItem() {
        insertSomeData();
        Uri secondItemURI = Uri.withAppendedPath(InventoryContract.InventoryEntry.CONTENT_URI, "2");
        int affectedRows = mockContentResolver.delete(secondItemURI, null, null);
        assertEquals(1, affectedRows);
        Cursor cursor = mockContentResolver.query(InventoryContract.InventoryEntry.CONTENT_URI, null, null, null, null);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        String parsedCursor = parser.getParsedQuery();
        Log.d(LOG_TAG, parsedCursor);
        assertEquals(false, parsedCursor.contains("2 | LG Leon | 9900 | 0 | LG | 534 5420 353 |"));
        assertEquals(true, parsedCursor.contains("3 | iPhone 6 | 64900 | 9 | Apple | null |"));
        assertEquals(true, parsedCursor.contains("4 | Huawei Y6 | 12900 | 3 | Huawei | null |"));
    }

    @Test
    public void testDeleteAllItems() {
        insertSomeData();
        int affectedRows = mockContentResolver.delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null);
        assertEquals(4, affectedRows);
        Cursor cursor = mockContentResolver.query(InventoryContract.InventoryEntry.CONTENT_URI, null, null, null, null);
        StringCursorParser parser = new StringCursorParser();
        parser.parse(cursor);
        String parsedCursor = parser.getParsedQuery();
        Log.d(LOG_TAG, parsedCursor);
        assertEquals(false, parsedCursor.contains("2 | LG Leon | 9900 | 0 | LG | 534 5420 353 |"));
        assertEquals(false, parsedCursor.contains("3 | iPhone 6 | 64900 | 9 | Apple | null |"));
        assertEquals(false, parsedCursor.contains("4 | Huawei Y6 | 12900 | 3 | Huawei | null |"));
    }

    private void insertSomeData() {
        Uri uri = InventoryContract.InventoryEntry.CONTENT_URI;

        mockContentResolver.insert(uri, getContentValuesFromInventory(new Inventory(true)));
        mockContentResolver.insert(uri, getContentValuesFromInventory(new Inventory(
                "LG Leon",
                9900,
                null,
                "LG",
                "534 5420 353"
        )));
        mockContentResolver.insert(uri, getContentValuesFromInventory(new Inventory(
                "iPhone 6",
                64900,
                9,
                "Apple",
                null
        )));
        mockContentResolver.insert(uri, getContentValuesFromInventory(new Inventory(
                "Huawei Y6",
                12900,
                3,
                "Huawei",
                null
        )));
    }


}
