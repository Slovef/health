package com.itheima.utils;

import com.itheima.entity.Result;

/**
 * 用于处理handler传来的结果,返回Pojo结果对象回去
 */
public class HandleResult {

    //返回Result结果类型
    public static Result getResult(Boolean flag,String message) {
        return new Result(flag,message);
    }
    public static Result getResult(Boolean flag,String message,Object data) {
        return new Result(flag,message,data);
    }



}
