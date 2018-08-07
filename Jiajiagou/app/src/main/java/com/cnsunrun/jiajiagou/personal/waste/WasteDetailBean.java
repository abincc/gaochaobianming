package com.cnsunrun.jiajiagou.personal.waste;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 16:23
 */
public class WasteDetailBean extends BaseResp {


    private List<InfoBean> info;

    public List<InfoBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<InfoBean> info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        private int id;
        private String image;
        private int pid;

        public void setId(int id){ this.id = id;}

        public int getId(){return id;}

        public void setPid(int pid){ this.pid = pid;}

        public int getPid(){return pid;}

        public void setImage(String image){ this.image = image;}

        public String getImage(){ return image;}
    }
}
