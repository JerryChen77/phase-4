

> Author ： Thor
>
> Version ：9.2.0



[TOC]



# 一、常见的认证机制

## 1.Http Basic Auth

> Http Basic Auth 是每次请求API时都提供用户的username和password，简言之， Basic Auth 是配合Restful API使用的最简单的认证方式，只需要提供用户名和密码即可。但由于有把用户名和密码暴露给第三方客户端的风险，在生产环境下被使用的越来越少。因此，在开发对外开放的Restful API时，应尽量避免采用 Http Basic Auth认证方式。



## 2.Cookie Auth

> Cookie认证机制是为一次请求认证在服务端创建一个Session对象，同时在客户端的浏览器端创建了一个Cookie（JSESSIONID）对象，通过客户端携带的Cookie对象与服务器端的Session对象匹配来实现状态管理。默认情况下，关闭浏览器时Cookie会被删除，可以通过设置Cookie的超时时间使Cookie在一定时间内有效。

![cookie认证](pic/cookie认证.jpg)



## 3.OAuth第三方授权

> OAuth（开放授权，Open Authorization）是一个开放的授权标准，为用户资源的授权提供了一个安全、开放而又简易的标准。与以往的授权方式不同之处是OAuth的授权不会使第三方触及到用户的账号信息（如用户名与密码），即第三方无需使用用户的用户名与密码就可以申请获得该用户资源的授权，因此OAuth是安全的。 12306-》用微信账号登陆， 登陆杂碎论坛-〉qq账号 

![oauth认证交互](pic/oauth认证交互.png)

> OAuth 在 "客户端" 与 "服务提供商" 之间，设置了一个授权层（authorization layer）。"客户端" 不能直接登录 "服务提供商"，只能登录授权层，以此将用户与客户端区分开来。"客户端" 登录授权层所用的令牌（token），与用户的密码不同。用户可以在登录的时候，指定授权层令牌的权限范围和有效期。"客户端" 登录授权层以后，"服务提供商" 根据令牌的权限范围和有效期，向 "客户端" 开放用户储存的资料。
>
> 这种基于OAuth的认证机制是用于个人消费类的互联网产品，如社交类、商超类App等应用，但不太适合拥有自有认证权限管理的企业应用



## 4. Token Auth

> 使用基于Token的身份验证方法，在服务端不需要存储用户的登陆信息。流程如下：
>
> - 客户端使用用户名和密码请求登陆。
> - 服务端收到请求，去验证用户名和密码。
> - 验证成功后，服务端会签发一个Token，再把这个Token发送给客户端。
> - 客户端收到Token以后可以把它存储在Cookie本地。
> - 客户端每次向服务端请求资源时需要携带Cookie中该Token。
> - 服务端收到请求后，验证客户端携带的Token，如果验证成功则返回数据。

![jwt认证](pic/jwt认证.jpg)

> Token认证方式比Http Basic Auth安全，比Cookie Auth更节约服务器资源，比OAuth更加轻量。Token Auth具体有以下优点：
>
> - 支持跨域访问：Cookie是不允许跨域访问的，这一点对Token机制是不存在的，前提是传输的用户认证信息通过Http头传输
> - 无状态（服务端可扩展行）：Token机制在服务端不需要存储session信息，因为Token自身包含了登陆用户的部分信息，只需要在客户端的cookie或本地介质存储状态信息。
> - 更适用CDN：可以通过内容分发网络请求服务端的所有资料（如js，html，图片等），而服务端只需要提供API即可。
> - 去耦合：不需要绑定到一个特定的身份验证方案。Token可以在任何地方生成，只要在API被调用时，生成Token即可。
> - 更适用于移动应用：当移动设备不支持Cookie验证时，采用Token验证即可。
> - CSRF：因为不再依赖于Cookie，就不需要考虑对CSRF（跨站请求伪造）的防范。
> - 性能：一次网络往返时间（通过数据库查询Session信息）总比做一次SHA256计算的Token验证和解析要费事的多。
> - 基于标准化：创建的API可以采用标准化的 **JSON Web Token（JWT）**。这个标准已经存在多个后端库（ .net，Ruby，Java，Python，Php ）和多家公司的支持（Firebase，Google，Microsoft）。



