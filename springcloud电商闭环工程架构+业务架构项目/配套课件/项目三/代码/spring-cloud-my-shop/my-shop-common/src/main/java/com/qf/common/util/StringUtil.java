package com.qf.common.util;

import java.util.UUID;

public class StringUtil {


    public static String getSmsCode(){


        int  code = (int) (Math.random()*10000);

        return String.valueOf(code);

    }

    //生成redis的键
    public static String getRedisKey(String pre, String phone) {

        return new StringBuilder().append(pre).append(phone).toString();

    }

    //uuid:  sldkjfs-sdfsdf-sdfsdf-sdfsd  ==> md5加密

    /**
     * 生成token
     * @return
     */
    public static String crtToken() {

        String uuid = UUID.randomUUID().toString();
        String token = uuid.replaceAll("-", "");
        return token;

    }
}
