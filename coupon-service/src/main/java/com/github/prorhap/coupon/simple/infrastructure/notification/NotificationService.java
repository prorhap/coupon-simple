package com.github.prorhap.coupon.simple.infrastructure.notification;

public interface NotificationService {

    void sendExpiryMessage(String mobileNumber, int expiryDaysBefore);
}
