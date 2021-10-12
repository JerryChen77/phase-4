package com.qf.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String crtJson(Map<String,Object> map) throws JsonProcessingException {

        return objectMapper.writeValueAsString(map);


    }

}
