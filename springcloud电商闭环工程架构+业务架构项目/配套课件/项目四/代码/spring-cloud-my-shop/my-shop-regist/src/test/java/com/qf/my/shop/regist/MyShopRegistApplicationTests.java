package com.qf.my.shop.regist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MyShopRegistApplicationTests {


    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void test2(){

        System.out.println(encoder.matches("123456", "$2a$10$PJFVx7jUkrhWftsCnj5Knu./6XAwDLRRRA3t.mtUddBndPTnyB7N6"));
        System.out.println(encoder.matches("123456","$2a$10$6E1C.77Tg7bytqEKl9L/Hu4kP2P7ke1rFjtke1UBYHFx0d.yGYh3G"));
    }

    @Test
    public void test1(){

        //加密
        String pass = "123456";
        String encode = encoder.encode(pass);
        System.out.println(encode);
        //$2a$10$PJFVx7jUkrhWftsCnj5Knu./6XAwDLRRRA3t.mtUddBndPTnyB7N6
        System.out.println("$2a$10$6E1C.77Tg7bytqEKl9L/Hu4kP2P7ke1rFjtke1UBYHFx0d.yGYh3G".length());

    }


    @Test
    void contextLoads() {

        int  code = (int) (Math.random()*10000);
        System.out.println(code);

    }

}
