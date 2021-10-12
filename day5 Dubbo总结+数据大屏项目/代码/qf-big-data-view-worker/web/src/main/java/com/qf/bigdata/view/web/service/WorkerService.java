package com.qf.bigdata.view.web.service;

import com.qf.data.view.core.model.response.WorkerResponse;
import com.qf.data.view.core.model.result.ResultModel;

public interface WorkerService {

    ResultModel<WorkerResponse> getWorkerById(Long id);

}
