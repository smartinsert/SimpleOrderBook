package com.data2alpha.service;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import com.data2alpha.file.utils.ActionHandlerFactory;
import com.data2alpha.utils.ActionHandler;

public class OrderBookService {
  private static ActionHandlerFactory actionHandlerFactory = new ActionHandlerFactory();
  public static void main(String[] args) {
    String filePath = args[0];
    long startTime = System.nanoTime();
    try {
      Files.lines(Paths.get(filePath), Charset.defaultCharset())
      .map(line -> line.trim())
      .map(line -> line.replaceAll("//","\n"))
      .filter(line -> !line.isEmpty())
      .forEach(line -> {
        ActionHandler actionHandler = actionHandlerFactory.getActionFor(line);
        actionHandler.actOn(line);
      });
    } catch (Exception e) {
      System.err.println("Could not parse line due to " + e);
    }
    long endTime = System.nanoTime();
    System.out.println("Total Execution Time: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms.");
  }

}
