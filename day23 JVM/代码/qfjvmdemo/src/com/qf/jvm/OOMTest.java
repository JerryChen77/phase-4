package com.qf.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OOMTest {

    public static void main(String[] args) {

        List<Object> list = new ArrayList<>();

        int i = 0;

        int j = 1000;

        while (true) {

            list.add(new User(i++,UUID.randomUUID().toString()));

            new User(list,j++,UUID.randomUUID().toString());

        }
    }
}