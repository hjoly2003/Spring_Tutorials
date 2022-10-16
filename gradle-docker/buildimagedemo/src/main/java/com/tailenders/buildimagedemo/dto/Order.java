package com.tailenders.buildimagedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private int amount;
  private String address;
  private boolean isPaid;
  private String[] items;
}
