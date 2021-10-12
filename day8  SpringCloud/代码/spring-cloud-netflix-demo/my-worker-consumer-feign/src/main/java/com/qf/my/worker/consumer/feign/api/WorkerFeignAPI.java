package com.qf.my.worker.consumer.feign.api;

import com.qf.common.entity.Worker;
import com.qf.common.entity.dto.WorkerDTO;
import com.qf.my.worker.consumer.feign.fallback.WorkerFeignFallback;
import com.qf.my.worker.consumer.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//加入拦截器的配置
@FeignClient(value="WORKER-PROVIDER",configuration = FeignInterceptor.class,fallback = WorkerFeignFallback.class)
//@FeignClient(value="WORKER-PROVIDER",fallback = WorkerFeignFallback.class)
public interface WorkerFeignAPI {

  @RequestMapping("/worker/get/{id}")
  public String getWorker(@PathVariable(name = "id") Long id);

  @RequestMapping("/worker/getForObject")
  public String getForObject(@RequestBody WorkerDTO workerDTO);

  @PostMapping("/worker/postForObject")
  public String postForObject(@RequestBody Worker worker);

  @PostMapping("/worker/getForObjectWithCookie")
  public String postForObject2(@RequestBody WorkerDTO workerDTO);
//  @PostMapping("/postForObject1")
//  public String postForObject1(Long id,String name);


}
