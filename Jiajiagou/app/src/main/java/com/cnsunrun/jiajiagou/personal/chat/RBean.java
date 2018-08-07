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
 * desc: 房间信息 Bean
 */

public class RBean extends BaseResp {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private int id;
        private int mid;
        private int oid;
        private int is_del;
        private String time;

        public int getId(){ return id; }
        public void setId(int id){ this.id = id; }

        public int getMid(){ return mid; }
        public void setMid(int id){ this.mid = mid; }

        public int getOid(){ return oid; }
        public void setOid(int oid){ this.oid = oid; }

        public int getIs_del(){ return is_del; }
        public void setIs_del(int is_del){ this.is_del = is_del; }

        public String getTime(){ return  time; }
        public void setTime(String time){ this.time = time; }
    }
}
