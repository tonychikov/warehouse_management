package com.warehousemanagement.exception;

public class BayNotFoundException extends RuntimeException {
    public BayNotFoundException(String warehouseCode, int rowNumber, int shelfNumber, int levelNumber) {
        super("Could not find bay " + warehouseCode + "." + rowNumber + "." + shelfNumber + "." + levelNumber);
    }
}
