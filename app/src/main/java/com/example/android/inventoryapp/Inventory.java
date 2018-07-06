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

    public float getFloatPrice() {
        return ((float) price)/100;
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
        if (productName == null) {
            throw new IllegalArgumentException("Product Name is not valid");
        }
        this.productName = productName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPrice(String price) {
        try {
            float realPrice = Float.valueOf(price);
            this.price = (int) Math.floor(realPrice*100);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity is not a Number");
        }
    }

    public void setQuantity(Integer quantity) throws IllegalArgumentException {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setQuantity(String quantity) throws IllegalArgumentException {
        int number;
        try {
            number = Integer.valueOf(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity is not a Natural Number");
        }
        setQuantity(number);
    }

    public void incrementQuantity() throws IllegalArgumentException {
        setQuantity(quantity + 1);
    }

    public void decrementQuantity() throws IllegalArgumentException {
        setQuantity(quantity - 1);
    }

    public void setSupplierName(String supplierName) {
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier Name is not valid");
        }
        this.supplierName = supplierName;
    }

    public void setSupplierPhoneNumber(String supplierPhoneNumber) {
        if (supplierPhoneNumber == null || supplierPhoneNumber.equals("")) {
            throw new IllegalArgumentException("Supplier Phone Number is not valid");
        }
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    private void setDummyData() {
        productName = "NOKIA 8";
        price = 39900;
        quantity = 5;
        supplierName = "NOKIA";
        supplierPhoneNumber = "444 2222 666";
    }
}