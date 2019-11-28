package com.data2alpha.utils;

public enum OrderSide {
  B("B"), S("S");
  
  private String orderSide;

  private OrderSide(String orderSide) {
    this.orderSide = orderSide;
  }

  public String getOrderSide() {
    return orderSide;
  }

  
}
