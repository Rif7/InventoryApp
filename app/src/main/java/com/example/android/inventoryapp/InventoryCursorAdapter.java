package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
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
    }

    private void setSingleTextView(View view, Cursor cursor, int viewID, String columnName) {
        TextView textView = (TextView) view.findViewById(viewID);
        int columnIndex = cursor.getColumnIndex(columnName);
        String productName = cursor.getString(columnIndex);
        textView.setText(productName);
    }
}
