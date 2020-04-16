package com.github.prorhap.coupon.simple.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.prorhap.coupon.simple.common.serializer.JsonDateDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CouponCreationRequest {
    private int amount;

    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date validFrom;

    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date expireAt;

    public CouponCreationRequest(int amount, Date validFrom, Date expireAt) {
        this.amount = amount;
        this.validFrom = validFrom;
        this.expireAt = expireAt;
    }
    
    //TODO: request body 매핑할 때 날짜 유효성 체크
}
