package com.cnsunrun.jiajiagou.personal.bean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-16 17:42
 */
public class MessageDetailBean {

    /**
     * status : 1
     * msg : OK
     * info : {"message_id":"42","title":"阿萨啊实打实","content":"奥术大师多","add_time":"2017-09-13
     * 17:57:10"}
     */

    private int status;
    private String msg;
    private InfoBean info;

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

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * message_id : 42
         * title : 阿萨啊实打实
         * content : 奥术大师多
         * add_time : 2017-09-13 17:57:10
         */

        private String message_id;
        private String title;
        private String content;
        private String add_time;

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
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

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
