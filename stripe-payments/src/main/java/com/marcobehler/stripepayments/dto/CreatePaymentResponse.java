package com.marcobehler.stripepayments.dto;

public class CreatePaymentResponse {
  private String clientSecret;
  public CreatePaymentResponse(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  String getClientSecret() {
    return clientSecret;
  }

  void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}