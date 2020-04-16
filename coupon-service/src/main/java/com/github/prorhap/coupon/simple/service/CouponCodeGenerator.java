package com.github.prorhap.coupon.simple.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class CouponCodeGenerator {
    private final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    public String generate(int codeLength) {
        Random random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            stringBuilder.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return stringBuilder.toString();
    }
}
