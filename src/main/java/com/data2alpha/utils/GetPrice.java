package com.data2alpha.utils;

public class GetPrice implements ActionHandler {

  @Override
  public void actOn(String line) {
    System.out.println(orderbook.get_price(orderSide(line), size(line)));
  }

  private String orderSide(String line) {
    return pattern.split(line, 7)[2];
  }
  
  private int size(String line) {
    return Integer.parseInt(pattern.split(line, 7)[3]);
  }
}
