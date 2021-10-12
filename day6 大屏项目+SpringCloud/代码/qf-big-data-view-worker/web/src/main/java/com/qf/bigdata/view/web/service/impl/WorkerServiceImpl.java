package com.qf.bigdata.view.web.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.bigdata.view.web.service.WorkerService;
import com.qf.data.view.core.model.request.WorkerSignRequest;
import com.qf.data.view.core.model.response.WorkerResponse;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.api.WorkerFacade;
import com.qf.data.view.facade.request.worker.WorkerModelRequest;
import com.qf.data.view.facade.request.worker.WorkerSignModelRequest;
import com.qf.data.view.facade.response.worker.WorkerModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Reference
    private WorkerFacade workerFacade;

    @Override
    public ResultModel<WorkerResponse> getWorkerById(Long id) {
        //封装请求对象
        WorkerModelRequest request = new WorkerModelRequest();
        request.setId(id);
        //调用dubbo的服务提供者
        ResultModel<WorkerModelResponse> response = workerFacade.getWorkerById(request);
        //解析结果
        if(Objects.nonNull(response.getData())){
            WorkerResponse workerResponse = new WorkerResponse();
            BeanUtils.copyProperties(response.getData(),workerResponse);
            return ResultModel.success(workerResponse);
        }
        return ResultModel.error();
    }

    //员工打卡的记录保存
    @Override
    public ResultModel workerSign(WorkerSignRequest request) {

        WorkerSignModelRequest modelRequest = new WorkerSignModelRequest();

        BeanUtils.copyProperties(request,modelRequest);

        return workerFacade.workerSign(modelRequest);

    }

    @Override
    public ResultModel getSignTotal() {
        return workerFacade.getSignTotal();
    }
}
