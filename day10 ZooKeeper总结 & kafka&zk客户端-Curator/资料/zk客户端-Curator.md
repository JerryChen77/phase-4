## 1.**什么是 Curator**

Curator 是一套由netflix 公司开源的，Java 语言编程的 ZooKeeper 客户端框架，Curator项目是现在ZooKeeper 客户端中使用最多，对ZooKeeper 版本支持最好的第三方客户端，并推荐使用，Curator 把我们平时常用的很多 ZooKeeper 服务开发功能做了封装，例如 Leader 选举、分布式计数器、分布式锁。这就减少了技术人员在使用 ZooKeeper 时的大部分底层细节开发工作。在会话重新连接、Watch 反复注册、多种异常处理等使用场景中，用原生的 ZooKeeper 处理比较复杂。而在使用 Curator 时，由于其对这些功能都做了高度的封装，使用起来更加简单，不但减少了开发时间，而且增强了程序的可靠性。

## 2.**Curator 实战** 

这里我们以 Maven 工程为例，首先要引入Curator 框架相关的开发包，这里为了方便测试引入了junit ，lombok，由于Zookeeper本身以来了 log4j 日志框架，所以这里可以创建对应的log4j配置文件后直接使用。 如下面的代码所示，我们通过将 Curator 相关的引用包配置到 Maven 工程的 pom 文件中，将 Curaotr 框架引用到工程项目里，在配置文件中分别引用了两个 Curator 相关的包，第一个是 curator-framework 包，该包是对 ZooKeeper 底层 API 的一些封装。另一个是 curator-recipes 包，该包封装了一些 ZooKeeper 服务的高级特性，如：Cache 事件监听、选举、分布式锁、分布式 Barrier。

```xml
  <dependencies>

    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
      <groupId>org.apache.zookeeper</groupId>
      <artifactId>zookeeper</artifactId>
      <version>3.4.14</version>
      <exclusions>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

    <dependency>
      <groupId>org.apache.curator</groupId>
      <artifactId>curator-framework</artifactId>
      <version>2.12.0</version>
      <exclusions>
        <exclusion>
          <groupId>org.apache.zookeeper</groupId>
          <artifactId>zookeeper</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.curator</groupId>
      <artifactId>curator-recipes</artifactId>
      <version>2.12.0</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>org.junit.vintage</groupId>
          <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
  </dependencies>

```

## 3.编写配置文件

要进行客户端服务器交互，第一步就要创建会话

```yml
curator.retryCount=5
curator.elapsedTimeMs=5000
curator.connectString=172.16.253.35:2181
curator.sessionTimeoutMs=60000
curator.connectionTimeoutMs=5000
```

connectionString：服务器地址列表，在指定服务器地址列表的时候可以是一个地址，也可以是多个地址。如果是多个地址，那么每个服务器地址列表用逗号分隔, 如  host1:port1,host2:port2,host3；port3 。 

retryPolicy：重试策略，当客户端异常退出或者与服务端失去连接的时候，可以通过设置客户端重新连接 ZooKeeper 服务端。而 Curator 提供了 一次重试、多次重试等不同种类的实现方式。在 Curator 内部，可以通过判断服务器返回的 keeperException 的状态代码来判断是否进行重试处理，如果返回的是 OK 表示一切操作都没有问题，而 SYSTEMERROR 表示系统或服务端错误。

| **策略名称**            | **描述**                             |
| ----------------------- | ------------------------------------ |
| ExponentialBackoffRetry | 重试一组次数，重试之间的睡眠时间增加 |
| RetryNTimes             | 重试最大次数                         |
| RetryOneTime            | 只重试一次                           |
| RetryUntilElapsed       | 在给定的时间结束之前重试             |

超时时间：Curator 客户端创建过程中，有两个超时时间的设置。一个是 sessionTimeoutMs 会话超时时间，用来设置该条会话在 ZooKeeper 服务端的失效时间。另一个是 connectionTimeoutMs 客户端创建会话的超时时间，用来限制客户端发起一个会话连接到接收 ZooKeeper 服务端应答的时间。sessionTimeoutMs 作用在服务端，而 connectionTimeoutMs 作用在客户端。



## 4.编写配置类

```java
package com.qf.bootzkclient.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "curator")
public class WrapperZK {

  private int retryCount;

  private int elapsedTimeMs;

  private String connectString;

  private int sessionTimeoutMs;

  private int connectionTimeoutMs;
}

```

## 5.编写配置bean

```java
package com.qf.bootzkclient.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfig {


    @Autowired
    WrapperZK wrapperZk;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
      return CuratorFrameworkFactory.newClient(
        wrapperZk.getConnectString(),
        wrapperZk.getSessionTimeoutMs(),
        wrapperZk.getConnectionTimeoutMs(),
        new RetryNTimes(wrapperZk.getRetryCount(), wrapperZk.getElapsedTimeMs()));
    }


}

```





## 5.**创建节点**

创建节点的方式如下面的代码所示，回顾我们之前课程中讲到的内容，描述一个节点要包括节点的类型，即临时节点还是持久节点、节点的数据信息、节点是否是有序节点等属性和性质。

```java
 @Test
public void testCreate() throws Exception {
    String path = curatorFramework.create().forPath("/curator-node");
    // curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/curator-node","some-data".getBytes())
    System.out.println(String.format("curator create node :%s  successfully.",path));
    System.in.read();
  
}
```

