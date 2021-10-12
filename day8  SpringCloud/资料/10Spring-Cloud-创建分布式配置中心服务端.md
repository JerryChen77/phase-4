# 分布式配置中心

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在 Spring Cloud 中，有分布式配置中心组件 Spring Cloud Config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程 Git 仓库中。在 Spring Cloud Config 组件中，分两个角色，一是 Config Server，二是 Config Client。



# 分布式配置中心服务端

## 概述

创建一个工程名为 `hello-spring-cloud-config` 的项目，`pom.xml` 配置文件如下：

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

    <artifactId>hello-spring-cloud-config</artifactId>
    <packaging>jar</packaging>

    <name>hello-spring-cloud-config</name>
    <url>http://www.duo.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Begin -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot End -->

        <!-- Spring Cloud Begin -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
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
                    <mainClass>com.duo.hello.spring.cloud.config.ConfigApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

主要增加了 `spring-cloud-config-server` 依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

## Application

通过 `@EnableConfigServer` 注解，开启配置服务器功能

```
package com.duo.hello.spring.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
```

## application.yml

增加 Config 相关配置，并设置端口号为：`8888`

```
spring:
  application:
    name: hello-spring-cloud-config
  cloud:
    config:
      label: master
      server:
        git:
          uri: https://github.com/topsale/spring-cloud-config
          search-paths: respo
          username:
          password:

server:
  port: 8888

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

相关配置说明，如下：

- `spring.cloud.config.label`：配置仓库的分支
- `spring.cloud.config.server.git.uri`：配置 Git 仓库地址（GitHub、GitLab、码云 ...）
- `spring.cloud.config.server.git.search-paths`：配置仓库路径（存放配置文件的目录）
- `spring.cloud.config.server.git.username`：访问 Git 仓库的账号
- `spring.cloud.config.server.git.password`：访问 Git 仓库的密码

注意事项：

- 如果使用 GitLab 作为仓库的话，`git.uri` 需要在结尾加上 `.git`，GitHub 则不用

## 测试

浏览器端访问：http://localhost:8888/config-client/dev/master 显示如下：

```
<Environment> 
  <name>config-client</name>  
  <profiles> 
    <profiles>dev</profiles> 
  </profiles>  
  <label>master</label>  
  <version>9646007f931753d7e96a6dcc9ae34838897a91df</version>  
  <state/>  
  <propertySources> 
    <propertySources> 
      <name>https://github.com/topsale/spring-cloud-config/respo/config-client-dev.yml</name>  
      <source> 
        <foo>foo version 1</foo>  
        <demo.message>Hello Spring Config</demo.message> 
      </source> 
    </propertySources> 
  </propertySources> 
</Environment>
```

证明配置服务中心可以从远程程序获取配置信息

## 附：HTTP 请求地址和资源文件映射

- ![image-20210616095356509](img/image-20210616095356509.png)
- http://ip:port/{application}-{profile}.yml
- http://ip:port/{label}/{application}-{profile}.yml
- http://ip:port/{application}-{profile}.properties
- http://ip:port/{label}/{application}-{profile}.properties