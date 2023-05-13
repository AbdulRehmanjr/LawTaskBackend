package com.lawstack.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/checkout")
@Slf4j
public class CheckoutController {

    @Value("${stripe_secert_key}")
    private String STRIPE_API;

    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPE_API;
    }

    @PostMapping("/api/charge")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info(paymentRequest.toString());
        try {
            // Create a charge
            Charge charge = Charge.create(
                    new ChargeCreateParams.Builder()
                            .setAmount(1999L) // Amount in cents (e.g., $19.99)
                            .setCurrency("usd")
                            .setDescription("Payment description")
                            .setSource(paymentRequest.getToken())
                            .build());

            // Handle successful charge
            return ResponseEntity.status(201).body("Payment processed successfully!");
        } catch (StripeException e) {
            // Handle Stripe API error
            e.printStackTrace();
            return ResponseEntity.status(501).body("Error processing payment: " + e.getMessage());
        }
    }
    @PostMapping("/payment-intent")
    public String createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
      log.info("{}",paymentRequest.getAmount());
  
      PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
          .setAmount(7L) // Amount in cents (e.g., $19.99)
          .setCurrency(paymentRequest.getCurrency())
          .setDescription(paymentRequest.getDescription())
          .build();
  
      try {
        PaymentIntent paymentIntent = PaymentIntent.create(createParams);
        return paymentIntent.getClientSecret();
      } catch (StripeException e) {
        e.printStackTrace();
        // Handle payment intent creation error
        return "Error creating payment intent: " + e.getMessage();
      }
    }
    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession() {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(2000L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Your Product")
                                                                .build())
                                                .build())
                                .setQuantity(1L)
                                .build())
                .setSuccessUrl("http://localhost:4200/home")
                .setCancelUrl("https://localhost:4200/home")
                .build();

        String sessionId;
        try {
            Session session = Session.create(params);
            sessionId = session.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Error creating Stripe checkout session", e);
        }

        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", sessionId);
        return responseData;
    }
}
