package com.cnsunrun.jiajiagou.personal.order;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/5on 15:35.
 */

public class AllOrderResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"order_id":"32","order_no":"2017090554499948","money_total":"2008.00","status":"10",
     * "add_time":"2017-09-05 15:25:10","product_info":[{"order_detail_id":"39","order_id":"32","product_id":"7",
     * "product_title":"鼠粮","product_price":"200.00","product_num":"10","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
     * "comment_status":"0"}],"status_title":"未付款"},{"order_id":"31","order_no":"2017090510297489",
     * "money_total":"2008.00","status":"10","add_time":"2017-09-05 15:25:03",
     * "product_info":[{"order_detail_id":"38","order_id":"31","product_id":"7","product_title":"鼠粮",
     * "product_price":"200.00","product_num":"10","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
     * "comment_status":"0"}],"status_title":"未付款"},{"order_id":"30","order_no":"2017090510198985",
     * "money_total":"2508.00","status":"10","add_time":"2017-09-05 15:24:30",
     * "product_info":[{"order_detail_id":"36","order_id":"30","product_id":"7","product_title":"鼠粮",
     * "product_price":"200.00","product_num":"10","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
     * "comment_status":"0"},{"order_detail_id":"37","order_id":"30","product_id":"6","product_title":"火雨奶茶",
     * "product_price":"100.00","product_num":"5","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599becf0cba39.jpg","product_spec_value":"白色 XS",
     * "comment_status":"0"}],"status_title":"未付款"},{"order_id":"29","order_no":"2017090548102489",
     * "money_total":"502.00","status":"10","add_time":"2017-09-05 15:24:16","product_info":[{"order_detail_id":"35",
     * "order_id":"29","product_id":"6","product_title":"火雨奶茶","product_price":"100.00","product_num":"5",
     * "product_image":"http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599becf0cba39.jpg",
     * "product_spec_value":"白色 XS","comment_status":"0"}],"status_title":"未付款"}]
     */

    private List<InfoBean> info;

    public List<InfoBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<InfoBean> info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        /**
         * order_id : 32
         * order_no : 2017090554499948
         * money_total : 2008.00
         * status : 10
         * add_time : 2017-09-05 15:25:10
         * product_info : [{"order_detail_id":"39","order_id":"32","product_id":"7","product_title":"鼠粮",
         * "product_price":"200.00","product_num":"10","product_image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
         * "comment_status":"0"}]
         * status_title : 未付款
         */

        private String order_id;
        private String order_no;
        private String money_total;
        private String add_time;
        private String status_title;
        private int status;

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        private List<OrderBean> product_info;

        public String getOrder_id()
        {
            return order_id;
        }

        public void setOrder_id(String order_id)
        {
            this.order_id = order_id;
        }

        public String getOrder_no()
        {
            return order_no;
        }

        public void setOrder_no(String order_no)
        {
            this.order_no = order_no;
        }

        public String getMoney_total()
        {
            return money_total;
        }

        public void setMoney_total(String money_total)
        {
            this.money_total = money_total;
        }

        public String getAdd_time()
        {
            return add_time;
        }

        public void setAdd_time(String add_time)
        {
            this.add_time = add_time;
        }

        public String getStatus_title()
        {
            return status_title;
        }

        public void setStatus_title(String status_title)
        {
            this.status_title = status_title;
        }

        public List<OrderBean> getProduct_info()
        {
            return product_info;
        }

        public void setProduct_info(List<OrderBean> product_info)
        {
            this.product_info = product_info;
        }


    }
}
