package com.qf.my.worker.consumer.feign.fallback;

import com.qf.common.entity.Worker;
import com.qf.common.entity.dto.WorkerDTO;
import com.qf.my.worker.consumer.feign.api.WorkerFeignAPI;
import org.springframework.stereotype.Component;

@Component
public class WorkerFeignFallback implements WorkerFeignAPI {
  @Override
  public String getWorker(Long id) {
    return "请检查你的网络";
  }

  @Override
  public String getForObject(WorkerDTO workerDTO) {
    return null;
  }

  @Override
  public String postForObject(Worker worker) {
    return null;
  }

  @Override
  public String postForObject2(WorkerDTO workerDTO) {
    return "请检查你的网络";
  }
}
