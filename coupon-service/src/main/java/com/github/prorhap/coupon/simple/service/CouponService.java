package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.dto.CouponCreationRequest;
import com.github.prorhap.coupon.simple.dto.CouponIssueRequest;
import com.github.prorhap.coupon.simple.dto.CouponResponse;
import com.github.prorhap.coupon.simple.dto.CouponUseResult;

import java.util.List;

public interface CouponService {

    void createCoupon(CouponCreationRequest couponCreationRequest);
    List<CouponResponse> getTodayExpiredCoupons();
    CouponUseResult useCoupon(String couponCode);
    CouponResponse issueCoupon(CouponIssueRequest couponIssueRequest);
    void cancelCoupon(String couponCode);

    void sendNotification(int expiryDaysBefore);
}
