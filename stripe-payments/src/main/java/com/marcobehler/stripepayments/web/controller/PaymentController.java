package com.marcobehler.stripepayments.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcobehler.stripepayments.dto.PaymentIntentResponse;
import com.marcobehler.stripepayments.web.form.FeatureReqForm;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.validation.Valid;

@RestController
public class PaymentController {

    /**
     * A post-request for preparing the <em>checkout</em> form.<p>
     * When the Thymeleaf {@code checkout.html} template is loaded, it loads the {@code checkout.js} script whose {@code initialize()} method posts a call to this method. This method creates a {@link com.stripe.model.PaymentIntent}.<p>
     * [N] Note: the {@code stripe.api.key} property is used in {@link com.marcobehler.stripepayments.StripePaymentsApplication StripePaymentsApplication} to initialize the {@code Stripe.apiKey}
     * 
     * @param featureReqForm [N]:mvc-validation - The {@code @Valid} annotation triggers the form validation
     * @return A {@link com.marcobehler.stripepayments.dto.PaymentIntentResponse CreatePaymentResponse}, a dto object holding the client secret required by Stripe for building the <em>checkout</em> form.
     * @throws StripeException
     */
    @PostMapping("/create-payment-intent")
    public PaymentIntentResponse createPaymentIntent(@RequestBody @Valid FeatureReqForm featureReqForm) throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(featureReqForm.amount() * 100L)
                .setCurrency("usd")
                // [me] Store featureReqForm data in pmt's meta-data to make it available to the payment completion page.
                .putMetadata("featureRequest", featureReqForm.featureRequest())
                .putMetadata("amount", featureReqForm.amount().toString())
                .putMetadata("email", featureReqForm.email())
                .addPaymentMethodType("card") // [me] Set the checkout form to handle only credit-card payments.
                .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new PaymentIntentResponse(paymentIntent.getClientSecret());
    }

}
