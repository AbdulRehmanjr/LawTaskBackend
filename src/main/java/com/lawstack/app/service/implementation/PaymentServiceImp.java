package com.lawstack.app.service.implementation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Order;
import com.lawstack.app.model.OrderPayment;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.Subscription;
import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.service.OrderPaymentService;
import com.lawstack.app.service.PaymentService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.SubscriptionService;
import com.lawstack.app.service.UserDashBoardService;
import com.lawstack.app.utils.enums.JobNumber;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImp implements PaymentService {

    @Autowired
    private SubscriptionService subService;

    @Value("${stripe_secert_key}")
    private String STRIPE_API;

    @Value("${Dew_Dropper}")
    private String dewString;

    @Value("${Sprinkle_Starter}")
    private String sprinkleString;

    @Value("${Rain_Maker}")
    private String rainString;

    @Autowired
    private OrderPaymentService orderPaymentService;

    @Autowired
    private UserDashBoardService uDashBoardService;

    @Autowired
    private SellerService sellerService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPE_API;
    }

    @Value("${web_domain}")
    private String Domain;
    
    @Override
    public String paymentCheckout(String type, String email) {
        Customer customer = checkAndCreateCustomer(email);
        // Coupon coupon = createCoupon();

        if (customer == null) {
            return null;
        }
        

        JobNumber currentSubscription = getSubscriptionLevel(email);

        JobNumber selectedSubscription = null;

        // Determine the selected subscription based on the current subscription level
        if (currentSubscription == null) {
            // New subscription
            selectedSubscription = JobNumber.valueOf(type.toUpperCase());
        } else if (currentSubscription == JobNumber.DEWDROPPER) {
            if (type.equals("SPRINKLE") || type.equals("RAINMAKER")) {
                selectedSubscription = JobNumber.valueOf(type.toUpperCase());
            }
        } else if (currentSubscription == JobNumber.SPRINKLE) {
            if (type.equals("RAINMAKER")) {
                selectedSubscription = JobNumber.valueOf(type.toUpperCase());
            }
        } else if (currentSubscription == JobNumber.RAINMAKER) {
            // The user is already subscribed to the Rain Maker package
            return null;
        }

        if (selectedSubscription != null) {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(Domain + "/home/job-list")
                    .setCancelUrl(Domain + "/home/job-list")
                    .setCustomer(customer.getId())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(getPriceToken(selectedSubscription.name()))
                                    .build())
                    .build();

            Session session;
            try {
                session = Session.create(params);
                return session.getUrl();
            } catch (StripeException e) {
                log.info("ERROR: {}", e.getMessage());
                return null;
            }
        }

        return null;
    }

    private JobNumber getSubscriptionLevel(String email) {
        Seller seller = this.sellerService.getByEmail(email);
        if (seller != null) {
            String type = seller.getSellerType();
            if(type.equals("NONE")){
                return null;
            }
            try {
                return JobNumber.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("MESSAGE : {}",e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String projectPayment(Order order) {
        

        Customer customer = checkAndCreateCustomer(order.getCustomerEmail(), order.getCustomerName());
        long price = (long) (order.getPrice() * 100);
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(Domain+"/home/messages")
                .setCancelUrl(Domain+"/home/messages")
                .setCustomer(customer.getId())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(price)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(order.getJob().getJobName())
                                                                .build())
                                                .build())
                                .build())
                .putMetadata("user", order.getUser().getUserId())
                .build();
        Session session;

        try {
            session = Session.create(params);

            OrderPayment pay = new OrderPayment();
            pay.setCustomerId(order.getCustomerId());
            pay.setStripeId(customer.getId());
            pay.setEmail(order.getCustomerEmail());
            pay.setName(order.getCustomerName());
            pay.setJobId(order.getJob().getJobId());
            pay.setSellerId(order.getUser().getUserId());
            pay.setPrice(order.getPrice());

            UserDashboard dash = this.uDashBoardService.getInfoByUserId(order.getUser().getUserId());

            if (dash != null) {
                dash.setRevenue(dash.getRevenue() + order.getPrice());
                this.uDashBoardService.updateDashboard(dash);
            }

            this.orderPaymentService.saveOrderPayment(pay);

            return session.getUrl();
        } catch (Exception e) {

            log.info("ERROR: {}", e.getMessage());
            return null;
        }
    }

    private String getPriceToken(String type) {

        if (type.equalsIgnoreCase("DEWDROPPER")) {
            return this.dewString;
        } else if (type.equalsIgnoreCase("SPRINKLE")) {
            return this.sprinkleString;
        } else {
            return this.rainString;
        }
    }

    private Customer checkAndCreateCustomer(String email, String name) {

        Subscription sub = this.subService.getCustomerByEmail(email);

        if (sub == null) {
            try {
                CustomerCreateParams params = CustomerCreateParams.builder()
                        .setEmail(email)
                        .setName(name)
                        .build();
                Customer customer = Customer.create(params);

                return customer;
            } catch (StripeException e) {
                log.error("Error : {}", e.getMessage());
                return null;
            }
        }
        try {
            log.info("retriving");
            Customer customer = Customer.retrieve(sub.getCustomerId());

            return customer;
        } catch (StripeException e) {
            log.error("Error : {}", e.getMessage());
            return null;
        }

    }

    private Customer checkAndCreateCustomer(String email) {

        Subscription sub = this.subService.getCustomerByEmail(email);

        if (sub == null) {
            try {
                CustomerCreateParams params = CustomerCreateParams.builder()
                        .setEmail(email)
                        .build();
                Customer customer = Customer.create(params);

                return customer;
            } catch (StripeException e) {
                log.error("Error : {}", e.getMessage());
                return null;
            }
        }
        try {
            log.info("retriving");
            Customer customer = Customer.retrieve(sub.getCustomerId());

            return customer;
        } catch (StripeException e) {
            log.error("Error : {}", e.getMessage());
            return null;
        }

    }

    private Coupon createCoupon() {
        try {
            BigDecimal off = new BigDecimal("2");
            CouponCreateParams params = CouponCreateParams.builder()
                    .setPercentOff(off)
                    .setDuration(CouponCreateParams.Duration.ONCE)
                    .build();

            Coupon coupon = Coupon.create(params);

            log.info("Coupon Id : {}", coupon.getId());
            return coupon;
        } catch (StripeException e) {
            log.error("Error : {}", e.getMessage());
            return null;
        }
    }

    @Override
    @Deprecated
    public String removeSubscription(String email) {

        Subscription response = this.subService.getCustomerByEmail(email);

        return null;

    }

    @Override
    public String getSubscriptionId(String email) {

        log.info("geting the subscription by email");

        return this.subService.getCustomerByEmail(email).getSubscriptionId();
    }


    @Override
    public String updateSubscription(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSubscription'");
    }

}
