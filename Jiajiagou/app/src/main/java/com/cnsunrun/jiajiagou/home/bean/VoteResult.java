package com.cnsunrun.jiajiagou.home.bean;

/**
 * Created by j2yyc on 2018/1/27.
 */

public class VoteResult {
    private int agree_number;
    private int disagree_number;
    private int abandon_number;
    private int total;
    private String title;

    public VoteResult(String title,int agree_number, int disagree_number, int abandon_number, int total) {
        this.agree_number = agree_number;
        this.disagree_number = disagree_number;
        this.abandon_number = abandon_number;
        this.total = total;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getAgree_number() {
        return agree_number;
    }

    public void setAgree_number(int agree_number) {
        this.agree_number = agree_number;
    }

    public int getDisagree_number() {
        return disagree_number;
    }

    public void setDisagree_number(int disagree_number) {
        this.disagree_number = disagree_number;
    }

    public int getAbandon_number() {
        return abandon_number;
    }

    public void setAbandon_number(int abandon_number) {
        this.abandon_number = abandon_number;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
