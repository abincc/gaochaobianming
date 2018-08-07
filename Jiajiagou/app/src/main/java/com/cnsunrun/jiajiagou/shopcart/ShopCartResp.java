package com.cnsunrun.jiajiagou.shopcart;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/30on 14:52.
 */

public class ShopCartResp extends BaseResp
{
    /**
     * status : 1
     * info : [{"cart_id":"4","sku_id":"4","product_id":"4","product_price":"17.00","product_spec_value":"白色 S",
     * "product_num":"2","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beb6ef0bb4.jpg"},{"cart_id":"3","sku_id":"9",
     * "product_id":"3","product_price":"6000.00","product_spec_value":"黑色","product_num":"1",
     * "product_image":"http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beace80a0a.png"},
     * {"cart_id":"2","sku_id":"8","product_id":"3","product_price":"10086.00","product_spec_value":"黄色",
     * "product_num":"4","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beace80a0a.png"}]
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
         * cart_id : 4
         * sku_id : 4
         * product_id : 4
         * product_price : 17.00
         * product_spec_value : 白色 S
         * product_num : 2
         * product_image : http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beb6ef0bb4.jpg
         */

        public String cart_id;
        public String sku_id;
        public String product_id;
        public String product_price;
        public String product_spec_value;
        public String product_num;
        public String product_image;
        public String product_title;
        public boolean isChecked;
    }
}
