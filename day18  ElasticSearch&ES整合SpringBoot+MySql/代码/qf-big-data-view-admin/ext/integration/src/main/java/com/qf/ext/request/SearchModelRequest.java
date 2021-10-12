package com.qf.ext.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchModelRequest implements Serializable {

    private Long deviceId;

    private String deviceName;
}
