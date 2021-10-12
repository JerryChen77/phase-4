# SpringBoot整合redis

## 引入依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```



## 配置文件 application.yml

```yaml
spring:
  redis:
    host: 192.168.2.147
    port: 6379
    password: java1909
    jedis:
      pool:
        max-active: 100
```







# Redis实战一之短信服务



```java
@Test
public void saveCodeTest(){
    String code = "6666";
    String phone = "18899887788";

    redisTemplate.opsForValue().set("register:code:"+phone,code);

    redisTemplate.expire("register:code:"+phone,3, TimeUnit.MINUTES);


}
```



```java
    @Test
    public void checkCodeTest(){
        String code = "6666";
        String phone = "18899887788";

        Object o = redisTemplate.opsForValue().get("register:code:" + phone);
        if("6666".equals(o)){
            System.out.println("验证码正确");
            redisTemplate.delete("register:code:" + phone);
        }else{
            System.out.println("验证码错误");

        }


    } 
```

# Redis实战二之数据缓存

```java
 @Test
    public void cacheTest(){

      List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

      if(types==null){
          System.out.println("查询数据库。。。");
          types = new ArrayList<>();
          types.add(new ProductType(1L,"手机数码"));
          types.add(new ProductType(2L,"家用电器"));

          redisTemplate.opsForValue().set("product:types",types);

      }else{
          System.out.println("查询缓存。。。");
      }
    } 

@Test
    public void delCacheTest(){
        redisTemplate.delete("product:types");
    }

```



# Redis热点缓存重建时出现缓存击穿的问题。

模拟高并发场景，出现缓存穿透的情况

```java
 @Test
    public void cacheTest(){

      List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

      if(types==null){
          System.out.println("查询数据库。。。");
          types = new ArrayList<>();
          types.add(new ProductType(1L,"手机数码"));
          types.add(new ProductType(2L,"家用电器"));

          redisTemplate.opsForValue().set("product:types",types);

      }else{
          System.out.println("查询缓存。。。");
      }
    }
```

```java
 @Test
    public void multiThreadTest() throws InterruptedException {

        ExecutorService pool = new ThreadPoolExecutor(100,200,100,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(100));

        for (int i = 0; i < 100; i++) {

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    cacheTest();
                }
            });
        }
        Thread.sleep(1000000);
    }
```



# 解决缓存穿透，Redis实现分布式锁

可以使用synchronized来加锁吗？  synchronized的作用域是JVM

使用分布式锁来实现，本质是一个第三方资源

​	方案一： 数据库里建表 ，表里有lock字段，0是未上锁，1是上锁，但是这是对数据库的io操作，性能不高。

​	方案二：使用redis做分布式锁

```
setnx Locks 1 ==>1
get Locks ==1
setnx Locks 2 ==>0  结果是0，因为之前已经存在Locks
del Locks
setnx Locks 2==>1
```



```java
@Test
    public void cacheTest1(){

        List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

        if(types==null) {
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("product:types:lock", 1);
            if (ifAbsent) {
                System.out.println("查询数据库。。。");
                types = new ArrayList<>();
                types.add(new ProductType(1L, "手机数码"));
                types.add(new ProductType(2L, "家用电器"));

                redisTemplate.opsForValue().set("product:types", types);
                redisTemplate.delete("product:types:lock");
            } else {

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cacheTest1();
            }
        }else{
            System.out.println("查询缓存。。。");

        }

    }

```

```java
 @Test
    public void multiThreadTest() throws InterruptedException {

        ExecutorService pool = new ThreadPoolExecutor(100,200,100,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(100));

        for (int i = 0; i < 100; i++) {

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    cacheTest1();
                }
            });
        }
        Thread.sleep(1000000);
    }

```



# 解决死锁问题



