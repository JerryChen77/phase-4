package cn.exrick.controller;


import cn.exrick.common.jedis.JedisClient;
import cn.exrick.pojo.common.Result;
import cn.exrick.pojo.front.CommonDto;
import cn.exrick.pojo.front.GeetInit;
import cn.exrick.pojo.front.Member;
import cn.exrick.pojo.front.MemberLoginRegist;
import cn.exrick.service.LoginService;
import cn.exrick.service.MemberFrontService;
import cn.exrick.service.RegisterService;
import cn.exrick.utils.GeetestLib;
import cn.exrick.utils.IPInfoUtil;
import cn.exrick.utils.ResultUtil;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

@RestController
@Api(description = "会员注册登录")
public class MemberFrontController {

    private final static Logger log= LoggerFactory.getLogger(MemberFrontController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private MemberFrontService memberFrontService;
    @Autowired
    private JedisClient jedisClient;

    @RequestMapping(value = "/member/geetestInit",method = RequestMethod.GET)
    @ApiOperation(value = "极验初始化")
    public String geetesrInit(HttpServletRequest request){

        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到redis中
        //request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        String key = UUID.randomUUID().toString();
        //jedisClient.set(key,gtServerStatus+"");
        //验证结果先写死
        jedisClient.set(key,"1");
        jedisClient.expire(key,360);

        resStr = gtSdk.getResponseStr();
        GeetInit geetInit = new Gson().fromJson(resStr,GeetInit.class);
        geetInit.setStatusKey(key);
        return new Gson().toJson(geetInit);
    }

    @RequestMapping(value = "/member/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist,
                                HttpServletRequest request){

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String challenge=memberLoginRegist.getChallenge();
        String validate=memberLoginRegist.getValidate();
        String seccode=memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(jedisClient.get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        Member member=new Member();
        /*int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            member=loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
        }
        else {
            // 验证失败
            member.setState(0);
            member.setMessage("验证失败");
        }*/

        //跳过验证
        member=loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/checkLogin",method = RequestMethod.GET)
    @ApiOperation(value = "判断用户是否登录")
    public Result<Member> checkLogin(@RequestParam(defaultValue = "") String token){

        Member member=loginService.getUserByToken(token);
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/loginOut",method = RequestMethod.GET)
    @ApiOperation(value = "退出登录")
    public Result<Object> logout(@RequestParam(defaultValue = "") String token){

        loginService.logout(token);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/member/register",method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    public Result<Object> register(@RequestBody MemberLoginRegist memberLoginRegist,
                                   HttpServletRequest request){

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String challenge=memberLoginRegist.getChallenge();
        String validate=memberLoginRegist.getValidate();
        String seccode=memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(jedisClient.get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            int result=registerService.register(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
            if(result==0){
                return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
            }else if(result==-1){
                return new ResultUtil<Object>().setErrorMsg("用户名密码不能为空");
            }
            return new ResultUtil<Object>().setData(result);
        }
        else {
            // 验证失败
            return new ResultUtil<Object>().setErrorMsg("验证失败");
        }
    }

    @RequestMapping(value = "/member/imgaeUpload",method = RequestMethod.POST)
    @ApiOperation(value = "用户头像上传")
    public Result<Object> imgaeUpload(@RequestBody CommonDto common,
                                      HttpServletRequest request){

        //防炸库验证
        String ip= IPInfoUtil.getIpAddr(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }
        String redisKey="imageUpload_"+ip;
        String temp=jedisClient.get(redisKey);
        if(StringUtils.isNotBlank(temp)){
            return new ResultUtil<Object>().setErrorMsg("您上传的太频繁啦，作者的学生服务器要炸啦！请5分钟后再试");
        }

        String imgPath = memberFrontService.imageUpload(common.getUserId(),common.getToken(),common.getImgData());

        //记录缓存
        jedisClient.set(redisKey,"ADDED");
        jedisClient.expire(redisKey,300);
        return new ResultUtil<Object>().setData(imgPath);
    }
}
