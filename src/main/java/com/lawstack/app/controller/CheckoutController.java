package com.lawstack.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.CardSubscription;
import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.Order;

import com.lawstack.app.model.PaymentRequest;
import com.lawstack.app.model.Seller;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.EmailService;

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

import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private DashboardService dashService;

    @Autowired
    private EmailService emailService;

    private String customerId = "";

    @Value("${stripe_webhook}")
    private String endpointSecret;

    @PostMapping("/cancel/{email}")
    ResponseEntity<String> cancelSubscription(@PathVariable String email) {

        log.info("Request to cancel the subscription");
        String id = this.paymentService.getSubscriptionId(email);

        Subscription subscription;
        try {
            subscription = Subscription.retrieve(id);
            this.customerId = subscription.getCustomer();
            subscription.cancel();
            Seller seller = this.sellerService.getByEmail(email);

            seller.setSellerType("NONE");

            this.sellerService.updateJobStatus(seller);
        } catch (StripeException e) {
            log.error("Error: {} Message: {}", e.getStatusCode(), e.getMessage());
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body("canceled");

    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PaymentRequest payment) {

        log.info("Request for checkout page received");

        String url = this.paymentService.paymentCheckout(payment.getType(), payment.getEmail());

        if (url != null) {

            return ResponseEntity.status(201).body(url);
        }
        return ResponseEntity.status(404).body(null);

    }

    @PostMapping("/project")
    public ResponseEntity<?> projectSession(@RequestBody Order order) {

        log.info("Request for checkout page received");

        String s = this.paymentService.projectPayment(order);

        if (s != null) {

            return ResponseEntity.status(201).body(s);
        }

        return ResponseEntity.status(404).body(null);

    }

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

                        // PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                        // Customer pay_Customer = null;
                        // try {
                        // // * get costomer form database if not exist make new one

                        // log.info("Payment Intent : {}", paymentIntent);
                        // log.

                        // } catch (Exception e) {
                        // log.error("Customer alreday exist in data base");
                        // }

                        log.info("PaymentIntent was successful!");
                        break;
                    case "payment_method.attached":
                        // PaymentMethod attached
                        // PaymentMethod attached
                        System.out.println("PaymentMethod was attached to a Customer!");
                        break;

                        case "checkout.session.completed":
                        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                        log.info("Session: {}", session);
                        if (session != null) {
                            if ("subscription".equals(session.getMode())) {
                                String subscriptionId = session.getSubscription();
                                String customerId = session.getCustomer();
                                String email = "";
                    
                                try {
                                    Customer customer = Customer.retrieve(customerId);
                                    Subscription subscription = Subscription.retrieve(subscriptionId);
                    
                                    String discountId = subscription.getDiscount() != null ? subscription.getDiscount().getId() : null;
                                    Price price = Price.retrieve(subscription.getItems().getData().get(0).getPrice().getId());
                                    Product product = Product.retrieve(price.getProduct());
                    
                                    email = customer.getEmail();
                    
                                    // Update the seller info after subscription
                                    CardSubscription card = new CardSubscription();
                                    card.setSubscription(product.getName());
                    
                                    this.sellerService.addSubscription(card, email, price.getUnitAmount());
                    
                                    this.subService.addCustomer(email, customerId, subscriptionId, discountId);
                    
                                    this.emailService.sendMail(email, "Purchase of Subscription",
                                            "Thanks for purchasing the subscription. Please visit your dashboard to add jobs.");
                                } catch (StripeException e) {
                                    log.error("ERROR: {} MESSAGE: {}", e.getCause(), e.getMessage());
                                } catch (Exception e) {
                                    log.error("ERROR: {}", e.getMessage());
                                }
                            } else {
                                PaymentIntent payment = PaymentIntent.retrieve(session.getPaymentIntent());
                                Dashboard dashboard = new Dashboard();
                                dashboard.setIncome(payment.getAmount() / 100.0);
                                this.dashService.updateDashboard(dashboard);
                            }
                        }
                        break;
                    case "customer.subscription.deleted":

                        try {
                            Customer response = Customer.retrieve(this.customerId);

                            this.subService.deleteSubscription(response.getEmail());
                        } catch (StripeException e) {
                            log.error("Error: {} Message: {}", e.getCause(), e.getMessage());
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
