package com.qf.provider.service.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Provider {

  public static void main(String[] args) throws IOException {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"provider.xml"});
    context.start();
    System.in.read(); // 让当前服务一直在线，不会被关闭，按任意键退出
  }

}
