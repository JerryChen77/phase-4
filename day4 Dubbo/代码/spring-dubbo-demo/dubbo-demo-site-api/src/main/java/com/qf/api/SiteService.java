package com.qf.api;

import com.qf.api.entity.Site;

public interface SiteService {

  String getName(String name);

  Site getSiteById(Long id);


}
