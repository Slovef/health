package com.itheima.exception;

public class CustomException extends Exception {
    private String msg;

    public CustomException() {
    }

    public CustomException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
