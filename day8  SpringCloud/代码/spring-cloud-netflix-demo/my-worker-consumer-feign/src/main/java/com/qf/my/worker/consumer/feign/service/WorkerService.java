package com.qf.my.worker.consumer.feign.service;

import com.qf.common.entity.dto.WorkerDTO;

public interface WorkerService {

  String getWorker(Long id);

    String getForObject(WorkerDTO workerDTO);

  String getForObjectWithCookie(WorkerDTO workerDTO);
}
