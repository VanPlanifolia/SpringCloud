package com.czy.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * bean的Config
 */
@Configuration
public class BeanConfig {
    /**
     * 把RestTemplate交给spring来托管
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
