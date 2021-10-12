package com.qf.dubbo.site.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.SiteService;
import com.qf.entity.Site;

@Service
public class SiteServiceImpl implements SiteService {
  @Override
  public Site getSiteById(Long id) {
    Site site = new Site();
    site.setId(id);
    return site;
  }
}
