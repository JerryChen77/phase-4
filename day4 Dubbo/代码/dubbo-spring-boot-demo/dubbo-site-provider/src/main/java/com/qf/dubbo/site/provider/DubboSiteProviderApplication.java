package com.qf.dubbo.site.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class DubboSiteProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(DubboSiteProviderApplication.class, args);
  }

}
