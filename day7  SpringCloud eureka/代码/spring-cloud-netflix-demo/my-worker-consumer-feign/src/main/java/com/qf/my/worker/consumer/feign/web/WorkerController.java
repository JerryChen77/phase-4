package com.qf.my.worker.consumer.feign.web;

import com.qf.my.worker.consumer.feign.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
public class WorkerController {

  @Autowired
  private WorkerService workerService;

  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id){
    return workerService.getWorker(id);
  }



}
