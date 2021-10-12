package com.qf.my.shop.front.index;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyShopFrontIndexApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopFrontIndexApplication.class, args);
    }

}
