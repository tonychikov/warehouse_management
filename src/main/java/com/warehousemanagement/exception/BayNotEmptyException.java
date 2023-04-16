package com.warehousemanagement.exception;

public class BayNotEmptyException extends RuntimeException {
    public BayNotEmptyException(String warehouseCode, int rowNumber, int shelfNumber, int levelNumber) {
        super("Deletion is stop. The bay " + warehouseCode + "." + rowNumber + "." + shelfNumber + "." + levelNumber + " is not empty");
    }
}
