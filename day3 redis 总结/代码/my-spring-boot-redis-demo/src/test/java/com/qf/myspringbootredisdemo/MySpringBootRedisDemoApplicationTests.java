package com.qf.myspringbootredisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MySpringBootRedisDemoApplicationTests {

  /**
   * 用来操作redis服务器的：
   * 操作redis的五种数据类型
   */
  @Autowired
  private RedisTemplate redisTemplate;


  @Test
  public void testRedisString(){

    //操作redis 来存入一个值
    ValueOperations valueOperations = redisTemplate.opsForValue();
    valueOperations.set("springboot:redis:1001","abc",10, TimeUnit.MINUTES);


  }

  @Test
  public void testRedisGetString(){

    //操作redis 来存入一个值
    ValueOperations valueOperations = redisTemplate.opsForValue();
    String value = (String) valueOperations.get("springboot:redis:1001");
    System.out.println(value);


  }





  @Test
  public void testHashGet(){
//    System.out.println(redisTemplate.opsForHash().get("student:1005", "name"));
    Map entries = redisTemplate.opsForHash().entries("student:1005");

    System.out.println(entries);
  }

  @Test
  public void testRedisHash(){

    Map<String,String> map = new HashMap<>();
    map.put("name","xiaoming");
    map.put("age","20");

    redisTemplate.opsForHash().putAll("student:1005",map);


  }



  @Test
  public void testRedisList(){

//    redisTemplate.opsForList().


  }

  @Test
  public void testRedisSet(){

//    redisTemplate.opsForSet().


  }

  @Test
  public void testRediszSet(){

//    redisTemplate.opsForZSet().


  }






  @Test
  void contextLoads() {
  }

}
