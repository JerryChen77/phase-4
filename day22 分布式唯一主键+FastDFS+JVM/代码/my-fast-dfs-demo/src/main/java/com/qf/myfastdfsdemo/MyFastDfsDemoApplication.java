package com.qf.myfastdfsdemo;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)
@SpringBootApplication
public class MyFastDfsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFastDfsDemoApplication.class, args);
    }

}
