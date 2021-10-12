# 概述

我 Fork 了 Leaf 的官方代码在此基础之上制作了 Dockerfile，可以很方便的构建并运行获取雪花算法的 ID

# 克隆

```
git clone https://github.com/funtl/Leaf.git
cd Leaf
mvn clean install -DskipTests
```

# 构建

```
cd leaf-docker
chmod +x build.sh
./build.sh
```

# 运行

```
docker-compose up -d
```

# 测试

```
curl http://localhost:8080/api/snowflake/get/test

# 输出如下
1209912709605228625
```