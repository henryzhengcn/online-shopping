package com.onlineshopping.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

/**
 * @description Online Shopping User Application
 * @author henryzheng
 * @date  2025/10/28
 */
@SpringBootApplication
@EntityScan(basePackages = {"com.onlineshopping.common.model.domain"})
@ComponentScan(basePackages = {"com.onlineshopping.merchant", "com.onlineshopping.user"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class))
public class OnlineShoppingUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingUserApplication.class, args);
    }

}
