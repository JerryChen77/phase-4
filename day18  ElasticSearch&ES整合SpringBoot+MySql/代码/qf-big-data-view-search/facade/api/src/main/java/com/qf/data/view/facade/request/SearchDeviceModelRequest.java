package com.qf.data.view.facade.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchDeviceModelRequest implements Serializable {

    private Long deviceId;
    private String deviceName;


}
