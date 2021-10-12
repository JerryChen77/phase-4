package com.qf.data.view.facade.service.worker;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.data.core.dal.po.WorkerPO;
import com.qf.data.view.core.model.constant.RedisConstant;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.core.model.util.RedisUtils;
import com.qf.data.view.core.service.worker.WorkerService;
import com.qf.data.view.facade.api.WorkerFacade;
import com.qf.data.view.facade.request.worker.WorkerModelRequest;
import com.qf.data.view.facade.request.worker.WorkerSignModelRequest;
import com.qf.data.view.facade.response.worker.WorkerModelResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class WorkerFacadeImpl implements WorkerFacade {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplatel;

    @Override
    public ResultModel<WorkerModelResponse> getWorkerById(WorkerModelRequest request) {
        //service->dao
        WorkerPO workerPO = workerService.selectByPrimaryKey(request.getId());
        //workerPO---复制成--->WorkerModelResponse
        WorkerModelResponse workerModelResponse = new WorkerModelResponse();
        BeanUtils.copyProperties(workerPO,workerModelResponse);
        return ResultModel.success(workerModelResponse);
    }

    /**
     * 保存数据到redis中
     * @param request signTime: 2021-08-13 08:55:33
     * @return
     */
    @Override
    public ResultModel workerSign(WorkerSignModelRequest request) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //1.获得redisKey， worker:sign:2021-08-16 worker:sign:2021-08-17 worker:sign:2021-08-18
        String signTime = request.getSignTime().substring(0, 10);
        //worker:sign:2021-08-16
        String redisKey = RedisUtils.getRedisKey(RedisConstant.WORKER_SIGN_PRE,signTime);
        //2.如果redis中没有这个值，初始化这个值,并且设置有效期
//        if (Objects.nonNull(redisTemplate.opsForValue().get(redisKey))) {
//            redisTemplate.opsForValue().set(redisKey,1,3, TimeUnit.DAYS);
//        }
        /*
        redis的原子操作-这样就可以实现分布式锁的效果：
        redisTemplate.opsForValue().increment(redisKey)
        给redis中的redisKey+1，每个线程只会得到一个唯一的值。体现了redis的单线程的特性。
         */
        if(redisTemplate.opsForValue().increment(redisKey)==1){
            redisTemplate.expire(redisKey,3,TimeUnit.DAYS);
        }
        return ResultModel.success();
    }

    //获得redis中的打卡员工总数
    @Override
    public ResultModel getSignTotal() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //1.获得当前时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(date);
        //2.组织redis的键
        String redisKey = RedisUtils.getRedisKey(RedisConstant.WORKER_SIGN_PRE, currentDate);
        Integer count = Integer.parseInt((String) redisTemplate.opsForValue().get(redisKey));
        return ResultModel.success(count);
    }
}
