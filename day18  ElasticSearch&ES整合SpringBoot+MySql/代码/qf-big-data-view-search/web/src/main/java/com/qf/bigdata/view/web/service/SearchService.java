package com.qf.bigdata.view.web.service;

import com.qf.data.view.core.model.request.SearchDeviceRequest;
import com.qf.data.view.core.model.result.ResultModel;

public interface SearchService {
    ResultModel searchDevice(SearchDeviceRequest request);
}
