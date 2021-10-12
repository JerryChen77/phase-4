package com.qf.my.worker.provider.controller;

import com.qf.common.entity.Worker;
import com.qf.common.entity.dto.WorkerDTO;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {

  @Value("${server.port}")
  private String serverPort;


  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id) throws InterruptedException {
//    if(id!=null){
//      throw new RuntimeException("出现了异常");
//    }
    //访问到此时线程会睡2s
    Thread.sleep(1500);
    return String.format("from %s:worker:%d",serverPort,id);
  }

//  @RequestMapping("/getForObject")
  public String getForObject(Long id,String name){
    return String.format("id:%d,name:%S",id,name);
  }

  @PostMapping("/postForObject")
  public String postForObject(@RequestBody Worker worker){
    return String.format("id:%d,name:%s",worker.getId(),worker.getName());
  }

  @PostMapping("/getForObject")
  public String postForObject1(@RequestBody WorkerDTO workerDTO){
    return String.format("id:%d,name:%s",workerDTO.getId(),workerDTO.getName());
  }

  @PostMapping("/getForObjectWithCookie")
  public String postForObject2(@RequestBody WorkerDTO workerDTO,
                               @CookieValue(name="login_token",required = false) String loginToken){
    return String.format("id:%d,name:%s,cookie:%s",workerDTO.getId(),workerDTO.getName(),loginToken);
  }


  @PostMapping("/postForObjectWithCookie")
  public String postForObjectWithCookie(@RequestBody Worker worker,@CookieValue(name = "login_token",required = false) String loginToken){
    return String.format("id:%d,name:%s,cookie:%s",worker.getId(),worker.getName(),loginToken);
  }

}
