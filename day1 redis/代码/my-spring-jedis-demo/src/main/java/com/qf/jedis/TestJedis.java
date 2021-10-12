package com.qf.jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qf.jedis.entity.Student;
import com.qf.jedis.util.JedisUtil;
import org.junit.Test;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class TestJedis {


  /**
   * 存储java对象
   * 向redis保存一个hash对象
   */
  @Test
  public void testHashObjectToReids(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();

    Student stu = new Student();
    stu.setId(2001L);
    stu.setName("xiaoming");
    stu.setAge(20);

    //2
    Map<String,String> map = new HashMap<>();
    map.put("id",String.valueOf(stu.getId()));
    map.put("name",stu.getName());
    map.put("age",String.valueOf(stu.getAge()));
    jedis.hmset("student1:2001",map);


  }



  /**
   * 取java对象
   */
  @Test
  public void testGetObjectFromRedis(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
    String key = "stu:2001";
    //序列化键
    byte[] keyBytes = SerializationUtils.serialize(key);

    //从redis中取值
    byte[] valueBytes = jedis.get(keyBytes);
    //反序列化
    Object deserialize = SerializationUtils.deserialize(valueBytes);
    if(deserialize instanceof Student){
      Student stu = (Student) deserialize;
      System.out.println(stu);
    }

  }






  /**
   * 存储java对象
   */
  @Test
  public void testObjectToRedis(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();

    Student stu = new Student();
    stu.setId(2001L);
    stu.setName("xiaoming");
    stu.setAge(20);



    String key = "stu:2001";

    byte[] keyBytes = SerializationUtils.serialize(key);

    byte[] valBytes = SerializationUtils.serialize(stu);


    jedis.set(keyBytes,valBytes);




  }





  /**
   * 从redis中取数据
   */
  @Test
  public void testGetObject(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
    String json = jedis.get("student:2001");
    //2.将json解析成java对象
    Student student = JSONObject.parseObject(json, Student.class);
    System.out.println(student);



  }

  /**
   * 存储java对象
   * 向redis保存一个json字符串
   */
  @Test
  public void testObject(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();

    Student stu = new Student();
    stu.setId(2001L);
    stu.setName("xiaoming");
    stu.setAge(20);

    //2.转换成json字符串
    String json = JSONObject.toJSONString(stu);
    jedis.set("student:"+stu.getId(),json);


  }



  @Test
  public void testZset(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
    Map<String, Double> map = new HashMap<>();
    map.put("nba",2000d);
    jedis.zadd("myzset",map);
  }


  @Test
  public void testSet(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
//    while (true){
////      if()//1,结束输入
//      jedis.sadd("myset","a");
//    }
    String myset = jedis.spop("myset");

  }

  @Test
  public void testList(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();

  }


  @Test
  public void testHash(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
//    jedis.hexists();
  }


  @Test
  public void testString(){
    //1.从连接池中获得一个连接对象
    Jedis jedis = JedisUtil.getJedis();
    //2.执行redis的操作
    jedis.set("k5","v5");
//    jedis.setnx();





  }

  @Test
  public void testJedis(){

    //1.连接redis服务器
    int port = 6379;
    String host = "172.16.253.34";
    Jedis jedis = new Jedis(host,port);
    //2.操作redis
    jedis.set("mydream","1000000000");
    System.out.println(jedis.get("mydream"));

  }



}
