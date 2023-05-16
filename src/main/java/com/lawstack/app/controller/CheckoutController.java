package com.lawstack.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.CardSubscription;
import com.lawstack.app.model.PaymentRequest;

import com.lawstack.app.service.PaymentService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.SubscriptionListParams;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/checkout")
@Slf4j
public class CheckoutController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SubscriptionService subService;

    @Autowired
    private SellerService sellerService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PaymentRequest payment) {

        log.info("Request for checkout page recived");

        String s = this.paymentService.paymentCheckout(payment.getType(), payment.getEmail());

        if (s != null) {

            return ResponseEntity.status(201).body(s);
        }

        return ResponseEntity.status(404).body(null);

    }

    private final String endpointSecret = "whsec_dab51112fcb3a579d7ecd42b56f103c1fbbe197877caf3a2b810c7d76f4d14b4";

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhookEvent(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {

        try {
            Event event = Webhook.constructEvent(payload, signature, endpointSecret);

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {

                stripeObject = dataObjectDeserializer.getObject().get();

                switch (event.getType()) {
                    // ! may be handle later
                    case "payment_intent.succeeded":

                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                        // Customer customer = null;
                        try {
                            // * get costomer form database if not exist make new one
                            // customer = this.subService.retrievCustomer(paymentIntent.getCustomer());

                            // log.info("customer : {}", customer);

                        } catch (Exception e) {
                            log.error("Customer alreday exist in data base");
                        }

                        log.info("PaymentIntent was successful!");
                        break;
                    case "payment_method.attached":
                        // PaymentMethod attached
                        // PaymentMethod attached
                        System.out.println("PaymentMethod was attached to a Customer!");
                        break;

                    case "checkout.session.completed":
                        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                        if (session != null) {
                            String customerId = session.getCustomer();

                            try {
                                Customer customer = Customer.retrieve(customerId);
                                SubscriptionListParams params = SubscriptionListParams.builder()
                                        .setCustomer(customerId)
                                        .build();
                                SubscriptionCollection subscriptions = Subscription.list(params);

                                for (Subscription subscription : subscriptions.getData()) {
                                    String subscriptionId = subscription.getId();
                                    String discountId = subscription.getDiscount() != null
                                            ? subscription.getDiscount().getId()
                                            : null;
                                    Price price = Price
                                            .retrieve(subscription.getItems().getData().get(0).getPrice().getId());
                                    Product product = Product.retrieve(price.getProduct());

                                    String customerEmail = customer.getEmail();

                                    // Update the seller info after subscription
                                    CardSubscription card = new CardSubscription();
                                    card.setSubscription(product.getName());

                                    this.sellerService.addSubscription(card, customerEmail);

                                    this.subService.addCustomer(customerEmail, customerId, subscriptionId, discountId);
                                }
                            } catch (StripeException e) {
                                // Handle any Stripe API errors
                                e.printStackTrace();
                            }
                        }
                        break;

                }
            } else {
                log.error("Error while making the event object serialization");
            }
            // stripeObject.toJson();
            // Handle the event

            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            log.error("Error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
