package com.cnsunrun.jiajiagou.personal.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/***
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the more protecting ━━━
 * <p/>
 * Created by abincc on 2018/06/05
 * desc: 关于附近的人 Bean
 */

public class NearByBean extends BaseResp {

    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {

        private int member_id;
        private String mobile;
        private String nickname;
        private int sex;
        private String headimg;
        private int district_id;
        private Msg msg;

        public int getMember_id(){ return member_id; }
        public void setMember_id(int member_id){ this.member_id = member_id; }

        public String getMobile(){ return  mobile; }
        public void setMobile(String mobile){ this.mobile = mobile; }

        public String getNickname(){ return  nickname; }
        public void setNickname(String nickname){ this.nickname = nickname; }

        public int getSex(){ return sex;}
        public void setSex(int sex){ this.sex = sex; }

        public String getHeadimg(){return headimg; }
        public void setHeadimg(String headimg){ this.headimg = headimg; }

        public int getDistrict_id(){ return district_id;}
        public void setDistrict_id(int district_id){ this.district_id = district_id; }

        public Msg getMsg(){ return msg; }
        public void setMsg(Msg msg){ this.msg = msg; }

        public static class Msg {
            private int id;
            private String content;

            public int getId(){ return  id;}
            public void setId(int id){ this.id = id;}

            public String getContent(){ return  content; }
            public void setContent(String content){ this.content = content; }

        }
    }
}
