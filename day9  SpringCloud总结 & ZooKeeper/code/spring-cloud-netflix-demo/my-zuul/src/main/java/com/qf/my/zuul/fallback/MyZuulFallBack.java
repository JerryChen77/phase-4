package com.qf.my.zuul.fallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.activation.MimeType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class MyZuulFallBack implements FallbackProvider {
  /**
   * 指明对哪个服务进行熔断
   * @return
   */
  @Override
  public String getRoute() {
//    return null; //对所有服务进行熔断配置
    return "WORKER-CONSUMER";//对WORKER-CONSUMER服务进行熔断配置
  }

  /**
   * 具体要熔断的内容
   * @param route
   * @param cause
   * @return 响应消息：消息头、消息行、消息体
   */
  @Override
  public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
    return new ClientHttpResponse() {
      /**
       * 设置响应消息的内容类型：json
       * @return
       */
      @Override
      public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
//        headers.put(HttpHeaders.CONTENT_TYPE, MimeType)
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
      }

      @Override
      public InputStream getBody() throws IOException {

        //java对象转换成json ： jackson、gson、fastjson
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",1000);
        map.put("message","success");
        map.put("data","Please Check Your Network!");
        String json = objectMapper.writeValueAsString(map);
        //String->byte数组-》存到输入流中
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return inputStream;
      }

      //响应行
      @Override
      public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.OK;
      }

      //响应行
      @Override
      public int getRawStatusCode() throws IOException {
        return HttpStatus.OK.value();
      }

      //响应行
      @Override
      public String getStatusText() throws IOException {
        return HttpStatus.OK.getReasonPhrase();
      }

      @Override
      public void close() {

      }
    };
  }
}
