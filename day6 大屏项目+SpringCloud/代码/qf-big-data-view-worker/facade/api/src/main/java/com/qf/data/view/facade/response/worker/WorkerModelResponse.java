package com.qf.data.view.facade.response.worker;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkerModelResponse implements Serializable {
    private Long id;

    private String name;

    private Boolean gender;

    private Integer age;

    private Boolean workType;

    private Boolean status;

    private Boolean flag;



}
