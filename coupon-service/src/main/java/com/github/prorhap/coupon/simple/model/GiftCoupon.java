package com.github.prorhap.coupon.simple.model;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Gift")
public class GiftCoupon extends Coupon {

    public GiftCoupon(String couponCode, Date validFrom, Date expireAt, boolean used, boolean issued) {
        super(couponCode, validFrom, expireAt, used, issued);
    }

    // Implement business
}
