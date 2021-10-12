# 一、Linux 远程控制管理

## 1.概述

传统的网络服务程序，FTP、POP、telnet 本质上都是不安全的，因为它们在网络上通过明文传送口令和数据，这些数据非常容易被截获。SSH 叫做 `Secure Shell`。通过 SSH，可以把传输数据进行加密，预防攻击，传输的数据进行了压缩，可以加快传输速度。

## 2.OpenSSH

SSH 是芬兰一家公司开发。但是受到版权和加密算法限制，现在很多人都使用 OpenSSH。OpenSSH 是 SSH 的替代软件，免费。

OpenSSH 由客户端和服务端组成。

- 基于口令的安全验证：知道服务器的帐号密码即可远程登录，口令和数据在传输过程中都会被加密。
- 基于密钥的安全验证：此时需要在创建一对密钥，把公有密钥放到远程服务器上自己的宿主目录中，而私有密钥则由自己保存。

### 1）检查软件是否安装

```
apt-cache policy openssh-client openssh-server
```

### 2）安装服务端

```
apt-get install openssh-server
```

### 3）安装客户端

```
apt-get install openssh-client
```

OpenSSH 服务器的主要配置文件为 `/etc/ssh/sshd\_config`，几乎所有的配置信息都在此文件中。

## 3.XShell

XShell 是一个强大的安全终端模拟软件，它支持 SSH1, SSH2, 以及 Microsoft Windows 平台的 TELNET 协议。XShell 通过互联网到远程主机的安全连接以及它创新性的设计和特色帮助用户在复杂的网络环境中享受他们的工作。

XShell 可以在 Windows 界面下用来访问远端不同系统下的服务器，从而比较好的达到远程控制终端的目的。



# 二、Linux 软件包管理

## 1.概述

APT(Advanced Packaging Tool) 是 Debian/Ubuntu 类 Linux 系统中的软件包管理程序, 使用它可以找到想要的软件包, 而且安装、卸载、更新都很简便；也可以用来对 Ubuntu 进行升级; APT 的源文件为 `/etc/apt/` 目录下的 `sources.list` 文件。

## 2.修改数据源

由于国内的网络环境问题，我们需要将 Ubuntu 的数据源修改为国内数据源，操作步骤如下：

### 1）查看系统版本

```
lsb_release -a
```

输出结果为

```
No LSB modules are available.
Distributor ID:	Ubuntu
Description:	Ubuntu 16.04 LTS
Release:	16.04
Codename:	xenial
```

**注意：** Codename 为 `xenial`，该名称为我们 Ubuntu 系统的名称，修改数据源需要用到该名称

### 2）编辑数据源

```
vi /etc/apt/sources.list
```

删除全部内容并修改为

```
deb http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse
```

### 3）更新数据源

```
apt-get update
```

### 4）可能会出现的问题

- 没有联网

  发现ping不通www.baidu.com,配置VMnet8虚拟网卡，IP及DNS配置如下

  ![](pic\1.bmp)

- 没有配置dns

  发现此时依然ping不通百度，但可以ping通百度的ip，说明DNS出了问题。配置ubuntu的dns服务器，步骤如下：

```shell
# 第一步：编辑（创建）文件
sudo vim /etc/resolvconf/resolv.conf.d/base
# 第二步：插入如下内容并保存退出
nameserver 8.8.8.8
# 第三步：执行如下命令使之生效
resolvconf -u
# 此时查看 /etc/resolv.conf发现多了dns的配置。
```

- 再次ping 百度，成功！

## 3.常用 APT 命令

### 1）安装软件包

```
apt-get install packagename
```

### 2）删除软件包

```
apt-get remove packagename
```

### 3）更新软件包列表

```
apt-get update
```

### 4）升级有可用更新的系统（慎用）

```
apt-get upgrade
```

## 4.其它 APT 命令

### 1）搜索

```
apt-cache search package
```

### 2）获取包信息

```
apt-cache show package
```

### 3）删除包及配置文件

```
apt-get remove package --purge
```

### 4）了解使用依赖

```
apt-cache depends package
```

### 5）查看被哪些包依赖

```
apt-cache rdepends package
```

### 6）安装相关的编译环境

```
apt-get build-dep package
```

### 7）下载源代码

