## 一、zk节点说明

### **常规配置文件说明：**

```
# zookeeper时间配置中的基本单位 (毫秒)
tickTime=2000
# 允许follower初始化连接到leader最大时长，它表示tickTime时间倍数 即:initLimit*tickTime
initLimit=10
# 允许follower与leader数据同步最大时长,它表示tickTime时间倍数 
syncLimit=5
#zookeper 数据存储目录
dataDir=/tmp/zookeeper
#对客户端提供的端口号
clientPort=2181
#单个客户端与zookeeper最大并发连接数
maxClientCnxns=60
# 保存的数据快照数量，之外的将会被清除
autopurge.snapRetainCount=3
#自动触发清除任务时间间隔，小时为单位。默认为0，表示不自动清除。
autopurge.purgeInterval=1
```
### **客户端命令：**

**node数据的增删改查**

```
# 列出子节点 
ls /
#创建节点
create /mm "mmn"
# 查看节点
get /mm
# 创建子节点 
create /mm/nn "man"
# 删除节点
delete /mm/nn
# 删除所有节点 包括子节点
deleteall /mm
```

### 节点类型
| 类型   | 描述   |
|:----|:----|
| PERSISTENT   | 持久节点   |
| PERSISTENT_SEQUENTIAL   | 持久序号节点   |
| EPHEMERAL   | 临时节点(不可在拥有子节点)   |
| EPHEMERAL_SEQUENTIAL   | 临时序号节点(不可在拥有子节点)   |

1. PERSISTENT（持久节点）

持久化保存的节点，也是默认创建的
```
#默认创建的就是持久节点
create /test
```

1. PERSISTENT_SEQUENTIAL(持久序号节点)

创建时zookeeper 会在路径上加上序号作为后缀，。非常适合用于分布式锁、分布式选举等场景。创建时添加 -s 参数即可。
```
#创建序号节点
create -s /test
#返回创建的实际路径
Created /test0000000001
create -s /test
#返回创建的实际路径2
Created /test0000000002
```

1. EPHEMERAL（临时节点）

临时节点会在客户端会话断开后自动删除。适用于心跳，服务发现等场景。创建时添加参数-e 即可。
```
#创建临时节点， 断开会话 在连接将会自动删除
create -e /temp
```

1. EPHEMERAL_SEQUENTIAL（临时序号节点）

与持久序号节点类似，不同之处在于EPHEMERAL_SEQUENTIAL是临时的会在会话断开后删除。创建时添加 -e -s 
```
create -e -s /temp/seq
```

### **节点属性**
```
# 查看节点属性
stat /mm
```

其属性说明如下表：
```
#创建节点的事物ID
cZxid = 0x385
#创建时间
ctime = Tue Sep 24 17:26:28 CST 2019
#修改节点的事物ID
mZxid = 0x385
#最后修改时间
mtime = Tue Sep 24 17:26:28 CST 2019
# 子节点变更的事物ID
pZxid = 0x385
#这表示对此znode的子节点进行的更改次数（不包括子节点）
cversion = 0
# 数据版本，变更次数
dataVersion = 0
#权限版本，变更次数
aclVersion = 0
#临时节点所属会话ID
ephemeralOwner = 0x0
#数据长度
dataLength = 17
#子节点数(不包括子子节点)
numChildren = 0
```

### 节点的监听：
客户添加 -w 参数可实时监听节点与子节点的变化，并且实时收到通知。非常适用保障分布式情况下的数据一至性。其使用方式如下：
| 命令   | 描述   |
|:----|:----|
| ls -w path     | 监听子节点的变化（增，删）   |
| get -w path   | 监听节点数据的变化   |
| stat -w path   | 监听节点属性的变化   |
| printwatches on\|off   | 触发监听后，是否打印监听事件(默认on)   |



## 二、分布式锁

---

### **锁的基本概念：**

开发中锁的概念并不陌生，通过锁可以实现在多个线程或多个进程间在争抢资源时，能够合理的分配置资源的所有权。在单体应用中我们可以通过 synchronized 或ReentrantLock 来实现锁。但在分布式系统中，仅仅是加synchronized 是不够的，需要借助第三组件来实现。比如一些简单的做法是使用 关系型数据行级锁来实现不同进程之间的互斥，但大型分布式系统的性能瓶颈往往集中在数据库操作上。为了提高性能得采用如Redis、Zookeeper之内的组件实现分布式锁。

**共享锁：**也称作只读锁，当一方获得共享锁之后，其它方也可以获得共享锁。但其只允许读取。在共享锁全部释放之前，其它方不能获得写锁。
**排它锁：**也称作读写锁，获得排它锁后，可以进行数据的读写。在其释放之前，其它方不能获得任何锁。

### 锁的获取：

某银行帐户，可以同时进行帐户信息的读取，但读取其间不能修改帐户数据。其帐户ID为:888

* 获得读锁流程：

![图片](pic\BFpx2XQYWf8ruFUt.png!thumbnail)
1、基于资源ID创建临时序号读锁节点 
   /lock/888.R0000000002 Read 
2、获取 /lock 下所有子节点，判断其最小的节点是否为读锁，如果是则获锁成功
3、最小节点不是读锁，则阻塞等待。添加lock/ 子节点变更监听。
4、当节点变更监听触发，执行第2步

**数据结构：**
![图片](pic\hgOxo7b5SPIdcXS1.png!thumbnail)

* 获得写锁：

1、基于资源ID创建临时序号写锁节点 
   /lock/888.R0000000002 Write 
2、获取 /lock 下所有子节点，判断其最小的节点是否为自己，如果是则获锁成功
3、最小节点不是自己，则阻塞等待。添加lock/ 子节点变更监听。
4、当节点变更监听触发，执行第2步

* 释放锁：

读取完毕后，手动删除临时节点，如果获锁期间宕机，则会在会话失效后自动删除。

### **关于羊群效应：**

在等待锁获得期间，所有等待节点都在监听 Lock节点，一但lock 节点变更所有等待节点都会被触发，然后在同时反查Lock 子节点。如果等待对例过大会使用Zookeeper承受非常大的流量压力。

  ![图片](pic\VsMAGsJSxhAOKnia.png!thumbnail)

为了改善这种情况，可以采用监听链表的方式，每个等待对列只监听前一个节点，如果前一个节点释放锁的时候，才会被触发通知。这样就形成了一个监听链表。
 ![图片](pic\JgVYw2L6xJcny6CN.png!thumbnail)



