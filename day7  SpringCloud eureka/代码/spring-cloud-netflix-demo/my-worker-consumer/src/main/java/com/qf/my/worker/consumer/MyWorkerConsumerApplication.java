package com.qf.my.worker.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyWorkerConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyWorkerConsumerApplication.class, args);
  }

}
