# Spring Boot 整合 Solr

## 创建搜索服务接口

创建一个名为 `myshop-service-search-api` 项目，该项目只负责定义接口

### POM

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.duo</groupId>
        <artifactId>myshop-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../myshop-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>myshop-service-search-api</artifactId>
    <packaging>jar</packaging>

    <url>http://www.duo.com</url>
    <inceptionYear>2018-Now</inceptionYear>
</project>
```

### TbItemResult

为了实现全文检索的功能，我们需要将几张表的内容汇总到一个结果集中，此时需要使用多表联查将结果集汇总，SQL 语句如下：

```
SELECT
  a.id,
  a.cid AS tb_item_cid,
  b.name AS tb_item_cname,
  a.title AS tb_item_title,
  a.sell_point AS tb_item_sell_point,
  c.item_desc AS tb_item_desc
FROM
  tb_item AS a
  LEFT JOIN tb_item_cat AS b
    ON a.cid = b.id
  LEFT JOIN tb_item_desc AS c
    ON c.item_id = a.id
```

实体类如下：

```
package com.duo.myshop.service.search.domain;

import java.io.Serializable;

public class TbItemResult implements Serializable {
    private Long id;
    private Long tbItemCid;
    private String tbItemCname;
    private String tbItemTitle;
    private String tbItemSellPoint;
    private String tbItemDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTbItemCid() {
        return tbItemCid;
    }

    public void setTbItemCid(Long tbItemCid) {
        this.tbItemCid = tbItemCid;
    }

    public String getTbItemCname() {
        return tbItemCname;
    }

    public void setTbItemCname(String tbItemCname) {
        this.tbItemCname = tbItemCname;
    }

    public String getTbItemTitle() {
        return tbItemTitle;
    }

    public void setTbItemTitle(String tbItemTitle) {
        this.tbItemTitle = tbItemTitle;
    }

    public String getTbItemSellPoint() {
        return tbItemSellPoint;
    }

    public void setTbItemSellPoint(String tbItemSellPoint) {
        this.tbItemSellPoint = tbItemSellPoint;
    }

    public String getTbItemDesc() {
        return tbItemDesc;
    }

    public void setTbItemDesc(String tbItemDesc) {
        this.tbItemDesc = tbItemDesc;
    }
}
```

### SearchService

```
package com.duo.myshop.service.search.api;

import com.duo.myshop.service.search.domain.TbItemResult;

import java.util.List;

public interface SearchService {
    /**
     * 搜索商品
     * @param query 查询关键字
     * @param page 页码
     * @param rows 笔数
     * @return
     */
    List<TbItemResult> search(String query, int page, int rows);
}
```

## 创建搜索服务提供者

创建一个名为 `myshop-service-search-provider` 项目，该项目负责实现查询接口并初始化 Solr

### POM

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.duo</groupId>
        <artifactId>myshop-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../myshop-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>myshop-service-search-provider</artifactId>
    <packaging>jar</packaging>

    <url>http://www.duo.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Starter Settings -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-solr</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Commons Settings -->
        <dependency>
            <groupId>de.javakaffee</groupId>
            <artifactId>kryo-serializers</artifactId>
        </dependency>

        <!-- Projects Settings -->
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-commons-dubbo</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-commons-mapper</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-service-search-api</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.duo.myshop.service.search.provider.MyShopServiceSearchProviderApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

主要增加了 `org.springframework.boot:spring-boot-starter-data-solr` 依赖

### TbItemResultMapper

创建原生 Mapper ，主要作用是通过多表联查将数据汇总

```
package com.duo.myshop.service.search.provider.mapper;

