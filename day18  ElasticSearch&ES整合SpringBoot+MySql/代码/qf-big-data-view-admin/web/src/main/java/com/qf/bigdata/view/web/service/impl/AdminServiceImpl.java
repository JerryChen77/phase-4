package com.qf.bigdata.view.web.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.bigdata.view.web.service.AdminService;
import com.qf.data.view.core.model.request.SearchRequest;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.ext.api.AdminExtAPI;
import com.qf.ext.request.SearchModelRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    //调用的搜索模块：ext来实现和其他服务之间的通信
    @Reference
    private AdminExtAPI adminExtAPI;

    /*@Autowired
    private DeviceFacade deviceFacade;

    @Autowired
    private UserFacade userFacade;//
    //
   String name =  userFacade.getUserByName();*/

    @Override
    public ResultModel searchDevice(SearchRequest request) {
        SearchModelRequest searchModelRequest = new SearchModelRequest();
        BeanUtils.copyProperties(request,searchModelRequest);
        //调用搜索服务（外部服务）的搜索功能
        //比如 需要的参数 需要从内部的多个服务中那
        ResultModel resultModel = adminExtAPI.searchDevice(searchModelRequest);
        return resultModel;
    }
}
