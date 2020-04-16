package com.github.prorhap.coupon.simple.model;

import com.github.prorhap.coupon.simple.common.utils.CouponDateUtils;
import com.github.prorhap.coupon.simple.dto.CouponUseResult;
import org.exparity.hamcrest.date.DateMatchers;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CouponTest {

    private CouponDateUtils couponDateUtils = new CouponDateUtils();
    private CouponFactory couponFactory = new CouponFactory();

    @Test
    public void itShouldBeInIssuedStatusAfterIssued() throws Exception {

        Coupon coupon = couponFactory.create("A1B1C1", couponDateUtils.yesterday(), couponDateUtils.tomorrow());
        Date issuedAt = new Date();
        coupon.issue(new User("bob", "01011111111"), issuedAt);

        assertThat(coupon.getIssuedAt(), equalTo(issuedAt));
        assertThat(coupon.isIssued(), equalTo(true));
    }

    @Test
    public void itShouldInUsedStatusAfterCouponUse() throws Exception {
        Coupon coupon = create(
                "A1B1C1", false, true,
                "2020-04-01", "2020-04-04", new User("bob", "01011111111"),
                "2020-04-02 08:00:00", null);

        final Date usedDate = couponDateUtils.parseDateTime("2020-04-02 10:00:00");
        coupon.use(usedDate);

        assertThat(coupon.isUsed(), equalTo(true));
        assertThat(coupon.getUsedAt(), DateMatchers.sameDay(usedDate));
    }

    @Test
    public void itShouldReturnExpiredResultWhenExpiredCouponUse() throws Exception {

        Coupon coupon = create(
                "A1B1C1", false, true,
                "2020-04-01", "2020-04-03", new User("bob", "01011111111"),
                "2020-04-02 08:00:00", null);

        CouponUseResult validationResult = coupon.use(couponDateUtils.parseDate("2020-04-04"));
        assertThat(validationResult, equalTo(CouponUseResult.EXPIRED));
    }

    @Test
    public void itShouldReturnAlreadyUsedResultWhenCouponAlreadyUsed() throws Exception {

        Coupon coupon = create(
                "A1B1C1", true, true,
                "2020-04-01", "2020-04-04", new User("bob", "01011111111"),
                "2020-04-02 08:00:00", "2020-04-02 20:30:50");

        CouponUseResult validationResult = coupon.use(couponDateUtils.parseDate("2020-04-04"));
        assertThat(validationResult, equalTo(CouponUseResult.ALREADY_USED));
    }


    private Coupon create(String couponCode, boolean used, boolean issued, String validFrom, String expireAt, User user, String issuedAt, String usedAt) throws Exception {
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