```
apt-get source package
```

### 8）清理无用的包

```
apt-get clean && apt-get autoclean
```

### 9）检查是否有损坏的依赖

```
apt-get check
```

# 三、Linux 用户和组管理

## 1.概述

Linux 操作系统是一个多用户操作系统，它允许多用户同时登录到系统上并使用资源。系统会根据账户来区分每个用户的文件，进程，任务和工作环境，使得每个用户工作都不受干扰。

## 2.使用 Root 用户

在实际生产操作中，我们基本上都是使用超级管理员账户操作 Linux 系统，也就是 Root 用户，Linux 系统默认是关闭 Root 账户的，我们需要为 Root 用户设置一个初始密码以方便我们使用。

### 1）设置 Root 账户密码

```
sudo passwd root
```

### 2）切换到 Root

```
su
```

### 3）设置允许远程登录 Root

```
vim /etc/ssh/sshd_config

# Authentication:
LoginGraceTime 120
#PermitRootLogin without-password     //注释此行
PermitRootLogin yes                             //加入此行
StrictModes yes

重启服务
service ssh restart
```

## 3.用户账户说明

### 1）普通用户

普通用户在系统上的任务是进行普通操作

### 2）超级管理员

管理员在系统上的任务是对普通用户和整个系统进行管理。对系统具有绝对的控制权，能够对系统进行一切操作。用 root 表示，root 用户在系统中拥有最高权限，默认下 Ubuntu 用户的 root 用户是不能登录的。

### 3）安装时创建的系统用户

此用户创建时被添加到 admin 组中，在 Ubuntu 中，admin 组中的用户默认是可以使用 `sudo` 命令来执行只有管理员才能执行的命令的。如果不使用 `sudo` 就是一个普通用户。

## 4.组账户说明

### 1）私有组

当创建一个用户时没有指定属于哪个组，Linux 就会建立一个与用户同名的私有组，此私有组只含有该用户。

### 2）标准组

当创建一个用户时可以选定一个标准组，如果一个用户同时属于多个组时，登录后所属的组为主组，其他的为附加组。

## 5.账户系统文件说明

### 1）/etc/passwd

每一行代表一个账号，众多账号是系统正常运行所必须的，例如 bin，nobody 每行定义一个用户账户，此文件对所有用户可读。每行账户包含如下信息：

`root:x:0:0:root:/root:/bin/bash`

```
szl:x:1000:1000:szl,,,:/home/szl:/bin/bash
```



- **用户名：** 就是账号，用来对应 UID，root UID 是 0。
- **口令：** 密码，早期 UNIX 系统密码存在此字段，由于此文件所有用户都可以读取，密码容易泄露，后来这个字段数据就存放到 /etc/shadow 中，这里只能看到 X。
- **用户标示号（UID）：** 系统内唯一，root 用户的 UID 为 0，普通用户从 1000 开始，1-999 是系统的标准账户，500-65536 是可登陆账号。
- **组标示号（GID）：** 与 /etc/group 相关用来规定组名和 GID 相对应。
- **注释：** 注释账号
- **宿主目录（主文件夹）：** 用户登录系统后所进入的目录 root 在 /root/
- **命令解释器（shell）：** 指定该用户使用的 shell ，默认的是 /bin/bash

### 2）/etc/shadow

为了增加系统的安全性，用户口令通常用 shadow passwords 保护。只有 root 可读。每行包含如下信息：

`root:$6$Reu571.V$Ci/kd.OTzaSGU.TagZ5KjYx2MLzQv2IkZ24E1.yeTT3Pp4o/yniTjus/rRaJ92Z18MVy6suf1W5uxxurqssel.:17465:0:99999:7:::`

- **账号名称：** 需要和 /etc/passwd 一致。

- 密码：


  经过加密，虽然加密，但不表示不会被破解，该文件默认权限如下：

  - -rw------- 1 root root 1560 Oct 26 17:20 passwd-
  - 只有root能都读写

- **最近修改密码日期：** 从1970-1-1起，到用户最后一次更改口令的天数

- **密码最小时间间隔：** 从1970-1-1起，到用户可以更改口令的天数

- **密码最大时间间隔：** 从1970-1-1起，必须更改的口令天数

- **密码到期警告时间：** 在口令过期之前几天通知

