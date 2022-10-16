package com.circuitbreaker.services.failures;

import com.circuitbreaker.exceptions.ProductServiceException;

public class SucceedXTimesFailYTimesThenSucceed implements IFailure {
  int successHowMany;
  int failHowMany;
  int successCount, failCount;

  public SucceedXTimesFailYTimesThenSucceed(int successHowMany, int failHowMany) {
    this.successHowMany = successHowMany;
    this.failHowMany = failHowMany;
  }

  @Override
  public void fail() {
    if (successCount < successHowMany) {
      successCount++;
      return;
    }
    if (failCount < failHowMany) {
      failCount++;
      throw new ProductServiceException("Product search failed");
    }
    return;
  }
}
