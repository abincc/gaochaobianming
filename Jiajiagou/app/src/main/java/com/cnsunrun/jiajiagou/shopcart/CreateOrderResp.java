package com.cnsunrun.jiajiagou.shopcart;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * Created by ${LiuDi}
 * on 2017/9/21on 17:31.
 */

public class CreateOrderResp extends BaseResp
{

    /**
     * status : 1
     * info : {"order_no":"2017092157509753","money":"1988.00","desc":"订单支付描述","order_id":"176"}
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
         * order_no : 2017092157509753
         * money : 1988.00
         * desc : 订单支付描述
         * order_id : 176
         */

        private String order_no;
        private String money;
        private String desc;
        private String order_id;
        // "callback_url_ali": "http://test.cnsunrun.com/wuye/Api/Common/Callback/ali_notify",   //支付宝支付回调地址
//         "callback_url_wx": "http://test.cnsunrun.com/wuye/Api/Common/Callback/wx_notify"   //微信支付回调地址
        private String callback_url_ali;
        public String product_id;
        public String getCallback_url_ali()
        {
            return callback_url_ali;
        }

        public void setCallback_url_ali(String callback_url_ali)
        {
            this.callback_url_ali = callback_url_ali;
        }

        public String getCallback_url_wx()
        {
            return callback_url_wx;
        }

        public void setCallback_url_wx(String callback_url_wx)
        {
            this.callback_url_wx = callback_url_wx;
        }

        private String callback_url_wx;

        public String getOrder_no()
        {
            return order_no;
        }

        public void setOrder_no(String order_no)
        {
            this.order_no = order_no;
        }

        public String getMoney()
        {
            return money;
        }

        public void setMoney(String money)
        {
            this.money = money;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc(String desc)
        {
            this.desc = desc;
        }

        public String getOrder_id()
        {
            return order_id;
        }

        public void setOrder_id(String order_id)
        {
            this.order_id = order_id;
        }
    }
}
