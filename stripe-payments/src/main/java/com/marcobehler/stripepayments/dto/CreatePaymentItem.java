package com.marcobehler.stripepayments.dto;

import com.google.gson.annotations.SerializedName;

class CreatePaymentItem {
  @SerializedName("id")
  String id;

  public String getId() {
    return id;
  }
}