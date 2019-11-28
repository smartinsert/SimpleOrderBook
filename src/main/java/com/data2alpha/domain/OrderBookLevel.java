package com.data2alpha.domain;

public class OrderBookLevel {

  private double price;
  private int numberOfOrders;
  private int size;
  
  public OrderBookLevel(double price, int numberOfOrders, int size) {
    super();
    this.price = price;
    this.numberOfOrders = numberOfOrders;
    this.size = size;
  }

  public double getPrice() {
    return price;
  }

  public int getNumberOfOrders() {
    return numberOfOrders;
  }

  public int getSize() {
    return size;
  }
  
  public void setNumberOfOrders(int numberOfOrders) {
    this.numberOfOrders = numberOfOrders;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "OrderBookLevel [price=" + price + ", numberOfOrders=" + numberOfOrders + ", size=" + size + "]";
  }
  
  
  
}
