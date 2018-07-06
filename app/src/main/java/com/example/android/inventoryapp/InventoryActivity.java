package com.example.android.inventoryapp;

import java.lang.Math;
import java.security.InvalidParameterException;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri currentInventoryUri;
    private Inventory inventory;
    private boolean inventoryHasChanged = false;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText suplierNameEditText;
    private EditText suplierPhoneEditText;

    private Button quantityMinusButton;
    private Button quantityPlusButton;

    private Button deleteButton;
    private Button saveButton;

    private TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initFieldsViews();

        if (currentInventoryUri == null) {
            setTitle("Add Product");
        } else {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
            createTextWatcher();
            inventoryHasChanged = false;
        }
    }

    private void initFieldsViews() {
        final Intent intent = getIntent();
        currentInventoryUri = intent.getData();

        nameEditText = (EditText) findViewById(R.id.inventory_name);
        priceEditText = (EditText) findViewById(R.id.inventory_price);
        quantityEditText = (EditText) findViewById(R.id.inventory_quantity);
        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    try {
                        inventory.setQuantity(quantityEditText.getText().toString());
                    } catch (IllegalArgumentException e) {
                        showToast(e.getMessage());
                        quantityEditText.setText(String.valueOf(inventory.getQuantity()));
                    }
                }
            }
        });

        suplierNameEditText = (EditText) findViewById(R.id.inventory_supplier_name);
        suplierPhoneEditText = (EditText) findViewById(R.id.inventory_supplier_phone);

        quantityMinusButton = (Button) findViewById(R.id.inventory_quantity_minus_btn);
        quantityMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inventory.decrementQuantity();
                    inventoryHasChanged = true;
                    quantityEditText.setText(String.valueOf(inventory.getQuantity()));
                } catch (IllegalArgumentException e) {
                    showToast(e.getMessage());
                }
            }
        });

        quantityPlusButton = (Button) findViewById(R.id.inventory_quantity_plus_but);
        quantityPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inventory.incrementQuantity();
                    inventoryHasChanged = true;
                    quantityEditText.setText(String.valueOf(inventory.getQuantity()));
                } catch (IllegalArgumentException e) {
                    showToast(e.getMessage());
                }
            }
        });

        deleteButton = (Button) findViewById(R.id.inventory_delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentInventoryUri != null) {
                    delete();
                }
            }
        });

        saveButton = (Button) findViewById(R.id.inventory_save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentInventoryUri != null) {
                    update();
                }
            }
        });

    }

    private void delete() {
        if (currentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(currentInventoryUri, null, null);
            String productName = inventory.getProductName();

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Failed to delete " + productName,
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Deleted " + productName,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void update() {
        try {
            inventory.setProductName(nameEditText.getText().toString());
            inventory.setPrice(priceEditText.getText().toString());
            inventory.setQuantity(quantityEditText.getText().toString());
            inventory.setSupplierName(suplierNameEditText.getText().toString());
            inventory.setSupplierPhoneNumber(suplierPhoneEditText.getText().toString());

            int rowsAffected = getContentResolver().update(currentInventoryUri,
                    InventoryUtils.getContentValuesFromInventory(inventory),
                    null, null);
            if (rowsAffected == 0) {
                throw new Exception("Failed to update Inventory");
            }
            finish();
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private void createTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inventoryHasChanged = true;
            }
        };

        nameEditText.addTextChangedListener(textWatcher);
        priceEditText.addTextChangedListener(textWatcher);
        quantityEditText.addTextChangedListener(textWatcher);
        suplierNameEditText.addTextChangedListener(textWatcher);
        suplierPhoneEditText.addTextChangedListener(textWatcher);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
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
        inventory = InventoryUtils.getInventoryFromSingleRowCursor(cursor);
        if (inventory != null) {
            nameEditText.setText(inventory.getProductName());
            priceEditText.setText(String.valueOf(inventory.getFloatPrice()));
            quantityEditText.setText(String.valueOf(inventory.getQuantity()));
            if (inventory.getSupplierName() != null) {
                suplierNameEditText.setText(inventory.getSupplierName());
            }
            if (inventory.getSupplierPhoneNumber() != null) {
                suplierPhoneEditText.setText(inventory.getSupplierPhoneNumber());
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


