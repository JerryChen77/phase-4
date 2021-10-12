# 一、Spring Cloud的应用背景

## 1.微服务碰到的几个重点问题

- 这么多服务如何注册与被发现
- 服务和服务之间如何进行通信
- 服务的治理怎么去实现

上面这几个问题，在微服务的应用场景中需要解决的，那谁来解决呢，怎么解决呢，解决有标准吗？——spring cloud就出现了



## 2.Spring Cloud的介绍

因为分布式微服务系统碰到了上面几个问题，springcloud就提供了解决这些问题的工具，因此springcloud就好比是一个工具箱

springcloud能够快速解决以下问题：

- 分布式/版本化配置
- 服务注册和发现
- 路由
- 服务到服务呼叫
- 负载均衡
- 断路器
- 全局锁
- 领导选举和集群状态
- 分布式消息传递

springcloud内目前我们重点关注两套解决方案

- spring cloud Netflix——非常经典，在早期的微服务里企业都是用这一套中间件

  - Eureka组件（工具）：解决服务的注册与发现

  - Ribbon：负载均衡器

  - zuul：服务网关（智能路由、服务限流、降级、熔断）

  - hystrix：断路器

  - spring cloud openfeign：服务之间的通信

  - spring cloud config：分布式配置中心

    

- spring cloud alibaba

  - nacos：服务注册与发现
  - nacos：分布式配置中心
  - dubbo rpc： 服务之间的通信
  - spring cloud stream： 消息系统
  - spring cloud bus：消息总线
  - seata：分布式事务解决方案



# 二、搭建Eureka注册中心

## 1.eureka作用

作为注册中心，负责服务的注册与发现。

## 2.搭建

- 引入依赖

```xml
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
```

- 编写配置文件

```yml
spring:
  application:
    name: hello-spring-cloud-eureka

server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    # 表示自己是一个注册中心，不是客户端
    registerWithEureka: false
    # 不进行集群注册，目前是一个单节点
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```



- 使用注解：启动类上使用注解@EnableEurekaServer

```java
package com.qf.my.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyEurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyEurekaApplication.class, args);
  }

}

```

- 浏览器中访问来查看注册中心的信息

```url
http://localhost:8761/
```



问题：eureka和zk作为注册中心在部署上有没有区别？



# 三、搭建服务提供者

## 1.引入依赖

```xml
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

```

## 2.编写配置文件

```yml
spring:
  application:
    name: worker-provider
server:
  port: 8762
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```

## 3.启动类上打上注解 @EnableEurekaClient

```java
package com.qf.my.worker.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MyWorkerProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyWorkerProviderApplication.class, args);
  }

}

```

## 4.编写具体的服务接口

```java
package com.qf.my.worker.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
public class WorkerController {

  @RequestMapping("/get/{id}")
  public String getWorker(@PathVariable Long id){
    return "worker:"+id;
  }

}

```



# 四、创建服务消费者-ribbon

## 0.ribbon的介绍

ribbon是一个spring cloud netflix提供的，实现http通信的负载均衡客户端。

可选的http的通信工具：

- URLConnection：原声的
- OKHttp：对移动端支持较好的
- RestTemplate：在Spring Boot（Spring Cloud）项目中对URLConnection的封装——选择它！



## 1.引入依赖

```xml
		<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
```

## 2.编写配置文件

```yml
spring:
  application:
    name: worker-consumer
server:
  port: 8763

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

```



## 3.启动类上打上注解

```java
package com.qf.my.worker.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyWorkerConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyWorkerConsumerApplication.class, args);
  }

}

```



## 4.编写配置类注入RestTemplate

```java
package com.qf.my.worker.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

  /**
   * 注入一个RestTemplate的bean
   * @return
   */
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate(){

    return new RestTemplate();

  }

}

```

其中这个@LoadBalanced注解非常重要：

让消费者去注册中心订阅具体的服务，如果没有这个注解。那么消费者不会去注册中心订阅，而直接发送http请求。





## 5.消费者调用服务提供者

```java
package com.qf.my.worker.consumer.service.impl;

import com.qf.my.worker.consumer.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class WorkerServiceImpl implements WorkerService {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String getWorker(Long id) {
    // 设置服务提供者的服务名称，然后进行http的调用
    String url = "http://WORKER-PROVIDER/worker/get/"+id;//http://localhost:8762/worker/get/1001
    String result = restTemplate.getForObject(url, String.class);

    return result;
  }
}

```

消费者调用服务提供者的关键，是使用restTemplate，设置服务提供者的服务名称，然后进行http的调用。

注意，这个url不是服务提供者的具体的ip地址，而是服务的名称，为什么呢？

因为消费者只知道自己需要哪个服务，至于该服务具体的ip地址是由注册中心提供的，因为在编写代码的时候只需要写明服务名即可。在运行时，注册中心会把当前可用的服务的地址列表交给服务消费者。



