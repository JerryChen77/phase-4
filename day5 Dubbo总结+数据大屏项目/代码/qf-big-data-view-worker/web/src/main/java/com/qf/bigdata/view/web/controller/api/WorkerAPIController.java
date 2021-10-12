package com.qf.bigdata.view.web.controller.api;

import com.qf.bigdata.view.web.service.WorkerService;
import com.qf.data.view.core.model.exception.ViewException;
import com.qf.data.view.core.model.request.WorkerSelectRequest;
import com.qf.data.view.core.model.response.WorkerResponse;
import com.qf.data.view.core.model.result.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/worker")
public class WorkerAPIController {

    @Autowired
    private WorkerService workerService;

    @PostMapping("/get")
    public ResultModel<WorkerResponse> getWorkerById(@RequestBody WorkerSelectRequest request){

        try {
            if(Objects.isNull(request) || Objects.isNull(request.getId())){
                //兜底
                throw new ViewException("参数有误");
            }
        } catch (ViewException e) {
            return ResultModel.error(e.getMessage());
        }

        return workerService.getWorkerById(request.getId());
    }



}
