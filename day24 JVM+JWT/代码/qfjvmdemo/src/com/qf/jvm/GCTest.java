package com.qf.jvm;

import java.util.ArrayList;

public class GCTest {

    byte[] a = new byte[1024*100];//100kb

    public static void main(String[] args) throws InterruptedException {
        ArrayList<GCTest> gcLists = new ArrayList<>();
        while(true){
            gcLists.add(new GCTest());
            Thread.sleep(30);
        }
    }

}
