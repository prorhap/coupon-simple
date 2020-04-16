package com.github.prorhap.coupon.simple;

import com.github.prorhap.coupon.simple.common.utils.CouponDateUtils;
import com.github.prorhap.coupon.simple.model.Coupon;
import com.github.prorhap.coupon.simple.model.CouponFactory;
import com.github.prorhap.coupon.simple.model.User;

import java.util.Date;

public class TestCouponFactory {
    private CouponDateUtils couponDateUtils = new CouponDateUtils();

    public Coupon createDefaultCoupon(String couponCode, Date validFrom, Date expireAt) {
        return new CouponFactory().create(couponCode, validFrom, expireAt);
    }

    public Coupon create(String couponCode, boolean used, boolean issued, String validFrom, String expireAt, User user, String issuedAt, String usedAt) throws Exception {
        return new Coupon(
                couponCode,
                used,
                issued,
                couponDateUtils.parseDate(validFrom),
                couponDateUtils.parseDate(expireAt),
                user,
                couponDateUtils.parseDateTime(issuedAt),
                couponDateUtils.parseDateTime(usedAt)
        );
    }
}