- **密码到期后账号宽限时间**

- **密码到期禁用账户时间：** 在用户口令过期后到禁用账户的天数

- **保留**

### 4）/etc/gshadow

该文件用户定义用户组口令，组管理员等信息只有root用户可读。

`root:\*::`

- **用户组名**
- **密码列**
- **用户组管理员的账号**
- **用户组所属账号**

## 6.账户管理常用命令

### 1）增加用户

```
useradd 用户名
useradd -u (UID号)
useradd -p (口令)
useradd -g (分组)
useradd -s (SHELL)
useradd -d (用户目录)
```

如：`useradd jack`

增加用户名为 jack 的账户

### 2）修改用户

```
usermod -u (新UID)
usermod -d (用户目录)
usermod -g (组名)
usermod -s (SHELL)
usermod -p (新口令)
usermod -l (新登录名)
usermod -L (锁定用户账号密码)
usermod -U (解锁用户账号)
```

如：`usermod -u 1024 -g group2 -G root jack`

将 jack 用户 uid 修改为 1024，默认组改为系统中已经存在的 group2，并且加入到系统管理员组

### 3）删除用户

```
userdel 用户名 (删除用户账号)
userdel -r 删除账号时同时删除目录
```

如：`userdel -r jack`

删除用户名为 jack 的账户并同时删除 jack 的用户目录

### 4）组账户维护

```
groupadd 组账户名 (创建新组)
groupadd -g 指定组GID
groupmod -g 更改组的GID
groupmod -n 更改组账户名
groupdel 组账户名 (删除指定组账户)
```

### 5）口令维护

```
passwd 用户账户名 (设置用户口令)
passwd -l 用户账户名 (锁定用户账户)
passwd -u 用户账户名 (解锁用户账户)
passwd -d 用户账户名 (删除账户口令)
gpasswd -a 用户账户名 组账户名 (将指定用户添加到指定组)
gpasswd -d 用户账户名 组账户名 (将用户从指定组中删除)
gpasswd -A 用户账户名 组账户名 (将用户指定为组的管理员)
```

### 6）用户和组状态

```
su 用户名(切换用户账户)
id 用户名(显示用户的UID，GID)
whoami (显示当前用户名称)
groups (显示用户所属组)
```

# 四、Linux 文件权限管理

## 1.查看文件和目录的权限

