package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Coupon;
import com.lawstack.app.model.Subscription;
import com.lawstack.app.service.CouponService;
import com.lawstack.app.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;




@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private SubscriptionService subService;

    @PostMapping("/save")
    ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {

        Coupon response = this.couponService.saveCoupon(coupon);

        if (response == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);
    }
    @PostMapping("/giveaway/{email}/{couponId}")
    ResponseEntity<?> giveAway(@PathVariable String email,@PathVariable String couponId) {

        Subscription subscription = this.subService.getCustomerByEmail(email);

        Coupon coupon = this.couponService.getById(couponId);

        if (subscription == null || coupon == null) {
            return ResponseEntity.status(404).body(null);
        }

        subscription.setDiscountId(coupon.getId());

        this.subService.updateSubscription(subscription);

        return ResponseEntity.status(201).body(subscription);
    }
    @GetMapping("/name/{name}")
    ResponseEntity<?> getCouponByName(@PathVariable String name) {

        Coupon response = this.couponService.getCouponByName(name);

        if (response == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllCoupons() {

        List<Coupon> response = this.couponService.getAllCoupon();

        try {
            if(response.isEmpty()){
                return ResponseEntity.status(404).body(null);
            
            }
        } catch (Exception e) {
          log.error("Coupon List in Empty");
          return ResponseEntity.status(404).body(null);
        }
        
        
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    void deleteCoupon(@PathVariable String id) {

        this.couponService.deleteCoupon(id);
    }
}