在 Curator 中，可以使用 create 函数创建数据节点，并通过 withMode 函数指定节点类型（持久化节点，临时节点，顺序节点，临时顺序节点，持久化顺序节点等），默认是持久化节点，之后调用 forPath 函数来指定节点的路径和数据信息。

**一次性创建带层级结构的节点**

```java
@Test
public void testCreateWithParent() throws Exception {
    String pathWithParent="/node-parent/sub-node-1";
    String path = curatorFramework.create().creatingParentsIfNeeded().forPath(pathWithParent);
    System.out.println(String.format("curator create node :%s  successfully.",path));
}
```

## 6.**获取数据**

```java
@Test
public void testGetData() throws Exception {
    byte[] bytes = curatorFramework.getData().forPath("/curator-node");
    System.out.println(new String(bytes));
}
```

## 7.**更新节点**

我们通过客户端实例的 setData() 方法更新 ZooKeeper 服务上的数据节点，在setData 方法的后边，通过 forPath 函数来指定更新的数据节点路径以及要更新的数据。

```java
@Test
public void testSetData() throws Exception {
    curatorFramework.setData().forPath("/curator-node","changed!".getBytes());
    byte[] bytes = curatorFramework.getData().forPath("/curator-node");
    System.out.println(new String(bytes));
}
```

## 8.**删除节点**

```java
@Test
public void testDelete() throws Exception {
    String pathWithParent="/node-parent";
    curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
}
```



guaranteed：该函数的功能如字面意思一样，主要起到一个保障删除成功的作用，其底层工作方式是：只要该客户端的会话有效，就会在后台持续发起删除请求，直到该数据节点在 ZooKeeper 服务端被删除。

deletingChildrenIfNeeded：指定了该函数后，系统在删除该数据节点的时候会以递归的方式直接删除其子节点，以及子节点的子节点。

## 9.**Curator 监听器**

```java
/**
 * Receives notifications about errors and background events
 */
public interface CuratorListener
{
    /**
     * Called when a background task has completed or a watch has triggered
     *
     * @param client client
     * @param event the event
     * @throws Exception any errors
     */
    public void         eventReceived(CuratorFramework client, CuratorEvent event) throws Exception;
}
```

针对 background 通知和错误通知。使用此监听器之后，调用inBackground 方法会异步获得监听

**Curator** **Caches:**

Curator 引入了 Cache 来实现对 Zookeeper 服务端事件监听，Cache 事件监听可以理解为一个本地缓存视图与远程 Zookeeper 视图的对比过程。Cache 提供了反复注册的功能。Cache 分为两类注册类型：节点监听和子节点监听。

**node cache:**

NodeCache 对某一个节点进行监听

```java
public NodeCache(CuratorFramework client,
                         String path)
Parameters:
client - the client
path - path to cache
```

可以通过注册监听器来实现，对当前节点数据变化的处理

```java
public void addListener(NodeCacheListener listener)
     Add a change listener
Parameters:
listener - the listener
```

```java
@Slf4j
public class NodeCacheTest extends AbstractCuratorTest{

    public static final String NODE_CACHE="/node-cache";

    @Test
    public void testNodeCacheTest() throws Exception {

        createIfNeed(NODE_CACHE);
        NodeCache nodeCache = new NodeCache(curatorFramework, NODE_CACHE);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("{} path nodeChanged: ",NODE_CACHE);
                printNodeData();
            }
        });

        nodeCache.start();
    }


    public void printNodeData() throws Exception {
        byte[] bytes = curatorFramework.getData().forPath(NODE_CACHE);
        log.info("data: {}",new String(bytes));
    }
}
```

**path cache:**  

PathChildrenCache 会对子节点进行监听，但是不会对二级子节点进行监听，

```java
public PathChildrenCache(CuratorFramework client,
                         String path,
                         boolean cacheData)
Parameters:
client - the client
path - path to watch
cacheData - if true, node contents are cached in addition to the stat
```



可以通过注册监听器来实现，对当前节点的子节点数据变化的处理

```java
public void addListener(PathChildrenCacheListener listener)
     Add a change listener
Parameters:
listener - the listener
```

```java
@Slf4j
public class PathCacheTest extends AbstractCuratorTest{

    public static final String PATH="/path-cache";

    @Test
    public void testPathCache() throws Exception {

        createIfNeed(PATH);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, PATH, true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                log.info("event:  {}",event);
            }
        });

        // 如果设置为true则在首次启动时就会缓存节点内容到Cache中
        pathChildrenCache.start(true);
    }
}
```



**tree cache:**

TreeCache 使用一个内部类TreeNode来维护这个一个树结构。并将这个树结构与ZK节点进行了映射。所以TreeCache 可以监听当前节点下所有节点的事件。

```java
public TreeCache(CuratorFramework client,
                         String path,
                         boolean cacheData)
Parameters:
client - the client
path - path to watch
cacheData - if true, node contents are cached in addition to the stat
```



可以通过注册监听器来实现，对当前节点的子节点，及递归子节点数据变化的处理

```java
public void addListener(TreeCacheListener listener)
     Add a change listener
Parameters:
listener - the listener
```

```java
@Slf4j
public class TreeCacheTest extends AbstractCuratorTest{

    public static final String TREE_CACHE="/tree-path";

    @Test
    public void testTreeCache() throws Exception {
        createIfNeed(TREE_CACHE);
        TreeCache treeCache = new TreeCache(curatorFramework, TREE_CACHE);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                log.info(" tree cache: {}",event);
            }
        });
        treeCache.start();
    }
}
```

