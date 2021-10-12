package com.qf.bigdata.view.web.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.bigdata.view.web.service.DeviceService;
import com.qf.data.view.core.model.request.device.DeviceInfoRequest;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.api.DeviceFacade;
import com.qf.data.view.facade.request.device.DeviceModelRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Reference
    private DeviceFacade deviceFacade;

    @Override
    public ResultModel deviceInfo(DeviceInfoRequest request) {
        DeviceModelRequest deviceModelRequest = new DeviceModelRequest();
        BeanUtils.copyProperties(request,deviceModelRequest);
        return deviceFacade.deviceInfo(deviceModelRequest);
    }
}
