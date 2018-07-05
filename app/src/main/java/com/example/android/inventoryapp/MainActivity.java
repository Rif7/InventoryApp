package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int INVENTORY_LOADER = 0;

    InventoryCursorAdapter inventoryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
    }

    private void initList() {
        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        inventoryCursorAdapter = new InventoryCursorAdapter(this, null);
        listView.setAdapter(inventoryCursorAdapter);

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    public void addData(View view) {
        InventoryUtils.insertInventory(getContentResolver(), new Inventory());
    }

    public void resetData(View view) {
        InventoryUtils.deleteAllInventory(getContentResolver());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return InventoryUtils.getCursorLoader(this,
                InventoryUtils.prepareMainActivityProjection());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        inventoryCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        inventoryCursorAdapter.swapCursor(null);
    }
}