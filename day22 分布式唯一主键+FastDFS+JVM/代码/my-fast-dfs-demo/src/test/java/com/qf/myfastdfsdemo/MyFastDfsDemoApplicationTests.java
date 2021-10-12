package com.qf.myfastdfsdemo;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class MyFastDfsDemoApplicationTests {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${image.server}")
    private String imageServer;

    @Test
    public void testUploadImage() throws FileNotFoundException {


        String path = "/Users/zeleishi/Desktop/my-fast-dfs-demo/src/main/resources/images/tu1.jpeg";

        File img = new File(path);

        FileInputStream fileInputStream = new FileInputStream(img);

        //执行文件上传
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                fileInputStream,//输入流
                img.length(),//文件的大小
                getExtFileName(path),//扩展名
                null
        );
        //打印结果
        System.out.println(imageServer+storePath.getFullPath());
        System.out.println(storePath.getGroup());
        System.out.println(storePath.getPath());




    }

    private String getExtFileName(String path) {
        return path.substring(path.lastIndexOf('.')+1);
    }


}
