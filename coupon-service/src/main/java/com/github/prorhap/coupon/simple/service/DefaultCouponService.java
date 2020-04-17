package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.common.utils.CouponDateUtils;
import com.github.prorhap.coupon.simple.dto.CouponCreationRequest;
import com.github.prorhap.coupon.simple.dto.CouponIssueRequest;
import com.github.prorhap.coupon.simple.dto.CouponResponse;
import com.github.prorhap.coupon.simple.dto.CouponUseResult;
import com.github.prorhap.coupon.simple.exception.CouponServiceException;
import com.github.prorhap.coupon.simple.infrastructure.notification.NotificationService;
import com.github.prorhap.coupon.simple.model.Coupon;
import com.github.prorhap.coupon.simple.model.CouponFactory;
import com.github.prorhap.coupon.simple.model.CouponIssueResult;
import com.github.prorhap.coupon.simple.model.User;
import com.github.prorhap.coupon.simple.repository.CouponRepository;
import com.github.prorhap.coupon.simple.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DefaultCouponService implements CouponService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int COUPON_CODE_LENGTH = 19;

    private NotificationService notificationService;

    private final CouponCodeGenerator couponCodeGenerator;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponFactory couponFactory;
    private final CouponDateUtils couponDateUtils;

    @Inject
    public DefaultCouponService(NotificationService notificationService, CouponCodeGenerator couponCodeGenerator, CouponRepository couponRepository, UserRepository userRepository, CouponFactory couponFactory, CouponDateUtils couponDateUtils) {
        this.notificationService = notificationService;
        this.couponCodeGenerator = couponCodeGenerator;
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.couponFactory = couponFactory;
        this.couponDateUtils = couponDateUtils;
    }

    @Override
    @Transactional
    public void createCoupon(CouponCreationRequest couponCreationRequest) {
        try {
            logger.debug("creating coupon. amount = {}", couponCreationRequest);

            couponRepository.saveAll(
                    IntStream.range(0, couponCreationRequest.getAmount())
                            .mapToObj(i -> couponCodeGenerator.generate(COUPON_CODE_LENGTH))
                            .map(code -> couponFactory.create(
                                    code,
                                    couponCreationRequest.getValidFrom(),
                                    couponCreationRequest.getExpireAt()))
                            .collect(Collectors.toList()));
            logger.debug("created coupon. amount = {}", couponCreationRequest.getAmount());
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }

    @Override
    public List<CouponResponse> getTodayExpiredCoupons() {
        try {
            return couponRepository
                    .findCouponsByExpireAtAndUsedFalse(couponDateUtils.today())
                    .stream()
                    .map(coupon -> CouponResponse.from(coupon))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }

    @Override
    @Transactional
    public CouponUseResult useCoupon(String couponCode) {
        try {
            Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);
            return coupon.use(couponDateUtils.now());
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }

    @Override
    @Transactional
    public CouponResponse issueCoupon(CouponIssueRequest couponIssueRequest) {
        try {
            Coupon coupon = couponRepository.findCouponByCouponCode(couponIssueRequest.getCouponCode());
            User user = userRepository.findUserByUserId(couponIssueRequest.getUserId());

            CouponIssueResult couponIssueResult = coupon.issue(user, couponDateUtils.now());
            logger.debug("issued coupon = {}", couponIssueResult);

            return CouponResponse.from(coupon);
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }

    @Override
    @Transactional
    public void cancelCoupon(String couponCode) {
        try {
            Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);
            coupon.cancel();
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }

    @Override
    public void sendNotification(int expiryDaysBefore) {
        try {
            couponRepository.findCouponsByUsedFalseAndIssuedTrueAndExpireAt(couponDateUtils.after(expiryDaysBefore))
                    .stream()
                    .forEach(coupon ->
                            notificationService.sendExpiryMessage(
                                    coupon.getUser().getMobileNumber(),
                                    expiryDaysBefore));
        } catch (Exception e) {
            throw new CouponServiceException(e);
        }
    }
}
