package com.qf.data.view.facade.service.device;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.api.DeviceFacade;
import com.qf.data.view.facade.request.device.DeviceModelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class DeviceFacadeImpl implements DeviceFacade {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    public static final String DEVICE_TOPIC = "device_topic";

    @Override
    public ResultModel deviceInfo(DeviceModelRequest request) {

        //request->json
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(request);
            //向kafka去发消息
            //1.创建消息
            kafkaTemplate.send(DEVICE_TOPIC,request.getDeviceKey(),json);
        } catch (Exception e) {
            return ResultModel.error(e.getMessage());
        }



        return ResultModel.success();
    }
}
