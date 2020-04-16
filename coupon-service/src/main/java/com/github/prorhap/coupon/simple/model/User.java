package com.github.prorhap.coupon.simple.model;

import com.github.prorhap.coupon.simple.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String mobileNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Coupon> coupons = new ArrayList<>();

    public User(String userId, String mobileNumber) {
        this.userId = userId;
        this.mobileNumber = mobileNumber;
    }
}
