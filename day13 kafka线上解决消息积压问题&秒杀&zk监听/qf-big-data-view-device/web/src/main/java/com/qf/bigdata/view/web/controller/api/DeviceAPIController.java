package com.qf.bigdata.view.web.controller.api;

import com.qf.bigdata.view.web.service.DeviceService;
import com.qf.data.view.core.model.exception.ViewException;
import com.qf.data.view.core.model.request.device.DeviceInfoRequest;
import com.qf.data.view.core.model.result.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/device")
public class DeviceAPIController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/info")
    public ResultModel deviceInfo(@RequestBody DeviceInfoRequest request){
        if(Objects.isNull(request)){
            try {
                throw new ViewException("参数异常");
            } catch (ViewException e) {
                return ResultModel.error(e.getMessage());
            }
        }
        return deviceService.deviceInfo(request);
    }




}
