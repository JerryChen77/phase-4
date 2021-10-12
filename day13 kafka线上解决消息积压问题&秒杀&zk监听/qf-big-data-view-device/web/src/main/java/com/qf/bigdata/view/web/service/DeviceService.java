package com.qf.bigdata.view.web.service;

import com.qf.data.view.core.model.request.device.DeviceInfoRequest;
import com.qf.data.view.core.model.result.ResultModel;

public interface DeviceService {

    ResultModel deviceInfo(DeviceInfoRequest request);

}
