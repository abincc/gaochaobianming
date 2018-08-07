package com.cnsunrun.jiajiagou.personal.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-23 19:18
 */
public class AddressBookBean extends BaseResp{


    /**
     * status : 1
     * msg : ok
     * info : [{"tbook_id":"4","title":"测试11","number":"86-12334345345","address":"武汉市江岸区"},
     * {"tbook_id":"5","title":"回家啊","number":"027-85032123","address":""},{"tbook_id":"3",
     * "title":"asdasD","number":"123123","address":""},{"tbook_id":"2","title":"B公司电话",
     * "number":"021-7777777","address":""},{"tbook_id":"1","title":"A公司电话",
     * "number":"027-888888","address":""}]
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
         * tbook_id : 4
         * title : 测试11
         * number : 86-12334345345
         * address : 武汉市江岸区
         */

        private String tbook_id;
        private String title;
        private String number;
        private String address;

        public String getTbook_id() {
            return tbook_id;
        }

        public void setTbook_id(String tbook_id) {
            this.tbook_id = tbook_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
