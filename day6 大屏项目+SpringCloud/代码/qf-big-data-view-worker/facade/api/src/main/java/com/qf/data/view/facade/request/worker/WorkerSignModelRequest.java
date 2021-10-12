package com.qf.data.view.facade.request.worker;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkerSignModelRequest implements Serializable {
    private Long workerId;
    private Long deviceId;
    private String signTime;

}
