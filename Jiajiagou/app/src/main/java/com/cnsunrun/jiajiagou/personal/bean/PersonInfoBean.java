package com.cnsunrun.jiajiagou.personal.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-07 10:11
 */
public class PersonInfoBean extends BaseResp
{

    /**
     * status : 1
     * msg : ok
     * info : {"headimg":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1504750210","nickname":"yyc","message_forum":"0","message_order":"0",
     * "message_property":"0","message_arrears":"0"}
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
         * headimg : http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1504750210
         * nickname : yyc
         * message_forum : 0
         * message_order : 0
         * message_property : 0
         * message_arrears : 0
         */

        private String headimg;
        private String nickname;
        private String message_forum;
        private String message_order;
        private String message_property;
        private String message_arrears;

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMessage_forum() {
            return message_forum;
        }

        public void setMessage_forum(String message_forum) {
            this.message_forum = message_forum;
        }

        public String getMessage_order() {
            return message_order;
        }

        public void setMessage_order(String message_order) {
            this.message_order = message_order;
        }

        public String getMessage_property() {
            return message_property;
        }

        public void setMessage_property(String message_property) {
            this.message_property = message_property;
        }

        public String getMessage_arrears() {
            return message_arrears;
        }

        public void setMessage_arrears(String message_arrears) {
            this.message_arrears = message_arrears;
        }
    }
}
