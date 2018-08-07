package com.cnsunrun.jiajiagou.personal.setting.address;

import com.yiguo.adressselectorlib.CityInterface;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 15:36.
 */

public class AreaBean implements CityInterface
{
    /**
     * id : 506
     * pid : 505
     * title : 新抚区
     * level : 2
     * path : -0-471-505-
     * first_char : X
     * pinyin : xinfuqu
     */

    private String id;
    private String pid;
    private String title;
    private String level;
    private String path;
    private String first_char;
    private String pinyin;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getFirst_char()
    {
        return first_char;
    }

    public void setFirst_char(String first_char)
    {
        this.first_char = first_char;
    }

    public String getPinyin()
    {
        return pinyin;
    }

    public void setPinyin(String pinyin)
    {
        this.pinyin = pinyin;
    }

    @Override
    public String getCityName()
    {
        return title;
    }
}
