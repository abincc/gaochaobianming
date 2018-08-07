package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-01 17:41
 */
public class ForumSearchBean extends BaseResp {


    /**
     * status : 1
     * info : {"forum_list":[{"id":"5","title":"贴图自拍","icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"7","title":"灌者为王",
     * "icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"2",
     * "title":"默认版块","icon":""},{"id":"4","title":"谈天说地","icon":""},{"id":"1","title":"默认分区",
     * "icon":""}],"thread_list":[{"id":"1","forum_id":"7","member_id":"7",
     * "member_nickname":"倪映冬","title":"解锁小米手机旅行拍照的秘籍",
     * "content":"我是一个坐不住的人。\r\n一天不出门走动，坐立难安。\r\n半年不出远门游玩，浑身难受。\r\n\r\n我也是一个懒癌患者。\r\n
     * 之前出去游玩，还能带上相机，背包，行李，甚至拿上吉他。\r\n而现在越来越懒，一个手机一个空荡荡的包，说走就走。\r\n\r\n我更是一个喜欢拍照的人。\r\n一路玩一路拍。\r
     * \n\r\n无论拍的好坏，无论用的什么设备，无论在什么时候。\r\n我都不愿去删。\r\n\r\n因为每一张照片，我每次再看到它的时候，就能想起当时按下快门时的心情。",
     * "views":"977","replies":"46","likes":"7","add_time":"2017-08-24 10:14",
     * "forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"136","forum_id":"5",
     * "member_id":"19","member_nickname":"123","title":"其实我很美丽",
     * "content":"测试测试测试测试测试测试测试测试测试测试测试测试","views":"122","replies":"0","likes":"1",
     * "add_time":"4天前","forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"134","forum_id":"7",
     * "member_id":"27","member_nickname":"裤子阿飞","title":"特我hmmm嗯哦哦","content":"可很OK魔门哦粉蓝色",
     * "views":"98","replies":"0","likes":"1","add_time":"5天前","forum_title":"灌者为王",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},
     * {"id":"10","forum_id":"2","member_id":"18","member_nickname":"皮仔","title":"Hhshhh I love",
     * "content":"Tyujjbvvcvvffcccxsss","views":"56","replies":"1","likes":"1",
     * "add_time":"2017-09-08 10:27","forum_title":"默认版块","forum_icon":"http://test.cnsunrun
     * .com/wuye/Api/Forum/ForumPublic/search"},{"id":"135","forum_id":"5","member_id":"19",
     * "member_nickname":"123","title":"贴图自拍贴图自拍",
     * "content":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试","views":"43","replies":"0",
     * "likes":"0","add_time":"4天前","forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"133","forum_id":"5",
     * "member_id":"27","member_nickname":"裤子阿飞","title":"里know哦哦YY","content":"走咯哦哦哦嗯哦哦我诺老婆",
     * "views":"34","replies":"0","likes":"0","add_time":"5天前","forum_title":"贴图自拍",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},
     * {"id":"118","forum_id":"2","member_id":"27","member_nickname":"裤子阿飞","title":"我擦我摸男模",
     * "content":"林业局弄XXOO哦哦哦我摸分我my","views":"32","replies":"0","likes":"0","add_time":"5天前",
     * "forum_title":"默认版块","forum_icon":"http://test.cnsunrun
     * .com/wuye/Api/Forum/ForumPublic/search"},{"id":"5","forum_id":"7","member_id":"19",
     * "member_nickname":"yyc","title":"发帖功能测试","content":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试",
     * "views":"30","replies":"1","likes":"2","add_time":"2017-09-06 09:50","forum_title":"灌者为王",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},
     * {"id":"131","forum_id":"5","member_id":"8","member_nickname":"庞雨琴","title":"我只是来测试的，而已",
     * "content":"我只是一个测试的人，而已","views":"30","replies":"1","likes":"0","add_time":"5天前",
     * "forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"130","forum_id":"5",
     * "member_id":"8","member_nickname":"庞雨琴","title":"我只是来测试的，而已","content":"我只是一个测试的人，而已",
     * "views":"26","replies":"0","likes":"0","add_time":"5天前","forum_title":"贴图自拍",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},
     * {"id":"132","forum_id":"5","member_id":"8","member_nickname":"庞雨琴","title":"我只是来测试的，而已",
     * "content":"我只是一个测试的人，而已","views":"13","replies":"0","likes":"0","add_time":"5天前",
     * "forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"137","forum_id":"5",
     * "member_id":"5","member_nickname":"陆吓飞","title":"问也Teemo","content":"one得得得问KKK咯么",
     * "views":"12","replies":"0","likes":"0","add_time":"昨天12:38","forum_title":"贴图自拍",
     * "forum_icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},
     * {"id":"7","forum_id":"5","member_id":"22","member_nickname":"napping","title":"七月半-中元节",
     * "content":"内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布","views":"10","replies":"1","likes":"0",
     * "add_time":"2017-09-07 09:20","forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"81","forum_id":"2",
     * "member_id":"13","member_nickname":"海仔","title":"int a = hjvfgjnvdyjbvgh","content":"int a
     * = hjvfgjnvdyjbvgh","views":"9","replies":"0","likes":"0","add_time":"5天前",
     * "forum_title":"默认版块","forum_icon":"http://test.cnsunrun
     * .com/wuye/Api/Forum/ForumPublic/search"},{"id":"2","forum_id":"7","member_id":"22",
     * "member_nickname":"napping","title":"标题测试三件事",
     * "content":"内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布","views":"8","replies":"3","likes":"1",
     * "add_time":"2017-09-04 22:30","forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"46","forum_id":"5",
     * "member_id":"8","member_nickname":"庞雨琴","title":"我是来测试的，请忽略，谢谢",
     * "content":"我是来测试的，请忽略，谢谢。","views":"7","replies":"1","likes":"0","add_time":"6天前",
     * "forum_title":"贴图自拍","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"},{"id":"3","forum_id":"7",
     * "member_id":"22","member_nickname":"napping","title":"标题测试三件事",
     * "content":"内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布","views":"7","replies":"1","likes":"0",
     * "add_time":"2017-09-04 22:34","forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"8","forum_id":"7",
     * "member_id":"22","member_nickname":"napping","title":"Craig Craig",
     * "content":"Jslkdjflsjdfljsldfjlsjdf","views":"6","replies":"0","likes":"0",
     * "add_time":"2017-09-07 22:04","forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"90","forum_id":"2",
     * "member_id":"13","member_nickname":"海仔","title":"int a = hjvfgjnvdyjbvgh","content":"int a
     * = hjvfgjnvdyjbvgh","views":"5","replies":"0","likes":"1","add_time":"5天前",
     * "forum_title":"默认版块","forum_icon":"http://test.cnsunrun
     * .com/wuye/Api/Forum/ForumPublic/search"},{"id":"6","forum_id":"7","member_id":"22",
     * "member_nickname":"napping","title":"七月半-中元节",
     * "content":"内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布内容发布","views":"4","replies":"0","likes":"0",
     * "add_time":"2017-09-06 10:16","forum_title":"灌者为王","forum_icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"}]}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private List<ForumListBean> forum_list;
        private List<ThreadListBean> thread_list;

        public List<ForumListBean> getForum_list() {
            return forum_list;
        }

        public void setForum_list(List<ForumListBean> forum_list) {
            this.forum_list = forum_list;
        }

        public List<ThreadListBean> getThread_list() {
            return thread_list;
        }

        public void setThread_list(List<ThreadListBean> thread_list) {
            this.thread_list = thread_list;
        }

        public static class ForumListBean {
            /**
             * id : 5
             * title : 贴图自拍
             * icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/5995736f83bf9.png
             */

            private String id;
            private String title;
            private String icon;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }

        public static class ThreadListBean {
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
             * views : 977
             * replies : 46
             * likes : 7
             * add_time : 2017-08-24 10:14
             * forum_title : 灌者为王
             * forum_icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png
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
}
