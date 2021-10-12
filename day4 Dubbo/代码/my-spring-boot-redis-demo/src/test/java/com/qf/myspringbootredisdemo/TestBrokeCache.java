package com.qf.myspringbootredisdemo;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestBrokeCache {


  @Autowired
  private RedisTemplate redisTemplate;


  /**
   * 模拟从数据库获取数据。但是数据库中只有id：1-10的数据
   * @param id
   */
  public String getValueFromDB(int id){

    if(id>10){
      //说明数据库中没有该id的数据
      return null;
    }
    //返回数据
    return "v"+id;

  }

  @Test
  public void testQuery(){

    for (int i = 61; i <= 70; i++) {
      String value = (String) redisTemplate.opsForValue().get("k" + i);
      if(Objects.isNull(value)){
        //redis中的数据是空的=》查询数据库
        String valueFromDB = getValueFromDB(i);
        if(Objects.isNull(valueFromDB)){
          //主动创建一个没有意义的空的对象。进行缓存。并设置有效期
          valueFromDB = new String();
          redisTemplate.opsForValue().set("k" + i,valueFromDB,300, TimeUnit.SECONDS);
          System.out.println("查询数据库");
        }else{
          //把数据存到redis中
          redisTemplate.opsForValue().set("k" + i,valueFromDB);
          System.out.println("查询数据库");
        }
        continue;
      }
      //查到数据后的业务
      System.out.println("查询缓存");
    }



  }


//  @Test
//  public void initCache(){
//    for (int i = 1; i <= 10; i++) {
//      //模拟从数据库获取数据存入到redis中
//      redisTemplate.opsForValue().set("k"+i,"v"+i);
//    }
//
//
//  }




}
