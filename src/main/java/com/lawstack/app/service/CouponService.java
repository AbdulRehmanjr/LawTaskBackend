package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Coupon;

public interface CouponService {
    

    Coupon saveCoupon(Coupon coupon);

    Coupon getCouponByName(String name);

    List<Coupon> getAllCoupon();

    void deleteCoupon(String id);
}
