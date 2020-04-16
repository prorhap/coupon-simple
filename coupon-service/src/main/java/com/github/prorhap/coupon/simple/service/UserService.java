package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.dto.CouponResponse;

import java.util.List;

public interface UserService {
    List<CouponResponse> getCoupons(String userId);
}