## 6.各角色之间的配置细节

| 角色       | 依赖          | 注解                   |
| ---------- | ------------- | ---------------------- |
| 注册中心   | Eureka-server | @EnableEurekaServer    |
| 服务提供者 | Eureka-client | @EnableEurekaClient    |
| 服务消费者 | Eureka-client | @EnableDiscoveryClient |



# 五、Eureka的细节

## 1.eureka集群同步

eureka为了避免单点故障，通常需要部署集群，集群之间会进行数据的同步，集群部署时。需要把这个参数设置成true

```yml
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: true #集群部署需要的
    fetchRegistry: false
    serviceUrl: # 存放集群的3个节点地址
      defaultZone: http://localhost:8761/eureka/,http://localhost:7761/eureka/,http://localhost:6761/eureka/
```

## 2.服务的注册

服务提供者进行注册的时候，会通过http请求把自己的信息（地址、端口、服务名、健康状态等）发送给eureka注册中心



## 3. 服务续约

服务提供者会每隔30秒（默认），向eureka服务器发送一次心跳，表示自己还活着，防止被剔除。



## 4.服务剔除

eureka服务器内部有一个定时任务，每隔60秒执行一次。

执行的时候去看有没有服务超过90秒没有续约，会把这些服务进行强制删除——服务剔除。



## 5.服务下线

当服务关闭或重启时，会主动通知eureka服务器，自己要下线了，eureka服务器进行数据同步（广播），接下来消费者就调用不到该服务了。



## 6.获取服务

服务消费者会在启动的时候从eureka注册中心获取订阅的服务提供者的地址列表，缓存在本地（默认保留30秒时间）。

同样。eureka服务器也会缓存30秒 服务提供者的地址列表。都是为了提高性能。

注意。服务提供者也是能够获取其他服务提供者的信息并进行调用的。



## 7.自我保护机制

eureka有一个自我保护机制：防止因为网络短时间内的故障造成eureka误删了服务。

阈值：15分钟内在线的服务低于总数的85%，就会触发自我保护机制。



# 六、Ribbon的执行原理

## 1.场景

在上面的介绍中，我们发现ribbon可以帮助我们去注册中心获得服务地址列表进行通信。那么这个过程具体是怎么实现的？



## 2.ribbon的负载均衡策略

ribbon在进行通信之前，会缓存服务提供者的地址列表到本地，那么如何选择哪一台服务提供者进行通信。这就跟ribbon使用的负载均衡策略有关。

<img src="img/IRule的结构图-9169992.png" alt="IRule的结构图" style="zoom:50%;" />



重点关注这几个：

- random： 随机
- roundrobin：轮询（默认）
- retry：如果这一次访问失败，会进行重试，访问集群中的另一台
- weightedresponsetime：根据响应时间为权重来选择访问哪一台，访问响应时间越短，获得的访问机会越大



## 3.ribbon的负载均衡具体是如何实现的

![截屏2021-08-17 上午11.39.14](img/截屏2021-08-17 上午11.39.14.png)

整个执行的过程：

- ribbon组件先将服务地址列表缓存进来
- 使用LoadBalancerInterceptor拦截器去获取请求的服务的名称（WORKER-PROVIDER）
- 根据服务名称获得地址列表中的多个ip地址
- 通过负载均衡器LoadBalancer，在多个ip地址选择一个ip地址，作为这一次请求的目标服务器
- 通过http的方式发送请求。

这个流程也解答了为什么restTemplate上面要打上LoadBalanced注解



# 七、RestTemplate详解

## 1.HTTP协议组成部分

- 请求
  - 请求头：压缩格式、请求时间、cookie等等信息
  - 请求行：url、请求方式
  - 请求体：请求携带的参数
- 响应
  - 响应头：压缩格式、响应的时间、内容的格式
  - 响应行：响应的状态码、状态值、状态说明
  - 响应体：具体要响应的内容

## 2.RestTemplate的Get请求

### 1) getForObject

有三种重载的方法：

-  设计好下游(服务提供者)：

````
@RequestMapping("/getForObject")
  public String getForObject(Long id,String name){
    return String.format("id:%d,name:%S",id,name);
  }
````

- 上游服务消费者重载的方法一：

```java
 //****getForObject(String url, Class<T> responseType)*****
  String url = "http://WORKER-PROVIDER/worker/getForObject?id="+id+"&name="+name;
  String result = restTemplate.getForObject(url, String.class);
```

- 上游服务消费者重载的方法二：

```java
//********getForObject(String url, Class<T> responseType, Object... uriVariables)***********
 String url = "http://WORKER-PROVIDER/worker/getForObject?id={1}&name={2}";
 String result = restTemplate.getForObject(url, String.class, new Object[]{id, name});
```

