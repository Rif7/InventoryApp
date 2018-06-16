package com.example.android.inventoryapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    }

    private void logDbPath() {
        if (mDbHelper!= null) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Log.e("MainActivity", db.getPath());
        } else {
            Log.e("MainActivity", "mDbHelper is null");
        }

    }

}