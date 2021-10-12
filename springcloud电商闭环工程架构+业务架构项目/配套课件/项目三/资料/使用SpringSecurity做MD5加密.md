# 使用SpringSecurity做MD5加密

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

