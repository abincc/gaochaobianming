package com.cnsunrun.jiajiagou.common.wxpay;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-27 14:53
 */
public class WechatModel {
    private String out_trade_no;
    private String money;
    private String name;
    private String detail;

    public WechatModel(String out_trade_no, String money, String name, String detail) {
        this.out_trade_no = out_trade_no;
        this.money = money;
        this.name = name;
        this.detail = detail;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