ls –al`使用 ls 不带参数只显示文件名称，通过`ls –al` 可以显示文件或者目录的权限信息。

`ls -l 文件名` 显示信息包括：文件类型 (`d` 目录，`-` 普通文件，`l` 链接文件)，文件权限，文件的用户，文件的所属组，文件的大小，文件的创建时间，文件的名称

`-rw-r--r-- 1 jack jack 675 Oct 26 17:20 .profile`

- `-`：普通文件
- `rw-`：说明用户 jack 有读写权限，没有运行权限
- `r--`：表示用户组 jack 只有读权限，没有写和运行的权限
- `r--`：其他用户只有读权限，没有写权限和运行的权限

| -rw-r--r--     | 1      | jack         | jack       | 675      | Oct 26 17:20       | .profile |
| -------------- | ------ | ------------ | ---------- | -------- | ------------------ | -------- |
| 文档类型及权限 | 连接数 | 文档所属用户 | 文档所属组 | 文档大小 | 文档最后被修改日期 | 文档名称 |

| -        | rw-                    | r--                         | r--                   |
| -------- | ---------------------- | --------------------------- | --------------------- |
| 文档类型 | 文档所有者权限（user） | 文档所属用户组权限（group） | 其他用户权限（other） |

### 1）文档类型

- `d` 表示目录
- `l` 表示软连接 
- `–` 表示文件
- `c` 表示串行端口字符设备文件
- `b` 表示可供存储的块设备文件
- 余下的字符 3 个字符为一组。`r` 只读，`w` 可写，`x` 可执行，`-` 表示无此权限

### 2）连接数

指有多少个文件指向同一个索引节点。

### 3）文档所属用户和所属组

就是文档属于哪个用户和用户组。文件所属用户和组是可以更改的

### 4）文档大小

默认是 bytes

## 2.更改操作权限

### 1）chown

是 change owner 的意思，主要作用就是改变文件或者目录所有者，所有者包含用户和用户组

`chown [-R] 用户名称 文件或者目录`

`chown [-R] 用户名称 用户组名称 文件或目录`

-R：进行递归式的权限更改，将目录下的所有文件、子目录更新为指定用户组权限

### 2）chmod

改变访问权限

`chmod [who] [+ | - | =] [mode] 文件名`

#### （1）who

表示操作对象可以是以下字母的一个或者组合

- u：用户 user
- g：用户组 group
- o：表示其他用户
- a：表示所有用户是系统默认的

#### （2）操作符号

- +：表示添加某个权限
- -：表示取消某个权限
- =：赋予给定的权限，取消文档以前的所有权限

#### （3）mode

表示可执行的权限，可以是 r、w、x

#### （4）文件名

文件名可以使空格分开的文件列表

#### （5）示例

```
jack@UbuntuBase:~$ ls -al test.txt 
-rw-rw-r-- 1 jack jack 6 Nov  2 21:47 test.txt
jack@UbuntuBase:~$ chmod u=rwx,g+r,o+r test.txt 
jack@UbuntuBase:~$ ls -al test.txt 
-rwxrw-r-- 1 jack jack 6 Nov  2 21:47 test.txt
jack@UbuntuBase:~$
```

## 3.数字设定法

数字设定法中数字表示的含义

- 0 表示没有任何权限
- 1 表示有可执行权限 = `x`
- 2 表示有可写权限 = `w`
- 4 表示有可读权限 = `r`

也可以用数字来表示权限如 chmod 755 file_name

| r w x | r – x | r - x  |
| ----- | ----- | ------ |
| 4 2 1 | 4 - 1 | 4 - 1  |
| user  | group | others |

若要 rwx 属性则 4+2+1=7

若要 rw- 属性则 4+2=6

若要 r-x 属性则 4+1=5

```shell
jack@UbuntuBase:~$ chmod 777 test.txt 
jack@UbuntuBase:~$ ls -al test.txt 
-rwxrwxrwx 1 jack jack 6 Nov  2 21:47 test.txt

jack@UbuntuBase:~$ chmod 770 test.txt 
jack@UbuntuBase:~$ ls -al test.txt 
-rwxrwx--- 1 jack jack 6 Nov  2 21:47 test.txt
```

# 五、Linux 安装 Java

## 1.概述

此处以 JDK 1.8.0_152 为例

## 2.下载地址

http://www.oracle.com/technetwork/java/javase/downloads/index.html

## 3.解压缩并移动到指定目录

### 1）解压缩

```
tar -zxvf jdk-8u152-linux-x64.tar.gz
```

### 2）创建目录

```
mkdir -p /usr/local/java
```

### 3）移动安装包

```
mv jdk1.8.0_152/ /usr/local/java/
```

### 4）设置所有者

```
chown -R root:root /usr/local/java/
```

## 5）配置环境变量

### (1)配置系统环境变量

```
nano /etc/environment
```

### (2)添加如下语句

```
PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games"
export JAVA_HOME=/usr/local/java/jdk1.8.0_191
export JRE_HOME=/usr/local/java/jdk1.8.0_191/jre
export CLASSPATH=$CLASSPATH:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
```

### (3)配置用户环境变量

```
nano /etc/profile
```

### (4)添加如下语句

```
if [ "$PS1" ]; then
  if [ "$BASH" ] && [ "$BASH" != "/bin/sh" ]; then
    # The file bash.bashrc already sets the default PS1.
    # PS1='\h:\w\$ '
    if [ -f /etc/bash.bashrc ]; then
      . /etc/bash.bashrc
    fi
  else
    if [ "`id -u`" -eq 0 ]; then
      PS1='# '
    else
      PS1='$ '
    fi
  fi
fi

export JAVA_HOME=/usr/local/java/jdk1.8.0_191
export JRE_HOME=/usr/local/java/jdk1.8.0_191/jre
export CLASSPATH=$CLASSPATH:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH:$HOME/bin

