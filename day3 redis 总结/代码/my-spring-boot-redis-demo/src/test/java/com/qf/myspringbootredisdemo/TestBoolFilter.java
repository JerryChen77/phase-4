package com.qf.myspringbootredisdemo;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestBoolFilter {

  @Test
  public void testBool(){


    Config config = new Config();
    config.useSingleServer().setAddress("redis://172.16.253.34:6379");
    config.useSingleServer().setPassword("qfjava");
    //构造Redisson
    RedissonClient redisson = Redisson.create(config);

    RLock lock = redisson.getLock("site:lock");
//    redisson.lock

    RBloomFilter<String> bloomFilter = redisson.getBloomFilter("nameList");
    //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小
    bloomFilter.tryInit(100000000L,0.03);
    //将xiaoming插入到布隆过滤器中
    bloomFilter.add("xiaoming");

    //判断下面号码是否在布隆过滤器中
    System.out.println(bloomFilter.contains("xiaoli"));//false
    System.out.println(bloomFilter.contains("xiaowang"));//false
    System.out.println(bloomFilter.contains("xiaoming"));//true


  }




}
