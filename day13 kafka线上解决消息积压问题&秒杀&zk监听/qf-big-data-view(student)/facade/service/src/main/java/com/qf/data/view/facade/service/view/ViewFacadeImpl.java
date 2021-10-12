package com.qf.data.view.facade.service.view;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.data.view.facade.api.ViewFacade;
import com.qf.data.view.facade.service.base.BaseFacade;

/**
 * facade的service要进行一些业务层面的数据的封装，比如说注册，时间的格式需要调整，于是在facade的service里进行时间格式的调整
 *         把用户信息插入到数据库里：注入view-core-service里面的viewService
 *         facade的service-》调用viewService，过程中需要传递数据，把数据封装在DTO里
 */
@Service
public class ViewFacadeImpl extends BaseFacade implements ViewFacade {

}
