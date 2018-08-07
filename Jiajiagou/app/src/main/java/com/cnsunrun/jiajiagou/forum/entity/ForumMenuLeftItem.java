package com.cnsunrun.jiajiagou.forum.entity;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-25 18:05
 */
public class ForumMenuLeftItem {
    private int id;
    private String title;
    private boolean isSelect=false;//记录点击的item

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
