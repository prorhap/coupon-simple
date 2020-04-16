package com.github.prorhap.coupon.simple.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prorhap.coupon.simple.dto.CouponCreationRequest;
import com.github.prorhap.coupon.simple.dto.CouponIssueRequest;
import com.github.prorhap.coupon.simple.dto.CouponResponse;
import com.github.prorhap.coupon.simple.service.CouponService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(couponController)
                .setControllerAdvice(new GlobalControllerExceptionHandler(objectMapper)).build();
    }

    @Test
    public void createCoupon() throws Exception {
        CouponCreationRequest couponCreationRequest = new CouponCreationRequest(
                5,
                DateUtils.parseDate("2020-04-08", "yyyy-MM-dd"),
                DateUtils.parseDate("2020-04-15", "yyyy-MM-dd"));

        String requestBody = "{\n" +
                "\t\"amount\": 5,\n" +
                "\t\"validFrom\": \"2020-04-08\",\n" +
                "\t\"expireAt\": \"2020-04-15\"\n" +
                "}";

        mockMvc.perform(post("/v1/coupons")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(couponService).createCoupon(couponCreationRequest);
    }

    @Test
    public void issueCoupon() throws Exception {
        String userId = "alice";
        String couponCode = "A111122222233333";
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(userId, couponCode);
        CouponResponse couponResponse = new CouponResponse(couponCode);

        when(couponService.issueCoupon(couponIssueRequest)).thenReturn(couponResponse);

        String requestBody = objectMapper.writeValueAsString(couponIssueRequest);
        mockMvc.perform(put("/v1/coupons/issue")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("couponCode").value("A1111-222222-33333"));

        verify(couponService).issueCoupon(couponIssueRequest);
    }

    @Test
    public void getCouponsExpiredToday() throws Exception {

        when(couponService.getTodayExpiredCoupons())
                .thenReturn(Arrays.asList(
                        new CouponResponse("A111122222233333"),
                        new CouponResponse("B111122222233333")));

        mockMvc.perform(get("/v1/coupons/expired/today")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].couponCode").value("A1111-222222-33333"))
                .andExpect(jsonPath("[1].couponCode").value("B1111-222222-33333"));
    }
}
