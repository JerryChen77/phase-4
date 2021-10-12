package com.qf.my.shop.regist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.common.mapper")
public class MyShopRegistApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopRegistApplication.class, args);
    }

}
