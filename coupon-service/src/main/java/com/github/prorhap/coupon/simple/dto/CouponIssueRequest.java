package com.github.prorhap.coupon.simple.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CouponIssueRequest {
    private String userId;
    private String couponCode;

    public CouponIssueRequest(String userId, String couponCode) {
        this.userId = userId;
        this.couponCode = couponCode;
    }
}
