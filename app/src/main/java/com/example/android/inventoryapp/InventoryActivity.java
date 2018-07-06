package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class InventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri currentInventoryUri;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText suplierNameEditText;
    private EditText suplierPhoneEditText;

    private Button QuantityMinusButton;
    private Button QuantityPlusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initFieldsViews();
    }

    private void initFieldsViews() {
        Intent intent = getIntent();
        currentInventoryUri = intent.getData();

        nameEditText = (EditText) findViewById(R.id.inventory_name);
        priceEditText = (EditText) findViewById(R.id.inventory_price);
        quantityEditText = (EditText) findViewById(R.id.inventory_quantity);
        suplierNameEditText = (EditText) findViewById(R.id.inventory_supplier_name);
        suplierPhoneEditText = (EditText) findViewById(R.id.inventory_supplier_phone);

        QuantityMinusButton = (Button) findViewById(R.id.inventory_quantity_minus_btn);
        QuantityPlusButton = (Button) findViewById(R.id.inventory_quantity_plus_but);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(InventoryActivity.this);
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return InventoryUtils.getCursorLoader(
                this, InventoryUtils.prepareProjection(), currentInventoryUri);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Inventory inv = InventoryUtils.getInventoryFromSingleRowCursor(cursor);
        if (inv != null) {
            nameEditText.setText(inv.getProductName());
            priceEditText.setText(String.valueOf(inv.getPrice()));
            quantityEditText.setText(String.valueOf(inv.getPrice()));
            if (inv.getSupplierName() != null) {
                suplierNameEditText.setText(inv.getSupplierName());
            }
            if (inv.getSupplierPhoneNumber() != null) {
                suplierPhoneEditText.setText(inv.getSupplierPhoneNumber());
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        suplierNameEditText.setText("");
        suplierPhoneEditText.setText("");
    }
}
