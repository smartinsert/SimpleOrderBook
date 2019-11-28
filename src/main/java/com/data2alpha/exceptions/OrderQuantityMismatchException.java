package com.data2alpha.exceptions;

public class OrderQuantityMismatchException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public OrderQuantityMismatchException(String message) {
    super(message);
  }

}
