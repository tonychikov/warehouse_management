package com.warehousemanagement.exception;

public class BayNotEmptyException extends RuntimeException {
    public BayNotEmptyException(String warehouseCode, int rowNumber, int shelfNumber, int levelNumber) {
        super("The bay " + warehouseCode + "." + rowNumber + "." + shelfNumber + "." + levelNumber + " is not empty");
    }
}
