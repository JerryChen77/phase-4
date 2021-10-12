package com.qf.data.view.facade.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchDeviceModelResponse implements Serializable {

    private Long deviceId;
    private String deviceName;
    private String lastActiveTime;

}


