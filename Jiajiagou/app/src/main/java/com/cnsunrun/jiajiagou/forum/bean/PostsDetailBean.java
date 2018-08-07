package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-01 10:31
 */
public class PostsDetailBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : {"id":"1","forum_id":"7","member_id":"7","member_nickname":"倪映冬",
     * "title":"解锁小米手机旅行拍照的秘籍","content":"我是一个坐不住的人。\r\n一天不出门走动，坐立难安。\r\n半年不出远门游玩，浑身难受。\r\n\r\n
     * 我也是一个懒癌患者。\r\n之前出去游玩，还能带上相机，背包，行李，甚至拿上吉他。\r\n而现在越来越懒，一个手机一个空荡荡的包，说走就走。\r\n\r\n
     * 我更是一个喜欢拍照的人。\r\n一路玩一路拍。\r\n\r\n无论拍的好坏，无论用的什么设备，无论在什么时候。\r\n我都不愿去删。\r\n\r\n
     * 因为每一张照片，我每次再看到它的时候，就能想起当时按下快门时的心情。","views":"52","replies":"4","likes":"1",
     * "lastpost_time":"2017-08-24 10:40","add_time":"2017-08-24 10:14","forum_title":"灌者为王",
     * "avatar":"http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
     * .jpg/time/1504233036","image_list":[{"id":"1","thread_id":"1","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369552488.jpg"},{"id":"2",
     * "thread_id":"1","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369555619.jpg"},{"id":"3",
     * "thread_id":"1","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369557b1b.jpg"}],"is_like":"0"}
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
         * id : 1
         * forum_id : 7
         * member_id : 7
         * member_nickname : 倪映冬
         * title : 解锁小米手机旅行拍照的秘籍
         * content : 我是一个坐不住的人。
         一天不出门走动，坐立难安。
         半年不出远门游玩，浑身难受。

         我也是一个懒癌患者。
         之前出去游玩，还能带上相机，背包，行李，甚至拿上吉他。
         而现在越来越懒，一个手机一个空荡荡的包，说走就走。

         我更是一个喜欢拍照的人。
         一路玩一路拍。

         无论拍的好坏，无论用的什么设备，无论在什么时候。
         我都不愿去删。

         因为每一张照片，我每次再看到它的时候，就能想起当时按下快门时的心情。
         * views : 52
         * replies : 4
         * likes : 1
         * lastpost_time : 2017-08-24 10:40
         * add_time : 2017-08-24 10:14
         * forum_title : 灌者为王
         * avatar : http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
         * .jpg/time/1504233036
         * image_list : [{"id":"1","thread_id":"1","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369552488.jpg"},{"id":"2",
         * "thread_id":"1","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369555619.jpg"},{"id":"3",
         * "thread_id":"1","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369557b1b.jpg"}]
         * is_like : 0
         */

        private String id;
        private String forum_id;
        private String member_id;
        private String member_nickname;
        private String title;
        private String content;
        private String views;
        private String replies;
        private String likes;
        private String lastpost_time;
        private String add_time;
        private String forum_title;
        private String avatar;
        private String is_like;
        private List<ImageListBean> image_list;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getLastpost_time() {
            return lastpost_time;
        }

        public void setLastpost_time(String lastpost_time) {
            this.lastpost_time = lastpost_time;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public List<ImageListBean> getImage_list() {
            return image_list;
        }

        public void setImage_list(List<ImageListBean> image_list) {
            this.image_list = image_list;
        }

        public static class ImageListBean {
            /**
             * id : 1
             * thread_id : 1
             * image : http://test.cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-08-24/599e369552488.jpg
             */

            private String id;
            private String thread_id;
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getThread_id() {
                return thread_id;
            }

            public void setThread_id(String thread_id) {
                this.thread_id = thread_id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}
