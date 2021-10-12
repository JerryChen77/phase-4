package com.qf.my.shop.regist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyShopRegistApplicationTests {

    @Test
    void contextLoads() {

        int  code = (int) (Math.random()*10000);
        System.out.println(code);

    }

}
