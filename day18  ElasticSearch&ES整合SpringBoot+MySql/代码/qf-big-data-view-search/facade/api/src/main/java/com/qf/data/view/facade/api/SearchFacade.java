package com.qf.data.view.facade.api;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.request.SearchDeviceModelRequest;
import com.qf.data.view.facade.response.SearchDeviceModelResponse;

import java.util.List;

public interface SearchFacade {

    ResultModel<List<SearchDeviceModelResponse>> searchDevice(SearchDeviceModelRequest request);


}
