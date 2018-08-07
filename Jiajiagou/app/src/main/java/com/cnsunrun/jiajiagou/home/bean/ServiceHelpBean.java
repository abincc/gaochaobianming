package com.cnsunrun.jiajiagou.home.bean;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-08 10:29
 */
public class ServiceHelpBean {

    /**
     * status : 1
     * msg : OK
     * info : [{"title":"代收快递","trifle_service_id":"1"},{"title":"代打LOL",
     * "trifle_service_id":"2"},{"title":"代交水电费","trifle_service_id":"3"},{"title":"麻将三缺一",
     * "trifle_service_id":"4"}]
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
         * title : 代收快递
         * trifle_service_id : 1
         */

        private String title;
        private String trifle_service_id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTrifle_service_id() {
            return trifle_service_id;
        }

        public void setTrifle_service_id(String trifle_service_id) {
            this.trifle_service_id = trifle_service_id;
        }
    }
}
