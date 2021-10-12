package com.qf.my.worker.consumer.feign.web;

import com.qf.common.entity.dto.WorkerDTO;
import com.qf.my.worker.consumer.feign.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {

  @Autowired
  private WorkerService workerService;

  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id){
    return workerService.getWorker(id);
  }

  @RequestMapping("/getForObject")
  public String getForObject(@RequestBody WorkerDTO workerDTO){
    return workerService.getForObject(workerDTO);
  }

  @RequestMapping("/getForObjectWithCookie")
  public String getForObjectWithCookie(@RequestBody WorkerDTO workerDTO){
    return workerService.getForObjectWithCookie(workerDTO);
  }



}
