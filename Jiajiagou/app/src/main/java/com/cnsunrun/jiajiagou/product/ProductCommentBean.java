package com.cnsunrun.jiajiagou.product;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-13 12:23
 */
public class ProductCommentBean {

    /**
     * status : 1
     * msg : ok
     * info : [{"comment_id":"26","content":"啦啦","star":"5.00","reply":"","add_time":"2017-09-13
     * 09:41:45","nickname":"test","headimg":"http://test.cnsunrun
     * .com/wuye/Uploads/Headimg/000/00/00/H_13_M.jpg?time=1505276403","images":["http://test
     * .cnsunrun.com/wuye/Uploads/Order/Comment/20170913/59b88cd9d5821.jpg","http://test.cnsunrun
     * .com/wuye/Uploads/Order/Comment/20170913/59b88cd9d72b5.jpg","http://test.cnsunrun
     * .com/wuye/Uploads/Order/Comment/20170913/59b88cd9d8b4e.jpg"]},{"comment_id":"22",
     * "content":"可以的","star":"5.00","reply":"","add_time":"2017-09-11 15:56:39",
     * "nickname":"test","headimg":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1505276403","images":["http://test.cnsunrun
     * .com/wuye/Uploads/Order/Comment/20170911/59b641b7b0200.jpg","http://test.cnsunrun
     * .com/wuye/Uploads/Order/Comment/20170911/59b641b7b2e8a.jpg"]}]
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
         * comment_id : 26
         * content : 啦啦
         * star : 5.00
         * reply :
         * add_time : 2017-09-13 09:41:45
         * nickname : test
         * headimg : http://test.cnsunrun.com/wuye/Uploads/Headimg/000/00/00/H_13_M
         * .jpg?time=1505276403
         * images : ["http://test.cnsunrun
         * .com/wuye/Uploads/Order/Comment/20170913/59b88cd9d5821.jpg","http://test.cnsunrun
         * .com/wuye/Uploads/Order/Comment/20170913/59b88cd9d72b5.jpg","http://test.cnsunrun
         * .com/wuye/Uploads/Order/Comment/20170913/59b88cd9d8b4e.jpg"]
         */

        private String comment_id;
        private String content;
        private String star;
        private String reply;
        private String add_time;
        private String nickname;
        private String headimg;
        private List<String> images;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
