package com.github.prorhap.coupon.simple.api.controller;

import com.github.prorhap.coupon.simple.dto.*;
import com.github.prorhap.coupon.simple.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
public class CouponController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CouponService couponService;

    @Inject
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping(value="/v1/coupons")
    public void createCoupon(@RequestBody CouponCreationRequest couponCreationRequest) {
        logger.info("createCoupon with {}", couponCreationRequest);
        couponService.createCoupon(couponCreationRequest);
    }

    @PutMapping(value="/v1/coupons/issue")
    public CouponResponse issueCoupon(@RequestBody CouponIssueRequest couponIssueRequest) {
        logger.info("issueCoupon with {}", couponIssueRequest);
        return couponService.issueCoupon(couponIssueRequest);
    }

    @PutMapping(value="/v1/coupons/use")
    public CouponUseResult useCoupon(@RequestBody CouponDefaultRequest couponDefaultRequest) {
        logger.info("useCoupon with {}", couponDefaultRequest);
        return couponService.useCoupon(couponDefaultRequest.getCouponCode());
    }

    @PutMapping(value="/v1/coupons/cancel")
    public void cancelCoupon(@RequestBody CouponDefaultRequest couponDefaultRequest) {
        logger.info("cancelCoupon with {}", couponDefaultRequest);
        couponService.cancelCoupon(couponDefaultRequest.getCouponCode());
    }

    @GetMapping(value="/v1/coupons/expired/today")
    public List<CouponResponse> getCouponsExpiredToday() {
        logger.info("getCouponsExpiredToday");
        return couponService.getTodayExpiredCoupons();
    }
}
