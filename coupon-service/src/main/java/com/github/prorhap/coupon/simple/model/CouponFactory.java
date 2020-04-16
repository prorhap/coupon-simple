package com.github.prorhap.coupon.simple.model;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CouponFactory {
    public Coupon create(String couponCode, Date validFrom, Date expireAt) {
        return new DiscountCoupon(couponCode, validFrom, expireAt, false, false);
    }
}
