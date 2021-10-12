package com.qf.myshardingjdbcdemo;

import com.qf.myshardingjdbcdemo.entity.TbOrder;
import com.qf.myshardingjdbcdemo.mapper.TbOrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyShardingJdbcDemoApplicationTests {

  @Test
  void contextLoads() {
  }


  @Autowired
  private TbOrderMapper tbOrderMapper;

  @Test
  public void testInsert1() {
    TbOrder tbOrder = new TbOrder();
    tbOrder.setId(2L);
    tbOrder.setUserId(1234L);
    tbOrder.setOrderId(1002L);
    tbOrderMapper.insert(tbOrder);

  }

//  @Test
//  public void testInsert2() {
//    // 按照配置文件中的规则
//    TOrder order = new TOrder();
//    // userId 为偶数进偶数库即 myshop_0
//    order.setUserId(2L);
//    // orderId 为偶数进偶数表即 tb_order_0
//    order.setOrderId(2L);
//    // 此时数据应插入 myshop_0.tb_order_0 中
//    orderMapper.insert(order);
//  }
//
//  @Test
//  public void testSelectAll() {
//    List<TbOrder> tOrders = TbOrderMapper.selectAll();
//    tOrders.forEach(tOrder -> {
//      System.out.println(tOrder);
//    });
//  }

}
