package com.qf.bigdata.view.web.listener;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.qf.bigdata.view.web.common.RedisTemplateLoader;
import com.qf.bigdata.view.web.dto.DeviceDTO;
import com.qf.data.view.facade.api.DeviceFacade;
import com.qf.data.view.facade.request.device.DeviceSaveRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DeviceKafkaListener {

    public static final String DEVICE_TOPIC = "device_topic";

    @Reference
    private DeviceFacade deviceFacade;


    /*@KafkaListener(topics = DEVICE_TOPIC,groupId = "group1")
    public void process(ConsumerRecord<String,String> record){
        //保存到db里面

        String value = record.value();
       if(StringUtils.isNotEmpty(value)){
           DeviceDTO deviceDTO = JSONObject.parseObject(value, DeviceDTO.class);
           DeviceSaveRequest deviceSaveRequest = new DeviceSaveRequest();
           BeanUtils.copyProperties(deviceDTO,deviceSaveRequest);
           deviceFacade.saveDevice(deviceSaveRequest);
       }
    }*/


    public void saveDeviceToDB(DeviceDTO deviceDTO){
        //保存到db里面

            DeviceSaveRequest deviceSaveRequest = new DeviceSaveRequest();
            BeanUtils.copyProperties(deviceDTO,deviceSaveRequest);
            deviceFacade.saveDevice(deviceSaveRequest);

    }



    private static Cache<String, Long> deviceOnLineCache = CacheBuilder.newBuilder().maximumSize(1000000).expireAfterWrite(1, TimeUnit.HOURS).build();

    @Autowired
    private RedisTemplateLoader redisTemplateLoader;

    @Resource(name = "listenerTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private int areaTotal = 4;

    private Map<String, Boolean> handlerRunMap = new ConcurrentHashMap<>();

    /**
     * 在初始化的时候，开启四个线程。消费redis中，四个队列的队头的信息
     */
    @PostConstruct
    public void initHandler() {
        for (int n = 0; n < areaTotal; n++) {
            String redisKey = "dev:onl:" + n;//redis中队列的key
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
    @KafkaListener(id="consumer2",topics = DEVICE_TOPIC, groupId = "MyGroup3")
    public void eventListenerCacheByRedis2(ConsumerRecord<Integer, String> record) {

            DeviceDTO commonDevice = JSON.parseObject(record.value(), DeviceDTO.class);
            //设置redis的key, 包含平台类型和设备key
            if (checkDeviceOnlineCache(commonDevice)) {
                return;
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



    class MyHandler implements Runnable {
        private String redisKeyRun;
        private Boolean isHead;
        //dev:onl:0 false
        public MyHandler(String redisKeyRun, Boolean isHead) {
            this.redisKeyRun = redisKeyRun;
            this.isHead = isHead;
        }

        @Override
        public void run() {
            String dataStr = org.apache.commons.lang3.StringUtils.EMPTY;
            try {
                if (org.apache.commons.lang3.StringUtils.isBlank(redisKeyRun)) {
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
                    if (org.apache.commons.lang3.StringUtils.isBlank(dataStr)) {
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
                    saveDeviceToDB(deviceDTO);

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
