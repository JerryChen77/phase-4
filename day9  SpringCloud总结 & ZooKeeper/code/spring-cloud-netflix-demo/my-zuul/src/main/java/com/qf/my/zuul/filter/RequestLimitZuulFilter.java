package com.qf.my.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
public class RequestLimitZuulFilter extends ZuulFilter {

  @Autowired
  private StringRedisTemplate redisTemplate;

  /**
   * 指明过滤器的类型
   * @return
   */
  @Override
  public String filterType() {
    return "pre";
  }

  /**
   * 指明相同类型的多个过滤器的顺序
   * @return
   */
  @Override
  public int filterOrder() {
    return 1;
  }

  /**
   * 是否执行过滤,如果有多个过滤器的，这个地方要设置成false，最后一个过滤器设置成true
   * @return
   */
  @Override
  public boolean shouldFilter() {
    return true;
  }

  /**
   * 执行具体的业务：做一个计数器限流，限制后端服务的访问数5个，访问数谁来维护
   *  重点关注：放行和不放行
   * @return
   * @throws ZuulException
   */
  @Override
  public Object run() throws ZuulException {
    //去获得一个zuul的上下文对象
    RequestContext context = RequestContext.getCurrentContext();


    //原子操作，防止线程安全问题
    Long count = redisTemplate.opsForValue().increment("zuul:count");
    if(count==1){
      redisTemplate.expire("zuul:count",60, TimeUnit.SECONDS);
    }
    if(count<=5){
      //获取cookie，传给下一个过滤器
      HttpServletRequest request = context.getRequest();
      Cookie[] cookies = request.getCookies();
      String loginToken = "";
      for (Cookie cookie : cookies) {
        if("login_token".equals(cookie.getName())){
          loginToken = cookie.getValue();
          break;
        }
      }
      if(StringUtils.isNotBlank(loginToken)){
        //存一个键值对。让下一个filter去取
        context.set("loginToken",loginToken);
      }
      //放行
      context.setSendZuulResponse(true);
      context.setResponseStatusCode(200);
      return null;
    }

    //不放行
    context.set("isLimit",true);
    context.setSendZuulResponse(false);
    context.setResponseStatusCode(200);
    context.setResponseBody("request reach max!");
    return null;
  }
}
