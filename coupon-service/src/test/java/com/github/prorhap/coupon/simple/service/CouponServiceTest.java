package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.TestCouponFactory;
import com.github.prorhap.coupon.simple.common.utils.CouponDateUtils;
import com.github.prorhap.coupon.simple.dto.CouponIssueRequest;
import com.github.prorhap.coupon.simple.dto.CouponResponse;
import com.github.prorhap.coupon.simple.exception.CouponServiceException;
import com.github.prorhap.coupon.simple.infrastructure.notification.NotificationService;
import com.github.prorhap.coupon.simple.model.Coupon;
import com.github.prorhap.coupon.simple.model.CouponFactory;
import com.github.prorhap.coupon.simple.model.CouponIssueResult;
import com.github.prorhap.coupon.simple.model.User;
import com.github.prorhap.coupon.simple.repository.CouponRepository;
import com.github.prorhap.coupon.simple.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private CouponCodeGenerator couponCodeGenerator;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponDateUtils couponDateUtils;

    private TestCouponFactory testCouponFactory = new TestCouponFactory();

    @Test
    public void itShouldIssueCoupon() throws Exception {

        CouponService couponService = new DefaultCouponService(
                notificationService, couponCodeGenerator, couponRepository, userRepository, new CouponFactory(), couponDateUtils);

        Coupon coupon = mock(Coupon.class);

        String userId = "alice";
        String couponCode = "1111222223333";
        User user = new User(userId, "010-1111-1111");
        Date issuedDate = DateUtils.parseDate("2020-04-02 10:00:00", "yyyy-MM-dd HH:mm:s");

        when(userRepository.findUserByUserId(userId)).thenReturn(user);
        when(couponRepository.findCouponByCouponCode(couponCode)).thenReturn(coupon);
        when(couponDateUtils.now()).thenReturn(issuedDate);
        when(coupon.issue(any(User.class), any(Date.class))).thenReturn(CouponIssueResult.ISSUED);
        when(coupon.getCouponCode()).thenReturn(couponCode);

        CouponResponse couponResponse = couponService.issueCoupon(new CouponIssueRequest(userId, couponCode));

        assertThat(couponResponse, equalTo(new CouponResponse(couponCode)));
        verify(coupon).issue(user, issuedDate);
    }

    @Test(expected = CouponServiceException.class)
    public void itShouldThrowNoIssueableCouponExceptionWhenIssueableCouponsDontExist() throws Exception {

        CouponService couponService = new DefaultCouponService(
                notificationService, couponCodeGenerator, couponRepository, userRepository, new CouponFactory(), couponDateUtils);

        String userId = "alice" ;
        String couponCode = "1111222223333";

        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(userId, couponCode);

        when(userRepository.findUserByUserId(userId)).thenReturn(mock(User.class));
        when(couponRepository.findCouponByCouponCode(couponCode)).thenReturn(null);

        couponService.issueCoupon(couponIssueRequest);
    }
}

