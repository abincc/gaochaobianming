package com.cnsunrun.jiajiagou.personal.bean;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:03
 */
public class ForumMessageBean {

    /**
     * status : 1
     * msg : ok
     * info : [{"id":"8","member_id":"19","type":"2","from_id":"22","from_name":"napping",
     * "thread_id":"5","post_id":"0","data_id":"62","thread_title":"发帖功能测试","add_time":"7DD",
     * "center":"赞了您的帖子","ending":"","is_red":"1"}]
     */

    private int status;
    private String msg;
    private List<InfoBean> info;

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

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 8
         * member_id : 19
         * type : 2
         * from_id : 22
         * from_name : napping
         * thread_id : 5
         * post_id : 0
         * data_id : 62
         * thread_title : 发帖功能测试
         * add_time : 7DD
         * center : 赞了您的帖子
         * ending :
         * is_red : 1
         */

        private String id;
        private String member_id;
        private String type;
        private String from_id;
        private String from_name;
        private String thread_id;
        private String post_id;
        private String data_id;
        private String thread_title;
        private String add_time;
        private String center;
        private String ending;
        private String is_red;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getFrom_id() {
            return from_id;
        }

        public void setFrom_id(String from_id) {
            this.from_id = from_id;
        }

        public String getFrom_name() {
            return from_name;
        }

        public void setFrom_name(String from_name) {
            this.from_name = from_name;
        }

        public String getThread_id() {
            return thread_id;
        }

        public void setThread_id(String thread_id) {
            this.thread_id = thread_id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getData_id() {
            return data_id;
        }

        public void setData_id(String data_id) {
            this.data_id = data_id;
        }

        public String getThread_title() {
            return thread_title;
        }

        public void setThread_title(String thread_title) {
            this.thread_title = thread_title;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getCenter() {
            return center;
        }

        public void setCenter(String center) {
            this.center = center;
        }

        public String getEnding() {
            return ending;
        }

        public void setEnding(String ending) {
            this.ending = ending;
        }

        public String getIs_red() {
            return is_red;
        }

        public void setIs_red(String is_red) {
            this.is_red = is_red;
        }
    }
}
