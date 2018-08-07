package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 18:28
 */
public class TabPostsBean extends BaseResp {

    /**
     * status : 1
     * msg : ok
     * info : [{"id":"1","forum_id":"7","title":"解锁小米手机旅行拍照的秘籍","views":"415","replies":"39",
     * "likes":"4","add_time":"2017-08-24 10:14","forum_title":"灌者为王","forum_icon":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"}]
     */

    private List<InfoBean> info;


    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 1
         * forum_id : 7
         * title : 解锁小米手机旅行拍照的秘籍
         * views : 415
         * replies : 39
         * likes : 4
         * add_time : 2017-08-24 10:14
         * forum_title : 灌者为王
         * forum_icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png
         */

        private String id;
        private String forum_id;
        private String title;
        private String views;
        private String replies;
        private String likes;
        private String add_time;
        private String forum_title;
        private String forum_icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getForum_id() {
            return forum_id;
        }

        public void setForum_id(String forum_id) {
            this.forum_id = forum_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getReplies() {
            return replies;
        }

        public void setReplies(String replies) {
            this.replies = replies;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getForum_title() {
            return forum_title;
        }

        public void setForum_title(String forum_title) {
            this.forum_title = forum_title;
        }

        public String getForum_icon() {
            return forum_icon;
        }

        public void setForum_icon(String forum_icon) {
            this.forum_icon = forum_icon;
        }
    }
}
