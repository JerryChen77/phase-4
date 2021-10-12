package com.qf.my.worker.provider.controller;

import com.qf.common.entity.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {

  @Value("${server.port}")
  private String serverPort;


  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id){
    return String.format("from %s:worker:%d",serverPort,id);
  }

  @RequestMapping("/getForObject")
  public String getForObject(Long id,String name){
    return String.format("id:%d,name:%S",id,name);
  }

  @PostMapping("/postForObject")
  public String postForObject(@RequestBody Worker worker){
    return String.format("id:%d,name:%s",worker.getId(),worker.getName());
  }

  @PostMapping("/postForObject1")
  public String postForObject1(Long id,String name){
    return String.format("id:%d,name:%s",id,name);
  }


  @PostMapping("/postForObjectWithCookie")
  public String postForObjectWithCookie(@RequestBody Worker worker,@CookieValue(name = "login_token",required = false) String loginToken){
    return String.format("id:%d,name:%s,cookie:%s",worker.getId(),worker.getName(),loginToken);
  }

}
