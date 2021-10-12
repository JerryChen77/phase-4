package com.qf.thread.pool.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MyThreadPoolDemo {
  public static void main(String[] args) {

    /*
    public ThreadPoolExecutor(int corePoolSize, 核心线程数
                              int maximumPoolSize, 最大线程数
                              long keepAliveTime, 发呆时间（空闲时间）
                              TimeUnit unit, 时间单位
                              BlockingQueue<Runnable> workQueue, 阻塞队列
                              ThreadFactory threadFactory, 线程工厂
                              RejectedExecutionHandler handler) 拒绝策略
     */

    //1.创建线程池的方式-官方提供的四种api创建线程池都会造成内存溢出
    /* 1)创建只有一条线程的线程池
    new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()) 队列容量过大，允许存入最大21亿个任务，导致内存溢出
     */
//    ExecutorService pool = Executors.newSingleThreadExecutor();

    /*
    2)创建一个具有多条线程的线程池
    new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());队列容量过大，允许存入最大21亿个任务，导致内存溢出
     */
//    ExecutorService pool = Executors.newFixedThreadPool(4);

    /*
    3)创建一个线程池，线程池中的线程数根据当前的内存情况来定，也就是说内存允许的情况下会一直创建出新的线程
    new ThreadPoolExecutor(0, Integer.MAX_VALUE, 创建的线程数过多，导致内存溢出
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
     */
//    ExecutorService pool = Executors.newCachedThreadPool();

    /*
    4)创建一个跟定时任务相关的线程池
     super(corePoolSize, Integer.MAX_VALUE, 最大线程数过大，导致内存溢出
     0, NANOSECONDS,
              new DelayedWorkQueue());
     */
//    ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);


  }
}
