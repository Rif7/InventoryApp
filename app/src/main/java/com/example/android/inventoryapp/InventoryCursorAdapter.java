package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

import java.text.NumberFormat;
import java.util.Locale;

public class InventoryCursorAdapter extends CursorAdapter {
    Context context;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setSingleTextView(view, cursor, R.id.list_item_name, InventoryEntry.COLUMN_PRODUCT_NAME);
        setSingleTextView(view, cursor, R.id.list_item_price, InventoryEntry.COLUMN_PRICE);
        setSingleTextView(view, cursor, R.id.list_item_quantity, InventoryEntry.COLUMN_QUANTITY);
        setOnClickListeners(view, cursor);
    }

    private void setSingleTextView(View view, Cursor cursor, int viewID, String columnName) {
        TextView textView = (TextView) view.findViewById(viewID);
        int columnIndex = cursor.getColumnIndex(columnName);
        String productDetail = cursor.getString(columnIndex);
        if (columnName.equals(InventoryEntry.COLUMN_PRICE)) {
            float price = Inventory.getFloatPrice(Integer.valueOf(productDetail));
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            productDetail = format.format(price);
        }
        textView.setText(productDetail);
    }

    private void setOnClickListeners(View view, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int index = cursor.getInt(columnIndex);
        final Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, index);
        View.OnClickListener details = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InventoryActivity.class);
                intent.setData(currentInventoryUri);
                context.startActivity(intent);
            }
        };

        View productDetails = view.findViewById(R.id.list_item_product_details);
        productDetails.setOnClickListener(details);
        View productQuantity = view.findViewById(R.id.list_item_quantity_details);
        productQuantity.setOnClickListener(details);

        final Inventory inventory = new Inventory();
        columnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
        String quantity = cursor.getString(columnIndex);
        inventory.setQuantity(quantity);
        Button saleButton = (Button) view.findViewById(R.id.list_item_sale_btn);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inventory.decrementQuantity();

                    context.getContentResolver().update(currentInventoryUri,
                            InventoryUtils.getContentValuesForSingleQuantity(inventory),
                            null, null);
                } catch (Exception ignored) {}
            }
        });
    }

}
