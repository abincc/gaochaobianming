package com.cnsunrun.jiajiagou.forum.bean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-29 13:30
 */
public class LoginBean {


    /**
     * status : 1
     * msg : OK
     * info : {"member_id":"19","type":"2","username":"yyc","nickname":"123",
     * "mobile":"15623251464","sex":"1","district_id":"5",
     * "ticket":"fnVyqrJ3dLCudXOuhHp1a4V1eaqDtpingnfN2bKbqsl-hXJps4eeb66re3Q",
     * "headimg":"http://test.cnsunrun.com/wuye/Uploads/Headimg/000/00/00/H_19_M
     * .jpg?time=1506049172","district_title":"琴台社区"}
     */

    private int status;
    private String msg;
    private InfoBean info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * member_id : 19
         * type : 2
         * username : yyc
         * nickname : 123
         * mobile : 15623251464
         * sex : 1
         * district_id : 5
         * ticket : fnVyqrJ3dLCudXOuhHp1a4V1eaqDtpingnfN2bKbqsl-hXJps4eeb66re3Q
         * headimg : http://test.cnsunrun.com/wuye/Uploads/Headimg/000/00/00/H_19_M
         * .jpg?time=1506049172
         * district_title : 琴台社区
         */

        private String member_id;
        private String type;
        private String username;
        private String nickname;
        private String mobile;
        private String sex;
        private String district_id;
        private String ticket;
        private String headimg;
        private String district_title;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(String district_id) {
            this.district_id = district_id;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getDistrict_title() {
            return district_title;
        }

        public void setDistrict_title(String district_title) {
            this.district_title = district_title;
        }
    }
}