```java
@Test
public void cacheTest1(){
    List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

    if(types==null) {
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("product:types:lock", 1);
        if (ifAbsent) {
            System.out.println("查询数据库。。。");
            types = new ArrayList<>();
            types.add(new ProductType(1L, "手机数码"));
            types.add(new ProductType(2L, "家用电器"));
			//发生了异常,没办法释放锁。
			int i = 10/0;
            redisTemplate.opsForValue().set("product:types", types);
            redisTemplate.delete("product:types:lock");
        } else {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cacheTest1();
        }
    }else{
        System.out.println("查询缓存。。。");

    }

}
```


方案一：try-finnaly

```java
  @Test
    public void cacheTest1() {

        List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

        if (types == null) {
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("product:types:lock", 1);
            if (ifAbsent) {
                try {
                    System.out.println("查询数据库。。。");
                    types = new ArrayList<>();
                    types.add(new ProductType(1L, "手机数码"));
                    types.add(new ProductType(2L, "家用电器"));
                    int i = 10 / 0;
                    redisTemplate.opsForValue().set("product:types", types);
                } finally {
                    redisTemplate.delete("product:types:lock");
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cacheTest1();
            }
        } else {
            System.out.println("查询缓存。。。");

        }

    }
```



方案二：给key设置有效期,但可能会造成误删锁，比如超过100毫秒的操作，进程1的锁已经到期了，进程2创建出了自己的锁，但进程1在执行时删除的是进程2的锁。

```java
    @Test
    public void cacheTest1() {

        List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

        if (types == null) {
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("product:types:lock", 1);
            if (ifAbsent) {
                //设置有效期
                redisTemplate.expire("product:types:lock",100,TimeUnit.MILLISECONDS);
                try {
                    System.out.println("查询数据库。。。");
                    types = new ArrayList<>();
                    types.add(new ProductType(1L, "手机数码"));
                    types.add(new ProductType(2L, "家用电器"));
                    int i = 10 / 0;
                    redisTemplate.opsForValue().set("product:types", types);
                } finally {
                    //需要避免误删别人的锁
                    redisTemplate.delete("product:types:lock");
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cacheTest1();
            }
        } else {
            System.out.println("查询缓存。。。");

        }

    }
```

方案三：避免误删锁

```java
@Test
    public void cacheTest1() {

        List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("product:types");

        if (types == null) {
            String uuid = UUID.randomUUID().toString();
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("product:types:lock", uuid);
            if (ifAbsent) {
                //设置有效期
                redisTemplate.expire("product:types:lock",100,TimeUnit.MILLISECONDS);
                try {
                    System.out.println("查询数据库。。。");
                    types = new ArrayList<>();
                    types.add(new ProductType(1L, "手机数码"));
                    types.add(new ProductType(2L, "家用电器"));
                    int i = 10 / 0;
                    redisTemplate.opsForValue().set("product:types", types);
                } finally {
                    //需要避免误删别人的锁
                    Object o = redisTemplate.opsForValue().get("product:types:lock");
                    if(uuid.equals(o)){
                        redisTemplate.delete("product:types:lock");
                    }
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cacheTest1();
            }
        } else {
            System.out.println("查询缓存。。。");

        }

    }

```



# 缓存穿透的问题

这是一种攻击手段，故意在查一些不存在的对象，所以 每次都会穿透缓存，直击数据库

```java
 @Test
    public void initProductTypeTest(){

        //提前在缓存中保存好商品的信息 缓存预热
        for (int i = 0; i < 10; i++) {
            ProductType type = new ProductType(i+1L,"type"+i);
            redisTemplate.opsForValue().set("product:type:"+i,type);
        }


    }
```

```java
  @Test
    public void cachePenetrationTest(){
        for (int i = 11; i < 20; i++) {
            ProductType type = (ProductType) redisTemplate.opsForValue().get("product:type:" + i);
            if(type==null){
                //查询数据库
                System.out.println("查询数据库");
                type= getId(i);//如果i大于10，type都是null
                redisTemplate.opsForValue().set("product:type:" + i,type);
            }else{
                System.out.println("查询缓存");
            }
        }
    }


    public ProductType getId(int i){
        if(i>=10){
            return null;
        }
        //查询数据库
        return new ProductType((long)i,"type"+i);
    }

```

