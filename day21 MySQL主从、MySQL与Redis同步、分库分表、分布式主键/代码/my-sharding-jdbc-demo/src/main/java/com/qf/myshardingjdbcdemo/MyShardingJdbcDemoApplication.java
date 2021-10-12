package com.qf.myshardingjdbcdemo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.myshardingjdbcdemo")
public class MyShardingJdbcDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyShardingJdbcDemoApplication.class, args);
  }

}
