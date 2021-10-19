package com.elco.system.platform.resources.config;

/**
 * @author kay
 * @date 2021/9/3
 */
public enum AllStatus {
    STATUS1("报警"),
    STATUS2("预警"),
    STATUS3("正常");

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    AllStatus(String msg) {
        this.msg=msg;
    }
}
