package com.data2alpha.exceptions;

public class OrderIdAlreadyExistsException extends NullPointerException {

  private static final long serialVersionUID = 1L;

  public OrderIdAlreadyExistsException(String s) {
    super(s);
  }

}
