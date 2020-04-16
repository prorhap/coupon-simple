package com.github.prorhap.coupon.simple.repository;

import com.github.prorhap.coupon.simple.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);

}