import com.duo.myshop.service.search.domain.TbItemResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbItemResultMapper {
    List<TbItemResult> selectAll();
}
```

- `TbItemResultMapper.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.duo.myshop.service.search.provider.mapper.TbItemResultMapper">
    <resultMap id="BaseResultMap" type="com.duo.myshop.service.search.domain.TbItemResult">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="tb_item_cid" jdbcType="BIGINT" property="tbItemCid" />
        <result column="tb_item_cname" jdbcType="VARCHAR" property="tbItemCname" />
        <result column="tb_item_title" jdbcType="VARCHAR" property="tbItemTitle" />
        <result column="tb_item_sell_point" jdbcType="VARCHAR" property="tbItemSellPoint" />
        <result column="tb_item_desc" jdbcType="VARCHAR" property="tbItemDesc" />
    </resultMap>

    <select id="selectAll" resultMap="BaseResultMap">
        SELECT
          a.id,
          a.cid AS tb_item_cid,
          b.name AS tb_item_cname,
          a.title AS tb_item_title,
          a.sell_point AS tb_item_sell_point,
          c.item_desc AS tb_item_desc
        FROM
          tb_item AS a
          LEFT JOIN tb_item_cat AS b
            ON a.cid = b.id
          LEFT JOIN tb_item_desc AS c
            ON c.item_id = a.id
    </select>
</mapper>
```

### Application

这里使用 Spring 提供的 MyBatis 包扫面注解

```
package com.duo.myshop.service.search.provider;

import com.alibaba.dubbo.container.Main;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication(scanBasePackages = "com.duo.myshop")
@EnableTransactionManagement
@MapperScan(basePackages = "com.duo.myshop.service.search.provider.mapper")
public class MyShopServiceSearchProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceSearchProviderApplication.class, args);
        Main.main(args);
    }
}
```

### 测试并初始化 Solr

```
package com.duo.myshop.service.search.provider.test;

import com.duo.myshop.service.search.domain.TbItemResult;
import com.duo.myshop.service.search.provider.MyShopServiceSearchProviderApplication;
import com.duo.myshop.service.search.provider.mapper.TbItemResultMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShopServiceSearchProviderApplication.class)
public class SearchServiceTest {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private TbItemResultMapper tbItemResultMapper;

