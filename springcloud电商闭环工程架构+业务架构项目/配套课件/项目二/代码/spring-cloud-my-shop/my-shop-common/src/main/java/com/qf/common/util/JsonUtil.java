package com.qf.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String crtJson(Map<String,Object> map) throws JsonProcessingException {

        return objectMapper.writeValueAsString(map);
    }

    /**
     * 将json字符串转换成指定类型的对象
     */
    public static <T>T getObjectFromJson(String json,Class<T> clz) throws JsonProcessingException {

        return objectMapper.readValue(json, clz);
    }

}
