package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateList();
    }

    // Temporary methods for testing

    public void addData(View view) {
        InventoryUtils.insertInventory(getContentResolver(), new Inventory());
        updateList();
    }

    public void resetData(View view) {
        InventoryUtils.deleteAllInventory(getContentResolver());
        updateList();
    }

    private void updateList() {
        TextView textView = (TextView) findViewById(R.id.db_content);
        StringCursorParser parser = new StringCursorParser();
        InventoryUtils.queryInventory(getContentResolver(), InventoryUtils.prepareProjection(),
                parser);
        textView.setText(parser.getParsedQuery());
    }
}