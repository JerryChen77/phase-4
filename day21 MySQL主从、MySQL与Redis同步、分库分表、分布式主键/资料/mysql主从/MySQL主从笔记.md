# 一、MYSQL主从的应用背景

为什么学这一块知识

![截屏2021-09-06 上午9.26.11](img/截屏2021-09-06 上午9.26.11.png)

# 二、MySQL主从同步的实现

## 1.MySQL主从同步原理

![截屏2021-09-06 上午9.37.55](img/截屏2021-09-06 上午9.37.55.png)

- 从节点申请与主节点同步
- 主节点把binlog文件发送给从节点
- 从节点拿到文件后写入到本地的relaylog（中继日志）文件中
- 从节点开启另一条子线程，负责把relaylog文件中的内容写入到数据库中



## 2.MySQL主从同步的应用场景

<img src="img/mysql和redis主从同步.jpg" alt="mysql和redis主从同步" style="zoom:30%;" />

## 3.搭建MySQL主服务器

### 1）通过docker-compose进行搭建

```yml
version: '3.1'
services:
  mysql:
    restart: always
    image: mysql:5.7.25
    container_name: mysql
    ports:
      - 3306:3306
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: 123456
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --max_allowed_packet=128M
      --server-id=47
      --log_bin=master-bin
      --log_bin-index=master-bin.index
      --skip-name-resolve
      --sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

里面加入了这几个关键配置

```yml
      --server-id=47 # 当前节点的id，在mysql集群中需要id唯一
      --log_bin=master-bin # 开启binlog
      --log_bin-index=master-bin.index # binlog文件的名称
      --skip-name-resolve # 跳过权限检查
```

### 2）通过命令查看主节点的信息

```sql
show master status;
```

![截屏2021-09-06 上午9.55.35](img/截屏2021-09-06 上午9.55.35.png)

看到两个关键的信息：

- binlog文件的名称
- 当前进行同步数据的偏移量



## 4.搭建MySQL从服务器

### 1）通过docker-compose进行搭建

```yml
version: '3.1'
services:
  mysql:
    restart: always
    image: mysql:5.7.25
    container_name: mysql
    ports:
      - 3306:3306
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: 123456
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --max_allowed_packet=128M
      --server-id=48
      --relay-log-index=slave-relay-bin.index
      --relay-log=slave-relay-bin
      --log-bin=mysql-bin
      --log-slave-updates=1
      --sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:


```

加入了如下关键的信息

```yml
			--server-id=48 # 
      --relay-log-index=slave-relay-bin.index #开启relay日志
      --relay-log=slave-relay-bin # 开启relay日志
      --log-bin=mysql-bin # 开启binlog
      --log-slave-updates=1 # 主从同步的数量 目前从节点是1个
