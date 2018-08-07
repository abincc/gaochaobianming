package com.cnsunrun.jiajiagou.personal.bean;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-16 17:28
 */
public class MessageBean {

    /**
     * status : 1
     * msg : OK
     * info : [{"message_id":"42","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"41","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"40","title":"12312啊啊十大","is_read":"0","add_time":"2017-09-16
     * 12:01:04"},{"message_id":"39","title":"阿萨德","is_read":"0","add_time":"2017-09-16
     * 12:00:57"},{"message_id":"38","title":"阿萨啊实打实","is_read":"1","add_time":"2017-09-16
     * 12:00:54"},{"message_id":"37","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"36","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"35","title":"12312啊啊十大","is_read":"0","add_time":"2017-09-16
     * 12:01:04"},{"message_id":"34","title":"阿萨德","is_read":"0","add_time":"2017-09-16
     * 12:00:57"},{"message_id":"33","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-16
     * 12:00:54"},{"message_id":"32","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"31","title":"阿萨啊实打实","is_read":"0","add_time":"2017-09-13
     * 17:57:10"},{"message_id":"30","title":"12312啊啊十大","is_read":"0","add_time":"2017-09-16
     * 12:01:04"},{"message_id":"4","title":"1111111111111","is_read":"1","add_time":"2017-09-07
     * 18:40:51"}]
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
         * message_id : 42
         * title : 阿萨啊实打实
         * is_read : 0
         * add_time : 2017-09-13 17:57:10
         */

        private String message_id;
        private String title;
        private String is_read;
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

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
