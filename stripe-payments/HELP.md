## Origin

This tutorial is coming from

* [Live Coding #1: Stripe payment integration - By @MarcoCodes
  ](https://www.youtube.com/watch?v=BIDNKRluql4) and
* [Live Coding #2: Stripe payment integration - By @MarcoCodes](https://www.youtube.com/watch?v=gUqMdwgEAIQ)

I've tried to reproduce the tutorial from the following: https://stripe.com/docs/payments/quickstart?lang=java.
All the modifications are annotated with `[N]`.
My personal modifications are annotated with `[me]`.

## Status

There's no database yet.
There are no test: be it unit or integration whatsoever.

## Changes

* Renaming of `Checkout` form into `FeatureReqForm`. This is the page specifying what we want and the price we're willing to pay.
* Renaming of `CreatePaymentResponse` into `PaymentIntentResponse`
* Deletion of dto `CreatePayment `since it has been replaced by `FeatureReqForm`
* Added `WebController.getCompletion()`. Note, the name of that method refers to the *Payment Completion Page* to which we get redirected once the payment has been completed.

## Technologies Used

* `[N]:stripePublicKey` - Management of the Stripe Public Key which is mandatory for constructing the Stripe payment elements in the *checkout form*.
* `[N]:mvc` - Spring Boot mvc

  * `[N]:mvc-validation` - Bean validation.
  * `[N]:th-variables` - Usage of Thymeleaf variables
* `[N]:webhooks` - A controller for handling incoming *Stripe webhooks events*. For this tutorial, it uses a single endpoint (http://localhost:8080/stripe/events) for handling Stripe events (see [Triggering actions with webhooks | Stripe Documentation](https://stripe.com/docs/payments/handling-payment-events)). In development, to make this work locally, you need to have the *Stripe CLI* intalled. Then, prior to starting your application, the *Stripe CLI* must be configured to send events to the application with the following commands:

  ```bash
  stripe login
  stripe listen --forward-to http://localhost:8080/stripe/events
  ```
  Later, when a *payment intent* gets created by the application, the *Stripe CLI* will generate events related to the lifecycle of this *payment intent*.
* `[me]:customizing-stripe-data-struture` - To enrich Stripe Obects with custom data. Doing so enables us to see that customized data in the Stripe Dashboard.
* `[me]:pmt-metadata` - When Stripe makes a GET request to display the *payment completion page*, I used the payment's metadata to recover the template arguments of the *checkout page* (used for the completion page).

## How to run it
From the project's root directory, type the following:
```bash
mvn spring-boot:run
```
When started, get to the following url: http://localhost:8080.
