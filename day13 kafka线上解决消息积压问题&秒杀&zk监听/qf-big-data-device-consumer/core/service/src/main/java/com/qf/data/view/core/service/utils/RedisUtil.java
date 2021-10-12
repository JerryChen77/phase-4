package com.qf.data.view.core.service.utils;

public class RedisUtil {
    /**
     * 生成redis的key
     * @param pre
     * @param content
     * @return
     */
    public static String crtRedisKey(String pre,String content){
        return new StringBuilder().append(pre).append(content).toString();
    }


}
