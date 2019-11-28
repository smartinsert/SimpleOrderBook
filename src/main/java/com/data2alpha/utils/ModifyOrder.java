package com.data2alpha.utils;

public class ModifyOrder implements ActionHandler {

  @Override
  public void actOn(String line) {
    orderbook.modify(orderId(line), size(line));
  }

  private int orderId(String line) {
    return Integer.parseInt(pattern.split(line, 5)[1]);
  }
  
  private int size(String line) {
    return Integer.parseInt(pattern.split(line, 5)[2]);
  }
  
}
