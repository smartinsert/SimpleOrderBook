package com.data2alpha.implementation;

import static com.data2alpha.utils.OrderSide.B;
import static com.data2alpha.utils.OrderSide.S;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;

import com.data2alpha.domain.Order;
import com.data2alpha.domain.OrderBookLevel;
import com.data2alpha.exceptions.OrderDoesNotExistException;
import com.data2alpha.exceptions.OrderIdAlreadyExistsException;
import com.data2alpha.exceptions.OrderQuantityMismatchException;
import com.data2alpha.facade.OrderBook;
import com.data2alpha.utils.OrderSide;
import com.data2alpha.utils.SetUtil;

public class SimpleOrderBookImplementation implements OrderBook {
  private List<Order> bidSideOrders;
  private List<Order> askSideOrders;
  private Map<Integer, OrderSide> orderIdToOrderSide;
  private Map<Double, OrderBookLevel> bidPriceToOrderBookLevel;
  private Map<Double, OrderBookLevel> askPriceToOrderBookLevel;

  public SimpleOrderBookImplementation() {
    super();
    this.bidSideOrders = new ArrayList<Order>();
    this.askSideOrders = new ArrayList<Order>();
    this.orderIdToOrderSide = new HashMap<Integer, OrderSide>();
    this.bidPriceToOrderBookLevel = new TreeMap<Double, OrderBookLevel>(Collections.reverseOrder());
    this.askPriceToOrderBookLevel = new TreeMap<Double, OrderBookLevel>();
  }

  @Override
  public void add(int orderId, String side, double price, int size) {
    if (!doesOrderExist(orderId)) {
      Order orderToBeAdded = new Order(orderId, side, price, size);
      orderIdToOrderSide.put(orderId, OrderSide.valueOf(side));
      addOrder(orderToBeAdded);
    } else 
      throw new OrderIdAlreadyExistsException("OrderId ".concat(String.valueOf(orderId)).concat(" already exists, Please add the order with a unique order ID!"));
  }

  @Override
  public void modify(int orderId, int newSize) {
    if (doesOrderExist(orderId)) {
      OrderSide orderSide = orderIdToOrderSide.get(orderId);
      if (orderSide.equals(B))
        modifyOrder(orderId, newSize, bidSideOrders);
      else if (orderSide.equals(S))
        modifyOrder(orderId, newSize, askSideOrders);
    } else {
      throw new OrderDoesNotExistException("The order Id ".concat(String.valueOf(orderId)).concat(" to be modified does not exist!"));
    }
  }

  @Override
  public void remove(int orderId) {
    if (doesOrderExist(orderId)) {
      OrderSide orderSide = orderIdToOrderSide.get(orderId);
      if (orderSide.equals(B))  {
        Order orderToBeRemoved = removeOrder(orderId, bidSideOrders);
        removeOrderQuantityFromPriceLevel(orderToBeRemoved, orderToBeRemoved.getSize(), bidPriceToOrderBookLevel);
      } else if (orderSide.equals(S)) {
        Order orderToBeRemoved = removeOrder(orderId, askSideOrders);
        removeOrderQuantityFromPriceLevel(orderToBeRemoved, orderToBeRemoved.getSize(), askPriceToOrderBookLevel);
      }
      orderIdToOrderSide.remove(orderId);
    } else {
      throw new OrderDoesNotExistException("The order Id ".concat(String.valueOf(orderId)).concat(" to be removed does not exist!"));
    }
  }

  @Override
  public double get_price(String side, int level) {
    if (OrderSide.valueOf(side).equals(B)) {
      Set<Double> bidOrderPrice = bidPriceToOrderBookLevel.keySet();
      Double retrievedPrice = SetUtil.nthElement(bidOrderPrice, level-1);
      if (retrievedPrice != null) {
        return retrievedPrice;
      }
    } else if (OrderSide.valueOf(side).equals(S)) {
      Set<Double> askOrderPrice = askPriceToOrderBookLevel.keySet();
      Double retrievedPrice = SetUtil.nthElement(askOrderPrice, level-1);
      if (retrievedPrice != null) {
        return retrievedPrice;
      }
    }
    return 0;
  }

  @Override
  public int get_size(String side, int level) {
    double concernedPriceLevel = get_price(side, level);
    if (OrderSide.valueOf(side).equals(B)) 
      return bidPriceToOrderBookLevel.get(concernedPriceLevel).getSize();
    else if (OrderSide.valueOf(side).equals(S))
      return askPriceToOrderBookLevel.get(concernedPriceLevel).getSize();
    return 0;
  }
  
  private synchronized void addOrder(Order order) {
    if (order.isBuy()) {
      bidSideOrders.add(order);
      bidPriceToOrderBookLevel.merge(order.getPrice(), new OrderBookLevel(order.getPrice(), 1, order.getSize()),
          addOrderRemappingFunction);
    } else if (order.isSell()) {
      askSideOrders.add(order);
      askPriceToOrderBookLevel.merge(order.getPrice(), new OrderBookLevel(order.getPrice(), 1, order.getSize()),
          addOrderRemappingFunction);
    }

  }

  public boolean doesOrderExist(int orderId) {
    return orderIdToOrderSide.keySet().contains(orderId);
  }
  
  private BiFunction<OrderBookLevel, OrderBookLevel, OrderBookLevel> addOrderRemappingFunction = 
      (existingPriceLevel, newPriceLevel) -> {
    int newSize = existingPriceLevel.getSize() + newPriceLevel.getSize();
    int newOrderCount = existingPriceLevel.getNumberOfOrders() + newPriceLevel.getNumberOfOrders();
    return new OrderBookLevel(existingPriceLevel.getPrice(), newOrderCount, newSize);
  };
  
  private synchronized void modifyOrder(int orderId, int newSize, List<Order> specifiedSideOrders) {
    Order foundOrder = removeOrder(orderId, specifiedSideOrders);
    removeOrderQuantityFromPriceLevel(foundOrder, newSize, bidPriceToOrderBookLevel);
    Order modifiedOrder = new Order(orderId, foundOrder.getSide().getOrderSide(), foundOrder.getPrice(), newSize);
    addOrder(modifiedOrder);
  }

  private synchronized Order removeOrder(int orderId, List<Order> specifiedSideOrders) {
    Order orderToBeRemoved = specifiedSideOrders.stream()
        .filter(order -> orderId == order.getOrderId()).findFirst()
        .orElseThrow(() -> {
          return new OrderDoesNotExistException("Order does not exist!");
        });
    specifiedSideOrders.remove(orderToBeRemoved);
    return orderToBeRemoved;
  }
  
  private void removeOrderQuantityFromPriceLevel(Order foundOrder, int newSize, Map<Double, OrderBookLevel> priceToOrderBookLevel) {
    OrderBookLevel existingPriceLevel = priceToOrderBookLevel.get(foundOrder.getPrice());
    int remainingSize = existingPriceLevel.getSize() - foundOrder.getSize();
    int remainingOrderCount = existingPriceLevel.getNumberOfOrders() - 1;
    if (remainingSize < 0) {
      throw new OrderQuantityMismatchException(
          "Could not modify bid order book due to quantity mismatch ! Existing Price Level Quantity: "
              + existingPriceLevel.getSize() + " Quantity to be removed: " + foundOrder.getSize());
    } else if (remainingSize == 0)
        priceToOrderBookLevel.remove(foundOrder.getPrice());
    else {
      existingPriceLevel.setNumberOfOrders(remainingOrderCount);
      existingPriceLevel.setSize(remainingSize);
    }
  }
}