package com.wajba.restaurant.Exceptions;

public class FoodAlreadyExistsException extends RuntimeException {
    public FoodAlreadyExistsException(String message) {
        super(message);
    }
}
