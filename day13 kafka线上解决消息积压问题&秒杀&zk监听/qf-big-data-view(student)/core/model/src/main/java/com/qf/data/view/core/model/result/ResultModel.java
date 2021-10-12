package com.qf.data.view.core.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ResultModel<T> implements Serializable {

    private int code;//1000 成功
    private String message;
    private T data;

    private static ResultModel ResultModel = new ResultModel();

    public static ResultModel success(){
        ResultModel.setCode(1000);
        ResultModel.setMessage("success");
        ResultModel.setData(null);
        return ResultModel;
    }

    public static ResultModel success(String message){
        ResultModel.setCode(1000);
        ResultModel.setMessage(message);
        ResultModel.setData(null);
        return ResultModel;
    }

    public static ResultModel success(String message,Object data){
        ResultModel.setCode(1000);
        ResultModel.setMessage(message);
        ResultModel.setData(data);
        return ResultModel;
    }

    public static ResultModel success(Object data){
        ResultModel.setCode(1000);
        ResultModel.setMessage("success");
        ResultModel.setData(data);
        return ResultModel;
    }


    public static ResultModel error(){
        ResultModel.setCode(500);
        ResultModel.setMessage("error");
        ResultModel.setData(null);
        return ResultModel;
    }

    public static ResultModel error(String message){
        ResultModel.setCode(500);
        ResultModel.setMessage(message);
        ResultModel.setData(null);
        return ResultModel;
    }




}
