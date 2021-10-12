package com.qf.common.util;

import com.qf.common.dto.ResultBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestHttpUtil {

    public static <T>T requestByPost(HttpHeaders headers,
                                           MultiValueMap<String, Object> map,
                                           String url,
                                           Class<T> clz,
                                           RestTemplate restTemplate){


        HttpEntity entity = new HttpEntity(map,headers);
        return restTemplate.postForObject(url, entity, clz);
    }


    /**
     * 发送带请求参数的post请求
     * @param headers
     * @param map
     * @param url
     * @param restTemplate
     * @return
     */
    public static ResultBean requestByPost(HttpHeaders headers,
                                           MultiValueMap<String, Object> map,
                                           String url,
                                           RestTemplate restTemplate){


        HttpEntity entity = new HttpEntity(map,headers);
        ResultBean resultBean = restTemplate.postForObject(
                url,
                entity,
                ResultBean.class
        );

        return resultBean;
    }


}
