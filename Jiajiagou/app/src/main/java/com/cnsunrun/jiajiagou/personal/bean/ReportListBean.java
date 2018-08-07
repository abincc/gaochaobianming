package com.cnsunrun.jiajiagou.personal.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 16:23
 */
public class ReportListBean extends BaseResp {

    /**
     * status : 1
     * info : [{"report_id":"18","title":"2017-11-24 (第四季度)"}]
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
         * report_id : 18
         * title : 2017-11-24 (第四季度)
         */

        private String report_id;
        private String title;

        public String getReport_id() {
            return report_id;
        }

        public void setReport_id(String report_id) {
            this.report_id = report_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
