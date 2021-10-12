package com.qf.thread.pool.demo;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolDemo1 {
  public static void main(String[] args) throws IOException {

    /*
    线程池初始化多少条线程是比较合适的？
    根据可获得cpu的核心数来确定
    最大线程数应该是多少：
    计算型：最大线程数=核心线程数
    突发型：最大线程数=核心线程数*2

     */
    //获得cpu的核心数
    int corePoolSize = Runtime.getRuntime().availableProcessors();

    //目标：使用线程池来创建多线程，执行多个任务
    ThreadPoolExecutor pool = new ThreadPoolExecutor(
      corePoolSize,
      corePoolSize * 2,
      60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue(100),
//      new ThreadPoolExecutor.DiscardPolicy()
      new ThreadPoolExecutor.CallerRunsPolicy()
    );

    for (int i = 0; i < 10000; i++) {
      //线程池提交10000个任务
      pool.submit(new MyRunnable(i));
    }

    System.in.read();


  }

  static class MyRunnable implements Runnable{

    private int i;
    public MyRunnable(int i){
      this.i = i;
    }


    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName()+":"+i);
    }
  }

}
