package com.cnsunrun.jiajiagou.forum.bean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-29 13:05
 */
public class RegistBean {


    /**
     * status : 0
     * msg : 密码格式不正确（8-30位必须包含数字字母）
     */

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
