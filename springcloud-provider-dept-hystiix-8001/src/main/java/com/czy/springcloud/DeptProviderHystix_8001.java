package com.czy.springcloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * 启动类
 */
@SpringBootApplication
//声明这是一个eureka的客户端，启动之后会将这个服务注册到eureka中
@EnableEurekaClient
//在springboot中开启熔断机制
@EnableHystrix
public class DeptProviderHystix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProviderHystix_8001.class,args);
    }
}
