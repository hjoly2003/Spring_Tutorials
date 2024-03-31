package com.marcobehler.stripepayments.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcobehler.stripepayments.web.form.FeatureReqForm;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.validation.Valid;

@Controller
public class WebController {
    
    // [N]:stripePublicKey
    @Value("${stripe.public.key}")
    private Object stripePublicKey;

    /**
     * Triggered when the client tries to access the index page.
     * @param model
     * @return
     */
    @GetMapping("/")
    public String home(Model model) {
        @SuppressWarnings("null")
        FeatureReqForm emptyRequest = new FeatureReqForm(null,null,null/* ,"fr" / * [me]:i18 */);
        model.addAttribute("featureReqForm", emptyRequest);
        return "index";
    }

    /**
     * [N]:mvc - Triggered when the client submit the <em>feature request</em> form.<p/>
     * Note that, on the client side, the <em>feature request</em> form is directly submitted to the Stripe sever via the {@code checkout.js} script and the Stripe javascript library.
     * @param featureReqForm [N]:mvc-validation - The {@code @Valid} annotation triggers the form validation
     * @param bindingResult [N]:mvc-validation - For Spring MVC validation. Note: this parameter must not be the last one in the method or else it won't work.
     * @param model Holds all the variables of the "index" page template.
     * @return The name of the Thymeleaf template file (either index or checkout (without the ".html" suffix)) located under {@code src/main/resources/templates} directory.
     */
    @PostMapping("/")
    public String checkout(@ModelAttribute @Valid FeatureReqForm featureReqForm, BindingResult bindingResult, Model model) {

        // [N]:mvc-validation
        if (bindingResult.hasErrors()) {

            // [N] Timeleaf will take care of error messages in the index form.
            return "index";
        }

        // [N]:stripePublicKey - we pass the stripePublicKey to the model of the presentation layer.
        model.addAttribute("stripePublicKey", stripePublicKey);

        model.addAttribute("amount", featureReqForm.amount());
        model.addAttribute("email", featureReqForm.email());
        model.addAttribute("featureRequest", featureReqForm.featureRequest()); 
        
        return "checkout";
    }
    
    /**
     * [me] This call is invoked by Stripe on successful submission of the <em>checkout form</em>.<p>
     * The url of this call got defined by the {@code confirmParams.return_url} parameter found in the {@code stripe.confirmPayment()} call within {@code checkout.js} script.
     * @param paymentIntentID The ID of the <em>payment intent</em> that was created by Stripe.
     * @param paymentIntentClientSecret
     * @param redirectStatus
     * @param model
     * @return The Thymeleaf {@code checkout} template used for the <em>payment completion page</em>.
     * @throws StripeException
     */
    @GetMapping("/checkout")
    public String getCompletion(
        @RequestParam(value="payment_intent", required=true) String paymentIntentID,
        @RequestParam(value="payment_intent_client_secret", required=true) String paymentIntentClientSecret,
        @RequestParam(value="redirect_status", required=true) String redirectStatus, Model model) throws StripeException {
        
        // [N]:stripePublicKey - we pass the stripePublicKey to the model of the presentation layer.
        model.addAttribute("stripePublicKey", stripePublicKey);

        // [me]:pmt-metadata - We fetch the pmt's metadata to make it available to the completion page.
        PaymentIntent pmtIntent = PaymentIntent.retrieve(paymentIntentID);
        Map<String, String> pmtIntentMap = pmtIntent.getMetadata();
        model.addAttribute("amount", pmtIntentMap.get("amount"));
        model.addAttribute("email", pmtIntentMap.get("email"));

        model.addAttribute("payment_intent", paymentIntentID);
        model.addAttribute("payment_intent_client_secret", paymentIntentClientSecret);
        model.addAttribute("redirect_status", redirectStatus);

        return "checkout";
    }
}
