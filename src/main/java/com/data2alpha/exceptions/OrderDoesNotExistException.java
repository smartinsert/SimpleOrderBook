package com.data2alpha.exceptions;

public class OrderDoesNotExistException extends NullPointerException {

  private static final long serialVersionUID = 1L;

  public OrderDoesNotExistException(String s) {
    super(s);
  }

}
