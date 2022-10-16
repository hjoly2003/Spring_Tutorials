package com.circuitbreaker.services.delays;

public class NSecondsDelay implements IDelay {
  int delayInSeconds;

  public NSecondsDelay(int delayInSeconds) {
    this.delayInSeconds = delayInSeconds;
  }

  @Override
  public void delay() {
    try {
      Thread.sleep(delayInSeconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
