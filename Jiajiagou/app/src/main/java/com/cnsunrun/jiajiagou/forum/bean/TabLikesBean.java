package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-15 11:07
 */
public class TabLikesBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : [{"id":"1","title":"解锁小米手机旅行拍照的秘籍","forum_id":"7","add_time":"2017-08-24 10:14",
     * "forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"5","title":"发帖功能测试",
     * "forum_id":"7","add_time":"2017-09-06 09:50","forum_title":"灌者为王",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"}]
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
         * title : 解锁小米手机旅行拍照的秘籍
         * forum_id : 7
         * add_time : 2017-08-24 10:14
         * forum_title : 灌者为王
         * forum_icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png
         */

        private String id;
        private String title;
        private String forum_id;
        private String add_time;
        private String forum_title;
        private String forum_icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getForum_id() {
            return forum_id;
        }

        public void setForum_id(String forum_id) {
            this.forum_id = forum_id;
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
