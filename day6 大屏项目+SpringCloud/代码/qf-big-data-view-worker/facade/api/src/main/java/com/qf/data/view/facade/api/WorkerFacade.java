package com.qf.data.view.facade.api;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.request.worker.WorkerModelRequest;
import com.qf.data.view.facade.request.worker.WorkerSignModelRequest;
import com.qf.data.view.facade.response.worker.WorkerModelResponse;

public interface WorkerFacade {

    ResultModel<WorkerModelResponse> getWorkerById(WorkerModelRequest request);

    ResultModel workerSign(WorkerSignModelRequest request);


    ResultModel getSignTotal();
}
