package com.cnsunrun.jiajiagou.personal.setting;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * Created by ${LiuDi}
 * on 2017/8/29on 17:30.
 */

public class PersonalInfoResp extends BaseResp
{

    /**
     * status : 1
     * info : {"username":"%E7%9A%AE%E4%BB%94","district_id":"3","nickname":"狗子","building_no":"1","room_no":"222",
     * "idcard":"421023199811232933","sex":"2","age":"21","signature":"输入","headimg":"http://test.cnsunrun
     * .com/wuye/Uploads/Headimg/000/00/00/H_13_M.jpg?time=1503998988"}
     */

    private InfoBean info;


    public InfoBean getInfo()
    {
        return info;
    }

    public void setInfo(InfoBean info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        /**
         * username : %E7%9A%AE%E4%BB%94
         * district_id : 3
         * nickname : 狗子
         * building_no : 1
         * room_no : 222
         * idcard : 421023199811232933
         * sex : 2
         * age : 21
         * signature : 输入
         * headimg : http://test.cnsunrun.com/wuye/Uploads/Headimg/000/00/00/H_13_M.jpg?time=1503998988
         */

        private String username;
        private String district_id;
        private String nickname;
        private String building_no;
        private String room_no;
        private String idcard;
        private int sex;
        private String age;
        private String signature;
        private String headimg;
        public String district_title;
        public String register_code;
        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getDistrict_id()
        {
            return district_id;
        }

        public void setDistrict_id(String district_id)
        {
            this.district_id = district_id;
        }

        public String getNickname()
        {
            return nickname;
        }

        public void setNickname(String nickname)
        {
            this.nickname = nickname;
        }

        public String getBuilding_no()
        {
            return building_no;
        }

        public void setBuilding_no(String building_no)
        {
            this.building_no = building_no;
        }

        public String getRoom_no()
        {
            return room_no;
        }

        public void setRoom_no(String room_no)
        {
            this.room_no = room_no;
        }

        public String getIdcard()
        {
            return idcard;
        }

        public void setIdcard(String idcard)
        {
            this.idcard = idcard;
        }

        public int getSex()
        {
            return sex;
        }

        public void setSex(int sex)
        {
            this.sex = sex;
        }

        public String getAge()
        {
            return age;
        }

        public void setAge(String age)
        {
            this.age = age;
        }

        public String getSignature()
        {
            return signature;
        }

        public void setSignature(String signature)
        {
            this.signature = signature;
        }

        public String getHeadimg()
        {
            return headimg;
        }

        public void setHeadimg(String headimg)
        {
            this.headimg = headimg;
        }
    }
}
