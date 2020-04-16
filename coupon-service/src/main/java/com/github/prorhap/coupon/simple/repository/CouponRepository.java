package com.github.prorhap.coupon.simple.repository;

import com.github.prorhap.coupon.simple.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findCouponByCouponCode(String couponCode);
    List<Coupon> findCouponsByExpireAtAndUsedFalse(Date startDate);

    Coupon findTopByUsedFalseAndIssuedFalse();

    List<Coupon> findCouponsByUsedFalseAndIssuedTrueAndExpireAt(Date date);
}
