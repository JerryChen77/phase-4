package com.qf.my.worker.consumer.feign.service.impl;

import com.qf.my.worker.consumer.feign.api.WorkerFeignAPI;
import com.qf.my.worker.consumer.feign.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl implements WorkerService {

  @Autowired
  private WorkerFeignAPI feignAPI;

  @Override
  public String getWorker(Long id) {
    //调用服务提供者的接口，ribbon是使用restTemplate来实现这个调用，但是feign不一样
    String result = feignAPI.getWorker(id);
    return result;
  }
}
