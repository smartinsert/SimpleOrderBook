package com.data2alpha.domain;


import static com.data2alpha.utils.OrderSide.B;
import static com.data2alpha.utils.OrderSide.S;

import com.data2alpha.utils.OrderSide;

public class Order {
  private int orderId;
  private OrderSide side;
  private double price;
  private int size;
  
  public Order(int orderId, String side, double price, int size) {
    super();
    this.orderId = orderId;
    this.side = OrderSide.valueOf(side);
    this.price = price;
    this.size = size;
  }

  public int getOrderId() {
    return orderId;
  }

  public OrderSide getSide() {
    return side;
  }

  public double getPrice() {
    return price;
  }

  public int getSize() {
    return size;
  }
  
  public boolean isBuy() {
    return side.equals(B);
  }
  
  public boolean isSell() {
    return side.equals(S);
  }
  
}
