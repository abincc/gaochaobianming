package com.cnsunrun.jiajiagou.map.bean;

import java.io.Serializable;

/**
 * 定位结果bean类
 * <p>
 * author:yyc
 * date: 2017-09-15 16:52
 */
public class LocationBean  implements Serializable {

    private double lon;
    private double lat;
    private String title;
    private String content;

    public LocationBean(double lon,double lat,String title,String content){
        this.lon = lon;
        this.lat = lat;
        this.title = title;
        this.content = content;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
