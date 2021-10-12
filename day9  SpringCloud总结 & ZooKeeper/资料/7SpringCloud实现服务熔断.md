# Hystrix 实现服务熔断

## 熔断器简介

在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以通过 `RPC或http` 相互调用。为了保证其高可用，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证 100% 可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，`Servlet` 容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的 **“雪崩”** 效应。

为了解决这个问题，业界提出了熔断器模型。

Netflix 开源了 Hystrix 组件，实现了熔断器模式，Spring Cloud 对这一组件进行了整合。在微服务架构中，一个请求需要调用多个服务是非常常见的，如下图：

![](pic\11.png)

较底层的服务如果出现故障，会导致连锁故障。当对特定的服务的调用的不可用达到一个阀值（Hystrix 是 **5 秒 20 次**） 熔断器将会被打开。

![](pic\12.png)

熔断器打开后，为了避免连锁故障，通过 `fallback` 方法可以直接返回一个固定值。







![1570676634175](pic\1570676634175.png)





![1570676646098](pic\1570676646098.png)



![1570676662451](pic\1570676662451.png)





- Hystrix的设计原理

(1)      防止任何单个依赖项耗尽所有容器用户线程

(2)      减少负载，快速失败，而不是排队

(3)      在失败的情况下提供回退保护用户可用性

(4)      使用隔离技术来限制依赖之间的影响

(5)      通过近乎实时的度量、监视和警报来优化发现时间

(6)      在大多数方面hystrix支持低延迟传播配置和动态属性修改来优化恢复时间，这允许您使用低延迟反馈循环进行实时操作修改

(7)      防止整个依赖客户端执行中的失败，而不仅仅是网络流量中的失败



- Hystrix如何实现的

(1)      通过hystrixCommand或者HystrixObservableCommand来封装对外部依赖的访问请求，这个访问请求一般会运行在独立的线程中

(2)      对于超出我们设定的阈值服务调用，直接进行超时返回，不允许它长时间的阻塞

(3)      对每一个依赖服务进行资源隔离。通过线程池或者是semaphore这两种方式

(4)      对依赖服务被调用的成功次数，失败次数，拒绝次数，超时次数进行统计

(5)      如果对某一个依赖服务的调用失败次数超过了一点的阈值，Hystrix自动进行熔断，并在一段时间内对该服务的调用直接进行降级，一段时间后再自动尝试恢复

(6)      当对一个服务调用出现失败、被拒绝、超时、短路等异常情况时，自动调用fallback降级机制

(7)      对属性和配置的修改提供近实时的支持









![1570676728368](pic\1570676728368.png)



## Ribbon 中使用熔断器

### 在 `pom.xml` 中增加依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

### 在 Application 中增加 `@EnableHystrix` 注解

```
package com.duo.hello.spring.cloud.web.admin.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class WebAdminRibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAdminRibbonApplication.class, args);
    }
}
```

### 在 Service 中增加 `@HystrixCommand` 注解

在 Ribbon 调用方法上增加 `@HystrixCommand` 注解并指定 `fallbackMethod` 熔断方法

```
package com.duo.hello.spring.cloud.web.admin.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String sayHi(String message) {
        return restTemplate.getForObject("http://HELLO-SPRING-CLOUD-SERVICE-ADMIN/hi?message=" + message, String.class);
    }

    public String hiError(String message) {
        return "Hi，your message is :\"" + message + "\" but request error.";
    }
}
```

### 测试熔断器

此时我们关闭服务提供者，再次请求 http://localhost:8764/hi?message=HelloRibbon 浏览器会显示：

```
Hi，your message is :"HelloRibbon" but request error.
```

## Feign 中使用熔断器

Feign 是自带熔断器的，但默认是关闭的。需要在配置文件中配置打开它，在配置文件增加以下代码：

```
feign:
  hystrix:
    enabled: true
```

### 在 Service 中增加 `fallback` 指定类

```
package com.duo.hello.spring.cloud.web.admin.feign.service;

import com.duo.hello.spring.cloud.web.admin.feign.service.hystrix.AdminServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hello-spring-cloud-service-admin", fallback = AdminServiceHystrix.class)
public interface AdminService {

    @RequestMapping(value = "hi", method = RequestMethod.GET)
    public String sayHi(@RequestParam(value = "message") String message);
}
```

### 创建熔断器类并实现对应的 Feign 接口

```
package com.duo.hello.spring.cloud.web.admin.feign.service.hystrix;

import com.duo.hello.spring.cloud.web.admin.feign.service.AdminService;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceHystrix implements AdminService {

    @Override
    public String sayHi(String message) {
        return "Hi，your message is :\"" + message + "\" but request error.";
    }
}
```

### 测试熔断器

此时我们关闭服务提供者，再次请求 http://localhost:8765/hi?message=HelloFeign 浏览器会显示：

```
Hi，your message is :"HelloFeign" but request error.
```



