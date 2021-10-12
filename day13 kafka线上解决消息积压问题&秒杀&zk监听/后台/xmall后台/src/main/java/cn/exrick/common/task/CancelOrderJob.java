package cn.exrick.common.task;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.mapper.TbOrderItemMapper;
import cn.exrick.mapper.TbOrderMapper;
import cn.exrick.mapper.TbOrderShippingMapper;
import cn.exrick.mapper.TbThanksMapper;
import cn.exrick.pojo.*;
import cn.exrick.service.OrderService;
import cn.exrick.utils.EmailUtil;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Exrickx
 */
@Component
public class CancelOrderJob {

    final static Logger log= LoggerFactory.getLogger(CancelOrderJob.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private TbThanksMapper tbThanksMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JedisClient jedisClient;

    private static final String CLOSE_PRE = "XMALL_CLOSE_PRE";

    /**
     * 每12个小时判断订单是否失效
     */
    @Scheduled(cron = "0 0 */12 * * ?")
    public void setOrder() {

        log.info("开始执行自动取消订单定时任务");
        orderService.cancelOrder();
    }

    /**
     * 每月1号自动删除订单
     */
    @Scheduled(cron = "0 0 0 1 * ? ")
    public void deleteOrder() {

        log.info("开始执行自动删除订单定时任务");
        tbOrderMapper.deleteByExample(new TbOrderExample());
        tbOrderItemMapper.deleteByExample(new TbOrderItemExample());
        tbOrderShippingMapper.deleteByExample(new TbOrderShippingExample());
    }

    /**
     * 每日2am清空除捐赠和审核中以外的数据
     */
    @Scheduled(cron="0 0 2 * * ?")
    public void cronJob(){

        TbThanksExample example = new TbThanksExample();
        TbThanksExample.Criteria criteria= example.createCriteria();
        criteria.andStateEqualTo(2);
        try {
            tbThanksMapper.deleteByExample(example);
        }catch (Exception e){
            log.error("定时删除未支付成功数据失败");
        }

        //设置未审核数据为支付失败
        TbThanksExample example1 = new TbThanksExample();
        TbThanksExample.Criteria criteria1= example1.createCriteria();
        criteria1.andStateEqualTo(0);
        List<TbThanks> list = tbThanksMapper.selectByExample(example1);
        for(TbThanks p:list){
            p.setState(2);
            try {
                tbThanksMapper.updateByPrimaryKey(p);
            }catch (Exception e){
                log.error("设置未审核数据"+p.getId()+"为支付失败出错");
            }
        }

        log.info("定时执行设置未审核数据为支付失败完毕");
    }

    /**
     * 每日10am统一发送支付失败邮件
     */
    @Scheduled(cron="0 0 10 * * ?")
    public void sendEmailJob(){

        TbThanksExample example = new TbThanksExample();
        TbThanksExample.Criteria criteria= example.createCriteria();
        criteria.andStateEqualTo(2);
        List<TbThanks> list = tbThanksMapper.selectByExample(example);
        for(TbThanks p:list){
            p.setTime(DateUtil.formatTime(p.getDate()));
            if(StringUtils.isNotBlank(p.getEmail())&& EmailUtil.checkEmail(p.getEmail())) {
                emailUtil.sendTemplateEmail(p.getEmail(),"【XMall商城】支付失败通知", "pay-fail",p);
            }
        }

        log.info("定时执行统一发送支付失败邮件完毕");
    }

    /**
     * 每日1am关闭系统
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void closePay(){

        jedisClient.set(CLOSE_PRE, "CLOSED");
        jedisClient.expire(CLOSE_PRE, 21600);
        log.info("定时关闭系统成功");
    }

    /**
     * 每日7am开启系统
     */
    @Scheduled(cron="0 0 7 * * ?")
    public void openPay(){

        jedisClient.del(CLOSE_PRE);
        log.info("定时开启系统成功");
    }
}
