package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.TestCouponFactory;
import com.github.prorhap.coupon.simple.common.utils.CouponDateUtils;
import com.github.prorhap.coupon.simple.dto.CouponCreationRequest;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
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

    @Mock
    private CouponFactory couponFactory;

    @InjectMocks
    private DefaultCouponService couponService;


    private TestCouponFactory testCouponFactory = new TestCouponFactory();

    @Test
    public void itShouldIssueCoupon() throws Exception {

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

        String userId = "alice" ;
        String couponCode = "1111222223333";

        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(userId, couponCode);

        when(userRepository.findUserByUserId(userId)).thenReturn(mock(User.class));
        when(couponRepository.findCouponByCouponCode(couponCode)).thenReturn(null);

        couponService.issueCoupon(couponIssueRequest);
    }

    @Test
    public void itShouldSaveCouponsWithRequestedCount() {

        String couponCode1 = "A111111111111111111";
        String couponCode2 = "B111111111111111111";
        String couponCode3 = "C111111111111111111";
        Date validFrom = couponDateUtils.yesterday();
        Date expireAt = couponDateUtils.tomorrow();

        Coupon expectedCoupon1 = testCouponFactory.createDefaultCoupon(couponCode1, validFrom, expireAt);
        Coupon expectedCoupon2 = testCouponFactory.createDefaultCoupon(couponCode2, validFrom, expireAt);
        Coupon expectedCoupon3 = testCouponFactory.createDefaultCoupon(couponCode3, validFrom, expireAt);

        CouponCreationRequest couponCreationRequest = new CouponCreationRequest(3, validFrom, expireAt);

        when(couponCodeGenerator.generate(anyInt()))
                .thenReturn(couponCode1)
                .thenReturn(couponCode2)
                .thenReturn(couponCode3);

        when(couponFactory.create(couponCode1, validFrom, expireAt))
                .thenReturn(expectedCoupon1);
        when(couponFactory.create(couponCode2, validFrom, expireAt))
                .thenReturn(expectedCoupon2);
        when(couponFactory.create(couponCode3, validFrom, expireAt))
                .thenReturn(expectedCoupon3);

        couponService.createCoupon(couponCreationRequest);

        verify(couponRepository).saveAll(Arrays.asList(expectedCoupon1, expectedCoupon2, expectedCoupon3));
    }
}

