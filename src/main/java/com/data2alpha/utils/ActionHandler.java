package com.data2alpha.utils;

import java.util.regex.Pattern;

import com.data2alpha.facade.OrderBook;
import com.data2alpha.implementation.SimpleOrderBookImplementation;

public interface ActionHandler {
  public Pattern pattern = Pattern.compile("\\s+");
  public OrderBook orderbook = new SimpleOrderBookImplementation();
  public void actOn(String line);
}
