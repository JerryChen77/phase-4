package com.qf.data.view.facade.request.device;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceSaveRequest implements Serializable {

    private String deviceKey;
    private Long lastActiveTime;


}
