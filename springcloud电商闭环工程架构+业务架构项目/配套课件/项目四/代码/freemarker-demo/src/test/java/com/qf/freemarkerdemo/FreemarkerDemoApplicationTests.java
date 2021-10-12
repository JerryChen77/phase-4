package com.qf.freemarkerdemo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class FreemarkerDemoApplicationTests {


    @Autowired
    private Configuration configuration;


    @Test
    void contextLoads() throws IOException, TemplateException {

        //获得模板对象（指向“hello.ftl”模板）
        Template template = configuration.getTemplate("hello.ftl");

        //数据
        Map<String,Object> map = new HashMap<>();

        map.put("name","小明");

        //输出 output
        String path = "D:\\2020\\2002\\freemarker-demo\\src\\main\\resources\\static\\";
        Writer out = new FileWriter(path+"hello.html");
        //用模板template+数据map，就会生成一个out输出流指向的那个页面（out输出流会生成一个页面）
        template.process(map,out);




    }

}
