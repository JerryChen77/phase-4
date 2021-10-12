package com.qf.my.worker.consumer.service;

import com.qf.common.entity.Worker;

public interface WorkerService {
  String getWorker(Long id);

  String getForObject(Long id, String name);

  String postForObject(Worker worker);

  String postForObjectWithCookie(Worker worker, String loginToken);
}
