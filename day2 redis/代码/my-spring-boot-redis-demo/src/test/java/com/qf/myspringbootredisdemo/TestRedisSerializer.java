package com.qf.myspringbootredisdemo;


import com.qf.myspringbootredisdemo.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestRedisSerializer {

  @Autowired
  private RedisTemplate redisTemplate;

  /*
   @Test
  public void testRedisString(){

    //操作redis 来存入一个值
    ValueOperations valueOperations = redisTemplate.opsForValue();
    valueOperations.set("springboot:redis:1001","abc",10, TimeUnit.MINUTES);


  }
   */


  @Test
  public void testObjectToRedis(){

    redisTemplate.setKeySerializer(new StringRedisSerializer());

    redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

    Student stu = new Student();
    stu.setId(1002L);
    stu.setName("xiaomin1g");
    stu.setAge(10);
    redisTemplate.opsForValue().set("mystu1",stu);



  }

  @Test
  public void testStringSerializer(){

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    ValueOperations valueOperations = redisTemplate.opsForValue();
    valueOperations.set("springboot:redis:1003","abc",10, TimeUnit.MINUTES);

  }

  @Test
  public void testGetStringSerializer(){

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    ValueOperations valueOperations = redisTemplate.opsForValue();
    System.out.println(valueOperations.get("springboot:redis:1003"));


  }



}
