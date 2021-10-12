服务网关地址:http://localhost:9001/

### 1)全局设置访问前缀

zuul.prefix=/order-api   为网关服务地址统一加上前缀
zuul.strip-prefix=false  表示是否去除前缀(去除下游目标服务的)   false 表示不取出    true 表示取出

在没有加入 zuul.prefix=/order-api zuul.strip-prefix=false 二项配置的时候，通过网关服务地址访问下游服务
http://localhost:9001/order-service/order/queryOrdersByUserId/1

加入了 zuul.prefix=/order-api zuul.strip-prefix=false 二项配置的时候，通过网关服务地址访问下游服务
http://localhost:9001/order-api/order-service/order/queryOrdersByUserId/1



那么下游服务的请求地址 就会自动转发带入了/order-api/   所以我们就可以通过设置下游服务的
http://localhost:8002/order-api/order/queryOrdersByUserId/1
server.servlet.context-path=/order-api 来指定


### 2)部分设置访问前缀

zuul.prefix=/order-api
zuul.strip-prefix=true  (表示目标下游服务会带上order-api )
zuul.routes.use-routing.serviceId=ms-provider-order
zuul.routes.use-routing.path=/order-service/**
zuul.routes.use-routing.stripPrefix=false  (表示目标下游服务 请求会带上order-service)
那么请求下游服务的地址是:http://localhost:8002/order-api/order-service/order/queryOrdersByUserId/1



