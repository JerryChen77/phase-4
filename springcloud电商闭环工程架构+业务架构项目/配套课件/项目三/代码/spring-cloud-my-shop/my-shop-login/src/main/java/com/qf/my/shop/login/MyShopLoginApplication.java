package com.qf.my.shop.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.qf.common.mapper")
@EnableEurekaClient
public class MyShopLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopLoginApplication.class, args);
    }

}
