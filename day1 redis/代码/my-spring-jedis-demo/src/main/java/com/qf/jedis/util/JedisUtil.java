package com.qf.jedis.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtil {

  private static GenericObjectPoolConfig poolConfig ;
  private static String host;
  private static JedisPool jedisPool;

  static{

    poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxIdle(10);
    poolConfig.setMaxTotal(10);//连接池的性能最好的时候，池中有10个连接被使用，且最大连接数是10
    poolConfig.setMinIdle(4);//最小连接数
    poolConfig.setMaxWaitMillis(3000);
    host = "172.16.253.34";

    //1.初始化连接池的配置
    jedisPool = new JedisPool(poolConfig,host);
  }

  /**
   * 获得一个连接对象
   * @return
   */
  public static Jedis getJedis(){
    Jedis jedis = jedisPool.getResource();
    return jedis;
  }

  /**
   * 获得连接池对象
   * @return
   */
  public static JedisPool getJedisPool(){
    return jedisPool;
  }

}
