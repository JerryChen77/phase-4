# 服务注册与发现

## 概述

在这里，我们需要用的组件是 Spring Cloud Netflix 的 Eureka，Eureka 是一个服务注册和发现模块

## 创建服务注册中心

其 `pom.xml` 文件配置如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.duo</groupId>
        <artifactId>hello-spring-cloud-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../hello-spring-cloud-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>hello-spring-cloud-eureka</artifactId>
    <packaging>jar</packaging>

    <name>hello-spring-cloud-eureka</name>
    <url>http://www.duo.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Begin -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot End -->

        <!-- Spring Cloud Begin -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <!-- Spring Cloud End -->
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.duo.hello.spring.cloud.eureka.EurekaApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Application

启动一个服务注册中心，只需要一个注解 `@EnableEurekaServer`

```
package com.duo.hello.spring.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

### application.yml

Eureka 是一个高可用的组件，它没有后端缓存，每一个实例注册之后需要向注册中心发送心跳（因此可以在内存中完成），在默认情况下 Erureka Server 也是一个 Eureka Client ,必须要指定一个 Server。

```
spring:
  application:
    name: hello-spring-cloud-eureka

server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

通过 `eureka.client.registerWithEureka:false` 和 `fetchRegistry:false` 来表明自己是一个 Eureka Server.

## 操作界面

Eureka Server 是有界面的，启动工程，打开浏览器访问： 

http://localhost:8761

![](pic\1.png)





# Eureka的相关注意事项

- Eureka Server 提供服务发现的能力，各个微服务启动时，会向Eureka Server注册自己的信息（例如IP、端口、微服务名称等），Eureka Server会存储这些信息；

- Eureka Client 是一个Java客户端，用于简化与Eureka Server的交互( 启动的时候，会向server注册自己的信息 )

- 微服务启动后 ，会周期性（ 默认30秒 ）地向Eureka Server发送心跳以续约自己的“租期”；
- 如果Eureka Server在一定时间内没有接收到某个微服务实例的心跳（最后一次续约时间开始计算），Eureka Server将会注销该实例（ 默认90秒 ）；

- 默认情况下， Eureka Server同时也是Eureka Client。多个Eureka Server实例，互相之间通过增量复制的方式，来实现服务注册表中数据的同步 。Eureka Server默认保证在90秒内，Eureka Server集群内的所有实例中的数据达到一致（从这个架构来看，Eureka Server所有实例所处的角色都是 对等 的，没有类似Zookeeper选举过程，也不存在主从， 所有的节点都是主节点 。Eureka官方将Eureka Server集群中的所有实例称为“ 对等体（peer））

- Eureka Client会缓存服务注册表 中的信息。这种方式有一定的优势——首先，微服务无需每次请求都查询Eureka Server，从而降低了Eureka Server的压力；其次，即使Eureka Server所有节点都宕掉，服务消费者依然可以使用缓存中的信息找到服务提供者并完成调用



# Eureka配置的总要信息讲解

## 服务注册配置

```
eureka.client.register-with-eureka=true
```

该项配置说明,是否向注册中心注册自己,在非集群环境下设置为false，表示自己不注册自己

```
eureka.client.fetch-registry=true
```

该项配置说明，注册中心只要维护微服务实例清单，非集群环境下,不需要作检索服务,所有也设置为false

## 服务续约配置

```
eureka.instance.lease-renewal-interval-in-seconds=30(默认)
```

该配置说明，服务提供者会维持一个心跳告诉eureka server 我还活着，这个就是一个心跳周期

```
eureka.instance.lease-expiration-duration-in-seconds=90(默认)
```

 该配置说明,你的最后一次续约时间开始，往后推90s 还没接受到你的心跳,那么我就需要把你剔除.

## 获取服务配置 (前提,eureka.client.fetch-registry为true)

```
eureka.client.registry-fetch-interval-seconds=30
```

缓存在调用方的微服务实例清单刷新时间



## eureka 的自我保护功能

默认情况下，若eureka server  在一段时间内（90s）没有接受到某个微服务实例的心跳，那么
eureka server 就会剔除该实例，当时由于网络分区故障，导致eureka server 和 服务之间无法通信，此时这个情况就变得很可怕---因为微服务是实例健康的,本不应注销该实例.
那么通过eureka server 自我保护模式就起作用了,当eureka server节点短时间(15min是否低于85%)丢失过多客户端是(由于网络分区)， 那么该eureka server节点就会进入自我保护模式，eureka server 就会自动保护注册表中的微服务实例，不再删除该注册表中微服务的信息，等到网络恢复，eureka server 节点就会退出自我保护功能(宁可放过一千，不要错杀一个) 

