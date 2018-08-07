package com.cnsunrun.jiajiagou.shopcart;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.personal.order.OrderBean;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/4on 16:37.
 */

public class OrderResp extends BaseResp
{

    /**
     * status : 1
     * info : {"num_total":"3","subtotal":"600.000","freight":"8","money_total":"608.00",
     * "address":{"address_id":"71","province":"2827","city":"2847","area":"2849","address_detail":"你屋里门口",
     * "mobile":"13555545745","name":"你爸爸","province_title":"陕西省","city_title":"宝鸡市","area_title":"金台区"},
     * "cart_list":[{"cart_id":"55","sku_id":"11","product_id":"7","product_title":"鼠粮","product_price":"200.00",
     * "product_spec_value":"白色","product_num":"2","product_image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg"},{"cart_id":"56","sku_id":"12",
     * "product_id":"7","product_title":"鼠粮","product_price":"200.00","product_spec_value":"红色","product_num":"1",
     * "product_image":"http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg"}]}
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
         * num_total : 3
         * subtotal : 600.000
         * freight : 8
         * money_total : 608.00
         * address : {"address_id":"71","province":"2827","city":"2847","area":"2849","address_detail":"你屋里门口",
         * "mobile":"13555545745","name":"你爸爸","province_title":"陕西省","city_title":"宝鸡市","area_title":"金台区"}
         * cart_list : [{"cart_id":"55","sku_id":"11","product_id":"7","product_title":"鼠粮","product_price":"200.00",
         * "product_spec_value":"白色","product_num":"2","product_image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg"},{"cart_id":"56","sku_id":"12",
         * "product_id":"7","product_title":"鼠粮","product_price":"200.00","product_spec_value":"红色",
         * "product_num":"1","product_image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg"}]
         */

        private String num_total;
        private String subtotal;
        private String freight;
        private String money_total;
        private AddressBean address;
        private List<OrderBean> cart_list;
        public int card_num;
        public String getNum_total()
        {
            return num_total;
        }

        public void setNum_total(String num_total)
        {
            this.num_total = num_total;
        }

        public String getSubtotal()
        {
            return subtotal;
        }

        public void setSubtotal(String subtotal)
        {
            this.subtotal = subtotal;
        }

        public String getFreight()
        {
            return freight;
        }

        public void setFreight(String freight)
        {
            this.freight = freight;
        }

        public String getMoney_total()
        {
            return money_total;
        }

        public void setMoney_total(String money_total)
        {
            this.money_total = money_total;
        }

        public AddressBean getAddress()
        {
            return address;
        }

        public void setAddress(AddressBean address)
        {
            this.address = address;
        }

        public List<OrderBean> getCart_list()
        {
            return cart_list;
        }

        public void setCart_list(List<OrderBean> cart_list)
        {
            this.cart_list = cart_list;
        }

    }
}
