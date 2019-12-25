package com.littlez.uiautomator.bean.eventbus;



/**
 * created by xiaozhi
 * <p>
 * Date 2019/1/2
 */
public class EventbusBean {


    public enum TYPE {
        mainActivity, OrderFragment,
    }


    private int type = -1;

    private String log;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
