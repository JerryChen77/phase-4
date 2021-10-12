package com.qf.bigdata.view.web.task;

import com.qf.bigdata.view.web.controller.api.WorkerAPIController;
import com.qf.data.view.core.model.request.WorkerSignRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class IncrSignTask {

    @Autowired
    WorkerAPIController workerAPIController;

    @Scheduled(cron = "0/1 * * * * ? ")
    public void process(){

        //向redis中增加打卡数量
        WorkerSignRequest request = new WorkerSignRequest();
        request.setWorkerId(1001L);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(date);
        request.setSignTime(time);
        workerAPIController.workerSign(request);
    }

}
