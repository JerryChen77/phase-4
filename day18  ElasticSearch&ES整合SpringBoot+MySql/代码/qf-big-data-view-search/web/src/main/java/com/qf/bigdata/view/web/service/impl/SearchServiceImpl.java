package com.qf.bigdata.view.web.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.bigdata.view.web.service.SearchService;
import com.qf.data.view.core.model.request.SearchDeviceRequest;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.api.SearchFacade;
import com.qf.data.view.facade.request.SearchDeviceModelRequest;
import com.qf.data.view.facade.response.SearchDeviceModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Reference
    private SearchFacade searchFacade;

    @Override
    public ResultModel searchDevice(SearchDeviceRequest request) {
        SearchDeviceModelRequest searchDeviceModelRequest = new SearchDeviceModelRequest();
        BeanUtils.copyProperties(request,searchDeviceModelRequest);
        ResultModel<List<SearchDeviceModelResponse>> listResultModel = searchFacade.searchDevice(searchDeviceModelRequest);
        return listResultModel;
    }
}
