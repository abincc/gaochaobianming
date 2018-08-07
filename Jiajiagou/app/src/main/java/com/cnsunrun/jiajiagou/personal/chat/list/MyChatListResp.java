package com.cnsunrun.jiajiagou.personal.chat.list;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/30on 16:33.
 */

public class MyChatListResp extends BaseResp

{
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
        public int id;
        public int mid;
        public int oid;
        public int is_del;
        public String time;
        public Member member;
        public Msg msg;

        public int getId(){ return  id; }
        public void setId(int id){ this.id = id;}

        public int getMid(){ return  mid; }
        public void setMid(){ this.mid = mid; }

        public int getOid(){  return  oid; }
        public void setOid(){ this.oid = oid; }

        public int getIs_del(){ return is_del; }
        public void setIs_del(){ this.is_del = is_del; }

        public String getTime(){ return time; }
        public void setTime(){ this.time = time; }

        public Member getMember(){ return member; }
        public void setMember(Member member){ this.member = member;}

        public Msg getMsg(){ return msg; }
        public void setMsg(Msg msg){ this.msg = msg; }

        public static class Member {
            public int id;
            public String nickname;
            public String mobile;
            public String headimg;

            public int getId(){ return id; }
            public void setId(int id){ this.id = id; }

            public String getNickname(){ return  nickname; }
            public void setNickname(String nickname){ this.nickname = nickname; }

            public String getMobile(){ return mobile; }
            public void setMobile(String mobile){ this.mobile = mobile; }

            public String getHeadimg(){ return headimg;}
            public void setHeadimg(String headimg){ this.headimg = headimg; }
        }

        public static class Msg{
            public int id;
            public String content;

            public int getId(){ return  id; }
            public void setId(int id){ this.id = id; }

            public String getContent(){ return content; }
            public void setContent( String content){ this.content = content; }
        }
    }
}
