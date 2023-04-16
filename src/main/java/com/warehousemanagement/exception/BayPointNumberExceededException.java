package com.warehousemanagement.exception;

public class BayPointNumberExceededException extends RuntimeException {
    public BayPointNumberExceededException(int holdingPoints, int occupiedPoints) {
        super("The number of occupied points (" + occupiedPoints + ") exideed holding points (" + holdingPoints + ")");
    }
}
