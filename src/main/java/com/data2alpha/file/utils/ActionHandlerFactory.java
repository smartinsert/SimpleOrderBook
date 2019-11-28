package com.data2alpha.file.utils;

import java.util.regex.Pattern;

import com.data2alpha.utils.ActionHandler;
import com.data2alpha.utils.AddOrder;
import com.data2alpha.utils.GetPrice;
import com.data2alpha.utils.GetSize;
import com.data2alpha.utils.ModifyOrder;
import com.data2alpha.utils.RemoveOrder;

public class ActionHandlerFactory {
  private Pattern pattern = Pattern.compile("\\s+");
  
  public ActionHandler getActionFor(String line) {
    if (isAddOrder(line))
      return new AddOrder();
    else if (isModifyOrder(line))
      return new ModifyOrder();
    else if (isRemoveOrder(line))
      return new RemoveOrder();
    else if (isGetPrice(line))
      return new GetPrice();
    else if (isGetSize(line))
      return new GetSize();
    return null;
  }

  private boolean isAddOrder(String line) {
    String firstWord = pattern.split(line, 4)[0];
    return firstWord.equalsIgnoreCase("add");
  }
  
  private boolean isModifyOrder(String line) {
    String firstWord = pattern.split(line, 4)[0];
    return firstWord.equalsIgnoreCase("modify");
  }

  private boolean isRemoveOrder(String line) {
    String firstWord = pattern.split(line, 4)[0];
    return firstWord.equalsIgnoreCase("remove");
  }
  
  private boolean isGetPrice(String line) {
    String firstWord = pattern.split(line, 4)[0];
    String secondWord = pattern.split(line, 4)[1];
    return firstWord.equalsIgnoreCase("get") && secondWord.equalsIgnoreCase("price");
  }

  private boolean isGetSize(String line) {
    String firstWord = pattern.split(line, 4)[0];
    String secondWord = pattern.split(line, 4)[1];
    return firstWord.equalsIgnoreCase("get") && secondWord.equalsIgnoreCase("size");
  }
  
}
