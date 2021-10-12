package com.qf.data.view.facade.api;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.request.device.DeviceModelRequest;

public interface DeviceFacade {
    ResultModel deviceInfo(DeviceModelRequest deviceModelRequest);
}