```

### 2)进入从节点中的容器中并进入到mysql中

```sql
docker exec -it 容器id bash;
mysql -uroot -p
```

### 3）设置同步的主节点的信息

```sql
#设置同步主节点：
CHANGE MASTER TO
MASTER_HOST='172.16.253.41',
MASTER_PORT=3306,
MASTER_USER='root',
MASTER_PASSWORD='123456',
MASTER_LOG_FILE='master-bin.000003', 
MASTER_LOG_POS=154;
```

关键是设置binlog文件的名称和同步的偏移量，从之前的主节点的信息中查看得知（2-3-2章节的内容）

### 4）在从节点中开启主从同步

```sql
start slave;
```

###  5）在从节点中查看主从同步的信息

```sql
show slave status \G;
```

<img src="img/截屏2021-09-06 上午10.13.37.png" alt="截屏2021-09-06 上午10.13.37" style="zoom:50%;" />

关键是两个yes

### 6）测试主从同步的效果

- 主节点添加数据
- 从节点同步到数据



### 7）关于读写分离的说明

问：从节点能添加数据吗？

可以，但是违背了我们的主负责写，从负责读的逻辑。因为主从同步是单向的，从写的数据不能同步给主。

如果从节点一定要实现只读，那么有两种方案：

- `set` `global read_only=1;` 这种方案只能约束普通用户只读，但是约束不了root用户
- `flush tables with read lock;` 这种方案可以约束root用户，但是不能主从同步。

这两种方案都不会去选择，如果一定要实现读写分离，可以通过中间件shardingshpere来实现。



# 三、MySQL和Redis之间的数据同步策略

## 1.为什么要研究MySQL和Redis之间的数据同步策略

<img src="img/截屏2021-09-06 上午10.56.32.png" alt="截屏2021-09-06 上午10.56.32" style="zoom:30%;" />

当MySQL中的数据更新了，但是redis还没及时的更新，那么就该采用一种策略：

下面来介绍这种策略



## 2.多种策略的比较

- 先更新数据库，后更新缓存

这种情况会造成脏读

<img src="img/截屏2021-09-06 上午11.13.31.png" alt="截屏2021-09-06 上午11.13.31" style="zoom:40%;" />

- 先删除缓存，后更新数据库

在并发严重的情况下，也会出现脏读

![截屏2021-09-06 上午11.14.45](img/截屏2021-09-06 上午11.14.45.png)

- ==先更新数据库，后删除缓存（采用的方案）==

![截屏2021-09-06 上午11.15.19](img/截屏2021-09-06 上午11.15.19.png)

这种方案在如上图情况下会出现脏读，但是上图的情况，读的操作比写的慢，是比较少出现的，因此这种方案是普通采用的方案，如果真的出现了读的操作比写的慢，会出现脏读，可以给缓存加一个到期时间。

# 四、MySQL和Redis之间数据同步的实现

## 1.使用中间件Canal来实现数据同步

Canal是一个伪装成MySQL Slave，实现获取Master上的binlog文件数据。实现自己的功能。

<img src="img/截屏2021-09-06 下午2.22.13.png" alt="截屏2021-09-06 下午2.22.13" style="zoom:30%;" />



## 2.搭建Canal的服务端

- 下载Canal的官方部署压缩包：https://github.com/alibaba/canal/releases/tag/canal-1.0.19
- 解压缩
- 编辑配置

```shell
# 修改 Mysql Master主节点的ip
example/instance.properties=>canal.instance.master.address = 172.16.253.41:3306 
# 修改canal服务端的ip
canal.properties=>canal.ip=127.0.0.1
```

- 启动服务

```shell
bin/startup.sh
```



## 3.搭建Canal客户端实现redis和mysql的数据同步



# 五、分库分表中间件ShardingSphere

## 1.分库分表的应用场景

<img src="img/截屏2021-09-06 下午3.37.53.png" alt="截屏2021-09-06 下午3.37.53" style="zoom:50%;" />

整个系统，访问较为频繁且数据量非常大的表才会涉及到分表的操作。

分表的操作会带来很多麻烦的地方，比如全表扫描，比如主键不连续且必须唯一，等等。

因此如果数据量不大的表，不建议做分表。



## 2.ShardingSphere的介绍

ShardingSphere目前支持三种模式

- ==sharding-jdbc：最稳定和最常用==
- sharding-proxy：需要独立部署一个代理服务
- sharding-sidecar：整合服务网格提出的一套新的解决方案（孵化中）



## 3.什么是分库分表

- 垂直分片：根据业务边界来划分，不同的业务操作的数据库应该是不同的，比如用户数据库、工地数据库
- 水平分片：将多条记录分到不同的库的不同的表中。如果一张表的数据量非常大，通过分表可以降低单表的数据量。

对于分库分表来说，首要解决的是分片策略是怎么实现的，以及有分片策略。



## 4.实现分库分表案例

实现分库分表的案例操作流程看文档即可（shardingshpere立即开始.md）。

需要弄清楚一下几个概念：

- 逻辑表
- 物理表
- 分片策略
- 分片列



我们发现有这么几个问题：

- 如果每张表的id都是自动增长，怎么解决？
- 查询的时候查所有记录是不是会查每张表？
- 如何做范围id的范围查找？



# 作业

- 搭建mysql主从服务器，完成主从同步
- 实现mysql和redis之间的主从同步
- 搭建分库分表MySQL服务器，创建多个库和多个表（根据文档里的例子）
- 完成分库分表的demo



