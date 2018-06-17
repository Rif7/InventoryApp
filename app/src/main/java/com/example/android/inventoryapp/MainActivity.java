package com.example.android.inventoryapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new InventoryDbHelper(this);
        logDbPath();
        updateList();
    }

    // Temporary methods for testing

    private void logDbPath() {
        if (mDbHelper!= null) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Log.e("MainActivity", db.getPath());
        } else {
            Log.e("MainActivity", "mDbHelper is null");
        }

    }

    public void addData(View view) {
        InventoryUtils.insertInventory(mDbHelper, new Inventory());
        updateList();
    }


    public void resetData(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "DELETE FROM " + InventoryContract.InventoryEntry.TABLE_NAME;
        db.execSQL(query);
        updateList();
    }

    private void updateList() {
        TextView textView =  (TextView) findViewById(R.id.db_content);
        StringCursorParser parser = new StringCursorParser();
        InventoryUtils.queryInventory(mDbHelper, parser);
        textView.setText(parser.getParsedQuery());
    }
}