# 二、JWT简介

## 1.什么是JWT

> JSON Web Token （JWT）是一个开放的行业标准（RFC 7519），它定义了一种简洁的、自包含的协议格式，用于在通信双方传递json对象，传递的信息经过数字签名可以被验证和信任。JWT可以使用HMAC算法或使用RSA的公钥/私钥对进行签名，防止被篡改。
>
> JWT官网： https://jwt.io
>
> JWT令牌的优点：
>
> - JWT基于json，非常方便解析。
> - 可以在令牌中自定义丰富的内容，易扩展。
> - 通过非对称加密算法及数字签名技术，JWT防止篡改，安全性高。
> - 资源服务使用JWT可不依赖认证服务即完成授权。
>
> JWT令牌的缺点：
>
> - JWT令牌较长，占存储空间比较大。



## 2.JWT组成

> 一个JWT实际上就一个字符串，它由三部分组成，头部、负载与签名。



### 1）头部（Header）

> 头部用于描述关于该JWT的最基本信息，例如其类型（即JWT）以及签名所用的算法（如HMAC SHA256 或 RSA）等。这也可以被表示成一个JSON对象。

```json
{
  "alg":"HS256",
  "typ":"JWT"
}
```

> - alg：签名算法
> - typ：类型
>
> 我们对头部的json字符串进行BASE64编码，编码后的字符串如下：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

> Base64是一种基于64个可打印字符串来表示二进制数据的表示方式。JDK提供了非常方便的Base64Encoder和Base64Decoder，用它们可以非常方便的完成基于Base64的编码和解码。



### 2）负载（Payload）

> 负载，是存放有效信息的地方，比如用户的基本信息可以存在该部分中。负载包含三个部分：
>
> - 标准中注册的声明（建议但不强制使用）
>   - iss:jwt签发者
>   - sub：jwt所面向的用户
>   - aud：接收jwt的一方
>   - exp：jwt的过期时间，过期时间必须大于签发时间
>   - nbf：定义在什么时间之前，该jwt都是不可用的
>   - iat：jwt的签发时间
>   - jti：jwt的唯一身份标识，主要用来作为一次性token，从而回避重放攻击。
>
> - 公共的声明
>
> 公共的声明可以添加任何信息，一般添加用户的相关信息或其他业务需要的必要信息，但不建议添加敏感信息，因为该部分在客户端可解密。
>
> - 私有的声明
>
> 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。
>
> 私有声明也就是自定义claim，用于存放自定义键值对。

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "classname":"qfhzjava2102",
  "iat": 1516239022,
  "exp":
}
```

> 其中sub是标准的声明，name是自定义的私有声明，编码后如下：

```
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
```



### 3）签证、签名（Signature）

> jwt的第三部分是一个签证信息，由三部分组成：
>
> - Header（Base64编码后）
> - Payload（Base64编码后）
> - Secret（盐，必须保密）
>
> 这个部分需要Base64加密后的header和base4加密后的payload使用.连接组成的字符串，然后通过header重声明的加密方式进行加盐Secret组合加密，然后就构成了JWT的第三部分——使用“qfjava”作为盐：

```
eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

> 从官方工具中可以看到，三个部分组合出的完整字符串：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

![image-20210310163620788](pic/jwttoken.png)



> 注意：secret是保存在服务器端的，jwt在签发生成也是在服务器端的，secret就是用来进行jwt的签发和验证，所以，它就是服务器端的私钥，在任何场景都不应该泄漏。一旦客户端得知这个secret，那就意味着客户端是可以自我签发jwt了。



# 三、JJWT的使用



## 1.什么是JJWT

> JJWT是一个提供端到端的JWT创建和验证的开源Java库。也就是说使用JJWT能快速完成JWT的功能开发。



## 2.快速开始

**1）引入依赖**

> 创建Springboot工程并引入jjwt依赖，pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.qf</groupId>
    <artifactId>boot-jjwt-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>boot-jjwt-demo</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <!--jjwt-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

**2）创建Token**

