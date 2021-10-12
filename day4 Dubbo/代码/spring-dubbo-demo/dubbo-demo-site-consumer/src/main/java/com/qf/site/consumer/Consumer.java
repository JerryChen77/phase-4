package com.qf.site.consumer;

import com.qf.api.SiteService;
import com.qf.api.entity.Site;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {


  public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"consumer.xml"});
    context.start();
    /*
    下面这一整个过程。都是在执行远程过程调用—— rpc remote produce call 服务框架
     */
    //获取一个代理，代理服务提供者内提供的bean
    SiteService service = (SiteService)context.getBean("siteService"); // 获取远程服务代理
    //调用代理对象的getName方法。通过代理对象调到服务提供者内的bean
//    String result = service.getName("hellodubbo");
    Site site = service.getSiteById(1001L);
    System.out.println(site);


  }

}
