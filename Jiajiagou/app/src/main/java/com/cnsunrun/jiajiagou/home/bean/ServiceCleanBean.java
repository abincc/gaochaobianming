package com.cnsunrun.jiajiagou.home.bean;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-15 10:36
 */
public class ServiceCleanBean {

    /**
     * status : 1
     * msg : OK
     * info : [{"title":"室内保洁","clean_service_id":"1"},{"title":"水管维修","clean_service_id":"2"}]
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
         * title : 室内保洁
         * clean_service_id : 1
         */

        private String title;
        private String clean_service_id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClean_service_id() {
            return clean_service_id;
        }

        public void setClean_service_id(String clean_service_id) {
            this.clean_service_id = clean_service_id;
        }
    }
}
