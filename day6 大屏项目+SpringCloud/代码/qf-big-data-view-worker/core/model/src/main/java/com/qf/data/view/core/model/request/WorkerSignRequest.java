package com.qf.data.view.core.model.request;

import lombok.Data;

@Data
public class WorkerSignRequest {

    private Long workerId;
    private Long deviceId;
    private String signTime;


}
