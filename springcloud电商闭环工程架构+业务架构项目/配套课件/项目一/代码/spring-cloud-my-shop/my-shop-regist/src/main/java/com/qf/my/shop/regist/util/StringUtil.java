package com.qf.my.shop.regist.util;

public class StringUtil {


    public static String getSmsCode(){


        int  code = (int) (Math.random()*10000);

        return String.valueOf(code);

    }

    //生成redis的键
    public static String getRedisKey(String pre, String phone) {

        return new StringBuilder().append(pre).append(phone).toString();

    }
}
