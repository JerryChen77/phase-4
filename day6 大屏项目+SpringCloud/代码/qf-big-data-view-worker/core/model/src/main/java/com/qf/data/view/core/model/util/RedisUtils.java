package com.qf.data.view.core.model.util;

public class RedisUtils {

    public static String getRedisKey(String prefix,String content){
        return new StringBuffer().append(prefix).append(":").append(content).toString();
    }


}
