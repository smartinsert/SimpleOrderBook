package com.data2alpha.facade;

public interface OrderBook {
  public void add(int orderId, String side, double price, int size);
  public void modify(int orderId, int newSize);
  public void remove(int orderId);
  public double get_price(String side, int level);
  public int get_size(String side, int level);
}