if [ -d /etc/profile.d ]; then
  for i in /etc/profile.d/*.sh; do
    if [ -r $i ]; then
      . $i
    fi
  done
  unset i
fi
```

### (5)使用户环境变量生效

```
source /etc/profile
```

## 6）测试是否安装成功

```
root@UbuntuBase:/usr/local/java# java -version
java version "1.8.0_152"
Java(TM) SE Runtime Environment (build 1.8.0_152-b16)
Java HotSpot(TM) 64-Bit Server VM (build 25.152-b16, mixed mode)
```

## 7）为其他用户更新用户环境变量

```
su jack
source /etc/profile
```

# 六、Linux 安装 Tomcat

## 1.概述

此处以 Tomcat 8.5.23 为例

## 2.下载地址

https://tomcat.apache.org/

## 3.解压缩并移动到指定目录

### 1）解压缩

```
tar -zxvf apache-tomcat-8.5.23.tar.gz
```

### 2）变更目录名

```
mv apache-tomcat-8.5.23 tomcat
```

### 3）移动目录

```
mv tomcat/ /usr/local/
```

## 4.常用命令

### 1）启动

```
/usr/local/tomcat/bin/startup.sh
```

### 2）停止

```
/usr/local/tomcat/bin/shutdown.sh
```

### 3）目录内执行脚本

```
./startup.sh
```

# 七、Linux 安装 MySQL

## 1.安装

### 1)更新数据源

```
apt-get update
```

### 2)安装 MySQL

```
apt-get install mysql-server
```

系统将提示您在安装过程中创建 root 密码。选择一个安全的密码，并确保你记住它，因为你以后需要它。接下来，我们将完成 MySQL 的配置。

## 2.配置

因为是全新安装，您需要运行附带的安全脚本。这会更改一些不太安全的默认选项，例如远程 root 登录和示例用户。在旧版本的 MySQL 上，您需要手动初始化数据目录，但 Mysql 5.7 已经自动完成了。

运行安全脚本：

```
mysql_secure_installation
```

这将提示您输入您在之前步骤中创建的 root 密码。您可以按 Y，然后 ENTER 接受所有后续问题的默认值，但是要询问您是否要更改 root 密码。您只需在之前步骤中进行设置即可，因此无需现在更改。

## 3.测试

按上边方式安装完成后，MySQL 应该已经开始自动运行了。要测试它，请检查其状态。

```
jack@ubuntu:~$ systemctl status mysql.service
● mysql.service - MySQL Community Server
   Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
   Active: active (running) since Tue 2017-11-21 13:04:34 CST; 3min 24s ago
 Main PID: 2169 (mysqld)
   CGroup: /system.slice/mysql.service
           └─2169 /usr/sbin/mysqld

Nov 21 13:04:33 ubuntu systemd[1]: Starting MySQL Community Server...
Nov 21 13:04:34 ubuntu systemd[1]: Started MySQL Community Server.
```



查看 MySQL 版本：

```
mysqladmin -p -u root version
```

## 4.配置远程访问

- 修改配置文件

```
nano /etc/mysql/mysql.conf.d/mysqld.cnf
```

- 注释掉(语句前面加上 # 即可)：

```
bind-address = 127.0.0.1
```

- 重启 MySQL

```
service mysql restart
```

- 登录 MySQL

```
mysql -u root -p
```

- 授权 root 用户允许所有人连接

```
grant all privileges on *.* to 'root'@'%' identified by 'qf123456';
```

### 因弱口令无法成功授权解决步骤

- 查看和设置密码安全级别

```
select @@validate_password_policy;
```

```
set global validate_password_policy=0;
```

- 查看和设置密码长度限制

```
select @@validate_password_length;
```

```
set global validate_password_length=1;
```

## 5.常用命令

### 1)启动

```
service mysql start
```

### 2)停止

```
service mysql stop
```

### 3)重启

```
service mysql restart
```

## 6.其它配置

修改配置 `mysqld.cnf` 配置文件

```
vi /etc/mysql/mysql.conf.d/mysqld.cnf
```

### 1.配置默认字符集

在 `[mysqld]` 节点上增加如下配置

```
[client]
default-character-set=utf8
```

在 `[mysqld]` 节点底部增加如下配置

```
default-storage-engine=INNODB
character-set-server=utf8
collation-server=utf8_general_ci
```

### 2.配置忽略数据库大小写敏感

在 `[mysqld]` 节点底部增加如下配置

```
lower-case-table-names = 1
```