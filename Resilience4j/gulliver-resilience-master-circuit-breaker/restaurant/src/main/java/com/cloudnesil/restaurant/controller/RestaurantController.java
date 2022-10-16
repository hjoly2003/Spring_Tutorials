package com.cloudnesil.restaurant.controller;

import com.cloudnesil.restaurant.domain.Restaurant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("restaurants")
public class RestaurantController {

    @GetMapping
    public Flux<Restaurant> getRestaurants(){
        return Flux.just(
          new Restaurant(1l, "Fiske"),
          new Restaurant(2l, "Alibaba Kebab"),
          new Restaurant(3l, "Apple Pizzeria"),
          new Restaurant(4l, "Simit Palace"),
          new Restaurant(5l,"Niagara Soup Place")
        );
    }
}