- 上游服务消费者重载的方法三：

```java
//********getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)***********
    String url = "http://WORKER-PROVIDER/worker/getForObject?id={id}&name={name}";
    Map<String,Object> map = new HashMap<>();
    map.put("id",id);
    map.put("name",name);
    String result = restTemplate.getForObject(url, String.class, map);
    return result;
```



### 2)getForEntity

getForEntity实际上和getForObject执行的过程是一样的，就是返回的内容不一样。

- getForEntity返回的是整个响应消息

- getForObject是获得返回的响应消息中的响应消息体中内容



## 3.RestTemplate的Post请求

### 1）普通的方式

- 下游(服务提供者)：

```java
@PostMapping("/postForObject")
  public String postForObject(@RequestBody Worker worker){
    return String.format("id:%d,name:%s",worker.getId(),worker.getName());
  }
```

- 上游（服务消费者）

```java
//方式一：postForEntity(String url, Object request, Class<T> responseType)
String url = "http://WORKER-PROVIDER/worker/postForObject";
ResponseEntity<String> entity = restTemplate.postForEntity(url, worker, String.class);
return entity.getBody();
```



### 2) 封装请求消息体和消息头方式一

- 下游

```java
@PostMapping("/postForObject1")
  public String postForObject1(Long id,String name){
    return String.format("id:%d,name:%s",id,name);
  }
```

- 上游

```java
 //方式二：postForObject(String url, Object request, Class<T> responseType)

    MultiValueMap<String, Object> parammap = new LinkedMultiValueMap<>();
    parammap.add("id",worker.getId());
    parammap.add("name",worker.getName());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    //请求消息entity中封装了请求消息体（ID，name），和请求头（封装了消息的类型是json格式）
    HttpEntity entity  =new HttpEntity(parammap,headers);

    String url = "http://WORKER-PROVIDER/worker/postForObject1";
    ResponseEntity<String> entity1 = restTemplate.postForEntity(url, entity, String.class);
    return entity1.getBody();
```



### 3）封装请求消息体和消息头方式二:携带cookie

- 下游：

```java
  @PostMapping("/postForObjectWithCookie")
  public String postForObjectWithCookie(@RequestBody Worker worker,@CookieValue(name = "login_token",required = false) String loginToken){
    return String.format("id:%d,name:%s,cookie:%s",worker.getId(),worker.getName(),loginToken);
  }
```

- 上游

```java
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
```



# 八、OpenFeign工具的使用

feign内部集成了ribbon，实现了面向接口的通信方式。简化了ribbon的http的请求。

## 1.feign的实现

- 引入依赖

```xml
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```

- 编写配置文件

```yml
spring:
  application:
    name: worker-consumer-feign
server:
  port: 8764
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

- 启动类上打上注解

```java
package com.qf.my.worker.consumer.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MyWorkerConsumerFeignApplication {
  public static void main(String[] args) {
    SpringApplication.run(MyWorkerConsumerFeignApplication.class, args);
  }
}
```

- 创建feign API层

创建接口，在接口中复制下游提供的web接口（里面有一些细节需要完善），注意接口上打上注解，指明服务名

```java
package com.qf.my.worker.consumer.feign.api;

import com.qf.common.entity.Worker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("WORKER-PROVIDER")  //指明服务名
public interface WorkerFeignAPI {

  @RequestMapping("/worker/get/{id}")
  public String getWorker(@PathVariable(name = "id") Long id); //@PathVariable

//  @RequestMapping("/getForObject")
//  public String getForObject(Long id,String name);

  @PostMapping("/worker/postForObject")
  public String postForObject(@RequestBody Worker worker);

//  @PostMapping("/postForObject1")
//  public String postForObject1(Long id,String name);


}

```

- Service实现层 :

```java
@Service
public class WorkerServiceImpl implements WorkerService {

  @Autowired
  private WorkerFeignAPI feignAPI;

  @Override
  public String getWorker(Long id) {
    //调用服务提供者的接口，ribbon是使用restTemplate来实现这个调用，但是feign不一样
    String result = feignAPI.getWorker(id);
    return result;
  }
}

```



细节：

- 方法的参数中一定要有注解，@RequestBody、@PathVariable（name=“一定要指明”） @RequestParam(name=“一定要指明”)
- 方法的参数中最多只能有一个参数。如果多个参数，请封装到一个dto（data transport object实体）进行传输。

![截屏2021-08-17 下午4.22.32](img/截屏2021-08-17 下午4.22.32.png)



# 作业

- 掌握eureka的细节，记背
- 掌握ribbon的底层原理，记背
- 掌握restTemplate的多种请求方式，动手实践
- 尝试curd，worker表，springboot整合mybatis。
  - 创建服务提供者，对worker数据进行增删改查
  - 创建服务消费者（ribbon，进行cr；用feign进行ud），调用服务提供者