# 一、常见的认证方式

## 1.Http Basic Auth

每次需要携带账号密码进行登录验证，这种方案比较不安全



## 2.Cookie Auth

通过Session+Cookie的方式，把登录信息保存在Session中，用cookie保存访问session的信息。

这种方案不适合分布式场景。

![截屏2021-09-09 下午2.17.37](img/截屏2021-09-09 下午2.17.37.png)

在分布式场景中，需要使用分布式session来解决这种方案

## 3.使用JWT做认证

![截屏2021-09-09 下午2.21.15](img/截屏2021-09-09 下午2.33.34.png)



## 4.第三方认证



# 二、JWT的介绍

## 1.jwt的效果

可以直接将内容编码成JWT字符串，内容丰富，易扩展。

收到JWT后可以直接进行解码，不需要经过繁琐的认证流程。

## 2.jwt的组成

1. 头部：描述基本信息：签名算法和文档类型

2. 负载

  - 公共的声明：每个jwt都有的
  - 私有的声明：存放一些自定义的信息

3. 签名：

  由 头部的base64编码+负载的base64编码+盐



# 三、JJWT的使用

## 1.什么是JJWT

JJWT（Java Json Web Token） 工具，实现jwt功能的java客户端工具

## 2.引入依赖

```xml
<!--jjwt-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>RELEASE</version>
        </dependency>
```



## 3.生成公共声明的token

```java
/**
     * 创建一个带有多个公共声明的jwt
     */
    @Test
    public void testCrtToken(){
        JwtBuilder jwtBuilder = Jwts.builder().setId("1001")
                .setSubject("qfjava")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava");//设置签名秘钥
        String token = jwtBuilder.compact();
        System.out.println(token);
    }

```



## 4.解析公共声明的token

```java
 /*
    解析
    eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoicWZqYXZhIiwiaWF0IjoxNjMxMTcwODU3fQ.6GZJW_zpNPK1NUlFvElzf3DKBDEt5HY8NCPjk0RPoi0
     */
    @Test
    public void testParserToken(){

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjMxMTcxMTg4LCJleHAiOjE2MzExNzEyNDh9.pPTCh4sYTmvM-wwbb7DYbPf2cul49VmcWr_cF_umwnc";
        Claims claims = Jwts.parser().setSigningKey("qfjava").parseClaimsJws(token).getBody();
        String id = claims.getId();
        String subject = claims.getSubject();
        Date issuedAt = claims.getIssuedAt();
        System.out.println("id:"+id);
        System.out.println("subject:"+subject);
        System.out.println("issuedAt:"+issuedAt);
    }
```





## 5.生成带超时时间的token

```java
/**
     * 设置带超时时间的token
     */
    @Test
    public void testExpToken(){
        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
                .setExpiration(new Date(exp));//设置超时
        System.out.println(builder.compact());
    }
```



## 6.生成并解析带自定义声明的token

```java
 /**
     * 生成带自定义声明的token
     */
    @Test
    public void testCrtCusToken(){

        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
//                .setExpiration(new Date(exp))//设置超时
                .claim("role","admin");
        System.out.println(builder.compact());
    }

    /**
     * 解析带自定义声明的token
     */
    @Test
    public void testParseCusToken(){

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjMxMTcxNDY1LCJyb2xlIjoiYWRtaW4ifQ.EmDVY_59TgM7GdE0XWYpIZ8NKM81tHZjyZSU-7G0N3U";

        Claims claims = Jwts.parser().setSigningKey("qfjava").parseClaimsJws(token).getBody();
        System.out.println(claims.get("role"));
    }
```



# 四、项目中怎么使用JWT

![Springsecurity+jwt项目流程](img/Springsecurity+jwt项目流程.jpg)

在此项目中JWT用来保存uuid，来去redis中查找数据。

还会在一些项目中，jwt作为访问后端接口的token。也就是说，如果要用postman访问后端接口，需要在请求头中加入：

```json
Authorization: Bearer XXX1.XXX2.XXX3
```

