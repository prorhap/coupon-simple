package com.github.prorhap.coupon.simple.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.prorhap.coupon.simple.filter.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.DispatcherType;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.List;

@Configuration
@EnableWebMvc
public class CouponWebConfig extends WebMvcConfigurerAdapter {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter  = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper  = jsonMessageConverter.getObjectMapper();
                objectMapper.disable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                );
                objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
                break;
            }
        }
    }

//    @Profile({"default", "dev"})
    @Configuration
    public static class DefaultAndDevConfig {
        @Bean
        public FilterRegistrationBean corsFilter() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new CORSFilter());
            registration.setOrder(5);
            registration.addInitParameter("cors.allowed.methods", "GET, POST, HEAD, OPTIONS, PUT, DELETE");
            registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
            return registration;
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
}
