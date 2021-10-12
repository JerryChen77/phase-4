package cn.exrick.controller;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.pojo.TbThanks;
import cn.exrick.pojo.common.Result;
import cn.exrick.service.OrderFrontService;
import cn.exrick.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Exrickx
 */
@RestController
@Api(description = "订单")
public class PayController {

    private final static Logger log= LoggerFactory.getLogger(PayController.class);

    @Autowired
    private OrderFrontService orderService;

    @Autowired
    private JedisClient jedisClient;

    private static final String CLOSE_PRE = "XMALL_CLOSE_PRE";

    @RequestMapping(value = "/pay/pass",method = RequestMethod.GET)
    @ApiOperation(value = "支付审核通过")
    public Result<Object> payPass(String tokenName,String token,String id,String sendType){

        int result=orderService.passPay(tokenName,token,id,sendType);
        if(result==-1){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if(result==0){
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/back",method = RequestMethod.GET)
    @ApiOperation(value = "支付审核驳回")
    public Result<Object> backPay(String tokenName,String token,String id){

        int result=orderService.backPay(tokenName,token,id);
        if(result==-1){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if(result==0){
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/passNotShow",method = RequestMethod.GET)
    @ApiOperation(value = "支付审核通过但不展示")
    public Result<Object> payNotShow(String tokenName,String token,String id){

        int result=orderService.notShowPay(tokenName,token,id);
        if(result==-1){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if(result==0){
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/edit",method = RequestMethod.GET)
    @ApiOperation(value = "支付审核编辑")
    public Result<Object> payEdit(String tokenName,String token,@ModelAttribute TbThanks tbThanks){

        int result=orderService.editPay(tokenName,token,tbThanks);
        if(result==-1){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if(result==0){
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/del",method = RequestMethod.GET)
    @ApiOperation(value = "支付删除")
    public Result<Object> payDel(String tokenName,String token,String id){

        int result=orderService.payDel(tokenName,token,id);
        if(result==-1){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if(result==0){
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/closeForSixHour",method = RequestMethod.GET)
    @ApiOperation(value = "关闭支付")
    public Result<Object> closeForSixHour(String tokenName,String token){

        String value=jedisClient.get(tokenName);
        if(!value.equals(token)){
            return new ResultUtil<Object>().setErrorMsg("无效的token");
        }
        jedisClient.set(CLOSE_PRE, "CLOSED");
        jedisClient.expire(CLOSE_PRE, 21600);
        return new ResultUtil<Object>().setData("关闭支付系统6小时成功");
    }

    @RequestMapping(value = "/pay/open",method = RequestMethod.GET)
    @ApiOperation(value = "开启支付")
    public Result<Object> open(String tokenName,String token){

        String value=jedisClient.get(tokenName);
        if(!value.equals(token)){
            return new ResultUtil<Object>().setErrorMsg("无效的token");
        }
        jedisClient.del(CLOSE_PRE);
        return new ResultUtil<Object>().setData("开启支付系统成功");
    }
}