```java
    @Test
    public void testCrtToken(){

        //创建JWT对象
        JwtBuilder builder = Jwts.builder().setId("1001")//设置负载内容
                .setSubject("小明")
                .setIssuedAt(new Date())//设置签发时间
                .signWith(SignatureAlgorithm.HS256, "qfjava");//设置签名秘钥
        //构建token
        String token = builder.compact();
        System.out.println(token);

    }
```



> JWT将用户信息转换成Token字符串，生成结果如下：

```
eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjE1MzY2MDEyfQ.2LNcw1v64TNQ96eCpWKvtAccBUA-cEVMDyJNMef-zu0
```



**3）解析Token**

> 通过JWT解析Token，获取Token中存放的用户信息，即生成Claims对象。

```java
    @Test
    public void testParseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjE1MzY2MDEyfQ.2LNcw1v64TNQ96eCpWKvtAccBUA-cEVMDyJNMef-zu0";
        //解析Token，生成Claims对象，Token中存放的用户信息解析到了claims对象中
        Claims claims = Jwts.parser().setSigningKey("qfjava").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("IssuedAt:" + claims.getIssuedAt());
    }
```

> 解析结果如下：

```
id:1001
subject:小明
IssuedAt:Wed Mar 10 16:46:52 CST 2021
```



**4）Token过期检验**

> 在有效期内Token可以正常读取，超过有效期则Token失效

```java
    @Test
    public void testExpToken(){
        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
                .setExpiration(new Date(exp));//设置超时
    }
```



**5）自定义claims**

> 除了使用官方api设置属性值，也可以添加自定义键值对。

```java
    @Test
    public void testCustomClaims(){
        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
                .setExpiration(new Date(exp))
                .claim("role", "admin");//设置自定义键值对
    }
```

> 使用下面语句获取属性值：

```java
claims.get("role")
```



# 四、Spring Security + JWT 实现后台管理系统的权限验证



## 1.后台管理系统权限模块介绍

> 后台管理系统的权限验证是企业中十分常见的技术场景。权限验证的作用简言之即不同的用户拥有不同的权限，相应的权限对应的系统能力也是不同的，比如管理员用户具有系统的所有权限：

![image-20210310170818387](pic/admin权限.png)



> 上图中拥有管理员角色的用户可以看到左边菜单栏的权限，而作为普通用户角色的用户，拥有的权限是不一样的：

![image-20210310171027255](pic/user权限.png)



> 同时，这些权限可以通过后台管理系统设置：

![image-20210310171241266](pic/权限设置.png)



## 2.数据库表设计

> 权限模块的核心表共有以下几个：用户表、角色表、权限表、用户角色表、角色权限表
>
> - 用户表： 存储用户数据
> - 角色表：存储角色数据
> - 权限表：存储权限数据
> - 用户角色表：用户和角色的关联表，哪个用户属于什么角色
> - 角色权限表：角色和权限的关联表，哪个角色拥有什么权限
>
> 比如：用户“小明”属于admin角色，admin角色拥有超级管理员的权限，因此小明就拥有超级管理员的权限。

![image-20210310171956831](pic/权限模块核心表.png)

> 数据库DDL如下：

```mysql


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parentId` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `css` varchar(30) DEFAULT NULL,
  `href` varchar(1000) DEFAULT NULL,
  `type` tinyint(1) NOT NULL,
  `permission` varchar(50) DEFAULT NULL,
  `sort` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES (1, 0, '用户管理', 'fa-users', '', 1, '', 1);
