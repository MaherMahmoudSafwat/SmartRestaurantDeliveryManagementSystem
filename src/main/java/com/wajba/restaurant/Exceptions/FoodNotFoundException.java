package com.wajba.restaurant.Exceptions;

public class FoodNotFoundException extends RuntimeException {
  public FoodNotFoundException(String message) {
    super(message);
  }
}
