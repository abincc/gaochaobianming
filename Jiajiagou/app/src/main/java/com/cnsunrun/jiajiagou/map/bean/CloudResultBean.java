package com.cnsunrun.jiajiagou.map.bean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-16 13:33
 */
public class CloudResultBean {
    private String id;//小区id
    private String title;//小区名
    private boolean isSelect=false;//记录点击的item
    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public CloudResultBean() {
    }

    public CloudResultBean(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
