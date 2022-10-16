package com.tailenders.buildimagedemo.resource;

import com.tailenders.buildimagedemo.dto.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationResource {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Order getOrder() {
    Order dummyOrder = new Order();
    dummyOrder.setAmount(544);
    dummyOrder.setPaid(true);
    dummyOrder.setAddress("Chennai");
    dummyOrder.setItems(new String[] {"bat"});
    return dummyOrder;
  }
}
