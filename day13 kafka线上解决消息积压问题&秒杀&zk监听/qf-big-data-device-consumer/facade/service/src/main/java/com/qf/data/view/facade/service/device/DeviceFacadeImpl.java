package com.qf.data.view.facade.service.device;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.data.core.dal.po.device.DevicePO;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.core.service.device.DeviceService;
import com.qf.data.view.facade.api.DeviceFacade;
import com.qf.data.view.facade.request.device.DeviceSaveRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DeviceFacadeImpl implements DeviceFacade {


    @Autowired
    private DeviceService deviceService;

    @Override
    public ResultModel saveDevice(DeviceSaveRequest request) {
        DevicePO devicePO = new DevicePO();
        BeanUtils.copyProperties(request,devicePO);
        deviceService.insert(devicePO);
        return ResultModel.success();
    }



}
