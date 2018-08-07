package com.cnsunrun.jiajiagou.personal.order;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/7on 10:26.
 */

public class OrderDetailResp extends BaseResp
{
    /**
     * status : 1
     * info : {"order_id":"41","order_no":"2017090610251545","money_total":"308.00","status":"10",
     * "add_time":"2017-09-06 18:22:39","status_title":"未付款","address":{"address_id":"81","province":"1709",
     * "city":"1710","area":"1716","address_detail":"您root诺送哦肉某您poor","mobile":"17362938757","name":"啊啊啊",
     * "province_title":"湖北省","city_title":"武汉市","area_title":"青山区"},"product_info":[{"order_detail_id":"51",
     * "product_id":"7","product_title":"鼠粮","product_price":"200.00","product_num":"1","product_image":"http://test
     * .cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
     * "comment_status":"0"},{"order_detail_id":"52","product_id":"6","product_title":"火雨奶茶",
     * "product_price":"100.00","product_num":"1","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599becf0cba39.jpg","product_spec_value":"白色 XS",
     * "comment_status":"0"}],"log":[{"log_id":"24","status":"10","remark":"客户下单","add_time":"2017-09-06 18:22:39",
     * "status_title":"未付款"}]}
     */

    private InfoBean info;

    public InfoBean getInfo()
    {
        return info;
    }

    public void setInfo(InfoBean info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        /**
         * order_id : 41
         * order_no : 2017090610251545
         * money_total : 308.00
         * status : 10
         * add_time : 2017-09-06 18:22:39
         * status_title : 未付款
         * address : {"address_id":"81","province":"1709","city":"1710","area":"1716",
         * "address_detail":"您root诺送哦肉某您poor","mobile":"17362938757","name":"啊啊啊","province_title":"湖北省",
         * "city_title":"武汉市","area_title":"青山区"}
         * product_info : [{"order_detail_id":"51","product_id":"7","product_title":"鼠粮","product_price":"200.00",
         * "product_num":"1","product_image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","product_spec_value":"红色",
         * "comment_status":"0"},{"order_detail_id":"52","product_id":"6","product_title":"火雨奶茶",
         * "product_price":"100.00","product_num":"1","product_image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599becf0cba39.jpg","product_spec_value":"白色 XS",
         * "comment_status":"0"}]
         * log : [{"log_id":"24","status":"10","remark":"客户下单","add_time":"2017-09-06 18:22:39","status_title":"未付款"}]
         */

        private String order_id;
        private String order_no;
        private String money_total;
        private int status;

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        private String pay_time;

        public String getPay_time()
        {
            return pay_time;
        }

        public void setPay_time(String pay_time)
        {
            this.pay_time = pay_time;
        }

        public String getShip_time()
        {
            return ship_time;
        }

        public void setShip_time(String ship_time)
        {
            this.ship_time = ship_time;
        }

        public String getConfirm_time()
        {
            return confirm_time;
        }

        public void setConfirm_time(String confirm_time)
        {
            this.confirm_time = confirm_time;
        }

        private String ship_time;
        private String confirm_time;

        private String add_time;
        private String status_title;
        private AddressBean address;
        private List<OrderBean> product_info;
        private List<LogBean> log;

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

        public AddressBean getAddress()
        {
            return address;
        }

        public void setAddress(AddressBean address)
        {
            this.address = address;
        }

        public List<OrderBean> getProduct_info()
        {
            return product_info;
        }

        public void setProduct_info(List<OrderBean> product_info)
        {
            this.product_info = product_info;
        }

        public List<LogBean> getLog()
        {
            return log;
        }

        public void setLog(List<LogBean> log)
        {
            this.log = log;
        }


        public static class LogBean
        {
            /**
             * log_id : 24
             * status : 10
             * remark : 客户下单
             * add_time : 2017-09-06 18:22:39
             * status_title : 未付款
             */

            private String log_id;
            @SerializedName("status")
            private String statusX;
            private String remark;
            private String add_time;
            private String status_title;

            public String getLog_id()
            {
                return log_id;
            }

            public void setLog_id(String log_id)
            {
                this.log_id = log_id;
            }

            public String getStatusX()
            {
                return statusX;
            }

            public void setStatusX(String statusX)
            {
                this.statusX = statusX;
            }

            public String getRemark()
            {
                return remark;
            }

            public void setRemark(String remark)
            {
                this.remark = remark;
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
        }
    }
}
