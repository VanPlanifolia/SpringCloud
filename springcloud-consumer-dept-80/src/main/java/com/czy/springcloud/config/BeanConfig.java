package com.czy.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
    //用于Ribbon负载均衡的注解
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
