package com.qf.ext.api;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.ext.request.SearchModelRequest;

public interface AdminExtAPI {

    ResultModel searchDevice(SearchModelRequest request);

}