# 解决缓存穿透

如果查询到的对象是空的，人为的创建一个对象放入到缓存中，并且设置一个较短的有效期。

```java
 @Test
    public void cachePenetrationTest(){
        for (int i = 11; i < 20; i++) {
            ProductType type = (ProductType) redisTemplate.opsForValue().get("product:type:" + i);
            if(type==null){
                //查询数据库
                System.out.println("查询数据库");
                type= getId(i);//如果i大于10，type都是null

                if(type==null){
                    type= new ProductType(1l,"");
                    redisTemplate.opsForValue().set("product:type:" + i,type);
                    redisTemplate.expire("product:type:" + i,1,TimeUnit.MINUTES);
                }else{
                    redisTemplate.opsForValue().set("product:type:" + i,type);
                    redisTemplate.expire("product:type:" + i,20,TimeUnit.MINUTES);
                }


            }else{
                System.out.println("查询缓存");
            }
        }
    }
```





# MD5加密

## 引入依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 配置

因为Spring  Security自带前台验证，需要通过配置去除

```java
package com.qf.v2.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```



## 生成MD5密文

```java
   @Autowired
    private BCryptPasswordEncoder encoder;


    @Test
    public void contextLoads() {

         System.out.println(encoder.encode("123456"));
         
    }
```



## 匹配密文

```java
 boolean matches = encoder.matches("123456",
                "$2a$10$DPP4c9idF9WWW5jM8eNiremRExNbfO6ur04gY/1aVM.kewebR5fLe");
```



## 配置Redis

```yaml
spring:
  redis:
    host: 192.168.2.147
    password: java1902
```



# 单点登录



![1564988507487](pic\1564988507487.png)



![1564989332378](pic\1564989332378.png)









## LoginController

```java
@Controller
@RequestMapping("user")
public class LoginController {


    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("ShowLogin")
    public String login() {
        return "login";
    }


    //未完
    @RequestMapping("checkLogin")
    public String checkLogin(TUser tUser, HttpServletResponse response) {

        //验证是否登录成功
        TUser currentUser = userService.checkIsExists(tUser);

        //登录失败
        if (currentUser == null) {
            return "login";
        }


        //登录成功
        String uuid = UUID.randomUUID().toString();

        String redisKey = new StringBuffer(RedisConstant.USER_TOKEN).append(uuid).toString();

        currentUser.setPassword(null);

        //登录成功

        //便于查询
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.opsForValue().set(redisKey, currentUser);
        //设置超时时间
        redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);

        //Cookie
        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN, uuid);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        
        return "redirect:http://www.baidu.com";
        
    }


    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_TOKEN, required = false) String uuid) {

        ResultBean resultBean = userService.checkIsLogin(uuid);
        return resultBean;

    }


    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = CookieConstant.USER_TOKEN, required = false) String uuid) {

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        String key = new StringBuffer(RedisConstant.USER_TOKEN).append(uuid).toString();

        redisTemplate.delete(key);

        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN, uuid);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return new ResultBean(0, "注销成功", "");

    }

}
```

## UserService

```java
@Component
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {


    @Autowired
    private TUserMapper userMapper;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;




    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }

    @Override
    public TUser checkIsExists(TUser tUser) {
        TUser user = userMapper.selectByUser(tUser.getUsername());

        if(user!=null){
            if(passwordEncoder.matches(tUser.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;

    }

    @Override
    public ResultBean checkIsLogin(String uuid) {

        if(uuid==null){
            return new ResultBean(1,null,"当前用户未登录");

        }

        String key = new StringBuffer(RedisConstant.USER_TOKEN).append(uuid).toString();

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        TUser user = (TUser) redisTemplate.opsForValue().get(key);

        if(user==null){
            return new ResultBean(1,null,"当前用户未登录");
        }
        //更新时间
        redisTemplate.expire(key,30, TimeUnit.MINUTES);

        return new ResultBean(0,user,"");
    }
}

```

