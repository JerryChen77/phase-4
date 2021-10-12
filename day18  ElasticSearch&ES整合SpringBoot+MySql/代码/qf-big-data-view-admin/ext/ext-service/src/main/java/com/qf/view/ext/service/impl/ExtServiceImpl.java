package com.qf.view.ext.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.ext.api.AdminExtAPI;
import com.qf.ext.request.SearchModelRequest;
import com.qf.view.ext.service.feignapi.SearchFeignAPI;
import com.qf.view.ext.service.request.SearchDeviceRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ExtServiceImpl implements AdminExtAPI {

    @Autowired
    private SearchFeignAPI searchFeignAPI;

    @Override
    public ResultModel searchDevice(SearchModelRequest request) {

        SearchDeviceRequest searchDeviceRequest = new SearchDeviceRequest();
        BeanUtils.copyProperties(request,searchDeviceRequest);

        ResultModel resultModel = searchFeignAPI.searchDevice(searchDeviceRequest);
        return resultModel;
    }
}
