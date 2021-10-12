package com.qf.common.dto;

public class ResultBean<T> {

    private int errno; //0 表示成功；1表示失败
    private String message;//信息
    private T object;


    public ResultBean(int errno, String message, T object) {
        this.errno = errno;
        this.message = message;
        this.object = object;
    }

    public ResultBean() {
    }

    public static ResultBean error(String message){
        return new ResultBean(1,message,null);
    }

    public static ResultBean error(){
        return ResultBean.error("error");
    }

    public static ResultBean success(Object obj){
        return ResultBean.success("success",obj);
    }

    public static ResultBean success(String message,Object obj){
        return new ResultBean(0,message,obj);
    }

    public static ResultBean success(){
        return ResultBean.success("success",null);
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
