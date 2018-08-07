package com.cnsunrun.jiajiagou.personal.chat;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

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

public class RLBean extends BaseResp {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private Me me;
        private Other other;

        public Me getMe(){ return  me; }
        public void setMe(Me me){ this.me = me;}

        public Other getOther(){ return other; }
        public void setOther(Other other){ this.other = other; }

        public static class Me{
            private String id;
            private String nickname;
            private String mobile;
            private String headimg;

            public String getId(){ return id; }
            public void setId(String id){ this.id = id; }

            public String getNickname(){return nickname; }
            public void setNickname(String nickname){ this.nickname = nickname; }

            public String getMobile(){ return mobile; }
            public void setMobile(String mobile){ this.mobile = mobile; }

            public String getHeadimg(){ return  headimg; }
            public void setHeadimg(String headimg){ this.headimg = headimg; }
        }

        public static class Other{
            private String id;
            private String nickname;
            private String mobile;
            private String headimg;

            public String getId(){ return id; }
            public void setId(String id){ this.id = id; }

            public String getNickname(){return nickname; }
            public void setNickname(String nickname){ this.nickname = nickname; }

            public String getMobile(){ return mobile; }
            public void setMobile(String mobile){ this.mobile = mobile; }

            public String getHeadimg(){ return  headimg; }
            public void setHeadimg(String headimg){ this.headimg = headimg; }
        }


    }
}
