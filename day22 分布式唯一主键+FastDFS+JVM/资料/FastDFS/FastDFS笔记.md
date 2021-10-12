# 一、FastDFS的应用场景

## 1.为什么需要分布式文件存储系统

![截屏2021-09-07 下午2.08.09](img/截屏2021-09-07 下午2.08.09.png)



## 2.阿里云OSS系统

很多中小公司为了节约成本，会使用在线的云文件存储系统（对象存储系统）。阿里云oss是一个很好的选择，怎么使用，参考下面的官方文档：

```url
https://help.aliyun.com/document_detail/84781.htm?spm=a2c4g.11186623.0.0.6fcc2cb7LMTDyG#concept-84781-zh
```

如果为了追求安全性，还是建议自己搭建一套分布式文件存储。

# 二、FastDFS的内部执行原理

![截屏2021-09-07 下午2.30.58](img/截屏2021-09-07 下午2.30.58.png)



# 三、搭建FastDFS服务器

- 准备一台虚拟机
- 准备教案里给的材料（部署文件夹）
- 根据文档流程逐一的执行



# 四、搭建FastDFS Java客户端实现文件上传

## 1.引入依赖

```xml
<dependency>
            <groupId>com.github.tobato</groupId>
            <artifactId>fastdfs-client</artifactId>
            <version>1.26.1-RELEASE</version>
        </dependency>
```



## 2.编写配置文件

```yml
server:
  port: 8001
fdfs:
  so-timeout: 1500
  connect-timeout: 600
  pool:
    jmx-enabled: false
  tracker-list: 172.16.253.48:22122
  thumb-image:
    height: 100
    width: 100
image:
  server: http://172.16.253.48:8888/
```

## 3.启动类上打上注解

```java
package com.qf.myfastdfsdemo;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)
@SpringBootApplication
public class MyFastDfsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFastDfsDemoApplication.class, args);
    }

}

```

## 4.编写上传程序

```java
package com.qf.myfastdfsdemo;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class MyFastDfsDemoApplicationTests {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${image.server}")
    private String imageServer;

    @Test
    public void testUploadImage() throws FileNotFoundException {

        String path = "/Users/zeleishi/Desktop/my-fast-dfs-demo/src/main/resources/images/tu1.jpeg";

        File img = new File(path);

        FileInputStream fileInputStream = new FileInputStream(img);

        //执行文件上传
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                fileInputStream,//输入流
                img.length(),//文件的大小
                getExtFileName(path),//扩展名
                null
        );
        //打印结果
        System.out.println(imageServer+storePath.getFullPath());
        System.out.println(storePath.getGroup());
        System.out.println(storePath.getPath());
    }

    private String getExtFileName(String path) {
        return path.substring(path.lastIndexOf('.')+1);
    }


}

```

