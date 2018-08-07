package com.cnsunrun.jiajiagou.personal.chat;

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
 * desc: 聊天信息 Bean
 */

public class MessageBean extends BaseResp {
    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {

        private int id;
        private int type;
        private int rid;
        private int uid;
        private int is_back;
        private int is_del;
        private String content;
        private String time;

        public int getId(){ return id; }
        public void setId(int id){ this.id = id; }

        public int getType(){ return  type; }
        public void setType(int type){ this.type = type; }

        public int getRid(){ return  rid; }
        public void setRid(int rid){ this.rid = rid; }

        public int getUid(){ return  uid; }
        public void setUid( int uid){ this.uid = uid; }

        public int getIs_back(){ return  is_back; }
        public void setIs_back(int is_back){ this.is_back = is_back; }

        public int getIs_del(){ return is_del; }
        public void setIs_del(int is_del){ this.is_del = is_del; }

        public String getContent(){ return  content; }
        public void setContent(String content){ this.content = content; }

        public String getTime(){ return  time; }
        public void setTime(String time){ this.time = time; }
    }
}
