package com.qf.my.worker.consumer.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * feign执行http请求之前会执行该拦截器
 */
public class FeignInterceptor implements RequestInterceptor {
  /*
  这个方法。是feign在发送http请求。我们重写这个方法的目的是feign发http请求时先获得cookie，然后再携带cookie发送http请求
   */
  @Override
  public void apply(RequestTemplate requestTemplate) {
    //1.先获得cookie，在request对象里，-》先获得request对象
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
    HttpServletRequest request = servletRequestAttributes.getRequest();
    //2.request对象中获得cookie
    Cookie[] cookies = request.getCookies();
    String loginToken = "";
    for (Cookie cookie : cookies) {
      if("login_token".equals(cookie.getName())){
        loginToken = cookie.getValue();
        break;
      }
    }
    if(StringUtils.isNotBlank(loginToken)){
      //2.封装到Feign的http请求头中
      requestTemplate.header(HttpHeaders.COOKIE,new StringBuffer().append("login_token").append("=").append(loginToken).toString());
    }

  }
}