    /**
     * 初始化 Solr
     */
    @Test
    public void testInitSolr() {
        List<TbItemResult> tbItemResults = tbItemResultMapper.selectAll();

        SolrInputDocument document = null;
        for (TbItemResult tbItemResult : tbItemResults) {
            document = new SolrInputDocument();
            document.addField("id", tbItemResult.getId());
            document.addField("tb_item_cid", tbItemResult.getTbItemCid());
            document.addField("tb_item_cname", tbItemResult.getTbItemCname());
            document.addField("tb_item_title", tbItemResult.getTbItemTitle());
            document.addField("tb_item_sell_point", tbItemResult.getTbItemSellPoint());
            document.addField("tb_item_desc", tbItemResult.getTbItemDesc());

            try {
                solrClient.add(document);
                solrClient.commit();
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加索引库
     */
    @Test
    public void testAddDocument() {
        // 创建文档对象
        SolrInputDocument document = new SolrInputDocument();

        // 向文档中添加域
        document.addField("id", 536563);
        document.addField("tb_item_title", "new2 - 阿尔卡特 (OT-927) 炭黑 联通3G手机 双卡双待");

        // 将文档添加到索引库
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引库
     */
    @Test
    public void testDeleteDocument() {
        try {
            solrClient.deleteByQuery("*:*");
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询索引库
     */
    @Test
    public void testQueryDocument() {
        // 创建查询对象
        SolrQuery query = new SolrQuery();

        // 设置查询条件
        query.setQuery("手机");

        // 设置分页
        query.setStart(0);
        query.setRows(10);

        // 设置默认搜索域
        query.set("df", "tb_item_keywords");

        // 设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("tb_item_title");
        query.setHighlightSimplePre("<span style='color:red;'>");
        query.setHighlightSimplePost("</span>");

        try {
            // 执行查询操作
            QueryResponse response = solrClient.query(query);

            // 获取查询结果集
            SolrDocumentList results = response.getResults();

            // 获取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

            // 遍历结果集
            for (SolrDocument result : results) {
                String tbItemTitle = "";
                List<String> strings = highlighting.get(result.get("id")).get("tb_item_title");
                if (strings != null && strings.size() > 0) {
                    tbItemTitle = strings.get(0);
                } else {
                    tbItemTitle = (String) result.get("tb_item_title");
                }

                System.out.println(tbItemTitle);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### SearchServiceImpl

```
package com.duo.myshop.service.search.provider.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.duo.myshop.service.search.api.SearchService;
import com.duo.myshop.service.search.domain.TbItemResult;
import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service(version = "${services.versions.search.v1}")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<TbItemResult> search(String query, int page, int rows) {
        List<TbItemResult> searchResults = Lists.newArrayList();

        // 创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        // 设置查询条件
        solrQuery.setQuery(query);

        // 设置分页条件
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);

        // 设置默认搜索域
        solrQuery.set("df", "tb_item_keywords");

        // 设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("tb_item_title");
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        try {
            // 执行查询操作
            QueryResponse queryResponse = solrClient.query(solrQuery);
            SolrDocumentList solrDocuments = queryResponse.getResults();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            for (SolrDocument solrDocument : solrDocuments) {
                TbItemResult result = new TbItemResult();

                result.setId(Long.parseLong(String.valueOf(solrDocument.get("id"))));
                result.setTbItemCid(Long.parseLong(String.valueOf(solrDocument.get("tb_item_cid"))));
                result.setTbItemCname((String) solrDocument.get("tb_item_cname"));
                result.setTbItemTitle((String) solrDocument.get("tb_item_title"));
                result.setTbItemSellPoint((String) solrDocument.get("tb_item_sell_point"));
                result.setTbItemDesc((String) solrDocument.get("tb_item_desc"));

                String tbItemTitle = "";
                List<String> list = highlighting.get(solrDocument.get("id")).get("tb_item_title");
                if (list != null && list.size() > 0) {
                    tbItemTitle = list.get(0);
                } else {
                    tbItemTitle = (String) solrDocument.get("tb_item_title");
                }
                result.setTbItemTitle(tbItemTitle);

                searchResults.add(result);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResults;
    }
}
```

### application.yml

主要增加了 `spring.data.solr.host` 配置：

```
spring:
  data:
    solr:
      host: http://192.168.10.131:8983/solr/ik_core
```

完整配置如下：

```
# Spring boot application
spring:
  application:
    name: myshop-service-search-provider
  datasource:
    druid:
      url: jdbc:mysql://192.168.10.131:3306/myshop?useUnicode=true&characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
      initial-size: 1
      min-idle: 1
      max-active: 20
      test-on-borrow: true
      driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    solr:
      host: http://192.168.10.131:8983/solr/ik_core
server:
  port: 8504

# MyBatis Config properties
mybatis:
  type-aliases-package: com.duo.myshop.commons.domain
  mapper-locations: classpath:mapper/*.xml

# Services Versions
services:
  versions:
    search:
      v1: 1.0.0

# Dubbo Config properties
dubbo:
  ## Base packages to scan Dubbo Component：@com.alibaba.dubbo.config.annotation.Service
  scan:
    basePackages: com.duo.myshop.service.search.provider.api.impl
  ## ApplicationConfig Bean
  application:
    id: myshop-service-search-provider
    name: myshop-service-search-provider
    qos-port: 22225
    qos-enable: true
  ## ProtocolConfig Bean
  protocol:
    id: dubbo
    name: dubbo
    port: 20884
    status: server
    serialization: kryo
  ## RegistryConfig Bean
  registry:
    id: zookeeper
    address: zookeeper://192.168.10.131:2181?backup=192.168.10.131:2182,192.168.10.131:2183

# Enables Dubbo All Endpoints
management:
  endpoint:
    dubbo:
      enabled: true
    dubbo-shutdown:
      enabled: true
    dubbo-configs:
      enabled: true
    dubbo-services:
      enabled: true
    dubbo-references:
      enabled: true
    dubbo-properties:
      enabled: true
  # Dubbo Health
  health:
    dubbo:
      status:
        ## StatusChecker Name defaults (default : "memory", "load" )
        defaults: memory
        ## StatusChecker Name extras (default : empty )
        extras: load,threadpool


logging:
  level.com.duo.myshop.commons.mapper: DEBUG
```

## 创建搜索服务消费者

创建一个名为 `myshop-service-search-cosumer` 项目

### POM

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.duo</groupId>
        <artifactId>myshop-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../myshop-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>myshop-service-search-consumer</artifactId>
    <packaging>jar</packaging>

    <url>http://www.duo.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Starter Settings -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Commons Settings -->
        <dependency>
            <groupId>de.javakaffee</groupId>
            <artifactId>kryo-serializers</artifactId>
        </dependency>

        <!-- Projects Settings -->
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-commons-dubbo</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-static-backend</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
        <dependency>
            <groupId>com.duo</groupId>
            <artifactId>myshop-service-search-api</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.duo.myshop.service.search.consumer.MyShopServiceSearchConsumerApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

### Application

```
package com.duo.myshop.service.search.consumer;

import com.alibaba.dubbo.container.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication(scanBasePackages = "com.duo.myshop", exclude = DataSourceAutoConfiguration.class)
public class MyShopServiceSearchConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceSearchConsumerApplication.class, args);
        Main.main(args);
    }
}
```

### application.yml

```
# Spring boot application
spring:
  application:
    name: myshop-service-search-consumer
  thymeleaf:
    cache: false # 开发时关闭缓存,不然没法看到实时页面
    mode: LEGACYHTML5 # 用非严格的 HTML
    encoding: UTF-8
    servlet:
      content-type: text/html
server:
  port: 8603

# Services Versions
services:
  versions:
    search:
      v1: 1.0.0

# Dubbo Config properties
dubbo:
  scan:
    basePackages: com.duo.myshop.service.search.consumer
  ## ApplicationConfig Bean
  application:
    id: myshop-service-search-consumer
    name: myshop-service-search-consumer
  ## ProtocolConfig Bean
  protocol:
    id: dubbo
    name: dubbo
    port: 30883
    status: server
    serialization: kryo
  ## RegistryConfig Bean
  registry:
    id: zookeeper
    address: zookeeper://192.168.10.131:2181?backup=192.168.10.131:2182,192.168.10.131:2183

# Dubbo Endpoint (default status is disable)
endpoints:
  dubbo:
    enabled: true

management:
  server:
    port: 8703
  # Dubbo Health
  health:
    dubbo:
      status:
        ## StatusChecker Name defaults (default : "memory", "load" )
        defaults: memory
  # Enables Dubbo All Endpoints
  endpoint:
    dubbo:
      enabled: true
    dubbo-shutdown:
      enabled: true
    dubbo-configs:
      enabled: true
    dubbo-services:
      enabled: true
    dubbo-references:
      enabled: true
    dubbo-properties:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
```

### Controller

```
package com.duo.myshop.service.search.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duo.myshop.service.search.api.SearchService;
import com.duo.myshop.service.search.domain.TbItemResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Reference(version = "${services.versions.search.v1}")
    private SearchService searchService;

    @RequestMapping(value = "search/{query}/{page}/{size}", method = RequestMethod.GET)
    public List<TbItemResult> search(
            @PathVariable(required = true) String query,
            @PathVariable(required = true) int page,
            @PathVariable(required = true) int size) {
        List<TbItemResult> tbItemResults = searchService.search(query, page, size);
        return tbItemResults;
    }
}
```

### 测试效果

在浏览器访问：http://localhost:8603/search/手机/1/10
