package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-01 11:24
 */
public class PostsCommentBean extends BaseResp{


    /**
     * status : 1
     * msg : ok
     * info : {"post_list":[{"id":"1","post_id":"0","member_id":"8","member_nickname":"庞雨琴",
     * "content":"好厉害的样子，好想学。","likes":"0","position":"1楼","post_info":{"id":"1","member_id":"8",
     * "member_nickname":"庞雨琴","content":"好厉害的样子，好想学。","add_time":"2017-08-24 10:22:26"},
     * "add_time":"2017-08-24 10:22","image_list":[{"id":"1","post_id":"4","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/ForumPost/2017-08-24/599e3c90617b3.jpg"}],
     * "avatar":"http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
     * .jpg/time/1504236169","is_like":"0"},{"id":"2","post_id":"1","member_id":"9",
     * "member_nickname":"计香桃","content":"淡然，平凡还是平庸。","likes":"0","position":"2楼",
     * "post_info":{"id":"1","member_id":"8","member_nickname":"庞雨琴","content":"好厉害的样子，好想学。",
     * "add_time":"2017-08-24 10:22:26"},"add_time":"2017-08-24 10:33","image_list":[],
     * "avatar":"http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
     * .jpg/time/1504236169","is_like":"0"},{"id":"3","post_id":"1","member_id":"10",
     * "member_nickname":"童代芙","content":"我觉得可以，问题是怎么做到的捏","likes":"0","position":"3楼",
     * "post_info":{"id":"1","member_id":"8","member_nickname":"庞雨琴","content":"好厉害的样子，好想学。",
     * "add_time":"2017-08-24 10:22:26"},"add_time":"2017-08-24 10:34","image_list":[],
     * "avatar":"http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
     * .jpg/time/1504236169","is_like":"0"},{"id":"4","post_id":"0","member_id":"8",
     * "member_nickname":"庞雨琴","content":"总的来说这些照片拍得很好，但是这个还是需要技术的，不然还要这些专业的摄影师干嘛，还是得努力努力的",
     * "likes":"0","position":"4楼","post_info":[],"add_time":"2017-08-24 10:40",
     * "image_list":[{"id":"1","post_id":"4","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumPost/2017-08-24/599e3c90617b3.jpg"}],"avatar":"http://test
     * .cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M.jpg/time/1504236169","is_like":"0"}]}
     */


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private List<PostListBean> post_list;

        public List<PostListBean> getPost_list() {
            return post_list;
        }

        public void setPost_list(List<PostListBean> post_list) {
            this.post_list = post_list;
        }

        public static class PostListBean {
            /**
             * id : 1
             * post_id : 0
             * member_id : 8
             * member_nickname : 庞雨琴
             * content : 好厉害的样子，好想学。
             * likes : 0
             * position : 1楼
             * post_info : {"id":"1","member_id":"8","member_nickname":"庞雨琴",
             * "content":"好厉害的样子，好想学。","add_time":"2017-08-24 10:22:26"}
             * add_time : 2017-08-24 10:22
             * image_list : [{"id":"1","post_id":"4","image":"http://test.cnsunrun
             * .com/wuye/Uploads/Forum/ForumPost/2017-08-24/599e3c90617b3.jpg"}]
             * avatar : http://test.cnsunrun.com/wuye/wuye/Public/Static/img/avatar_M
             * .jpg/time/1504236169
             * is_like : 0
             */

            private String id;
            private String post_id;
            private String member_id;
            private String member_nickname;
            private String content;
            private String likes;
            private String position;
            private PostInfoBean post_info;
            private String add_time;
            private String avatar;
            private String is_like;
            private List<ImageListBean> image_list;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public PostInfoBean getPost_info() {
                return post_info;
            }

            public void setPost_info(PostInfoBean post_info) {
                this.post_info = post_info;
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

            public static class PostInfoBean {
                /**
                 * id : 1
                 * member_id : 8
                 * member_nickname : 庞雨琴
                 * content : 好厉害的样子，好想学。
                 * add_time : 2017-08-24 10:22:26
                 */

                private String id;
                private String member_id;
                private String member_nickname;
                private String content;
                private String add_time;

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

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getAdd_time() {
                    return add_time;
                }

                public void setAdd_time(String add_time) {
                    this.add_time = add_time;
                }
            }

            public static class ImageListBean {
                /**
                 * id : 1
                 * post_id : 4
                 * image : http://test.cnsunrun.com/wuye/Uploads/Forum/ForumPost/2017-08-24/599e3c90617b3.jpg
                 */

                private String id;
                private String post_id;
                private String image;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPost_id() {
                    return post_id;
                }

                public void setPost_id(String post_id) {
                    this.post_id = post_id;
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
}
