package com.github.prorhap.coupon.simple;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.github.prorhap.coupon.simple"})
@EnableJpaRepositories(basePackages = {"com.github.prorhap.coupon.simple"})
@EntityScan(basePackages = {"com.github.prorhap.coupon.simple"})
@EnableJpaAuditing
@Configuration
public class ServiceConfig {
}
