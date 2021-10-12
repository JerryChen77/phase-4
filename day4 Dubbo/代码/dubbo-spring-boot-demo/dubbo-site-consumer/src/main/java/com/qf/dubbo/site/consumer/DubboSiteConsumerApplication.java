package com.qf.dubbo.site.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DubboSiteConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DubboSiteConsumerApplication.class,args);
  }


}
