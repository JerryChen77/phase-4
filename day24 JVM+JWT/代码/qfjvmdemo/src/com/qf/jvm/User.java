package com.qf.jvm;

import java.util.List;

public class User {


    private List list;

    private int id;

    private String name;

    public User(List list, int id, String name) {
        this.list = list;
        this.id = id;
        this.name = name;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        if(id%10==0){
            list.add(this);
            System.out.println("对象"+id+"没有被回收");
        }else{
            System.out.println("对象"+id+"被回收");
        }

    }
}
