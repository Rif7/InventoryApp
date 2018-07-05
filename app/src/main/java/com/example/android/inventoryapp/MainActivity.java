package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        updateList();
    }

    private void updateList() {
        CursorAdapterParser cursorParser = new CursorAdapterParser(this);
        InventoryUtils.queryInventory(getContentResolver(), InventoryUtils.prepareMainActivityProjection(), cursorParser);
        listView.setAdapter(cursorParser.getAdapter());
    }

    public void addData(View view) {
        InventoryUtils.insertInventory(getContentResolver(), new Inventory());
        updateList();
    }

    public void resetData(View view) {
        InventoryUtils.deleteAllInventory(getContentResolver());
        updateList();
    }

}