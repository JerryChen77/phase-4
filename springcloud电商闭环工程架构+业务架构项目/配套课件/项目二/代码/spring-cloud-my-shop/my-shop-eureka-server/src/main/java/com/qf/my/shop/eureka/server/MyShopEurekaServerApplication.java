package com.qf.my.shop.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyShopEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopEurekaServerApplication.class, args);
    }

}
