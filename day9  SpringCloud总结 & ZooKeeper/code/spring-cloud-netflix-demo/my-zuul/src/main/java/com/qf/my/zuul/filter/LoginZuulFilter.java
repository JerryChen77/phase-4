package com.qf.my.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class LoginZuulFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 2;
  }

  @Override
  public boolean shouldFilter() {
    //context.set("limit",true);
    RequestContext context = RequestContext.getCurrentContext();
    Object isLimit = context.get("isLimit");
    if(Objects.isNull(isLimit)){
      return true;
    }
    return !(boolean)isLimit;
  }

  /**
   * 获取到cookie：login_token
   * @return
   * @throws ZuulException
   */
  @Override
  public Object run() throws ZuulException {
    //1.获得request对象
    RequestContext context = RequestContext.getCurrentContext();

    String loginToken = (String) context.get("loginToken");

    //2.判断是否有cookie
    if(StringUtils.isNotBlank(loginToken)){
      //有cookie，就放行
      context.setSendZuulResponse(true);
      return null;
    }
    //不放行
    context.setSendZuulResponse(false);
    context.setResponseStatusCode(200);
    context.setResponseBody("please do login!");

    return null;
  }
}
