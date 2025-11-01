package com.onlineshopping.merchant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description Merchant OpenAPI Config
 * @author henryzheng
 * @date  2025/10/28
 */
@Configuration
public class MerchantOpenApiConfig {

    @Bean
    public OpenAPI merchantOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Shopping API Specification")
                        .version("V1.0"));
    }

}
