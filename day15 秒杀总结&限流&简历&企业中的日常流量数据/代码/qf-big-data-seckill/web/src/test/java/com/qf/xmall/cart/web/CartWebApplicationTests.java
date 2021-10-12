package com.qf.bigdata.view.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class viewWebApplicationTests {

    @Test
    void contextLoads() {
        User user = new User();
        User user1 = new User();
        System.out.println(user.equals(user1));


    }

}
