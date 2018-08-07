package com.cnsunrun.jiajiagou.video.ys7;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/30on 16:33.
 */

public class DeviceListResp extends BaseResp

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

//    "id": "101",
//            "uid": "101",
//            "district_id": "22",
//            "member_id": "0",
//            "ezopen": "ezopen://open.ys7.com/C04910992/1.hd.live",
//            "title": "测试账号",
//            "remark": "测试账号",
//            "is_hide": "0",
//            "is_del": "0",
//            "update_time": "2018-07-03 08:11:21",
//            "add_time": "2018-07-03 08:11:21",
//            "video": {
//    "id": "101",
//            "title": "测试账号",
//            "appkey": "7a86047b6a174d4692dded53232139d2",
//            "access_token": "at.582plf4vdgmbwbk54yvdd7u19hsbymb8-2a2jx5od5j-0wink98-aniuv2n2s",
//            "appsecret": "07e390489e672649fe0aef3541d5a65f"
//}

    public static class InfoBean
    {
        public int id;
        public int uid;
        public int district_id;
        public int member_id;
        public String ezopen;
        public String title;
        public String image;
        public String remark;
        public String update_time;
        public Video video;

        public int getId(){return id; }
        public void setId(int id){this.id = id; }

        public int getUid(){ return uid; }
        public void setUid(int uid){ this.uid = uid; }

        public int getDistrict_id(){return district_id; }
        public void setDistrict_id(int district_id){ this.district_id = district_id; }

        public int getMember_id(){return member_id; }
        public void setMember_id(int member_id){this.member_id = member_id; }

        public String getEzopen(){return  ezopen; }
        public void setEzopen(String ezopen){this.ezopen = ezopen; }

        public String getTitle(){ return title; }
        public void setTitle(String title){ this.title = title; }

        public String getImage(){ return image; }
        public void setImage(String image){ this.image = image; }

        public String getRemark(){ return  remark; }
        public void setRemark(String remark){ this.remark = remark; }

        public String getUpdate_time(){ return  update_time; }
        public void setUpdate_time(String update_time){ this.update_time = update_time; }

        public Video getVideo(){ return  video;}
        public void setVideo(Video video){ this.video = video; }

        public static class Video {
            public int id;
            public String title;
            public String appkey;
            public String access_token;
            public String appsecret;

            public int getId(){ return id; }
            public void setId(int id){  this.id = id; }

            public String getTitle(){ return title; }
            public void setTitle(String title){ this.title = title; }

            public String getAppkey(){ return appkey; }
            public void setAppkey(String appkey){ this.appkey = appkey; }

            public String getAccess_token(){ return access_token; }
            public void setAccess_token(String access_token){ this.access_token = access_token; }

            public String getAppsecret(){ return appsecret; }
            public void setAppsecret(String appsecret){ this.appsecret = appsecret; }
        }
    }
}
