package com.qf.jvm;

import com.sun.crypto.provider.DESKeyFactory;
import java.lang.String;


public class TestClassLoader {

    public static void main(String[] args) {

        //bootstrap启动类加载器  native
        System.out.println(String.class.getClassLoader());
        //ExtClassLoader扩展类加载器
        System.out.println(DESKeyFactory.class.getClassLoader());
        //AppClassLoader系统类加载器
        System.out.println(TestClassLoader.class.getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());
        //不同的类由不同的类加载器去加载？
        String str = new String();
        str = "abc";
        System.out.println(str);
    }
}
