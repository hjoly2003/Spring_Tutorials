package com.marcobehler.stripepayments.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcobehler.stripepayments.dto.CreatePayment;
import com.marcobehler.stripepayments.dto.CreatePaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;


@RestController
public class PaymentController {

  /**
   * [N] Note: the {@code stripe.api.key} property is used in {@link com.marcobehler.stripepayments.StripePaymentsApplication StripePaymentsApplication} to initialize the {@code Stripe.apiKey}
   */
  @PostMapping("/create-payment-intent")
  public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {

      PaymentIntentCreateParams params =
          PaymentIntentCreateParams.builder()
          .setAmount(15 * 100L)
          .setCurrency("usd")
          // [N]
          .addPaymentMethodType("card")
          /* // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
          .setAutomaticPaymentMethods(
              PaymentIntentCreateParams.AutomaticPaymentMethods
              .builder()
              .setEnabled(true)
              .build()
          ) */
          .build();
  
      // Create a PaymentIntent with the order amount and currency
      PaymentIntent paymentIntent = PaymentIntent.create(params);
  
      return new CreatePaymentResponse(paymentIntent.getClientSecret());
  }
    
}
