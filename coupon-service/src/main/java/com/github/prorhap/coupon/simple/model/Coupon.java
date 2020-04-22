package com.github.prorhap.coupon.simple.model;

import com.github.prorhap.coupon.simple.common.entity.BaseEntity;
import com.github.prorhap.coupon.simple.dto.CouponUseResult;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "couponType")
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor
public class Coupon extends BaseEntity {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String couponCode;

    private boolean used;
    private boolean issued;

    private Date validFrom;
    private Date expireAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Date issuedAt;
    private Date usedAt;

    public Coupon(String couponCode, Date validFrom, Date expireAt, boolean used, boolean issued) {
        this.couponCode = couponCode;
        this.validFrom = validFrom;
        this.expireAt = expireAt;
        this.used = used;
        this.issued = issued;
    }

    public Coupon(String couponCode, boolean used, boolean issued, Date validFrom, Date expireAt, User user, Date issuedAt, Date usedAt) {
        this.couponCode = couponCode;
        this.used = used;
        this.issued = issued;
        this.validFrom = validFrom;
        this.expireAt = expireAt;
        this.user = user;
        this.issuedAt = issuedAt;
        this.usedAt = usedAt;
    }

    public CouponUseResult use(Date now) {

        if (isUsed()) {
            return CouponUseResult.ALREADY_USED;
        } else if (!now.before(expireAt)) {
            return CouponUseResult.EXPIRED;
        } else if (now.before(validFrom)) {
            return CouponUseResult.NOT_STARTED;
        } else if (!isIssued()) {
            return CouponUseResult.NOT_ISSUED;
        }

        this.setUsed(true);
        this.setUsedAt(now);

        return CouponUseResult.VALID;
    }

    public CouponIssueResult issue(User user, Date now) {

        if (isUsed()) {
            return CouponIssueResult.ALREADY_USED;
        } else if (isIssued()) {
            return CouponIssueResult.ALREADY_ISSUED;
        } else if (!now.before(expireAt)) {
            return CouponIssueResult.ALREADY_EXPIRED;
        }

        setUser(user);
        this.issuedAt = now;
        this.issued = true;

        return CouponIssueResult.ISSUED;
    }

    private void setUser(User user) {
        this.user = user;

        List<Coupon> coupons = user.getCoupons();
        if (coupons.contains(this)) {
            coupons.remove(this);
        }
        coupons.add(this);
    }

    public void cancel() {
        user = null;
        issuedAt = null;
        this.issued = false;
    }

}
