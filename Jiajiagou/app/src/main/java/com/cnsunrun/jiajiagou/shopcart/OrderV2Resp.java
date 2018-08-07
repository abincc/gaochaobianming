package com.cnsunrun.jiajiagou.shopcart;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;

/**
 * Created by ${LiuDi}
 * on 2017/9/8on 12:08.
 */

public class OrderV2Resp extends BaseResp
{

    /**
     * status : 1
     * info : {"num_total":"1","subtotal":"1888","freight":"100.00","money_total":"1988.00",
     * "address":{"address_id":"81","province":"1709","city":"1710","area":"1716","address_detail":"您root诺送哦肉某您poor",
     * "mobile":"17362938757","name":"啊啊啊","province_title":"湖北省","city_title":"武汉市","area_title":"青山区"},
     * "product_info":{"sku_id":"10","product_id":"2","price":"1888.00","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png","spec_value":"",
     * "product_title":"饼干大将克力架","product_num":"1"}}
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
         * num_total : 1
         * subtotal : 1888
         * freight : 100.00
         * money_total : 1988.00
         * address : {"address_id":"81","province":"1709","city":"1710","area":"1716",
         * "address_detail":"您root诺送哦肉某您poor","mobile":"17362938757","name":"啊啊啊","province_title":"湖北省",
         * "city_title":"武汉市","area_title":"青山区"}
         * product_info : {"sku_id":"10","product_id":"2","price":"1888.00","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png","spec_value":"",
         * "product_title":"饼干大将克力架","product_num":"1"}
         */

        private String num_total;
        private String subtotal;
        private String freight;
        private String money_total;
        private AddressBean address;
        private ProductInfoBean product_info;
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

        public ProductInfoBean getProduct_info()
        {
            return product_info;
        }

        public void setProduct_info(ProductInfoBean product_info)
        {
            this.product_info = product_info;
        }

        public static class ProductInfoBean
        {
            /**
             * sku_id : 10
             * product_id : 2
             * price : 1888.00
             * image : http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png
             * spec_value :
             * product_title : 饼干大将克力架
             * product_num : 1
             */

            private String sku_id;
            private String product_id;
            private String price;
            private String image;
            private String spec_value;
            private String product_title;
            private String product_num;

            public String getSku_id()
            {
                return sku_id;
            }

            public void setSku_id(String sku_id)
            {
                this.sku_id = sku_id;
            }

            public String getProduct_id()
            {
                return product_id;
            }

            public void setProduct_id(String product_id)
            {
                this.product_id = product_id;
            }

            public String getPrice()
            {
                return price;
            }

            public void setPrice(String price)
            {
                this.price = price;
            }

            public String getImage()
            {
                return image;
            }

            public void setImage(String image)
            {
                this.image = image;
            }

            public String getSpec_value()
            {
                return spec_value;
            }

            public void setSpec_value(String spec_value)
            {
                this.spec_value = spec_value;
            }

            public String getProduct_title()
            {
                return product_title;
            }

            public void setProduct_title(String product_title)
            {
                this.product_title = product_title;
            }

            public String getProduct_num()
            {
                return product_num;
            }

            public void setProduct_num(String product_num)
            {
                this.product_num = product_num;
            }
        }
    }
}
