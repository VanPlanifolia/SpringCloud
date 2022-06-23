package com.czy.springcloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动类
 */
@SpringBootApplication
//声明这是一个EurekaServer
@EnableEurekaServer
public class Eureka_7003 {
    public static void main(String[] args) {
        SpringApplication.run(Eureka_7003.class,args);
    }
}
