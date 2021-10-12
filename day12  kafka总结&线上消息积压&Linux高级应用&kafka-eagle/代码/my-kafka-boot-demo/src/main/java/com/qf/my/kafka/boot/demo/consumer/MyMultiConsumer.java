package com.qf.my.kafka.boot.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.qf.my.kafka.boot.demo.common.RedisTemplateLoader;
import com.qf.my.kafka.boot.demo.entity.DeviceDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MyMultiConsumer {

  private static Cache<String, Long> deviceOnLineCache = CacheBuilder.newBuilder().maximumSize(1000000).expireAfterWrite(1, TimeUnit.HOURS).build();



  @Autowired
  private RedisTemplateLoader redisTemplateLoader;

  @Resource(name = "listenerTaskExecutor")
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  private int areaTotal = 4;

  private Map<String, Boolean> handlerRunMap = new ConcurrentHashMap<>();

  @PostConstruct
  public void initHandler() {
    for (int n = 0; n < areaTotal; n++) {
      String redisKey = "dev:onl:" + n;
      if (!handlerRunMap.containsKey(redisKey)) {
        handlerRunMap.put(redisKey, true);
        threadPoolTaskExecutor.execute(new MyHandler(redisKey, true));
      }
    }
  }

  private boolean checkDeviceOnlineCache(DeviceDTO deviceDTO) {
    String commonDeviceRedisKey = deviceDTO.getDeviceKey();
    Long lastTime = deviceOnLineCache.getIfPresent(commonDeviceRedisKey);
    if (lastTime == null || lastTime <= 0 || lastTime <= deviceDTO.getLastActiveTime()) {
      deviceOnLineCache.put(commonDeviceRedisKey, deviceDTO.getLastActiveTime());
      return false;
    }
    return true;
  }




  /**
   * 使用Redis队列+多线程+解决Kafka消息过多，DB处理过慢，造成消息堆积的问题
   */
  @KafkaListener(id="consumer",topicPartitions = { @TopicPartition(topic = "my-replicated-topic")}, groupId = "MyGroup1",containerFactory = "kafkaListenerContainerFactory")
  public void eventListenerCacheByRedis2(List<ConsumerRecord<Integer, String>> records) {
    for (ConsumerRecord<Integer, String> record : records) {
      DeviceDTO commonDevice = JSON.parseObject(record.value(), DeviceDTO.class);
      //设置redis的key, 包含平台类型和设备key
      if (checkDeviceOnlineCache(commonDevice)) {
        continue;
      }
      //dev:onl:0 dev:onl:1 dev:onl:2 dev:onl:3
      String redisKey = "dev:onl:" + Math.abs(commonDevice.getDeviceKey().hashCode()) % areaTotal;
      redisTemplateLoader.push(redisKey, record.value(), true);
      if (!handlerRunMap.containsKey(redisKey)) {
        handlerRunMap.put(redisKey, true);
        //消费最新数据
        threadPoolTaskExecutor.execute(new MyHandler(redisKey, true));
        //消费最老数据，防止老数据消费不到，导致数据堆积
        threadPoolTaskExecutor.execute(new MyHandler(redisKey, false));
      }
    }
  }



  class MyHandler implements Runnable {
    private String redisKeyRun;
    private Boolean isHead;
  //dev:onl:0 true
    public MyHandler(String redisKeyRun, Boolean isHead) {
      this.redisKeyRun = redisKeyRun;
      this.isHead = isHead;
    }

    @Override
    public void run() {
      String dataStr = StringUtils.EMPTY;
      try {
        if (StringUtils.isBlank(redisKeyRun)) {
          Thread.sleep(1000);
          return;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      boolean isUse = true;
      for (; isUse; ) {
        try {
          ////dev:onl:0 true
          dataStr = redisTemplateLoader.pop(redisKeyRun, isHead);
          if (StringUtils.isBlank(dataStr)) {
            break;//redis队列里已经没有消息
          }
          //把redis里弹出来的消息转换成DeviceDTO设备对象
          DeviceDTO deviceDTO = JSON.parseObject(dataStr, DeviceDTO.class);
          //判断缓存中的时间（和消息传过来的设备时间进行比较）是否是最新的，如果是最新的：true。否则返回false，并更新缓存中的时间
          if (checkDeviceOnlineCache(deviceDTO)) {
            continue;
          }
          //保存设备信息到到DB
//          saveDevice(deviceDTO);
        } catch (Exception e) {
          e.printStackTrace();
          isUse = false;
        }
      }
      if (isHead) {
        handlerRunMap.remove(redisKeyRun);
      }
    }
  }



}
