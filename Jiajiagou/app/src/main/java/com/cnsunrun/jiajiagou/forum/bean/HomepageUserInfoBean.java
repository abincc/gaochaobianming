package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-02 18:07
 */
public class HomepageUserInfoBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : {"id":"7","nickname":"倪映冬","signature":"","register_time":"2017-08-24 09:36:10",
     * "login_time":"2017-09-01 14:24:42","threads":"1","posts":"0","likes":"0",
     * "avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1504346837"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 7
         * nickname : 倪映冬
         * signature :
         * register_time : 2017-08-24 09:36:10
         * login_time : 2017-09-01 14:24:42
         * threads : 1
         * posts : 0
         * likes : 0
         * avatar : http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1504346837
         */

        private String id;
        private String nickname;
        private String signature;
        private String register_time;
        private String login_time;
        private String threads;
        private String posts;
        private String likes;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getThreads() {
            return threads;
        }

        public void setThreads(String threads) {
            this.threads = threads;
        }

        public String getPosts() {
            return posts;
        }

        public void setPosts(String posts) {
            this.posts = posts;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
