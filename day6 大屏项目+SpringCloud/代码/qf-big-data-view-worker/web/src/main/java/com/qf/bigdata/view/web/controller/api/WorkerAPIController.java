package com.qf.bigdata.view.web.controller.api;

import com.qf.bigdata.view.web.service.WorkerService;
import com.qf.data.view.core.model.exception.ViewException;
import com.qf.data.view.core.model.request.WorkerSelectRequest;
import com.qf.data.view.core.model.request.WorkerSignRequest;
import com.qf.data.view.core.model.response.WorkerResponse;
import com.qf.data.view.core.model.result.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/worker")
public class WorkerAPIController {

    @Autowired
    private WorkerService workerService;

    /**
     * 打卡的接口：
     *    员工的id
     *    设备的id
     *    打卡的时间
     * @return
     */
    @PostMapping("/sign")
    public ResultModel workerSign(@RequestBody WorkerSignRequest request){

        try {
            if(Objects.isNull(request) || Objects.isNull(request.getWorkerId())){
                throw new ViewException("参数有误");
            }
        } catch (ViewException e) {
            return ResultModel.error(e.getMessage());
        }

        return workerService.workerSign(request);


    }

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

    /**
     * redis: worker:sign:2020-09-09  ==>拿当前时间
     */
    @GetMapping("/sign/total")
    public ResultModel getSignTotal(){
        return workerService.getSignTotal();

    }


}
