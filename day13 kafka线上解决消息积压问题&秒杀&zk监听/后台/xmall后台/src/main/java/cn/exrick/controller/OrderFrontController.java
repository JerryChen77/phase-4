package cn.exrick.controller;


import cn.exrick.common.jedis.JedisClient;
import cn.exrick.pojo.TbThanks;
import cn.exrick.pojo.common.Result;
import cn.exrick.pojo.front.Order;
import cn.exrick.pojo.front.OrderInfo;
import cn.exrick.pojo.front.PageOrder;
import cn.exrick.service.OrderFrontService;
import cn.exrick.utils.EmailUtil;
import cn.exrick.utils.IPInfoUtil;
import cn.exrick.utils.ResultUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Exrickx
 */
@RestController
@Api(description = "订单")
public class OrderFrontController {

    private final static Logger log= LoggerFactory.getLogger(OrderFrontController.class);

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private OrderFrontService orderService;

    @Autowired
    EmailUtil emailUtil;

    private static final String CLOSE_PRE = "XMALL_CLOSE_PRE";

    @RequestMapping(value = "/member/orderList",method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size){

        PageOrder pageOrder=orderService.getOrderList(Long.valueOf(userId), page, size);
        return new ResultUtil<PageOrder>().setData(pageOrder);
    }

    @RequestMapping(value = "/member/orderDetail",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(String orderId){

        Order order=orderService.getOrder(Long.valueOf(orderId));
        return new ResultUtil<Order>().setData(order);
    }

    @RequestMapping(value = "/member/addOrder",method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request){

        //防炸库验证
        String ip= IPInfoUtil.getIpAddr(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }
        //保证订单的提交（add）幂等性保证 强一致性 zk的分布式锁
        String redisKey="addOrder_"+ip;
        String temp=jedisClient.get(redisKey);
        if(StringUtils.isNotBlank(temp)){
            return new ResultUtil<Object>().setErrorMsg("您提交的太频繁啦，作者的学生服务器要炸啦！请2分钟后再试");
        }

        Long orderId=orderService.createOrder(orderInfo);

        //记录缓存
        jedisClient.set(redisKey,"ADDED");
        jedisClient.expire(redisKey,120);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @RequestMapping(value = "/member/cancelOrder",method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(@RequestBody Order order){

        int result=orderService.cancelOrder(order.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delOrder",method = RequestMethod.GET)
    @ApiOperation(value = "删除订单")
    public Result<Object> delOrder(String orderId){

        int result=orderService.delOrder(Long.valueOf(orderId));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/payOrder",method = RequestMethod.POST)
    @ApiOperation(value = "支付订单")
    public Result<Object> payOrder(@RequestBody TbThanks tbThanks, HttpServletRequest request){

        String v=jedisClient.get(CLOSE_PRE);
        if(StrUtil.isNotBlank(v)){
            return new ResultUtil<Object>().setErrorMsg("这么晚啦别支付啦，请于每日7:00am起支付");
        }
        //防炸库验证
        String ip= IPInfoUtil.getIpAddr(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }
        String redisKey="payOrder_"+ip;
        String temp=jedisClient.get(redisKey);
        if(StringUtils.isNotBlank(temp)){
            return new ResultUtil<Object>().setErrorMsg("您提交的太频繁啦，作者的学生服务器要炸啦！请2分钟后再试");
        }

        String result = orderService.payOrder(tbThanks);

        //记录缓存
        jedisClient.set(redisKey,"ADDED");
        jedisClient.expire(redisKey,120);
        return new ResultUtil<Object>().setData(result);
    }
}
