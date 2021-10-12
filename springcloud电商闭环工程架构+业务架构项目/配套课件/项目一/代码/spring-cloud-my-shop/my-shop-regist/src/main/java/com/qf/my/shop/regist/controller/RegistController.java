package com.qf.my.shop.regist.controller;

import com.qf.common.constant.RedisConstant;
import com.qf.common.dto.ResultBean;
import com.qf.my.shop.regist.common.vo.TUserVO;
import com.qf.my.shop.regist.service.IRegistService;
import com.qf.my.shop.regist.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/regist")
public class RegistController {

    @Autowired
    private IRegistService registService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/show")
    public String show(Model model) {
        model.addAttribute("username", "张三");
        return "register";
    }

    @GetMapping("/sms/code")
    @ResponseBody
    public ResultBean getSmsCode(String phone) {
        return registService.getSmsCode(phone);
    }

    @RequestMapping("/action")
    public String registAction(TUserVO user){

        //1.获得用户手机号对应的验证码——从redis——要去访问数据访问层
        String phone = user.getPhone();
        //1-1 组织redis的键
        String redisKey = StringUtil.getRedisKey(RedisConstant.REGIST_PRE, phone);

        //2.redis中的验证码与user中的验证码进行比对
        String url = "http://localhost:9202/cache/data/get?key={key}";
        Map<String,Object> map = new HashMap<>();
        map.put("key",redisKey);
        ResultBean resultBean = restTemplate.getForObject(url, ResultBean.class, map);
        System.out.println(resultBean);

        //3.根据不同的结果返回不同的内容



        return "";
    }

}
