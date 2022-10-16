package com.circuitbreaker.services.failures;

import com.circuitbreaker.exceptions.ProductServiceException;

public class SucceedNTimesThenFail implements IFailure {

  int n;
  int successCount;

  public SucceedNTimesThenFail(int n) {
    this.n = n;
  }

  @Override
  public void fail() {
    if (successCount < n) {
      successCount++;
      return;
    }
    throw new ProductServiceException("Error occurred during product search");
  }
}
