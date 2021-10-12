package com.qf.my.worker.consumer.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.qf.common.entity.Worker;
import com.qf.my.worker.consumer.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkerServiceImpl implements WorkerService {

  @Autowired
  private RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "hiError")
  @Override
  public String getWorker(Long id) {

    String url = "http://WORKER-PROVIDER/worker/get/"+id;//http://localhost:8762/worker/get/1001
//    String url = "http://localhost:8762/worker/get/"+id;//http://localhost:8762/worker/get/1001
    String result = restTemplate.getForObject(url, String.class);



    return result;
  }

  public String hiError(Long id) {
    return "你的网络有问题，请联系当地的电信服务提供商";
  }


  @Override
  public String getForObject(Long id, String name) {

    //=========getForObject===============

      //****getForObject(String url, Class<T> responseType)*****
//    String url = "http://WORKER-PROVIDER/worker/getForObject?id="+id+"&name="+name;
//    String result = restTemplate.getForObject(url, String.class);


      //*****getForObject(URI url, Class<T> responseType)*****
//    URI uri = ;
//    String result = restTemplate.getForObject(uri, String.class);

    //********getForObject(String url, Class<T> responseType, Object... uriVariables)***********
//    String url = "http://WORKER-PROVIDER/worker/getForObject?id={1}&name={2}";
//    String result = restTemplate.getForObject(url, String.class, new Object[]{id, name});

    //********getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)***********
//    String url = "http://WORKER-PROVIDER/worker/getForObject?id={id}&name={name}";
//    Map<String,Object> map = new HashMap<>();
//    map.put("id",id);
//    map.put("name",name);
//    String result = restTemplate.getForObject(url, String.class, map);


    //=========getForEntity===============
    String url = "http://WORKER-PROVIDER/worker/getForObject?id="+id+"&name="+name;
    ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
    String result = entity.getBody();


    return result;
  }

  /**
   * 使用restTemplate完成post请求
   * @param worker
   * @return
   */
  @Override
  public String postForObject(Worker worker) {

    //方式一：postForEntity(String url, Object request, Class<T> responseType)
//    String url = "http://WORKER-PROVIDER/worker/postForObject";
//    ResponseEntity<String> entity = restTemplate.postForEntity(url, worker, String.class);
//    return entity.getBody();

//    //方式二：
//
//    MultiValueMap<String, Object> parammap = new LinkedMultiValueMap<>();
//    parammap.add("id",worker.getId());
//    parammap.add("name",worker.getName());
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//    //请求消息entity中封装了请求消息体（ID，name），和请求头（封装了消息的类型是json格式）
//    HttpEntity entity  =new HttpEntity(parammap,headers);
//
//    String url = "http://WORKER-PROVIDER/worker/postForObject1";
//    ResponseEntity<String> entity1 = restTemplate.postForEntity(url, entity, String.class);
//    return entity1.getBody();

        //方式三：


    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    //请求消息entity中封装了请求消息体（ID，name），和请求头（封装了消息的类型是json格式）
    HttpEntity entity  =new HttpEntity(worker);

    String url = "http://WORKER-PROVIDER/worker/postForObject";
    ResponseEntity<String> entity1 = restTemplate.postForEntity(url, entity, String.class);
    return entity1.getBody();





  }

  //携带cookie
  @Override
  public String postForObjectWithCookie(Worker worker, String loginToken) {

    HttpHeaders headers = new HttpHeaders();
    //list里可以存放多个cookie键值对
    List<String> cookieValue = new ArrayList<>();
    cookieValue.add("login_token="+loginToken);
    headers.put("Cookie",cookieValue);
    //封装了请求体，和请求头，请求头中有cookie
    HttpEntity entity  =new HttpEntity(worker,headers);

    String url = "http://WORKER-PROVIDER/worker/postForObjectWithCookie";
    ResponseEntity<String> entity1 = restTemplate.postForEntity(url, entity, String.class);
    return entity1.getBody();
  }
}
