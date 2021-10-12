package com.qf.dubbo.site.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.SiteService;
import com.qf.entity.Site;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/site")
public class SiteController {

  @Reference
  private SiteService service;

  @GetMapping("/get/{id}")
  public Site getSiteById(@PathVariable Long id){

    return service.getSiteById(id);


  }



}
