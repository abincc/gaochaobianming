package com.cnsunrun.jiajiagou.home.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by j2yyc on 2018/1/29.
 */

public class RecommentThreadBean extends BaseResp {

    /**
     * status : 1
     * info : [{"id":"192","member_id":"64","member_nickname":"论坛管理员","title":"海鲜螃蟹粥","views":"25","replies":"0",
     * "likes":"0","add_time":"2017-11-03 11:35","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1517196989","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg","width":"500","height":"367"},{"id":"194",
     * "member_id":"64","member_nickname":"论坛管理员","title":"抗癌佳品，罗汉果雪梨汤。","views":"7","replies":"0","likes":"0",
     * "add_time":"2017-11-03 11:55","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1517196989","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe8b64ebef.jpg","width":"373","height":"230"},{"id":"185",
     * "member_id":"64","member_nickname":"肖琼","title":"丝瓜排骨汤","views":"15","replies":"0","likes":"0",
     * "add_time":"2017-11-02 08:40","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1517196989","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-02/59fa6994e57fd.jpg","width":"290","height":"377"},{"id":"184",
     * "member_id":"64","member_nickname":"肖琼","title":"雪梨百合山药粥","views":"28","replies":"0","likes":"0",
     * "add_time":"2017-11-02 08:34","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1517196989","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-02/59fa6823aeae1.jpg","width":"318","height":"296"},{"id":"181",
     * "member_id":"67","member_nickname":"音音","title":"滋补鸽子汤","views":"33","replies":"0","likes":"1",
     * "add_time":"2017-10-31 11:00","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1517196989","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-10-31/59f7e7500dac9.jpg","width":"401","height":"257"}]
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
         * id : 192
         * member_id : 64
         * member_nickname : 论坛管理员
         * title : 海鲜螃蟹粥
         * views : 25
         * replies : 0
         * likes : 0
         * add_time : 2017-11-03 11:35
         * avatar : http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1517196989
         * image : http://test.cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg
         * width : 500
         * height : 367
         */

        private String id;
        private String member_id;
        private String member_nickname;
        private String title;
        private String views;
        private String replies;
        private String likes;
        private String add_time;
        private String avatar;
        private String image;
        private String width;
        private String height;

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

        public String getMember_nickname() {
            return member_nickname;
        }

        public void setMember_nickname(String member_nickname) {
            this.member_nickname = member_nickname;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }
}
