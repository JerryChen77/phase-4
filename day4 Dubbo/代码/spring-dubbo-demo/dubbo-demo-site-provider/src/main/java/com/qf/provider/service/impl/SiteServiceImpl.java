package com.qf.provider.service.impl;

import com.qf.api.SiteService;
import com.qf.api.entity.Site;

//要把这个服务交给dubbo容器-》在项目中整合dubbo
public class SiteServiceImpl implements SiteService {
  @Override
  public String getName(String name) {
    return "name:"+name;
  }

  @Override
  public Site getSiteById(Long id) {
    Site site = new Site();
    site.setId(id);
    return site;
  }
}