INSERT INTO `sys_permission` VALUES (2, 1, '用户查询', 'fa-user', 'pages/user/userList.html', 1, '', 2);
INSERT INTO `sys_permission` VALUES (3, 2, '查询', '', '', 2, 'sys:user:query', 100);
INSERT INTO `sys_permission` VALUES (4, 2, '新增', '', '', 2, 'sys:user:add', 100);
INSERT INTO `sys_permission` VALUES (6, 0, '修改密码', 'fa-pencil-square-o', 'pages/user/changePassword.html', 1, 'sys:user:password', 4);
INSERT INTO `sys_permission` VALUES (7, 0, '系统设置', 'fa-gears', '', 1, '', 5);
INSERT INTO `sys_permission` VALUES (8, 7, '菜单', 'fa-cog', 'pages/menu/menuList.html', 1, '', 6);
INSERT INTO `sys_permission` VALUES (9, 8, '查询', '', '', 2, 'sys:menu:query', 100);
INSERT INTO `sys_permission` VALUES (10, 8, '新增', '', '', 2, 'sys:menu:add', 100);
INSERT INTO `sys_permission` VALUES (11, 8, '删除', '', '', 2, 'sys:menu:del', 100);
INSERT INTO `sys_permission` VALUES (12, 7, '角色', 'fa-user-secret', 'pages/role/roleList.html', 1, '', 7);
INSERT INTO `sys_permission` VALUES (13, 12, '查询', '', '', 2, 'sys:role:query', 100);
INSERT INTO `sys_permission` VALUES (14, 12, '新增', '', '', 2, 'sys:role:add', 100);
INSERT INTO `sys_permission` VALUES (15, 12, '删除', '', '', 2, 'sys:role:del', 100);
INSERT INTO `sys_permission` VALUES (16, 0, '文件管理', 'fa-folder-open', 'pages/file/fileList.html', 1, '', 8);
INSERT INTO `sys_permission` VALUES (17, 16, '查询', '', '', 2, 'sys:file:query', 100);
INSERT INTO `sys_permission` VALUES (18, 16, '删除', '', '', 2, 'sys:file:del', 100);
INSERT INTO `sys_permission` VALUES (22, 0, '公告管理', 'fa-book', 'pages/notice/noticeList.html', 1, '', 12);
INSERT INTO `sys_permission` VALUES (23, 22, '查询', '', '', 2, 'notice:query', 100);
INSERT INTO `sys_permission` VALUES (24, 22, '添加', '', '', 2, 'notice:add', 100);
INSERT INTO `sys_permission` VALUES (25, 22, '删除', '', '', 2, 'notice:del', 100);
INSERT INTO `sys_permission` VALUES (26, 0, '日志查询', 'fa-reorder', 'pages/log/logList.html', 1, 'sys:log:query', 13);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `updateTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, 'ADMIN', '管理员', '2021-02-01 13:25:39', '2021-03-10 09:25:05');
INSERT INTO `sys_role` VALUES (2, 'USER', '普通用户', '2021-02-01 21:47:31', '2021-03-10 09:25:26');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `roleId` int NOT NULL,
  `permissionId` int NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permission` VALUES (1, 1);
