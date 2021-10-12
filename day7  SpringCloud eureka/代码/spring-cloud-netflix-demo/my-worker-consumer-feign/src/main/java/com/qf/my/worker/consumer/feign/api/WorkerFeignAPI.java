package com.qf.my.worker.consumer.feign.api;

import com.qf.common.entity.Worker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("WORKER-PROVIDER")
public interface WorkerFeignAPI {

  @RequestMapping("/worker/get/{id}")
  public String getWorker(@PathVariable(name = "id") Long id);

//  @RequestMapping("/getForObject")
//  public String getForObject(Long id,String name);

  @PostMapping("/worker/postForObject")
  public String postForObject(@RequestBody Worker worker);

//  @PostMapping("/postForObject1")
//  public String postForObject1(Long id,String name);


}
