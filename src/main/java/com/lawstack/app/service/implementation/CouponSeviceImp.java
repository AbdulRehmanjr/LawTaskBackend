package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.sym.Name;
import com.lawstack.app.model.Coupon;
import com.lawstack.app.repository.CouponRepository;
import com.lawstack.app.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CouponSeviceImp implements CouponService{

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public Coupon saveCoupon(Coupon coupon) {

        log.info("Saving coupon to database");

        String id = UUID.randomUUID().toString();
        coupon.setId(id);

        try {
            coupon = this.couponRepository.save(coupon);
        } catch (Exception e) {
            log.info("Error :{}  Message : {}",e.getCause(),e.getMessage());
        }
        return coupon;

    }
    
    @Override
    public Coupon getCouponByName(String name) {
      log.info("Geting coupon by Name");

      return this.couponRepository.findByName(name);
    }
    @Override
    public List<Coupon> getAllCoupon() {

        log.info("Getting all coupons");
        return this.couponRepository.findAll();
    }

    @Override
    public void deleteCoupon(String id) {
        log.info("Deleting the coupon from database");
        Coupon coupon = this.couponRepository.findById(id).get();
        if(coupon != null){
            this.couponRepository.delete(coupon);
        }
        
    }

}
