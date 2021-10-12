package com.qf.data.view.core.model.common;


import java.util.concurrent.TimeUnit;

public interface IRedisTemplateLoader {
  /**
   * 通过redisKey获得redis中对应的value
   *
   * @param redisKey
   * @return
   */
  String get(String redisKey);

  /**
   * 存入redis key-value键值对
   *
   * @param redisKey
   * @param redisContent
   */
  void set(String redisKey, String redisContent);

  /**
   * 存入redis key-value键值对和超时时间
   *
   * @param redisKey
   * @param redisContent
   * @param time
   * @param timeUnit
   */
  void set(String redisKey, String redisContent, Long time, TimeUnit timeUnit);

  /**
   * 删除ManagerId
   */
  void deleteManagerIp(String redisKey);

  /**
   * 通过ManagerId获得redis中对应的ip
   *
   * @param redisKey
   * @return
   */
  String getManagerIp(String redisKey);

  /**
   * 存入redis ManagerId-ip键值对
   *
   * @param redisKey
   * @param redisContent
   */
  void setManagerIp(String redisKey, String redisContent);

  String pop(String key, boolean isHead);

  double push(String key, String value, boolean isHead);
}

