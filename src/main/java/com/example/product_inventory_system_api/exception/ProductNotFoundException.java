package com.example.product_inventory_system_api.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String sku, String action) {
        super(String.format("Product with SKU '%s' not found while attempting to %s", sku, action));
    }
}
