package com.qf.common.util;

import com.qf.common.dto.ResultBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestHttpUtil {

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
