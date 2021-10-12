# 前端页面

```js
$.ajax({
				url:"checkIsLogin",
				type: 'get',
                //允许携带证书
                xhrFields: {
                    withCredentials: true
                },
                //允许跨域
                crossDomain: true,
				success: function(d){
					console.log(d);
					if(d.result==true){
						$("#loginBox").html("欢迎你，"+d.data.username+"!<a href=\"#\" target=\"_top\">注销</a>")
					}else{
						$("#loginBox").html("<a href=\"http://sso.qf.com:9092/user/login\" target=\"_top\" class=\"h\">亲，请登录</a>\n" +
								"\t\t<a href=\"#\" target=\"_top\">免费注册</a>");
					}
				}
			});
```



# 后端接口

```java
@RequestMapping("checkIsLogin")
    public ResultVO checkIsLogin(@CookieValue(name= CookieConstant.USER_TOKEN,required = false)String uuid){
        return userService.checkIsLogin(uuid);
    }
```

