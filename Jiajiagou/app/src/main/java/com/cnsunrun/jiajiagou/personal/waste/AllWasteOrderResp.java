package com.cnsunrun.jiajiagou.personal.waste;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.personal.order.OrderBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/5on 15:35.
 */

public class AllWasteOrderResp extends BaseResp
{

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

        private String id;
        private String order_no;
        private String add_time;
        private String status_title;
        private int status;
        private String date;

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        private List<OrderBean> product_info;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getOrder_no()
        {
            return order_no;
        }

        public void setOrder_no(String order_no)
        {
            this.order_no = order_no;
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

        public String getDate(){return date;}

        public void setDate(){this.date = date;}

    }
}
