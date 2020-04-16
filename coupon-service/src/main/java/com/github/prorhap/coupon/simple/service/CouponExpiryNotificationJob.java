package com.github.prorhap.coupon.simple.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CouponExpiryNotificationJob {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${coupon.notification.expiry.days.before}")
    private int expiryDaysBefore;

    private final CouponService couponService;


    @Inject
    public CouponExpiryNotificationJob(CouponService couponService) {
        this.couponService = couponService;
    }

    @Scheduled(cron = "${coupon.notification.expiry.cron}")
    public void task() {
        couponService.sendNotification(expiryDaysBefore);
        logger.info("########### run job");
    }
}
