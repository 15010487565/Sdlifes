package com.sdlifes.sdlifes.util;

/**
 * Created by gs on 2018/2/11.
 */

public class EventBusMsg {

    public EventBusMsg() {

    }
    private String msg;
    public EventBusMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
