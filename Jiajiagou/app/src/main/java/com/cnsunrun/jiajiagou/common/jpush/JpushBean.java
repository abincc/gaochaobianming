package com.cnsunrun.jiajiagou.common.jpush;

/**
 * Created by ${LiuDi}
 * on 2017/10/13on 10:34.
 */

public class JpushBean
{
    /**
     * type : 2
     * id : 342
     */

    private int type;
    private int id;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    /**
     *  聊天信息字段
     */

    private String rid;
    private String mid;
    private String content;
    private String time;

    public String getRid(){return  rid; }
    public void setRid(String rid){ this.rid = rid; }

    public String getMid(){ return  mid; }
    public void setMid(String mid){ this.mid = mid; }

    public String getContent(){ return content; }
    public void setContent(){ this.content = content; }

    public String getTime(){ return time; }
    public void setContent(String time){ this.time = time; }

}
