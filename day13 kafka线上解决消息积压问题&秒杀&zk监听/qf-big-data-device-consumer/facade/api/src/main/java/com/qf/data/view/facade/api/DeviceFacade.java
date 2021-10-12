package com.qf.data.view.facade.api;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.request.device.DeviceSaveRequest;

public interface DeviceFacade {

    ResultModel saveDevice(DeviceSaveRequest request);

}
