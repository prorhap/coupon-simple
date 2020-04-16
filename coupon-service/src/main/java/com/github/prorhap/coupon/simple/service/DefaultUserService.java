package com.github.prorhap.coupon.simple.service;

import com.github.prorhap.coupon.simple.dto.CouponResponse;
import com.github.prorhap.coupon.simple.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<CouponResponse> getCoupons(String userId) {
        return userRepository.findUserByUserId(userId).getCoupons().stream()
                .map(coupon -> CouponResponse.from(coupon))
                .collect(Collectors.toList());
    }
}
