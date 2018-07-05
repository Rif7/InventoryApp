package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateList();
    }

    private void updateList() {
        ListView listView = (ListView) findViewById(R.id.list);
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