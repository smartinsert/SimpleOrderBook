package com.data2alpha.utils;

public class RemoveOrder implements ActionHandler {

  @Override
  public void actOn(String line) {
    orderbook.remove(orderId(line));
  }

  private int orderId(String line) {
    return Integer.parseInt(pattern.split(line, 3)[1]);
  }
  
}
