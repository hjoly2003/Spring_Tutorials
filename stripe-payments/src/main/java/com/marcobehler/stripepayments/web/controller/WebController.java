package com.marcobehler.stripepayments.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @Value("${stripe.public.key}")
    private Object stripePublicKey;

    @GetMapping("/")
    public String home(Model model) {
        // [N]:stripePublicKey - we pass the stripePublicKey to the model of the presentation layer.
        model.addAttribute("stripePublicKey", stripePublicKey);
        return "index";
    }
}
