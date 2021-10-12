package com.qf.bigdata.view.web.service;

import com.qf.data.view.core.model.request.SearchRequest;
import com.qf.data.view.core.model.result.ResultModel;

public interface AdminService {
    ResultModel searchDevice(SearchRequest request);
}
