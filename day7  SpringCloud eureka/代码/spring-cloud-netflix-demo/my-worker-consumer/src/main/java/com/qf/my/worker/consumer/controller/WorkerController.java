package com.qf.my.worker.consumer.controller;

import com.qf.common.entity.Worker;
import com.qf.my.worker.consumer.service.WorkerService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {;

  @Autowired
  private WorkerService workerService;

  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id){
    //去调用服务提供者
    return workerService.getWorker(id);
  }

  @RequestMapping("/get/{id}/{name}")
  public String getForObject(@PathVariable Long id,@PathVariable String name){
    //去调用服务提供者
    return workerService.getForObject(id,name);
  }

  @PostMapping("/post")
  public String postForObject(@RequestBody Worker worker){
    return workerService.postForObject(worker);
  }

  @PostMapping("/postWithCookie")
  public String postForObject(@RequestBody Worker worker, @CookieValue("login_token") String loginToken){
    return workerService.postForObjectWithCookie(worker,loginToken);
  }



}
