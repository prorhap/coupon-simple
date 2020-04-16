package com.github.prorhap.coupon.simple.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.prorhap.coupon.simple.common.serializer.CouponCodeSerializer;
import com.github.prorhap.coupon.simple.model.Coupon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CouponResponse {

    @JsonSerialize(using = CouponCodeSerializer.class)
    private String couponCode;

    public CouponResponse(String couponCode) {
        this.couponCode = couponCode;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getCouponCode());
    }
}
