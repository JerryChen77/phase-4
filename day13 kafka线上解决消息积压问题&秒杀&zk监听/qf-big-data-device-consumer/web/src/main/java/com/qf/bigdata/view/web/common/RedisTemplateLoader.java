package com.qf.bigdata.view.web.common;

import com.qf.data.view.core.model.common.IRedisTemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTemplateLoader implements IRedisTemplateLoader {


  @Autowired
  private StringRedisTemplate redisTemplate;


  private static final int REDIS_DB_INDEX = 15;

  private static final int MANAGER_TIMEOUT = 43200;
  private static final String MANAGER_ID_PREFIX = "managerId:";


  @Override
  public String get(String redisKey) {
    return redisTemplate.opsForValue().get(redisKey);
  }

  @Override
  public void set(String redisKey, String redisContent) {
    redisTemplate.opsForValue().set(redisKey, redisContent, MANAGER_TIMEOUT, TimeUnit.SECONDS);
  }

  @Override
  public void set(String redisKey, String redisContent, Long time, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(redisKey, redisContent, time, timeUnit);
  }

  @Override
  public void deleteManagerIp(String redisKey) {
    redisTemplate.delete(MANAGER_ID_PREFIX + redisKey);
  }

  @Override
  public String getManagerIp(String redisKey) {
    return redisTemplate.opsForValue().get(MANAGER_ID_PREFIX + redisKey);
  }

  @Override
  public void setManagerIp(String redisKey, String redisContent) {
    redisTemplate.opsForValue().set(MANAGER_ID_PREFIX + redisKey, redisContent, MANAGER_TIMEOUT, TimeUnit.SECONDS);
  }

  @Override
  public double push(String key, String value, boolean isHead) {
    double retVal = -1;
    try {
      if (isHead) {
        retVal = redisTemplate.opsForList().leftPush(key, value);
      } else {
        retVal = redisTemplate.opsForList().rightPush(key, value);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return retVal;
  }

  @Override
  public String pop(String key, boolean isHead) {//dev:onl:0 false
    String retVal = null;
    try {
      if (isHead) {
        retVal = redisTemplate.opsForList().leftPop(key);
      } else {
        retVal = redisTemplate.opsForList().rightPop(key);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return retVal;
  }


}
