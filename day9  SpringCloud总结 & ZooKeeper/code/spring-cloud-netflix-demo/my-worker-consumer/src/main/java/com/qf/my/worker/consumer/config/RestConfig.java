package com.qf.my.worker.consumer.config;

import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

  /**
   * 注入一个RestTemplate的bean
   * @return
   */
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  public RandomRule randomRule(){
    return new RandomRule();
  }

}
