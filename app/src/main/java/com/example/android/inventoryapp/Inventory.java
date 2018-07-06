package com.example.android.inventoryapp;

class Inventory {
    private String productName;
    // Use Integer object instead of raw int to distinguish null value
    private Integer price;
    private Integer quantity;

    private String supplierName;
    private String supplierPhoneNumber;

    Inventory() {
        setDummyData();
    }

    Inventory(String productName, Integer price, Integer quantity, String supplierName, String supplierPhoneNumber) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierPhoneNumber(String supplierPhoneNumber) {
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    private void setDummyData() {
        productName = "NOKIA 8";
        price = 39900;
        quantity = 5;
        supplierName = "NOKIA";
        supplierPhoneNumber = null;
    }
}