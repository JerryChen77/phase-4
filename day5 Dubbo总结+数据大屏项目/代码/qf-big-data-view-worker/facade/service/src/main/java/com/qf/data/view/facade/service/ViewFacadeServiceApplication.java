package com.qf.data.view.facade.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.qf.data")
@EnableDubbo
@MapperScan("com.qf.data.core.dal")
public class ViewFacadeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViewFacadeServiceApplication.class, args);
    }

}
