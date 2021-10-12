package com.qf.data.view.facade.service.worker;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.data.core.dal.po.WorkerPO;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.core.service.worker.WorkerService;
import com.qf.data.view.facade.api.WorkerFacade;
import com.qf.data.view.facade.request.worker.WorkerModelRequest;
import com.qf.data.view.facade.response.worker.WorkerModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class WorkerFacadeImpl implements WorkerFacade {

    @Autowired
    private WorkerService workerService;

    @Override
    public ResultModel<WorkerModelResponse> getWorkerById(WorkerModelRequest request) {
        //service->dao
        WorkerPO workerPO = workerService.selectByPrimaryKey(request.getId());
        //workerPO---复制成--->WorkerModelResponse
        WorkerModelResponse workerModelResponse = new WorkerModelResponse();
        BeanUtils.copyProperties(workerPO,workerModelResponse);
        return ResultModel.success(workerModelResponse);
    }
}
