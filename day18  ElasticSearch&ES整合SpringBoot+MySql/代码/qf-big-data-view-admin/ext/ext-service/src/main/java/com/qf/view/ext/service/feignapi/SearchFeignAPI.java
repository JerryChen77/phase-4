package com.qf.view.ext.service.feignapi;

import com.qf.data.view.core.model.result.ResultModel;
import com.qf.view.ext.service.request.SearchDeviceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("SEARCH-WEB")
public interface SearchFeignAPI {


    @RequestMapping("/search/device")
    ResultModel searchDevice(@RequestBody SearchDeviceRequest request);

}
