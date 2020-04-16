package com.github.prorhap.coupon.simple.infrastructure.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService implements NotificationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${coupon.notification.expiry.message-template}")
    private String messageTemplate;

    @Override
    public void sendExpiryMessage(String mobileNumber, int expiryDaysBefore) {
        logger.debug("send an expiry notification to {}", mobileNumber);
        logger.info(String.format(messageTemplate, expiryDaysBefore));
    }
}
