package com.qf.myleafclientdemo;

import com.qf.myleafclientdemo.util.OkHttpClientUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

@SpringBootTest
class MyLeafClientDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 注意我这里写死了获取 Leaf 地址，只是为了方便演示
     */
    private static final String LEAF_HOST = "http://172.16.253.47:8080/api/snowflake/get/test";

    /**
     * 生成 ID
     *
     * @return {@code Long} 雪花 ID
     */
    public Long genId() {
        try {
            String string = Objects.requireNonNull(OkHttpClientUtil.getInstance().getData(LEAF_HOST).body()).string();
            return Long.valueOf(string);
        } catch (IOException e) {
            return 0L;
        }
    }

    /**
     * 测试
     *
     */
    @Test
    public void testLeaf() {
        for (int i = 0; i < 100; i++) {
            System.out.println(genId());
        }
    }





}
