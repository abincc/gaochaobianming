package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-18 16:33
 */
public class TabReplyBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : [{"id":"46","forum_id":"7","thread_id":"1","content":"测试主题评论功能功能Google测试测试测试测试",
     * "add_time":"2017-09-09 13:37","thread_info":{"id":"1","title":"解锁小米手机旅行拍照的秘籍",
     * "forum_id":"7","forum_title":"灌者为王"}},{"id":"45","forum_id":"7","thread_id":"1",
     * "content":"回复功能测试。。。。。。。。。。","add_time":"2017-09-08 15:17","thread_info":{"id":"1",
     * "title":"解锁小米手机旅行拍照的秘籍","forum_id":"7","forum_title":"灌者为王"}},{"id":"44","forum_id":"7",
     * "thread_id":"1","content":"测试回复功能。。。。。。。。。。。。。。","add_time":"2017-09-08 14:20",
     * "thread_info":{"id":"1","title":"解锁小米手机旅行拍照的秘籍","forum_id":"7","forum_title":"灌者为王"}},
     * {"id":"43","forum_id":"7","thread_id":"1","content":"评论主题测试测试测试带图片","add_time":"2017-09-08
     * 14:17","thread_info":{"id":"1","title":"解锁小米手机旅行拍照的秘籍","forum_id":"7",
     * "forum_title":"灌者为王"}},{"id":"42","forum_id":"7","thread_id":"1","content":"评论主题测试测试测试",
     * "add_time":"2017-09-08 14:16","thread_info":{"id":"1","title":"解锁小米手机旅行拍照的秘籍",
     * "forum_id":"7","forum_title":"灌者为王"}}]
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
         * id : 46
         * forum_id : 7
         * thread_id : 1
         * content : 测试主题评论功能功能Google测试测试测试测试
         * add_time : 2017-09-09 13:37
         * thread_info : {"id":"1","title":"解锁小米手机旅行拍照的秘籍","forum_id":"7","forum_title":"灌者为王"}
         */

        private String id;
        private String forum_id;
        private String thread_id;
        private String content;
        private String add_time;
        private ThreadInfoBean thread_info;

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

        public String getThread_id() {
            return thread_id;
        }

        public void setThread_id(String thread_id) {
            this.thread_id = thread_id;
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

        public ThreadInfoBean getThread_info() {
            return thread_info;
        }

        public void setThread_info(ThreadInfoBean thread_info) {
            this.thread_info = thread_info;
        }

        public static class ThreadInfoBean {
            /**
             * id : 1
             * title : 解锁小米手机旅行拍照的秘籍
             * forum_id : 7
             * forum_title : 灌者为王
             */

            private String id;
            private String title;
            private String forum_id;
            private String forum_title;

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

            public String getForum_title() {
                return forum_title;
            }

            public void setForum_title(String forum_title) {
                this.forum_title = forum_title;
            }
        }
    }
}
