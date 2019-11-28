package com.data2alpha.utils;

public class AddOrder implements ActionHandler {

  @Override
  public void actOn(String line) {
    orderbook.add(orderId(line), orderSide(line), orderPrice(line), size(line));
  }

  private int orderId(String line) {
    return Integer.parseInt(pattern.split(line, 9)[1]);
  }
  
  private String orderSide(String line) {
    return pattern.split(line, 9)[2];
  }
  
  private double orderPrice(String line) {
    return Double.parseDouble(pattern.split(line, 9)[3]);
  }
  
  private int size(String line) {
    return Integer.parseInt(pattern.split(line, 9)[4]);
  }
}
