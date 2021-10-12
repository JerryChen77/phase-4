package com.qf.myspringbootredisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootTest
public class TestPipeline {

  @Autowired
  private RedisTemplate redisTemplate;


  @Test
  public void test1(){
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
      redisTemplate.opsForValue().set("k"+i,"a"+i);
    }
    long end = System.currentTimeMillis();
    System.out.println("消耗的时间："+(end-start));
  }


  @Test
  public void testPipeline(){
    long start = System.currentTimeMillis();

    redisTemplate.executePipelined(new SessionCallback() {
      @Override
      public Object execute(RedisOperations operations) throws DataAccessException {
        for (int i = 0; i < 10000; i++) {
          operations.opsForValue().set("k"+i,"a"+i);
        }
        return null;
      }
    });
    long end = System.currentTimeMillis();
    System.out.println("消耗的时间："+(end-start));
  }


}
