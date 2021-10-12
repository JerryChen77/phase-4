package com.qf.bigdata.view.web;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ViewWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViewWebApplication.class, args);
    }

}