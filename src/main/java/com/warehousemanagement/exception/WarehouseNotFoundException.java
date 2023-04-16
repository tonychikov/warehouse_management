package com.warehousemanagement.exception;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(String code) {
        super("Could not find warehouse with code " + code);
    }
}