INSERT INTO `sys_role_permission` VALUES (1, 2);
INSERT INTO `sys_role_permission` VALUES (1, 3);
INSERT INTO `sys_role_permission` VALUES (1, 4);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (1, 8);
INSERT INTO `sys_role_permission` VALUES (1, 9);
INSERT INTO `sys_role_permission` VALUES (1, 10);
INSERT INTO `sys_role_permission` VALUES (1, 11);
INSERT INTO `sys_role_permission` VALUES (1, 12);
INSERT INTO `sys_role_permission` VALUES (1, 13);
INSERT INTO `sys_role_permission` VALUES (1, 14);
INSERT INTO `sys_role_permission` VALUES (1, 15);
INSERT INTO `sys_role_permission` VALUES (1, 16);
INSERT INTO `sys_role_permission` VALUES (1, 17);
INSERT INTO `sys_role_permission` VALUES (1, 18);
INSERT INTO `sys_role_permission` VALUES (1, 22);
INSERT INTO `sys_role_permission` VALUES (1, 23);
INSERT INTO `sys_role_permission` VALUES (1, 24);
INSERT INTO `sys_role_permission` VALUES (1, 25);
INSERT INTO `sys_role_permission` VALUES (1, 26);
INSERT INTO `sys_role_permission` VALUES (2, 1);
INSERT INTO `sys_role_permission` VALUES (2, 2);
INSERT INTO `sys_role_permission` VALUES (2, 3);
INSERT INTO `sys_role_permission` VALUES (2, 4);
INSERT INTO `sys_role_permission` VALUES (2, 6);
INSERT INTO `sys_role_permission` VALUES (2, 7);
INSERT INTO `sys_role_permission` VALUES (2, 8);
INSERT INTO `sys_role_permission` VALUES (2, 9);
INSERT INTO `sys_role_permission` VALUES (2, 10);
INSERT INTO `sys_role_permission` VALUES (2, 11);
INSERT INTO `sys_role_permission` VALUES (2, 12);
INSERT INTO `sys_role_permission` VALUES (2, 13);
INSERT INTO `sys_role_permission` VALUES (2, 14);
INSERT INTO `sys_role_permission` VALUES (2, 15);
INSERT INTO `sys_role_permission` VALUES (2, 16);
INSERT INTO `sys_role_permission` VALUES (2, 17);
INSERT INTO `sys_role_permission` VALUES (2, 18);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `userId` int NOT NULL,
  `roleId` int NOT NULL,
  PRIMARY KEY (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_user` VALUES (1, 1);
INSERT INTO `sys_role_user` VALUES (2, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(60) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `headImgUrl` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `telephone` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `createTime` datetime NOT NULL,
  `updateTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$iYM/H7TrSaLs7XyIWQdGwe1xf4cdmt3nwMja6RT0wxG5YY1RjN0EK', '管理员', NULL, '', '', '', '1998-07-01', 0, 1, '2021-02-10 15:21:38', '2021-03-06 09:20:19');
INSERT INTO `sys_user` VALUES (2, 'user', '$2a$10$ooGb4wjT7Hg3zgU2RhZp6eVu3jvG29i/U4L6VRwiZZ4.DZ0OOEAHu', '用户', NULL, '', '', '', NULL, 1, 1, '2021-02-01 21:47:18', '2021-03-01 21:47:18');
COMMIT;


```



## 3.登陆逻辑

> 用户在登录时进行用户名和密码校验，若校验成功则进入下一步（用JWT创建Token，并将Token返回给用户）。

![登陆校验逻辑](pic/登陆校验逻辑.jpg)



> 关键代码如下：

```java
/**
 * spring security登陆处理
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;
	@Autowired
	private PermissionDao permissionDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = userService.getUser(username);
		if (sysUser == null) {
			throw new AuthenticationCredentialsNotFoundException("用户名不存在");
		} else if (sysUser.getStatus() == Status.LOCKED) {
			throw new LockedException("用户被锁定,请联系管理员");
		} else if (sysUser.getStatus() == Status.DISABLED) {
			throw new DisabledException("用户已作废");
		}

		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);

		List<Permission> permissions = permissionDao.listByUserId(sysUser.getId());
		loginUser.setPermissions(permissions);

		return loginUser;
	}

}

```

> UserDetailsService接口做登陆校验，通过loadUserByUsername()从数据库中校验用户名和密码。校验逻辑如上述代码所示，校验成功返回UserDetails对象，校验失败则抛出异常。



## 4.登陆成功后生成Token并存储

> 登陆成功后，将请求消息中的用户信息存入到redis中，并使用JWT生成Token返回给用户

![登陆校验逻辑-2](pic/登陆校验逻辑-2.jpg)

**1）生成Token**

```java
/**
	 * 登陆成功，返回Token
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				LoginUser loginUser = (LoginUser) authentication.getPrincipal();

				Token token = tokenService.saveToken(loginUser);//使用jwt生成Token
				ResponseUtil.responseJson(response, HttpStatus.OK.value(), token);
			}
		};
	}
```



**2）使用JWT生成Token**

```java
/**
 * token存到redis的实现类<br>
 * jwt实现的token
 *
 */
@Primary
@Service
public class TokenServiceJWTImpl implements TokenService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	/**
	 * token过期秒数
	 */
	@Value("${token.expire.seconds}")
	private Integer expireSeconds;
	@Autowired
	private RedisTemplate<String, LoginUser> redisTemplate;
	@Autowired
	private SysLogService logService;
	/**
	 * 私钥
	 */
	@Value("${token.jwtSecret}")
	private String jwtSecret;

	private static Key KEY = null;
	private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

	@Override
	public Token saveToken(LoginUser loginUser) {
		loginUser.setToken(UUID.randomUUID().toString());
		cacheLoginUser(loginUser);//将用户信息存入redis
		// 登陆日志
		logService.save(loginUser.getId(), "登陆", true, null);
		//使用jwt生成token
		String jwtToken = createJWTToken(loginUser);

		return new Token(jwtToken, loginUser.getLoginTime());
	}

	/**
	 * 生成jwt
	 * 
	 * @param loginUser
	 * @return
	 */
	private String createJWTToken(LoginUser loginUser) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户

		String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
				.compact();

		return jwtToken;
	}

	private void cacheLoginUser(LoginUser loginUser) {
		loginUser.setLoginTime(System.currentTimeMillis());
		loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
		// 根据uuid将loginUser缓存
		redisTemplate.boundValueOps(getTokenKey(loginUser.getToken())).set(loginUser, expireSeconds, TimeUnit.SECONDS);
	}

	/**
	 * 更新缓存的用户信息
	 */
	@Override
	public void refresh(LoginUser loginUser) {
		cacheLoginUser(loginUser);
	}

	@Override
	public LoginUser getLoginUser(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			return redisTemplate.boundValueOps(getTokenKey(uuid)).get();
		}

		return null;
	}

	
}
```



> 登陆成功后从请求头中获取用户数据，将用户数据以相应的键值对存入到redis中，并使用jwt生成token，并返回给客户端

```java
public class ResponseUtil {

	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);
      //token数据返回
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```



## 5.通过JWT从Token中获取用户数据

> 用户在完成登陆后，访问服务器的其他资源时，将会经过TokenFilter过滤器，该过滤器将会获取用户请求数据中的Token，并用JWT进行解析，获取用户数据。

![访问其他页面的jwt校验](pic/访问其他页面的jwt校验.jpg)



> 关键代码如下：

```java
/**
 * Token过滤器
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

	public static final String TOKEN_KEY = "token";

	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserDetailsService userDetailsService;
	private static final Long MINUTES_10 = 10 * 60 * 1000L;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(request);
		if (StringUtils.isNotBlank(token)) {
			//通过JWT解析请求数据中的Token，获取用户数据
			LoginUser loginUser = tokenService.getLoginUser(token);
			if (loginUser != null) {
				loginUser = checkLoginTime(loginUser);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser,
						null, loginUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 校验时间<br>
	 * 过期时间与当前时间对比，临近过期10分钟内的话，自动刷新缓存
	 * 
	 * @param loginUser
	 * @return
	 */
	private LoginUser checkLoginTime(LoginUser loginUser) {
		long expireTime = loginUser.getExpireTime();
		long currentTime = System.currentTimeMillis();
		if (expireTime - currentTime <= MINUTES_10) {
			String token = loginUser.getToken();
			loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginUser.getUsername());
			loginUser.setToken(token);
			tokenService.refresh(loginUser);
		}
		return loginUser;
	}

	/**
	 * 根据参数或者header获取token
	 * 
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request) {
		String token = request.getParameter(TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			token = request.getHeader(TOKEN_KEY);
		}

		return token;
	}

}
```



> JWT解析Token，返回用户数据

```java
  @Override
	public LoginUser getLoginUser(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			return redisTemplate.boundValueOps(getTokenKey(uuid)).get();
		}

		return null;
	}

private String getUUIDFromJWT(String jwtToken) {
		if ("null".equals(jwtToken) || StringUtils.isBlank(jwtToken)) {
			return null;
		}

		try {
			Map<String, Object> jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwtToken).getBody();
			return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
		} catch (ExpiredJwtException e) {
			log.error("{}已过期", jwtToken);
		} catch (Exception e) {
			log.error("{}", e);
		}

		return null;
	}
```



> 项目采用LayUI+Springboot+Spring Security+JWT+Redis+Mysql实现，项目运行方式请参考README.md

## 6.README

```
# 后台管理系统
#### 项目介绍
使用spring security进行权限控制，layui、bootstrap、jquery、html为前端。基于json进行交互，接口完全Restful风格设计。

#### 环境要求
1. jdk8、mysql、maven、redis

#### 初始化工作
1. 运行数据库脚本，在db/db_security.sql
2. 需要完成数据库和redis的配置

#### 测试账号
1. 管理员： 账号：admin 密码： admin
2. 普通用户： 账号： user 密码： user

#### 访问地址
http://localhost:8080/
```



身边有人要了解java，找磊哥，18667135181

?